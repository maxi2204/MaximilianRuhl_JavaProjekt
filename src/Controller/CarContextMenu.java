package Controller;

import Model.Car;
import Model.RoadTraffic;
import javafx.scene.control.MenuItem;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class CarContextMenu {
    private RoadTraffic roadTraffic;

    public CarContextMenu(RoadTraffic roadTraffic) {
        this.roadTraffic = roadTraffic;
        Method[] methods = this.getMethods(roadTraffic.getCar());
        for (Method method : methods) {
            MenuItem m = new MenuItem(this.getMethodName(method));
        }
    }

    public Method[] getMethods(Car car) {
        ArrayList<Method> res = new ArrayList<Method>();
        if (Car.class != car.getClass()) {
            for (Method methods : car.getClass().getDeclaredMethods()) {

            }
        }
        return null;
    }

    private String getMethodName(Method method) {
        return method.getName();
    }
}
