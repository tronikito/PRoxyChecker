package sample;

import javafx.scene.control.Button;

public class EnableBtn implements Runnable {

    private Button btn;

    public EnableBtn(Button btn) {
        this.btn = btn;
    }

    @Override
    public void run() {
        if (btn.isDisable())  btn.setDisable(false);
        else if (!btn.isDisable()) btn.setDisable(true);
    }
}

