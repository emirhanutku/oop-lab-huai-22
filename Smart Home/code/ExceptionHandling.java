import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public  class ExceptionHandling extends Exception {

    public ExceptionHandling(String message) {
        super(message);
    }

    /**
     * If no time has been assigned yet, it checks whether the first incoming command is SetInitialTime.
     * @param time Inıtıal time
     * @param commands incoming command
     * @throws ExceptionHandling
     */
    public static void checkFirstSetInitialTime(LocalDateTime time,String[] commands) throws ExceptionHandling {
        if (time==null && !commands[0].equals("SetInitialTime")) {
            Output.writeFile("ERROR: First command must be set initial time! Program is going to terminate!");
            System.exit(0);
        }
    }

    /**
     * If the current time is already assigned and SetInıtıalTime is entered, an error is given.
     * An error is given if the time is not assigned and the entered time is not in the correct format.
     * @param commands
     * @param initialTime
     * @param status Whether the time format is correct or not.
     * @throws ExceptionHandling
     */
    public static void checkSetInitialTime(String[]commands,LocalDateTime initialTime,Boolean status) throws ExceptionHandling {
        if (initialTime!=null){
            throw  new ExceptionHandling("ERROR: Erroneous command!");
        }
        else if (initialTime==null &!status){
            Output.writeFile("ERROR: Format of the initial date is wrong! Program is going to terminate!");
            System.exit(0);
        }
    }

    /**
     Checks whether the given time string matches the specified format.
     @param time the time string to be checked.
     @param formatter the date time formatter specifying the expected format.
     @return true if the time string matches the specified format, false otherwise.
     */
    public static boolean trueFormat(String time,DateTimeFormatter formatter){
        try {
            LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    /**
     Checks if the given time string is in the correct format as per the given formatter.
     @param time the time string to be checked
     @param formatter the DateTimeFormatter to be used to parse the time string
     @param initialTime the initial LocalDateTime object against which the time string is to be checked for format correctness
     */
    public static void checkTimeFormat(String time, DateTimeFormatter formatter,LocalDateTime initialTime) throws ExceptionHandling {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
        }catch (Exception e){
            if (initialTime==null){
                Output.writeFile("ERROR: Format of the initial date is wrong! Program is going to terminate!");
                System.exit(0);
            } else if (initialTime!=null) {
                throw new ExceptionHandling("ERROR: Time format is not correct!");
            }

        }



    }

    /**
     This method checks if the input string for skipping minutes is valid and throws an exception if it is not.
     @param skipMinute a string representing the number of minutes to skip
     */
    public static void checkMinutes(String skipMinute) throws ExceptionHandling {
        try{
            int intSkipMinute=Integer.parseInt(skipMinute);
            if (intSkipMinute==0){
                throw new ExceptionHandling("ERROR: There is nothing to skip!");
            }
            else if (intSkipMinute <= 0){
                throw new ExceptionHandling("ERROR: Time cannot be reversed!");
            }

        }
        catch (NumberFormatException e){
            throw  new ExceptionHandling("ERROR: Erroneous command!");
        }


    }

    /**
     This method checks if the new time is not before the initial time.
     @param newTime the new time that is being set
     @param initialTime the initial time of the program
     @throws ExceptionHandling if the new time is before the initial time
     */

    public static void checkSetTime(LocalDateTime newTime,LocalDateTime initialTime) throws ExceptionHandling {
        if (initialTime.isAfter(newTime)){
            throw new ExceptionHandling("ERROR: Time cannot be reversed!");
        }
    }

    /**
     Checks if the length of the command array is 0.
     @param length the length of the command array
     @throws ExceptionHandling if the length of the command array is 0
     */
    public static void checkNop(int length) throws ExceptionHandling {
        if (length==0){
            throw new ExceptionHandling("ERROR: There is nothing to switch!");
        }
    }

    /**
     *Checks whether a device with the same name already exists in the addedDevices map.
     * @param addedDevices Checks whether a device with the same name already exists in the addedDevices map.
     * @param name name of the device
     * @throws ExceptionHandling if a device with the same name already exists in the addedDevices map.
     */
    public static void controlSame(LinkedHashMap<String, SmartDevices> addedDevices,String name) throws ExceptionHandling {
        if (addedDevices.containsKey(name)){
            throw new ExceptionHandling("ERROR: There is already a smart device with same name!");
        }
    }

    /**
     Checks if the initial status is valid.
     @param status the initial status of the device
     @throws ExceptionHandling if the status is not "Off" or "On"
     */
    public static void controlInitialStatus(String status) throws ExceptionHandling {
        if (status.equals("Off")||status.equals("On")){

        }
        else {
            throw new ExceptionHandling("ERROR: Erroneous command!");
        }

    }

    /**
     *This method checks whether the given ampere or megabyte value is a valid double or not.If the value is valid,
     * it checks whether it is a positive number or not. If the value is not a positive number, it throws an exception with
     * an appropriate error message. If the value is not a valid double, it throws an exception with a different error message
     * depending on the className parameter.
     @param ampere The value of the ampere for SmartPlug or the value of the megabyte for SmartCamera
     @param className The name of the class that the given value belongs to
     @throws ExceptionHandling if the given value is not a valid double or not a positive number.
     */

    public static void controlDouble(String ampere,String className) throws ExceptionHandling {
        try{
            double doubleAmpere=Double.parseDouble(ampere);
            if (doubleAmpere <= 0){
                if (className.equals("SmartPlug")){
                    throw new ExceptionHandling("ERROR: Ampere value must be a positive number!");
                } else if (className.equals("SmartCamera")) {
                    throw new ExceptionHandling("ERROR: Megabyte value must be a positive number!");
                }

            }
        }
        catch (NumberFormatException e){
            if (className.equals("SmartPlug")){
                throw  new ExceptionHandling("ERROR: Ampere must be integer");
            }
            else if (className.equals("SmartCamera")){
                throw  new ExceptionHandling("ERROR: Megabyte must be integer");
            }

        }

    }

    /**
     *Checks whether the given kelvin value is in the range of 2000K to 6500K and throws an exception if not.
     @param kelvinValue the kelvin value to be checked as a String
     @throws ExceptionHandling if the kelvin value is not in the specified range or if it is not an integer
     */


    public static void controlKelvin(String kelvinValue) throws ExceptionHandling {
        try {
            int integerKelvin=Integer.parseInt(kelvinValue);
            if (!(integerKelvin<=6500&&integerKelvin>=2000)){
                throw new ExceptionHandling("ERROR: Kelvin value must be in range of 2000K-6500K!");
            }
        }
        catch (NumberFormatException e){
            throw  new ExceptionHandling("ERROR: Kelvin must be integer");
        }
    }
    /**
     *Checks whether the given brightness value is in the range of 0 to 100 and throws an exception if not.
     @param brightnessValue the brightness value to be checked as a String
     @throws ExceptionHandling if the brightness value is not in the specified range or if it is not an integer
     */

    public static void controlBrightness(String brightnessValue) throws ExceptionHandling {
        try {
            int intBrightnessValue=Integer.parseInt(brightnessValue);
            if (!(intBrightnessValue>=0&&intBrightnessValue<=100)){
                throw new ExceptionHandling("ERROR: Brightness must be in range of 0%-100%!");
            }

        }
        catch (NumberFormatException e){
            throw  new ExceptionHandling("ERROR: Brightness must be integer");
        }
    }

    /**
     * It checks whether the device is in the added list or not
     * @param addedDevices Device list
     * @param removedDevice The device on which the operation is performed.
     * @throws ExceptionHandling
     */
    public static void controlPresence(LinkedHashMap<String, SmartDevices> addedDevices,String removedDevice) throws ExceptionHandling {
        if (!addedDevices.containsKey(removedDevice)){
            throw new ExceptionHandling("ERROR: There is not such a device!");
        }
    }

    /**
     *This method checks if the device's new status is different than its old status, throws an exception if they are the same.
     @param deviceOldStatus The old status of the device.
     @param deviceNewStatus The new status of the device.
     @throws ExceptionHandling If the old and new status of the device are the same, an exception is thrown.
     */

    public static void controlSwitch(String deviceOldStatus,String deviceNewStatus)throws ExceptionHandling{
        if (deviceOldStatus.equals(deviceNewStatus)){
            throw new ExceptionHandling("ERROR: This device is already switched "+deviceNewStatus+"!");
        }
    }

    /**
     * Checks whether a new device name is valid and can be changed from old name.
     * @param oldName the old name of the device
     * @param newName the new name of the device
     * @param addedDevices the LinkedHashMap containing the devices and their names
     * @param commandLength the length of the command entered by the user
     * @throws ExceptionHandling if the old and new names are same or if a device with the new name already exists in the addedDevices map.
     */
    public static void controlChangeName(String oldName,String newName,LinkedHashMap<String, SmartDevices> addedDevices,int commandLength) throws ExceptionHandling {
        if (oldName.equals(newName)){
            throw new ExceptionHandling("ERROR: Both of the names are the same, nothing changed!");
        }
        else if (addedDevices.containsKey(newName)){
            throw new ExceptionHandling("ERROR: There is already a smart device with same name!");
        }
    }

    /**
     *Controls if the given plug is already plugged in or not according to the given command.
     * @param isPluggedIn the boolean value indicating if the plug is already plugged in or not
     * @param inOrOut the string command for plug-in or plug-out
     */

    public static void controlPlug(Boolean isPluggedIn,String inOrOut) throws ExceptionHandling {
          if (inOrOut.equals("PlugIn")) {
            if (isPluggedIn){
                throw new ExceptionHandling("ERROR: There is already an item plugged in to that plug!");
            }
        }
        else if (inOrOut.equals("PlugOut")){
            if (!isPluggedIn){
                throw new ExceptionHandling("ERROR: This plug has no item to plug out from that plug!");
            }
        }
    }

    /**
     * Checks whether a given color code is valid.
     * @param colorCode  the color code to be checked
     * @throws ExceptionHandling f the color code is not in the correct format or if the
     * hexadecimal value of the color code is not in the range of 0x0-0xFFFFFF
     */
    public static void controlColorCode(String colorCode) throws ExceptionHandling {
        try {
            int hexNumber=Integer.parseInt(colorCode.substring(2),16);
            if (!(hexNumber >= 0 && hexNumber <= 0xFFFFFF)) {
                throw new ExceptionHandling("ERROR: Color code value must be in range of 0x0-0xFFFFFF");
            }
        }
        catch (NumberFormatException e){
            throw new ExceptionHandling("ERROR: Erroneous command!");
        }
    }

    /**
     * Checks if the given command name is a valid command that can be executed.
     * @param commandName the name of the command to be checked
     * @throws ExceptionHandling if the command is not a valid command
     */
    public static void controlUnWantedCommand(String commandName) throws ExceptionHandling {
        ArrayList<String> commandList = new ArrayList<String>(Arrays.asList("Remove", "ChangeName", "SetTime",
                "SkipMinutes", "SetSwitchTime", "Switch","PlugIn", "PlugOut","SetKelvin", "SetBrightness","SetColor", "SetWhite", "SetColorCode","SetInitialTime","Nop","ZReport"));
       if (!commandList.contains(commandName)){
           throw new ExceptionHandling("ERROR: Erroneous command!");
       }
    }

    /**
     * This method is used to check whether the length of the command is within the given range or not.
     * If the command length is less than the least value or greater than the most value, an exception is thrown with the error message "ERROR: Erroneous command!".
     * @param leastValue the minimum length of the command
     * @param mostValue the maximum length of the command
     * @param commandLength the length of the given command
     * @throws ExceptionHandling if the length of the command is not within the given range
     */

    public static void controlCommandTruth(int leastValue,int mostValue,int commandLength) throws ExceptionHandling {
        if (!(commandLength >= leastValue && commandLength<=mostValue)){
            throw new ExceptionHandling("ERROR: Erroneous command!");
        }
    }

    /**
     * This method checks whether the switch time is after the initial time.
     * If the initial time is after the switch time, it throws an exception with an error message.
     * @param switchTime The time at which a switch should occur.
     * @param initialTime The initial time from which the switch time is being compared.
     * @throws ExceptionHandling If the initial time is after the switch time, an exception is thrown with the error message "ERROR: Switch time cannot be in the past!".
     */
    public static void controlSwitchTimeTruth(LocalDateTime switchTime,LocalDateTime initialTime) throws ExceptionHandling {
        if (initialTime.isAfter(switchTime)){
            throw new ExceptionHandling("ERROR: Switch time cannot be in the past!");
        }
    }

    /**
     * This method checks whether the given time is equal to the initial time.
     * If the times are equal, it throws an exception with an error message indicating that there is nothing to change.
     * @param time The time to be compared with the initial time.
     * @param initialTime The initial time to be compared with the given time.
     * @throws ExceptionHandling If the given time is equal to the initial time, an exception is thrown with the error message "ERROR: There is nothing to change!".
     */
    public static void controlSetTimeTruth(LocalDateTime time,LocalDateTime initialTime) throws ExceptionHandling {
        if (time.equals(initialTime)){
            throw new ExceptionHandling("ERROR: There is nothing to change!");
        }

    }


}
