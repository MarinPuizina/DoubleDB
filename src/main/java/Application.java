import db.Database;
import db.mysql.MySQL;
import user.UserHelper;

import java.sql.Connection;

public class Application {

    private static final String DB_NAME = "root";
    private static final String DB_PASSWORD = "root";


    public static void main(String[] args){

        UserHelper userHelper = new UserHelper();
        Database mySQLDB = new MySQL();

        Connection liveConnection;

        mySQLDB.setDbUserName(DB_NAME);
        mySQLDB.setDbPassword(DB_PASSWORD);

        liveConnection = mySQLDB.doConnectToDB();

        userHelper.userMenu(liveConnection);

        mySQLDB.doDisconnectFromDB(liveConnection);

    }

}
