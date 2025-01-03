import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;



public class Screen extends DuckHunt {

    /**
     Creates a MediaPlayer object and sets the music from the specified path.
     @param path The path of the music file.
     @return The MediaPlayer object with the specified music.
     */
    public MediaPlayer setMusic(String path){
        Media media = new Media(new File(path).toURI().toString());
        MediaPlayer music = new MediaPlayer(media);
        music.setVolume(volume);
        return music;
    }
}
