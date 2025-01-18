package com.ppbarber.ppbarber.Controller;

import com.ppbarber.ppbarber.Model.*;
import com.ppbarber.ppbarber.SceneHandler;
import com.ppbarber.ppbarber.View.CardContainer;
import com.ppbarber.ppbarber.View.Dialog;
import com.ppbarber.ppbarber.View.MyInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

public class ServiziViewController {

    @FXML
    private Button addButton;

    @FXML
    private AnchorPane ancorPane;

    @FXML
    private HBox search_hbox;
    @FXML
    private TextField cercaField;

    @FXML
    private Button searchButton;

    @FXML
    private VBox vboxEsterno;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label labelServizi;

    @FXML
    private ComboBox <String> filtroBox;

    @FXML
    void actionAddButton(ActionEvent event) {
        System.out.println("sono prima");
        Dialog.getInstance().requestDialog(Dialog.from.SERVIZI, Dialog.actions.AGGIUNGI, "-1", ancorPane);
        System.out.println("so dopo");
    }


    @FXML
    void actionSearchButton(ActionEvent event) {
        if(filtroBox.getValue() == null){
            SceneHandler.getInstance().generaAlert("Non hai selezionato il filtro.", false);
        }else if(cercaField.getText().isEmpty()){
            SceneHandler.getInstance().generaAlert("Non hai inserito il valore da cercare.", false);
        }else{
            generaCard(true, filtroBox.getValue(), cercaField.getText());
        }
    }

    private void generaCard(boolean cerca, String filtro, String valore){
        vboxEsterno.getChildren().clear();
        LinkedList<Servizio> res = null;

        if(!cerca){
            res = ReusableDBResultsConverter.getInstance().getServizi((ArrayList<String>) GestoreDbThreaded.getInstance().runQuery(1, GestoreDB.entità.Servizi, null));

        }else{
            ArrayList<String> servizi = new ArrayList<String>();
            String [] parametri = {filtro , valore};
            servizi.add( (String) GestoreDbThreaded.getInstance().runQuery(6, GestoreDB.entità.Servizi, parametri));
            res = ReusableDBResultsConverter.getInstance().getServizi(servizi);
        }
        CardContainer.getInstance().setCardContainer(res, vboxEsterno);
    }
    @FXML
    void initialize() throws IOException {
        vboxEsterno.setSpacing(15);
        if(MyInfo.getInstance().getFont().equals("Dyslexie")){
            labelServizi.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(),MyInfo.getInstance().getSizeLabel()));
            cercaField.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(),cercaField.getFont().getSize()-1));
            searchButton.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(), searchButton.getFont().getSize()-1));
            addButton.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(), addButton.getFont().getSize()-1));
        }else{
            labelServizi.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(),MyInfo.getInstance().getSizeLabel()));
            cercaField.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), cercaField.getFont().getSize()));
            searchButton.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), searchButton.getFont().getSize()));
            addButton.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), addButton.getFont().getSize()));
        }
        filtroBox.getItems().addAll("Id", "Tipo", "Prezzo");

        generaCard(false, "", "");
    }


}
