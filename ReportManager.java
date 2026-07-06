package LostAndFound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReportManager {

    private ArrayList<LostItem> lostItems;

    private ArrayList<FoundItems> foundItems;

    private HashMap<String, User> users;



    public ReportManager(ArrayList<LostItem> lostItems, ArrayList<FoundItems> foundItems, HashMap<String, User> users) {

        this.lostItems = lostItems;

        this.foundItems = foundItems;

        this.users = users;

    }



    public void generateStatistics() {

        int lostCount = lostItems.size();

        int foundCount = foundItems.size();

        HashMap<String, Integer> categoryCount = new HashMap<>();



        for (LostItem li : lostItems) categoryCount.put(li.getCategory(), categoryCount.getOrDefault(li.getCategory(), 0) + 1);

        for (FoundItems fi : foundItems) categoryCount.put(fi.getCategory(), categoryCount.getOrDefault(fi.getCategory(), 0) + 1);



        String popular = "None";

        int max = 0;

        for (Map.Entry<String, Integer> e : categoryCount.entrySet()) {

            if (e.getValue() > max) { max = e.getValue(); popular = e.getKey(); }

        }



        System.out.println("\n--- System Statistics ---");

        System.out.println("Total Registered Users: " + users.size());

        System.out.println("Total Lost Items: " + lostCount);

        System.out.println("Total Found Items: " + foundCount);

        System.out.println("Most Popular Category: " + popular);

        System.out.println("--------------------------\n");

    }

}
