// File: ButtonDecorator.java
package com.ppbarber.ppbarber.View;

import javafx.scene.control.Button;

public class ButtonDecorator implements ButtonInterface {
    protected final ButtonInterface decoratedButton;

    public ButtonDecorator(ButtonInterface decoratedButton) {
        this.decoratedButton = decoratedButton;
    }

    @Override
    public void setText(String text) {
        decoratedButton.setText(text);
    }

    @Override
    public void setTooltipText(String text) {
        decoratedButton.setTooltipText(text);
    }

    @Override
    public Button getButton() {
        return decoratedButton.getButton();
    }
}
