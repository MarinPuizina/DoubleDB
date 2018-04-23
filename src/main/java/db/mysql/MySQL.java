package db.mysql;

import db.Database;

public class MySQL extends Database {

    public MySQL() {

        connectToDBBehavior = new ConnectToMySQL();
        disconnectFromDBBehavior = new DisconnectFromMySQL();

    }

}
