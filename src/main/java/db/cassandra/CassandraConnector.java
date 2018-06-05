package db.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;

public class CassandraConnector {

    private Cluster cluster;
    private Session session;

    public void connect(String node, Integer port) {

        // Contact points are addresses of Cassandra nodes that the driver uses to discover the cluster topology.
        Cluster.Builder b = Cluster.builder().addContactPoint(node);

        if (port != null) {
            b.withPort(port);
        }

        cluster = b.build();

        // Metadata -> Keeps metadata on the connected cluster, including known nodes and schema definitions.
        // cluster.getMetadata() -> Returns read-only metadata on the connected cluster.
        final Metadata metadata = cluster.getMetadata();
        System.out.printf("Connected to cluster: %s \n", metadata.getClusterName());

        for (final Host host : metadata.getAllHosts()) {
            System.out.printf("Datacenter: %s; Host: %s; Rack: %s \n",
                    host.getDatacenter(), host.getAddress(), host.getRack());
        }

        session = cluster.connect();

    }


    public Session getSession() {
        return session;
    }


    public void close() {
        session.close();
        cluster.close();
    }


}
