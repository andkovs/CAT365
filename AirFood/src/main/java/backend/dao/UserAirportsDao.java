package backend.dao;

import backend.model.Airport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserAirportsDao {

    public ArrayList<Airport> getAirportsByUserId(int id) {
        ArrayList<Airport> airports = new ArrayList<Airport>();
        ArrayList<Integer> airportIds = new ArrayList<Integer>();
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        try {
            String sql = "SELECT user_airports.airport_id FROM user_airports WHERE user_airports.user_id = ?";
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setInt(1, id);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                airportIds.add(
                        rs.getInt("airport_id")
                );
            }
            AirportDao dao = new AirportDao();
            for (Integer i :
                    airportIds) {
                airports.add(dao.getAirportById(i));
            }
            rs.close();
            psql.close();
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return airports;
    }

    public String setUserAirportsInDb(int userId, Long airportId) {
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        try {
            String sql = "INSERT INTO user_airports " +
                    "(user_airports.user_id, user_airports.airport_id) " +
                    "VALUES (?, ?)";
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setInt(1, userId);
            psql.setLong(2, airportId);
            psql.executeUpdate();
            psql.close();
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return "ok";
    }

    public String deleteAirportIdsFromDbByUserId(int id) {
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        try {
            String sql = "delete from user_airports where user_airports.user_id = ?;";
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setInt(1, id);
            psql.executeUpdate();
            psql.close();
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return "ok";
    }

}
