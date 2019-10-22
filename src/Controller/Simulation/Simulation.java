package Controller.Simulation;

import Model.Car;
import Model.RoadTraffic;
import javafx.application.Platform;


public class Simulation extends Thread implements Observer {
    RoadTraffic roadTraffic;
    SimulationManager simulationManager;
    Car car;
    boolean stop;
    boolean pause;
    private Runnable after;

    Simulation(SimulationManager simulationManager, RoadTraffic roadTraffic) {
        this.roadTraffic = roadTraffic;
        this.simulationManager = simulationManager;
        this.car = roadTraffic.getCar();
        System.out.println(car);
        System.out.println(roadTraffic);

    }

    public void togglePause() {
        pause = !pause;
    }

    @Override
    public void run() {
        this.roadTraffic.addObserver(this);
        try {
            this.car.main();
        } catch (Exception exc) {
// TODO Meldung falls Irgendwas nicht geht
            exc.printStackTrace();
        } finally {
            this.roadTraffic.deleteObserver(this);
            this.simulationManager.simulationEnded();
            after.run();
        }
    }

    public void startSimulation() {
        this.start();
    }

    void breakSimulation() {

    }

    void resumeSimulation() {
        this.pause = false;
        this.stop = false;
    }


    public void stopCar() {
        this.stop = true;
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
                    throw new SimulationStoppedException();
                }
            }
        } catch (InterruptedException ignored) {

        }
        if (this.stop) {
            throw new SimulationStoppedException();
        }
    }

    public void startSimulation(Runnable doAfter) {
        this.after = doAfter;
        startSimulation();
    }
}
