package Controller;

import View.SimulatorView;

public class PlacingState {
    private int selected;

    final static int CAR = 0;
    final static int TIRES = 1;

    final static int LIGHT = 2;
    final static int CLEAR_TILE = 3;

    SimulatorView view;

    public PlacingState(SimulatorView view) {
        this.view = view;
        this.selected = -1;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
        switch (selected) {
            case CAR:
                view.getBtnNewCar().setSelected(true);
                view.getPlaceCarRadioItem().setSelected(true);
                break;
            case TIRES:
                view.getBtnTire().setSelected(true);
                view.getPlaceTireRadioItem().setSelected(true);
                break;
            case LIGHT:
                view.getBtnLight().setSelected(true);
                view.getPlaceLightRadioItem().setSelected(true);
                break;
            case CLEAR_TILE:
                view.getBtnDelete().setSelected(true);
                view.getClearRadioItem().setSelected(true);
                break;
                default:
                    break;
        }
    }

    public static int getCar() {
        return CAR;
    }

    public static int getTires() {
        return TIRES;
    }

    public static int getLight() {
        return LIGHT;
    }

    public static int getClearTile() {
        return CLEAR_TILE;
    }
}
