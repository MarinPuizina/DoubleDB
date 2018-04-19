package DB.MySQL;

import java.sql.Connection;

public interface DisconnectFromDBBehavior {

    void disconnectFromDB(Connection liveConnection);

}
