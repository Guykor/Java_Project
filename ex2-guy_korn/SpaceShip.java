import java.awt.Image;

import oop.ex2.*;

/**
 * The API spaceships need to implement for the SpaceWars game.
 * This is a Super class the space ships types in the game.
 * @author oop
 */

public class SpaceShip {

    // Constants - API integration with other classes
    /**
     * turn left flag for the GameGui.move method
     */
    private static final int TURN_LEFT = 1;

    /**
     * turn right flag for the GameGui.move method
     */
    private static final int TURN_RIGHT = -1;

    /**
     * do not turn flag for the GameGui.move method
     */
    private final int DONT_TURN = 0;

    // Constants - Game Rules
    /**
     * maximal eneregy level, initialized while constructing a spaceship
     */
    private static final int MAX_ENERGY_LEVEL = 210;

    /**
     * current energy level when the ship initialiized
     */
    private static final int INITIALIZED_ENERGY_LEVEL = 190;

    /**
     * current energy level when the ship initialiized
     */
    private final int INITIALIZED_HEALTH_LEVEL = 22;

    /**
     * lower bound for health level
     */
    private static final int HEALTH_LOWER_BOUND = 0;

    /**
     * the energy current level in every round will increase by this number
     */
    private final int ENERGY_CHARGED_PER_ROUND = 1;

    /**
     * collision affect on health level - with shield up.
     */
    private static final int BASH_SHIELD_HEALTH_IMPACT = 0;

    /**
     * collision affect on health level - with no shield.
     */
    private static final int BASH_NO_SHIELD_HEALTH_IMPACT = 1;

    /**
     * collision affect on both on current and maximum energy level - when shield is up
     */
    private final int BASH_SHIELD_ENERGY_IMPACT = 18;

    /**
     * getting hit (collision or shot) affect maximal energy level - with no shield.
     */
    private static final int HIT_NO_SHIELD_MAX_ENERGY_IMPACT = 10;

    /**
     * firing a shot cost to energy level.
     */
    private static final int FIRE_ENERGY_COST = 19;

    /**
     * after shooting once, the ability will be disable for this fixed time.
     */
    private static final int GUN_COOLDOWN_PERIOD = 7;

    /**
     * teleporting cost to energy level.
     */
    private static final int TELEPORT_COST = 140;

    /**
     * shield cost to energy level.
     */
    private static final int SHIELD_ENERGY_COST = 3;


    // Data Members

    /**
     * The Physics object that controls the ship movement and location,
     * initializes the spaceship location randomly when created
     */
    private SpaceShipPhysics shipPhysics;

    /**
     * maximal energy level
     */
    private int maxEnergyLevel;

    /**
     * current energy level, ranges between 0 to maximal energy level
     */
    private int currentEnergyLevel;

    /**
     * current health level, ranges between 0 to 22
     */
    private int healthLevel;

    /**
     * a flag if the ship's shield is on
     */
    private boolean shieldIsOn;

    /**
     * cyclic count for number of rounds passed since last shooting, every times the cycle ends the fire
     * method will reset this counter  to 0
     */
    private int fireCooldownCounter;

    /**
     * a flag for if a fire had been shot in the last cycle
     */
    private boolean gunDisabled;


    /**
     * constructor for the spaceShip superclass,
     */
    private SpaceShip() {
        this.reset();
    }

    /**
     * Does the actions of this ship for this round.
     * This is called once per round by the SpaceWars game driver.
     * Notes - Teleport is a prioritized action, if teleport and other movement keys
     * are pressed, the ship will accelerate or turn from it's updated new location - within the same round!
     *
     * @param game the game object to which this ship belongs.
     */

    public void doAction(SpaceWars game) {
        this.getPhysics().move(true, DONT_TURN);
    }


    /**
     * updating the cyclic counter represents how many rounds past since the  last shot fired
     */
    protected void updateGunCooldownStatus() {
        this.fireCooldownCounter = ++this.fireCooldownCounter % GUN_COOLDOWN_PERIOD;
        if (this.fireCooldownCounter == 0) {
            this.gunDisabled = false;
        }
    }

    /**
     * charges the energy current level by a fixed value.
     */
    protected void chargeEnergy() {
        if (this.currentEnergyLevel <= this.maxEnergyLevel)
            this.currentEnergyLevel += ENERGY_CHARGED_PER_ROUND;
    }

