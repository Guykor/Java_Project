import java.util.Random;

/**
 * the Drunkard ship, the driver acts like he had a drink or more.
 * will mainly fire as long as he can, and will fall asleep on the wheel once in a while.
 */
public class Drunkard extends SpaceShip {
    /**max sleep time for the driver to sleep on the wheel.*/
    private final static int MAX_SLEEP_TIME = 100;
    /** the random generator used in the class*/
    private final static Random randomGenerator = new Random();
    /** the driver sleeping period, randomly picked within MAX_SLEEP_TIME boundry.*/
    private final static int SLEEPING_CYCLE = 1 + randomGenerator.nextInt(MAX_SLEEP_TIME); //ranges within [1:MAX_SLEEP_TIME]
    /**the  sleeping counter, will be used in the class as a cyclic counter (mod SLEEPING_CYCLE)*/
    private int sleepCounter = 0;
    /**acceleration flag to use in the game.move method*/
    private boolean accelerateFlag = true;
    /**direction flag to use in the game.move method*/
    private int directionFlag;
    /**random state decided binary for shield or fire pick*/
    private int fireShieldFlag = 0;


    /**
     * a random state for each feature will remain static within every the sleeping cycle.
     * @param game the game object to which this ship belongs.
     */
    public void doAction(SpaceWars game) {
        this.shieldIsOn = false;
        sleepCounter = (++sleepCounter % SLEEPING_CYCLE);
        makeMove();
        if (this.gunDisabled) {
            this.updateGunCooldownStatus();
        }
        alternateFireShield();
        switch (fireShieldFlag){
            case 1:
                this.fire(game);
            case 0:
                this.shieldOn();
        }

        this.chargeEnergy();
    }

    /**
     * alternates randomly between the  3 option - left, right or don't turn.
     */
    private void decideDirection() {
        if (sleepCounter == 0) {
            directionFlag = TURN_RIGHT + randomGenerator.nextInt(TURN_LEFT - TURN_RIGHT + 1); // in the range [-1:1]
        }
    }

    /**
     * every cycle of fixed time the ship will accelerate or stop alternately.
     */
    private void decideAcceleration() {
        if (sleepCounter == 0) { //if sleep cycle was over
            accelerateFlag = !accelerateFlag;
        }
    }

    /**
     * Preform movements of the ship.
     */
    private void makeMove() {
        decideDirection();
        decideAcceleration();
        this.getPhysics().move(accelerateFlag, directionFlag);
    }
    private void alternateFireShield(){
        if (sleepCounter == 0){
            fireShieldFlag = randomGenerator.nextInt(2);
        }
    }
}

