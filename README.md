Lost & Found Management System

A console-based Java application for managing lost and found item reports on a college campus. Students can report lost items and browse found items; admins can log found items, review system-wide reports, and generate automatic lost-to-found matches using a text-similarity scoring algorithm.

Built as a data structures & OOP-focused project — the emphasis is on clean use of core Java collections (HashMap, HashSet, Stack, ArrayList) to solve real lookup, deduplication, and undo/redo problems, rather than on frameworks or UI.


Features


Role-based access — separate menus and permissions for Students and Admins
Lost item reporting — students log lost items with category, location, and date
Found item logging — admins log items found on campus
Automatic matching — a weighted similarity score (name, description, category, location) surfaces likely lost↔found pairs
Priority sorting — lost items ranked by category urgency (e.g. Medical > Electronics > Clothing)
Undo/Redo — stack-based undo/redo for both lost and found item entries
Duplicate detection — O(1) composite-key lookups prevent the same item being logged twice
Search — keyword search across name, description, location, and category
Admin reporting dashboard — aggregate stats: total users, total items, most-reported category
Persistence — all data is serialized to disk (.dat files) and reloaded on startup, so state survives restarts



Tech Stack


Language: Java 17+ (uses switch expressions with arrow syntax)
Persistence: Java Serialization (ObjectOutputStream / ObjectInputStream)
No external dependencies — pure JDK, no build tool required



Project Structure

LostAndFound/
├── Item.java               # Abstract base class shared by LostItem & FoundItems
├── LostItem.java           # Lost item entity (extends Item)
├── FoundItems.java         # Found item entity (extends Item)
├── LostItemsManager.java   # CRUD, search, undo/redo, and persistence for lost items
├── FoundItemsManager.java  # CRUD, search, undo/redo, and persistence for found items
├── User.java               # User entity (student/admin)
├── UserManagement.java     # Registration, authentication, persistence
├── MatchManager.java       # Similarity-based lost↔found matching engine
├── PriorityTracker.java    # Category-based priority comparator for lost items
├── ReportManager.java      # Aggregate statistics for the admin dashboard
└── Main.java               # Entry point; console menu routing

Design notes


LostItem and FoundItems extend a common abstract Item class, which owns shared fields (name, description, location, category, UUID generation) and input/validation logic — avoiding duplicated logic across the two entity types.
Each manager maintains a HashSet<String> of composite keys (name + description + location + category, normalized) for O(1) duplicate checking, rather than scanning the full list on every insert.
Undo/redo is implemented with a pair of Stack<Item> per manager — a push on add, pop-and-push-to-redo on undo.
Matching uses Jaccard-style word-overlap similarity across name, description, category, and location, combined into a single weighted score (weights sum to 1.0), with only matches above a quality threshold surfaced.



Getting Started

Prerequisites


JDK 17 or later
No external libraries or build tool needed


Compile & Run

bash# From the project root
javac -d out src/LostAndFound/*.java
java -cp out LostAndFound.Main

On first run, the app will create three data files in the working directory to persist state between sessions:


users_data.dat
lost_items_data.dat
found_items_data.dat



Usage

On launch you'll see:

===== !!!WELCOME TO LOST & FOUND MANAGEMENT SYSTEM!!! =====

Main Menu:
1. Register if you are a new Student
2. Login if you have already registered
3. Exit

Students can report lost items, view/search their own reports, and generate matches against found items.

Admins (password-protected) can log found items, view all lost/found items (optionally sorted by priority), run the matching engine across all items, and view aggregate system statistics.


Student registration validates the User ID against fixed department/year ranges and requires a @cumminscollege.in email — adjust UserManagement.isValidUserId() / isValidCollegeEmail() if adapting this for another institution.
