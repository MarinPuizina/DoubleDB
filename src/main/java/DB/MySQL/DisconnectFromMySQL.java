package DB.MySQL;

import java.sql.Connection;
import java.sql.SQLException;

public class DisconnectFromMySQL implements DisconnectFromDBBehavior {

    /**
     * Method used for closing connection from MySQL DB
     *
     * @param liveConnection
     */
    public void disconnectFromDB(Connection liveConnection) {

        try {

            liveConnection.close();
            System.out.println("You have disconnected from MySQL DB.");

        } catch (SQLException e) {
            System.out.println("Issue with disconnecting from MySQL DB.");
            e.printStackTrace();
        }

    }

}
