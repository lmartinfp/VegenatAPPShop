package asee.giis.unex.vegenatnavigationdrawer.ui.pedidos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.Order;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class DummyContent {

    /**
     * An array of sample (Order) items.
     */
    public static final List<Order> ITEMS = new ArrayList<Order>();

    /**
     * A map of sample (Order) items, by ID.
     */
    public static final Map<String, Order> ITEM_MAP = new HashMap<String, Order>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addOrder(createOrder(i));
        }
    }

    private static void addOrder(Order item) {
        ITEMS.add(item);
        ITEM_MAP.put(String.valueOf(item.getId_order()), item);
    }

    private static Order createOrder(int position) {
        return new Order(position, " ", 0.0f, "");
    }

}