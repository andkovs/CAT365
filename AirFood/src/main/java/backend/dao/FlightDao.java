package backend.dao;

import backend.model.Flight;

import java.sql.*;
import java.util.ArrayList;

public class FlightDao {

    public ArrayList<Flight> getAllFlights() {
        ArrayList<Flight> flightList = new ArrayList<Flight>();
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        try {
            String sql = "SELECT * FROM flight_numbers WHERE flight_numbers.is_deleted = ?";
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setInt(1, 0);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                flightList.add(new Flight(
                        rs.getLong("flight_id"),
                        rs.getLong("airline_id"),
                        rs.getString("flight_number"))
                );
            }
            rs.close();
            psql.close();
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return flightList;
    }

    public Flight getFlightById(Long id) {
        Flight flight = new Flight();
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        try {
            String sql = "SELECT * FROM flight_numbers WHERE flight_numbers.flight_id = ?";
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setLong(1, id);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                flight.setId(rs.getLong("flight_id"));
                flight.setAirlineId(rs.getLong("airline_id"));
                flight.setNumber(rs.getString("flight_number"));
            }
            rs.close();
            psql.close();
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return flight;
    }

    public Flight setFlightInDB(Flight flight) {
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        Flight responseFlight = new Flight();
        try {
            String checkSql = "SELECT flight_numbers.flight_id FROM flight_numbers WHERE flight_numbers.is_deleted = ? AND flight_numbers.flight_number = ?";
            Integer checkId = supportDao.checkDeletedEntity(connection, flight.getNumber(), "flightid", checkSql);
            if (checkId != null) {
                String status = updateFlightInDB(checkId, flight);
                String recoverySql = "UPDATE flight_numbers " +
                        "SET flight_numbers.is_deleted = ? " +
                        "WHERE flight_numbers.flight_id = ?";
                supportDao.deleteOrRecoveryEntity(connection, checkId, recoverySql, false);
                if (status != null) {
                    flight.setId((long) checkId);
                    return flight;
                } else {
                    return null;
                }
            }
            String sql = "INSERT INTO flight_numbers " +
                    "(flight_numbers.airline_id, flight_numbers.flight_number) " +
                    "VALUES (?, ?)";
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setLong(1, flight.getAirlineId());
            psql.setString(2, flight.getNumber());
            psql.executeUpdate();
            sql = "SELECT MAX(flight_numbers.flight_id) FROM flight_numbers";
            psql = connection.prepareStatement(sql);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                responseFlight.setId(rs.getLong("MAX(flight_numbers.flight_id)"));
            }
            responseFlight.setAirlineId(flight.getAirlineId());
            responseFlight.setNumber(flight.getNumber());
            rs.close();
            psql.close();
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return responseFlight;
    }

    public String updateFlightInDB(int id, Flight flight) {
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        try {
            String sql = "UPDATE flight_numbers " +
                    "SET flight_numbers.airline_id = ?, flight_numbers.flight_number = ?" +
                    " WHERE flight_numbers.flight_id = ?";
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setLong(1, flight.getAirlineId());
            psql.setString(2, flight.getNumber());
            psql.setInt(3, id);
            psql.executeUpdate();
            psql.close();
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return "ok";
    }

    public String deleteFlightFromDB(int id) {
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        try {
            String sql = "UPDATE flight_numbers " +
                    "SET flight_numbers.is_deleted = ? " +
                    "WHERE flight_numbers.flight_id = ?";
            supportDao.deleteOrRecoveryEntity(connection, id, sql, true);
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return "ok";
    }
}
