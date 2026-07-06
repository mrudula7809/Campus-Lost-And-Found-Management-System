package LostAndFound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        //Managers
        UserManagement userMgr = new UserManagement(sc);
        LostItemsManager lostMgr = new LostItemsManager();
        FoundItemsManager foundMgr = new FoundItemsManager();
        MatchManager matchMgr = new MatchManager(lostMgr.getLostItemsList(), foundMgr.getFoundItemsList(), userMgr.getUsersMap());
        PriorityTracker tracker = new PriorityTracker();
        LostItem lItem=new LostItem();

        System.out.println("\n===== !!!WELCOME TO LOST & FOUND MANAGEMENT SYSTEM!!! =====");

        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Register if you are a new Student");
            System.out.println("2. Login if you have already registered");
            System.out.println("3. Exit");
            System.out.print("Choose one of the options above options : ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> userMgr.addUser();

                case "2" -> {
                    User logged = userMgr.authenticateUser();
                    if (logged == null) continue;

                    if (logged.getRole() == 2)
                        adminMenu(sc, userMgr, lostMgr, foundMgr, matchMgr,tracker);
                    else
                        studentMenu(sc, userMgr, lostMgr,lItem, foundMgr, matchMgr, logged);
                }


                case "3" -> {
                    System.out.println("Exiting... (All data saved)");
                    sc.close();
                    System.exit(0);
                }

                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // ================== ADMIN MENU ==================
    private static void adminMenu(Scanner sc, UserManagement userMgr, LostItemsManager lostMgr,
                                  FoundItemsManager foundMgr, MatchManager matchMgr,PriorityTracker tracker) {

        while (true) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. View All Users");
            System.out.println("2. View All Lost Items");
            System.out.println("3. View All Lost Items by Priority");
            System.out.println("4. View All Found Items");
            System.out.println("5. Add Found Item");
            System.out.println("6. Search Lost Item by ID");
            System.out.println("7. Search Found Items (keyword)");
            System.out.println("8. Generate & View All Matches");
            System.out.println("9. Undo Last Found Item Added");
            System.out.println("10.Redo Last Found Item Added");
            System.out.println("11.View Entire Report");
            System.out.println("12.Back to Main Menu");
            System.out.print("Choose one of the above options : ");
            String ch = sc.nextLine().trim();

            switch (ch) {
                case "1" -> userMgr.displayAllUsers();
                case "2" -> lostMgr.displayLostItems(null);

                case "3" -> {
                    ArrayList<LostItem> lostItems = lostMgr.getLostItemsList();
                    lostItems.sort(tracker.getLostItemPriorityComparator());int i=0;
                    System.out.println("\n--- Lost Items Sorted by Priority ---");
                    for (LostItem item : lostItems) {
                        System.out.println();
                        System.out.println( (i+1)+"."+"Item Name - "+item.getItemName() + "\nCategory - " +
                                item.getCategory()+"\nItem ID - "+item.getItemId()+"\nReported by - "+item.getUserId());
                        System.out.println("---------------------------");
                    }
                }
                case "4" -> foundMgr.displayAllFound();
                case "5" -> {
                    System.out.print("Enter admin ID for record: ");
                    String admId = sc.nextLine().trim();
                    foundMgr.addFoundItems(admId, 2,sc);
                }
                case "6" -> {
                    System.out.print("Enter Lost Item ID: ");
                    lostMgr.searchById(sc.nextLine().trim());
                }
                case "7" -> {
                    System.out.print("Enter keyword to search found items: ");
                    foundMgr.searchFoundItems(sc.nextLine().trim(),null);
                }
                case "8" -> {
                    System.out.println("\n Generating possible matches...");
                    matchMgr.generateMatches();
                }

                case "9" ->{
                    foundMgr.undoItem();
                }
                case "10" ->{
                    foundMgr.redoItem();
                }

                case "11" -> {
                    ArrayList<LostItem> lostList = lostMgr.getLostItemsList();
                    ArrayList<FoundItems> foundList = foundMgr.getFoundItemsList();
                    HashMap<String, User> usersMap = userMgr.getUsersMap();

                    ReportManager rpt = new ReportManager(lostList, foundList, usersMap);
                    rpt.generateStatistics();
              }
                case "12" -> { return; }

                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // ================== STUDENT MENU ==================
    private static void studentMenu(Scanner sc, UserManagement userMgr, LostItemsManager lostMgr,LostItem lItem,
                                    FoundItemsManager foundMgr, MatchManager matchMgr, User logged) {

        while (true) {
            System.out.println("\n--- STUDENT MENU (" + logged.getName() + ") ---");
            System.out.println("1. Report Lost Item");
            System.out.println("2. View My Lost Items");
            System.out.println("3. View Found Items reported by me");
            System.out.println("4. Search My Lost Items (keyword)");
            System.out.println("5. Generate & View My Matches");
            System.out.println("6. Undo Last Lost Item Added");
            System.out.println("7. Redo Lost Item Added");
            System.out.println("8. Back to Main Menu");
            System.out.print("Choose one of the above options: ");
            String ch = sc.nextLine().trim();

            switch (ch) {
                case "1" -> lostMgr.addLostItem(logged.getUserId(), sc);
                case "2" -> lostMgr.displayLostItems(logged.getUserId());
                case "3" -> logged.viewMyFoundItems();
                case "4" -> {
                    System.out.print("Enter keyword: ");
                    lostMgr.searchItem(sc.nextLine().trim(), logged.getUserId());
                }
                case "5" -> {
                    System.out.println("\n Generating matches for your items...");
                    matchMgr.generateMatches();
                }
                case "6" -> lostMgr.undoItem();
                case "7" -> lostMgr.redoItem();
                case "8" -> { return; }
                default -> System.out.println(" Invalid choice.");
            }
        }
    }
}
