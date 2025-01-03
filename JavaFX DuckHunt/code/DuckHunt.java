import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;

public class DuckHunt extends Application {

    double volume=0.030;

    double scale=4;

    private MediaPlayer mediaPlayer;
    @Override
    public void start(Stage primaryStage) {
        TitleScreen titleScreen=new TitleScreen();
        Media media = new Media(new File("assets/effects/Title.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(volume);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        titleScreen.titleScene(primaryStage,mediaPlayer);






    }

    public static void main(String[] args) {
        launch(args);
    }
}
