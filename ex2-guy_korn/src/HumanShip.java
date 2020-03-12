import oop.ex2.GameGUI;

public class HumanShip extends SpaceShip {

    private int count =0;

    /**
     * adjust the actions of a human controlled ship, same features with different input.
     * @param game the game object to which this ship belongs.
     */
    public void doAction(SpaceWars game) {
        this.shieldIsOn = false; // deactivating shield after every round.
        GameGUI gameGui = game.getGUI();
        makeMove(gameGui);
        if (gameGui.isShieldsPressed()){this.shieldOn();}
        if (this.gunDisabled){this.updateGunCooldownStatus();}
        if (gameGui.isShotPressed()) {this.fire(game);}
        this.chargeEnergy(); // prioritized code replica in the price of expanding API.d
    }

    /**
     * Preform a check on the user input (keys pressed) whether
     * to turn and to which side.
     * @param gui - The current GameGui object.
     * @return int representing decision.
     */
    private int decideDirection(GameGUI gui){
        if (gui.isLeftPressed() && gui.isRightPressed())
            return DONT_TURN;
        if (gui.isRightPressed())
            return TURN_RIGHT;
        else if (gui.isLeftPressed())
            return TURN_LEFT;
        else return DONT_TURN;
    }

    /**Preform the movement part in a single game round
     * @param gameGui - current gui of the game.
     */
    private void makeMove(GameGUI gameGui) {
        if (gameGui.isTeleportPressed())
            this.teleport();
        this.getPhysics().move(gameGui.isUpPressed(), this.decideDirection(gameGui));
    }
}
