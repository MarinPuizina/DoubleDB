import DB.Database;
import DB.MySQL.MySQL;

import java.sql.Connection;

public class Application {

    public static void main(String[] args){

        Database mySQLDB = new MySQL();
        Connection liveConnection;

        mySQLDB.setDbUserName("root");
        mySQLDB.setDbPassword("root");

        liveConnection = mySQLDB.doConnectToDB();

        mySQLDB.doDisconnectFromDB(liveConnection);

    }

}
