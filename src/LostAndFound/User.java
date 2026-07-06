package LostAndFound.src.LostAndFound;

import java.util.*;
import java.io.Serializable;

public class User implements Serializable{
    private static final long serialVersionUID = 1L;

    private String userId;
    private String name;
    private String email;
    private String phone;
    private String password;
    private int role;
    private String adminPassword;
    private ArrayList<FoundItems> myFoundItems = new ArrayList<>();

    // -----------------------------
    // Default constructor
    // -----------------------------
    public User() {}

    // -----------------------------
    // Parameterized constructor
    // -----------------------------
    public User(String userId, String name, String email, String phone, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.adminPassword="ccoew@123";
        this.myFoundItems = new ArrayList<>();

    }


    // -----------------------------
    // Display user details
    // -----------------------------
    public void displayUserDetails() {
        System.out.println("User ID: " + userId);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phone);
    }



    public void addFoundItem(FoundItems foundItem) {
        if (myFoundItems == null) {
            myFoundItems = new ArrayList<>();
        }
        if (!myFoundItems.contains(foundItem)) {
            myFoundItems.add(foundItem);
        }
    }

    public void viewMyFoundItems() {
        if (myFoundItems == null || myFoundItems.isEmpty()) {
            System.out.println("You currently have no found items.");
            return;
        }

        System.out.println("Your Found Items:");
        for (FoundItems item : myFoundItems) {
            System.out.println(item);
        }
    }


    // -----------------------------
    // Getters
    // -----------------------------
    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getRole(){
        return role;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    // -----------------------------
    // Setters
    // -----------------------------
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
