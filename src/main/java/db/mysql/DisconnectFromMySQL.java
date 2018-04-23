package db.mysql;

import java.sql.Connection;
import java.sql.SQLException;

public class DisconnectFromMySQL implements DisconnectFromDBBehavior {

    /**
     * Method used for closing connection from mysql db
     *
     * @param liveConnection
     */
    public void disconnectFromDB(Connection liveConnection) {

        try {

            liveConnection.close();
            System.out.println("You have disconnected from mysql db.");

        } catch (SQLException e) {
            System.out.println("Issue with disconnecting from mysql db.");
            e.printStackTrace();
        }

    }

}
