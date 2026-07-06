package LostAndFound.src.LostAndFound;

import java.util.*;

//A match class to keep tract of the lost and found items matches
class Match {
    LostItem lost;
    FoundItems found;
    double score;

    Match(LostItem l, FoundItems f, double s) {
        lost = l;
        found = f;
        score = s;
    }
}


public class MatchManager {

    private ArrayList<LostItem> lostItemsList;
    private ArrayList<FoundItems> foundItemsList;
    private HashMap<String, ArrayList<String>> matchedPairs;
    private List<Match> allMatches;
    List<Match> topMatches;

    public MatchManager(ArrayList<LostItem> lostItemsList, ArrayList<FoundItems> foundItemsList, HashMap<String, User> usersMap) {
        this.lostItemsList = lostItemsList;
        this.foundItemsList = foundItemsList;
        this.matchedPairs = new HashMap<>();
        this.allMatches = new ArrayList<>();
        this.topMatches=new ArrayList<>();

    }

    // Helper: Normalize text (case-insensitive, punctuation removed)
    private String normalize(String text) {
        return text == null ? "" +
                "" : text.toLowerCase().replaceAll("[^a-z0-9 ]", " ").trim();
    }

    // Helper: Compute similarity score between two strings
    private double computeSimilarity(String s1, String s2) {
        s1 = normalize(s1);
        s2 = normalize(s2);

        if (s1.isEmpty() || s2.isEmpty()) return 0;

        Set<String> set1 = new HashSet<>(Arrays.asList(s1.split("\\s+")));
        Set<String> set2 = new HashSet<>(Arrays.asList(s2.split("\\s+")));

        //finding common words amongst the two sets
        int common = 0;
        for (String w : set1){
            if (set2.contains(w)){
                common++;
            }
        }

        double score = (2.0 * common) / (set1.size() + set2.size());
        return score;  // between 0 and 1
    }


    public void generateMatches() {
        matchedPairs.clear();
        allMatches.clear();
        //creating an ArrayList of Match class to store all matches
        for (LostItem lost : lostItemsList) {
            for (FoundItems found : foundItemsList) {

                // Compute similarity across name + description
                double nameSim = computeSimilarity(lost.getItemName(), found.getItemName());
                double descSim = computeSimilarity(lost.getDescription(), found.getDescription());

                // Strong location filter
                double locationSim = computeSimilarity(lost.getLocation(), found.getLocation());
                double categorySim = computeSimilarity(lost.getCategory(),found.getCategory());

                // Weighted score (you can tweak weights)
                double finalScore = (0.4 * nameSim) + (0.25 * descSim) + (0.20* categorySim)+(0.15*locationSim);

                if (finalScore >= 0.44) {  // minimum quality threshold
                    allMatches.add(new Match(lost, found, finalScore));
                }
            }
        }

        // Sort by score (descending)
        allMatches.sort((a, b) -> Double.compare(b.score, a.score));

        // Only keep top 3 matches
        topMatches = allMatches.stream().limit(Math.min(3,allMatches.size())).toList();

        System.out.println("\n✅ --- Top Matches ---");
        System.out.println("--------------------------------------");
        for (Match m : topMatches) {
            System.out.println("Lost Item Name : " + m.lost.getItemName());
            System.out.println("Found Item Name: " + m.found.getItemName());
            System.out.println("Location       : " + m.lost.getLocation());
            System.out.println("Match Score    : " + String.format("%.2f", m.score));
            System.out.println("Category       : " + m.lost.getCategory());
            System.out.println("Lost by UserID : " + m.lost.getUserId());
            System.out.println("Found by UserID: " + m.found.getUserId());
            System.out.println("--------------------------------------");
        }

        if (topMatches.isEmpty()) {
            System.out.println("No strong matches found!");
        }
    }
}


