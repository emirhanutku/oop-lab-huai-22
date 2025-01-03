import java.time.LocalDateTime;
import java.util.*;

public class Repository{

    LinkedHashMap<String, SmartDevices> addedDevices=new LinkedHashMap<>();
    LocalDateTime initialTime;

    /**
     *Adds a new SmartDevice to the collection of added devices, with the given name as the key.
     * @param name the name of the SmartDevice being added
     * @param smartDevices the SmartDevice being added to the collection
     */

    public void add(String name,SmartDevices smartDevices){
        addedDevices.put(name,smartDevices);
    }

    LinkedHashMap<String, LocalDateTime> switchTimesMap=new LinkedHashMap<>();
    //ArrayList<LocalDateTime> switchTimeArray = new ArrayList<>();
    LinkedHashMap<String,LocalDateTime>openTime=new LinkedHashMap<>();
    LinkedHashMap<String,LocalDateTime>closeTime=new LinkedHashMap<>();

    List<SmartDevices> smartDevicesNotNullList = new ArrayList<>();
    List<SmartDevices> smartDevicesNullList = new ArrayList<>();

}
