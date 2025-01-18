package com.ppbarber.ppbarber.Controller;

import com.ppbarber.ppbarber.Main;
import com.ppbarber.ppbarber.SceneHandler;
import com.ppbarber.ppbarber.View.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Objects;

public class HomeController {

    @FXML
    private AnchorPane anchorPaneParent;

    @FXML
    private ImageView logoView;

    @FXML
    private Pane viewPane;

    @FXML
    private VBox vbox;

    @FXML
    private void avviaPane(String nomeVista) throws IOException {
        viewPane.getChildren().clear();

        FXMLLoader loader = SceneHandler.getInstance().creaPane(nomeVista);
        Pane pane = loader.load();

        SceneHandler.getInstance().setRightPaneContainerContent(pane);

        pane.prefWidthProperty().bind(viewPane.widthProperty());
        pane.prefHeightProperty().bind(viewPane.heightProperty());

        viewPane.layoutBoundsProperty().addListener(obs -> {
            pane.setPrefSize(viewPane.getWidth(), viewPane.getHeight());
        });

        Dialog.getInstance().setAnchorPaneFather(anchorPaneParent);
        viewPane.getChildren().add(pane);

        System.out.println("Vista caricata con successo: " + nomeVista);
    }

    public void initialize() {
        try {

            Image image = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/com/ppbarber/ppbarber/img/Logo.png")));
            logoView.setImage(image);

            int BUTTONS_WIDTH = 140;


            ButtonInterface dashboard = creaPulsante("DashboardView", "/com/ppbarber/ppbarber/img/dashboard.png", "Vai alla Dashboard", BUTTONS_WIDTH);
            ButtonInterface appuntamenti = creaPulsante("AppuntamentiView", "/com/ppbarber/ppbarber/img/event.png", "Gestisci appuntamenti", BUTTONS_WIDTH);
            ButtonInterface statistiche = creaPulsante("StatisticheView", "/com/ppbarber/ppbarber/img/stats.png", "Visualizza statistiche", BUTTONS_WIDTH);
            ButtonInterface servizi = creaPulsante("ServiziView", "/com/ppbarber/ppbarber/img/task.png", "Gestisci servizi", BUTTONS_WIDTH);
            ButtonInterface clienti = creaPulsante("ClientiView", "/com/ppbarber/ppbarber/img/clients.png", "Gestisci clienti", BUTTONS_WIDTH);
            ButtonInterface dipendenti = creaPulsante("DipendentiView", "/com/ppbarber/ppbarber/img/employee.png", "Gestisci dipendenti", BUTTONS_WIDTH);
            ButtonInterface impostazioni = creaPulsante("ImpostazioniView", "/com/ppbarber/ppbarber/img/settings.png", "Modifica impostazioni", BUTTONS_WIDTH);


            ButtonInterface logout = creaPulsante("Logout", "/com/ppbarber/ppbarber/img/logout.png", "Esci dall'applicazione", BUTTONS_WIDTH);
            logout.getButton().getStyleClass().add("sidebar-logout-button");


            vbox.getChildren().addAll(
                    dashboard.getButton(),
                    appuntamenti.getButton(),
                    statistiche.getButton(),
                    servizi.getButton(),
                    clienti.getButton(),
                    dipendenti.getButton(),
                    impostazioni.getButton(),
                    logout.getButton()
            );


            impostaAzioniPulsanti(dashboard, appuntamenti, statistiche, servizi, clienti, dipendenti, impostazioni, logout);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private ButtonInterface creaPulsante(String testo, String iconaPath, String tooltip, int larghezza) {

        ButtonInterface basicButton = new BasicButton();
        basicButton.setText(testo);
        basicButton.setTooltipText(tooltip);


        ButtonInterface decoratedButton = new ButtonDecorator(basicButton);


        try {
            ImageView icona = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconaPath))));
            icona.setFitWidth(20);
            icona.setFitHeight(20);
            decoratedButton.getButton().setGraphic(icona);
        } catch (Exception e) {
            System.err.println("Errore caricamento immagine: " + iconaPath);
            e.printStackTrace();
        }

        decoratedButton.getButton().setPrefWidth(larghezza);
        decoratedButton.getButton().getStyleClass().add("sidebar-button");

        return decoratedButton;
    }


    private void impostaAzioniPulsanti(ButtonInterface... pulsanti) {
        for (ButtonInterface pulsante : pulsanti) {
            pulsante.getButton().setOnAction(event -> {
                String testo = pulsante.getButton().getText().trim();
                if (testo.equals("Logout")) {
                    SceneHandler.getInstance().launchLogin();
                    return;
                }


                try { avviaPane(testo); }
                catch (IOException e) { throw new RuntimeException(e); }
            });
        }
    }
}