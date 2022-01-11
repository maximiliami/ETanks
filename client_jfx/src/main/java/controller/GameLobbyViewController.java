package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.ETankApplication;
import model.data.User;
import model.game.logic.GameLobby;
import model.game.logic.Player;
import model.service.SocketClient;


import java.io.IOException;

public class GameLobbyViewController {


    SocketClient sc = new SocketClient("localhost",3333,this);
    private ETankApplication eTankApplication;
    public Player signedPlayer;



    ObservableList<GameLobby> lobbyList = FXCollections.observableArrayList();


    @FXML
    private TableView<GameLobby> lobbyTable;
    @FXML
    private TableColumn<GameLobby, Long> columnLobbyNumber;
    @FXML
    private TableColumn<GameLobby, Integer> columnLobbySeats;

    @FXML
    private ListView playerList;

    @FXML
    private VBox vbxJoin;

    @FXML
    private VBox vbxLobby;

    @FXML
    private VBox vbxInit;

    @FXML
    private HBox hbxHostPanel;

    @FXML
    private HBox hbxJoinerPanel;

    public GameLobbyViewController() throws IOException {

    }

    @FXML
    private void initialize() {
        switchToInit();
        //lobbyTable.setItems(lobbyList);
        columnLobbyNumber.setCellValueFactory(cellData -> cellData.getValue().gameLobbyIDProperty().asObject());
        columnLobbySeats.setCellValueFactory(cellData -> cellData.getValue().seatCounterProperty().asObject());

    }

    @FXML
    public void switchToInit() {
        resetViews();
        vbxInit.setVisible(true);
    }
    @FXML
    public void hostGame() throws IOException {

        resetViews();
        vbxLobby.setVisible(true);
        hbxHostPanel.setVisible(true);
        GameLobby lobby = new GameLobby();
        lobby.buildLobbyID();
   //     User sU = eTankApplication.getSignedUser();
   //     this.signedPlayer = new Player(sU.getId(), sU.getUserName(), sU.getPublicName(), sU.getUserImage(), sU.getPassword(),
   //             sU.getUserSettings());
   //     lobby.addPlayer(this.signedPlayer);
   //     this.lobbyList.add(lobby);
        sc.connect();
    }

    @FXML
    private void closeLobby() {
        switchToInit();
        for (GameLobby lobby: lobbyList) {
            if(lobby.getPlayers().get(0).getId() == eTankApplication.getSignedUser().getId()) {
                lobbyList.remove(lobby);
                break;
            }
        }
    }
    @FXML
    public void joinGame() {
        resetViews();
        vbxJoin.setVisible(true);
    }

    @FXML
    public void switchBack() throws IOException {
        eTankApplication.showMenuView();
    }

    @FXML
    public void switchToGameView() throws IOException{
        eTankApplication.showGameView();

    }

    @FXML
    public void setRdyTrue() {
    }

    @FXML
    public void switchBackToInit() {
        switchToInit();
        for (GameLobby lobby: this.lobbyList) {
            for (Player player: lobby.getPlayers()) {
                if(player.getId() == eTankApplication.getSignedUser().getId()) {
                    lobby.removePlayer(player);
                }
            }
        }
    }

    @FXML
    public GameLobby joinSelectedGame() throws IOException {
        GameLobby selectedGameLobby = lobbyTable.getSelectionModel().getSelectedItem();
        if(selectedGameLobby.getSeatCounter() < 4) {
            System.out.println("joining game");
            User sU = eTankApplication.getSignedUser();
            Player player = new Player(sU.getId(), sU.getUserName(), sU.getPublicName(), sU.getUserImage(), sU.getPassword(),
                    sU.getUserSettings());
            selectedGameLobby.addPlayer(player);
            resetViews();
            vbxLobby.setVisible(true);
            hbxJoinerPanel.setVisible(true);

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Schlacht ist schon voll");
            alert.setContentText("Wähle eine andere oder erstelle selbst eine");
            alert.showAndWait();
        }
        return selectedGameLobby;
    }

    @FXML
    private void setStatusReady(ActionEvent actionEvent) {
    }

    public void resetViews() {
        vbxLobby.setVisible(false);
        vbxJoin.setVisible(false);
        vbxInit.setVisible(false);
        hbxHostPanel.setVisible(false);
        hbxJoinerPanel.setVisible(false);
    }
    public void enterChatHandleS(KeyEvent keyEvent) {
    }

    public void sendMessageS(ActionEvent actionEvent) {
    }

    public void setETankApplication(ETankApplication eTankApplication) {
        this.eTankApplication = eTankApplication;
    }


}
