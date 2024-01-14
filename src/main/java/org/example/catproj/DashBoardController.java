package org.example.catproj;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class DashBoardController {

    @FXML
    private Button addEvent_btn;

    @FXML
    private Button addEvent_clear;

    @FXML
    private TableColumn<eventData, String> addEvent_col_date;

    @FXML
    private TableColumn<eventData, String> addEvent_col_desc;

    @FXML
    private TableColumn<eventData, String> addEvent_col_name;

    @FXML
    private TableColumn<eventData, String> addEvent_col_time;

    @FXML
    private TextField addEvent_date;

    @FXML
    private Button addEvent_delete;

    @FXML
    private TextField addEvent_desc;

    @FXML
    private AnchorPane addEvent_form;

    @FXML
    private ImageView addEvent_imageview;

    @FXML
    private Button addEvent_import;

    @FXML
    private Button addEvent_insert;

    @FXML
    private TextField addEvent_name;

    @FXML
    private TextField addEvent_search;

    @FXML
    private TableView<eventData> addEvent_tableview;

    @FXML
    private TextField addEvent_time;

    @FXML
    private Button addEvent_update;

    @FXML
    private Button dashboard_btn;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private Label dashboard_totalevents;

    @FXML
    private Label dashboard_totalusers;

    @FXML
    private Button joinEvent_btn;

    @FXML
    private TableColumn<eventData, String> joinEvent_col_date;

    @FXML
    private TableColumn<eventData, String> joinEvent_col_desc;

    @FXML
    private TableColumn<eventData, String> joinEvent_col_name;

    @FXML
    private TableColumn<eventData, String> joinEvent_col_time;

    @FXML
    private TextField joinEvent_date;

    @FXML
    private TextField joinEvent_email;

    @FXML
    private TextField joinEvent_firstname;

    @FXML
    private AnchorPane joinEvent_form;

    @FXML
    private ImageView joinEvent_imageview;

    @FXML
    private Label joinEvent_label;

    @FXML
    private TextField joinEvent_lastname;

    @FXML
    private TextField joinEvent_name;

    @FXML
    private TextField joinEvent_phone;

    @FXML
    private Button joinEvent_register;

    @FXML
    private Button joinEvent_select;

    @FXML
    private TableView<?> joinEvent_tableview;

    @FXML
    private Label joinEvent_text;

    @FXML
    private TextField joinEvent_time;

    @FXML
    private Button participants_btn;

    @FXML
    private Button participants_check;

    @FXML
    private TableColumn<?, ?> participants_col_;

    @FXML
    private TableColumn<?, ?> participants_col_email;

    @FXML
    private TableColumn<?, ?> participants_col_lastname;

    @FXML
    private TableColumn<?, ?> participants_col_phone;

    @FXML
    private TextField participants_date;

    @FXML
    private AnchorPane participants_form;

    @FXML
    private TextField participants_name;

    @FXML
    private TextField participants_search;

    @FXML
    private TableView<?> participants_tableview;

    @FXML
    private TextField participants_time;

    @FXML
    private Button signout;

    @FXML
    private Button updateEvent_btn;

    @FXML
    private TableColumn<?, ?> updateEvent_col_date;

    @FXML
    private TableColumn<?, ?> updateEvent_col_desc;

    @FXML
    private TableColumn<?, ?> updateEvent_col_name;

    @FXML
    private TableColumn<?, ?> updateEvent_col_time;

    @FXML
    private ComboBox<?> updateEvent_current;

    @FXML
    private Button updateEvent_delete;

    @FXML
    private AnchorPane updateEvent_form;

    @FXML
    private ImageView updateEvent_imageview;

    @FXML
    private Label updateEvent_label;

    @FXML
    private TextField updateEvent_search;

    @FXML
    private TableView<?> updateEvent_tableview;

    @FXML
    private Label updateEvent_text;

    @FXML
    private Button updateEvent_update;

    @FXML
    private Label username;
    public void searchAddEvents() {
        try {
            FilteredList<eventData> filteredData = new FilteredList<>(listAddEvent, b -> true);
            addEvent_search.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(event -> {
                    if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
                        return true;
                    }

                    String searchKeyword = newValue.toLowerCase();

                    return event.getName().toLowerCase().contains(searchKeyword);
                });
            });

            SortedList<eventData> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(addEvent_tableview.comparatorProperty());
            addEvent_tableview.setItems(sortedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayUsername(){
        username.setText(getData.username);
    }

    private Image image;
    private double x = 0;
    private double y = 0;

    private Connection connect;
    private ResultSet result;
    public void importImage(){

        FileChooser open = new FileChooser();

        open.setTitle("Open Image File");
        open.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image File","*png","*jpg"));

        Stage stage = (Stage) addEvent_form.getScene().getWindow();
        File file = open.showOpenDialog(stage);

        if (file != null) {

            image = new Image(file.toURI().toString(), 120, 160, false, true);
            addEvent_imageview.setImage(image);

            String destinationFolder = "src/main/resources/image";  // Adjust the path as needed
            String filename = file.getName();
            File destination = new File(destinationFolder, filename);

            try {
                Path sourcePath = file.toPath();
                Path destinationPath = destination.toPath();
                Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

                getData.path = "./src/main/resources/image/" + filename;
            } catch (IOException e) {e.printStackTrace();}

        }
    }

    public void clearAddEventList(){
        addEvent_name.setText("");
        addEvent_date.setText("");
        addEvent_time.setText("");
        addEvent_desc.setText("");
        addEvent_imageview.setImage(null);
        getData.id = 0;
    }

    public void updateAddEvents() {

        String uri = getData.path;
        uri = uri.replace("\\","\\\\");

        String sql = "UPDATE EVENT_DET SET EVENT_NAME = '" + addEvent_name.getText()
                + "', EVENT_DATE = '" + addEvent_date.getText()
                + "', EVENT_TIME = '" + addEvent_time.getText()
                + "', EVENT_DESC = '" + addEvent_desc.getText()
                + "', EVENT_IMAGE_PATH = '" + uri
                + "' WHERE EVENT_ID = '" + getData.id + "'";

        connect = database.connectDB();

        try{
            Statement statement = connect.createStatement();

            Alert alert;

            if(addEvent_name.getText().isEmpty()||addEvent_date.getText().isEmpty()||addEvent_time.getText().isEmpty()||addEvent_desc.getText().isEmpty()||addEvent_imageview.getImage() == null){

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill in the blanks.");
                alert.showAndWait();
            }else{
                statement.executeUpdate(sql);
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Update was made successfully");
                alert.showAndWait();

                clearAddEventList();
                showAddEventList();

            }
        }catch(Exception e){
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
        showAddEventList();
        searchAddEvents();
    }

    public void deleteAddEvents() {

        String sql = "DELETE FROM EVENT_DET WHERE EVENT_ID = '" + getData.id + "'";

        connect = database.connectDB();

        try {
            Alert alert;

            if (getData.id == 0)
            {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select an Event.");
                alert.showAndWait();
            } else {
                try (PreparedStatement pstmt = connect.prepareStatement(sql)) {
                    pstmt.executeUpdate();
                }
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Event deleted successfully");
                alert.showAndWait();

                clearAddEventList();
                showAddEventList();
            }
        } catch (SQLException e) {
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
        showAddEventList();
        searchAddEvents();
    }

    public void insertAddEvents(){

        String sql1 = "SELECT * FROM EVENT_DET WHERE EVENT_NAME = '" + addEvent_name.getText() +"'";

        connect = database.connectDB();

        Alert alert;

        try{
            PreparedStatement pstmt1 = connect.prepareStatement(sql1);
            result = pstmt1.executeQuery();

            if(result.next()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText(addEvent_name.getText() + " was already exist!");
                alert.showAndWait();

            } else{
                if(addEvent_name.getText().isEmpty()||addEvent_date.getText().isEmpty()||addEvent_time.getText().isEmpty()||addEvent_desc.getText().isEmpty()||addEvent_imageview.getImage() == null){

                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Please fill in the blanks");
                    alert.showAndWait();

                } else {

                    String sql = "INSERT INTO EVENT_DET(EVENT_NAME, EVENT_DATE, EVENT_TIME, EVENT_DESC, EVENT_IMAGE_PATH) VALUES (?,?,?,?,?)";

                    String uri = getData.path;
                    uri = uri.replace("\\","\\\\");

                    PreparedStatement pstmt = connect.prepareStatement(sql);
                    pstmt.setString(1, addEvent_name.getText());
                    pstmt.setString(2, addEvent_date.getText());
                    pstmt.setString(3, addEvent_time.getText());
                    pstmt.setString(4, addEvent_desc.getText());
                    pstmt.setString(5, uri);

                    pstmt.execute();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully add new event!");
                    alert.showAndWait();

                    clearAddEventList();
                    showAddEventList();
                }
            }

        } catch(Exception e){
            e.printStackTrace();
        } finally {
            // Close the database connection in a finally block to ensure it gets closed even if an exception occurs
            try {
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the exception according to your application's needs
            }
        }
        showAddEventList();
        searchAddEvents();
    }
    public ObservableList<eventData> addEventList(){

        ObservableList<eventData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM EVENT_DET";

        connect = database.connectDB();

        try{

            PreparedStatement pstmt = connect.prepareStatement(sql);
            result = pstmt.executeQuery();

            eventData eveD;

            while(result.next()){
                int eventID = result.getInt("EVENT_ID");
                String eventName = result.getString("EVENT_NAME");
                String eventDate = result.getString("EVENT_DATE");
                String eventTime = result.getString("EVENT_TIME");
                String eventDesc = result.getString("EVENT_DESC");
                String eventImagePath = result.getString("EVENT_IMAGE_PATH");

                eveD = new eventData(eventID,eventName, eventDate, eventTime, eventDesc, eventImagePath);

                listData.add(eveD);

            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            // Close the database connection in a finally block to ensure it gets closed even if an exception occurs
            try {
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the exception according to your application's needs
            }
        }
        return listData;
    }

    private ObservableList<eventData> listAddEvent;
    public void showAddEventList(){
        listAddEvent = addEventList();

        addEvent_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        addEvent_col_name.setCellFactory(getWrapTextCellFactory());
        addEvent_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        addEvent_col_time.setCellValueFactory(new PropertyValueFactory<>("time"));
        addEvent_col_desc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        addEvent_col_desc.setCellFactory(getWrapTextCellFactory());

        addEvent_tableview.setItems(listAddEvent);
    }


    private Callback<TableColumn<eventData, String>, TableCell<eventData, String>> getWrapTextCellFactory() {
        return col -> {
            TableCell<eventData, String> cell = new TableCell<>() {
                private final Text text = new Text();

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setGraphic(null);
                    } else {
                        text.setText(item);
                        text.wrappingWidthProperty().bind(col.widthProperty());
                        setGraphic(text);
                    }
                }
            };

            cell.setStyle("-fx-alignment: CENTER-LEFT;");

            return cell;
        };
    }

    public void selectAddEventList(){
        eventData eveD = addEvent_tableview.getSelectionModel().getSelectedItem();
        int num = addEvent_tableview.getSelectionModel().getSelectedIndex();

        if((num-1)< -1){
            return;
        }

        getData.id = eveD.getid();

        getData.path = eveD.getImag();
        addEvent_name.setText(eveD.getName());
        addEvent_date.setText(eveD.getDate());
        addEvent_time.setText(eveD.getTime());
        addEvent_desc.setText(eveD.getDesc());

        String projectRoot = System.getProperty("user.dir");
        String imagePath = eveD.getImag();

        File imageFile = new File(projectRoot, imagePath);
        URI imageUri = imageFile.toURI();

        URL imageUrl = null;
        try {
            imageUrl = imageUri.toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }


        if(imageFile.exists()) {
        image = new Image(imageUrl.toString(), 120, 160, false, true);
        addEvent_imageview.setImage(image);
    } else {
        System.out.println("Image file not found: " + imageFile.getAbsolutePath());
    }
    }
    public void logOut(){
        signout.getScene().getWindow().hide();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("AccessPage.fxml"));

            Stage stage = new Stage();
            Scene scene = new Scene(root);

            root.setOnMousePressed((MouseEvent event) ->{
                x = event.getSceneX();
                y = event.getSceneY();
            });

            root.setOnMouseDragged((MouseEvent event) ->{
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
            });

            stage.setScene(scene);
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void switchForm(ActionEvent event){
        if(event.getSource()==dashboard_btn){
            dashboard_form.setVisible(true);
            addEvent_form.setVisible(false);
//            joinEvent_form.setVisible(false);
//            participants_form.setVisible(false);

            dashboard_btn.setStyle("-fx-background-color: #ae2d3c");
            addEvent_btn.setStyle("-fx-background-color: transparent");
//            joinEvent_btn.setStyle("-fx-background-color: transparent");
//            participants_btn.setStyle("-fx-background-color: transparent");
        } else if(event.getSource()==addEvent_btn){
            dashboard_form.setVisible(false);
            addEvent_form.setVisible(true);
//            joinEvent_form.setVisible(false);
//            participants_form.setVisible(false);

            dashboard_btn.setStyle("-fx-background-color: transparent");
            addEvent_btn.setStyle("-fx-background-color: #ae2d3c");
//            joinEvent_btn.setStyle("-fx-background-color: transparent");
//            participants_btn.setStyle("-fx-background-color: transparent");
        }else if(event.getSource()==joinEvent_btn){
            dashboard_form.setVisible(false);
            addEvent_form.setVisible(false);
//            joinEvent_form.setVisible(true);
//            participants_form.setVisible(false);

            dashboard_btn.setStyle("-fx-background-color: transparent");
            addEvent_btn.setStyle("-fx-background-color: transparent");
//            joinEvent_btn.setStyle("-fx-background-color: #ae2d3c");
//            participants_btn.setStyle("-fx-background-color: transparent");
        }else if(event.getSource()==participants_btn){
            dashboard_form.setVisible(false);
            addEvent_form.setVisible(false);
//            joinEvent_form.setVisible(false);
//            participants_form.setVisible(true);

            dashboard_btn.setStyle("-fx-background-color: transparent");
            addEvent_btn.setStyle("-fx-background-color: transparent");
//            joinEvent_btn.setStyle("-fx-background-color: transparent");
//            participants_btn.setStyle("-fx-background-color: #ae2d3c");
        }
    }

    public void initialize(){
        displayUsername();
        showAddEventList();
        searchAddEvents();
    }
}
