package LostAndFound.src.LostAndFound;

import java.io.*;
import java.util.*;

class LostItemsManager {

    private ArrayList<LostItem> lostItemsList;

    private Stack<LostItem> undoStack;

    private Stack<LostItem> redoStack;

    private HashMap<String, ArrayList<LostItem>> categoryMap;

    private HashMap<String, LostItem> itemsById;

    private final HashSet<String> lostItemKeys=new HashSet<>();

    private static final String FILE_NAME = "lost_items_data.dat";



    public LostItemsManager() {

        lostItemsList = new ArrayList<>();

        undoStack = new Stack<>();

        redoStack = new Stack<>();

        categoryMap = new HashMap<>();

        itemsById = new HashMap<>();

        loadFromFile(); // load existing data

        // rebuild indexes
        for (LostItem it : lostItemsList) {

            itemsById.put(it.getItemId(), it);

            categoryMap
                    .computeIfAbsent(it.getCategory(), k -> new ArrayList<>())
                    .add(it);

            lostItemKeys.add(buildKey(it));
        }

    }



    public void addLostItem(String userId, Scanner sc) {

        if (userId == null || userId.trim().isEmpty()) {

            System.out.println("Invalid user ID. Cannot add lost item.");

            return;

        }

        LostItem item = new LostItem();

        item.acceptItemDetails(userId,sc);

        if (checkDuplicates(item)) {

            System.out.println("Item report has been logged already!!!");

            return;

        }

        lostItemsList.add(item);
        lostItemKeys.add(buildKey(item));

        undoStack.push(item);

        redoStack.clear();

        itemsById.put(item.getItemId(), item);

        categoryMap.computeIfAbsent(item.getCategory(), k -> new ArrayList<>()).add(item);

        saveToFile();

        System.out.println("Successfully added the item to our lost items list !!");

    }



    public void displayLostItems(String currentUserId) {

        if (lostItemsList.isEmpty()) {

            System.out.println("No lost items recorded yet.");

            return;

        }

        System.out.println("List of Lost Items");

        int i = 1;

        for (LostItem item : lostItemsList) {

            if (currentUserId == null || Objects.equals(item.getUserId(), currentUserId)) {

                System.out.print(i + ". ");

                item.displayItemDetails();

                System.out.println();

                i++;

            }

        }

        if (i == 1) {

            System.out.println(currentUserId == null ? "No lost items in system." : "You have not reported any lost items yet.");

        }

    }

    public void searchItem(String keyword, String currentUserId) {

        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("Search keyword cannot be empty.");
            return;
        }

        keyword = keyword.trim().toLowerCase();

        boolean found = false;
        int count = 0;

        for (LostItem item : lostItemsList) {

            boolean belongsToUser =
                    currentUserId == null ||
                            Objects.equals(item.getUserId(), currentUserId);

            boolean matchesKeyword =
                    (item.getItemName() != null &&
                            item.getItemName().toLowerCase().contains(keyword))
                            ||  (item.getDescription() != null &&
                            item.getDescription().toLowerCase().contains(keyword))
                            ||  (item.getLocation() != null &&
                            item.getLocation().toLowerCase().contains(keyword))
                            ||  (item.getCategory() != null &&
                            item.getCategory().toLowerCase().contains(keyword));

            if (belongsToUser && matchesKeyword) {
                item.displayItemDetails();
                found = true;
                count++;
            }
        }

        if (!found) {
            System.out.println("No item found matching the keyword \"" + keyword + "\"");
        } else {
            System.out.println("Found " + count + " item(s) matching your search.");
        }
    }

    public void searchById(String id) {

        if (id == null || id.trim().isEmpty()) {

            System.out.println("ID cannot be empty.");

            return;

        }

        LostItem item = itemsById.get(id);

        if (item != null) {

            System.out.println("Item found by ID:");

            item.displayItemDetails();

        } else {

            System.out.println("No item found with ID: " + id);

        }

    }


    // Undo / Redo

    public void undoItem() {

        if (!undoStack.isEmpty()) {

            LostItem lastItem = undoStack.pop();

            lostItemsList.remove(lastItem);
            lostItemKeys.remove(buildKey(lastItem));

            redoStack.push(lastItem);

            itemsById.remove(lastItem.getItemId());

            ArrayList<LostItem> catItems = categoryMap.get(lastItem.getCategory());

            if(catItems != null){

                catItems.remove(lastItem);

                if(catItems.isEmpty()){
                    categoryMap.remove(lastItem.getCategory());
                }
            }

            saveToFile();

            System.out.println("Undo successful! Removed: " + lastItem.getItemName());

        } else {

            System.out.println("No actions to undo!");

        }

    }



    public void redoItem() {

        if (!redoStack.isEmpty()) {

            LostItem item = redoStack.pop();

            undoStack.push(item);

            lostItemsList.add(item);
            lostItemKeys.add(buildKey(item));

            itemsById.put(item.getItemId(), item);

            categoryMap.computeIfAbsent(item.getCategory(), k -> new ArrayList<>()).add(item);

            saveToFile();

            System.out.println("Redo successful: " + item.getItemName());

        } else {

            System.out.println("No items to redo!");

        }

    }



    public boolean checkDuplicates(LostItem item) {
        return lostItemKeys.contains(buildKey(item));
    }



    public ArrayList<LostItem> getLostItemsList() { return lostItemsList; }


    // -----------------------------

    // Save & Load lost items

    // -----------------------------

    public void saveToFile() {

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(lostItemsList);

            //System.out.println("Lost items saved.");

        } catch (IOException e) {

            System.out.println("Error saving lost items: " + e.getMessage());

        }

    }

    public void loadFromFile() {

        File f = new File(FILE_NAME);

        if (!f.exists()) {

            //System.out.println("(No previous lost items data found)");

            return;

        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            Object data = ois.readObject();

            if (data instanceof ArrayList) {

                lostItemsList = (ArrayList<LostItem>) data;

            } else if (data instanceof HashMap) {

                // convert old HashMap<Integer, LostItem> to ArrayList

                HashMap<?, ?> map = (HashMap<?, ?>) data;

                lostItemsList = new ArrayList<>();

                for (Object v : map.values()) {

                    if (v instanceof LostItem) lostItemsList.add((LostItem) v);

                }

                System.out.println("⚙️ Converted old lost-items HashMap to ArrayList.");

            }

            //System.out.println("Lost items loaded.");

        } catch (IOException | ClassNotFoundException e) {

            System.out.println("Error loading lost items: " + e.getMessage());

        }

    }

    private String buildKey(LostItem item) {
        return (item.getItemName().trim().toLowerCase() + "|"
                + item.getLocation().trim().toLowerCase());
    }

}



