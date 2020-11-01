package ru.geekbrains.java2.network.client.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.*;

import ru.geekbrains.java2.network.client.NetworkChatClient;
import ru.geekbrains.java2.network.client.models.Network;

import java.io.*;
import java.text.DateFormat;

import java.util.Date;


public class ViewController {

    @FXML
    public ListView<String> usersList;
    @FXML
    private Button sendButton;
    @FXML
    private TextArea chatHistory;
    @FXML
    private TextField textField;
    private Network network;

    private String selectedRecipient;

    @FXML
    public void initialize() {


        sendButton.setOnAction(event -> {
            try {
                sendMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        textField.setOnAction(event -> {
            try {
                sendMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });



    }

    private void sendMessage() throws IOException {
        String message = textField.getText();
        appendMessage("Я: " + message);
        textField.clear();

        try {
            if (selectedRecipient != null) {
                network.sendPrivateMessage(message, selectedRecipient);
            }
            else {
                network.sendMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
            String errorMessage = "Failed to send message";
            NetworkChatClient.showNetworkError(e.getMessage(), errorMessage);
        }
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void appendMessage(String message) throws IOException {
        String timestamp = DateFormat.getInstance().format(new Date());
        chatHistory.appendText(timestamp+":"+message + "\n");
        appendHistory(message);

    }
    public void appendHistory(String message) throws IOException {
        String timestamp = DateFormat.getInstance().format(new Date());
        try (FileWriter out = new FileWriter("NetworkClient/history.txt", true)) {
            out.write(timestamp + ": " + message + "\n");
            out.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

   public  void getHistory(){

       try (BufferedReader bufferedReader = new BufferedReader(new FileReader("NetworkClient/history.txt"))) {

           
           while (bufferedReader.read() != -1) {
               chatHistory.appendText(bufferedReader.readLine()+"\n");
           }
       } catch (IOException e) {
           e.printStackTrace();
       }
   }



    public void showError(String title, String message) {
        NetworkChatClient.showNetworkError(message, title);
    }
}