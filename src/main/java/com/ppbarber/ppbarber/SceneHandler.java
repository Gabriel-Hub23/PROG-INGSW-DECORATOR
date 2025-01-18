package com.ppbarber.ppbarber;

import com.ppbarber.ppbarber.Controller.LoginController;
import com.ppbarber.ppbarber.Model.GestoreDB;
import com.ppbarber.ppbarber.View.MyInfo;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.exit;

public class SceneHandler {

    private static final SceneHandler instance = new SceneHandler();
    private Stage stage;
    private Scene scene;
    private FXMLLoader loader;

    private DialogPane dialog;
    private static final Logger LOGGER = Logger.getLogger(SceneHandler.class.getName());

    // Singleton instance
    public static SceneHandler getInstance() {
        return instance;
    }

    private SceneHandler() {}

    public void init(Stage stage) throws SQLException {
        if (this.stage == null) {
            this.stage = stage;
            stage.setTitle(Settings.INIT_TITLE);
            launchWelcomeFirstOpening();
            stage.setScene(scene);
            GestoreDB.getInstance().createConnection();
            MyInfo.getInstance().prendiDati();
            loadStyle();
            stage.show();
            stage.setOnCloseRequest(event -> {
                try {
                    GestoreDB.getInstance().closeConnection();
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Errore nella chiusura della connessione al DB", e);
                }
                exit(0);
            });
        }
    }

