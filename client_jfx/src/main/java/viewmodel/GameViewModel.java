package viewmodel;

import de.saxsys.mvvmfx.ViewModel;
import javafx.animation.*;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import main.ETankApplication;
import model.game.elements.*;
import model.game.logic.GameLobby;
import model.game.logic.GamePhysics;
import model.game.logic.GamePlay;
import view.GameView;

import java.util.ArrayList;


public class GameViewModel implements ViewModel {
    ETankApplication eTankApplication;
    GameLobby gameLobby;
    GamePlay gamePlay;
    GameView gameView;

    int whichTank = 0;
    boolean canShoot = true;
    double shootDelay = GamePhysics.DELAY_SECOND;

    ObservableList<LevelElement> elementList = FXCollections.observableArrayList();
    ObservableList<ImageView> bulletList = FXCollections.observableArrayList();

    public void startTimer() {
        AnimationTimer gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                playerMovementDetection();
                bulletCollisionDetection();
            }
        };
        gameTimer.start();
    }

    public void bulletCollisionDetection() {
        shootDelay += 0.03;
        if (shootDelay >= 2) {
            shootDelay = 0;
            canShoot = true;
        }

        boolean isHit = false;
        LevelElement toRemove = null;

        for (LevelElement element : elementList) {

            if (element.getType().equals("bullet")) {

                for (int i = 1; i < elementList.size(); i++) {
                    if (elementList.get(i).getType().equals("tank")) {
                        if (element.getBoundsInParent().intersects(elementList.get(i).getBoundsInParent())) {

                            BulletMainWeapon tempBullet = ((BulletMainWeapon) element);
                            int playerId = tempBullet.getTankFired().getPlayerId();
                            //Hier bekommt der Player Punkte

                            toRemove = element;
                            element.setDisable(true);
                            //Hier verliert der andere Player Leben
                            Tank tank = (Tank) elementList.get(i);
                            tank.reduceLivePoints();
                            isHit = true;

                            System.out.println("Player: " + playerId + " Du hast Player: " + tank.getPlayerId() + " getroffen!");
                            break;
                        } else {
                            System.out.println(element.getX());
                            if(element.getX() > GamePhysics.GAME_WIDTH){
                                System.out.println("Raus");
                                toRemove = element;
                                isHit = true;
                            }
                        }
                    } else if (elementList.get(i).getType().equals("block")) {
                        if (element.getBoundsInParent().intersects(elementList.get(i).getBoundsInParent())) {
                            System.out.println("Dat ist eine Wand du Idiot");
                            isHit = true;
                        }
                    }
                }
                if (!isHit) {
                    moveBullet((BulletMainWeapon) element);
                }
            }
        }
        if (toRemove != null){
            elementList.remove(toRemove);
        }
    }

    public void playerMovementDetection() {

        ArrayList<LevelElement> filteredList = new ArrayList<>();

        for (LevelElement element : elementList) {
            if (element.getType().equals("tank") || element.getType().equals("block")) {
                filteredList.add(element);
            }
        }

        for (int i = 1; i < filteredList.size(); i++) {
            if (elementList.get(0).getBoundsInParent().intersects(filteredList.get(i).getBoundsInParent())) {
                if (elementList.get(0).getRotate() == 360.0) {
                    elementList.get(0).setLayoutY(filteredList.get(0).getLayoutY() + 5);
                } else if (elementList.get(0).getRotate() == 90.0) {
                    elementList.get(0).setLayoutX(filteredList.get(0).getLayoutX() - 5);
                } else if (elementList.get(0).getRotate() == 180.0) {
                    elementList.get(0).setLayoutY(filteredList.get(0).getLayoutY() - 5);
                } else if (elementList.get(0).getRotate() == 270.0) {
                    elementList.get(0).setLayoutX(filteredList.get(0).getLayoutX() + 5);
                }
            }
        }
    }

    public void handle(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.W) {
            System.out.println("Up: " + keyEvent.getCode() + "Aktueller Kurs: " + elementList.get(whichTank).getRotate());
            ((Tank) elementList.get(0)).moveTank(360.0);
        }
        if (keyEvent.getCode() == KeyCode.S) {
            System.out.println("Down: " + keyEvent.getCode() + "Aktueller Kurs: " + elementList.get(whichTank).getRotate());
            ((Tank) elementList.get(0)).moveTank(180.0);
        }
        if (keyEvent.getCode() == KeyCode.D) {
            System.out.println("Right: " + keyEvent.getCode() + "Aktueller Kurs: " + elementList.get(whichTank).getRotate());
            ((Tank) elementList.get(0)).moveTank(90.0);
        }
        if (keyEvent.getCode() == KeyCode.A) {
            System.out.println("Left: " + keyEvent.getCode() + "Aktueller Kurs: " + elementList.get(whichTank).getRotate());
            ((Tank) elementList.get(0)).moveTank(270.0);
        }
        if (keyEvent.getCode() == KeyCode.SPACE) {
            System.out.println("FEUERTASTE: " + keyEvent.getCode() + "Aktueller Kurs: " + elementList.get(whichTank).getRotate());
            fireMainWeapon(elementList.get(0));
        }
    }

    private void fireMainWeapon(LevelElement myTank) {
        if(canShoot){
            canShoot = false;
            Tank tank = (Tank) myTank;
            double[] bsp = tank.setCorrectBulletPosition(myTank);
            gameView.createMainBullet(myTank, bsp);
        }
    }

    public void moveBullet(BulletMainWeapon bullet) {
        double rotation = bullet.getRotate();
        if (rotation == 90.0) {
            bullet.setX(bullet.getX() + 5);
        } else if (rotation == 360 || rotation == 0) {
            bullet.setY(bullet.getY() - 5);
        } else if (rotation == 270) {
            bullet.setX(bullet.getX() - 5);
        } else if (rotation == 180) {
            bullet.setY(bullet.getY() + 5);
        }
    }

    public void setElementList(ObservableList<LevelElement> elementList) {
        this.elementList = elementList;
    }

    public void setETankApplication(ETankApplication eTankApplication) {
        this.eTankApplication = eTankApplication;
    }

    public void setGame(GameLobby gameLobby) {
        this.gameLobby = gameLobby;
    }

    public void setGamePlay(ObservableList<LevelElement> elementList) {
        this.gamePlay = new GamePlay();
        gamePlay.setElementList(elementList);
    }

    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }

    public GamePlay getGamePlay() {
        return this.gamePlay;
    }

}
