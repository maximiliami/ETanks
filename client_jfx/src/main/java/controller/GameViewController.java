package controller;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import main.ETankApplication;
import model.Game;
import model.Level;
import java.util.Objects;


public class GameViewController {
    private ETankApplication eTankApplication;
    private Game game;
    private Level level;
    private Timeline timeline;

    public GameViewController() {

    }
    @FXML
    private GridPane ground;

    @FXML
    private Group tankOne;

    @FXML
    public void initialize() {
        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 30; col++) {
                ImageView cell = new ImageView();
                cell.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("../img/images/levelBackground/Ground_Tile_02_A.png"))));
                cell.setFitHeight(40.0);
                cell.setFitWidth(40.0);
                ground.add(cell, col, row);
                System.out.println(col + " " +row);
            }
        }
        ImageView tankHullA = new ImageView();
        ImageView tankWeaponA = new ImageView();
    }

    public void keyPressed(KeyEvent keyEvent) {
        Group myTank = tankOne;
        if (keyEvent.getCode() == KeyCode.W) {
            moveTank(myTank, 0.0);
        }
        if (keyEvent.getCode() == KeyCode.S) {
            moveTank(myTank, 180.0);
        }
        if (keyEvent.getCode() == KeyCode.D) {
            moveTank(myTank, 90.0);
        }
        if (keyEvent.getCode() == KeyCode.A) {
            moveTank(myTank, 270.0);
        }
        if (keyEvent.getCode() == KeyCode.SPACE) {
            fireMainWeapon(myTank);
            System.out.println("FEUERTASTE: " + keyEvent.getCode());
        }
    }
    //TODO auslagern nach Tank
    private void fireMainWeapon(Group myTank) {
        double[] bsp = setCorrectPosition(myTank);
        //BulletMainWeapon bmw = new BulletMainWeapon(bsp[0], bsp[1], setMainBulletPath(), myTank.getRotate(), true,);
        ImageView mainBullet = new ImageView();
        mainBullet.setFitHeight(5);
        mainBullet.setFitWidth(5);
        mainBullet.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(setMainBulletPath()[5]))));
        mainBullet.setLayoutX(bsp[0]);
        mainBullet.setLayoutY(bsp[1]);
        mainBullet.setDisable(false);
        mainBullet.setVisible(true);
        System.out.println(mainBullet.getFitHeight() +" "+ mainBullet.getFitWidth());
    }
    //TODO auslagern nach Tank
    private void fireSecondaryWeapon(Group myTank) {
    }

    //TODO auslagern nach Tank?
    private double[] setCorrectPosition(Group myTank) {
        double[] bulletStartPosition = new double[2];
        if(myTank.getRotate() == 0) {
            bulletStartPosition[0] = myTank.getLayoutX()+17.5;
            bulletStartPosition[1] = myTank.getLayoutY()+0.0;
        } else if (myTank.getRotate() == 90.0) {
            bulletStartPosition[0] = myTank.getLayoutX()+0.0;
            bulletStartPosition[1] = myTank.getLayoutY()-17.5;
        } else if (myTank.getRotate() == 180.0) {
            bulletStartPosition[0] = myTank.getLayoutX()+17.5;
            bulletStartPosition[1] = myTank.getLayoutY()+0.0;
        } else if (myTank.getRotate() == 270.0) {
            bulletStartPosition[0] = myTank.getLayoutX()+0.0;
            bulletStartPosition[1] = myTank.getLayoutY()+17.5;
        }
        return bulletStartPosition;
    }
    //TODO Auslagern nach BulletMainWeapon!
    private String[] setMainBulletPath() {
        String[] bulletColours = new String[6];
        bulletColours[0] = "../img/images/bullets/Flash_A_01.png";
        bulletColours[1] = "../img/images/bullets/Flash_A_02.png";
        bulletColours[2] = "../img/images/bullets/Flash_A_03.png";
        bulletColours[3] = "../img/images/bullets/Flash_A_04.png";
        bulletColours[4] = "../img/images/bullets/Flash_A_05.png";
        bulletColours[5] = "../img/images/bullets/Medium_Shell.png";
        return  bulletColours;
    }

    //TODO Auslagern nach Tank
    public void moveTank(Group myTank, double newCourse) {
        double speed = 5.0;
        RotateTransition rt = new RotateTransition(Duration.seconds(0.2), myTank);
        rt.setFromAngle(myTank.getRotate());
        rt.setToAngle(newCourse);
        if(rt.getStatus() != Animation.Status.RUNNING) {
            if(myTank.getRotate() == newCourse) {
                if(newCourse==0.0) {
                    myTank.setLayoutY(myTank.getLayoutY()-speed);
                } else if (newCourse==180.0) {
                    myTank.setLayoutY(myTank.getLayoutY()+speed);
                } else if (newCourse==90.0) {
                    myTank.setLayoutX(myTank.getLayoutX()+speed);
                } else if (newCourse==270.0) {
                    myTank.setLayoutX(myTank.getLayoutX()-speed);
                }
            } else {
                rt.play();
            }
        }
    }

    public void keyTyped(KeyEvent keyEvent) {
        Group myTank = tankOne;
        if (keyEvent.getCode() == KeyCode.SPACE) {
            fireMainWeapon(myTank);
        }
        if (keyEvent.getCode() == KeyCode.ENTER) {
            fireSecondaryWeapon(myTank);
        }
    }

    public void setETankApplication(ETankApplication eTankApplication) {
        this.eTankApplication = eTankApplication;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
