import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.File;
import java.util.Arrays;


public class BackgroundSelectionScreen extends Screen {

    private static final String enterMessage = "PRESS ENTER TO START";
    private static final String arrowMessage = "USE ARROW KEYS TO NAVIGATE";

    File backgroundFolder = new File("assets/background");
    File[] backgroundFiles = backgroundFolder.listFiles();

    File foregroundFolder=new File("assets/foreground");
    File[] foregroundFiles = foregroundFolder.listFiles();

    int totalBackgrounds = backgroundFiles.length;
    int currentIndex = 0;
    ImageView backgroundImageView;

    ImageView foregroundImageView;

    File crossHairFolder = new File("assets/crosshair");
    File[] crossHairFiles = crossHairFolder.listFiles();

    int totalCrossHairs = crossHairFiles.length;
    int currentIndexCrossHair = 0;
    ImageView crossHairImageView;


    private Text enterText;
    private Text arrowText;

    double crossHairWidth;
    double crossHairHeight;
    double backGroundWidth;
    double foreGroundWidth;
    double backGroundHeight;
    double foreGroundHeight;

    public void backgroundSelectionScene(Stage primaryStage, MediaPlayer mediaPlayer) {

        StackPane root = new StackPane();
        Arrays.sort(backgroundFiles);
        Arrays.sort(crossHairFiles);
        Arrays.sort(foregroundFiles);



        File backgroundFile = backgroundFiles[currentIndex];
        File foregroundFile=foregroundFiles[currentIndex];

        Image backgroundImage = new Image(backgroundFile.toURI().toString());
        Image foregroundImage=new Image(foregroundFile.toURI().toString());

        backgroundImageView = new ImageView(backgroundImage);
        foregroundImageView=new ImageView(foregroundImage);

         backGroundWidth=backgroundImage.getWidth()*scale;
         foreGroundWidth=foregroundImage.getWidth()*scale;

         backGroundHeight=backgroundImage.getHeight()*scale;
         foreGroundHeight=foregroundImage.getHeight()*scale;



        backgroundImageView.setFitWidth(backGroundWidth);
        backgroundImageView.setFitHeight(backGroundHeight);


        foregroundImageView.setFitWidth(foreGroundWidth);
        foregroundImageView.setFitHeight(foreGroundHeight);

        root.getChildren().add(backgroundImageView);




        File crossHairfile=crossHairFiles[currentIndexCrossHair];
        Image crossHairImage=new Image(crossHairfile.toURI().toString());
        crossHairWidth=crossHairImage.getWidth()*scale;
        crossHairHeight=crossHairImage.getHeight()*scale;
        Image crossHairImageWithScale=new Image(crossHairfile.toURI().toString(),crossHairWidth,crossHairHeight,false,false);
        crossHairImageView=new ImageView(crossHairImageWithScale);




        crossHairImageView.setFitHeight(crossHairHeight);
        crossHairImageView.setFitWidth(crossHairWidth);

        root.getChildren().add(crossHairImageView);


        enterText = createText(enterMessage, 5);
        enterText.setFill(Color.ORANGE);

        enterText.setTranslateX(backGroundWidth/25);
        enterText.setTranslateY(-(backGroundHeight/2.6));
        root.getChildren().add(enterText);


        arrowText = createText(arrowMessage, 5);
        arrowText.setFill(Color.ORANGE);


        arrowText.setTranslateX(backGroundWidth/25);
        arrowText.setTranslateY(-(backGroundHeight/2.4));

        root.getChildren().add(arrowText);

        Text exitText=createText("PRESS ESC TO EXİT",5);
        exitText.setFill(Color.ORANGE);
        exitText.setTranslateX(backGroundWidth/25);
        exitText.setTranslateY(-(backGroundWidth/3));
        root.getChildren().add(exitText);




        Scene scene = new Scene(root, backGroundWidth, backGroundHeight);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                mediaPlayer.stop();
                MediaPlayer gameMusic=setMusic("assets/effects/Intro.mp3");
                gameMusic.play();
                gameMusic.setOnEndOfMedia(()->new GameScreenLevel1(backgroundImageView,foregroundImageView,crossHairImageView,primaryStage,1,3,mediaPlayer));

            } else if (event.getCode() == KeyCode.ESCAPE) {
                TitleScreen titleScreen = new TitleScreen();
                titleScreen.titleScene(primaryStage,mediaPlayer);
            } else if (event.getCode() == KeyCode.RIGHT) {
                changeBackground(true,root);
            } else if (event.getCode() == KeyCode.LEFT) {
                changeBackground(false,root);
            } else if (event.getCode()==KeyCode.UP) {
                changeCrossHair(true,root);
            }
            else if (event.getCode()==KeyCode.DOWN) {
                changeCrossHair(false,root);
            }
        });

        primaryStage.setScene(scene);


        primaryStage.show();


    }

    private Text createText(String message, double fontSize) {
        Text text = new Text(message);
        text.setFont(Font.font("Arial", FontWeight.BOLD, fontSize*scale));
        return text;
    }

    private void changeBackground(boolean next,StackPane root) {
        root.getChildren().removeAll(backgroundImageView,crossHairImageView,enterText,arrowText);
        if (next) {
            currentIndex++;
            if (currentIndex >= totalBackgrounds) {
                currentIndex = 0;

            }
        }
        else {
            currentIndex--;
            if (currentIndex < 0) {
                currentIndex = totalBackgrounds - 1;
            }
        }
        File backgroundFile = backgroundFiles[currentIndex];
        File foregroundFile=foregroundFiles[currentIndex];

        Image backgroundImage = new Image(backgroundFile.toURI().toString());
        Image foregroundImage=new Image(foregroundFile.toURI().toString());

        backgroundImageView = new ImageView(backgroundImage);
        foregroundImageView=new ImageView(foregroundImage);

        backgroundImageView.setFitWidth(backGroundWidth);
        backgroundImageView.setFitHeight(backGroundHeight);
        foregroundImageView.setFitWidth(foreGroundWidth);
        foregroundImageView.setFitHeight(foreGroundHeight);
        root.getChildren().add(backgroundImageView);
        root.getChildren().add(crossHairImageView);
        root.getChildren().add(enterText);
        root.getChildren().add(arrowText);
    }

    private void changeCrossHair(boolean next,StackPane root){
        root.getChildren().removeAll(backgroundImageView,crossHairImageView,enterText,arrowText);
        if (next) {
            currentIndexCrossHair++;
            if (currentIndexCrossHair >= totalCrossHairs) {
                currentIndexCrossHair = 0;

            }
        }
        else {
            currentIndexCrossHair--;
            if (currentIndexCrossHair < 0) {
                currentIndexCrossHair = totalCrossHairs - 1;
            }
        }
        File crossHairFile = crossHairFiles[currentIndexCrossHair];
        Image crossHairImage = new Image(crossHairFile.toURI().toString(),crossHairWidth,crossHairHeight,false,false);
        crossHairImageView = new ImageView(crossHairImage);
        crossHairImageView.setFitWidth(crossHairWidth);
        crossHairImageView.setFitHeight(crossHairHeight);

        root.getChildren().add(backgroundImageView);
        root.getChildren().add(crossHairImageView);
        root.getChildren().add(enterText);
        root.getChildren().add(arrowText);
    }


}
