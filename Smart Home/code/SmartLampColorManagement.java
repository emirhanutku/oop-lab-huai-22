import java.time.LocalDateTime;

public class SmartLampColorManagement extends BaseManagement implements  AddCommand,DeviceInfo{
    /**
     * The necessary if block is entered according to the length of the incoming input, and the registration process is performed
     * @param command The device to be added
     * @throws ExceptionHandling
     */
    @Override
    public void Add(String[] command) throws ExceptionHandling {
        if (command.length==5){
            throw new ExceptionHandling("ERROR: Erroneous command!");
        }
        ExceptionHandling.controlCommandTruth(3,6,command.length);
        ExceptionHandling.controlSame(repository.addedDevices,command[2]);
        if (command.length==3){
            repository.add(command[2],new SmartLampColor(command[2]));
        } else if (command.length==4) {
            ExceptionHandling.controlInitialStatus(command[3]);
            SmartLampColor smartLampColor=new SmartLampColor(command[2]);
            smartLampColor.initialStatus=command[3].toLowerCase();
            repository.add(command[2],smartLampColor);
        } else if (command.length==6) {
            ExceptionHandling.controlInitialStatus(command[3]);
            ExceptionHandling.controlBrightness(command[5]);

            if (command[4].substring(0,2).equals("0x")){
                SmartLampColor smartLampColor=new SmartLampColor(command[2]);
                smartLampColor.initialStatus=command[3].toLowerCase();
                smartLampColor.brightness=Integer.parseInt(command[5]);
                try {
                    int hexNumber=Integer.parseInt(command[4].substring(2),16);
                    if (hexNumber >= 0 && hexNumber <= 0xFFFFFF) {
                        smartLampColor.colorCode=command[4];
                        repository.add(command[2],smartLampColor);
                    } else {
                        throw new ExceptionHandling("ERROR: Color code value must be in range of 0x0-0xFFFFFF!");
                    }
                }
                catch (NumberFormatException e){
                    throw new ExceptionHandling("ERROR: Erroneous command!");
                }
            }
            else {
                ExceptionHandling.controlKelvin(command[4]);
                SmartLampColor smartLampColor=new SmartLampColor(command[2]);
                smartLampColor.initialStatus=command[3].toLowerCase();
                smartLampColor.colorCode=command[4];
                smartLampColor.brightness=Integer.parseInt(command[5]);
                repository.add(command[2],smartLampColor);
            }


        }
    }

    /**
     *The properties of the relevant device are obtained and written using the get methods
     * @param closedItem The device whose information will be printed
     */

    @Override
    public void getInfo(SmartDevices closedItem) {
        String name=closedItem.name;
        String status=closedItem.initialStatus;
        Object colorValue=closedItem.getProperty("colorCode");
        Object brightness=closedItem.getProperty("brightness");
        LocalDateTime switchTime=closedItem.switchTime;
        String switchTimeFormat;
        if (switchTime==null){
            switchTimeFormat=null;
        }
        else {
            switchTimeFormat=BaseManagement.convertTimeTrueFormat(switchTime);
        }
        if (colorValue.toString().substring(0,2).equals("0x")){
            String stringFormat=String.format("Smart Color Lamp %s is %s and its color value is %s with %s%s brightness, " +
                    "and its time to switch its status is %s.",name,status,colorValue,brightness,"%",switchTimeFormat);
            Output.writeFile(stringFormat);
        }
        else {
            String stringFormat=String.format("Smart Color Lamp %s is %s and its color value is %sK with %s%s brightness, " +
                    "and its time to switch its status is %s.",name,status,colorValue,brightness,"%",switchTimeFormat);
            Output.writeFile(stringFormat);
        }


    }
}
