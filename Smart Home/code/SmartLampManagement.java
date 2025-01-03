import java.time.LocalDateTime;

public class SmartLampManagement extends BaseManagement implements AddCommand,DeviceInfo {
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
        if (command.length == (3)) {
            repository.add(command[2], new SmartLamp(command[2]));
        } else if (command.length == 4) {
            ExceptionHandling.controlInitialStatus(command[3]);

            SmartLamp smartLamp = new SmartLamp(command[2]);
            smartLamp.initialStatus = command[3].toLowerCase();
            repository.add(command[2], smartLamp);
        } else if (command.length==6) {
            ExceptionHandling.controlInitialStatus(command[3]);
            ExceptionHandling.controlKelvin(command[4]);
            ExceptionHandling.controlBrightness(command[5]);

            SmartLamp smartLamp=new SmartLamp(command[2]);
            smartLamp.initialStatus=command[3].toLowerCase();
            smartLamp.kelvin=Integer.parseInt(command[4]);
            smartLamp.brightness=Integer.parseInt(command[5]);
            repository.add(command[2],smartLamp);
        }


    }

    /**
     *The properties of the relevant device are obtained and written using the get methods
     * @param closedItemL The device whose information will be printed
     */

    @Override
    public void getInfo(SmartDevices closedItemL) {
        String name=closedItemL.name;
        String status=closedItemL.initialStatus;
        int kelvinValue=Integer.parseInt(closedItemL.getProperty("kelvin").toString());
        int brightness=Integer.parseInt(closedItemL.getProperty("brightness").toString());
        LocalDateTime switchTime=closedItemL.switchTime;
        String switchTimeFormat;
        if (switchTime==null){
            switchTimeFormat=null;
        }
        else {
            switchTimeFormat=BaseManagement.convertTimeTrueFormat(switchTime);
        }
        String stringFormat=String.format("Smart Lamp %s is %s and its kelvin value is %dK with %d%s brightness, " +
                "and its time to switch its status is %s.",name,status,kelvinValue,brightness,"%",switchTimeFormat);
        Output.writeFile(stringFormat);


    }
}
