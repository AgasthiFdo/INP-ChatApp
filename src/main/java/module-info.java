module lk.ijse.chat.exam.chatappexam {
    requires javafx.controls;
    requires javafx.fxml;


    opens lk.ijse.chat.exam.chatappexam to javafx.fxml;
    exports lk.ijse.chat.exam.chatappexam;
    exports lk.ijse.chat.exam.chatappexam.controller;
    opens lk.ijse.chat.exam.chatappexam.controller to javafx.fxml;
}