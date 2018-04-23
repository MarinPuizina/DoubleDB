package db;

import db.mysql.ConnectToDBBehavior;
import db.mysql.DisconnectFromDBBehavior;

import java.sql.Connection;

public class Database {

    private String dbUserName;
    private String dbPassword;

    protected ConnectToDBBehavior connectToDBBehavior;
    protected DisconnectFromDBBehavior disconnectFromDBBehavior;


    public Connection doConnectToDB() {
        return connectToDBBehavior.connectToDB(dbUserName, dbPassword);
    }

    public void doDisconnectFromDB(Connection liveConnection){
        disconnectFromDBBehavior.disconnectFromDB(liveConnection);
    }

//    SETTERS
    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

}
