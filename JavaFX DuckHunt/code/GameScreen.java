import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class GameScreen extends Screen {

    Text levelText;
    Text ammoText;
    Text gameOverText;
    Text exitText;
    Text playAgainText;
    Text youWinText;
    Text nextLevelText;

    Text completeText;
     MediaPlayer gunMusic;

     MediaPlayer duckDeathMusic;

     MediaPlayer gameOverMusic;

     MediaPlayer nextLevelMusic;

     MediaPlayer lastMusic;
    double screenWidth;
    double screenHeight;
    double imageHeight;
    double imageWidth;





    StackPane root;
    Scene scene;

    /**
     * Constructs a GameScreen object with the specified parameters.
     * @param backgroundView The ImageView representing the background of the game screen.
     * @param foregroundImageView The ImageView representing the foreground of the game screen.
     * @param crosshairView The ImageView representing the crosshair of the game screen.
     * @param primaryStage The primary Stage of the application.
     * @param level The current level of the game.
     * @param ammoNumber The number of ammunition available to the player.
     */

    public GameScreen(ImageView backgroundView,ImageView foregroundImageView, ImageView crosshairView, Stage primaryStage,int level,int ammoNumber) {
         screenWidth=backgroundView.getFitWidth();
         screenHeight=backgroundView.getFitHeight();



         root = new StackPane();
         root.getChildren().add(backgroundView);


        gameOverText=desiredText("GAME OVER!",-(screenHeight/15),11.6);
        completeText=desiredText("You have completed the game!",-(screenHeight/15),10);
        exitText=desiredText("Press ESC to exit",screenHeight/37.5,11.6);
        playAgainText=desiredText("Press ENTER to play again",(-screenHeight/50),11.6);
        youWinText=desiredText("YOU WIN!",-(screenHeight/15),11.6);
        levelText=desiredText("Level "+level+"/6",-(screenHeight/2.06),6);
        nextLevelText=desiredText("Press ENTER to play next level",-(screenHeight/50),10);

        gunMusic=setMusic("assets/effects/Gunshot.mp3");
        duckDeathMusic=setMusic("assets/effects/DuckFalls.mp3");
        gameOverMusic=setMusic("assets/effects/GameOver.mp3");
        nextLevelMusic=setMusic("assets/effects/LevelCompleted.mp3");
        lastMusic=setMusic("assets/effects/GameCompleted.mp3");

        root.getChildren().add(levelText);


        scene = new Scene(root, screenWidth, screenHeight);
        scene.setOnMouseMoved(event -> {
            double mouseX = event.getSceneX();
            double mouseY = event.getSceneY();

            if (mouseX >= 0 && mouseX <= scene.getWidth() && mouseY >= 0 && mouseY <= scene.getHeight()) {
                Image image = crosshairView.getImage();
                ImageCursor cursor = new ImageCursor(image);
                scene.setCursor(cursor);
            }


        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     Applies a fade-in and fade-out animation to the provided Text node.
     @param text The Text node to animate.
     */
    public  void animationText(Text text){
        FadeTransition animationText=TitleScreen.createFadeTransition(text,1,0, Duration.seconds(1.5));
        animationText.setCycleCount(Animation.INDEFINITE);
        animationText.play();
    }

    /**
     * Creates a Text node with the specified message, font size, and position on the Y-axis.
     * @param message The text message to be displayed.
     * @param yAxis The position of the text on the Y-axis.
     * @param fontSize The font size of the text.
     * @return The Text node with the desired properties.
     */
    public Text desiredText(String message, double yAxis, double fontSize){
        Text text = createText(message, fontSize);
        text.setFill(Color.ORANGE);
        text.setTranslateY(yAxis);
        return text;

    }

    /**
     * Creates a Text node with the specified message and font size.
     * @param message The text message to be displayed.
     * @param fontSize The font size of the text.
     * @return The Text node with the desired properties.
      */

    private    Text createText(String message, double fontSize) {
        Text text = new Text(message);
        text.setFont(Font.font("Arial", FontWeight.BOLD, fontSize*scale));
        return text;
    }

    /**
     Creates a group of flying ducks that move horizontally across the screen.

     @param pathName The path to the images of the flying ducks.

     @param foregroundImageView The ImageView representing the foreground image.

     @param root The StackPane root of the scene.

     @param setTranslateY The Y-coordinate translation of the duck group.

     @param fromX The starting X-coordinate of the duck's movement.

     @param toX The ending X-coordinate of the duck's movement.

     @param scene The Scene object.

     @param reverse Determines whether the duck group should be reversed.

     @return The Group object representing the flying ducks.
     */
    public  Group horizontalFlyDuck(String pathName, ImageView foregroundImageView, StackPane root, double setTranslateY, double fromX, double toX, Scene scene, boolean reverse){
        ArrayList<ImageView> imageViewDuckArray=createDesiredDuckList(pathName);

        Group duck=new Group(imageViewDuckArray.get(3));
        if (reverse){
            duck.setScaleX(-duck.getScaleX());
        }

        duck.setTranslateY(setTranslateY);
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(400), t -> duck.getChildren().setAll(imageViewDuckArray.get(3))),
                new KeyFrame(Duration.millis(800), t -> duck.getChildren().setAll(imageViewDuckArray.get(4))),
                new KeyFrame(Duration.millis(1200), t -> duck.getChildren().setAll(imageViewDuckArray.get(5)))
        );
        timeline.play();
        if (root.getChildren().contains(foregroundImageView)) {
            root.getChildren().remove(foregroundImageView);
        }
        root.getChildren().add(duck);

        newStartWalking(fromX,toX,duck);
        return duck;
    }

    /**
     Starts the walking animation for the duck group, moving it horizontally from the starting X-coordinate to the ending X-coordinate.

     @param fromX The starting X-coordinate of the duck's movement.

     @param toX The ending X-coordinate of the duck's movement.

     @param duck The Group object representing the duck group.
     */

    public  void newStartWalking(double fromX,double toX,Group duck) {
        TranslateTransition walk = new TranslateTransition(Duration.seconds(4), duck);
        walk.setFromX(fromX);
        walk.setToX(toX);


        walk.setOnFinished(event -> {
            newReverseWalking(fromX,toX,duck);
        });
        walk.play();
    }

    /**
     Reverses the walking animation of a Group object (duck) by scaling and translating it.

     The animation starts from the specified 'fromX' coordinate and ends at the specified 'toX' coordinate.

     @param fromX The starting X-coordinate of the animation.

     @param toX The ending X-coordinate of the animation.

     @param duck The Group object (duck) to be animated.
     */

    public void newReverseWalking(double fromX,double toX,Group duck) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.001), duck);
        scaleTransition.setToX(-duck.getScaleX());
        scaleTransition.setOnFinished(event -> {
            TranslateTransition reverseTransition = new TranslateTransition(Duration.seconds(4), duck);
            reverseTransition.setFromX(toX);
            reverseTransition.setToX(fromX);


            reverseTransition.setOnFinished(event2 -> {
                ScaleTransition restoreScale = new ScaleTransition(Duration.seconds(0.001), duck);
                restoreScale.setToX(-duck.getScaleX());
                restoreScale.play();
                newStartWalking(fromX,toX,duck);
            });
            reverseTransition.play();
        });
        scaleTransition.play();
    }

    /**
     Applies a death effect animation to a duck object by removing it from the scene, playing a death sound,

     and displaying fading and translation animations with different images.

     @param root The StackPane that contains the scene elements.

     @param pathName The path name of the image directory.

     @param foregroundImageView The ImageView representing the foreground.

     @param duck The Group object (duck) to apply the death effect to.

     @param text1 The Text object 1.

     @param text2 The Text object 2.
     */
    public void duckDeathEffect(StackPane root,String pathName,ImageView foregroundImageView,Group duck,Text text1,Text text2){
        root.getChildren().remove(duck);
        duckDeathMusic.stop();
        duckDeathMusic.play();
        double xAxis=duck.getTranslateX();
        double yAxis=duck.getTranslateY();

        Image newImageImage=new Image(pathName+"/7.png");
        Image lastImageImage=new Image(pathName+"/8.png");
        ImageView lastImage = new ImageView(lastImageImage);
        ImageView newImage=new ImageView(newImageImage);

        if (duck.getScaleX()==-1){
            newImage.setScaleX(-newImage.getScaleX());
            lastImage.setScaleX(-lastImage.getScaleX());
        }
        double newImageHeight=newImageImage.getHeight()*scale;
        double lastImageHeight=lastImageImage.getHeight()*scale;

        double newImageWidth=newImageImage.getWidth()*scale;
        double lastImageWidth=lastImageImage.getWidth()*scale;


        newImage.setFitWidth(newImageWidth);
        newImage.setFitHeight(newImageHeight);


        newImage.setTranslateX(xAxis);
        newImage.setTranslateY(yAxis);
        root.getChildren().add(newImage);
        foregroundImageView.toFront();
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.15), newImage);
        fadeTransition.setOnFinished(event1 -> {
            root.getChildren().remove(newImage);


            lastImage.setFitHeight(lastImageHeight);
            lastImage.setFitWidth(lastImageWidth);

            lastImage.setTranslateY(yAxis);
            lastImage.setTranslateX(xAxis);


            TranslateTransition transition = new TranslateTransition(Duration.seconds(2), lastImage);
            transition.setToY(screenHeight/2);
            transition.play();

            root.getChildren().add(lastImage);
            foregroundImageView.toFront();
            text1.toFront();
            text2.toFront();
            if (root.getChildren().contains(completeText)){
                completeText.toFront();
            }


            transition.setOnFinished(event2 ->{
                root.getChildren().remove(lastImage);
            });
        });
        fadeTransition.play();

    }

    /**
     Checks if a shot hit the duck and caused its death based on the positions of the duck and the shot.

     @param duckXAxis The X-axis coordinate of the duck.

     @param duckYAxis The Y-axis coordinate of the duck.

     @param shootXAxis The X-axis coordinate of the shot.

     @param shootYAxis The Y-axis coordinate of the shot.

     @return true if the shot hit the duck, false otherwise.
     */

    public boolean checkDeath(double duckXAxis,double duckYAxis,double shootXAxis,double shootYAxis){
        return duckXAxis + ((screenWidth/2)-(imageWidth/2.5)) < shootXAxis && duckXAxis + ((screenWidth/2)+(imageWidth/2.5)) > shootXAxis && duckYAxis + ((screenHeight/2)-(imageHeight/2.5)) < shootYAxis && duckYAxis+((screenHeight/2)+(imageHeight/2.5)) > shootYAxis;
    }


    /**
     Handles the situation when there is no more ammo left in the game. It displays the game over screen,
     including the "Play Again" and "Exit" options, and listens for keyboard input to restart or exit the game.
     @param root The StackPane that contains the scene elements.
     @param scene The Scene object.
     @param playAgain The Text object for the "Play Again" option.
     @param ammoNumber The total number of available ammo.
     @param exit The Text object for the "Exit" option.
     @param gameOver The Text object for the "Game Over" message.
     @param primaryStage The primary Stage of the application.
     @param mediaPlayer The MediaPlayer for game audio.
     @param backgroundView The ImageView representing the background.
     @param crosshairView The ImageView representing the crosshair.
     @param foregroundImageView The ImageView representing the foreground.
     @param ordinal The AtomicInteger object to keep track of the current ammo count.
     */
   public  void noMoreAmmoSituation(StackPane root, Scene scene, Text playAgain, int ammoNumber, Text exit, Text gameOver, Stage primaryStage, MediaPlayer mediaPlayer, ImageView backgroundView, ImageView crosshairView, ImageView foregroundImageView, AtomicInteger ordinal){
       if (ordinal.get()==ammoNumber){
           scene.setOnMouseClicked(null);
           gameOverMusic.play();
           animationText(playAgain);
           animationText(exit);
           root.getChildren().add(gameOver);
           root.getChildren().add(playAgain);
           root.getChildren().add(exit);
           scene.setOnKeyPressed(event1 -> {
               if (event1.getCode() == KeyCode.ENTER) {
                   gameOverMusic.stop();
                   new GameScreenLevel1(backgroundView,foregroundImageView,crosshairView,primaryStage,1,3,mediaPlayer);
               }
               if (event1.getCode()==KeyCode.ESCAPE){
                   gameOverMusic.stop();
                   TitleScreen titleScreen=new TitleScreen();
                   titleScreen.titleScene(primaryStage,mediaPlayer);
               }
           });
       }
   }

    /**
     Creates a Group object containing a single ImageView representing a duck in both horizontal and vertical orientations.
     @param pathName The path name of the image directory.
     @return The Group object containing the duck ImageView.
     */


   public  Group createHorizontalAndVerticalDuck(String pathName){
       ArrayList<ImageView> imageViewDuckArray=createDesiredDuckList(pathName);
       return new Group(imageViewDuckArray.get(0));
   }

    /**
     *Creates a Timeline animation for a duck to fly in horizontal and vertical directions.

     @param root The StackPane that contains the scene elements.

     @param foregroundImageView The ImageView representing the foreground.

     @param pathName The path name of the image directory.

     @param initialY The initial Y-axis position of the duck.

     @param initialX The initial X-axis position of the duck.

     @param duck The Group object representing the duck.

     @return The created Timeline animation.
     */

   public  Timeline horizontalAndVerticalFly(StackPane root,ImageView foregroundImageView,String pathName,double initialY,double initialX,Group duck){
       ArrayList<ImageView> imageViewDuckArray=createDesiredDuckList(pathName);
       duck.setTranslateY(initialY);
       duck.setTranslateX(initialX);

       Timeline timeline = new Timeline();
       timeline.setCycleCount(Animation.INDEFINITE);
       timeline.getKeyFrames().addAll(
               new KeyFrame(Duration.millis(300), t -> duck.getChildren().setAll(imageViewDuckArray.get(0))),
               new KeyFrame(Duration.millis(600), t -> duck.getChildren().setAll(imageViewDuckArray.get(1))),
               new KeyFrame(Duration.millis(800), t -> duck.getChildren().setAll(imageViewDuckArray.get(2)))
       );
       timeline.play();

       root.getChildren().add(duck);


        double[] xAxis = {duck.getTranslateX()};
        double[] yAxis = {duck.getTranslateY()};

       final double[] xVelocity = {5*scale};
       final double[] yVelocity = {-5*scale};


       Timeline animation = new Timeline(
               new KeyFrame(Duration.millis(100), e -> {
                   xAxis[0] += xVelocity[0];
                   yAxis[0] += yVelocity[0];


                   if (xAxis[0]+(imageWidth/2) >= (screenWidth/2)|| xAxis[0] -(imageWidth/2) <= -(screenWidth/2)) {
                       xVelocity[0] *= -1;
                       duck.setScaleX(-duck.getScaleX());
                   }
                   if (yAxis[0] + (imageHeight/2) >= (screenHeight/2) || yAxis[0] -(imageHeight/2) <= -(screenHeight/2)){

                       yVelocity[0] *=-1;
                       duck.setScaleY(-duck.getScaleY());
                   }
                   duck.setTranslateY(yAxis[0]);
                   duck.setTranslateX(xAxis[0]);
               }));
       animation.setCycleCount(Timeline.INDEFINITE);
       animation.play();
       return animation;

   }

    /**
     Creates an ArrayList of ImageView objects representing different ducks from the given path name.

     @param pathName The path name of the image directory.

     @return The ArrayList of ImageView objects representing ducks.
     */


    public  ArrayList<ImageView> createDesiredDuckList(String pathName){
        File pngDucks = new File(pathName);
        File[] ducksArray = pngDucks.listFiles();
        Arrays.sort(ducksArray);
        ArrayList<ImageView> imageViewDuckArray=new ArrayList<>();

        for (File eachDuck:ducksArray){
            Image image = new Image(eachDuck.toURI().toString());
             imageHeight=image.getHeight()*scale;
             imageWidth=image.getWidth()*scale;
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(imageWidth);
            imageView.setFitHeight(imageHeight);
            imageViewDuckArray.add(imageView);
        }
        return  imageViewDuckArray;
    }








}
