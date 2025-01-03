public class SmartPlug extends SmartDevices {
    public double ampere=0;
    public double voltage=220;
    Boolean isPluggedIn=false;
    double consumedValue=0;

    public SmartPlug(String name) {
        super(name);
    }

}
