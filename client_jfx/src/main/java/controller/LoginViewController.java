package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import controller.ViewController;

import java.io.IOException;

public class LoginViewController {

    @FXML
    TextField UsernameField;
    @FXML
    PasswordField pwField;
    @FXML
    Label forgotPwLabel;
    @FXML
    Label newUserLabel;
    @FXML
    Button loginBtn;

    @FXML
    public void changeView(){
        ViewController viewController = new ViewController();
        System.out.println("HIER HIER HIER");
    }



}
