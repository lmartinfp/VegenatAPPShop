package asee.giis.unex.vegenatnavigationdrawer.ui.products;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.Product;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class DummyContent {

    /**
     * An array of sample (Product) items.
     */
    public static final List<Product> ITEMS = new ArrayList<Product>();

    /**
     * A map of sample (Product) items, by ID.
     */
    public static final Map<String, Product> ITEM_MAP = new HashMap<String, Product>();

    private static final int COUNT = 10;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createProduct(i));
        }
    }

    private static void addItem(Product item) {
        ITEMS.add(item);
        ITEM_MAP.put(String.valueOf(item.getId()), item);
    }

    private static Product createProduct(int position) {
        return new Product(position, "", "", 0.0f, "");
    }

}