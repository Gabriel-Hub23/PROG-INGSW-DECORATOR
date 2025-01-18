// File: BasicButton.java
package com.ppbarber.ppbarber.View;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

public class BasicButton implements ButtonInterface {
    private final Button button;

    public BasicButton() {
        this.button = new Button();
    }

    @Override
    public void setText(String text) {
        button.setText(text);
    }

    @Override
    public void setTooltipText(String text) {
        button.setTooltip(new Tooltip(text));
    }

    @Override
    public Button getButton() {
        return button;
    }
}
