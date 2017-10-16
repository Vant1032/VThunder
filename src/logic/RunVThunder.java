package logic;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import viewModel.VThunderUI;


public class RunVThunder extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Platform.setImplicitExit(true);
        VThunderUI vThunderUI = new VThunderUI();
    }
}