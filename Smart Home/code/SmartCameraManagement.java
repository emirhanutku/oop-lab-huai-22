import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;

public class SmartCameraManagement extends BaseManagement implements AddCommand,DeviceInfo{
    /**
     * The necessary if block is entered according to the length of the incoming input, and the registration process is performed
     * @param command The device to be added
     * @throws ExceptionHandling
     */

    @Override
    public void Add(String[] command) throws ExceptionHandling {
        ExceptionHandling.controlCommandTruth(4,5,command.length);
        ExceptionHandling.controlSame(repository.addedDevices,command[2]);
        if (command.length==4){
            ExceptionHandling.controlDouble(command[3],command[1]);
            repository.add(command[2],new SmartCamera(command[2],Double.parseDouble(command[3])));
        } else if (command.length==5) {
            ExceptionHandling.controlDouble(command[3],command[1]);
            ExceptionHandling.controlInitialStatus(command[4]);
            SmartCamera smartCamera=new SmartCamera(command[2],Double.parseDouble(command[3]));
            smartCamera.initialStatus=command[4].toLowerCase();
            if (smartCamera.initialStatus.equals("on")){
                repository.openTime.put(command[2],repository.initialTime);
            }
            repository.add(command[2],smartCamera);
        }
    }

    /**
     *The properties of the relevant device are obtained and written using the get methods
     * @param closedItem The device whose information will be printed
     */
    @Override
    public void getInfo(SmartDevices closedItem) {
        String name=closedItem.name;
        String initialStatus= closedItem.initialStatus;
        Double consumedValue=Double.parseDouble(closedItem.getProperty("consumedValue").toString());
        LocalDateTime switchTime=closedItem.switchTime;
        String switchTimeFormat;
        if (switchTime==null){
            switchTimeFormat=null;
        }
        else {
            switchTimeFormat=BaseManagement.convertTimeTrueFormat(switchTime);
        }

        String stringFormat=String.format("Smart Camera %s is %s and used %.2f MB of storage so far " +
                "(excluding current status), and its time to switch its status is %s.",name,initialStatus,consumedValue,switchTimeFormat);
        Output.writeFile(stringFormat);

    }

    /**
     * The opening and closing times are retrieved from hashmaps with the name of the device,
     * and the consumedValue is calculated by calculating the time difference between them.
     * @param device The name of the device to be calculated.
     */
    public static void calculateConsumedValue(String device){
        double takeMinutes= Duration.between(repository.openTime.get(device),repository.closeTime.get(device)).toMinutes();
        double megabytePerMin=((Double)repository.addedDevices.get(device).getProperty("megabytePerMin"));
        double consumedValue=takeMinutes*megabytePerMin;
        SmartCamera initialCamera=(SmartCamera)repository.addedDevices.get(device);
        initialCamera.consumedValue+=consumedValue;
        repository.closeTime.remove(device);
        repository.openTime.remove(device);
    }
}
