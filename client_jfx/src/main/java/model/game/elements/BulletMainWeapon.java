package model.game.elements;
import javafx.scene.image.Image;

import java.util.Objects;

public class BulletMainWeapon extends LevelElement{

    private final Tank FIRINGTANK;
    double rotations;
    private boolean hit = false;

    public BulletMainWeapon(Image image, String type, double positionX, double positionY, double width, double height, double rotation, Tank firingTank) {
        super(image, type, positionX, positionY, width, height, rotation);
        this.FIRINGTANK = firingTank;
        this.rotations = rotations;
    }

    public Tank getTankFired() {
        return FIRINGTANK;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }
}
