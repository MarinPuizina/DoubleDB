package DB.MySQL;

import DB.Database;

public class MySQL extends Database {

    public MySQL() {

        connectToDBBehavior = new ConnectToMySQL();
        disconnectFromDBBehavior = new DisconnectFromMySQL();

    }

}
