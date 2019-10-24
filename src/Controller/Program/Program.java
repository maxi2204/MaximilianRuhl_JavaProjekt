package Controller.Program;

import Controller.FileCanNotWriteException;
import Controller.FileIsNotADirectoryException;
import Controller.NoWritePermissionInDirectoryException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Program {
    private String name;

    public Program() throws IOException, FileIsNotADirectoryException, FileCanNotWriteException, NoWritePermissionInDirectoryException {
        this.name = ProgramController.DEFAULT_NAME;
        create();
    }

    public Program(String name) throws IOException, FileIsNotADirectoryException, FileCanNotWriteException, NoWritePermissionInDirectoryException {
        this.name = name;
        create();
    }

    public String getFullFileName() {
        return ProgramController.DIRECTORY + File.separator + getFileName();
    }

    public void setName(String name) {
        this.name = name;
    }

    //Methode returnt den Code aus dem Programm für die Editor Klasse
    public String getCarProgramCode() {
        try {
            Path file = Paths.get(ProgramController.DIRECTORY, getFileName());
            StringBuilder text = new StringBuilder();
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            for (int i = 0; i < lines.size(); i++) {
                text.append(lines.get(i));
                if (i < lines.size() - 1) {
                    text.append(System.lineSeparator());
                }
            }
            text.delete(0, getPrefix().length());
            text.delete(text.length() - getPostfix().length(), text.length());
            return text.toString();
        } catch (Exception exc) {
            return ProgramController.PROGRAM_TEMPLATE;
        }
    }

    // Speichert den code aus der Textarea und liefert true wenn es geklappt hat
    public void save(String code) {
        if (!checkPath()) {
            return;
        }
        code = this.getPrefix() + code + this.getPostfix();
        ArrayList<String> lines = new ArrayList<>();
        lines.add(code);
        try {
            Path path = Paths.get(ProgramController.DIRECTORY, getFileName());
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (Exception exc) {
        }
    }
    // Von Dibos Source

    /**
     * Falls noch kein Pfad vorhanden ist wird ein neuer erstellt
     *
     * @throws IOException .
     */
    private void create() throws IOException, FileCanNotWriteException, FileIsNotADirectoryException, NoWritePermissionInDirectoryException {
        // Pfad zum Dateiordner
        File directoryFile = new File(ProgramController.DIRECTORY);
        //Path filePath2 = Paths.get(ProgramController.DIRECTORY, getFileName());
        String filePath = new String(ProgramController.DIRECTORY + getFileName());
        File file = new File(filePath);
        if (!directoryFile.exists()) {
            directoryFile.mkdir();
        } else if (!directoryFile.isDirectory()) {
            throw new FileIsNotADirectoryException();
        } else if (!directoryFile.canWrite()) {
            throw new NoWritePermissionInDirectoryException();
        }

        if (!file.exists()) {
            file.createNewFile();
            String code = this.getPrefix() + ProgramController.PROGRAM_TEMPLATE + this.getPostfix();
            ArrayList<String> lines = new ArrayList<>();
            lines.add(code);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(lines.toString());
            fileWriter.close();
           // Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
        }
        if (!file.canWrite()) {
            throw new FileCanNotWriteException();
        }
    }

    // Wenn noch kein Ordner angelegt wurde, wird ein neuer angelegt
    private boolean checkPath() {
        try {
            Path dirPath = Paths.get(ProgramController.DIRECTORY);
            if (!Files.exists(Paths.get(ProgramController.DIRECTORY))) {
                dirPath = Files.createDirectory(dirPath);
            } else if (!Files.isDirectory(dirPath)) {
                return false;
            } else if (!Files.isWritable(dirPath)) {
                return false;
            }
            return true;
        } catch (Exception exc) {
            return false;
        }
    }

    private String getPrefix() {
        return "public class " + this.getName() + " extends Model.Car { public ";
    }

    private String getPostfix() {
        return "}";
    }

    public String getFileName() {
        return name + ".java";
    }

    public String getName() {
        return name;
    }

}
