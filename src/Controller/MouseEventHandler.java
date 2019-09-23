package Controller;

import Model.RoadTraffic;
import View.SimulatorView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class MouseEventHandler implements EventHandler<MouseEvent> {

    private SimulatorView view;
    private RoadTraffic roadTraffic;

    public MouseEventHandler(SimulatorView view, RoadTraffic roadTraffic) {
        this.view = view;
        this.roadTraffic = roadTraffic;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
            mousePress(event);
        }
        if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {
            mouseDrag(event);
        }
        // Keine Funktion
        if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
            mouseReleased(event);
        }

    }

    private void mouseDrag(MouseEvent event) {
        int newColumn = ((int)event.getX()/34);
        int newRow = ((int)event.getY()/34);
        if (view.getBtnNewCar().isSelected()) {
            if (!(roadTraffic.isLightOnPos(newRow, newColumn))) {
                roadTraffic.setCar(newRow, newColumn);
            }
        }
    }

    private void mousePress(MouseEvent event) {
        int newColumn = ((int)event.getX()/34);
        int newRow = ((int)event.getY()/34);
        if (view.getBtnNewCar().isSelected()) {
            if (!(roadTraffic.isLightOnPos(newRow, newColumn))) {
                roadTraffic.setCar(newRow, newColumn);
            }
        }
        else if (view.getBtnLight().isSelected()) {
            if (roadTraffic.getCarRow() == newRow && roadTraffic.getCarColumns() == newColumn) {
                return;
            }
            roadTraffic.setLight(newRow, newColumn);
        }
        else if (view.getBtnTire().isSelected()) {
            roadTraffic.addTires(newRow,newColumn,1);
        }
        else if (view.getBtnDelete().isSelected()) {
            roadTraffic.clearTile(newRow,newColumn);
        }
    }

    // Noch nicht implementiert
    private void mouseReleased(MouseEvent event) {

    }
}
