import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicInteger;

public class GameScreenLevel5 extends GameScreen {

    int ammo;
    boolean lastAmmo;
    public GameScreenLevel5(ImageView backgroundView, ImageView foregroundImageView, ImageView crosshairView, Stage primaryStage, int level, int ammoNumber, MediaPlayer mediaPlayer) {
        super(backgroundView, foregroundImageView, crosshairView, primaryStage, level, ammoNumber);


        this.ammo=ammoNumber;

        ammoText=desiredText("Ammo Left: "+ammo,-(screenHeight/2.06),6);
        ammoText.setTranslateX(screenWidth/2.4);
        root.getChildren().add(ammoText);

        Group duck1=createHorizontalAndVerticalDuck("assets/duck_black");
        Timeline animationDuck1=horizontalAndVerticalFly(root,foregroundImageView,"assets/duck_black",-200,100,duck1);

        Group duck2=horizontalFlyDuck("assets/duck_blue",foregroundImageView,root,-(screenHeight/3),-(screenWidth/2.17),screenWidth/2.17,scene,false);
        Group duck3=horizontalFlyDuck("assets/duck_red",foregroundImageView,root,-(screenHeight/6),screenWidth/2.17,-(screenWidth/2.17),scene,true);

        root.getChildren().add(foregroundImageView);

        AtomicInteger ordinal = new AtomicInteger(0);
        scene.setOnMouseClicked(event -> {
            gunMusic.stop();
            gunMusic.play();
            ammo--;
            ordinal.getAndIncrement();
            ammoText.setText("Ammo Left: " + ammo);

            double shootXAxis = event.getX();
            double shootYAxis = event.getY();
            if (checkDeath(duck1.getTranslateX(),duck1.getTranslateY(),shootXAxis,shootYAxis)){
                duckDeathEffect(root,"assets/duck_black",foregroundImageView,duck1,nextLevelText,youWinText);
                animationDuck1.stop();
                duck1.setTranslateY(3000);
            }
            if (checkDeath(duck2.getTranslateX(),duck2.getTranslateY(),shootXAxis,shootYAxis)){
                duckDeathEffect(root,"assets/duck_blue",foregroundImageView,duck2,nextLevelText,youWinText);
                duck2.setTranslateY(3000);
            }
            if (checkDeath(duck3.getTranslateX(),duck3.getTranslateY(),shootXAxis,shootYAxis)){
                duckDeathEffect(root,"assets/duck_red",foregroundImageView,duck3,nextLevelText,youWinText);
                duck3.setTranslateY(3000);
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
                        new GameScreenLevel6(backgroundView,foregroundImageView,crosshairView,primaryStage,6,9,mediaPlayer);
                    }
                });
            }
            if (!lastAmmo){
                noMoreAmmoSituation(root,scene,playAgainText,ammoNumber,exitText,gameOverText,primaryStage,mediaPlayer,backgroundView,crosshairView,foregroundImageView,ordinal);
            }


        });
    }
}
