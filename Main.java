import java.sql.*;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- Habit Tracker ---");
            System.out.println("1. Add Habit");
            System.out.println("2. View Habits");
            System.out.println("3. Add Record");
            System.out.println("4. View Records + Suggestion");
            System.out.println("5. Exit");

                        System.out.println("\n Enter Your Choice :");
            int choice;
                       try {
                        choice = Integer.parseInt(sc.next());
                       } catch (Exception e) {
                           System.out.println("Invalid choice! Enter number.");
                         continue;
                       }

            switch (choice) {
                case 1: addHabit(); break;
                case 2: viewHabits(); break;
                case 3: addRecord(); break;
                case 4: viewRecords(); break;
                case 5: System.exit(0);
                default: System.out.println("Invalid choice");
            }
        }
    }
          // Add Habit
          static void addHabit() {
    try {
        Connection con = DBConnection.getConnection();

        // Get next ID
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT MAX(id) FROM habits");

        int nextId = 1;
        if (rs.next() && rs.getInt(1) != 0) {
            nextId = rs.getInt(1) + 1;
        }

        System.out.println("Next Habit ID will be: " + nextId);

        System.out.print("Enter habit name: ");
        sc.nextLine();
        String name = sc.nextLine();

        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO habits(name) VALUES(?)"
        );
        ps.setString(1, name);
        ps.executeUpdate();

        System.out.println("Habit added successfully!");

    } catch (Exception e) {
        System.out.println(e);
    }
}
            // View Habits
    static void viewHabits() {
        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM habits");

            while (rs.next()) {
                System.out.println(rs.getInt("id") + " - " + rs.getString("name"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

           // Add Record
   static void addRecord() {
    try {
        Connection con = DBConnection.getConnection();

        // Show available habits first
        System.out.println("\nAvailable Habits:");
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM habits");

        while (rs.next()) {
            System.out.println(rs.getInt("id") + " - " + rs.getString("name"));
        }

        System.out.print("\nEnter habit ID: ");
        int habitId = sc.nextInt();

        System.out.print("Enter hours: ");
        int hours;
                try {
                      hours = Integer.parseInt(sc.next());
                } catch (Exception e) {
                      System.out.println("Invalid input! Enter numbers only.");
                 return;
               }

        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO records(habit_id, hours, date) VALUES (?, ?, CURDATE())"
        );
        ps.setInt(1, habitId);
        ps.setInt(2, hours);
        ps.executeUpdate();

        System.out.println("Record added successfully!");

    } catch (Exception e) {
        System.out.println(e);
    }
}
          // View Records + Suggestion
    static void viewRecords() {
        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(
                "SELECT r.id, h.name, r.hours, r.date FROM records r JOIN habits h ON r.habit_id = h.id"
            );

            int totalHours = 0;
            int count = 0;

            while (rs.next()) {
                int hours = rs.getInt("hours");
                totalHours += hours;
                count++;

                System.out.println(
                    rs.getInt("id") + " | " +
                    rs.getString("name") + " | " +
                    hours + " hrs | " +
                    rs.getDate("date")
                );
            }

            // Call suggestion AFTER displaying data
            generateSuggestion(totalHours, count);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

           // AI-style suggestion logic
    static void generateSuggestion(int totalHours, int count) {
        if (count == 0) {
            System.out.println("No data available yet.");
            return;
        }

        double avg = (double) totalHours / count;

        System.out.println("\n--- Suggestion ---");

        if (avg < 2) {
            System.out.println("You are spending less time. Try increasing focus gradually.");
        } else if (avg < 5) {
            System.out.println("Good consistency. Try pushing a bit more for improvement.");
        } else {
            System.out.println("Excellent productivity! Keep it up!");
        }
        System.out.println("Project By Team TJ");
    }
}