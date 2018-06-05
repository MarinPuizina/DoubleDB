import com.datastax.driver.core.Session;
import db.Database;
import db.cassandra.CassandraConnector;
import db.cassandra.CassandraHelper;
import db.mysql.MySQL;
import user.UserHelper;

import java.sql.Connection;

public class Application {

    private static final String DB_NAME = "root";
    private static final String DB_PASSWORD = "root";

    private static final String CASS_IP_ADDRESS = "127.0.0.1";
    private static final Integer CASS_PORT = 9042;

    public static void main(String[] args){

        UserHelper userHelper = new UserHelper();
        Database mySQLDB = new MySQL();

        Connection liveConnection;

        mySQLDB.setDbUserName(DB_NAME);
        mySQLDB.setDbPassword(DB_PASSWORD);

        //liveConnection = mySQLDB.doConnectToDB();

        //userHelper.userMenu(liveConnection);

        //mySQLDB.doDisconnectFromDB(liveConnection);


        final CassandraConnector client = new CassandraConnector();
        System.out.println("Connection to IP ADDRESS -> " + CASS_IP_ADDRESS + ":" + CASS_PORT);
        client.connect(CASS_IP_ADDRESS, CASS_PORT);
        Session session = client.getSession();

        CassandraHelper.createKeySpace(session, "new_keyspace");
        CassandraHelper.createTestTable(session);
        CassandraHelper.insertDataIntoTable(session);
        CassandraHelper.queryTableByName(session, "SlaveZero");
        client.close();

    }

}
