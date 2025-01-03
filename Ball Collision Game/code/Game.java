import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class Game {

    int indexOfColumn;
    int indexOfRow;
    int bonus;


    public void playGame(ArrayList<ArrayList<String>> boardArray, ArrayList<String> moveArray, int indexOfColumn, int indexOfRow) {
        this.indexOfColumn = indexOfColumn;
        this.indexOfRow = indexOfRow;


    }

    public void leftMove(ArrayList<ArrayList<String>> _boardArray) {
        try {

            String changedLetter = _boardArray.get(this.indexOfRow).get(this.indexOfColumn - 1);
            if (changedLetter.equals("W")) {
                rightMove(_boardArray);
            } else if (changedLetter.equals("H")) {
                _boardArray.get(this.indexOfRow).set(this.indexOfColumn," ");
                finalOfGame(_boardArray,changedLetter);

            } else {
                if (changedLetter.equals("R") || changedLetter.equals("Y") || changedLetter.equals("B")) {
                    calculateScore(changedLetter);
                    _boardArray.get(this.indexOfRow).set(this.indexOfColumn-1,"X");
                }
                Collections.swap(_boardArray.get(this.indexOfRow), this.indexOfColumn - 1, this.indexOfColumn);
                this.indexOfColumn = this.indexOfColumn - 1;
            }
        } catch (IndexOutOfBoundsException e) {

            int numberOfColumn = _boardArray.get(this.indexOfColumn).size();
            String changedLetter = _boardArray.get(this.indexOfRow).get(numberOfColumn - 1);
            if (changedLetter.equals("W")) {
                rightMove(_boardArray);
            } else if (changedLetter.equals("H")) {
                _boardArray.get(this.indexOfRow).set(this.indexOfColumn," ");
                finalOfGame(_boardArray,changedLetter);

            } else {
                if (changedLetter.equals("R") || changedLetter.equals("Y") || changedLetter.equals("B")){
                    calculateScore(changedLetter);
                    _boardArray.get(this.indexOfRow).set(numberOfColumn-1,"X");
                }
                Collections.swap(_boardArray.get(this.indexOfRow), numberOfColumn - 1, this.indexOfColumn);
                this.indexOfColumn = numberOfColumn - 1;
            }
        }
    }

    public void rightMove(ArrayList<ArrayList<String>> _boardArray) {
        try {
            String changedLetter = _boardArray.get(this.indexOfRow).get(this.indexOfColumn + 1);
            if (changedLetter.equals("W")) {
                leftMove(_boardArray);
            }else if (changedLetter.equals("H")) {
                _boardArray.get(this.indexOfRow).set(this.indexOfColumn," ");
                finalOfGame(_boardArray,changedLetter);
            } else {
                if (changedLetter.equals("R") || changedLetter.equals("Y") || changedLetter.equals("B")){
                    calculateScore(changedLetter);
                    _boardArray.get(this.indexOfRow).set(this.indexOfColumn+1,"X");
                }
                Collections.swap(_boardArray.get(this.indexOfRow), this.indexOfColumn + 1, this.indexOfColumn);
                this.indexOfColumn += 1;
            }


        } catch (IndexOutOfBoundsException e) {
            int numberOfColumn = _boardArray.get(this.indexOfColumn).size();
            String changedLetter = _boardArray.get(this.indexOfRow).get(0);
            if (changedLetter.equals("W")) {
                leftMove(_boardArray);
            }
            else if (changedLetter.equals("H")) {
                _boardArray.get(this.indexOfRow).set(this.indexOfColumn," ");
                finalOfGame(_boardArray,changedLetter);
            }else {
                if (changedLetter.equals("R") || changedLetter.equals("Y") || changedLetter.equals("B")){
                    calculateScore(changedLetter);
                    _boardArray.get(this.indexOfRow).set(0,"X");
                }
                Collections.swap(_boardArray.get(this.indexOfRow), 0, numberOfColumn - 1);
                this.indexOfColumn = 0;
            }

        }

    }

    public void upMove(ArrayList<ArrayList<String>> _boardArray) {
        try {
            String changedLetter = _boardArray.get(this.indexOfRow - 1).get(this.indexOfColumn);
            if (changedLetter.equals("W")) {
                downMove(_boardArray);
            }else if (changedLetter.equals("H")) {
                _boardArray.get(this.indexOfRow).set(this.indexOfColumn," ");
                finalOfGame(_boardArray,changedLetter);
            }
            else {
                if (changedLetter.equals("R") || changedLetter.equals("Y") || changedLetter.equals("B")){
                    calculateScore(changedLetter);
                    changedLetter="X";
                }
                _boardArray.get(this.indexOfRow - 1).set(this.indexOfColumn, "*");
                _boardArray.get(this.indexOfRow).set(this.indexOfColumn, changedLetter);
                this.indexOfRow -= 1;
            }

        } catch (IndexOutOfBoundsException e) {
            int numberOfRow = _boardArray.size();
            String changedLetter = _boardArray.get(numberOfRow - 1).get(this.indexOfColumn);
            if (changedLetter.equals("W")) {
                downMove(_boardArray);
            } else if (changedLetter.equals("H")) {
                _boardArray.get(this.indexOfRow).set(this.indexOfColumn," ");
                finalOfGame(_boardArray,changedLetter);
            }
            else {
                if (changedLetter.equals("R") || changedLetter.equals("Y") || changedLetter.equals("B")){
                    calculateScore(changedLetter);
                    changedLetter="X";
                }
                _boardArray.get(numberOfRow - 1).set(this.indexOfColumn, "*");
                _boardArray.get(this.indexOfRow).set(this.indexOfColumn, changedLetter);
                this.indexOfRow = numberOfRow - 1;
            }


        }

    }

    public void downMove(ArrayList<ArrayList<String>> _boardArray) {
        try {
            String changedLetter = _boardArray.get(this.indexOfRow + 1).get(this.indexOfColumn);
            if (changedLetter.equals("W")) {
                upMove(_boardArray);
            }else if (changedLetter.equals("H")) {
                _boardArray.get(this.indexOfRow).set(this.indexOfColumn," ");
                finalOfGame(_boardArray,changedLetter);
            }
            else {
                if (changedLetter.equals("R") || changedLetter.equals("Y") || changedLetter.equals("B")){
                    calculateScore(changedLetter);
                    changedLetter="X";
                }
                _boardArray.get(this.indexOfRow + 1).set(this.indexOfColumn, "*");
                _boardArray.get(this.indexOfRow).set(this.indexOfColumn, changedLetter);
                this.indexOfRow = this.indexOfRow + 1;
            }


        } catch (IndexOutOfBoundsException e) {
            String changedLetter = _boardArray.get(0).get(this.indexOfColumn);
            if (changedLetter.equals("W")) {
                upMove(_boardArray);
            }else if (changedLetter.equals("H")) {
                _boardArray.get(this.indexOfRow).set(this.indexOfColumn," ");
                finalOfGame(_boardArray,changedLetter);
            } else {
                if (changedLetter.equals("R") || changedLetter.equals("Y") || changedLetter.equals("B")){
                    calculateScore(changedLetter);
                    changedLetter="X";
                }
                _boardArray.get(0).set(this.indexOfColumn, "*");
                _boardArray.get(this.indexOfRow).set(this.indexOfColumn, changedLetter);
                this.indexOfRow = 0;
            }


        }


    }


    public int calculateScore(String letter){
        switch (letter) {
            case "R":
                bonus += 10;
                break;
            case "Y":
                bonus += 5;
                break;
            case "B":
                bonus -= 5;

                break;
        }
        return bonus;
    }

    public void finalOfGame(ArrayList<ArrayList<String>> _boardArray,String changedLetter){

        try {


            out.println("");
            out.println("");
            int rowSize=_boardArray.size();
            out.println("Your output is:");
            for (int i=0; i<rowSize; i++){
                for (String j:_boardArray.get(i)){
                    out.print(j+" ");
                }
                out.println(" ");
            }
            out.println(" ");
            if (changedLetter.equals("H")){
                out.println("Game Over!");
            }
            out.println("Score: "+calculateScore(" "));
            out.close();
            System.exit(0);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }

}



