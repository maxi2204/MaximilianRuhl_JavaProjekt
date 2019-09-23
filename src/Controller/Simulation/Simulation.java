package Controller.Simulation;

import Model.Car;
import Model.RoadTraffic;

import java.util.Observable;
import java.util.Observer;

public class Simulation extends Thread implements Observer {
    Object syncObject;
    RoadTraffic roadTraffic;
    SimulationManager simulationManager;
    Car car;
    boolean stop;
    boolean pause;

    Simulation(SimulationManager simulationManager, RoadTraffic roadTraffic) {
        this.syncObject = new Object();
        this.roadTraffic = roadTraffic;
        this.simulationManager = simulationManager;
        this.car = roadTraffic.getCar();

    }

    @Override
    public void run() {
        this.roadTraffic.addObserver(this);
        try {
            this.car.main();
        } catch (Exception exc) {

        } finally {
            this.roadTraffic.deleteObserver(this);
            this.simulationManager.simulationEnded();
        }
    }

    public void startSimulation() {

    }

    void breakSimulation() {

    }

    void resumeSImulation() {
        synchronized (this.syncObject) {
            this.pause = false;

        }
    }

    void stopSimulation() {
        synchronized (this.syncObject) {

        }
    }

    public void isStopped() {

    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            Thread.sleep(SimulationManager.MAXSPEED * 5 - this.simulationManager.getSpeed());
        } catch (InterruptedException exc) {
            this.interrupt();
        }
        synchronized (this.syncObject) {
            try {
                while (this.pause) {
                    this.syncObject.wait();
                }
            } catch (InterruptedException exc) {

            }
        }
        if (this.stop) {
            throw new SimulationStoppedException();
        }
    }
}
