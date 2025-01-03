import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.util.concurrent.atomic.AtomicInteger;

public class GameScreenLevel1 extends GameScreen {

    int ammo;
    boolean lastAmmo;


    /**
     Creates a GameScreenLevel1 object with the specified parameters.

     @param backgroundView The ImageView representing the background image.

     @param foregroundImageView The ImageView representing the foreground image.

     @param crosshairView The ImageView representing the crosshair image.

     @param primaryStage The primary stage of the JavaFX application.

     @param level The level number of the game screen.

     @param ammoNumber The initial number of ammo.

     @param mediaPlayer The MediaPlayer for playing music.
     */
    public GameScreenLevel1(ImageView backgroundView, ImageView foregroundImageView, ImageView crosshairView, Stage primaryStage, int level, int ammoNumber, MediaPlayer mediaPlayer) {
        super(backgroundView,foregroundImageView, crosshairView,primaryStage,level,ammoNumber);
        this.ammo=ammoNumber;
        ammoText=desiredText("Ammo Left: "+ammo,-(screenHeight/2.06),6);
        ammoText.setTranslateX(screenWidth/2.4);
        root.getChildren().add(ammoText);
        Group duck1=horizontalFlyDuck("assets/duck_blue",foregroundImageView,root,-(screenHeight/3),-(screenWidth/2.17),(screenWidth/2.17),scene,false);
        root.getChildren().add(foregroundImageView);
        AtomicInteger ordinal = new AtomicInteger(0);
        scene.setOnMouseClicked(event -> {
            gunMusic.stop();
            gunMusic.play();
            ammo--;
            ammoText.setText("Ammo Left: "+ammo);

            ordinal.getAndIncrement();
            double duck1XAxis=duck1.getTranslateX();
            double duck1YAxis=duck1.getTranslateY();
            double shoot1XAxis=event.getX();
            double shoot1YAxis=event.getY();
           if (checkDeath(duck1XAxis,duck1YAxis,shoot1XAxis,shoot1YAxis)){
               duckDeathMusic.play();
               duckDeathEffect(root,"assets/duck_blue",foregroundImageView,duck1,nextLevelText,youWinText);
               duck1.setTranslateY(3000);
           }
            if (root.getChildren().isEmpty() || root.getChildren().stream().noneMatch(node -> node instanceof Group)) {
                lastAmmo=true;
                nextLevelMusic.play();
                animationText(nextLevelText);
                root.getChildren().add(nextLevelText);
                root.getChildren().add(youWinText);
                scene.setOnMouseClicked(null);
                scene.setOnKeyPressed(event1 -> {
                    if (event1.getCode() == KeyCode.ENTER) {
                        nextLevelMusic.stop();

                        new GameScreenLevel2(backgroundView,foregroundImageView,crosshairView,primaryStage,2,3,mediaPlayer);
                    }
                });
            }
            if (!lastAmmo){
                noMoreAmmoSituation(root,scene,playAgainText,ammoNumber,exitText,gameOverText,primaryStage,mediaPlayer,backgroundView,crosshairView,foregroundImageView,ordinal);


            }

        });
    }

}

