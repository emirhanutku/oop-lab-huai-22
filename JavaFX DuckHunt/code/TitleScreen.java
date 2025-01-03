import javafx.animation.Animation;
import javafx.animation.FadeTransition;
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
import javafx.util.Duration;

public class TitleScreen extends Screen {
    private static final String GAME_TITLE = "HUBBM Duck Hunt";
    private static final String WELCOME_PATH = "assets/welcome/1.png";
    private static final String faviconPath = "assets/favicon/1.png";
    private static final String ENTER_MESSAGE = "PRESS ENTER TO PLAY";
    private static final String EXIT_MESSAGE = "PRESS ESC TO EXIT";



    private Text enterText;
    private Text exitText;
    double welcomeImageHeight;
    double welcomeImageWidth;

    /**
     Sets up and displays the title scene of the game.

     @param primaryStage The primary stage of the JavaFX application.

     @param mediaPlayer The MediaPlayer object for playing music.
     */
    public  void   titleScene(Stage primaryStage,MediaPlayer mediaPlayer){
        Image favicon = new Image("file:" + faviconPath);
        primaryStage.getIcons().add(favicon);

        primaryStage.setTitle(GAME_TITLE);
        mediaPlayer.play();

        StackPane root = new StackPane();

        Image welcomeImage = new Image("file:" + WELCOME_PATH);
        ImageView backgroundImageView = new ImageView(welcomeImage);

        welcomeImageWidth=welcomeImage.getWidth()*scale;
        welcomeImageHeight=welcomeImage.getHeight()*scale;
        backgroundImageView.setFitWidth(welcomeImageWidth);
        backgroundImageView.setFitHeight(welcomeImageHeight);



        root.getChildren().add(backgroundImageView);




        enterText = createText(ENTER_MESSAGE, 10);
        enterText.setFill(Color.ORANGE);

        enterText.setTranslateY(welcomeImageHeight/6);
        enterText.setOpacity(0);
        root.getChildren().add(enterText);


        exitText = createText(EXIT_MESSAGE, 10);
        exitText.setFill(Color.ORANGE);
        //exitText.setTranslateY(130);
        exitText.setTranslateY(welcomeImageHeight/4.5);
        exitText.setOpacity(0);
        root.getChildren().add(exitText);

        FadeTransition enterFadeTransition = createFadeTransition(enterText, 0.0, 1.0, Duration.seconds(1.5));
        enterFadeTransition.setCycleCount(Animation.INDEFINITE);


        FadeTransition exitFadeTransition = createFadeTransition(exitText, 0.0, 1.0, Duration.seconds(1.5));
        exitFadeTransition.setCycleCount(Animation.INDEFINITE);







        Scene scene = new Scene(root,welcomeImageWidth,welcomeImageHeight );
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                BackgroundSelectionScreen backgroundSelectionScreen=new BackgroundSelectionScreen();
                backgroundSelectionScreen.backgroundSelectionScene(primaryStage,mediaPlayer);

            } else if (event.getCode() == KeyCode.ESCAPE) {
                primaryStage.close();
            }
        });

        primaryStage.setScene(scene);


        primaryStage.show();
        enterFadeTransition.play();
        exitFadeTransition.play();


    }

    /**
     Creates a Text object with the specified message and font size.
     @param message The text message to be displayed.
     @param fontSize The font size of the text.
     @return The Text object with the specified message and font size.
     */
    private Text createText(String message, double fontSize) {
        Text text = new Text(message);
        text.setFont(Font.font("Arial", FontWeight.BOLD, fontSize*scale));
        return text;
    }

    /**
     Creates a FadeTransition object with the specified parameters.
     @param text The Text object to apply the fade transition.
     @param fromValue The starting opacity value of the text.
     @param toValue The ending opacity value of the text.
     @param duration The duration of the fade transition.
     @return The FadeTransition object with the specified parameters.
     */
    public static FadeTransition createFadeTransition(Text text, double fromValue, double toValue, Duration duration) {
        FadeTransition fadeTransition = new FadeTransition(duration, text);
        fadeTransition.setFromValue(fromValue);
        fadeTransition.setToValue(toValue);
        return fadeTransition;
    }
}
