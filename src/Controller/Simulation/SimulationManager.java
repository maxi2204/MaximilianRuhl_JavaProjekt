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

    public void toggle(Runnable doAfter) {
        if (this.status == SimulationState.RUNNING) {
            this.status = SimulationState.PAUSED;
        } else {
            this.status = SimulationState.RUNNING;
        }
        simulation.togglePause(doAfter);
    }

    public void stop(Runnable doAfter) {
        simulation.stopCar(doAfter);
    }

    public void simulationEnded() {
        this.simulation = null;
        this.status = SimulationState.NOT_RUNNING;
    }

    public void start(Runnable doAfter) {
        if (this.status == SimulationState.PAUSED) {
            this.status = SimulationState.RUNNING;
            simulation.resumeSimulation();
        } else {
            simulation = new Simulation(this, roadTraffic);
            this.status = SimulationState.RUNNING;
            this.simulation.startSimulation(doAfter);
        }
    }
}

