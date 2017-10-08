package backend.dao;

import backend.model.Board;

import java.sql.*;
import java.util.ArrayList;

public class BoardDao {

    public ArrayList<Board> getAllBoards() {
        ArrayList<Board> boardList = new ArrayList<Board>();
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        try {
            String sql = "SELECT * FROM board_numbers WHERE board_numbers.is_deleted = ?";
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setInt(1, 0);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                boardList.add(new Board(
                        rs.getLong("board_id"),
                        rs.getLong("airline_id"),
                        rs.getString("board_number"),
                        rs.getString("aircraft_type"))
                );
            }
            rs.close();
            psql.close();
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return boardList;
    }

    public Board setBoardInDB(Board board) {
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        Board responseBoard = new Board();
        try {
            String checkSql = "SELECT board_numbers.board_id FROM board_numbers WHERE board_numbers.is_deleted = ? AND board_numbers.board_number = ? AND board_numbers.airline_id = ?";
            Integer checkId = null;
            PreparedStatement psql = connection.prepareStatement(checkSql);
            psql.setInt(1, 1);
            psql.setString(2, board.getNumber());
            psql.setLong(3, board.getAirlineId());
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                checkId = rs.getInt("board_id");
            }
            if (checkId != null) {
                String status = updateBoardInDB(checkId, board);
                String recoverySql = "UPDATE board_numbers " +
                        "SET board_numbers.is_deleted = ? " +
                        "WHERE board_numbers.board_id = ?";
                supportDao.deleteOrRecoveryEntity(connection, checkId, recoverySql, false);
                if (status != null) {
                    board.setId((long) checkId);
                    return board;
                } else {
                    return null;
                }
            }
            String sql = "INSERT INTO board_numbers " +
                    "(board_numbers.airline_id, board_numbers.board_number, board_numbers.aircraft_type) " +
                    "VALUES (?, ?, ?)";
            psql = connection.prepareStatement(sql);
            psql.setLong(1, board.getAirlineId());
            psql.setString(2, board.getNumber());
            psql.setString(3, board.getAircraft());
            psql.executeUpdate();
            sql = "SELECT MAX(board_numbers.board_id) FROM board_numbers";
            psql = connection.prepareStatement(sql);
            rs = psql.executeQuery();
            while (rs.next()) {
                responseBoard.setId(rs.getLong("MAX(board_numbers.board_id)"));
            }
            responseBoard.setAirlineId(board.getAirlineId());
            responseBoard.setNumber(board.getNumber());
            responseBoard.setAircraft(board.getAircraft());
            rs.close();
            psql.close();
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return responseBoard;
    }

    public String updateBoardInDB(int id, Board board) {
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        try {
            String sql = "UPDATE board_numbers " +
                    "SET board_numbers.airline_id = ?, board_numbers.board_number = ?, " +
                    " board_numbers.aircraft_type = ?" +
                    " WHERE board_numbers.board_id = ?";
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setLong(1, board.getAirlineId());
            psql.setString(2, board.getNumber());
            psql.setString(3, board.getAircraft());
            psql.setInt(4, id);
            psql.executeUpdate();
            psql.close();
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return "ok";
    }

    public String deleteBoardFromDB(int id) {
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        try {
            String sql = "UPDATE board_numbers " +
                    "SET board_numbers.is_deleted = ? " +
                    "WHERE board_numbers.board_id = ?";
            supportDao.deleteOrRecoveryEntity(connection, id, sql, true);
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return "ok";
    }
}
