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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

//import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.nio.file.Files;
import java.nio.file.Path;

public class serverController implements Initializable {

    @FXML
    private TabPane EmogiPane;

    @FXML
    private VBox EmojiPickerBox;

    @FXML
    private VBox ServerMassegeBox;

    @FXML
    private ScrollPane ServerscrollPane;

    @FXML
    private TextField Servertxt;

    @FXML
    private Button btnEmoji;

    @FXML
    private Button btnImage;

    @FXML
    private Button btnSend;

    private ServerSocket serverSocket;
    private Socket socket;
    private Socket socket1;
    private Socket socket2;
    private DataInputStream dataInputStream1;
    private DataOutputStream dataOutputStream1;
    private String message = "";
    private DataInputStream dataInputStream2;
    private DataOutputStream dataOutputStream2;
    private DataInputStream dataInputStream3;
    private DataOutputStream dataOutputStream3;
    private String client;

    private final String[][] emoji = {
            {"😀", "😃", "😄", "😁", "😅", "😂", "🤣"},
            {"❤️", "🧡", "💛", "💚", "💙", "💜", "🖤"}
    };

    private final String[] categories = {"Smileys", "Hearts"};


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try{
            initializeThread1();
            initializeThread2();
            initializeThread3();
            initializeEmojiBar();
            EmojiPickerBox.setVisible(false);

            ServerMassegeBox.setSpacing(10);
            ServerscrollPane.setContent(ServerMassegeBox);
            ServerscrollPane.setFitToHeight(true);

            ServerMassegeBox.heightProperty().addListener((observable, oldValue, newValue) ->
                    ServerscrollPane.setVvalue(1.0));


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initializeThread1(){
        client = "client1";

        new Thread(()->{
            try{
                serverSocket = new ServerSocket(5000);


                socket = serverSocket.accept();

                Platform.runLater(() -> addMessage("Server Connected") );
                Platform.runLater(() -> addMessage("Client_1 Logging") );
                Platform.runLater(() -> addMessage("Client_2 Logging") );
                Platform.runLater(() -> addMessage("Client_3 Logging") );

                dataInputStream1 = new DataInputStream(socket.getInputStream());
                dataOutputStream1 = new DataOutputStream(socket.getOutputStream());

                while(!message.equals("Exit")){
                    message = dataInputStream1.readUTF();

                    if (message.equals("[IMAGE]")) {
                        int length = dataInputStream1.readInt();
                        byte[] imageBytes = new byte[length];
                        dataInputStream1.readFully(imageBytes);

                        Platform.runLater(() -> {
                            ImageVisibility(imageBytes);
                            addMessage("Client 1: [Image Received]");
                        });

                        if (dataOutputStream2 != null) {
                            dataOutputStream2.writeUTF("[IMAGE]");
                            dataOutputStream2.writeInt(length);
                            dataOutputStream2.write(imageBytes);
                            dataOutputStream2.flush();
                        }
                        if (dataOutputStream3 != null) {
                            dataOutputStream3.writeUTF("[IMAGE]");
                            dataOutputStream3.writeInt(length);
                            dataOutputStream3.write(imageBytes);
                            dataOutputStream3.flush();
                        }
                    }else {
                        Platform.runLater(() -> addMessage("Client 1: " + message));
                    }

                    if (dataOutputStream2 != null) {
                        dataOutputStream2.writeUTF("Client 1 : " +message);
                        dataOutputStream2.flush();
                    }
                    if (dataOutputStream3 != null) {
                        dataOutputStream3.writeUTF("Client 1 : " +message);
                        dataOutputStream3.flush();
                    }

                }
            } catch (IOException e) {
                Platform.runLater(() -> addMessage("Error: an error occur in your connection") );
                throw new RuntimeException(e);

            }
        }).start();
    }
    private void initializeThread2(){
        client = "client2";
        new Thread(()->{
            try {
                serverSocket = new ServerSocket(5001);


                socket1 = serverSocket.accept();

                Platform.runLater(() -> addMessage("Client 2 Connected") );

                dataInputStream2 = new DataInputStream(socket1.getInputStream());
                dataOutputStream2 = new DataOutputStream(socket1.getOutputStream());

                while(!message.equals("Exit")){
                    message = dataInputStream2.readUTF();
                    if (message.equals("[IMAGE]")) {
                        int length = dataInputStream2.readInt();
                        byte[] imageBytes = new byte[length];
                        dataInputStream2.readFully(imageBytes);

                        Platform.runLater(() -> {
                            ImageVisibility(imageBytes);
                            addMessage("Client 1: [Image Received]");
                        });

                        if (dataOutputStream1 != null) {
                            dataOutputStream1.writeUTF("[IMAGE]");
                            dataOutputStream1.writeInt(length);
                            dataOutputStream1.write(imageBytes);
                            dataOutputStream1.flush();
                        }
                        if (dataOutputStream3 != null) {
                            dataOutputStream3.writeUTF("[IMAGE]");
                            dataOutputStream3.writeInt(length);
                            dataOutputStream3.write(imageBytes);
                            dataOutputStream3.flush();
                        }
                    }else {
                        Platform.runLater(() -> addMessage("Client 2: " + message));
                    }

                    if (dataOutputStream1 != null) {
                        dataOutputStream1.writeUTF("Client 2 : " +message);
                        dataOutputStream1.flush();
                    }
                    if (dataOutputStream3 != null) {
                        dataOutputStream3.writeUTF("Client 2 : " +message);
                        dataOutputStream3.flush();
                    }

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
    private void initializeThread3(){
        client = "client3";
        new Thread(()->{
            try {
                serverSocket = new ServerSocket(5002);


                socket2 = serverSocket.accept();

                Platform.runLater(() -> addMessage("Client 3 Connected") );

                dataInputStream3 = new DataInputStream(socket2.getInputStream());
                dataOutputStream3 = new DataOutputStream(socket2.getOutputStream());

                while(!message.equals("Exit")){
                    message = dataInputStream3.readUTF();
                    if (message.equals("[IMAGE]")) {
                        int length = dataInputStream3.readInt();
                        byte[] imageBytes = new byte[length];
                        dataInputStream3.readFully(imageBytes);

                        Platform.runLater(() -> {
                            ImageVisibility(imageBytes);
                            addMessage("Client 1: [Image Received]");
                        });

                        if (dataOutputStream1 != null) {
                            dataOutputStream1.writeUTF("[IMAGE]");
                            dataOutputStream1.writeInt(length);
                            dataOutputStream1.write(imageBytes);
                            dataOutputStream1.flush();
                        }
                        if (dataOutputStream2 != null) {
                            dataOutputStream2.writeUTF("[IMAGE]");
                            dataOutputStream2.writeInt(length);
                            dataOutputStream2.write(imageBytes);
                            dataOutputStream2.flush();
                        }
                    }else {
                        Platform.runLater(() -> addMessage("Client 2: " + message));
                    }

                    if (dataOutputStream1 != null) {
                        dataOutputStream1.writeUTF("Client 3 : " +message);
                        dataOutputStream1.flush();
                    }
                    if (dataOutputStream2 != null) {
                        dataOutputStream2.writeUTF("Client 3 : " +message);
                        dataOutputStream2.flush();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private void initializeEmojiBar(){
        for (int i = 0; i < categories.length; i++){
            Tab tab = new Tab(categories[i]);
            FlowPane flowPane = new FlowPane();
            flowPane.setHgap(5);
            flowPane.setVgap(5);
            flowPane.setPrefWrapLength(200);

            for(String emoji : emoji[i]){
                Button button = new Button(emoji);
                button.setStyle("-fx-font-size: 20px; -fx-background-color: yellow; -fx-background-radius: 20px;");
                button.setOnAction(e ->{
                    Servertxt.appendText(emoji);
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
    void FileSendOnAction(ActionEvent event) {

    }

    @FXML
    void SendOnAction(ActionEvent event) {
        try{
            String Message = Servertxt.getText().trim();
            if(!Message.isEmpty()){
                if (dataOutputStream1 != null) {
                    dataOutputStream1.writeUTF(Message);
                    dataOutputStream1.flush();
                }
                if (dataOutputStream2 != null) {
                    dataOutputStream2.writeUTF(Message);
                    dataOutputStream2.flush();
                }
                if (dataOutputStream3 != null) {
                    dataOutputStream3.writeUTF(Message);
                    dataOutputStream3.flush();
                }
                addMessage("Server: " + Message);
                Servertxt.clear();          }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void addMessage(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        ServerMassegeBox.getChildren().add(label);
    }

    public void txtMessageOnAction(ActionEvent actionEvent) {

        SendOnAction(actionEvent);
    }

    public void ImageSendOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                byte[] imageBytes = Files.readAllBytes(file.toPath());

                if (dataOutputStream1 != null) {
                    sendImage(dataOutputStream1, imageBytes);
                }
                if (dataOutputStream2 != null) {
                    sendImage(dataOutputStream2, imageBytes);
                }
                if (dataOutputStream3 != null) {
                    sendImage(dataOutputStream3, imageBytes);
                }

                ImageVisibility(imageBytes);

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

    private void ImageVisibility(byte[] imageBytes) {
        try {
            InputStream is = new ByteArrayInputStream(imageBytes);
            Image image = new Image(is);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(150);
            imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);
            ServerMassegeBox.getChildren().add(imageView);
        } catch (Exception e) {
            addMessage("Error displaying image");
            e.printStackTrace();
        }
    }
}

