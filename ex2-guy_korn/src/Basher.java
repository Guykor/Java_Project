import oop.ex2.SpaceShipPhysics;

/**
 * A subClass for SpaceShip in the game space wars,
 * this type of ship deliberately tries to collide with other ships.
 */
public class Basher extends SpaceShip {
//    TODO: check if the angles shit working as they wanted cause it's shitty.
    /**a closest ship distance boundry for this ship will activate shields*/
    private static final double SHIELD_DISTANCE_THRESHOLD = 0.19;
    /**the constant status of the ship acceleration movement*/
    private static final boolean ACCELRATE_STATUS = true;


    /**
     * Does the actions of this ship for this round.
     * This is called once per round by the SpaceWars game driver.
     * @param game the game object to which this ship belongs.
     */
    public void doAction(SpaceWars game) {
        this.shieldIsOn = false;
        SpaceShipPhysics thisShip = this.getPhysics();
        SpaceShipPhysics otherShip = game.getClosestShipTo(this).getPhysics();
        makeMove(thisShip, otherShip);
        activateShield(thisShip, otherShip);
        this.chargeEnergy();
    }

    /**
     * checks where is the target (closest) ship and turn to it's direction.
     * @param thisShip - this ship physics instance
     * @param otherShip - the target ship physics instance.
     * @return int represent which way to turn.
     */
    private int decideDirection(SpaceShipPhysics thisShip, SpaceShipPhysics otherShip) {
        double targetAngle = thisShip.angleTo(otherShip);

        int targetDir = this.DONT_TURN;
        if (targetAngle < 0) { //if the threat is in the right, turn left
            targetDir = this.TURN_RIGHT;
        } else if (targetAngle > 0) {
            targetDir = this.TURN_LEFT;
        }
        return targetDir;
    }

    /**
     * decides to lift shield up if the
     * closest ship (target) distance is bealow a fixed threshold.
     * @param thisShip - this ship physics instance
     * @param otherShip - the physics instance of closest ship to "thisShip"
     */
    private void activateShield(SpaceShipPhysics thisShip, SpaceShipPhysics otherShip) {
        double targetDistance = thisShip.distanceFrom(otherShip);
        if (targetDistance < SHIELD_DISTANCE_THRESHOLD) {
            this.shieldOn();
        }
    }

    /**
     * Preform movements of the ship.
     * @param thisShip - this ship physics instance
     * @param otherShip - the physics instance of closest ship to "thisShip"
     */
    protected void makeMove(SpaceShipPhysics thisShip, SpaceShipPhysics otherShip) {
        int desiredDirection = this.decideDirection(thisShip, otherShip);
        thisShip.move(ACCELRATE_STATUS, desiredDirection);
        }
}


