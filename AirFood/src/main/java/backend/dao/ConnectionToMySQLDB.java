package backend.dao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionToMySQLDB {

    public static Connection getConnaction() {
        Context initCtx;
        Connection connection;
        try {
            initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            // Look up our data source
            DataSource ds = (DataSource) envCtx.lookup("jdbc/airfoodDB");
            // Allocate and use a connection from the pool
            connection = ds.getConnection();
        } catch (NamingException e) {
            return null;
        } catch (SQLException e) {
            return null;
        }
        return connection;
    }
}
