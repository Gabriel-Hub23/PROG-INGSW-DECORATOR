package com.ppbarber.ppbarber.Controller;

import com.ppbarber.ppbarber.Model.*;
import com.ppbarber.ppbarber.SceneHandler;
import com.ppbarber.ppbarber.View.Dialog;
import com.ppbarber.ppbarber.View.MyInfo;
import com.itextpdf.text.DocumentException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class AppuntamentiController{
    @FXML
    private HBox scontrino_hbox;

    @FXML
    private HBox search_hbox;
    @FXML
    private TextField idField;

    @FXML
    private Label labelAppuntamenti;
    @FXML
    private Button removeButton;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private TextField idScontrino;
    @FXML
    private TextField cercaField;

    @FXML
    private TableColumn<Appuntamento, Date> colonnaData;

    @FXML
    private TableColumn<Appuntamento, String> colonnaDipendente;

    @FXML
    private TableColumn<Appuntamento, Integer> colonnaId;

    @FXML
    private TableColumn<Appuntamento, String> colonnaEmail;

    @FXML
    private TableColumn<Appuntamento, String> colonnaNomeCognome;

    @FXML
    private TableColumn<Appuntamento, String> colonnaNumero;
    @FXML
    private TableColumn<Appuntamento, Double> colonnaPrezzo;

    @FXML
    private TableColumn<Appuntamento, String> colonnaServizio;

    @FXML
    private Button cercaButton;

    @FXML
    private Button generaButton;

    @FXML
    private TableView<Appuntamento> table;

    @FXML
    private AnchorPane ancorPane;

    @FXML
    private ComboBox<String> filtroBox;


    private void setCellValue(){
        colonnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colonnaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colonnaData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colonnaPrezzo.setCellValueFactory(new PropertyValueFactory<>("prezzo"));
        colonnaDipendente.setCellValueFactory(new PropertyValueFactory<>("dipendente"));
        colonnaNomeCognome.setCellValueFactory(new PropertyValueFactory<>("identificativo"));
        colonnaNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colonnaServizio.setCellValueFactory(new PropertyValueFactory<>("servizio"));
    }



    private void aggiungiItems(){
        filtroBox.getItems().addAll("Id","Email","Nome","Cognome","Numero","Data","Dipendente","Servizio","Prezzo");
    }


    @FXML
    void aggiungiAppuntamento(ActionEvent event) throws IOException {

        Dialog.getInstance().requestDialog(Dialog.from.APPUNTAMENTI, Dialog.actions.AGGIUNGI, "-1", ancorPane);
    }



    @FXML
    void modificaAppuntamento(ActionEvent event) throws IOException {
        if(!idField.getText().equals("")){
            String [] parametri ={"Id", idField.getText()};
            if(!GestoreDbThreaded.getInstance().runQuery(5, GestoreDB.getInstance().getAppuntamenti(), parametri).toString().equals(idField.getText())){
                SceneHandler.getInstance().generaAlert("Id inserito non trovato.",false);
            }else {
                Dialog.getInstance().requestDialog(Dialog.from.APPUNTAMENTI, Dialog.actions.MODIFICA, idField.getText(), ancorPane);
            }
        }else{
            SceneHandler.getInstance().generaAlert("Non hai inserito l'id per modificare",false);
        }
    }



    @FXML
    void rimuoviAppuntamento(ActionEvent event) throws IOException {
        if(!idField.getText().equals("")){
            String [] parametri ={"Id",idField.getText()};
            if(!GestoreDbThreaded.getInstance().runQuery(5,GestoreDB.getInstance().getAppuntamenti(), parametri).toString().equals(idField.getText())){
                SceneHandler.getInstance().generaAlert("Id inserito non trovato.",false);
            }else {
                String [] par ={idField.getText()};
                GestoreDbThreaded.getInstance().runQuery(4, GestoreDB.entità.Appuntamenti, par);
            }
        }else{
            SceneHandler.getInstance().generaAlert("Non hai inserito l'id per modificare",false);
        }
        initialize();
    }



    @FXML
    void generaScontrino(ActionEvent event) throws DocumentException, FileNotFoundException, SQLException {
        if(idScontrino.getText().equals("")){
            SceneHandler.getInstance().generaAlert("Non hai inserito l'id da cercare.",false);
        }else {
            String [] parametri ={"Id", idScontrino.getText()};
            if(!GestoreDbThreaded.getInstance().runQuery(5,GestoreDB.getInstance().getAppuntamenti(), parametri).toString().equals(idScontrino.getText())){
                SceneHandler.getInstance().generaAlert("L'id inserito non esiste.", false);
            }else{
                GeneraScontrino.getInstance().generaScontrino(idScontrino.getText());
            }

        }
        idScontrino.setText("");
        idScontrino.setPromptText("Id");
    }



    @FXML
    void cerca(ActionEvent event) throws SQLException {
        if(filtroBox.getValue() == null){
            SceneHandler.getInstance().generaAlert("Non hai selezionato il filtro.", false);
        } else if (cercaField.getText().equals("")) {
            SceneHandler.getInstance().generaAlert("Non hai inserito il valore da cercare.",false);
        } else {
            table.getItems().clear();
            String filtro = "";
            switch (filtroBox.getValue()) {
                case "Id" -> filtro = "A.Id";
                case "Email" -> filtro = "C.Email";
                case "Nome" -> filtro = "C.Nome";
                case "Cognome" -> filtro = "C.Cognome";
                case "Numero" -> filtro = "C.Numero";
                case "Data" -> filtro = "A.Data";
                case "Dipendente" -> filtro = "D.Username";
                case "Servizio" -> filtro = "S.Tipo";
                case "Prezzo" -> filtro = "S.Prezzo";
            }
            ObservableList<Appuntamento> app = FXCollections.observableArrayList(GestoreAppuntamenti.getInstance().listaAppuntamenti(true,filtro, cercaField.getText()));
            table.setItems(app);
            setCellValue();
        }
        cercaField.setText("");
        cercaField.setPromptText("Cerca");
    }



    private void impostaTemi() throws IOException {
        if(MyInfo.getInstance().getFont().equals("Dyslexie")){
            labelAppuntamenti.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(),MyInfo.getInstance().getSizeLabel()));
            cercaField.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(),cercaField.getFont().getSize()-1));
            idField.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(),idField.getFont().getSize()-1));
            idScontrino.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(),idScontrino.getFont().getSize()-1));
            cercaButton.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(), cercaButton.getFont().getSize()-1));
            generaButton.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(), generaButton.getFont().getSize()-1));
            addButton.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(), addButton.getFont().getSize()-1));
            removeButton.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(), removeButton.getFont().getSize()-1));
            editButton.setFont(Font.loadFont(MyInfo.getInstance().getFontDyslexia(), editButton.getFont().getSize()-1));
        }else{
            labelAppuntamenti.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(),MyInfo.getInstance().getSizeLabel()));
            cercaField.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), cercaField.getFont().getSize()));
            idField.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), idField.getFont().getSize()));
            idScontrino.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), idScontrino.getFont().getSize()));
            cercaButton.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), cercaButton.getFont().getSize()));
            generaButton.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), generaButton.getFont().getSize()));
            addButton.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), addButton.getFont().getSize()));
            removeButton.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), removeButton.getFont().getSize()));
            editButton.setFont(Font.font(MyInfo.getInstance().getFontQuicksand(), editButton.getFont().getSize()));
        }
    }



    @FXML
    public void initialize() throws IOException {
        impostaTemi();
        aggiungiItems();
        table.getItems().clear();
        try {
            ArrayList<Appuntamento> app = GestoreAppuntamenti.getInstance().listaAppuntamenti(false, "", "");
            ObservableList<Appuntamento> observableApp = FXCollections.observableArrayList(app);
            table.setItems(observableApp);
            setCellValue();
        } catch (SQLException e) {
            SceneHandler.getInstance().generaAlert("Qualcosa è andato storto!", false);
        }
    }
}
