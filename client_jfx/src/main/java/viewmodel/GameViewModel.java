package viewmodel;

import de.saxsys.mvvmfx.ViewModel;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.ETankApplication;
import model.game.elements.*;
import model.game.logic.GameLobby;
import model.game.logic.GamePhysics;
import model.game.logic.GamePlay;
import org.boon.core.Sys;
import view.GameView;
import java.util.ArrayList;


public class GameViewModel implements ViewModel {
    ETankApplication eTankApplication;
    GameLobby gameLobby;
    GamePlay gamePlay;
    GameView gameView;

    int whichTank = 2;
    boolean isMovingUp;
    boolean isMovingDown;
    boolean isMovingLeft;
    boolean isMovingRight;
    boolean isFiringMainWeapon;
    boolean canShoot = true;
    double shootDelay = GamePhysics.DELAY_SECOND;

    ObservableList<LevelElement> elementList = FXCollections.observableArrayList();


    public void startTimer() {
        AnimationTimer gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                playerMovementDetection();
                bulletCollisionDetection();
                shootDelayer();
                shootCollector();
            }
        };
        gameTimer.start();
    }

    private void shootCollector(){
        ArrayList<LevelElement> bulletsToRemove = new ArrayList<>();
        for(LevelElement element : elementList){
            if (element.getType().equals("bullet")){
                if(element.getX() == 0 && element.getY() == 0){
                    bulletsToRemove.add(element);
                    System.out.println(bulletsToRemove.size());
                }
            }
        }
        elementList.removeAll(bulletsToRemove);
        bulletsToRemove.clear();
    }

    private void shootDelayer(){
        shootDelay += 0.05;
        if (shootDelay >= 2) {
            canShoot = true;
            shootDelay = 0;
        }
    }

    private void bulletCollisionDetection() {

        boolean isHit = false;
        LevelElement toRemove = null;
        LevelElement toRemoveTwo = null;
        boolean myBullet = false;
        Tank myTankTemp = (Tank) elementList.get(whichTank);

        for (LevelElement element : elementList) {
            if (element.getType().equals("bullet")) {

                BulletMainWeapon tempBullet = ((BulletMainWeapon) element);
                int playerId = tempBullet.getTankFired().getPlayerId();
                if( playerId == myTankTemp.getPlayerId()) {
                    myBullet = true;
                } else {
                    myBullet = false;
                }

                for (int i = 0; i < elementList.size(); i++) {
                    if (elementList.get(i).getType().equals("tank")) {
                        if (element.getBoundsInParent().intersects(elementList.get(i).getBoundsInParent())) {
                            Tank tank = (Tank) elementList.get(i);
                            if(playerId != tank.getPlayerId()){
                                //Bullet ausblenden
                                toRemove = element;
                                element.setDisable(true);
                                isHit = true;
                                if(!myBullet){
                                    //Ich wurde getroffen
                                    ((Tank) elementList.get(whichTank)).reduceLivePoints();
                                    System.out.println("Du wurdest von: "  + tank.getPlayerId() + " getroffen!");
                                    //Hier wird die Statistik des Players aktualisiert
                                    gamePlay.getGameStatistic().setDeaths(gamePlay.getGameStatistic().getDeaths()+1);
                                } else {
                                    //Hier verliert der andere Player Leben
                                    tank.reduceLivePoints();
                                    System.out.println("Player: " + playerId + " Du hast Player: " + tank.getPlayerId() + " getroffen!");
                                    //Hier wird die Statistik des Players aktualisiert
                                    gamePlay.getGameStatistic().setKills(gamePlay.getGameStatistic().getKills()+1);
                                    gamePlay.getGameStatistic().setHitPoints(gamePlay.getGameStatistic().getHitPoints()+10);
                                    gamePlay.getGameStatistic().setGamePoints(gamePlay.getGameStatistic().getGamePoints()+10);
                                    System.out.println("Kills: " + gamePlay.getGameStatistic().getKills() + " HitPoints: " +  gamePlay.getGameStatistic().getHitPoints()+ " GamePoints: " + gamePlay.getGameStatistic().getGamePoints());
                                }
                            }
                        }
                    } else if (elementList.get(i).getType().equals("blockMetal")) {
                        if (element.getBoundsInParent().intersects(elementList.get(i).getBoundsInParent())) {
                            toRemove = element;
                            element.setDisable(true);
                            isHit = true;
                        }
                    } else if (elementList.get(i).getType().equals("blockWood")) {
                        Block woodenBlock = (Block) elementList.get(i);
                        if (element.getBoundsInParent().intersects(elementList.get(i).getBoundsInParent())) {
                            if(woodenBlock.getLives() == 3){
                                woodenBlock.setOpacity(.75);
                                woodenBlock.setLives(2);
                                toRemove = element;
                                isHit = true;
                            } else if (woodenBlock.getLives() == 2){
                                woodenBlock.setOpacity(.50);
                                woodenBlock.setLives(1);
                                toRemove = element;
                                isHit = true;
                            } else if (woodenBlock.getLives() == 1){
                                woodenBlock.setOpacity(.25);
                                woodenBlock.setLives(0);
                                toRemove = element;
                                isHit = true;
                            } else if (woodenBlock.getLives() == 0){
                                if(myBullet){
                                    gamePlay.getGameStatistic().setGamePoints(gamePlay.getGameStatistic().getGamePoints()+5);
                                    System.out.println("Kills: " + gamePlay.getGameStatistic().getKills() + " HitPoints: " +  gamePlay.getGameStatistic().getHitPoints()+ " GamePoints: " + gamePlay.getGameStatistic().getGamePoints());
                                }
                                woodenBlock.setOpacity(.0);
                                toRemove = element;
                                toRemoveTwo = woodenBlock;
                                element.setDisable(true);
                                elementList.get(i).setDisable(true);
                                woodenBlock.setDisable(true);
                                isHit = true;
                            }
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
        if (toRemoveTwo != null){
            elementList.remove(toRemoveTwo);
        }
    }

    private void playerMovementDetection() {

        ArrayList<LevelElement> filteredList = new ArrayList<>();

        for (LevelElement element : elementList) {
            if (element.getType().equals("tank") ||element.getType().equals("blockWood") ||element.getType().equals("blockMetal")) {
                filteredList.add(element);
            }
        }

        for (int i = 0; i < filteredList.size(); i++) {
            Tank myTankTemp = (Tank) elementList.get(whichTank);
            Tank tankTemp;
            Boolean myTank = false;

            if(filteredList.get(i).getType().equals("tank")){
                tankTemp = (Tank) filteredList.get(i);
                if(tankTemp.getPlayerId() == myTankTemp.getPlayerId()) {
                    myTank = true;
                } else {
                    myTank = false;
                }
            }
            if(!myTank){
                if (elementList.get(whichTank).getBoundsInParent().intersects(filteredList.get(i).getBoundsInParent())) {
                    if (elementList.get(whichTank).getRotate() == 360.0) {
                        elementList.get(whichTank).setLayoutY(filteredList.get(whichTank).getLayoutY() + 5);
                    } else if (elementList.get(whichTank).getRotate() == 90.0) {
                        elementList.get(whichTank).setLayoutX(filteredList.get(whichTank).getLayoutX() - 5);
                    } else if (elementList.get(whichTank).getRotate() == 180.0) {
                        elementList.get(whichTank).setLayoutY(filteredList.get(whichTank).getLayoutY() - 5);
                    } else if (elementList.get(whichTank).getRotate() == 270.0) {
                        elementList.get(whichTank).setLayoutX(filteredList.get(whichTank).getLayoutX() + 5);
                    }
                }
            }

        }
    }

    public void handleKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().toString().equals(eTankApplication.getSignedUser().getUserSettings().getMoveUpKey()) || isMovingUp && isFiringMainWeapon) {
            System.out.println("Up: " + keyEvent.getCode() + "Aktueller Kurs: " + elementList.get(whichTank).getRotate());
            this.isMovingUp = true;
            ((Tank) elementList.get(whichTank)).moveTank(360.0);
        }
        if (keyEvent.getCode().toString().equals(eTankApplication.getSignedUser().getUserSettings().getMoveDownKey()) || isMovingDown && isFiringMainWeapon) {
            System.out.println("Down: " + keyEvent.getCode() + "Aktueller Kurs: " + elementList.get(whichTank).getRotate());
            this.isMovingDown = true;
            ((Tank) elementList.get(whichTank)).moveTank(180.0);
        }
        if (keyEvent.getCode().toString().equals(eTankApplication.getSignedUser().getUserSettings().getMoveRightKey()) || isMovingRight && isFiringMainWeapon) {
            System.out.println("Right: " + keyEvent.getCode() + "Aktueller Kurs: " + elementList.get(whichTank).getRotate());
            this.isMovingRight = true;
            ((Tank) elementList.get(whichTank)).moveTank(90.0);
        }
        if (keyEvent.getCode().toString().equals(eTankApplication.getSignedUser().getUserSettings().getMoveLeftKey()) || isMovingLeft && isFiringMainWeapon) {
            System.out.println("Left: " + keyEvent.getCode() + "Aktueller Kurs: " + elementList.get(whichTank).getRotate());
            this.isMovingLeft = true;
            ((Tank) elementList.get(whichTank)).moveTank(270.0);
        }
        if (keyEvent.getCode().toString().equals(eTankApplication.getSignedUser().getUserSettings().getFireMainWeaponKey()) || isFiringMainWeapon) {
            System.out.println("FEUERTASTE: " + keyEvent.getCode() + "Aktueller Kurs: " + elementList.get(whichTank).getRotate());
            this.isFiringMainWeapon = true;
            fireMainWeapon(elementList.get(whichTank));
        }
    }

    public void handleKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode().toString().equals(eTankApplication.getSignedUser().getUserSettings().getMoveUpKey())) {
            this.isMovingUp = false;
        }
        if (keyEvent.getCode().toString().equals(eTankApplication.getSignedUser().getUserSettings().getMoveDownKey())) {
            this.isMovingDown = false;
        }
        if (keyEvent.getCode().toString().equals(eTankApplication.getSignedUser().getUserSettings().getMoveRightKey())) {
            this.isMovingRight = false;
        }
        if (keyEvent.getCode().toString().equals(eTankApplication.getSignedUser().getUserSettings().getMoveLeftKey())) {
            this.isMovingLeft = false;
        }
        if (keyEvent.getCode().toString().equals(eTankApplication.getSignedUser().getUserSettings().getFireMainWeaponKey())) {
            this.isFiringMainWeapon = false;
        }
    }

    private void fireMainWeapon(LevelElement myTank) {
        if(canShoot){
            gamePlay.getGameStatistic().setShots(gamePlay.getGameStatistic().getShots()+1);
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
        System.out.println("Etankap - game view Model");
        this.eTankApplication = eTankApplication;
    }

    public void setGame(GameLobby gameLobby) {
        this.gameLobby = gameLobby;
    }

    public void setGameView(GameView gameView) {
        /*Default Player, später über Lobby übergeben*/
        //Sets Player -> tank
        this.gameView = gameView;
    }

    public void setGamePlay(ObservableList<LevelElement> elementList) {
        gamePlay = new GamePlay();
        //todo SocketClient muss irgendwie in Konstruktor.
        gamePlay.setElementList(elementList);
        gamePlay.setETankApplication(eTankApplication);
        gamePlay.createGameStatistic();
    }

    public GamePlay getGamePlay() {
        return this.gamePlay;
    }
}