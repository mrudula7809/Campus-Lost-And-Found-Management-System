package LostAndFound;

import java.io.*;
import java.util.*;

class FoundItemsManager {

    private ArrayList<FoundItems> foundItemsList;

    private final HashSet<String> foundItemKeys = new HashSet<>();

    private Stack<FoundItems> undoStack;

    private Stack<FoundItems> redoStack;

    private static final String FILE_NAME = "found_items_data.dat";


    public FoundItemsManager() {

        foundItemsList = new ArrayList<>();

        undoStack = new Stack<>();

        redoStack = new Stack<>();

        loadFromFile();

        rebuildKeySet(); // reconstruct keys after loading existing items

    }

    //Adding found items to FoundItems list
    public void addFoundItems(String userId, int role, Scanner sc) {

        if (role != 2) {

            System.out.println("Only admins can add found items.");

            return;

        }

        //object of FoundItems class
        FoundItems fItem = new FoundItems();

        //accepting found item details
        fItem.acceptItemDetails(userId,sc);

        //checking for duplicates
        if (checkDuplicates(fItem)) {

            System.out.println("Item already exists in the found list!");

            return;

        }

        //if not duplicate adding it to the list
        foundItemsList.add(fItem);
        foundItemKeys.add(generateKey(fItem));

        undoStack.push(fItem);

        redoStack.clear();

        saveToFile();

        System.out.println("Item added successfully.");

    }


    // ============================= DUPLICATE CHECK =============================
    //method to check duplicate entries of FoundItems
    private boolean checkDuplicates(FoundItems item) {
        if (item == null) return false;

        String key = generateKey(item);
        return foundItemKeys.contains(key);
    }

    private String generateKey(FoundItems item) {
        // Composite key for uniqueness (case-insensitive)
        return String.join("|",
                item.getItemName().trim().toLowerCase(),
                item.getDescription().trim().toLowerCase(),
                item.getLocation().trim().toLowerCase(),
                item.getCategory().trim().toLowerCase());
    }

    private void rebuildKeySet() {
        foundItemKeys.clear();
        for (FoundItems item : foundItemsList) {
            foundItemKeys.add(generateKey(item));
        }
    }


    public void displayAllFound() {

        if (foundItemsList.isEmpty()) {

            System.out.println("No found items recorded yet.");

            return;

        }

        System.out.println("------- All Found Items -------");

        int count = 1;

        for (FoundItems item : foundItemsList) {

            System.out.print(count++ + ". ");

            item.displayItemDetails();

            System.out.println("---------------------------");

        }

    }


    public void searchFoundItems(String keyword, String currentUserId) {
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("Search keyword cannot be empty.");
            return;
        }

        keyword = keyword.trim().toLowerCase();

        boolean found = false;
        int count = 0;

        for (FoundItems item : foundItemsList) {

            boolean belongsToUser =
                    currentUserId == null ||
                            Objects.equals(item.getUserId(), currentUserId);

            boolean matchesKeyword =
                    (item.getItemName() != null &&
                            item.getItemName().toLowerCase().contains(keyword))
                            ||
                            (item.getDescription() != null &&
                                    item.getDescription().toLowerCase().contains(keyword))
                            ||
                            (item.getLocation() != null &&
                                    item.getLocation().toLowerCase().contains(keyword)||
                                    (item.getCategory() != null &&
                                            item.getCategory().toLowerCase().contains(keyword)));

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


    public ArrayList<FoundItems> getFoundItemsList() {
        return foundItemsList;
    }

    // Save & Load
    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(foundItemsList);
        } catch (IOException e) {
            System.out.println("Error saving found items: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        File f = new File(FILE_NAME);
        if (!f.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            Object data = ois.readObject();

            if (data instanceof ArrayList) {
                foundItemsList = (ArrayList<FoundItems>) data;
            } else if (data instanceof HashMap) {
                HashMap<?, ?> map = (HashMap<?, ?>) data;
                foundItemsList = new ArrayList<>();
                for (Object v : map.values())
                    if (v instanceof FoundItems)
                        foundItemsList.add((FoundItems) v);
                System.out.println("Converted old found-items HashMap to ArrayList.");
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading found items: " + e.getMessage());
        }
    }

    // Undo last found item
    void undoItem() {
        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo!");
            return;
        }

        FoundItems lastItem = undoStack.pop();
        foundItemsList.remove(lastItem);
        foundItemKeys.remove(generateKey(lastItem));

        redoStack.push(lastItem);
        saveToFile();

        System.out.println("Undo successful: Removed " + lastItem.getItemName());

    }

    // Redo last undone found item
    void redoItem() {
        if (redoStack.isEmpty()) {
            System.out.println("Nothing to redo!");
            return;
        }

        FoundItems redoItem = redoStack.pop();
        foundItemsList.add(redoItem);

        foundItemKeys.add(generateKey(redoItem));

        undoStack.push(redoItem);
        saveToFile();

        System.out.println("Redo successful: Re-added " + redoItem.getItemName());

    }
}