package LostAndFound.src.LostAndFound;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import  java.util.Scanner;

public abstract class Item implements Serializable {

    protected String itemId;
    protected String itemName;
    protected String description;
    protected String location;
    protected String category;
    protected String userId;
    protected static final String[] CATEGORIES = {"Medical", "Electronics", "Ornaments", "Clothing", "Documents", "Accessories", "Others"};
    private static final long serialVersionUID = 1L;

    public Item(){
        this.itemId=UUID.randomUUID().toString();
    }

    public String getItemId(){
        return itemId;
    }
    public String getItemName(){
        return itemName;
    }
    public String getDescription(){
        return description;
    }
    public String getLocation(){
        return location;
    }
    public String getCategory(){
        return category;
    }
    public String getUserId(){
        return userId;
    }

    protected void inputCommonDetails(String userId, Scanner sc){
        this.userId=userId;
        // Input name
        do {
            System.out.println("Enter name of the item: ");
            itemName = sc.nextLine().trim();
            if (!isValidText(itemName)) {
                System.out.println("Invalid name. Please enter a proper item name (not just symbols or numbers).");
            }
        } while (!isValidText(itemName));

        // Input description
        do {
            System.out.println("Enter description of the item: ");
            description = sc.nextLine().trim();
            if (!isValidText(description)) {
                System.out.println("Invalid description. Please describe the item properly.");
            }
        } while (!isValidText(description));

        // Input location
        do {
            System.out.println("Enter location where the item was seen: ");
            location = sc.nextLine().trim();
            if (!isValidText(location)) {
                System.out.println("Invalid location. Please enter a valid place name or area.");
            }
        } while (!isValidText(location));

        // Category selection
        System.out.println("Select category:");
        for (int i = 0; i < CATEGORIES.length; i++) {
            System.out.println((i + 1) + ". " + CATEGORIES[i]);
        }

        int choice = 0;
        boolean validChoice = false;
        while (!validChoice) {
            try {
                System.out.print("Enter choice (1-" + CATEGORIES.length + "): ");
                choice = Integer.parseInt(sc.nextLine().trim());
                if (choice < 1 || choice > CATEGORIES.length) {
                    System.out.println("Choice out of range. Try again.");
                } else {
                    validChoice = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a number between 1 and " + CATEGORIES.length);
            }
        }

        if (choice == CATEGORIES.length) { // Others
            do {
                System.out.println("Please describe the category: ");
                category = sc.nextLine().trim();
                if (!isValidText(category)) {
                    System.out.println("Invalid category. Please enter a meaningful name.");
                }
            } while (!isValidText(category));
        } else {
            category = CATEGORIES[choice - 1];
        }

    }

    protected boolean isValidText(String input) {
        if (input == null) return false;

        input = input.trim();

        // Empty or too short
        if (input.isEmpty() || input.length() < 2) return false;

        // Check if it contains at least one alphabetic character
        if (!input.matches(".*[a-zA-Z].*")) return false;

        // Check if it’s not just symbols or special characters
        if (input.matches("^[^a-zA-Z0-9]+$")) return false;

        return true;
    }

    public abstract void displayItemDetails();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Item)) return false;

        Item other = (Item) obj;
        return Objects.equals(itemId, other.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId);
    }
}
