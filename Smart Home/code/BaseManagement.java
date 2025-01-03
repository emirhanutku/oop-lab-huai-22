import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public  class BaseManagement {
     static Repository repository=new Repository();

    /**
     *This method takes a LocalDateTime object and converts it to a string representation of the date and time
     *in the format of "yyyy-MM-dd_HH:mm:ss".
     * @param time a LocalDateTime object representing the date and time to be converted
     * @return representation of the date and time in the format of "yyyy-MM-dd_HH:mm:ss"
     */
     public static String  convertTimeTrueFormat(LocalDateTime time){
         Date date = Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
         DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
         String dateStr = formatter.format(date);
         return dateStr;
     }

    /**
     * Sets the initial time for the program.
     */

    public void SetInitialTime(String[]commands) throws ExceptionHandling {
          if (repository.initialTime==null && commands.length!=2){
            Output.writeFile("ERROR: First command must be set initial time! Program is going to terminate!");
            System.exit(0);
        }
          ExceptionHandling.controlCommandTruth(2,2,commands.length);
        ExceptionHandling.checkSetInitialTime(commands,repository.initialTime,ExceptionHandling.trueFormat(commands[1],DateTimeFormatter.ofPattern("y-M-d_H:m:s") ));
        LocalDateTime initialTime=stringToLocalTime(commands[1]);
        repository.initialTime=initialTime;
        Output.writeFile("SUCCESS: Time has been set to "+convertTimeTrueFormat(initialTime)+"!");
    }

    /**
     * Sets the time for the program to a new value.
     * @throws ExceptionHandling if the command is invalid or the new time is before the initial time
     */
    public void SetTime(String[]commands) throws ClassNotFoundException, ExceptionHandling {
        ExceptionHandling.controlCommandTruth(2,2,commands.length);
        LocalDateTime newTime=stringToLocalTime(commands[1]);

        ExceptionHandling.checkSetTime(newTime,repository.initialTime);
        ExceptionHandling.controlSetTimeTruth(newTime,repository.initialTime);
        LocalDateTime oldTime=repository.initialTime;
        repository.initialTime=newTime;
        changeStatus();

    }

    /**
     *Skips the specified number of minutes from the current time.
     * @throws ExceptionHandling  if the command is invalid
     */
    public void SkipMinutes(String[]commands) throws ClassNotFoundException, ExceptionHandling {
        ExceptionHandling.controlCommandTruth(2,2,commands.length);
        LocalDateTime oldTime=repository.initialTime;
        ExceptionHandling.checkMinutes(commands[1]);
        LocalDateTime newTime=repository.initialTime.plusMinutes(Integer.parseInt(commands[1]));
        repository.initialTime=newTime;
        changeStatus();
    }

    /**
     *The ArrayList that holds switch times is sorted, and the closest switch time is set to the current time
     * @throws ExceptionHandling To catch some errors
     */
    public void Nop(String[] commands) throws ClassNotFoundException, ExceptionHandling {
        /*Collections.sort(repository.switchTimeArray);
        LocalDateTime newTime=repository.switchTimeArray.get(0);
        LocalDateTime oldTime=repository.initialTime;
        repository.initialTime=newTime;
        repository.switchTimeArray.remove(0);*/
        ExceptionHandling.controlCommandTruth(1,1,commands.length);

        ArrayList<LocalDateTime> switchTimeArray = new ArrayList<>(repository.switchTimesMap.values());
        ExceptionHandling.checkNop(switchTimeArray.size());
        Collections.sort(switchTimeArray);
        repository.initialTime=switchTimeArray.get(0);
        changeStatus();
    }

    /**
     * Removes a smart device from the system and its associated data from the repository.
     * @throws ExceptionHandling if the command is invalid or the specified smart device is not present in the system
     * @throws ClassNotFoundException
     */
    public void Remove(String[]commands) throws ExceptionHandling, ClassNotFoundException {
        ExceptionHandling.controlCommandTruth(2,2,commands.length);
        ExceptionHandling.controlPresence(repository.addedDevices,commands[1]);
        SmartDevices closedItem=repository.addedDevices.get(commands[1]);
        String className=closedItem.getClass().getSimpleName();
        repository.switchTimesMap.remove(commands[1]);
        Output.writeFile("SUCCESS: Information about removed smart device is as follows:");
        switch (className){
            case "SmartLampColor":
                SmartLampColorManagement smartLampColorManagement=new SmartLampColorManagement();
                smartLampColorManagement.getInfo(closedItem);
                repository.addedDevices.remove(commands[1]);
                removeZReport(closedItem);
                break;
            case "SmartPlug":
                SmartPlugManagement smartPlugManagement=new SmartPlugManagement();
                closedItem.initialStatus="off";
                timeManagement1(closedItem.name);
                smartPlugManagement.getInfo(closedItem);
                repository.addedDevices.remove(commands[1]);
                removeZReport(closedItem);
                break;
            case "SmartLamp":
                SmartLampManagement smartLampManagement=new SmartLampManagement();
                smartLampManagement.getInfo(closedItem);
                repository.addedDevices.remove(commands[1]);
                removeZReport(closedItem);
                break;
            case "SmartCamera":
                SmartCameraManagement smartCameraManagement=new SmartCameraManagement();
                closedItem.initialStatus="off";
                timeManagement1(closedItem.name);
                smartCameraManagement.getInfo(closedItem);
                repository.addedDevices.remove(commands[1]);
                removeZReport(closedItem);
                break;
        }


    }

    /**
     * Sets the switch time for a specific smart device.
     */
    public void SetSwitchTime(String[]commands) throws ExceptionHandling, ClassNotFoundException {
        ExceptionHandling.controlCommandTruth(3,3,commands.length);
        ExceptionHandling.controlPresence(repository.addedDevices,commands[1]);
        LocalDateTime switchTime=stringToLocalTime(commands[2]);
        ExceptionHandling.controlSwitchTimeTruth(switchTime,repository.initialTime);
        repository.addedDevices.get(commands[1]).switchTime=switchTime;
        //repository.switchTimeArray.add(switchTime);
        repository.switchTimesMap.put(commands[1],stringToLocalTime(commands[2]));
        if (repository.initialTime.equals(switchTime)){
            repository.smartDevicesNullList.remove(repository.addedDevices.get(commands[1]));
        }
        changeStatus();

    }

    /**
     * Method to switch the status of a smart device to on/off.
     * @param commands
     */
    public void Switch(String[]commands) throws ClassNotFoundException, ExceptionHandling {
        ExceptionHandling.controlCommandTruth(3,3,commands.length);
        ExceptionHandling.controlPresence(repository.addedDevices,commands[1]);
        ExceptionHandling.controlSwitch(repository.addedDevices.get(commands[1]).initialStatus,commands[2].toLowerCase());
        repository.addedDevices.get(commands[1]).initialStatus=commands[2].toLowerCase();
        timeManagement1(commands[1]);

        if (repository.addedDevices.get(commands[1]).switchTime!=null){
            repository.addedDevices.get(commands[1]).switchTime=null;
            repository.switchTimesMap.remove(commands[1]);
            repository.smartDevicesNotNullList.remove(repository.addedDevices.get(commands[1]));
            repository.smartDevicesNullList.add(0,repository.addedDevices.get(commands[1]));

        }

    }

    /**
     *Changes the name of a given smart device with the new given name.
     *Throws an exception if the command is erroneous or if the given device name is not present in the added devices map.
     */
    public void ChangeName(String[]commands) throws ExceptionHandling {
        /*if (commands.length!=3){
            throw new ExceptionHandling("ERROR: Erroneous command!");
        }*/
        ExceptionHandling.controlCommandTruth(3,3,commands.length);
        ExceptionHandling.controlChangeName(commands[1],commands[2],repository.addedDevices,commands.length);
        ExceptionHandling.controlPresence(repository.addedDevices,commands[1]);
        repository.addedDevices=changeKey(repository.addedDevices,commands[1],commands[2]);
        repository.addedDevices.get(commands[2]).name=commands[2];
    }

    /**
     *Converts a string representing a time in the format "y-M-d_H:m:s" to a LocalDateTime object.
     * @param time time a string representing a time in the format "y-M-d_H:m:s"
     * @return a LocalDateTime object representing the input time.
     */
    private LocalDateTime  stringToLocalTime(String time) throws ExceptionHandling {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("y-M-d_H:m:s");
        ExceptionHandling.checkTimeFormat(time,formatter,repository.initialTime);
        LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
        return dateTime;
    }

    /**
     *This method plugs in a smart plug and sets its ampere value. It throws an exception if the device is not a smart plug,
     * if the plug is already plugged in or the given ampere value is not valid.
     * It also calls the timeManagement1 method to update the device's last usage time.
     * @throws ExceptionHandling if the command is not valid, the device is not found, the device is not a smart plug,
     * the plug is already plugged in or the given ampere value is not valid.
     */
    public void PlugIn(String[]commands) throws ClassNotFoundException, ExceptionHandling {
        ExceptionHandling.controlCommandTruth(3,3,commands.length);
        ExceptionHandling.controlPresence(repository.addedDevices,commands[1]);
        SmartDevices pluggedInDevice=repository.addedDevices.get(commands[1]);
        if (!pluggedInDevice.getClass().getSimpleName().equals("SmartPlug")){
            throw new ExceptionHandling("ERROR: This device is not a smart plug!");
        }
        ExceptionHandling.controlPlug(Boolean.parseBoolean(pluggedInDevice.getProperty("isPluggedIn").toString()),commands[0]);
        ExceptionHandling.controlDouble(commands[2],pluggedInDevice.getClass().getSimpleName());


        Class<?> cls=Class.forName("SmartPlug");
        if (classControl(pluggedInDevice,cls)){
            pluggedInDevice.setProperty("isPluggedIn",true);
            pluggedInDevice.setProperty("ampere",Double.parseDouble(commands[2]));
            timeManagement1(pluggedInDevice.name);
        }
    }

    /**
     *This method is used to unplug a smart plug device from the power outlet.
     * The method sets the value of "isPluggedIn" property to false and calls the timeManagement1
     * method to update the device's status in the repository.
     * @throws ExceptionHandling if the command or device name are invalid or if the device is not a smart plug or if the device is already unplugged
     */
    public void PlugOut(String[] commands) throws ClassNotFoundException, ExceptionHandling {
        ExceptionHandling.controlCommandTruth(2,2,commands.length);
        ExceptionHandling.controlPresence(repository.addedDevices,commands[1]);
        SmartDevices pluggedOutDevice=repository.addedDevices.get(commands[1]);
        if (!pluggedOutDevice.getClass().getSimpleName().equals("SmartPlug")){
            throw new ExceptionHandling("ERROR: This device is not a smart plug!");
        }
        ExceptionHandling.controlPlug(Boolean.parseBoolean(pluggedOutDevice.getProperty("isPluggedIn").toString()),commands[0]);
        Class<?> cls=Class.forName("SmartPlug");
        if (classControl(pluggedOutDevice,cls)){
            pluggedOutDevice.setProperty("isPluggedIn",false);
            timeManagement1(pluggedOutDevice.name);
        }
    }

    /**
     * Changes the key of an element in the given LinkedHashMap and returns a new LinkedHashMap with the updated keys
     * @param oldMap the original LinkedHashMap with the old key
     * @param oldKey the old key to be replaced
     * @param newKey the new key to replace the old key
     * @return a new LinkedHashMap with the same elements as the old LinkedHashMap, but with the updated key
     */
    public LinkedHashMap<String, SmartDevices> changeKey(LinkedHashMap<String, SmartDevices> oldMap,String oldKey,String newKey){
        LinkedHashMap<String, SmartDevices> newMap=new LinkedHashMap<>();
        for (String key: oldMap.keySet()){
            if (key.equals(oldKey)){
                newMap.put(newKey,repository.addedDevices.get(key));
            }
            else {
                newMap.put(key,repository.addedDevices.get(key));
            }
        }
        return newMap;
    }

    /**
     * @param commands Sets the kelvin value or color code of a smart lamp or smart color lamp.
     */

    public void SetKelvin(String[] commands) throws ClassNotFoundException, ExceptionHandling {
        if (commands[0].equals("SetKelvin")){
            ExceptionHandling.controlCommandTruth(3,3,commands.length);
        }

        ExceptionHandling.controlPresence(repository.addedDevices,commands[1]);
        if (!repository.addedDevices.get(commands[1]).getClass().getSimpleName().equals("SmartLamp")&&!repository.addedDevices.get(commands[1]).getClass().getSimpleName().equals("SmartLampColor")){
            throw new ExceptionHandling("ERROR: This device is not a smart lamp!");
        }
        ExceptionHandling.controlKelvin(commands[2]);
        SmartDevices desiredDevice=repository.addedDevices.get(commands[1]);
        Class<?> lampClass=Class.forName("SmartLamp");
        Class<?> colorLampClass=Class.forName("SmartLampColor");
        if (classControl(desiredDevice,lampClass)){
            desiredDevice.setProperty("kelvin",Integer.parseInt(commands[2]));
        } else if (classControl(desiredDevice,colorLampClass)) {
            desiredDevice.setProperty("colorCode",commands[2]);
        }
    }

    /**
     * Sets the brightness value or color code of a smart lamp or smart color lamp.
     */

    public void SetBrightness(String[] commands) throws ClassNotFoundException, ExceptionHandling {
        ExceptionHandling.controlCommandTruth(3,3,commands.length);
        ExceptionHandling.controlPresence(repository.addedDevices,commands[1]);
        if (!repository.addedDevices.get(commands[1]).getClass().getSimpleName().equals("SmartLamp")&&!repository.addedDevices.get(commands[1]).getClass().getSimpleName().equals("SmartLampColor")){
            throw new ExceptionHandling("ERROR: This device is not a smart lamp!");
        }
        ExceptionHandling.controlBrightness(commands[2]);
        SmartDevices desiredDevice=repository.addedDevices.get(commands[1]);
        Class<?> lampClass=Class.forName("SmartLamp");
        Class<?> colorLampClass=Class.forName("SmartLampColor");
        if (classControl(desiredDevice,lampClass)||classControl(desiredDevice,colorLampClass)){
            desiredDevice.setProperty("brightness",Integer.parseInt(commands[2]));
        }
    }

    /**
     * Sets the color code property of a SmartLampColor device.
     */
    public void SetColorCode(String[] commands) throws ClassNotFoundException, ExceptionHandling {
        if (commands[0].equals("SetColorCode")){
            ExceptionHandling.controlCommandTruth(3,3,commands.length);
        }
        ExceptionHandling.controlPresence(repository.addedDevices,commands[1]);
        if (!repository.addedDevices.get(commands[1]).getClass().getSimpleName().equals("SmartLampColor")){
            throw new ExceptionHandling("ERROR: This device is not a smart color lamp!");
        }
        ExceptionHandling.controlColorCode(commands[2]);


        SmartDevices desiredDevice=repository.addedDevices.get(commands[1]);
        Class<?> colorLampClass=Class.forName("SmartLampColor");
        if (classControl(desiredDevice,colorLampClass)){
            desiredDevice.setProperty("colorCode",commands[2]);
        }
    }

    /**
     * Sets a white color for the specified smart lamp device, with the given brightness and kelvin values.
     */
    public void SetWhite(String[] commands) throws ClassNotFoundException, ExceptionHandling {
        ExceptionHandling.controlCommandTruth(4,4,commands.length);
        ExceptionHandling.controlPresence(repository.addedDevices,commands[1]);
        SetKelvin(commands);
        ExceptionHandling.controlBrightness(commands[3]);


        SmartDevices desiredDevice=repository.addedDevices.get(commands[1]);
        Class<?> lampClass=Class.forName("SmartLamp");
        Class<?> colorLampClass=Class.forName("SmartLampColor");
        if (classControl(desiredDevice,lampClass)||classControl(desiredDevice,colorLampClass)){
            desiredDevice.setProperty("brightness",Integer.parseInt(commands[3]));
        }
    }

    /**
     *Sets the color of the specified smart color lamp device with the given commands.
     */
    public void SetColor(String[]commands) throws ClassNotFoundException, ExceptionHandling {
        ExceptionHandling.controlCommandTruth(4,4,commands.length);
        ExceptionHandling.controlPresence(repository.addedDevices,commands[1]);
        ExceptionHandling.controlBrightness(commands[3]);
        SetColorCode(commands);



        SmartDevices desiredDevice=repository.addedDevices.get(commands[1]);
        Class<?> colorLampClass=Class.forName("SmartLampColor");
        if (classControl(desiredDevice,colorLampClass)){
            desiredDevice.setProperty("brightness",Integer.parseInt(commands[3]));
        }
    }

    /**
     *Changes the initial status of the device that has passed its switch time and removes
     it from the list of devices with switch times. Adds devices without switch times to the list of devices, in order.
     * @throws ClassNotFoundException if the class specified in the code block cannot be found
     * @see "timeManagement2"
     */
    public void changeStatus() throws ClassNotFoundException {
        ArrayList<String> deletedDevices=new ArrayList<>();
        ArrayList<SmartDevices> deletedDevicesClass=new ArrayList<>();
        Comparator<Map.Entry<String, LocalDateTime>> valueComparator = Comparator.comparing(Map.Entry::getValue);
        LinkedHashMap<String, LocalDateTime> sortedMap = repository.switchTimesMap.entrySet()
                .stream()
                .sorted(valueComparator)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        for (String i:sortedMap.keySet()){
            if (repository.initialTime.isAfter(repository.switchTimesMap.get(i))||repository.initialTime.equals(repository.switchTimesMap.get(i))){
                String initialStatus=(repository.addedDevices.get(i).initialStatus.equals("off")) ? "on":"off";
                repository.addedDevices.get(i).initialStatus=initialStatus;
                timeManagement2(i,repository.switchTimesMap.get(i));
                deletedDevices.add(i);
                repository.smartDevicesNotNullList.remove(repository.addedDevices.get(i));
                deletedDevicesClass.add(repository.addedDevices.get(i));
                repository.addedDevices.get(i).switchTime=null;

            }
        }

        for (String deletedDevice:deletedDevices){
            repository.switchTimesMap.remove(deletedDevice);
        }





        for (SmartDevices smartDevices:deletedDevicesClass){
            repository.smartDevicesNullList.add(0,smartDevices);
        }
    }

    /**
     * Determines whether the device is a SmartPlug or a SmartCamera. If the necessary conditions are met,
     sets the switch on/off time for the device accordingly. If turning off the device, calls another method within
      the code block to calculate the power consumption of the corresponding device
     * @see "SmartPlugManagement.calculateConsumedValue(device)"
     * @see "SmartCameraManagement.calculateConsumedValue(device)"
     * @param device The name of the relevant device.
     * @throws ClassNotFoundException if the class specified in the code block cannot be found
     */

    public static void timeManagement1(String device) throws ClassNotFoundException {   //switch ve plugIn için geçerli çünkü değiştirilme zamanı tam o zaman oluyor
        if (classControl(repository.addedDevices.get(device),Class.forName("SmartPlug"))){
            if (repository.addedDevices.get(device).initialStatus.equals("on") && repository.addedDevices.get(device).getProperty("isPluggedIn").equals(true)){
                repository.openTime.put(device,repository.initialTime);
            } else if ((repository.addedDevices.get(device).initialStatus.equals("on") && repository.addedDevices.get(device).getProperty("isPluggedIn").equals(false))
                    || repository.addedDevices.get(device).initialStatus.equals("off")&&repository.addedDevices.get(device).getProperty("isPluggedIn").equals(true)) {
                if (repository.openTime.containsKey(device)){
                    repository.closeTime.put(device,repository.initialTime);
                    SmartPlugManagement.calculateConsumedValue(device);
                }
            }
        } else if (classControl(repository.addedDevices.get(device),Class.forName("SmartCamera"))) {
            if (repository.addedDevices.get(device).initialStatus.equals("on")){
                repository.openTime.put(device,repository.initialTime);
            } else if (repository.addedDevices.get(device).initialStatus.equals("off")) {
                if (repository.openTime.containsKey(device)){
                    repository.closeTime.put(device,repository.initialTime);
                    SmartCameraManagement.calculateConsumedValue(device);
                }
            }
        }
    }

    /**
     * In the timeManagement1 method, we check the same conditions as before, but when adding the switch on/off times,
      we do not use the current time.
     * @param device The name of the relevant device.
     * @param switchTime The device's switch on/off time.
     * @throws ClassNotFoundException if the class specified in the code block cannot be found
     */
    public void timeManagement2(String device,LocalDateTime switchTime) throws ClassNotFoundException { //skip,setTime,nop için
        if (classControl(repository.addedDevices.get(device),Class.forName("SmartPlug"))){
            if (repository.addedDevices.get(device).initialStatus.equals("on") && repository.addedDevices.get(device).getProperty("isPluggedIn").equals(true)){
                repository.openTime.put(device,switchTime);
            }
            else if ((repository.addedDevices.get(device).initialStatus.equals("on") && repository.addedDevices.get(device).getProperty("isPluggedIn").equals(false))
                    || repository.addedDevices.get(device).initialStatus.equals("off")&&repository.addedDevices.get(device).getProperty("isPluggedIn").equals(true)){
                if (repository.openTime.containsKey(device)){
                    repository.closeTime.put(device, switchTime);
                    SmartPlugManagement.calculateConsumedValue(device);
                }
            }
        }
        else if (classControl(repository.addedDevices.get(device),Class.forName("SmartCamera"))){
            if (repository.addedDevices.get(device).initialStatus.equals("on")){
                repository.openTime.put(device,switchTime);
            }
            else if (repository.addedDevices.get(device).initialStatus.equals("off")&&repository.openTime.containsKey(device)){
                repository.closeTime.put(device,switchTime);
                SmartCameraManagement.calculateConsumedValue(device);
            }
        }
    }

    /**
     * The sortEvent method is called to perform the sorting operation before printing the device information.
     The writeInfo method is called to print the names of the devices.
     * @see  "writeInfo"
     * @see "sortEvent"
     * @param commands
     */

    public void ZReport(String[] commands) throws ExceptionHandling {
        /*Comparator<SmartDevices> comparator = Comparator
                .comparing((SmartDevices s) -> s.getSwitchTime() != null ? 0 : 1)
                .thenComparing(SmartDevices::getSwitchTime, Comparator.nullsLast(Comparator.naturalOrder()));
        LinkedHashMap<String, SmartDevices> sortedSmartDevices = repository.addedDevices.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(comparator))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));*/
        ExceptionHandling.controlCommandTruth(1,1,commands.length);
        sortEvent();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        String formattedDateTime = repository.initialTime.format(formatter);
        Output.writeFile("Time is:\t"+formattedDateTime);
        for (SmartDevices smartDevicesS: repository.smartDevicesNotNullList){
            writeInfo(smartDevicesS);
        }
        for (SmartDevices smartDevices: repository.smartDevicesNullList){
            writeInfo(smartDevices);
        }






    }

    /**
     * If the switch time is not null and is not already in the list, it is removed from the list of devices without
      switch time and added to the list of devices with switch time. If the switch time is null, it is added to the
      list of devices without switch time, and after each addition, the list of devices with switch time is sorted.
     */

    public void sortEvent() {
        for (String key : repository.addedDevices.keySet()) {
            SmartDevices smartDevice = repository.addedDevices.get(key);
            if (smartDevice.getSwitchTime() != null&& !repository.smartDevicesNotNullList.contains(smartDevice)) {
                repository.smartDevicesNullList.remove(smartDevice);
                repository.smartDevicesNotNullList.add(smartDevice);
            }
            else if (smartDevice.getSwitchTime() == null&& !repository.smartDevicesNullList.contains(smartDevice)){
                repository.smartDevicesNullList.add(smartDevice);
            }
        }
        Collections.sort(repository.smartDevicesNotNullList);


    }

    /**
     * The class of the relevant device is determined and the method that writes the information of this device to the system is called
     * @param desiredDevice The device whose information will be written
     */

    public void writeInfo(SmartDevices desiredDevice){
        String className=desiredDevice.getClass().getSimpleName();
        switch (className){
            case "SmartLampColor":
                SmartLampColorManagement smartLampColorManagement=new SmartLampColorManagement();
                smartLampColorManagement.getInfo(desiredDevice);
                break;
            case "SmartPlug":
                SmartPlugManagement smartPlugManagement=new SmartPlugManagement();
                smartPlugManagement.getInfo(desiredDevice);
                break;
            case "SmartLamp":
                SmartLampManagement smartLampManagement=new SmartLampManagement();
                smartLampManagement.getInfo(desiredDevice);
                break;
            case "SmartCamera":
                SmartCameraManagement smartCameraManagement=new SmartCameraManagement();
                smartCameraManagement.getInfo(desiredDevice);
                break;
        }

    }

    /**
     * The removed device is deleted from the necessary lists to prevent it from being printed in the Z Report.
     * @param closedItem The removed device.
     */

    public void removeZReport(SmartDevices closedItem){
        if (closedItem.switchTime!=null){
            repository.smartDevicesNotNullList.remove(closedItem);
        }
        else {
            repository.smartDevicesNullList.remove(closedItem);
        }

    }


    /**
     Determines if a given SmartDevice object is an instance of a specified class.
     @param device the SmartDevice object to check
     @param className the class to check if the device object is an instance of
     @return true if the device object is an instance of the specified class, false otherwise
     */
    public static Boolean classControl(SmartDevices device,Class<?> className){
        return className.isInstance(device);
    }
}
