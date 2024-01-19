package org.example.catproj;

import javafx.beans.property.SimpleStringProperty;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DashBoardController {

    @FXML
    private Button addEvent_btn;

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
    private TextField addEvent_desc;

    @FXML
    private AnchorPane addEvent_form;

    @FXML
    private ImageView addEvent_imageview;

    @FXML
    private TextField addEvent_name;

    @FXML
    private TextField addEvent_search;

    @FXML
    private TableView<eventData> addEvent_tableview;

    @FXML
    private TextField addEvent_time;

    @FXML
    private TableView<eventData> checkEvent_tableview;

    @FXML
    private TableColumn<eventData, String> checkEvent_col_name;

    @FXML
    private TableColumn<eventData, String> checkEvent_col_time;

    @FXML
    private TableColumn<eventData, String> checkEvent_col_date;

    @FXML
    private Button dashboard_btn;

    @FXML
    private AnchorPane dashboard_form;

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
    private TableView<eventData> joinEvent_tableview;

    @FXML
    private TextField joinEvent_time;

    @FXML
    private Button participants_btn;

    @FXML
    private TableView<participantData> participants_tableview;

    @FXML
    private TableColumn<participantData, String> displayParticipant_col_fname;

    @FXML
    private TableColumn<participantData, String> displayParticipant_col_lname;

    @FXML
    private TableColumn<participantData, String> displayParticipant_col_email;

    @FXML
    private TableColumn<participantData, String> displayParticipant_col_phone;

    @FXML
    private AnchorPane participants_form;

    @FXML
    private Button signout;

    @FXML
    private Label username;

    @FXML
    private TextField updateFirstName;
    @FXML
    private TextField updateLastName;
    @FXML
    private TextField updateEmailAddress;
    @FXML
    private TextField updatePhoneNumber;

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
    public void switchadminForm(ActionEvent event){
        if(event.getSource()==dashboard_btn){
            dashboard_form.setVisible(true);
            addEvent_form.setVisible(false);
//            joinEvent_form.setVisible(false);
            participants_form.setVisible(false);

            dashboard_btn.setStyle("-fx-background-color: #2355b0");
            addEvent_btn.setStyle("-fx-background-color: transparent");
//            joinEvent_btn.setStyle("-fx-background-color: transparent");
            participants_btn.setStyle("-fx-background-color: transparent");
        } else if(event.getSource()==addEvent_btn){
            dashboard_form.setVisible(false);
            addEvent_form.setVisible(true);
//            joinEvent_form.setVisible(false);
            participants_form.setVisible(false);

            dashboard_btn.setStyle("-fx-background-color: transparent");
            addEvent_btn.setStyle("-fx-background-color: #2355b0");
//            joinEvent_btn.setStyle("-fx-background-color: transparent");
            participants_btn.setStyle("-fx-background-color: transparent");
//        }else if(event.getSource()==joinEvent_btn){
//            dashboard_form.setVisible(false);
//            addEvent_form.setVisible(false);
//            joinEvent_form.setVisible(true);
//            participants_form.setVisible(false);
//
//            dashboard_btn.setStyle("-fx-background-color: transparent");
//            addEvent_btn.setStyle("-fx-background-color: transparent");
//            joinEvent_btn.setStyle("-fx-background-color: #ae2d3c");
            participants_btn.setStyle("-fx-background-color: transparent");
        }else if(event.getSource()==participants_btn){
            showCheckEventList();
            dashboard_form.setVisible(false);
            addEvent_form.setVisible(false);
//            joinEvent_form.setVisible(false);
            participants_form.setVisible(true);

            dashboard_btn.setStyle("-fx-background-color: transparent");
            addEvent_btn.setStyle("-fx-background-color: transparent");
//            joinEvent_btn.setStyle("-fx-background-color: transparent");
            participants_btn.setStyle("-fx-background-color: #2355b0");
        }
    }
    public void switchUserForm(ActionEvent event){
        if(event.getSource()==dashboard_btn){
            dashboard_form.setVisible(true);
            addEvent_form.setVisible(false);
            joinEvent_form.setVisible(false);
//            participants_form.setVisible(false);

            dashboard_btn.setStyle("-fx-background-color: #2355b0");
            addEvent_btn.setStyle("-fx-background-color: transparent");
            joinEvent_btn.setStyle("-fx-background-color: transparent");
//            participants_btn.setStyle("-fx-background-color: transparent");
        } else if(event.getSource()==addEvent_btn){
            dashboard_form.setVisible(false);
            addEvent_form.setVisible(true);
            joinEvent_form.setVisible(false);
//            participants_form.setVisible(false);

            dashboard_btn.setStyle("-fx-background-color: transparent");
            addEvent_btn.setStyle("-fx-background-color: #2355b0");
            joinEvent_btn.setStyle("-fx-background-color: transparent");
//            participants_btn.setStyle("-fx-background-color: transparent");
        }else if(event.getSource()==joinEvent_btn){
            dashboard_form.setVisible(false);
            addEvent_form.setVisible(false);
            joinEvent_form.setVisible(true);
//            participants_form.setVisible(false);

            dashboard_btn.setStyle("-fx-background-color: transparent");
            addEvent_btn.setStyle("-fx-background-color: transparent");
            joinEvent_btn.setStyle("-fx-background-color: #2355b0");
//            participants_btn.setStyle("-fx-background-color: transparent");
        }else if(event.getSource()==participants_btn){
            dashboard_form.setVisible(false);
            addEvent_form.setVisible(false);
            joinEvent_form.setVisible(false);
//            participants_form.setVisible(true);

            dashboard_btn.setStyle("-fx-background-color: transparent");
            addEvent_btn.setStyle("-fx-background-color: transparent");
            joinEvent_btn.setStyle("-fx-background-color: transparent");
//            participants_btn.setStyle("-fx-background-color: #ae2d3c");
        }
    }

    private ObservableList<eventData> listJoinEvent;
    public void showJoinEventList(){
        if (joinEvent_tableview != null) {
            listJoinEvent = addEventList();

            joinEvent_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
            joinEvent_col_name.setCellFactory(getWrapTextCellFactory());
            joinEvent_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
            joinEvent_col_time.setCellValueFactory(new PropertyValueFactory<>("time"));
            joinEvent_col_desc.setCellValueFactory(new PropertyValueFactory<>("desc"));
            joinEvent_col_desc.setCellFactory(getWrapTextCellFactory());

            joinEvent_tableview.setItems(listJoinEvent);
        }
    }

    public void selectAvailableEvent() {

        eventData eventD = joinEvent_tableview.getSelectionModel().getSelectedItem();
        int num = joinEvent_tableview.getSelectionModel().getSelectedIndex();

        if ((num-1) < -1){
            return;
        }

        joinEvent_name.setText(eventD.getName());
        joinEvent_date.setText(eventD.getDate());
        joinEvent_time.setText(eventD.getTime());

        getData.path = eventD.getImag();
        getData.title = eventD.getName();
    }
    public void selectEvent() {

        Alert alert;
        if(joinEvent_name.getText().isEmpty() || joinEvent_time.getText().isEmpty() || joinEvent_date.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select the event first");
            alert.showAndWait();
        }else {
            String imagePath = "file:" + getData.path;

            image = new Image(imagePath, 120, 160, false, true);
            joinEvent_imageview.setImage(image);
            joinEvent_label.setText(getData.title);

        }
    }
    @FXML
    private void registerParticipant(ActionEvent event) {
        eventData selectedEvent = (eventData) joinEvent_tableview.getSelectionModel().getSelectedItem();

        if (selectedEvent != null) {
            String firstName = joinEvent_firstname.getText();
            String lastName = joinEvent_lastname.getText();
            String email = joinEvent_email.getText();
            String phone = joinEvent_phone.getText();

            // Validate user input
            if (validateParticipantInput(firstName, lastName, email, phone)) {
                storeParticipantInDatabase(selectedEvent, firstName, lastName, email, phone);
                clearRegistrationForm();
            } else {
                showAlert("Error", "Invalid input", "Please fill in all fields.");
            }
        } else {
            showAlert("Error", "Event not selected", "Please select an event before registering.");
        }
    }

    private boolean validateParticipantInput(String firstName, String lastName, String email, String phone) {
        return !firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !phone.isEmpty();
    }

    private void storeParticipantInDatabase(eventData selectedEvent, String firstName, String lastName, String email, String phone) {
        Connection connect = null;

        try {
            connect = database.connectDB();

            String sql = "INSERT INTO PARTICIPANTS (EVENT_ID, FIRST_NAME, LAST_NAME, EMAIL, PHONE) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = connect.prepareStatement(sql)) {
                pstmt.setInt(1, selectedEvent.getid());
                pstmt.setString(2, firstName);
                pstmt.setString(3, lastName);
                pstmt.setString(4, email);
                pstmt.setString(5, phone);

                pstmt.executeUpdate();

                showAlert("Success", "Registration Successful", "Participant registered successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the database connection
            try {
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearRegistrationForm() {
        joinEvent_firstname.clear();
        joinEvent_lastname.clear();
        joinEvent_email.clear();
        joinEvent_phone.clear();
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private ObservableList<eventData> listCheckEvent;
    public void showCheckEventList() {

        // Fetch the latest data from the database
        listCheckEvent = fetchCheckEventListFromDatabase();

        checkEvent_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        checkEvent_col_name.setCellFactory(getWrapTextCellFactory());
        checkEvent_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        checkEvent_col_time.setCellValueFactory(new PropertyValueFactory<>("time"));

        // Set the items to the checkEvent_tableview
        checkEvent_tableview.setItems(listCheckEvent);
    }

    private ObservableList<eventData> fetchCheckEventListFromDatabase() {
        ObservableList<eventData> checkEventList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM EVENT_DET";
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connect = database.connectDB();
            preparedStatement = connect.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            eventData eventD;

            while (resultSet.next()) {
                int eventID = resultSet.getInt("EVENT_ID");
                String eventName = resultSet.getString("EVENT_NAME");
                String eventDate = resultSet.getString("EVENT_DATE");
                String eventTime = resultSet.getString("EVENT_TIME");

                eventD = new eventData(eventID, eventName, eventDate, eventTime, "", "");

                checkEventList.add(eventD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connect != null) connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return checkEventList;
    }
    @FXML
    private void checkParticipants(ActionEvent event) {
        eventData selectedEvent = checkEvent_tableview.getSelectionModel().getSelectedItem();

        if (selectedEvent != null) {
            // Fetch participants from the database for the selected event

            tempData.setSelectedEvent(selectedEvent);

            ObservableList<participantData> participants = fetchParticipantsFromDatabase(selectedEvent);

            // Display participants in the participants_tableview
            setDisplayParticipant(participants);
            listCheckEvent.clear();
            showCheckEventList();

        } else {
            showAlert("Error", "Event not selected", "Please select an event before checking participants.");
        }
    }

    public ObservableList<participantData> fetchParticipantsFromDatabase(eventData selectedEvent) {
        ObservableList<participantData> participants = FXCollections.observableArrayList();

        String sql = "SELECT * FROM PARTICIPANTS WHERE EVENT_ID = ?";
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connect = database.connectDB();
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setInt(1, selectedEvent.getid());

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("PARTICIPANT_ID");
                String firstName = resultSet.getString("FIRST_NAME");
                String lastName = resultSet.getString("LAST_NAME");
                String email = resultSet.getString("EMAIL");
                String phone = resultSet.getString("PHONE");

                participantData participant = new participantData(id, firstName, lastName, email, phone);

                participants.add(participant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connect != null) connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return participants;
    }

    private void setDisplayParticipant(ObservableList<participantData> participants){

        TableColumn<participantData, String> displayParticipant_col_no = new TableColumn<>("No.");
        displayParticipant_col_no.setCellValueFactory(data -> new SimpleStringProperty(Integer.toString(participants.indexOf(data.getValue()) + 1)));
        displayParticipant_col_fname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        displayParticipant_col_lname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        displayParticipant_col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        displayParticipant_col_email.setCellFactory(getParticipantWrapTextCellFactory());
        displayParticipant_col_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        // Set the columns to the participants_tableview
        participants_tableview.getColumns().setAll(displayParticipant_col_no,displayParticipant_col_fname, displayParticipant_col_lname, displayParticipant_col_email, displayParticipant_col_phone);

        // Set the items to the participants_tableview
        participants_tableview.setItems(participants);
    }

    public void selectParticipant(){
        participantData selectedParticipant = participants_tableview.getSelectionModel().getSelectedItem();
        int num = participants_tableview.getSelectionModel().getSelectedIndex();

        if((num-1)< -1){
            return;
        }

        updateFirstName.setText(selectedParticipant.getFirstName());
        updateLastName.setText(selectedParticipant.getLastName());
        updateEmailAddress.setText(selectedParticipant.getEmail());
        updatePhoneNumber.setText(selectedParticipant.getPhone());
    }
    @FXML
    private void deleteParticipant(ActionEvent event) {
        participantData selectedParticipant = participants_tableview.getSelectionModel().getSelectedItem();
        eventData selectedEvent = tempData.getSelectedEvent();

        if (selectedParticipant != null && selectedEvent != null) {
            // Show a confirmation dialog
            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationDialog.setTitle("Delete Participant");
            confirmationDialog.setHeaderText(null);
            confirmationDialog.setContentText("Are you sure you want to delete this participant?");

            Optional<ButtonType> result = confirmationDialog.showAndWait();

            // Process the result
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // User clicked OK, proceed with deletion
                deleteParticipantFromDatabase(selectedParticipant);

                // After deletion, update the participants_tableview
                ObservableList<participantData> updatedParticipants = fetchParticipantsFromDatabase(selectedEvent);
                setDisplayParticipant(updatedParticipants);
                listCheckEvent.clear();
                showCheckEventList();
            }
        } else {
            showAlert("Error", "Participant or Event not selected", "Please select a participant and an event before deleting.");
        }
    }

    private void deleteParticipantFromDatabase(participantData selectedParticipant) {
        Connection connection = null;

        try {
            connection = database.connectDB();

            String sql = "DELETE FROM PARTICIPANTS WHERE PARTICIPANT_ID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, selectedParticipant.getId());

                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    showAlert("Success", "Deletion Successful", "Participant deleted successfully!");

                    showCheckEventList();
                } else {
                    showAlert("Error", "Deletion Failed", "No participant found with the specified ID.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database Error", "Failed to delete participant from the database.");
        } finally {
            // Close the database connection
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshParticipantsTable() {
        // Fetch the selected event
        eventData selectedEvent = checkEvent_tableview.getSelectionModel().getSelectedItem();

        if (selectedEvent != null) {
            // Fetch participants from the database for the selected event
            ObservableList<participantData> participants = fetchParticipantsFromDatabase(selectedEvent);

            // Display participants in the participants_tableview
            setDisplayParticipant(participants);
//            listCheckEvent.clear();
            showCheckEventList();
        }
    }

    private Callback<TableColumn<participantData, String>, TableCell<participantData, String>> getParticipantWrapTextCellFactory() {
        return col -> {
            TableCell<participantData, String> cell = new TableCell<>() {
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

    public void initialize(){
        displayUsername();
        showAddEventList();
        showJoinEventList();
        searchAddEvents();
    }
}
