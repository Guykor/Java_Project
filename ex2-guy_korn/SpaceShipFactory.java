public class SpaceShipFactory {
    // Constants
    private static final String TYPE_HUMAN = "h";
    private static final String TYPE_RUNNER = "r";
    private static final String TYPE_BASHER = "b";
    private static final String TYPE_AGRESSIVE = "a";
    private static final String TYPE_DRUNKARD = "d";
    private static final String TYPE_SPECIAL = "s";


    public static SpaceShip[] createSpaceShips(String[] args) {
        SpaceShip[] ships = new SpaceShip[args.length];
        for (int i = 0; i < args.length; i++) {
            switch (args[i]){
                case TYPE_HUMAN:
                    ships[i] = new HumanShip();
                    break;
                case TYPE_RUNNER:
                    ships[i] = new Runner();
                    break;
                case TYPE_BASHER:
                    ships[i] = new Basher();
                    break;
                case TYPE_AGRESSIVE:
                    ships[i] = new Aggressive();
                    break;
                case TYPE_DRUNKARD:
                    ships[i] = new Drunkard();
                    break;
                case TYPE_SPECIAL:
                    ships[i] = new Special();
                    break;
                default:
                    ships[i] = new SpaceShip();
                    break;
            }
        }
        return ships;
    }
}

