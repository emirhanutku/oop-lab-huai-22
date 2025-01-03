public class SmartCamera extends SmartDevices {
    double megabytePerMin=0;
    double consumedValue=0;

    public SmartCamera(String name, double megabytePerMin) {
        super(name);
        this.megabytePerMin = megabytePerMin;
    }
}
