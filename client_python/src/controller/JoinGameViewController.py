import json

from PySide6 import QtGui, QtCore
from PySide6.QtWidgets import QWidget, QListWidgetItem
from model.data.Lobby import Lobby
from model.service.Message import Message
from resources.view.JoinGameView import Ui_joinGameView


class JoinGameViewController(QWidget):
    def __init__(self, newGameViewController):
        super().__init__()

        self.joinGameView = Ui_joinGameView()
        self.joinGameView.setupUi(self)
        self.newGameViewController = newGameViewController
        self.lobbySocket = self.newGameViewController.clientSocket
        self.signedUser = self.newGameViewController.mainMenuViewController.signedUser
        self.gameLobbyList = []
        self.enableThread = True
        self.joinGameView.toNewGameView.clicked.connect(self.showNewGameView)
        self.joinGameView.refreshGameListButton.clicked.connect(self.getLobbyList)
        self.joinGameView.joinSelectedButton.clicked.connect(self.joinSelectedGameLobby)
        self.joinGameView.gameList.doubleClicked.connect(self.joinSelectedGameLobby)

    def joinSelectedGameLobby(self):
        selectedLobbyId = self.joinGameView.gameList.currentItem().text()
        print(selectedLobbyId)

        for lobby in self.gameLobbyList:
            if lobby.id == selectedLobbyId and lobby.seats < 4:
                print(lobby.seats)
                self.newGameViewController.lobbyJoinView.lobbyId = selectedLobbyId
                self.newGameViewController.lobbyJoinView.registerJoinedUserToLobby()
                self.newGameViewController.mainMenuViewController.stackedWidget. \
                    setCurrentWidget(self.newGameViewController.lobbyJoinView)

    def fillLobbyList(self):
        self.joinGameView.gameList.clear()
        self.joinGameView.seatsList.clear()
        for lobby in self.gameLobbyList:
            self.joinGameView.gameList.addItem(self.buildLobbyListItem(lobby.id))
            self.joinGameView.seatsList.addItem(self.buildSeatItem(lobby.seats))

    def sendJoinMsg(self, msg):
        data_as_dict = vars(msg)
        msgJSON = json.dumps(data_as_dict)
        self.lobbySocket.sendMsg(msgJSON)
        print("Gesendet in JoinGameView: ", msgJSON)

    def receiveJoinMsg(self, msg):
        if msg is not None:
            if msg.messageType == "GET_LOBBIES":
                recvLobby = Lobby()
                recvLobby.id = msg.gameLobbyNumber
                recvLobby.seats = msg.payload
                for lobby in self.gameLobbyList:
                    if recvLobby.id == lobby.id:
                        self.gameLobbyList.remove(lobby)

                self.gameLobbyList.append(recvLobby)
                self.fillLobbyList()

    def showNewGameView(self):
        self.newGameViewController.mainMenuViewController.stackedWidget.setCurrentWidget(self.newGameViewController)

    def getLobbyList(self):
        getLobbyMsg = Message()
        getLobbyMsg.messageType = "GET_LOBBIES"
        self.sendJoinMsg(getLobbyMsg)

    @staticmethod
    def buildLobbyListItem(lobbyNumber):
        pixmap = QtGui.QPixmap()
        pixmap.load("../resources/images/germany_flag.png")
        pic = pixmap.scaled(40, 40, QtCore.Qt.IgnoreAspectRatio)
        return QListWidgetItem(pic, str(lobbyNumber))

    @staticmethod
    def buildSeatItem(takenSeats):
        value = str(takenSeats) + "/4 Plätze"
        return QListWidgetItem(value)
