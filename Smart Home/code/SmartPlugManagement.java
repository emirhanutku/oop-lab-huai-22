import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;

public class SmartPlugManagement extends BaseManagement implements AddCommand,DeviceInfo{
    /**
     * The necessary if block is entered according to the length of the incoming input, and the registration process is performed
     * @param command The device to be added
     * @throws ExceptionHandling
     */
    @Override
    public void Add(String[] command)throws ExceptionHandling {
        ExceptionHandling.controlCommandTruth(3,5,command.length);
        ExceptionHandling.controlSame(repository.addedDevices,command[2]);
        if (command.length==3){
            repository.add(command[2],new SmartPlug(command[2]));
        } else if (command.length==4) {
            ExceptionHandling.controlInitialStatus(command[3]);
            SmartPlug smartPlug=new SmartPlug(command[2]);
            smartPlug.initialStatus=command[3].toLowerCase();
            repository.add(command[2],smartPlug);
        } else if (command.length==5) {
            ExceptionHandling.controlInitialStatus(command[3]);
            ExceptionHandling.controlDouble(command[4],command[1]);
            SmartPlug smartPlug=new SmartPlug(command[2]);
            smartPlug.initialStatus=command[3].toLowerCase();
            smartPlug.ampere=Double.parseDouble(command[4]);
            smartPlug.setProperty("isPluggedIn",true);
            if (smartPlug.initialStatus.equals("on")){
                repository.openTime.put(smartPlug.name,repository.initialTime);
            }
            repository.add(command[2],smartPlug);
        }
    }
    /**
     *The properties of the relevant device are obtained and written using the get methods
     * @param closedItem The device whose information will be printed
     */
    @Override
    public void getInfo(SmartDevices closedItem) {
        String name=closedItem.name;
        String initialStatus=closedItem.initialStatus;
        Double consumedValue=Double.parseDouble(closedItem.getProperty("consumedValue").toString());
        LocalDateTime switchTime=closedItem.switchTime;
        String switchTimeFormat;
        if (switchTime==null){
            switchTimeFormat=null;
        }
        else {
            switchTimeFormat=BaseManagement.convertTimeTrueFormat(switchTime);
        }
        String  stringFormat=String.format("Smart Plug %s is %s and consumed %.2fW so far " +
                "(excluding current device), and its time to switch its status is %s.",name,initialStatus,consumedValue,switchTimeFormat);
        Output.writeFile(stringFormat);
    }
    /**
     * The opening and closing times are retrieved from hashmaps with the name of the device,
     * and the consumedValue is calculated by calculating the time difference between them.
     * @param device The name of the device to be calculated.
     */

    public static void calculateConsumedValue(String device){
        double takeMinutes= Duration.between(repository.openTime.get(device),repository.closeTime.get(device)).toMinutes();
        double ampere=((Double)repository.addedDevices.get(device).getProperty("ampere"));
        double voltage=(Double)repository.addedDevices.get(device).getProperty("voltage");
        double consumedValue=(takeMinutes*ampere*voltage)/60;
        SmartPlug initialPlug=(SmartPlug)repository.addedDevices.get(device);
        initialPlug.consumedValue+=consumedValue;
        repository.closeTime.remove(device);
        repository.openTime.remove(device);
    }
}
