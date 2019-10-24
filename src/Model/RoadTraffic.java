package Model;


import Controller.Simulation.Observable;

import java.lang.reflect.Method;

public class RoadTraffic extends Observable {

    // Autor: Maximilian Ruhl, WI2018

    final static int startRows = 20; // Zeilen
    final static int startColumns = 20; // SPalten

    final static int LIGHT = -1;
    // Die Richtung des Autos kann 4 Werte annehmen: 0 = Nord, 1 = Ost, 2 = Süd, 3 = West
    // Die Standard Richtung des Autos ist Osten (1)
    final static int START_CAR_DIRECTION = 1;
    // -1 = Ampel , 0 = Feld ist leer, größer 0 sind Reifen auf der Kachel
    private int[][] tiles;
    // Car Attribute
    private Car car;
    private int carRow;
    private int carColumns;
    private int carDirection;
    private int carTires;
    // Aktuelle Größen Angaben
    private int actualRows;
    private int actualColumns;

    // Konstruktor
    public RoadTraffic() {
        this.car = new Car();
        this.car.setRoadTraffic(this);
        this.carDirection = START_CAR_DIRECTION;
        this.carColumns = 0;
        this.carRow = 0;
        this.carTires = 0;
        this.tiles = new int[startRows][startColumns];
        this.createRoadTraffic();
        this.actualRows = startRows;
        this.actualColumns = startColumns;
    }

    // Start Welt wird erzeugt
    private void createRoadTraffic() {
        for (int i = 0; i < startRows; i++) {
            for (int j = 0; j < startColumns; j++) {
                tiles[i][j] = 0;
            }
        }
    }

    public void changeCar(Car car) {
        car.setRoadTraffic(this);
        this.car = car;
        // Die alte Car-Klasse kann immernoch die Position des Autos im Roadtraffic beeinflussen,
        // wenn gerade die Main Methode der Klasse läuft. Dies ist nützlich, da so ein Austausch der
        // Car-Klasse die laufende Simulation nicht beeinflusst
    }

    // Wenn an den Koordinaten eine Ampel sich befindet wird true returnt
    public boolean isLightOnPos(int row, int column) {
        return tiles[row][column] == LIGHT;
    }

    // Car setzen
    public void setCar(int row, int column) {
        synchronized (this) {
            if (!isLightOnPos(row, column)) {
                carRow = row;
                carColumns = column;
            }
        }
        setChanged();
        notifyObservers();
    }

    // Ampel setzen an bestimmten Koordinaten
    public void setLight(int row, int column) {
        if (!isLightOnPos(row, column)) {
            tiles[row][column] = LIGHT;
        }
        setChanged();
        notifyObservers();
    }

    // Reifen hinzufügen an bestimmten Koordinaten
    public void addTires(int row, int column, int numberOfTires) {
        if (!isLightOnPos(row, column) && row < actualRows && column < actualColumns) {
            tiles[row][column] = tiles[row][column] + numberOfTires;
        }
        setChanged();
        notifyObservers();
    }

    // Reifen im Kofferraum werden zurück geliefert
    public int getCarTires() {
        return this.carTires;
    }

    // Car Richtung wird zurück geliefert 0 = Nord, 1 = Ost, 2 = Süd, 3 = West
    public int getCarDirection() {
        return carDirection;
    }

    // Die Car Richtung verschiebt sich so, dass das Car sich nach links dreht
    public void leftAround() {
        if (carDirection == 0) {
            carDirection = 3;
        } else if (carDirection == 1) {
            carDirection = 0;
        } else if (carDirection == 2) {
            carDirection = 1;
        } else if (carDirection == 3) {
            carDirection = 2;
        }
        setChanged();
        notifyObservers();
    }

    // Hilfsmethode um zu überprüfen ob die Koordinaten sich noch im Bereich befinden
    public boolean isInTraffic(int row, int column) {
        if (row < 0 || row >= actualRows || column < 0 || column >= actualColumns) {
            return false;
        }
        return true;
    }

    // Bei Aufruf der ago() Methode werden die Koordinaten des Autos verschoben, so dass sich das Car vorwärts bewegt
    public void ago() throws LightAtPosException, NotInTrafficException {
        if (carDirection == 0) {
            if (!(isInTraffic(((carRow - 1)), carColumns))) {
                throw new NotInTrafficException();
            } else if (isLightOnPos(((carRow - 1)), carColumns)) {
                throw new LightAtPosException();
            } else {
                carRow--;
            }
        } else if (carDirection == 1) {
            if (!(isInTraffic(carRow, (carColumns + 1)))) {
                throw new NotInTrafficException();
            } else if (isLightOnPos(carRow, (carColumns + 1))) {
                throw new LightAtPosException();
            } else {
                carColumns++;
            }
        } else if (carDirection == 2) {
            if (!(isInTraffic((carRow + 1), carColumns))) {
                throw new NotInTrafficException();
            } else if (isLightOnPos((carRow + 1), carColumns)) {
                throw new LightAtPosException();
            } else {
                carRow++;
            }
        } else if (carDirection == 3) {
            if (!(isInTraffic(carRow, (carColumns - 1)))) {
                throw new NotInTrafficException();
            } else if (isLightOnPos(carRow, (carColumns - 1))) {
                throw new LightAtPosException();
            } else {
                carColumns--;
            }
        }
        setChanged();
        notifyObservers();
    }

