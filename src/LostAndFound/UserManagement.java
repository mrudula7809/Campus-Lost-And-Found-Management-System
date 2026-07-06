package LostAndFound.src.LostAndFound;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;

class UserManagement {

    private HashMap<String, User> users = new HashMap<>();

    private static final String FILE_NAME = "users_data.dat";

    private final String adminPassword = "ccoew@123";

    private Scanner sc;



    public UserManagement(Scanner sc) {

        this.sc = sc;

        loadFromFile();

    }



    // registration (student only)

    public void addUser() {

        User newUser = new User();

        newUser.setRole(1); // student



        System.out.println("\n--- Register New Student ---");

        System.out.print("Enter User ID: ");

        String userId = sc.nextLine().trim().toUpperCase();

        if (!isValidUserId(userId)) {

            System.out.println("Invalid User ID! Registration failed.");

            return;

        }

        if (users.containsKey(userId)) {

            System.out.println("User ID already exists! Try again with a different ID.");

            return;

        }

        newUser.setUserId(userId);



        System.out.print("Enter Name: ");

        newUser.setName(sc.nextLine().trim());



        System.out.print("Enter College Email: ");

        String email = sc.nextLine().trim();

        if (!isValidCollegeEmail(email)) {

            System.out.println("Invalid College Email! Registration failed.");

            return;

        }

        newUser.setEmail(email);



        System.out.print("Enter Phone Number : ");

        String phone = sc.nextLine().trim();

        if (!phone.isEmpty()) newUser.setPhone(phone);



        System.out.print("Set a password for your account: ");

        String pass = sc.nextLine();

        newUser.setPassword(pass);



        users.put(userId, newUser);

        saveToFile();

        System.out.println("Student registered successfully!\n");

    }



    // authenticate user (student or admin)

    public User authenticateUser() {

        System.out.println("\n--- User Login ---");

        System.out.println("Select Role: 1. Student  2. Admin");

        int roleChoice = -1;

        try {

            roleChoice = Integer.parseInt(sc.nextLine().trim());

        } catch (NumberFormatException e) {

            System.out.println("Invalid input! Please enter 1 or 2.");

            return null;

        }



        if (roleChoice == 1) {

            System.out.print("Enter User ID: ");

            String userId = sc.nextLine().trim().toUpperCase();

            System.out.print("Enter Password: ");

            String pass = sc.nextLine();



            if (users.containsKey(userId)) {

                User user = users.get(userId);

                if (user.getPassword() != null && user.getPassword().equals(pass) && user.getRole() == 1) {

                    System.out.println("Login successful! Welcome, " + user.getName() + ".");

                    return user;

                } else {

                    System.out.println("Password does not match our records!");

                    return null;

                }

            } else {

                System.out.println("Student not found!");

                return null;

            }

        } else if (roleChoice == 2) {

            System.out.print("Enter Admin Password: ");

            String adminPass = sc.nextLine().trim();

            if (adminPass.equals(adminPassword)) {

                User admin = new User();

                admin.setRole(2);

                admin.setName("Admin");

                System.out.println("Admin login successful!");

                return admin;

            } else {

                System.out.println("Wrong Admin Password!");

                return null;

            }

        } else {

            System.out.println("Invalid choice! Enter 1 for Student or 2 for Admin.");

            return null;

        }

    }



    // helpers

    private boolean isValidUserId(String userId) {

        try {

            if (userId.startsWith("UCE")) {

                int num = Integer.parseInt(userId.substring(3));

                if (num >= 2025101 && num <= 2025377) return true; // FY

                if (num >= 2024401 && num <= 2024675) return true; // SY

                if (num >= 2023501 && num <= 2023675) return true; // TY

                if (num >= 2022501 && num <= 2022675) return true; // BTech

            } else if (userId.startsWith("UIT")) {

                int num = Integer.parseInt(userId.substring(3));

                if (num >= 2025401 && num <= 2025570) return true;

                if (num >= 2024701 && num <= 2024870) return true;

                if (num >= 2023601 && num <= 2023770) return true;

                if (num >= 2022601 && num <= 2022770) return true;

            } else if (userId.startsWith("UEN")) {

                int num = Integer.parseInt(userId.substring(3));

                if (num >= 2025601 && num <= 2025875) return true;

                if (num >= 2024801 && num <= 2025075) return true;

                if (num >= 2023801 && num <= 2024075) return true;

                if (num >= 2022801 && num <= 2023075) return true;

            } else if (userId.startsWith("UIN")) {

                int num = Integer.parseInt(userId.substring(3));

                if (num >= 2025901 && num <= 2025975) return true;

                if (num >= 2025101 && num <= 2025175) return true;

                if (num >= 2024101 && num <= 2024175) return true;

                if (num >= 2023101 && num <= 2023175) return true;

            } else if (userId.startsWith("UME")) {

                int num = Integer.parseInt(userId.substring(3));

                if (num >= 20251001 && num <= 20251070) return true;

                if (num >= 20244001 && num <= 20244070) return true;

                if (num >= 20237001 && num <= 20237070) return true;

                if (num >= 20230001 && num <= 20230070) return true;

            }

        } catch (NumberFormatException e) {

            return false;

        }

        return false;

    }



    private boolean isValidCollegeEmail(String email) {

        return email.matches("^[a-z]+\\.[a-z]+@cumminscollege\\.in$");

    }



    public User searchUser(String userId) {

        return users.get(userId);

    }



    public void displayAllUsers() {

        if (users.isEmpty()) {

            System.out.println("\nNo users registered yet.");

            return;

        }

        System.out.println("\n--- Registered Users ---");

        for (User u : users.values()) {

            u.displayUserDetails();

            System.out.println("-------------------");

        }

    }


    public void updateUserDetails(String userId) {

        if (!users.containsKey(userId)) {

            System.out.println("User not found!");

            return;

        }

        User user = users.get(userId);

        System.out.println("\n--- Update User Details ---");

        System.out.print("Enter new name (leave blank to keep unchanged): ");

        String newName = sc.nextLine();

        if (!newName.isEmpty()) user.setName(newName);



        System.out.print("Enter new email (leave blank to keep unchanged): ");

        String newEmail = sc.nextLine();

        if (!newEmail.isEmpty() && isValidCollegeEmail(newEmail)) user.setEmail(newEmail);



        System.out.print("Enter new phone (leave blank to keep unchanged): ");

        String newPhone = sc.nextLine();

        if (!newPhone.isEmpty()) user.setPhone(newPhone);



        saveToFile();

        System.out.println("✅ User details updated successfully!\n");

    }



    // -----------------------------

    // Save & Load (serialization)

    // -----------------------------

    public void saveToFile() {

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(users);

            //System.out.println("Users saved.");

        } catch (IOException e) {

            System.out.println("Error saving users: " + e.getMessage());

        }

    }



    @SuppressWarnings("unchecked")

    public void loadFromFile() {

        File f = new File(FILE_NAME);

        if (!f.exists()) {

            //System.out.println("(No previous user data found)");

            return;

        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            Object data = ois.readObject();

            if (data instanceof HashMap) {

                users = (HashMap<String, User>) data;

            } else if (data instanceof ArrayList) {

                ArrayList<User> list = (ArrayList<User>) data;

                users = new HashMap<>();

                for (User u : list) users.put(u.getUserId(), u);

                System.out.println("⚙️ Converted old user ArrayList to HashMap.");

            }

            //System.out.println("Users loaded.");

        } catch (IOException | ClassNotFoundException e) {

            System.out.println("Error loading users: " + e.getMessage());

        }

    }



    // for reports

    public HashMap<String, User> getUsersMap() { return users; }

}

