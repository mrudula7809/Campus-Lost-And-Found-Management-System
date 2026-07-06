package LostAndFound.src.LostAndFound;

import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LostItem extends Item {

    private String dateLost;
    private String status; // Lost, Found, Resolved

    public LostItem() {
        status = "Lost"; // default status
    }

    void acceptItemDetails(String userId,Scanner sc) {

        inputCommonDetails(userId,sc);

        // Date input
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        boolean validDate = false;
        do {
            System.out.print("Enter the date when you lost your item (yyyy-MM-dd): ");
            String input = sc.nextLine().trim();
            try {
                LocalDate date = LocalDate.parse(input, formatter);
                if (date.isAfter(LocalDate.now())) {
                    System.out.println("Date cannot be in the future!");
                } else {
                    dateLost = input;
                    validDate = true;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format! Please use yyyy-MM-dd.");
            }
        } while (!validDate);

        // Generate unique ID
        System.out.println("Your lost item ID = " + getItemId());
    }
    @Override
    public void displayItemDetails() {
        System.out.println("--Details of lost object(s)--");
        System.out.println("ID       : " + getItemId());
        System.out.println("Name     : " + getItemName());
        System.out.println("Description : " + getDescription());
        System.out.println("Location : " + getLocation());
        System.out.println("Lost Date: " + getDateLost());
        System.out.println("Category : " + getCategory());
        System.out.println("Status   : " + getStatus());
    }


    // Getters
    String getDateLost() { return dateLost; }
    String getStatus() { return status; }

    // Setter
    public void setStatus(String status) {
        if (status.equalsIgnoreCase("Lost") || status.equalsIgnoreCase("Found") || status.equalsIgnoreCase("Resolved")) {
            this.status = status;
        } else {
            System.out.println("Invalid status! Must be Lost, Found, or Resolved.");
        }
    }
}
