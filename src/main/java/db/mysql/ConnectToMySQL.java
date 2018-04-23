package db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectToMySQL implements ConnectToDBBehavior {

    private final String  DB_URL = "jdbc:mysql://localhost:3306/";

    /**
     * Method used for connecting to mysql db
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

            conn = DriverManager.getConnection(DB_URL, connectionProps);
            System.out.println("You have connected to mysql db.");

        } catch (SQLException e) {
            System.out.println("Issue with connecting to mysql db.");
            e.printStackTrace();
        }

        return conn;

    }

}
