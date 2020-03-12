import oop.ex3.spaceship.Item;

import java.util.Map;

public interface PhysicalStorage {
    int SUCCESSFUL_OPERATION = 0;
    int FAILED_OPERATION = -1;

    int addItem(Item item, int n);

    int getItemCount(String type);

    Map<String, Integer> getInventory();

    int getCapacity();

    int getAvailableCapacity();

}
