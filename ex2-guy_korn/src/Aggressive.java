import oop.ex2.SpaceShipPhysics;

/**
 * The Aggressive ship, will pursue ships and try to fire at them.
 */
public class Aggressive extends Basher {
    private static final double FIRE_DISTANCE_THRESHOLD = 0.21;

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
        if (this.gunDisabled){this.updateGunCooldownStatus();}
        if (isFireNecessary(thisShip,otherShip)){
            this.fire(game);}
        this.chargeEnergy();
    }

    /**
     * checks if the angle of a close ship is within the fixed angle range.
     * @param thisShip - this ship physics instance
     * @param otherShip - the target ship physics instance.
     * @return true if angle is within threshold, false otherwise.
     */
    protected boolean isFireNecessary(SpaceShipPhysics thisShip, SpaceShipPhysics otherShip){
        double angleToTarget = thisShip.angleTo(otherShip);
        return (angleToTarget < Math.abs(FIRE_DISTANCE_THRESHOLD));
        }

}