    private void loadFXML(String resourceName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SceneHandler.class.getResource(resourceName));
            if (scene == null) {
                scene = new Scene(fxmlLoader.load());
            } else {
                scene.setRoot(fxmlLoader.load());
            }
            loader = fxmlLoader;

        } catch (IOException e) {
            generaAlert("Errore nel caricamento della vista!", false);
            LOGGER.log(Level.SEVERE, "Errore nel caricamento di FXML", e);
        }
    }

    public void launchWelcomeFirstOpening() {
        loadFXML("/com/ppbarber/ppbarber/fxml/WelcomePage.fxml");
        setWindowLoginDimension();
    }

    public void launchLogin() {
        loadFXML("/com/ppbarber/ppbarber/fxml/LoginView.fxml");
        LoginController controller = loader.getController();
        controller.init(stage);
        setWindowLoginDimension();
    }

    public void launchDashboard() {
        setWindowAppDimension();
        loadFXML("/com/ppbarber/ppbarber/fxml/HomeView.fxml");
    }

    public FXMLLoader creaPane(String nomeVista) {
        try {
            String path = "/com/ppbarber/ppbarber/fxml/" + nomeVista + ".fxml";
            System.out.println(path);
            FXMLLoader loader = new FXMLLoader(SceneHandler.class.getResource(path));
            if (loader.getLocation() == null) {
                throw new IOException("File FXML non trovato: " + path);
            }
            return loader;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Errore nel caricamento del file FXML: {0}", nomeVista);
            return null;
        }
    }

    private void setWindowLoginDimension() {
        stage.setMinHeight(Settings.MIN_WINDOW_HEIGHT);
        stage.setMinWidth(Settings.MIN_WINDOW_WIDTH_LOGIN);
        stage.setHeight(Settings.DEFAULT_WINDOW_HEIGHT);
        stage.setWidth(Settings.DEFAULT_WINDOW_WIDTH);
        stage.setResizable(true);
    }

    private void setWindowAppDimension() {
        stage.setMinHeight(Settings.MIN_WINDOW_HEIGHT);
        stage.setMinWidth(Settings.MIN_WINDOW_WIDTH_APP);
        stage.setHeight(Settings.DEFAULT_WINDOW_HEIGHT);
        stage.setWidth(Settings.DEFAULT_WINDOW_WIDTH);
        stage.setResizable(true);
    }

    private void loadStyle() {
        for (String font : Settings.fonts)
            Font.loadFont(Objects.requireNonNull(SceneHandler.class.getResource(font)).toExternalForm(), 10);

        for (String style : Settings.styles)
            scene.getStylesheets().add(Objects.requireNonNull(SceneHandler.class.getResource(style)).toExternalForm());

        setTheme();
    }

    public void setTheme() {
        String tema = MyInfo.getInstance().getTema();
        if (tema == null) {
            tema = "DARK"; // Tema di default
        }

        String pathTheme = switch (tema) {
            case "DARK" -> Settings.themes[0];
            case "LIGHT" -> Settings.themes[1];
            case "BLU" -> Settings.themes[2];
            default -> {
                LOGGER.severe("Tema non valido: " + tema);
                exit(11);
                yield Settings.themes[0];
            }
        };

        scene.getStylesheets().clear();
        scene.getStylesheets().add(Objects.requireNonNull((SceneHandler.class.getResource(Settings.styles[0])).toExternalForm()));
        scene.getStylesheets().add(Objects.requireNonNull(SceneHandler.class.getResource(pathTheme)).toExternalForm());
    }

    public void generaAlert(String testo, boolean notifica) {
        Alert alert = notifica ? new Alert(Alert.AlertType.INFORMATION) : new Alert(Alert.AlertType.WARNING);

        String tema = MyInfo.getInstance().getTema();
        if (tema == null) {
            tema = "DARK"; // Tema di default
        }

        String pathTheme = switch (tema) {
            case "DARK" -> Settings.themes[0];
            case "LIGHT" -> Settings.themes[1];
            case "BLU" -> Settings.themes[2];
            default -> {
                LOGGER.severe("Tema non valido: " + tema);
                exit(11);
                yield Settings.themes[0];
            }
        };

        dialog = alert.getDialogPane();
        dialog.getStylesheets().add(Objects.requireNonNull((SceneHandler.class.getResource(Settings.styles[0])).toExternalForm()));
        dialog.getStylesheets().add(Objects.requireNonNull(SceneHandler.class.getResource(pathTheme)).toExternalForm());
        dialog.getStyleClass().add("alert");

        alert.setTitle(notifica ? "Notifica" : "Attenzione");
        alert.setHeaderText(notifica ? "Informazione" : "Errore");
        alert.setContentText(testo);
        alert.setResizable(false);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.showAndWait();
    }

    public boolean generaAlertConfirm() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        dialog = alert.getDialogPane();
        String pathTheme = switch (MyInfo.getInstance().getTema()) {
            case "DARK" -> Settings.themes[0];
            case "LIGHT" -> Settings.themes[1];
            case "BLU" -> Settings.themes[2];
            default -> {
                LOGGER.severe("Tema non valido: " + MyInfo.getInstance().getTema());
                exit(11);
                yield Settings.themes[0];
            }
        };
        dialog.getStylesheets().add(Objects.requireNonNull((SceneHandler.class.getResource(Settings.styles[0])).toExternalForm()));
        dialog.getStylesheets().add(Objects.requireNonNull(SceneHandler.class.getResource(pathTheme)).toExternalForm());
        dialog.getStyleClass().add("alert");
        alert.setTitle("Conferma");
        alert.setHeaderText("Confermi di voler eliminare i dati?");
        alert.setContentText("Nota: Dovrai inserire il proprietario.\n(Con username e password \n uguale a Nome.Cognome.1)");
        alert.setResizable(false);
        alert.initStyle(StageStyle.UNDECORATED);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    Pane paneRightContainerContent = null;

    public void setRightPaneContainerContent(Pane paneRightContainerContent) {
        this.paneRightContainerContent = paneRightContainerContent;
    }

    public Pane getPaneRightContainerContent() {
        return paneRightContainerContent;
    }

    public String apriDirectoryChooser() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Scegli cartella");
        String name = "";
        try {
            name = chooser.showDialog(stage).getAbsolutePath();
        } catch (NullPointerException e) {
            LOGGER.log(Level.WARNING, "DirectoryChooser returned null", e);
        }
        return name;
    }
}
