package Controller.Program;

import Model.Car;
import Model.RoadTraffic;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class CompileController {
    // Compile Methode wird ausgeführt bei jeder neuen Stage
    public static void firstCompile(RoadTraffic roadTraffic, Program program) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int i = ToolProvider.getSystemJavaCompiler().run(null,null,stream,program.getFullFileName());
        if (i == 0) {
           // CompileController.loadCompiled(roadTraffic, program);
        }
    }
    // Compile Methode für jeden weiteren Compile Vorgang beim manuellen betätigen des Buttons
    public static void compileReload(RoadTraffic roadTraffic, Program program, String code) {
        program.save(code);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int i = ToolProvider.getSystemJavaCompiler().run(null,null,stream,program.getFullFileName());
        if (i != 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR,stream.toString(), ButtonType.OK);
            alert.setTitle("Da meckert der Kompiler :(");
            alert.showAndWait();
        }
        else {
            //CompileController.loadCompiled(roadTraffic,program);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erfolgreich kompiliert! :)");
            alert.showAndWait();
        }
    }
    // Eine .class Datei wird ausgelesen, ein neues car Objekt erzeugt, sowie dieses objekt in den roadTraffic zu initieren
    // Läuft noch nicht
    /*
    public static void loadCompiled(RoadTraffic roadTraffic, Program program) {
    // Classloader wird erzeugt, lädt die Klasse die zuvor erzeugt wurde,
        URLClassLoader classLoader = null;
        Car car = new Car();
        try {
            File dir = new File(ProgramController.DIRECTORY);
            URL[] urls = new URL[]{dir.toURI().toURL()};
        } catch (MalformedURLException exc) {
            exc.printStackTrace();
        }  finally {
            roadTraffic.changeCar(car);
        }

     */


}
