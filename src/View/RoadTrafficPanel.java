package View;

import Controller.CarContextMenuEventHandler;
import Controller.MouseEventHandler;
import Controller.Simulation.Observable;
import Controller.Simulation.Observer;
import Model.RoadTraffic;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;


public class RoadTrafficPanel extends Region implements Observer {
    final static int CELLSIZE = 34;

    private RoadTraffic roadTraffic;

    private Image light;
    private Image tire;
    private List<Image> carImages;

    private Canvas canvas;
    private GraphicsContext gc;
    private SimulatorView view;

    // Konstruktor
    public RoadTrafficPanel(SimulatorView view, RoadTraffic roadTraffic) {
        this.roadTraffic = roadTraffic;
        this.view = view;
        this.testRoadTraffic();
        this.roadTraffic.addObserver(this);
        this.loadImages();
        this.init();
        MouseEventHandler mouseEventHandler = new MouseEventHandler(view, roadTraffic);
        CarContextMenuEventHandler carContextMenuEventHandler = new CarContextMenuEventHandler(roadTraffic, this);
        canvas.addEventHandler(MouseEvent.ANY, mouseEventHandler);
        canvas.addEventHandler(ContextMenuEvent.ANY, carContextMenuEventHandler);
    }

    // Returnt die Spielfeldbreite
    public double getRoadTrafficWidth() {
        return roadTraffic.getActualColumns() * RoadTrafficPanel.CELLSIZE + 2;
    }

    // Returnt die Spielfeldhöhe
    public double getRoadTrafficHeigh() {
        return roadTraffic.getActualRows() * RoadTrafficPanel.CELLSIZE + 2;
    }

    // Bilder werden geladen
    private void loadImages() {
        this.tire = new Image(getClass().getResource("Resources/reifen24.png").toString());
        this.carImages = new ArrayList<>();
        for (int i = 0; i <= 3; i++) {
            this.carImages.add(new Image(getClass().getResource("Resources/auto30" + i + ".png").toString()));
        }
        this.light = new Image(getClass().getResource("Resources/ampel24.png").toString());
    }

    // Canvas wird erstellt und mit der paint() Methode gezeichnet
    private void init() {
        this.canvas = new Canvas();
        this.gc = this.canvas.getGraphicsContext2D();
        this.paint();
        getChildren().add(this.canvas);
    }

    // Diese Methode zeichnet das Spielfeld
    private void paint() {
        canvas.setHeight(getRoadTrafficHeigh());
        canvas.setWidth(getRoadTrafficWidth());

        gc.clearRect(0, 0, 10000, 10000);
        gc.setFill(Color.rgb(112, 138, 183));
      //  gc.fillRect(0, 0, 2000, 2000);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        double row = (canvas.getHeight() - roadTraffic.getActualRows() * CELLSIZE) / 2;
        double column = (canvas.getWidth() - roadTraffic.getActualColumns() * CELLSIZE) / 2;

        gc.fillRect(row, column, roadTraffic.getActualColumns() * CELLSIZE, roadTraffic.getActualRows() * CELLSIZE);
        for (int z = 0; z < roadTraffic.getTiles().length; z++) {
            for (int s = 0; s < roadTraffic.getTiles()[0].length; s++) {
                gc.strokeRect(column + s * CELLSIZE, row + z * CELLSIZE, CELLSIZE, CELLSIZE);
            }
        }
        paintTires();
        paintCar();
        paintLights();
    }

    private void paintLights() {
        for (int z = 0; z < roadTraffic.getActualRows(); z++) {
            for (int s = 0; s < roadTraffic.getActualColumns(); s++) {
                if (roadTraffic.isLightOnPos(z, s)) {
                    gc.drawImage(light, 3 + (s * 34), 3 + (z * 34), 32, 32);
                }
            }
        }

    }

    private void paintCar() {
        for (int z = 0; z < roadTraffic.getActualRows(); z++) {
            for (int s = 0; s < roadTraffic.getActualColumns(); s++) {
                if (roadTraffic.getCarRow() == z && roadTraffic.getCarColumns() == s) {
                    switch (roadTraffic.getCarDirection()) {
                        case 0:
                            gc.drawImage(carImages.get(0), 4 + (s * 34), 4 + (z * 34));
                            break;
                        case 1:
                            gc.drawImage(carImages.get(1), 4 + (s * 34), 4 + (z * 34));
                            break;
                        case 2:
                            gc.drawImage(carImages.get(2), 4 + (s * 34), 4 + (z * 34));
                            break;
                        case 3:
                            gc.drawImage(carImages.get(3), 4 + (s * 34), 4 + (z * 34));
                            break;
                    }
                }
            }
        }
    }

    private void paintTires() {
        for (int z = 0; z < roadTraffic.getActualRows(); z++) {
            for (int s = 0; s < roadTraffic.getActualColumns(); s++) {
                if (roadTraffic.getTiresAtPos(z, s) > 0) {
                    gc.drawImage(tire, 2 + (s * 34), 2 + (z * 34));
                    // Die Idee mit dem Text stammt von Patrick Tonne
                    if (roadTraffic.getTiresAtPos(z,s) < 10) {
                    gc.strokeText(String.valueOf(roadTraffic.getTiresAtPos(z, s)), 2 + (s * 34) + 24, 2 + (z * 34) + 30);
                    }
                    else {
                        gc.strokeText(String.valueOf(roadTraffic.getTiresAtPos(z, s)), 2 + (s * 34) + 19, 2 + (z * 34) + 30); }
                    }
                }
            }
        }


    public void update(Observable o) {
        if (Platform.isFxApplicationThread()) {
            paint();
            view.center(view.getSc().getViewportBounds(),this.canvas);
        } else {
            Platform.runLater(this::paint);
        }
    }


    // Füllen des Spielfeldes
    private void testRoadTraffic() {
        roadTraffic.setLight(1, 9);
        roadTraffic.setLight(9, 5);
        roadTraffic.addTires(2, 2, 3);
        roadTraffic.addTires(5, 3, 1);
        roadTraffic.addTires(1, 8, 1);
        roadTraffic.addTires(2, 6, 2);
        roadTraffic.addTires(7, 4, 5);
    }

    public Canvas getCanvas() {
        return canvas;
    }

}
