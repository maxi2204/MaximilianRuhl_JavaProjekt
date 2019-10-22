package Controller.Simulation;

import Model.RoadTraffic;


public class SimulationManager extends Observable {
    final static int MINSPEED = 0;
    final static int MEDIUMSPEED = 50;
    final static int MAXSPEED = 100;


    private int status;
    private volatile int speed;
    private RoadTraffic roadTraffic;
    private Simulation simulation;

    public SimulationManager(RoadTraffic roadTraffic) {
        this.roadTraffic = roadTraffic;
        this.status = SimulationState.NOT_RUNNING;
        this.speed = SimulationManager.MEDIUMSPEED;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int x) {
        this.speed = x;
    }

    public int getStatus() {
        return status;
    }

    public void start() {
        simulation = new Simulation(this, roadTraffic);
        this.status = SimulationState.RUNNING;
        this.simulation.startSimulation();

    }

    public void toggle() {
       simulation.togglePause();
    }

    public void stop() {
        simulation.stopCar();
    }

    public void simulationEnded() {
        this.simulation = null;
        this.status = SimulationState.NOT_RUNNING;
    }

    public void start(Runnable doAfter) {
        simulation = new Simulation(this, roadTraffic);
        this.status = SimulationState.RUNNING;
        this.simulation.startSimulation(doAfter);
    }
}

