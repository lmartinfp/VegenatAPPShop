package asee.giis.unex.vegenatnavigationdrawer.ui.lista_compra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.ProductWithQuantity;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class DummyContent {

    /**
     * An array of sample (productwithquantity) items.
     */
    public static final List<ProductWithQuantity> ITEMS = new ArrayList<ProductWithQuantity>();

    /**
     * A map of sample (productwithquantity) items, by ID.
     */
    public static final Map<String, ProductWithQuantity> ITEM_MAP = new HashMap<String, ProductWithQuantity>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createProductWithQuantity(i));
        }
    }

    private static void addItem(ProductWithQuantity item) {
        ITEMS.add(item);
        ITEM_MAP.put(String.valueOf(item.getIdItemLista()), item);
    }

    private static ProductWithQuantity createProductWithQuantity(int position) {
        return new ProductWithQuantity(position, "", 0, 0.0f);
    }

}