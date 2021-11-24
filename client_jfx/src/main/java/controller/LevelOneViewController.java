package controller;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import main.ETankApplication;
import java.net.URL;
import java.util.ResourceBundle;


public class LevelOneViewController implements Initializable {

    ETankApplication eTankApplication;
    boolean allowInput = true;

    @FXML
    public Group tankOne;

    @FXML
    public Group tankTwo;

    @FXML
    public Group tankThree;

    @FXML
    public Group tankFour;

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
    }

    public static void moveTank(Group myTank, double newCourse) {
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

    private void fireMainWeapon(Group myTank) {
    }

    private void fireSecondaryWeapon(Group myTank) {
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setETankApplication(ETankApplication eTankApplication) {
        this.eTankApplication = eTankApplication;

    }
}