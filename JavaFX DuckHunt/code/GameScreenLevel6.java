import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicInteger;

public class GameScreenLevel6 extends GameScreen {




    int ammo;

    boolean lastAmmo;
    public GameScreenLevel6(ImageView backgroundView, ImageView foregroundImageView, ImageView crosshairView, Stage primaryStage, int level, int ammoNumber, MediaPlayer mediaPlayer) {
        super(backgroundView, foregroundImageView, crosshairView, primaryStage, level, ammoNumber);



        this.ammo=ammoNumber;

        ammoText=desiredText("Ammo Left: "+ammo,-(screenHeight/2.06),6);
        ammoText.setTranslateX(screenWidth/2.6);
        root.getChildren().add(ammoText);

        Group duck1=horizontalFlyDuck("assets/duck_blue",foregroundImageView,root,-(screenHeight/3),(screenWidth/2.17),-(screenWidth/2.17),scene,true);

        Group duck2=createHorizontalAndVerticalDuck("assets/duck_black");
        Timeline animationDuck2=horizontalAndVerticalFly(root,foregroundImageView,"assets/duck_black",-(screenHeight/3),(screenWidth/5),duck2);


        Group duck3=createHorizontalAndVerticalDuck("assets/duck_red");
        Timeline animationDuck3=horizontalAndVerticalFly(root,foregroundImageView,"assets/duck_red",-(screenHeight/6),(screenWidth/6.25),duck3);

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
                duckDeathEffect(root,"assets/duck_blue",foregroundImageView,duck1,playAgainText,exitText);
                duck1.setTranslateY(3000);
            }
            if (checkDeath(duck2.getTranslateX(),duck2.getTranslateY(),shootXAxis,shootYAxis)){
                duckDeathEffect(root,"assets/duck_black",foregroundImageView,duck2,playAgainText,exitText);
                animationDuck2.stop();
                duck2.setTranslateY(3000);
            }
            if (checkDeath(duck3.getTranslateX(),duck3.getTranslateY(),shootXAxis,shootYAxis)){
                duckDeathEffect(root,"assets/duck_red",foregroundImageView,duck3,playAgainText,exitText);
                animationDuck3.stop();
                duck3.setTranslateY(3000);
            }

            if (root.getChildren().isEmpty() || root.getChildren().stream().noneMatch(node -> node instanceof Group)) {
                lastAmmo=true;
                lastMusic.play();
                animationText(playAgainText);
                animationText(exitText);
                root.getChildren().add(playAgainText);
                root.getChildren().add(exitText);
                root.getChildren().add(completeText);
                scene.setOnMouseClicked(null);
                scene.setOnKeyPressed(event1 -> {
                    if (event1.getCode() == KeyCode.ENTER) {
                        lastMusic.stop();
                        new GameScreenLevel1(backgroundView,foregroundImageView,crosshairView,primaryStage,1,3,mediaPlayer);
                    } else if (event1.getCode()==KeyCode.ESCAPE) {
                        lastMusic.stop();
                        TitleScreen titleScreen=new TitleScreen();
                        titleScreen.titleScene(primaryStage,mediaPlayer);
                    }
                });
            }
            if (!lastAmmo){
                noMoreAmmoSituation(root,scene,playAgainText,ammoNumber,exitText,gameOverText,primaryStage,mediaPlayer,backgroundView,crosshairView,foregroundImageView,ordinal);
            }

        });







    }
}
