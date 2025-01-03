import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicInteger;

public class GameScreenLevel3 extends GameScreen {

    int ammo;
    boolean lastAmmo;

    public GameScreenLevel3(ImageView backgroundView, ImageView foregroundImageView, ImageView crosshairView, Stage primaryStage, int level, int ammoNumber, MediaPlayer mediaPlayer) {
        super(backgroundView, foregroundImageView, crosshairView, primaryStage, level, ammoNumber);

        this.ammo=ammoNumber;

        ammoText=desiredText("Ammo Left: "+ammo,-(screenHeight/2.06),6);
        ammoText.setTranslateX(screenWidth/2.4);
        root.getChildren().add(ammoText);


        Group duck1=horizontalFlyDuck("assets/duck_blue",foregroundImageView,root,-(screenHeight/3),-(screenWidth/2.17),(screenWidth/2.17),scene,false);
        Group duck2=horizontalFlyDuck("assets/duck_red",foregroundImageView,root,-(screenHeight/6),(screenWidth/2.17),-(screenWidth/2.17),scene,true);
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

            double duck2XAxis=duck2.getTranslateX();
            double duck2YAxis=duck2.getTranslateY();

            double shootXAxis=event.getX();
            double shootYAxis=event.getY();

            if (checkDeath(duck1XAxis,duck1YAxis,shootXAxis,shootYAxis)){
                duckDeathEffect(root,"assets/duck_blue",foregroundImageView,duck1,nextLevelText,youWinText);
                duck1.setTranslateY(3000);
            }
            if (checkDeath(duck2XAxis,duck2YAxis,shootXAxis,shootYAxis)){
                duckDeathEffect(root,"assets/duck_red",foregroundImageView,duck2,nextLevelText,youWinText);
                duck2.setTranslateY(3000);
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
                        new GameScreenLevel4(backgroundView,foregroundImageView,crosshairView,primaryStage,4,6,mediaPlayer);
                    }
                });
            }
            if (!lastAmmo){
                noMoreAmmoSituation(root,scene,playAgainText,ammoNumber,exitText,gameOverText,primaryStage,mediaPlayer,backgroundView,crosshairView,foregroundImageView,ordinal);
            }

        });

    }
}
