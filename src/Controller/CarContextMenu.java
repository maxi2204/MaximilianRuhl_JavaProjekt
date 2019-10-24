package Controller;

import Model.Car;
import Model.RoadTraffic;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class CarContextMenu extends ContextMenu {
    private RoadTraffic roadTraffic;

    public CarContextMenu(RoadTraffic roadTraffic) {
        this.roadTraffic = roadTraffic;
        ArrayList<Method> methods = this.getMethods(roadTraffic.getCar());
        for (Method method : methods) {
            //TODO AUSNAHMEN: abstrakte, static, private und invisible UND alle methode die parameter besitzen
            if (method.getParameterTypes().length == 0 && !method.isAnnotationPresent(Car.Invisible.class)) {
            MenuItem m = new MenuItem(this.getMethodName(method));
            m.setOnAction(event -> {
                try {
                    method.invoke(roadTraffic.getCar());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
            this.getItems().add(m); }
        }
    }
    //TODO boolean/int ... fehlt vor der Methode
    private ArrayList<Method> getMethods(Car car) {
        ArrayList<Method> res = new ArrayList<Method>();
        if (Car.class != car.getClass()) {
            res.addAll(Arrays.asList(car.getClass().getDeclaredMethods()));
            res.addAll(Arrays.asList(Car.class.getDeclaredMethods()));
            return res;
        }
        return null;
    }

    private String getMethodName(Method method) {
        return method.getName();
    }
}
