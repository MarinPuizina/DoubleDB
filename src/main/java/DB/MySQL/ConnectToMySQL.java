package DB.MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectToMySQL implements ConnectToDBBehavior {

    /**
     * Method used for connecting to MySQL DB
     *
     * @param dbUserName is user name used for login into your db
     * @param dbPassword is password used for login into your db
     */
    public Connection connectToDB(String dbUserName, String dbPassword) {

        Connection conn = null;

        Properties connectionProps = new Properties();
        connectionProps.put("user", dbUserName);
        connectionProps.put("password", dbPassword);

        try {

            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", connectionProps);
            System.out.println("You have connected to MySQL DB.");

        } catch (SQLException e) {
            System.out.println("Issue with connecting to MySQL DB.");
            e.printStackTrace();
        }

        return conn;

    }

}
