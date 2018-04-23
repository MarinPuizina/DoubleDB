package db.mysql;

import java.sql.Connection;

public interface DisconnectFromDBBehavior {

    void disconnectFromDB(Connection liveConnection);

}
