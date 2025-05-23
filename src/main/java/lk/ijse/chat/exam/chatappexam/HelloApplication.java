package lk.ijse.chat.exam.chatappexam;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader serverLoader = new FXMLLoader(getClass().getResource("/view/server.fxml"));
        Parent serverRoot = serverLoader.load();
        Stage serverStage = new Stage();
        serverStage.setTitle("Server");
        serverStage.setScene(new Scene(serverRoot));
        serverStage.show();


        FXMLLoader client1Loader = new FXMLLoader(getClass().getResource("/view/client.fxml"));
        Parent client1Root = client1Loader.load();
        Stage client1Stage = new Stage();
        client1Stage.setTitle("Client 01");
        client1Stage.setScene(new Scene(client1Root));
        client1Stage.show();


        FXMLLoader client2Loader = new FXMLLoader(getClass().getResource("/view/client2.fxml"));
        Parent client2Root = client2Loader.load();
        Stage client2Stage = new Stage();
        client2Stage.setTitle("Client 02");
        client2Stage.setScene(new Scene(client2Root));
        client2Stage.show();



        FXMLLoader client3Loader = new FXMLLoader(getClass().getResource("/view/client3.fxml"));
        Parent client3Root = client3Loader.load();
        Stage client3Stage = new Stage();
        client3Stage.setTitle("Client 03");
        client3Stage.setScene(new Scene(client3Root));
        client3Stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