    /**
     * subtract health level by a constant factor of the ship after collision,
     * depends if the shield is on.
     */
    private void contactHealthImpact() {
        if (this.shieldIsOn)
            this.healthLevel -= BASH_SHIELD_HEALTH_IMPACT;
        else this.healthLevel -= BASH_NO_SHIELD_HEALTH_IMPACT;
    }

    //Design choice - when getting shot with shield  has no impact so it was needed to break this function to both
    // collision with (and with outside method without shield) and only for no shield shot.

    /**
     * adjust current and maximal energy levels by a fixed value,
     * after collision - when shield is on.
     */
    private void collideEnergyImpact() {
        if (this.shieldIsOn) {
            this.maxEnergyLevel = this.maxEnergyLevel + BASH_SHIELD_ENERGY_IMPACT;
            this.currentEnergyLevel = this.currentEnergyLevel + BASH_SHIELD_ENERGY_IMPACT;
        } else shotEnergyImpact();

    }

    /**
     * decrease energy maximal level when the ship collides or gets shot.
     * only if the current energy level is above new max level, current level will align.
     */
    private void shotEnergyImpact() {
        if (!this.shieldIsOn)
            this.maxEnergyLevel = this.maxEnergyLevel - HIT_NO_SHIELD_MAX_ENERGY_IMPACT;
        if (this.currentEnergyLevel > this.maxEnergyLevel)
            this.currentEnergyLevel = this.maxEnergyLevel;
    }

    /**
     * This method is called every time a collision with this ship occurs
     */
    public void collidedWithAnotherShip() {
        this.contactHealthImpact();
        this.collideEnergyImpact();
    }

    /**
     * This method is called whenever a ship has died. It resets the ship's
     * attributes, and starts it at a new random position.
     */
    public void reset() {
        this.shipPhysics = new SpaceShipPhysics();
        this.maxEnergyLevel = MAX_ENERGY_LEVEL;
        this.currentEnergyLevel = INITIALIZED_ENERGY_LEVEL;
        this.healthLevel = INITIALIZED_HEALTH_LEVEL;
        this.shieldIsOn = false;
        this.fireCooldownCounter = 0;

    }

    /**
     * Checks if this ship is dead.
     *
     * @return true if the ship is dead. false otherwise.
     */
    public boolean isDead() {
        return this.healthLevel <= HEALTH_LOWER_BOUND;
    }

    /**
     * Gets the physics object that controls this ship.
     *
     * @return the physics object that controls the ship.
     */
    public SpaceShipPhysics getPhysics() {
        return this.shipPhysics;
    }

    /**
     * This method is called by the SpaceWars game object when ever this ship
     * gets hit by a shot.
     */
    public void gotHit() {
        this.contactHealthImpact();
        this.shotEnergyImpact();
    }

    /**
     * Gets the image of this ship. This method should return the image of the
     * ship with or without the shield. This will be displayed on the GUI at
     * the end of the round.
     *
     * @return the image of this ship.
     */
    public Image getImage() {
        if (this instanceof HumanShip) {
            if (this.shieldIsOn) {
                return GameGUI.SPACESHIP_IMAGE_SHIELD;
            } else {
                return GameGUI.SPACESHIP_IMAGE;
            }
        } else {
            if (this.shieldIsOn) {
                return GameGUI.ENEMY_SPACESHIP_IMAGE_SHIELD;
            } else {
                return GameGUI.ENEMY_SPACESHIP_IMAGE;
            }
        }
    }

    /**
     * Attempts to fire a shot.
     *
     * @param game the game object.
     */
    public void fire(SpaceWars game) {
        if ((this.currentEnergyLevel >= FIRE_ENERGY_COST) && (!this.gunDisabled)) {
            game.addShot(this.shipPhysics);
            this.currentEnergyLevel -= FIRE_ENERGY_COST;
            this.gunDisabled = true;
        }
    }

    /**
     * Attempts to turn on the shield.
     */
    public void shieldOn() {
        if (this.currentEnergyLevel >= SHIELD_ENERGY_COST) {
            this.shieldIsOn = true;
            this.currentEnergyLevel -= SHIELD_ENERGY_COST;
        }
    }

    /**
     * Attempts to teleport.
     */
    public void teleport() {
        if (this.currentEnergyLevel >= TELEPORT_COST) {
            this.shipPhysics = new SpaceShipPhysics();
            this.currentEnergyLevel -= TELEPORT_COST;
        }
    }
}
