package lk.ijse.chat.exam.chatappexam.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

//import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class clientController2 implements Initializable {

    @FXML
    private VBox Client1MassegeBox;

    @FXML
    private ScrollPane Client1scrollPane;

    @FXML
    private TextField Client1txt;

    @FXML
    private TabPane EmogiPane;

    @FXML
    private VBox EmojiPickerBox;

    @FXML
    private Button btnEmoji;

    @FXML
    private Button btnImage;

    @FXML
    private Button btnSend;


    private Socket socket;
    private DataInputStream dataInputStream4;
    private DataOutputStream dataOutputStream4;
    private String messege = "";

    private final String[][] emoji = {
            {"😀", "😃", "😄", "😁", "😅", "😂", "🤣"},
            {"❤️", "🧡", "💛", "💚", "💙", "💜", "🖤"}
    };
    private  final  String[] categories = {"Smileys", "Hearts"};


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeEmojiBar();
        EmojiPickerBox.setVisible(false);

        if(Client1MassegeBox != null) {
            Client1MassegeBox.setSpacing(10);
            Client1scrollPane.setContent(Client1MassegeBox);
            Client1scrollPane.setFitToHeight(true);

            Client1MassegeBox.heightProperty().addListener((observable, oldValue, newValue) ->
                    Client1scrollPane.setVvalue(1.0));

        }else {
            System.out.println("massegeVbox is null");
        }
        new Thread(() -> {
            try {
                socket = new Socket("localhost", 5000);


                Platform.runLater(() -> addMessage("Client 2 "));
                Platform.runLater(() -> addMessage("Client 1 Logging"));
                Platform.runLater(() -> addMessage("Client 3 Logging"));

                dataInputStream4 = new DataInputStream(socket.getInputStream());
                dataOutputStream4 = new DataOutputStream(socket.getOutputStream());

                while (!messege.equals("exit")) {
                    messege = dataInputStream4.readUTF();

                    if (messege.equals("[IMAGE]")) {
                        int length = dataInputStream4.readInt();
                        byte[] imageBytes = new byte[length];
                        dataInputStream4.readFully(imageBytes);

                        Platform.runLater(() -> {
                            ImageDisplay(imageBytes);
                            addMessage("Client: [Image Received]");
                        });
                    }else {

                        Platform.runLater(() -> addMessage( messege));
                    }

                }


            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }).start();
    }
    private void addMessage(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14px;");
        label.setWrapText(true);
        Client1MassegeBox.getChildren().add(label);
    }

    private void initializeEmojiBar(){
        for(int i = 0; i < categories.length; i++){
            Tab tab = new Tab(categories[i]);
            FlowPane flowPane = new FlowPane();
            flowPane.setHgap(5);
            flowPane.setVgap(5);
            flowPane.setPrefWrapLength(200);

            for(String emoji : emoji[i]){
                Button button = new Button(emoji);
                button.setStyle("-fx-font-size: 20px; -fx-background-color: transparent;");
                button.setOnAction(e -> {
                    Client1txt.appendText(emoji);
                    EmojiPickerBox.setVisible(false);
                });
                flowPane.getChildren().add(button);
            }
            tab.setContent(flowPane);
            EmogiPane.getTabs().add(tab);
        }
    }



    @FXML
    void EmojiSendOnAction(ActionEvent event) {
        EmojiPickerBox.setVisible(!EmojiPickerBox.isVisible());
    }


    @FXML
    void SendOnAction(ActionEvent event) {
        try {
            String message = Client1txt.getText().trim();
            if (!message.isEmpty()) {
                dataOutputStream4.writeUTF(message);
                dataOutputStream4.flush();
                addMessage("Client: " + message);
                Client1txt.clear();
            }
        } catch (IOException e) {
            addMessage("Error: Failed to send message");
            e.printStackTrace();
        }
    }

    public void ImageSendOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                byte[] imageBytes = Files.readAllBytes(file.toPath());

                if (dataOutputStream4 != null) {
                    sendImage(dataOutputStream4, imageBytes);
                }

                ImageDisplay(imageBytes);

            } catch (IOException e) {
                addMessage("Error reading image file");
                e.printStackTrace();
            }
        }

    }
    private void sendImage(DataOutputStream dos, byte[] imageBytes) throws IOException {
        dos.writeUTF("[IMAGE]");
        dos.writeInt(imageBytes.length);
        dos.write(imageBytes);
        dos.flush();
    }

    public void txtMessageOnAction(ActionEvent actionEvent) {
    }
    private void ImageDisplay(byte[] imageBytes) {
        try {
            InputStream is = new ByteArrayInputStream(imageBytes);
            Image image = new Image(is);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(150);
            imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);
            Client1MassegeBox.getChildren().add(imageView);
        } catch (Exception e) {
            addMessage("Error displaying image");
            e.printStackTrace();
        }
    }
}


