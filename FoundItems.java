package LostAndFound;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class FoundItems extends Item{
    private String dateFound;

    void acceptItemDetails(String userId,Scanner sc) {
        // Input common item details
       inputCommonDetails(userId,sc);

        // Input date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        boolean validDate = false;
        do {
            System.out.print("Enter the date when the item was found(yyyy-MM-dd): ");
            String input = sc.nextLine().trim();
            try {
                LocalDate date = LocalDate.parse(input, formatter);
                if (date.isAfter(LocalDate.now())) {
                    System.out.println("Date cannot be in the future!");
                } else {
                    dateFound = input;
                    validDate = true;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format! Please use yyyy-MM-dd.");
            }
        } while (!validDate);

        // Generate UUID safely
        System.out.println("Found item ID: " + getItemId());
    }

    // Found item details
    @Override
    public void displayItemDetails() {
        System.out.println("--Details of Found Item--");
        System.out.println("ID: " + getItemId());
        System.out.println("Name: " + getItemName());
        System.out.println("Location: " + getLocation());
        System.out.println("Date Found: " + getDateFound());
        System.out.println("Category: " + getCategory());
    }

    String getDateFound() {
        return dateFound;
    }

}
