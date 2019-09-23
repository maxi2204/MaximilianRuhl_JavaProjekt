import Controller.Program.ProgramController;
import View.SimulatorView;
import javafx.application.Application;
import javafx.stage.Stage;


public class ProgramStart extends Application {

    // Autor: Maximilian Ruhl, WI2018
    // Datum: 22.09.2019

    @Override
    public void start(Stage primaryStage) throws Exception {
        SimulatorView.createStage(ProgramController.DEFAULT_NAME);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}