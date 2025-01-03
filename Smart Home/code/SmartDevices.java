import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class SmartDevices implements Comparable<SmartDevices> {
    String name;
    String initialStatus="off";
    LocalDateTime switchTime;

    public SmartDevices(String name) {
        this.name = name;
    }

    /**
     *Returns the value of the specified property using Java Reflection.
     * @param propertyName the name of the property to get
     * @return the value of the specified property
     */
    public Object getProperty(String propertyName) {
        try {
            Field field = this.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            return field.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }

    /**
     * Sets the value of the specified property using Java Reflection.
     * @param propertyName the name of the property to set
     * @param value the value to set the property to
     */
    public void setProperty(String propertyName, Object value) {
        try {
            Field field = this.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            field.set(this, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return switchTime of device
     */
    public LocalDateTime getSwitchTime(){
        return switchTime;
    }

    /**
     * Compares this SmartDevices object with another SmartDevices object based on their switch time.
     * @param other the SmartDevices object to compare to
     */

    public int compareTo(SmartDevices other) {
        return switchTime.compareTo(other.switchTime);
    }}
