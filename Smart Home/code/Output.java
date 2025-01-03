import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class Output {
    static String outputTxt;
    static {
        outputTxt= Main.outputFile;
    }
    public Output(){
        try {new BufferedWriter(new FileWriter(outputTxt, false));
        }catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(String text) {
        try {BufferedWriter writer = new BufferedWriter(new FileWriter(outputTxt, true));
            writer.write(text + "\n");
            writer.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
