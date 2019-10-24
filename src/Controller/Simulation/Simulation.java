package Controller.Simulation;

import Model.Car;
import Model.NotInTrafficException;
import Model.RoadTraffic;
import javafx.application.Platform;


public class Simulation extends Thread implements Observer {
    private RoadTraffic roadTraffic;
    private SimulationManager simulationManager;
    private Car car;
    private volatile boolean stop;
    private volatile boolean pause;
    private Runnable after;

    Simulation(SimulationManager simulationManager, RoadTraffic roadTraffic) {
        this.roadTraffic = roadTraffic;
        this.simulationManager = simulationManager;
        this.car = roadTraffic.getCar();
    }

    synchronized void togglePause(Runnable doAfter) {
        this.after = doAfter;
        this.togglePause();
    }

    private synchronized void togglePause() {
        pause = !pause;
    }

    @Override
    public void run() {
        this.roadTraffic.addObserver(this);
        try {
            this.car.main();
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            this.roadTraffic.deleteObserver(this);
            this.simulationManager.simulationEnded();
            after.run();
        }
    }

    void startSimulation() {
        this.pause = false;
        this.stop = false;
        this.start();
    }

    void resumeSimulation() {
        this.pause = false;
        this.stop = false;
    }


    void stopCar(Runnable doAfter) {
        this.after = doAfter;
        this.stop = true;
        this.pause = false;
    }

    public boolean isStopped() {
        return this.stop;

    }

    @Override
    public void update(Observable o) {
        if (Platform.isFxApplicationThread()) {
            return;
        }
        try {
            Thread.sleep((SimulationManager.MAXSPEED - this.simulationManager.getSpeed()) * 50);
        } catch (InterruptedException exc) {
            this.interrupt();
        }
        try {
            while (this.pause) {
                Thread.sleep(this.simulationManager.getSpeed());
                if (this.stop) {
                    throw new ThreadDeath();
                }
            }
        } catch (InterruptedException ignored) {

        }
        if (this.stop) {
            throw new ThreadDeath();
        }
    }

    public void startSimulation(Runnable doAfter) {
        this.after = doAfter;
        startSimulation();
    }
}
