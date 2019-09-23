package View;

import Controller.Program.Program;
import javafx.scene.control.TextArea;


public class Editor extends TextArea {
    Program program;

    Editor (Program program) {
        super();
        this.program = program;
        this.setText(this.program.getCarProgramCode());

    }
}
