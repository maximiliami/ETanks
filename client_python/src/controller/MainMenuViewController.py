from PySide6.QtWidgets import QWidget

from resources.view.MainMenuView import Ui_mainMenuView


class MainMenuViewController(QWidget):
    def __init__(self, stackedWidget):
        super().__init__()
        self.mainMenuView = Ui_mainMenuView()
        self.mainMenuView.setupUi(self)

        self.stackedWidget = stackedWidget

        self.mainMenuView.newGameButton.clicked.connect(self.openGameLobbyView)
        self.mainMenuView.showProfilButton.clicked.connect(self.openProfilView)
        self.mainMenuView.showSettingsButton.clicked.connect(self.openSettingsView)
        self.mainMenuView.showStatisticButton.clicked.connect(self.openStatisticView)

    def openGameLobbyView(self):
        pass

    # TODO: implement connection to GameLobbyView

    def openProfilView(self):
        self.stackedWidget.setCurrentIndex(1)

    def openSettingsView(self):
        pass

    # TODO: implement connection to SettingsView
    def openStatisticView(self):
        pass
    # TODO: implement connection to StatisticView