import oop.ex2.SpaceShipPhysics;

/**
 * the special ship, her main feature is that it sits and wait for a pray
 * (given a proximity degree) and teleports on it).
 */
public class Special extends Aggressive {

    /**
     * how long the ship pursue a specific target
     */
    private static int ASSAULT_TIME = 100;

    /**
     * the target located current position
     */
    private SpaceShipPhysics targetShip;

    /**cyclic counter for how many rounds the ship is at a pursue (mod ASSUALT TIME).*/
    private int targetCounter = 0;


    /**
     * the driver of the ships actions in each round.
     * @param game the game object to which this ship belongs.
     */
    public void doAction(SpaceWars game) {
        if (targetCounter == 0) {
            teleport();
            replaceTarget(game);
        } else {
            updateTargetCyclicCounter();
        }
        SpaceShipPhysics thisShip = this.getPhysics();
        makeMove(thisShip,targetShip);
        if (this.gunDisabled) {this.updateGunCooldownStatus(); }
        if (this.isFireNecessary(thisShip,targetShip)){
            this.fire(game);}
        this.chargeEnergy();
    }

    /**creates and update the cyclic count of  how long is the ship in pursue after a single ship*/
    private void updateTargetCyclicCounter() {
        targetCounter = (++targetCounter % ASSAULT_TIME);
    }

    /**after the target cycle was finish, the ship finds another target
     * @param game - the current game instance*/
    private void replaceTarget(SpaceWars game) {
        targetShip = game.getClosestShipTo(this).getPhysics();
    }

}


