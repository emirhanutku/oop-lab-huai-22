import java.io.BufferedReader;
import java.io.FileReader;

public class ReadFile {
    public ReadFile(String inputFile) {
        BufferedReader reader;
        try {
            Management management=new Management();
            reader = new BufferedReader(new FileReader(inputFile));
            String line = reader.readLine();
            while (line != null) {
                try {
                    String[] desiredInput=line.split("\t");
                    String command=desiredInput[0];
                    switch (command){
                        case "addBook":
                            management.addBook(desiredInput);
                            break;
                        case "addMember":
                            management.addMember(desiredInput);
                            break;
                        case "borrowBook":
                            management.borrowBook(desiredInput);
                            break;
                        case "returnBook":
                            management.returnBook(desiredInput);
                            break;
                        case "extendBook":
                            management.extendBook(desiredInput);
                            break;
                        case "readInLibrary":
                            management.readLibrary(desiredInput);
                            break;
                        case "getTheHistory":
                            management.getHistory(desiredInput);
                            break;
                    }
                    line = reader.readLine();
                }
                catch (Exception f){

                    Output.writeFile(f.getMessage());
                    line=reader.readLine();
                }

            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
