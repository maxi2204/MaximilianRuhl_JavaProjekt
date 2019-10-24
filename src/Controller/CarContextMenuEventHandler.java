package Controller;


import Model.RoadTraffic;
import View.RoadTrafficPanel;
import javafx.event.EventHandler;
import javafx.scene.input.ContextMenuEvent;

public class CarContextMenuEventHandler implements EventHandler<ContextMenuEvent> {
    private RoadTraffic roadTraffic;
    private CarContextMenu carContextMenu;
    private RoadTrafficPanel roadTrafficPanel;

    public CarContextMenuEventHandler (RoadTraffic roadTraffic, RoadTrafficPanel roadTrafficPanel) {
        this.roadTraffic = roadTraffic;
        this.roadTrafficPanel = roadTrafficPanel;
    }

    @Override
    public void handle (ContextMenuEvent contextMenuEvent) {
        int newColumn = ((int)contextMenuEvent.getX()/34);
        int newRow = ((int)contextMenuEvent.getY()/34);
        if (roadTraffic.getCarRow() == newRow && roadTraffic.getCarColumns() == newColumn) {
            carContextMenu = new CarContextMenu(roadTraffic);
            carContextMenu.show(roadTrafficPanel.getCanvas(),contextMenuEvent.getScreenX(),contextMenuEvent.getScreenY());
        }
    }
}
