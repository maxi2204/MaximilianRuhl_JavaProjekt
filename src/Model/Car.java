package Model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Car {

    private RoadTraffic roadTraffic;
    String file = "death.wav";
    final Media Medium = new Media(new File(file).toURI().toString());
    MediaPlayer mediaPlayer;

    public Car() {
    }

    @Invisible
    public void main() {

    }
    public void vor() {
        try {
        this.roadTraffic.ago(); }
        catch (NotInTrafficException e) {
            this.mediaPlayer = new MediaPlayer(Medium);
            this.mediaPlayer.play();
        }
    }

    public void nimm() {
        this.roadTraffic.take();
    }

    public void gib() {
        this.roadTraffic.put();
    }

    public void linksUm() {
        this.roadTraffic.leftAround();
    }

    public boolean vornFrei() {
        return this.roadTraffic.isFree();
    }

    public boolean kofferraumLeer() {
        return this.roadTraffic.trunkEmpty();
    }

    public boolean reifenDa() {
        return this.roadTraffic.tiresAtCarPos();
    }

    @Invisible
    public RoadTraffic getRoadTraffic() {
        return roadTraffic;
    }
    @Invisible
    public void setRoadTraffic(RoadTraffic roadTraffic) {
        this.roadTraffic = roadTraffic;
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface  Invisible {

    }
}