    // hilfsmethode um fetzustellen ob sich Reifen an bestimmten Koordinaten befinden
    private boolean tiresAtPos(int row, int column) {
        return tiles[row][column] > 0;
    }

    // Methode um Reifen aufzusammeln und in den Kofferraum des Autos abzulegen
    public void take() throws TiresException {
        if (tiresAtPos(carRow, carColumns)) {
            carTires++;
            tiles[carRow][carColumns]--;
        }
        setChanged();
        notifyObservers();
    }

    // Hilfsmethode um festzustellen ob sich Reifen im Kofferaum befinden
    private boolean tiresInTrunk() {
        return carTires > 0;
    }

    // Methode um Reifen aus dem Kofferraum abzulegen
    public void put() throws TrunkEmptyException {
        if (tiresInTrunk()) {
            carTires--;
            tiles[carRow][carColumns]++;
        }
        setChanged();
        notifyObservers();
    }

    // Methode um zu überprüfen ob vor dem Car keine Ampel ist und der Bereich nicht zu Ende ist
    boolean isFree() {
        switch (carDirection) {
            case 0:
                if (!(isInTraffic((carRow - 1), carColumns))) {
                    return false;
                } else if (isLightOnPos((carRow - 1), carColumns)) {
                    return false;
                } else {
                    return true;
                }
            case 1:
                if (!(isInTraffic(carRow, (carColumns + 1)))) {
                    return false;
                } else if (isLightOnPos(carRow, (carColumns + 1))) {
                    return false;
                } else {
                    return true;
                }
            case 2:
                if (!(isInTraffic((carRow + 1), carColumns))) {
                    return false;
                } else if (isLightOnPos((carRow + 1), carColumns)) {
                    return false;
                } else {
                    return true;
                }
            case 3:
                if (!(isInTraffic(carRow, (carColumns - 1)))) {
                    return false;
                } else if (isLightOnPos(carRow, (carColumns - 1))) {
                    return false;
                } else {
                    return true;
                }
            default:
                return false;
        }
    }

    // Hilfsmethode für das Car
    boolean trunkEmpty() {
        return tiresInTrunk();
    }

    // Hilfsmethode für das Car
    boolean tiresAtCarPos() {
        return tiresAtPos(carRow, carColumns);
    }

    // Größe des RoadTraffics ändern
    public void setSize(int row, int column) {
        int[][] tmp = new int[row][column];
        for (int i = 0; i < tmp.length && i < tiles.length; i++) {
            for (int j = 0; j < tmp[i].length && j < tiles[i].length; j++) {
                if (i < row && j < column) {
                    tmp[i][j] = this.tiles[i][j];
                }
            }
        }

        if (this.carRow >= row || this.carColumns >= column) {
            this.tiles[0][0] = 0;
            this.carRow = 0;
            this.carColumns = 0;
        }
        this.tiles = tmp;
        this.actualRows = tiles.length;
        this.actualColumns = tiles[0].length;
        setChanged();
        notifyObservers();
    }

    // Methode um Kachel zu löschen bzw. auf 0 zu setzen
    public void clearTile(int zeile, int spalte) {
        tiles[zeile][spalte] = 0;
        setChanged();
        notifyObservers();
    }

    // Aktuelle Anzahl der Zeilen wird returnt
    public int getActualRows() {
        return actualRows;
    }

    // Aktuelle Anzahl der Spalten wird returnt
    public int getActualColumns() {
        return actualColumns;
    }

    // Hilfsmethode um festzustellen wie viele Reifen sich an bestimmten Koordinaten befinden
    public int getTiresAtPos(int row, int column) {
        if (tiresAtPos(row, column)) {
            return tiles[row][column];
        }
        return 0;
    }

    // Aktuelle Zeilen-Position des Autos wird zurückgegeben
    public int getCarRow() {
        return carRow;
    }

    // Aktuelle Spalten-Position des Autos wird zurückgegeben
    public int getCarColumns() {
        return carColumns;
    }

    public int[][] getTiles() {
        return tiles;
    }

    public Car getCar() {
        return car;
    }
}
