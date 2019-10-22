package Controller.Program;

import View.SimulatorView;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.lang.model.SourceVersion;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

public class ProgramController {
    private static ArrayList<Program> programList = new ArrayList<>();
    public final static String DIRECTORY = "programs";
    public final static String PROGRAM_TEMPLATE = "void main()  {" + System.lineSeparator() + System.lineSeparator() + "}";
    public final static String DEFAULT_NAME = "DefaultCar";

    ProgramController() {
    }

    public static void add(Program program) {
        programList.add(program);
    }

    public static void removeProgram(Program program, String code) {
        program.save(code);
        programList.remove(program);
        if (programList.isEmpty()) {
            Platform.exit();
        }
    }

    public static void newProgram() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Neu");
        dialog.setHeaderText("Bitte Namen eingeben!");
        dialog.setContentText("Programm Name: ");

        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);

        dialog.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.isEmpty() ||
                    !ProgramController.validClassName(newValue) ||
                    ProgramController.availableProgram(newValue));
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> SimulatorView.createStage(name));
    }

    // Methode stammt von: https://stackoverflow.com/questions/13979172/how-to-check-if-the-class-name-is-valid
    private static boolean validClassName(String str) {
        return SourceVersion.isIdentifier(str) && !SourceVersion.isKeyword(str) && Character.isUpperCase(str.charAt(0));
    }
    private static boolean availableProgram(String str) {
        return Files.exists(Paths.get(DIRECTORY, str, ".java"));
    }

    public static void openProgram(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Programm auswählen");
        File dir = new File(DIRECTORY);
        fileChooser.setInitialDirectory(dir);
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(".java-Dateien","*.java");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            if (ProgramController.isOpen(file.getName())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "text");
                alert.showAndWait();
            }
            else {
                String name = file.getName();
                SimulatorView.createStage(name.substring(0, name.length()));
            }
        }
    }
    // Methode entatand durch: https://www.geeksforgeeks.org/stream-anymatch-java-examples/
    private static boolean isOpen (String str) {
        return programList.stream().anyMatch(p -> p.getName().equals(str));
    }
}
