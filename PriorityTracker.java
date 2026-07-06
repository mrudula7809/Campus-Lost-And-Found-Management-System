package LostAndFound;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class PriorityTracker {
    private static final Map<String, Integer> prioritizeCategory = new HashMap<>();

    public  PriorityTracker() {
        prioritizeCategory.put("Medical", 5);
        prioritizeCategory.put("Electronics", 4);
        prioritizeCategory.put("Ornaments", 4);
        prioritizeCategory.put("Documents", 3);
        prioritizeCategory.put("Accessories", 2);
        prioritizeCategory.put("Clothing", 2);
        prioritizeCategory.put("Others", 1);
    }

    private final Comparator<LostItem> lostItemPriorityComparator = (a, b) -> {
        int p1 = prioritizeCategory.getOrDefault(a.getCategory(), 1);
        int p2 = prioritizeCategory.getOrDefault(b.getCategory(), 1);
        return Integer.compare(p2, p1); // higher value = higher priority
    };

    public Comparator<LostItem> getLostItemPriorityComparator() {
        return lostItemPriorityComparator;
    }
}
