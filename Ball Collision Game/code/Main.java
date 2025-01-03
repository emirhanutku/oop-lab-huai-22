import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            ArrayList<ArrayList<String>> boardArray = new ArrayList<ArrayList<String>>();
            ArrayList<String> moveArray = new ArrayList<String>();
            String boardFile = args[0];
            String moveFile = args[1];
            boardReadFile(boardArray,boardFile);
            moveReadFile(moveArray,moveFile);
            int columnIndex=columnIndexOfWhiteBall(boardArray);
            int rowIndex=rowIndexOfWhiteBall(boardArray);
            Game game=new Game();
            game.playGame(boardArray,moveArray,columnIndex,rowIndex);
            gameTime(boardArray,moveArray,columnIndex,rowIndex);



        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public static ArrayList<ArrayList<String>> boardReadFile(ArrayList<ArrayList<String>> _boardArray,String boardFile)
            throws IOException {
        String line;
        BufferedReader BoardfileRead = new BufferedReader(new FileReader(boardFile));
        while ((line = BoardfileRead.readLine()) != null) {
            List<String> myList = new ArrayList<String>(Arrays.asList(line.split(" ")));
            _boardArray.add(new ArrayList<String>(myList));
        }
        return _boardArray;
}



    public static ArrayList<String> moveReadFile(ArrayList<String> _moveArray,String moveFile)
            throws IOException {
        String line;
        BufferedReader MoveFileRead = new BufferedReader(new FileReader(moveFile));
        while ((line = MoveFileRead.readLine()) != null){
                Collections.addAll(_moveArray,line.split(" "));
        }
        return _moveArray;

    }

    public static int columnIndexOfWhiteBall(ArrayList<ArrayList<String>> __boardArray){
        int columnNumber=0;
        for (ArrayList<String> i:__boardArray){
            columnNumber=i.indexOf("*");
            if (columnNumber>=0){
                break;
            }
        }
        return columnNumber;
    }
    public static int rowIndexOfWhiteBall(ArrayList<ArrayList<String>> __boardArray){
        int rowNumber=0;
        int columnNumber=0;
        for (ArrayList<String> i:__boardArray){
            columnNumber=i.indexOf("*");
            if (columnNumber>=0){
                break;
            }
            rowNumber++;
        }
            return  rowNumber;
    }


    public static void gameTime(ArrayList<ArrayList<String>> boardArray,ArrayList<String> moveArray,int columnIndex, int rowIndex ) throws IOException {
        Game game=new Game();
        game.playGame(boardArray,moveArray,columnIndex,rowIndex);
        PrintWriter out = new PrintWriter(new FileOutputStream("output.txt",true));
        out.println("Game board:");

        for (ArrayList<String> strings : boardArray) {
            for (String j : strings) {
                out.print(j+" ");
            }
            out.println(" ");
        }
        out.println("");
        out.println("Your movement is:");
        out.close();
        for (String move:moveArray){
            out = new PrintWriter(new FileOutputStream("output.txt",true));
            out.write(move+" ");

            switch (move){
                case ("L"):
                    out.close();
                    game.leftMove(boardArray);
                    break;
                case("R"):
                    out.close();
                    game.rightMove(boardArray);
                    break;
                case ("U"):
                    out.close();
                    game.upMove(boardArray);
                    break;
                case ("D"):
                    out.close();
                    game.downMove(boardArray);
                    break;
            }

        }

        game.finalOfGame(boardArray,"");


    }
}



