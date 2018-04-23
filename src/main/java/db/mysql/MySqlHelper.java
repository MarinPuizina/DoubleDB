package db.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlHelper {

    private static final String DB_TABLE = "doubledb.userdata";
    private static final String QUERY_FIND_USER_NAME = "SELECT * FROM " + DB_TABLE + " WHERE userName = ? COLLATE UTF8_GENERAL_CI";
    private static final String QUERY_INSERT_USER_DATA = "INSERT INTO " + DB_TABLE + " (userName, userPassword) VALUES (?,?)";


    public static boolean findUserByName(Connection liveConnection, String userName) {

        PreparedStatement findUserName = null;
        ResultSet resultSet;
        boolean check = false;

        try {

            liveConnection.setAutoCommit(false);

            findUserName = liveConnection.prepareStatement(QUERY_FIND_USER_NAME);
            findUserName.setString(1, userName);

            resultSet = findUserName.executeQuery();

            if (resultSet.next()) {
                check = true;
            }

            liveConnection.commit();

        } catch (SQLException e) {
            System.out.println("Prepare statement execution failed!");
            e.printStackTrace();

            if (liveConnection != null) {

                System.out.println("Connection is still alive, trying to rolled back transaction.");

                try {
                    liveConnection.rollback();
                } catch (SQLException e1) {
                    System.out.println("Transaction roll back failed!");
                    e1.printStackTrace();
                }

            }

        } finally {

            // No need to close db connection here,
            // we are doing it later with disconnect method

            try {

                if (findUserName != null) {
                    findUserName.close();
                }

            } catch (SQLException e) {
                System.out.println("Failed to close prepare statement and/or connection!");
                e.printStackTrace();
            }

        }

        return check;

    }

    public static void insertUserData(Connection liveConnection, String userName, String userPassword) {

        PreparedStatement insertUserData = null;

        try {

            liveConnection.setAutoCommit(false);

            insertUserData = liveConnection.prepareStatement(QUERY_INSERT_USER_DATA);
            insertUserData.setString(1, userName);
            insertUserData.setString(2, userPassword);

            insertUserData.execute();

            liveConnection.commit();

        } catch (SQLException e) {
            System.out.println("Prepare statement execution failed!");
            e.printStackTrace();

            if (liveConnection != null) {

                System.out.println("Connection is still alive, trying to rolled back transaction.");

                try {
                    liveConnection.rollback();
                } catch (SQLException e1) {
                    System.out.println("Transaction roll back failed!");
                    e1.printStackTrace();
                }

            }

        } finally {

            // No need to close db connection here,
            // we are doing it later with disconnect method

            try {

                if (insertUserData != null) {
                    insertUserData.close();
                }

            } catch (SQLException e) {
                System.out.println("Failed to close prepare statement and/or connection!");
                e.printStackTrace();
            }

        }



    }

}
