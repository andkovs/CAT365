package backend.dao;

import backend.model.Airport;
import backend.model.User;

import java.sql.*;
import java.util.ArrayList;

public class UserDao {

    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<User>();
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        try {
            String sql = "SELECT * FROM users";
            PreparedStatement psql = connection.prepareStatement(sql);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                Boolean isEnabled = true;
                if (rs.getInt("is_enabled") == 0) {
                    isEnabled = false;
                }
                users.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("middle_name"),
                        rs.getString("email"),
                        isEnabled)
                );
            }
            rs.close();
            psql.close();
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return users;
    }

    public User getUserByIdFromDb(int id) {
        if (id == 0) {
            return new User(0, "", "", "", "", "");
        }
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        User user = new User();
        String sql = "SELECT * FROM users " +
                "WHERE users.user_id = ?";
        try {
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setInt(1, id);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                user.setLogin(rs.getString("login"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setMiddleName(rs.getString("middle_name"));
                user.setEmail(rs.getString("email"));
                if (rs.getInt("is_enabled") == 1) {
                    user.setEnabled(true);
                    user.setPassword(rs.getString("password"));
                } else {
                    user.setEnabled(false);
                    user.setPassword(rs.getString("pass_archive"));
                }
            }
            rs.close();
            psql.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        user.setId(id);
        user.setRoles(new RoleDao().getRolesByLogin(user.getLogin()));
        user.setAirports(new UserAirportsDao().getAirportsByUserId(user.getId()));
        return user;
    }

    public User setUserInDB(User user) {
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        String sql = null;
        if (user.getEnabled()) {
            sql = "INSERT INTO users " +
                    "(users.login, users.password, " +
                    "users.first_name, users.last_name, " +
                    "users.middle_name, " +
                    "users.email) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        } else {
            sql = "INSERT INTO users " +
                    "(users.login, users.pass_archive, " +
                    "users.first_name, users.last_name, " +
                    "users.middle_name, " +
                    "users.email) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        }
        try {
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setString(1, user.getLogin());
            psql.setString(2, user.getPassword());
            psql.setString(3, user.getFirstName());
            psql.setString(4, user.getLastName());
            psql.setString(5, user.getMiddleName());
            psql.setString(6, user.getEmail());
            psql.executeUpdate();
            sql = "SELECT MAX(users.user_id) FROM users";
            psql = connection.prepareStatement(sql);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                user.setId(rs.getInt("MAX(users.user_id)"));
            }
            rs.close();
            psql.close();
            connection.close();
            RoleDao roleDao = new RoleDao();
            UserAirportsDao userAirportsDao = new UserAirportsDao();
            for (String r :
                    user.getRoles()) {
                roleDao.setUserToRoleInDb(r, user.getLogin());
            }
            for (Airport a :
                    user.getAirports()) {
                userAirportsDao.setUserAirportsInDb(user.getId(), a.getId());
            }
            new TokenDAO().updateTokenInDB(user.getLogin(), TokenGenerator.generateToken(user.getLogin()));
        } catch (SQLException e) {
            return null;
        }
        return user;
    }

    public String updateUserInDB(int id, User user) {
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        String sql = null;
        if (user.getEnabled()) {
            sql = "UPDATE users " +
                    "SET users.password = ?, " +
                    "users.first_name = ?, users.last_name = ?, " +
                    "users.middle_name = ?, " +
                    "users.email = ? " +
                    " WHERE users.user_id = ?";
        } else {
            sql = "UPDATE users " +
                    "SET users.pass_archive = ?, " +
                    "users.first_name = ?, users.last_name = ?, " +
                    "users.middle_name = ?, " +
                    "users.email = ? " +
                    " WHERE users.user_id = ?";
        }
        try {

            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setString(1, user.getPassword());
            psql.setString(2, user.getFirstName());
            psql.setString(3, user.getLastName());
            psql.setString(4, user.getMiddleName());
            psql.setString(5, user.getEmail());
            psql.setInt(6, id);
            psql.executeUpdate();
            psql.close();
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        RoleDao roleDao = new RoleDao();
        UserAirportsDao userAirportsDao = new UserAirportsDao();
        roleDao.deleteRoleFromDbByLogin(user.getLogin());
        userAirportsDao.deleteAirportIdsFromDbByUserId(user.getId());
        for (String r :
                user.getRoles()) {
            roleDao.setUserToRoleInDb(r, user.getLogin());
        }
        for (Airport a :
                user.getAirports()) {
            userAirportsDao.setUserAirportsInDb(user.getId(), a.getId());
        }
        return "ok";
    }

    public String deleteUserFromDB(int id) {
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        try {
            RoleDao dao = new RoleDao();
            User user = getUserByIdFromDb(id);
            dao.deleteRoleFromDbByLogin(user.getLogin());
            String sql = "delete from users where users.user_id = ?;";
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setInt(1, id);
            psql.executeUpdate();
            RoleDao roleDao = new RoleDao();
            UserAirportsDao userAirportsDao = new UserAirportsDao();
            roleDao.deleteRoleFromDbByLogin(user.getLogin());
            userAirportsDao.deleteAirportIdsFromDbByUserId(user.getId());
            psql.close();
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return "ok";
    }

    public User getUserByLogin(String login) {
        User user = new User();
        String sql = "SELECT * FROM users " +
                "WHERE users.login = ?";
        try {
            Connection connection = ConnectionToMySQLDB.getConnaction();
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setString(1, login);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                user.setId(rs.getInt("user_id"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
            }
            rs.close();
            psql.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public String enableUserInDB(int id) {
        String sql = "SELECT passArchive FROM users " +
                "WHERE users.user_id = ?";
        String passArchive = null;
        try {
            Connection connection = ConnectionToMySQLDB.getConnaction();
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setInt(1, id);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                passArchive = rs.getString("pass_archive");
            }
            sql = "UPDATE users " +
                    "SET users.password = ?, " +
                    "users.pass_archive = ?, " +
                    "users.is_enabled = ? " +
                    " WHERE users.user_id = ?";
            psql = connection.prepareStatement(sql);
            psql.setString(1, passArchive);
            psql.setString(2, "TransCaT3652016");
            psql.setInt(3, 1);
            psql.setInt(4, id);
            psql.executeUpdate();
            rs.close();
            psql.close();
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return "ok";
    }

    public String disableUserInDB(int id) {
        String sql = "SELECT password FROM users " +
                "WHERE users.user_id = ?";
        String password = null;
        try {
            Connection connection = ConnectionToMySQLDB.getConnaction();
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setInt(1, id);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                password = rs.getString("password");
            }
            sql = "UPDATE users " +
                    "SET users.password = ?, " +
                    "users.pass_archive = ?, " +
                    "users.is_enabled = ? " +
                    " WHERE users.user_id = ?";
            psql = connection.prepareStatement(sql);
            psql.setString(1, "TransCaT3652016");
            psql.setString(2, password);
            psql.setInt(3, 0);
            psql.setInt(4, id);
            psql.executeUpdate();
            rs.close();
            psql.close();
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return "ok";
    }

    public String getLoginByTokenFromDB(String token) {
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        User user = new User();
        String sql = "SELECT * FROM users " +
                "WHERE users.token = ?";
        try {
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setString(1, token);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                user.setLogin(rs.getString("login"));
            }
            rs.close();
            psql.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user.getLogin();
    }

    public Integer getUserIdByToken(String token) {
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        Integer id = null;
        String sql = "SELECT * FROM users " +
                "WHERE users.token = ?";
        try {
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setString(1, token);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                id = rs.getInt("user_id");
            }
            rs.close();
            psql.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

}
