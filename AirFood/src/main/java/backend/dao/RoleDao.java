package backend.dao;

import java.sql.*;
import java.util.ArrayList;

public class RoleDao {

    public ArrayList<String> getAllRoles() {
        ArrayList<String> roles = new ArrayList<String>();
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        try {
            String sql = "SELECT * FROM roles";
            PreparedStatement psql = connection.prepareStatement(sql);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                roles.add(rs.getString("role_name"));
            }
            rs.close();
            psql.close();
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return roles;
    }

    public ArrayList<String> getRolesByLogin(String login){
        Connection connection = ConnectionToMySQLDB.getConnaction();
        ArrayList<String> roles = new ArrayList<String>();
        try {
            String sql = "SELECT role_name FROM user_roles " +
                    "WHERE user_roles.login = ?";
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setString(1, login);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                roles.add(rs.getString("role_name"));
            }
            rs.close();
            psql.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    public ArrayList<String> getRolesByToken(String token) {
        String login = new UserDao().getLoginByTokenFromDB(token);
        ArrayList<String> roles = getRolesByLogin(login);
        return roles;
    }

    public String setUserToRoleInDb(String title, String login) {
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        try {
            String sql = "INSERT INTO user_roles " +
                    "(user_roles.login, user_roles.role_name) " +
                    "VALUES (?, ?)";
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setString(1, login);
            psql.setString(2, title);
            psql.executeUpdate();
            psql.close();
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return "ok";
    }

    public String deleteRoleFromDbByLogin(String login) {
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        try {
            String sql = "delete from user_roles where user_roles.login = ?;";
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setString(1, login);
            psql.executeUpdate();
            psql.close();
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return "ok";
    }
}
