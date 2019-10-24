package View;

import Controller.FileCanNotWriteException;
import Controller.FileIsNotADirectoryException;
import Controller.NoWritePermissionInDirectoryException;
import Controller.PlacingState;
import Controller.Program.CompileController;
import Controller.Program.Program;
import Controller.Program.ProgramController;
import Controller.Simulation.SimulationManager;
import Controller.Simulation.SimulationState;
import Model.RoadTraffic;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class SimulatorView {

    // Autor: Maximilian Ruhl, WI2018
    // Datum: 25.08.2019
    // Model
    private RoadTraffic roadTraffic;
    // PlacingState
    private PlacingState placingState;
    // SimulationManager
    private SimulationManager simulationManager;
    // Stage
    private Stage stage;
    // Programm
    private Program program;
    // Wichtigste Javafx Elemente
    private BorderPane mainPane;
    private BorderPane centerPane;
    private SplitPane splitPane;
    private TextArea textArea;
    private ScrollPane sc;
    private Label meldungen;

    private MenuBar mainMenuBar;
    private ToolBar hauptToolBar;
    private RoadTrafficPanel sPanel;
    private ToggleGroup toggleGroup;
    private ToggleGroup toggleGroup2;
    // Menüs
    private Menu editorMenu, worldMenu, carMenu, simulationMenu;
    private Menu saveMenu, pictureSaveMenu, loadMenu;
    // CheckMenuItems
    private RadioMenuItem placeCarRadioItem, placeTireRadioItem, placeLightRadioItem, clearRadioItem;
    // Menü Items
    private MenuItem printItem, changeSizeItem, closeItem, printItem2, openItem, newItem, compileItem, xmlItem, jaxbItem, serialItem;
    private MenuItem tiresInTrunkItem, takeItem, deliverItem, leftAroundItem, agoItem, startItem, freezeItem, breakItem;
    // Toolbar Buttons
    private ToggleButton btnNew;
    private ToggleButton btnOpen;
    private ToggleButton btnSave;
    private ToggleButton btnComp;
    private ToggleButton btnTerrain;
    private ToggleButton btnNewCar;
    private ToggleButton btnTire;
    private ToggleButton btnLight;
    private ToggleButton btnDelete;
    private ToggleButton btnTiresInsideCar;
    private ToggleButton btnCarLeftAround;
    private ToggleButton btnCarAgo;
    private ToggleButton btnTakeTires;
    private ToggleButton btnDeliverTires;
    private ToggleButton btnPlay;
    private ToggleButton btnBreak;
    private ToggleButton btnStop;

    private Dialog<Pair<Integer, Integer>> dialog;
    String file = "death.wav";
    final Media Medium = new Media(new File(file).toURI().toString());
    MediaPlayer mediaPlayer;

    public SimulatorView(RoadTraffic roadTraffic, Stage stage, Program program, SimulationManager simulationManager) {
        this.roadTraffic = roadTraffic;
        this.stage = stage;
        this.program = program;
        this.placingState = new PlacingState(this);
        sPanel = new RoadTrafficPanel(this, this.roadTraffic);
        this.simulationManager = simulationManager;
        //Haupt Layout mit MenüBar
        mainPane = new BorderPane();
        //MenuBar menu = new MenuBar();
        mainPane.setTop(createMainMenu());
        //Inneres Layout für Toolbar und Content
        centerPane = new BorderPane();
        centerPane.setTop(createMainToolBar());
        // Split Pane für den Content
        splitPane = new SplitPane();
        // TextEditor für den linken Bereich
        textArea = new TextArea();
        textArea = new Editor(this.program);
        splitPane.getItems().add(textArea);
        sc = new ScrollPane();
        sc.setContent(sPanel);

        this.center(sc.getViewportBounds(), sPanel.getCanvas());

        sc.viewportBoundsProperty().addListener((observable, oldValue, newValue) -> this.center(newValue, sPanel.getCanvas()));
        splitPane.getItems().add(sc);

        // SplitPane hinzufügen
        centerPane.setCenter(splitPane);

        // Label für Meldungen
        meldungen = new Label("Herzlich Willkommen!");

        mainPane.setCenter(centerPane);
        mainPane.setBottom(meldungen);
        Scene scene = new Scene(mainPane, 1400, 900);

        stage.setTitle(this.program.getName());
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> ProgramController.removeProgram(this.program, this.textArea.getText()));
    }


    private MenuBar createMainMenu() {
        this.mainMenuBar = new MenuBar();
        // Editor Menü
        editorMenu = new Menu("_Editor");
        // Neues Programm erstellen
        newItem = new MenuItem("_Neu");
        newItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+N"));
        Image imgNew = new Image(getClass().getResource("Resources/New16.gif").toString());
        newItem.setGraphic(new ImageView(imgNew));
        newItem.setOnAction(event -> ProgramController.newProgram());
        // Öffnen eines neuen Programms
        openItem = new MenuItem("_Öffnen");
        Image imgOpen = new Image(getClass().getResource("Resources/Open16.gif").toString());
        openItem.setGraphic(new ImageView(imgOpen));
        openItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+O"));
        openItem.setOnAction(event -> ProgramController.openProgram(this.stage));
        // Kompilieren des bestehenden Programms
        compileItem = new MenuItem("_Kompilieren");
        compileItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+K"));
        compileItem.setOnAction(event -> CompileController.compileReload(this.roadTraffic, this.program, this.textArea.getText()));
        // Drucken
        printItem = new MenuItem("_Drucken");
        printItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+P"));
        Image imgDrucken = new Image(getClass().getResource("Resources/Print16.gif").toString());
        printItem.setGraphic(new ImageView(imgDrucken));
        // Beenden
        closeItem = new MenuItem("_Beenden");
        closeItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+Q"));
        closeItem.setOnAction(e -> {
            ProgramController.removeProgram(this.program, this.textArea.getText());
            this.stage.close();
        });
        editorMenu.getItems().addAll(newItem, openItem, new SeparatorMenuItem(), compileItem, printItem, new SeparatorMenuItem(), closeItem);
        // Welt Menü
        worldMenu = new Menu("_Welt");
        saveMenu = new Menu("_Speichern");
        xmlItem = new MenuItem("_XML");
        jaxbItem = new MenuItem("_JAXB");
        serialItem = new MenuItem("_Serialisieren");
        saveMenu.getItems().addAll(xmlItem, jaxbItem, serialItem);
        loadMenu = new Menu("_Laden");
        pictureSaveMenu = new Menu("_Als Bild saveMenu");
        printItem2 = new MenuItem("_Drucken");
        changeSizeItem = new MenuItem("_Größe ändern...");
        changeSizeItem.setOnAction(event -> this.paintDialog());

        this.toggleGroup2 = new ToggleGroup();
        placeCarRadioItem = new RadioMenuItem("_Auto platzieren");
        placeCarRadioItem.setOnAction(event -> placingState.setSelected(PlacingState.getCar()));
        placeTireRadioItem = new RadioMenuItem("_Reifen platzieren");
        placeTireRadioItem.setOnAction(event -> placingState.setSelected(PlacingState.getTires()));
        placeLightRadioItem = new RadioMenuItem("_Ampel platzieren");
        placeLightRadioItem.setOnAction(event -> placingState.setSelected(PlacingState.getLight()));
        clearRadioItem = new RadioMenuItem("_Kachel löschen");
        clearRadioItem.setOnAction(event -> placingState.setSelected(PlacingState.getClearTile()));
        toggleGroup2.getToggles().addAll(placeCarRadioItem, placeTireRadioItem, placeLightRadioItem, clearRadioItem);
        worldMenu.getItems().addAll(saveMenu, loadMenu, pictureSaveMenu, printItem2, changeSizeItem, new SeparatorMenuItem(), placeCarRadioItem, placeTireRadioItem, placeLightRadioItem, clearRadioItem);
        // Car Menü
        carMenu = new Menu("_Auto");
        tiresInTrunkItem = new MenuItem("_Reifen im Kofferraum...");
        leftAroundItem = new MenuItem("_linksUm");
        leftAroundItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+Shift+L"));
        leftAroundItem.setOnAction(event -> roadTraffic.leftAround());
        agoItem = new MenuItem("_vor");
        agoItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+Shift+V"));
        agoItem.setOnAction(event -> roadTraffic.ago());
        takeItem = new MenuItem("_aufnehmen");
        takeItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+Shift+N"));
        takeItem.setOnAction(event -> roadTraffic.take());
        deliverItem = new MenuItem("_abgeben");
        deliverItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+Shift+G"));
        deliverItem.setOnAction(event -> roadTraffic.put());
        carMenu.getItems().addAll(tiresInTrunkItem, leftAroundItem, agoItem, takeItem, deliverItem);
        // Simulation Menü
        simulationMenu = new Menu("_Simulation");
        startItem = new MenuItem("_Start/Fortsetzen");
        startItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+F11"));
        Image imgStart = new Image(getClass().getResource("Resources/Play16.gif").toString());
        startItem.setGraphic(new ImageView(imgStart));
        startItem.setOnAction(event -> {
            int status = simulationManager.getStatus();
            if (status == SimulationState.RUNNING) {
                simulationManager.toggle(() ->
                        btnPlay.setDisable(false));
            } else {
                simulationManager.start(() ->
                        btnPlay.setDisable(true));
            }
        });
        breakItem = new MenuItem("_Pause");
        Image imgBreak = new Image(getClass().getResource("Resources/Pause16.gif").toString());
        breakItem.setGraphic(new ImageView(imgBreak));
        breakItem.setOnAction(event -> {
            simulationManager.toggle(() ->
                    btnPlay.setDisable(true));
        });
        freezeItem = new MenuItem("_Stopp");
        freezeItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+F12"));
        Image imgFreeze = new Image(getClass().getResource("Resources/Stop16.gif").toString());
        freezeItem.setGraphic(new ImageView(imgFreeze));
        simulationMenu.getItems().addAll(startItem, breakItem, freezeItem);
        this.mainMenuBar.getMenus().addAll(editorMenu, worldMenu, carMenu, simulationMenu);
        return this.mainMenuBar;
    }

    private ToolBar createMainToolBar() {
        // Layout für die Toolbar erstellen
        // Buttons und weitere Elemente für die Toolbar erstellen
        this.toggleGroup = new ToggleGroup();

        btnNew = new ToggleButton();
        Image imgNew = new Image(getClass().getResource("Resources/New24.gif").toString());
        btnNew.setGraphic(new ImageView(imgNew));
        btnNew.setTooltip(new Tooltip("Neu"));
        btnNew.setOnAction(event -> ProgramController.newProgram());
        btnNew.setToggleGroup(toggleGroup);

        btnOpen = new ToggleButton();
        Image imgOpen = new Image(getClass().getResource("Resources/Open24.gif").toString());
        btnOpen.setGraphic(new ImageView(imgOpen));
        btnOpen.setTooltip(new Tooltip("Öffnen"));
        btnOpen.setOnAction(event -> ProgramController.openProgram(this.stage));
        btnOpen.setToggleGroup(toggleGroup);

        btnSave = new ToggleButton();
        Image imgSave = new Image(getClass().getResource("Resources/Save24.gif").toString());
        btnSave.setGraphic(new ImageView(imgSave));
        btnSave.setTooltip(new Tooltip("Speichern"));
        btnSave.setOnAction(event -> this.program.save(textArea.getText()));
        btnSave.setToggleGroup(toggleGroup);

        btnComp = new ToggleButton();
        Image imgComp = new Image(getClass().getResource("Resources/Compile24.gif").toString());
        btnComp.setGraphic(new ImageView(imgComp));
        btnComp.setTooltip(new Tooltip("Kompilieren"));
        btnComp.setOnAction(event -> CompileController.compileReload(this.roadTraffic, this.program, this.textArea.getText()));
        btnComp.setToggleGroup(toggleGroup);

        btnTerrain = new ToggleButton();
        Image imgTer = new Image(getClass().getResource("Resources/Terrain24.gif").toString());
        btnTerrain.setGraphic(new ImageView(imgTer));
        btnTerrain.setTooltip(new Tooltip("Größe ändern"));
        btnTerrain.setOnAction(event -> this.paintDialog());
        btnTerrain.setToggleGroup(toggleGroup);

        btnNewCar = new ToggleButton();
        Image imgCar = new Image(getClass().getResource("Resources/Auto24_2.png").toString());
        btnNewCar.setGraphic(new ImageView(imgCar));
        btnNewCar.setTooltip(new Tooltip("Neues Car platzieren"));
        btnNewCar.setOnAction(event -> placingState.setSelected(PlacingState.getCar()));
        btnNewCar.setToggleGroup(toggleGroup);

        btnTire = new ToggleButton();
        Image imgTire = new Image(getClass().getResource("Resources/Reifen24.png").toString());
        btnTire.setGraphic(new ImageView(imgTire));
        btnTire.setTooltip(new Tooltip("Reifen platzieren"));
        btnTire.setOnAction(event -> placingState.setSelected(PlacingState.getTires()));
        btnTire.setToggleGroup(toggleGroup);

        btnLight = new ToggleButton();
        Image imgLight = new Image(getClass().getResource("Resources/ampel24.png").toString());
        btnLight.setGraphic(new ImageView(imgLight));
        btnLight.setTooltip(new Tooltip("Ampel setzen"));
        btnLight.setOnAction(event -> placingState.setSelected(PlacingState.getLight()));
        btnLight.setToggleGroup(toggleGroup);

        btnDelete = new ToggleButton();
        Image imgDelete = new Image(getClass().getResource("Resources/Delete24.gif").toString());
        btnDelete.setGraphic(new ImageView(imgDelete));
        btnDelete.setTooltip(new Tooltip("Kachel löschen"));
        btnDelete.setOnAction(event -> placingState.setSelected(PlacingState.getClearTile()));
        btnDelete.setToggleGroup(toggleGroup);

        btnTiresInsideCar = new ToggleButton();
        Image imgReifenImAuto = new Image(getClass().getResource("Resources/reifenimauto.png").toString());
        btnTiresInsideCar.setGraphic(new ImageView(imgReifenImAuto));
        btnTiresInsideCar.setTooltip(new Tooltip("Anzahl der Reifen im Car"));
        btnTiresInsideCar.setOnAction(event -> {
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setHeaderText("Anzahl der Reifen im Auto:");
            info.setContentText("" + roadTraffic.getCarTires());
            info.showAndWait();
        });
        btnTiresInsideCar.setToggleGroup(toggleGroup);

        btnCarLeftAround = new ToggleButton();
        Image imgCarLeftAround = new Image(getClass().getResource("Resources/autodrehen.png").toString());
        btnCarLeftAround.setGraphic(new ImageView(imgCarLeftAround));
        btnCarLeftAround.setTooltip(new Tooltip("Car nach links drehen"));
        btnCarLeftAround.setOnAction(event -> roadTraffic.leftAround());
        btnCarLeftAround.setToggleGroup(toggleGroup);

        btnCarAgo = new ToggleButton();
        Image imgCarAgo = new Image(getClass().getResource("Resources/autogas.png").toString());
        btnCarAgo.setGraphic(new ImageView(imgCarAgo));
        btnCarAgo.setTooltip(new Tooltip("Car vorwärts bewegen"));
        btnCarAgo.setOnAction(event -> {
            try {
                roadTraffic.ago();
            } catch (Exception e) {
                loadMusic(mediaPlayer);
                mediaPlayer.play();
            }
        });
        btnCarAgo.setToggleGroup(toggleGroup);

        btnTakeTires = new ToggleButton();
        Image imgTakeTires = new Image(getClass().getResource("Resources/reifensammeln.png").toString());
        btnTakeTires.setGraphic(new ImageView(imgTakeTires));
        btnTakeTires.setTooltip(new Tooltip("Reifen aufsammeln"));
        btnTakeTires.setOnAction(event -> roadTraffic.take());
        btnTakeTires.setToggleGroup(toggleGroup);

        btnDeliverTires = new ToggleButton();
        Image imgDeliverTires = new Image(getClass().getResource("Resources/reifenablegen.png").toString());
        btnDeliverTires.setGraphic(new ImageView(imgDeliverTires));
        btnDeliverTires.setTooltip(new Tooltip("Reifen ablegen"));
        btnDeliverTires.setOnAction(event -> roadTraffic.put());
        btnDeliverTires.setToggleGroup(toggleGroup);

        btnPlay = new ToggleButton();
        Image imgPlay = new Image(getClass().getResource("Resources/Play24.png").toString());
        btnPlay.setGraphic(new ImageView(imgPlay));
        btnPlay.setTooltip(new Tooltip("Starten"));
        btnPlay.setOnAction(event -> {
            btnPlay.setDisable(true);
            btnBreak.setDisable(false);
            btnStop.setDisable(false);
            simulationManager.start(() -> {
                btnPlay.setDisable(false);
                btnStop.setDisable(true);
                btnBreak.setDisable(true);
            });
        });
        btnPlay.setToggleGroup(toggleGroup);

        btnBreak = new ToggleButton();
        Image imgBreak = new Image(getClass().getResource("Resources/Pause24.png").toString());
        btnBreak.setGraphic(new ImageView(imgBreak));
        btnBreak.setTooltip(new Tooltip("Pause"));
        btnBreak.setDisable(true);
        btnBreak.setOnAction(event -> {
            btnPlay.setDisable(false);
            btnStop.setDisable(false);
            btnBreak.setDisable(true);
            simulationManager.toggle(() -> {
                btnPlay.setDisable(true);
            });
        });
        btnBreak.setToggleGroup(toggleGroup);

        btnStop = new ToggleButton();
        Image imgStop = new Image(getClass().getResource("Resources/Stop24.png").toString());
        btnStop.setGraphic(new ImageView(imgStop));
        btnStop.setTooltip(new Tooltip("Stopp"));
        btnStop.setDisable(true);
        btnStop.setToggleGroup(toggleGroup);
        btnStop.setOnAction(e -> {
            btnBreak.setDisable(true);
            simulationManager.stop(()->btnPlay.setDisable(false));
            btnStop.setDisable(true);

        });

        Slider slider = createSlider();
        slider.valueProperty().addListener((observable, oldValue, newValue)
                -> simulationManager.setSpeed(newValue.intValue()));
        // Toolbar initialisieren und mit den Elementen befüllen
        this.hauptToolBar = new ToolBar(btnNew, btnOpen, new Separator(), btnSave, btnComp, new Separator(), btnTerrain, btnNewCar, btnTire, btnLight, btnDelete, new Separator(), btnTiresInsideCar, btnCarLeftAround, btnCarAgo, btnTakeTires, btnDeliverTires, new Separator(), btnPlay, btnBreak, btnStop, new Separator(), slider);
        // layout.getChildren().addAll(hauptToolBar);

        return this.hauptToolBar;
    }

    // Diese Methode entstand durch Hilfe von https://stackoverflow.com/questions/30687994/how-to-center-the-content-of-a-javafx-8-scrollpane
    // Diese Methode zentriert das Canvas
    void center(Bounds viewPortBounds, Canvas canvas) {
        double width = viewPortBounds.getWidth();
        double height = viewPortBounds.getHeight();

        if (width > canvas.getWidth()) {
            canvas.setTranslateX((width - canvas.getWidth()) / 2);
        } else {
            canvas.setTranslateX(0);
        }
        if (height > canvas.getHeight()) {
            canvas.setTranslateY((height - canvas.getHeight()) / 2);
        } else {
            canvas.setTranslateY(0);
        }
    }

    private void loadMusic(MediaPlayer mediaPlayer) {
        this.mediaPlayer = new MediaPlayer(Medium);
    }

    public static void createStage(String str) {
        RoadTraffic roadTraffic = new RoadTraffic();
        Stage stage = new Stage();
        try {
            Program program = new Program(str);
            CompileController.firstCompile(roadTraffic, program);
            ProgramController.add(program);
            SimulationManager simulationManager = new SimulationManager(roadTraffic);
            new SimulatorView(roadTraffic, stage, program, simulationManager);
            stage.show();
        } catch (IOException exc) {
            System.out.println("Fehler beim erstellen der Stage");
        } catch (FileCanNotWriteException | NoWritePermissionInDirectoryException | FileIsNotADirectoryException e) {
            e.printStackTrace();
        }
    }

    private Slider createSlider() {
        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(100);
        slider.setValue(50);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(50);
        slider.setTooltip(new Tooltip("Geschwindigkeit ändern"));
        return slider;
    }


    public RadioMenuItem getPlaceCarRadioItem() {
        return placeCarRadioItem;
    }

    public Stage getStage() {
        return stage;
    }

    public BorderPane getMainPane() {
        return mainPane;
    }

    public BorderPane getCenterPane() {
        return centerPane;
    }

    public SplitPane getSplitPane() {
        return splitPane;
    }

    public TextArea getTextArea() {
        return textArea;
    }

    public ScrollPane getSc() {
        return sc;
    }

    public Label getMeldungen() {
        return meldungen;
    }

    public MenuBar getMainMenuBar() {
        return mainMenuBar;
    }

    public ToolBar getHauptToolBar() {
        return hauptToolBar;
    }

    public RoadTrafficPanel getsPanel() {
        return sPanel;
    }

    public RoadTraffic getRoadTraffic() {
        return roadTraffic;
    }

    public ToggleGroup getToggleGroup() {
        return toggleGroup;
    }

    public Menu getEditor() {
        return editorMenu;
    }

    public Menu getWorldMenu() {
        return worldMenu;
    }

    public Menu getCarMenu() {
        return carMenu;
    }

    public Menu getSimulationMenu() {
        return simulationMenu;
    }

    public Menu getSaveMenu() {
        return saveMenu;
    }

    public Menu getPictureSaveMenu() {
        return pictureSaveMenu;
    }

    public Menu getLoadMenu() {
        return loadMenu;
    }

    public RadioMenuItem getPlaceTireRadioItem() {
        return placeTireRadioItem;
    }

    public RadioMenuItem getPlaceLightRadioItem() {
        return placeLightRadioItem;
    }

    public RadioMenuItem getClearRadioItem() {
        return clearRadioItem;
    }

    public MenuItem getPrintItem() {
        return printItem;
    }

    public MenuItem getChangeSizeItem() {
        return changeSizeItem;
    }

    public MenuItem getCloseItem() {
        return closeItem;
    }

    public MenuItem getPrintItem2() {
        return printItem2;
    }

    public MenuItem getOpenItem() {
        return openItem;
    }

    public MenuItem getNewItem() {
        return newItem;
    }

    public MenuItem getCompileItem() {
        return compileItem;
    }

    public MenuItem getXmlItem() {
        return xmlItem;
    }

    public MenuItem getJaxbItem() {
        return jaxbItem;
    }

    public MenuItem getSerialItem() {
        return serialItem;
    }

    public MenuItem getTiresInTrunkItem() {
        return tiresInTrunkItem;
    }

    public MenuItem getTakeItem() {
        return takeItem;
    }

    public MenuItem getDeliverItem() {
        return deliverItem;
    }

    public MenuItem getLeftAroundItem() {
        return leftAroundItem;
    }

    public MenuItem getAgoItem() {
        return agoItem;
    }

    public MenuItem getStartItem() {
        return startItem;
    }

    public MenuItem getFreezeItem() {
        return freezeItem;
    }

    public MenuItem getBreakItem() {
        return breakItem;
    }

    public ToggleButton getBtnNew() {
        return btnNew;
    }

    public ToggleButton getBtnOpen() {
        return btnOpen;
    }

    public ToggleButton getBtnSave() {
        return btnSave;
    }

    public ToggleButton getBtnComp() {
        return btnComp;
    }

    public ToggleButton getBtnTerrain() {
        return btnTerrain;
    }

    public ToggleButton getBtnNewCar() {
        return btnNewCar;
    }

    public ToggleButton getBtnTire() {
        return btnTire;
    }

    public ToggleButton getBtnLight() {
        return btnLight;
    }

    public ToggleButton getBtnDelete() {
        return btnDelete;
    }

    public ToggleButton getBtnTiresInsideCar() {
        return btnTiresInsideCar;
    }

    public ToggleButton getBtnCarLeftAround() {
        return btnCarLeftAround;
    }

    public ToggleButton getBtnCarAgo() {
        return btnCarAgo;
    }

    public ToggleButton getBtnTakeTires() {
        return btnTakeTires;
    }

    public ToggleButton getBtnDeliverTires() {
        return btnDeliverTires;
    }

    public ToggleButton getBtnPlay() {
        return btnPlay;
    }

    public ToggleButton getBtnBreak() {
        return btnBreak;
    }

    public ToggleButton getBtnStop() {
        return btnStop;
    }

    public void setBtnNewCar(ToggleButton tb) {
        this.btnNewCar = tb;
    }

    // Methode ist angelehnt an https://stackoverflow.com/questions/31556373/javafx-dialog-with-2-input-fields

    public void paintDialog() {
        this.dialog = new Dialog<>();
        dialog.setTitle("Grösse des Spielfeldes ändern");
        ButtonType loginButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButton, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField height = new TextField();
        height.setPromptText("5-100");
        TextField width = new TextField();
        width.setPromptText("5-100");
        dialog.getDialogPane().lookupButton(loginButton).setDisable(true);

        gridPane.add(new Label("Zeilen:"), 0, 0);
        gridPane.add(height, 1, 0);
        gridPane.add(new Label("Spalten:"), 2, 0);
        gridPane.add(width, 3, 0);

        height.textProperty().addListener((value, before, after) -> {
            checkInputOfDialog(loginButton, height, after, width);
        });

        width.textProperty().addListener((value, before, after) -> {
            checkInputOfDialog(loginButton, width, after, height);
        });

        dialog.getDialogPane().setContent(gridPane);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButton) {
                try {
                    int rows = Integer.parseInt(height.getText());
                    int cols = Integer.parseInt(width.getText());
                    roadTraffic.setSize(rows, cols);
                } catch (Exception exc) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Fehler bei der Eingabe");
                    alert.setHeaderText("Etwas ist schief gelaufen!");
                    alert.showAndWait();
                }
            }
            return null;
        });
        Optional<Pair<Integer, Integer>> result = dialog.showAndWait();
    }

    private void checkInputOfDialog(ButtonType loginButton, TextField textField, String after, TextField textField2) {
        if (!after.isEmpty()) {
            try {
                int value = Integer.parseInt(after);
                int secondValue;
                if (!textField2.getText().isEmpty()) {
                    secondValue = Integer.parseInt(textField2.getText());
                } else {
                    secondValue = 0;
                }
                if (value >= 5 && value <= 100 && secondValue >= 5 && secondValue <= 100) {
                    dialog.getDialogPane().lookupButton(loginButton).setDisable(false);
                } else {
                    dialog.getDialogPane().lookupButton(loginButton).setDisable(true);
                }
            } catch (Exception exc) {
                textField.setText("5");
                //dialog.getDialogPane().lookupButton(loginButton).setDisable(false);
            }
        } else {
            dialog.getDialogPane().lookupButton(loginButton).setDisable(true);
        }
    }
}
