import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    // JDBC driver name and database URL


    public Connection getConection() {
        Connection conn = null;

        try {
            //STEP 2: Register JDBC driver
            String url = "jdbc:sqlite:/Users/cameronpugh/SFile.db";

            // create a connection to the database
            conn = DriverManager.getConnection(url);
            System.out.println("Connected database successfully...");

            return conn;

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }

        return conn;

    }
}
