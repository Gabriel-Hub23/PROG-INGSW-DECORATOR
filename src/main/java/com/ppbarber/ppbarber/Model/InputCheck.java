package com.ppbarber.ppbarber.Model;

import com.ppbarber.ppbarber.SceneHandler;

public class InputCheck {

    public InputCheck() { }

    public static boolean checkFloat(String newValue) {
        try {
            Double.parseDouble(newValue);
            return true;
        } catch (Exception e) {
            SceneHandler.getInstance().generaAlert("Qualcosa è andato storto!", false);
            return false;
        }
    }

    public static boolean checkLettersNumbers(String value) {
        if (value.length() < 3)
            return false;

        if (!value.matches("[a-zA-Z0-9 ]+[a-zA-Z0-9 ]"))
            return false;

        return true;
    }

    public static boolean checkNameLastname(String newValue) {
        if (newValue.length() < 3)
            return false;

        if (!newValue.matches("[a-zA-Z ]+[a-zA-Z ]")) {
            return false;
        }

        return true;
    }

    public static boolean checkDate(String value) {
        if (value.length() == 0)
            return false;

        if (!value.matches("[0-9]{2}/[0-9]{2}/[0-9]{4}")) {
            return false;
        }


        return true;
    }

    public static boolean checkInt(String newValue) {
        if (newValue.matches("[0-9]+[0-9]*")) {
            return true;
        }
        return false;
    }

    public static boolean checkEmail(String newValue) {
        if (newValue.matches("[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}")) {
            return true;
        }

        return false;
    }
}
