package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class AddTaskController {
    @FXML
    private TextArea downloadAddress;
    @FXML
    private Button download;
    @FXML
    private TextField fileName;
    @FXML
    private Text fileSize;
    @FXML
    private TextField saveDirectory;
    @FXML
    private ImageView openFileChooser;

    public TextArea getDownloadAddress() {
        return downloadAddress;
    }

    public Button getDownload() {
        return download;
    }

    public TextField getFileName() {
        return fileName;
    }

    public Text getFileSize() {
        return fileSize;
    }

    public TextField getSaveDirectory() {
        return saveDirectory;
    }

    public ImageView getOpenFileChooser() {
        return openFileChooser;
    }


}
