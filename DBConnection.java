import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {
        Connection con = null;

        try {
            
            String url = "jdbc:mysql://localhost:3306/habit_tracker";

            String user = "root";

            String password = "root";

            con = DriverManager.getConnection(url, user, password);

            System.out.println("Connected to MySQL!");

        } catch (Exception e) {
            System.out.println("Connection Error: " + e.getMessage());
        }

        return con;
    }
}