import oop.ex2.SpaceShipPhysics;

/**
 * this is the Runner SpaceShip, it will always try to run away from the fight.
 * meaning it's main feature is to locate threat and move or teleport away from it.
 */

public class Runner extends SpaceShip {

    /**a threat distance boundry that below it the ship will teleport*/
    private static final double TELEPORT_DISTANCE_THRESHOLD = 0.25;
    /**a threat angle boundry that below it the ship will teleport*/
    private static final double TELEPORT_ANGLE_THRESHOLD = 0.23;
    /**the constant status of the ship acceleration movement*/
    private static final boolean ACCELRATE_STATUS = true;

    /**
     * Does the actions of this ship for this round.
     * This is called once per round by the SpaceWars game driver.
     * @param game the game object to which this ship belongs.
     */
    public void doAction(SpaceWars game) {
        makeMove(game);
        this.chargeEnergy();
    }

    /**
     * checks where is the closest ship and turn in the other direction.
     * by default, ship will turn right if the closest ship is straight ahead.
     * Note - angle from this ship to the threat < 0 => the threat is on the right, and vice versa.
     * @param thisShip - this ship physics instance
     * @param otherShip - the physics instance of closest ship to "thisShip"
     * @return int represent which way to turn.
     */
    private int decideDirection(SpaceShipPhysics thisShip, SpaceShipPhysics otherShip) {
        double threatAngle = thisShip.angleTo(otherShip);

        int otherWay = this.DONT_TURN;
        if (threatAngle < 0) { //if the threat is in the right, turn left
            otherWay = this.TURN_LEFT;
        } else if (threatAngle > 0) {
            otherWay = this.TURN_RIGHT;
        } else if (threatAngle == 0) {
            otherWay = this.TURN_RIGHT; //default option, if the threat iz ahead.
        }
        return otherWay;
    }

    /**
     * checks whether the threat to this ship
     * (closest) is below fixed thresholds of distance and angle.
     * @param thisShip - this ship physics instance
     * @param otherShip - the physics instance of closest ship to "thisShip"
     * @return true if teleport is required, false otherwise.
     */
    private boolean decideTeleport(SpaceShipPhysics thisShip, SpaceShipPhysics otherShip) {
        double threatDistance = thisShip.distanceFrom(otherShip);
        return ((threatDistance < TELEPORT_DISTANCE_THRESHOLD) &&
                (otherShip.angleTo(thisShip) < Math.abs(TELEPORT_ANGLE_THRESHOLD)));
    }

    /**
     * Preform movements of the ship, E.g telporting and accelerating and turning/
     * @param game - the instance of the current SpaceWars game.
     */
    private void makeMove(SpaceWars game) {
        SpaceShipPhysics thisShip = this.getPhysics();
        SpaceShipPhysics threat = game.getClosestShipTo(this).getPhysics();
        if (this.decideTeleport(thisShip, threat)) {
            this.teleport();
        } else {
            int desiredDirection = this.decideDirection(thisShip, threat);
            thisShip.move(ACCELRATE_STATUS, desiredDirection);
        }
    }
}