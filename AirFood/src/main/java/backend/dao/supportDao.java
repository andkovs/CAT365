package backend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class supportDao {

    public static void deleteOrRecoveryEntity(Connection connection, int id, String sql, boolean delete) throws SQLException {
        PreparedStatement psql = connection.prepareStatement(sql);
        if(delete) {
            psql.setInt(1, 1);
        }else{
            psql.setInt(1, 0);
        }
        psql.setInt(2, id);
        psql.executeUpdate();
        psql.close();
    }

    public static Integer checkDeletedEntity(Connection connection, String name, String column, String sql) throws SQLException {
        Integer id = null;
        PreparedStatement psql = connection.prepareStatement(sql);
        psql.setInt(1, 1);
        psql.setString(2, name);
        ResultSet rs = psql.executeQuery();
        while (rs.next()) {
            id =  rs.getInt(column);
        }
        return id;
    }
}
