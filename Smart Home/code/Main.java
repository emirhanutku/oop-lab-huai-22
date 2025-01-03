import javax.jws.soap.SOAPBinding;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Main {
    static String outputFile;
    public static void main(String[] args) {
        /*Map<String, SmartDevices> addedDevices=new HashMap<>();
        Repository repository=new Repository(addedDevices);

        SmartLampColorManagement smartLampColorManagement=new SmartLampColorManagement();
        SmartCameraManagement smartCameraManagement=new SmartCameraManagement();
        smartCameraManagement.Add("Emirhan salak".split(" "),repository);
        smartLampColorManagement.Add("Emirhan salak".split(" "),repository);
        System.out.println(repository.getAddedDevices());*/
        String inputFile=args[0];
        outputFile=args[1];
        Output output=new Output();
        readFile(inputFile);


    }


    public static void readFile(String inputfile){

        BufferedReader reader;
        ArrayList<String>methodList=new ArrayList<>();

        try {
            reader = new BufferedReader(new FileReader(inputfile));
            String line = reader.readLine();
            while (line != null) {
                if (!falseLine(line)){
                    Output.writeFile("COMMAND: "+line);
                    String[] commands=line.split("\t");
                    String methodName=commands[0];
                    methodList.add(methodName);
                    if (methodName.equals("Add")){
                        try{
                            ExceptionHandling.checkFirstSetInitialTime(BaseManagement.repository.initialTime, commands);
                            String device=commands[1];
                            switch(device){
                                case "SmartPlug":
                                    SmartPlugManagement smartPlugManagement=new SmartPlugManagement();
                                    smartPlugManagement.Add(commands);
                                    break;
                                case "SmartCamera":
                                    SmartCameraManagement smartCameraManagement =new SmartCameraManagement();
                                    smartCameraManagement.Add(commands);
                                    break;
                                case "SmartLamp":
                                    SmartLampManagement smartLampManagement=new SmartLampManagement();
                                    smartLampManagement.Add(commands);
                                    break;
                                case "SmartColorLamp":
                                    SmartLampColorManagement smartLampColorManagement=new SmartLampColorManagement();
                                    smartLampColorManagement.Add(commands);
                            }
                        }catch (Exception e){
                            Output.writeFile(e.getMessage());
                        }

                    }
                    else {
                        try {
                            ExceptionHandling.checkFirstSetInitialTime(BaseManagement.repository.initialTime, commands);
                            ExceptionHandling.controlUnWantedCommand(methodName);




                            Class<?>clazz=Class.forName("BaseManagement");
                            Object obj=clazz.newInstance();
                            Method method=clazz.getMethod(methodName,String[].class);
                            method.invoke(obj,(Object) commands);
                        }

                        catch (Exception e){
                            if (e.getCause()!=null){
                                Output.writeFile(e.getCause().getMessage());
                            }
                            else {
                                Output.writeFile(e.getMessage());
                            }

                        }


                    }
                }

                line = reader.readLine();
            }
            if (!methodList.get(methodList.size()-1).equals("ZReport")){
                Output.writeFile("ZReport:");
                String[]emptyCommand="Assignment2   ".split("\t");
                Class<?>clazz=Class.forName("BaseManagement");
                Object obj=clazz.newInstance();
                Method method=clazz.getMethod("ZReport", String[].class);
                method.invoke(obj,(Object)emptyCommand );
            }
            reader.close();
        } catch (Exception e) {
            Output.writeFile(e.getMessage());
        }
    }

    /**
     *Determines if the given command consists only of whitespace or tab characters, or if it is completely empty.
     * @param command
     * @return a boolean value, true if the string consists only of whitespace or tab characters, or is completely empty; false otherwise.
     */

    public static boolean falseLine(String command){
        Pattern pattern = Pattern.compile("^[ \t]+$");
        return pattern.matcher(command).matches() || command.isEmpty();
    }
}