import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.util.concurrent.atomic.AtomicInteger;

public class GameScreenLevel2 extends GameScreen {


    int ammo;
    boolean lastAmmo;



    public GameScreenLevel2(ImageView backgroundView, ImageView foregroundImageView, ImageView crosshairView, Stage primaryStage, int level, int ammoNumber, MediaPlayer mediaPlayer) {
        super(backgroundView, foregroundImageView, crosshairView, primaryStage, level, ammoNumber);

        this.ammo=ammoNumber;

        ammoText=desiredText("Ammo Left: "+ammo,-(screenHeight/2.06),6);
        ammoText.setTranslateX(screenWidth/2.4);
        root.getChildren().add(ammoText);


        Group duck=createHorizontalAndVerticalDuck("assets/duck_black");
        Timeline animation=horizontalAndVerticalFly(root,foregroundImageView,"assets/duck_black",-(screenHeight/3),screenWidth/5,duck);
        root.getChildren().add(foregroundImageView);

        AtomicInteger ordinal = new AtomicInteger(0);
        scene.setOnMouseClicked(event -> {
            gunMusic.stop();
            gunMusic.play();
            ordinal.getAndIncrement();
            ammo--;
            ammoText.setText("Ammo Left: "+ammo);
            double shoot1XAxis=event.getX();
            double shoot1YAxis=event.getY();
            if (checkDeath(duck.getTranslateX(),duck.getTranslateY(),shoot1XAxis,shoot1YAxis)){
                duckDeathMusic.play();
                duckDeathEffect(root,"assets/duck_black",foregroundImageView,duck,nextLevelText,youWinText);
                animation.stop();
                duck.setTranslateY(3000);
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
                        new GameScreenLevel3(backgroundView,foregroundImageView,crosshairView,primaryStage,3,6,mediaPlayer);
                    }
                });
            }
            if (!lastAmmo){
                noMoreAmmoSituation(root,scene,playAgainText,ammoNumber,exitText,gameOverText,primaryStage,mediaPlayer,backgroundView,crosshairView,foregroundImageView,ordinal);
            }

        });


    }


}
