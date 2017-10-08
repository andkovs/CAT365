package backend.dao;

import backend.model.Airline;

import java.sql.*;
import java.util.ArrayList;

public class AirlineDao {

    public ArrayList<Airline> getAllAirlines() {
        ArrayList<Airline> airlineList = new ArrayList<Airline>();
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        try {
            String sql = "SELECT * FROM airlines WHERE airlines.is_deleted = ?";
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setInt(1, 0);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                airlineList.add(new Airline(
                        rs.getLong("airline_id"),
                        rs.getString("name"),
                        rs.getString("full_name"),
                        rs.getString("iata"),
                        rs.getString("phone"),
                        rs.getString("email"))
                );
            }
            rs.close();
            psql.close();
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return airlineList;
    }

    public Airline getAirlineById(Long id) {
        Airline airline = new Airline();
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        try {
            String sql = "SELECT * FROM airlines WHERE airlines.airline_id = ?";
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setLong(1, id);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                airline.setId(rs.getLong("airline_id"));
                airline.setName(rs.getString("name"));
                airline.setFullname(rs.getString("full_name"));
                airline.setIata(rs.getString("iata"));
                airline.setPhone(rs.getString("phone"));
                airline.setEmail(rs.getString("email"));
            }
            rs.close();
            psql.close();
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return airline;
    }

    public Airline setAirlineInDB(Airline airline) {
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        Airline responseAirline = new Airline();
        try {
            String checkSql = "SELECT airlines.airline_id FROM airlines WHERE airlines.is_deleted = ? AND airlines.name = ?";
            Integer checkId = supportDao.checkDeletedEntity(connection, airline.getName(), "airline_id", checkSql);
            if (checkId != null) {
                String status = updateAirlineInDB(checkId, airline);
                String recoverySql = "UPDATE airlines " +
                        "SET airlines.is_deleted = ? " +
                        "WHERE airlines.airline_id = ?";
                supportDao.deleteOrRecoveryEntity(connection, checkId, recoverySql, false);
                if (status != null) {
                    airline.setId((long) checkId);
                    return airline;
                } else {
                    return null;
                }
            }
            String sql = "INSERT INTO airlines " +
                    "(airlines.name, airlines.full_name, airlines.iata, airlines.phone, airlines.email) " +
                    "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setString(1, airline.getName());
            psql.setString(2, airline.getFullname());
            psql.setString(3, airline.getIata());
            psql.setString(4, airline.getPhone());
            psql.setString(5, airline.getEmail());
            psql.executeUpdate();
            sql = "SELECT MAX(airlines.airline_id) FROM airlines";
            psql = connection.prepareStatement(sql);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                responseAirline.setId(rs.getLong("MAX(airlines.airline_id)"));
            }
            responseAirline.setName(airline.getName());
            responseAirline.setFullname(airline.getFullname());
            responseAirline.setIata(airline.getIata());
            responseAirline.setPhone(airline.getPhone());
            responseAirline.setEmail(airline.getEmail());
            rs.close();
            psql.close();
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return responseAirline;
    }

    public String updateAirlineInDB(int id, Airline airline) {
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        try {
            String sql = "UPDATE airlines " +
                    "SET airlines.name = ?, airlines.full_name = ?," +
                    " airlines.iata = ?, airlines.phone = ?, airlines.email = ?" +
                    " WHERE airlines.airline_id = ?";
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setString(1, airline.getName());
            psql.setString(2, airline.getFullname());
            psql.setString(3, airline.getIata());
            psql.setString(4, airline.getPhone());
            psql.setString(5, airline.getEmail());
            psql.setInt(6, id);
            psql.executeUpdate();
            psql.close();
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return "ok";
    }

    public String deleteAirlineFromDB(int id) {
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        try {
            String sqlAirline = "UPDATE airlines " +
                    "SET airlines.is_deleted = ? " +
                    "WHERE airlines.airline_id = ?";
            String sqlFlight = "UPDATE flight_numbers " +
                    "SET flight_numbers.is_deleted = ? " +
                    "WHERE flight_numbers.airline_id = ?";
            String sqlBoard = "UPDATE board_numbers " +
                    "SET board_numbers.is_deleted = ? " +
                    "WHERE board_numbers.airline_id = ?";
            supportDao.deleteOrRecoveryEntity(connection, id, sqlAirline, true);
            supportDao.deleteOrRecoveryEntity(connection, id, sqlFlight, true);
            supportDao.deleteOrRecoveryEntity(connection, id, sqlBoard, true);
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return "ok";
    }

}
