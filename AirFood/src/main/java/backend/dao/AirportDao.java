package backend.dao;

import backend.model.Airport;
import java.sql.*;
import java.util.ArrayList;

public class AirportDao {

    public String getAirportNameById(int id){
        String name = null;
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if(connection==null){
            return null;
        }
        try {
            String sql = "SELECT airports.name FROM airports WHERE airports.airport_id = ?";
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setInt(1, id);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                name =  rs.getString("name");
            }
            rs.close();
            psql.close();
            connection.close();
        } catch (Exception e) {
            return null;
        }
        return name;
    }

    public Airport getAirportById(int id){
        Airport airport = new Airport();
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if(connection==null){
            return null;
        }
        try {
            String sql = "SELECT * FROM airports WHERE airports.airport_id = ?";
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setInt(1, id);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                airport.setId(rs.getLong("airport_id"));
                airport.setName(rs.getString("name"));
            }
            rs.close();
            psql.close();
            connection.close();
        } catch (Exception e) {
            return null;
        }
        return airport;
    }


    public ArrayList<Airport> getAllAirports()  {
        ArrayList<Airport> airportList = new ArrayList<Airport>();
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if(connection==null){
            return null;
        }
        try {
            String sql = "SELECT * FROM airports WHERE airports.is_deleted = ?";
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setInt(1, 0);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                airportList.add(new Airport(rs.getLong("airport_id"), rs.getString("name")));
            }
            rs.close();
            psql.close();
            connection.close();
        } catch (Exception e) {
            return null;
        }
        return airportList;
    }

    public Airport setAirportInDB(Airport airport) {
        Airport responseAirport = new Airport();
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if(connection==null){
            return null;
        }
        try {
            String checkSql = "SELECT airports.airport_id FROM airports WHERE is_deleted = ? AND name = ?";
            Integer checkId = supportDao.checkDeletedEntity(connection, airport.getName(), "airport_id", checkSql);
            if(checkId!=null){
                String status = updateAirportInDB(checkId, airport);
                String recoverySql = "UPDATE airports " +
                        "SET airports.is_deleted = ? " +
                        "WHERE airports.airport_id = ?";
                supportDao.deleteOrRecoveryEntity(connection, checkId, recoverySql, false);
                if(status!=null){
                    airport.setId((long)checkId);
                    return airport;
                }
                else{
                    return null;
                }
            }
            String sql = "INSERT INTO airports (airports.name) VALUES (?)";
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setString(1, airport.getName());
            psql.executeUpdate();
            sql = "SELECT MAX(airports.airport_id) FROM airports";
            psql = connection.prepareStatement(sql);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                responseAirport.setId(rs.getLong("MAX(airports.airport_id)"));
            }
            responseAirport.setName(airport.getName());
            rs.close();
            psql.close();
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return responseAirport;
    }

    public String updateAirportInDB(int id, Airport airport) {
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if(connection==null){
            return null;
        }
        try {
            String sql = "UPDATE airports " +
                    "SET airports.name = ?"+
                    " WHERE airports.airport_id = ?";
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setString(1, airport.getName());
            psql.setInt(2, id);
            psql.executeUpdate();
            psql.close();
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return "ok";
    }

    public String deleteAirportFromDB(int id) {
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if(connection==null){
            return null;
        }
        try {
            String sql = "UPDATE airports " +
                    "SET airports.is_deleted = ? " +
                    "WHERE airports.airport_id = ?";
            supportDao.deleteOrRecoveryEntity(connection, id, sql, true);
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return "ok";
    }

    public ArrayList<Airport> getAllAirportsByUserId(Integer userId) {
        ArrayList<Airport> airportList = new ArrayList<Airport>();
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if(connection==null){
            return null;
        }
        try {
            String sql = "SELECT a.airport_id, a.name " +
                    "FROM airports a " +
                    "JOIN user_airports u ON a.airport_id = u.airport_id " +
                    "WHERE u.user_id = ?";
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setInt(1, userId);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                airportList.add(new Airport(rs.getLong("airport_id"), rs.getString("name")));
            }
            rs.close();
            psql.close();
            connection.close();
        } catch (Exception e) {
            return null;
        }
        return airportList;
    }
}