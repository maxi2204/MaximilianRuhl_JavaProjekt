package Controller.Simulation;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable {
    private boolean changed = false;
    private List<Observer> observers;

    public Observable() {
        observers = new ArrayList<>();
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void deleteObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        observers.forEach(o -> o.update(this));
        changed = false;
    }

    public void notifyObservers(Object obj) {

    }

    public void setChanged() {
        changed = true;
    }

    public void deleteObservers() {

    }

    public int countObservers() {
        return observers.size();
    }
}
