package db.mysql;

import java.sql.Connection;

public interface ConnectToDBBehavior {

    Connection connectToDB(String dbuserName, String dbPassword);

}
