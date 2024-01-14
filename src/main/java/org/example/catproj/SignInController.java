package org.example.catproj;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInController{

    @FXML
    private Button signin_button;

    @FXML
    private AnchorPane signin_form;

    @FXML
    private PasswordField signin_password;

    @FXML
    private Hyperlink signin_signup;

    @FXML
    private TextField signin_username;

    @FXML
    private Button signup_button;

    @FXML
    private TextField signup_email;

    @FXML
    private AnchorPane signup_form;

    @FXML
    private Hyperlink signup_login;

    @FXML
    private PasswordField signup_password;

    @FXML
    private TextField signup_username;

    private Connection connect;
    private ResultSet result;

    public boolean validEmail(){

        Pattern pattern = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+");

        Matcher match = pattern.matcher(signup_email.getText());

        Alert alert;

        if(match.find() && match.group().equals(signup_email.getText())){
            return true;
        }else{
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Invalid email");
            alert.showAndWait();

            return false;
        }
    }
    public void signup(){

        String sql = "INSERT INTO USER_PROF (USER_EMAIL, USER_NAME, USER_PASS) VALUES(?,?,?)";

        String sql1= "SELECT USER_NAME FROM USER_PROF WHERE USER_NAME = '" +signup_username.getText() + "'";

        connect = database.connectDB();

        try{
            PreparedStatement pstmt = connect.prepareStatement(sql);
            pstmt.setString(1, signup_email.getText());
            pstmt.setString(2, signup_username.getText());
            pstmt.setString(3, signup_password.getText());

            Alert alert;

            if(signup_email.getText().isEmpty()||signup_username.getText().isEmpty()||signup_username.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();

            } else if(signup_password.getText().length() > 8) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid Password");
                    alert.showAndWait();
            } else {

                if (validEmail()) {
                    PreparedStatement pstmt1 = connect.prepareStatement(sql1);
                    result = pstmt1.executeQuery();

                    if(result.next()){

                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText(signup_username.getText() + " was taken.");
                        alert.showAndWait();

                    } else {

                        pstmt.execute();

                        alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully created an account!");
                        alert.showAndWait();

                        signup_email.setText("");
                        signup_username.setText("");
                        signup_password.setText("");
                    }

                }
            }
        } catch(Exception e){e.printStackTrace();}
    }
    public void signin() {

        String sql = "SELECT * FROM USER_PROF WHERE USER_NAME = ? AND USER_PASS = ?";

        connect = database.connectDB();

        try {
            if (connect != null) { // Check if the connection is successful
                PreparedStatement pstmt = connect.prepareStatement(sql);
                pstmt.setString(1, signin_username.getText());
                pstmt.setString(2, signin_password.getText());

                result = pstmt.executeQuery();

                Alert alert;

                if (signin_username.getText().isEmpty() || signin_password.getText().isEmpty()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Please fill all blank fields");
                    alert.showAndWait();

                } else {
                    if (result.next()) {

                        getData.username = signin_username.getText();

                        alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Information Message.");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully Login!");
                        alert.showAndWait();

                        signin_button.getScene().getWindow().hide();

                        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("DashBoard.fxml")));

                        Stage stage = new Stage();
                        Scene scene = new Scene(root);

                        stage.setScene(scene);
                        stage.show();

                    } else {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Message.");
                        alert.setHeaderText(null);
                        alert.setContentText("Wrong Username/Password");
                        alert.showAndWait();
                    }
                }
            } else {
                System.out.println("Database connection failed."); // Print a message if connection is not successful
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void switchform(ActionEvent event){
        if(event.getSource() == signin_signup){
            signin_form.setVisible(false);
            signup_form.setVisible(true);
        } else if (event.getSource() == signup_login) {
            signin_form.setVisible(true);
            signup_form.setVisible(false);
        }
    }
}
