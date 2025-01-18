package com.ppbarber.ppbarber.Model;

import java.sql.SQLException;
import java.util.ArrayList;

public class GestoreAppuntamenti {

    private static final GestoreAppuntamenti instance = new GestoreAppuntamenti();

    public static GestoreAppuntamenti getInstance() {
        return instance;
    }
    private GestoreAppuntamenti(){}

    public ArrayList<Appuntamento> listaAppuntamenti (boolean cerca, String filtro, String valore) throws SQLException {
        ArrayList <Appuntamento> lista = new ArrayList<Appuntamento>();
        String [] parametri ={String.valueOf(cerca),filtro, valore};
        ArrayList <String> query = (ArrayList<String>) GestoreDbThreaded.getInstance().runQuery(11, null, parametri);
        for (String i : query){
            Appuntamento a = new Appuntamento(i.split(";"));
            lista.add(a);
        }
        return lista;
    }


}
