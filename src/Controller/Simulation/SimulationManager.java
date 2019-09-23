package Controller.Simulation;

import Model.RoadTraffic;

import java.util.Observable;

public class SimulationManager extends Observable implements Runnable{
    final static int MINSPEED = 0;
    final static int MEDIUMSPEED = 50;
    final static int MAXSPEED = 100;


    private int status;
    private volatile int speed;
    private RoadTraffic roadTraffic;
    private Simulation simulation;

    SimulationManager () {
         this.roadTraffic = new RoadTraffic();
         this.status = SimulationState.NOT_RUNNING;
         this.speed = SimulationManager.MEDIUMSPEED;
    }

    public int getSpeed () {
        return speed;
    }
    public void setSpeed (int x) {
            this.speed = x;
    }

    public int getStatus () {
        return status;
    }

    public void start () {
        simulation = new Simulation(this,roadTraffic);
        this.simulation.startSimulation();
        this.status = SimulationState.RUNNING;

    }

    @Override
    public void run() {
        if (this.status == SimulationState.NOT_RUNNING) {
            this.start();
        } else {
            this.resume();
        }
    }

    private void resume() {
    }

    public void simulationEnded() {
this.simulation = null;
this.status = SimulationState.NOT_RUNNING;
    }
}

