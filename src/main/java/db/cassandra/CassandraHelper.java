package db.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class CassandraHelper {

    /**
     * If you are coming from a relational database, then the schema is the respective keyspace in Cassandra.
     * The keyspace is the outermost container for data in Cassandra.
     * The main attributes to set per keyspace are the Replication Factor,
     * the Replica Placement Strategy and the Column Families
     */
    public static void createKeySpace(Session session, String keySpaceName) {

        String query = "CREATE KEYSPACE IF NOT EXISTS " + keySpaceName +
                " WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 3};";

        session.execute(query);

    }


    public static void deleteKeySpace(Session session, String keySpaceName) {

        String query = "DROP KEYSPACE " + keySpaceName;

        session.execute(query);

    }


    public static void createTestTable(Session session) {

        StringBuilder createTable = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append("new_keyspace.tableprime").append("(")
                .append("id uuid PRIMARY KEY,")
                .append("Name text,")
                .append("Occupation text);");

        String query = createTable.toString();

        session.execute(query);

    }


    public static void queryTableByName(Session session, String name) {

        StringBuilder queryByName = new StringBuilder("SELECT * ")
                .append("FROM new_keyspace.tableprime ")
                .append("WHERE Name = ? ALLOW FILTERING;");

        String query = queryByName.toString();

        ResultSet resultSet = session.execute(query, name);
        Row resultRow = resultSet.one();


        System.out.printf("id: %s ; Name: %s ; Occupation: %s",
                resultRow.getUUID("id"),
                resultRow.getString("Name"),
                resultRow.getString("Occupation"));


    }


    public static void insertDataIntoTable(Session session) {

        StringBuilder dataToInsert = new StringBuilder("INSERT INTO ")
                .append("new_keyspace.tableprime ")
                .append("(id, Name, Occupation) ")
                .append("VALUES (123e4567-e89b-12d3-a456-426655440000, 'SlaveZero', 'Gamer');");

        String query = dataToInsert.toString();

        session.execute(query);

    }


}
