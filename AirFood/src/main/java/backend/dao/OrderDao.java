package backend.dao;

import backend.model.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.sql.*;
import java.util.ArrayList;

public class OrderDao {

    //private static final String SERVER_UPLOAD_LOCATION_FOLDER = "C://IdeaProjects//airfoodFiles//";
    private static final String SERVER_UPLOAD_LOCATION_FOLDER = "/var/cat/docs/";

    //GET

    public Order getOrderById(Long id) {
        Order order = new Order();
        if (id == 0) {
            order.setId(0L);
            Date d = new Date();
            order.setArriveDateTime(null);
            order.setInspectionDateTime(null);
            order.setWorkDateTime(null);
            order.setReadyDateTime(null);
            order.setDepartureDateTime(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(d));
            return order;
        }
        String sql = "SELECT * FROM orders " +
                "WHERE orders.order_id = ?";
        try {
            Connection connection = ConnectionToMySQLDB.getConnaction();
            if (connection == null) {
                return null;
            }
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setLong(1, id);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                order.setId(rs.getLong("order_id"));
                order.setDepAirportId(rs.getLong("dep_airport_id"));
                order.setArrAirportId(rs.getLong("arr_airport_id"));
                if (rs.getObject("board_id") == null) {
                    order.setBoardId(null);
                } else {
                    order.setBoardId(rs.getLong("board_id"));
                }

                order.setFlightIdDirect(rs.getLong("direct_flight_id"));
                if (rs.getObject("reverse_flight_id") == null) {
                    order.setFlightIdReverse(null);
                } else {
                    order.setFlightIdReverse(rs.getLong("reverse_flight_id"));
                }
                order.setArriveDateTime(parseDateForShortList(rs.getString("arrive_date_time")));
                order.setDepartureDateTime(parseDateForShortList(rs.getString("departure_date_time")));
                order.setInspectionDateTime(parseDateForShortList(rs.getString("inspection_date_time")));
                order.setWorkDateTime(parseDateForShortList(rs.getString("work_date_time")));
                order.setReadyDateTime(parseDateForShortList(rs.getString("ready_date_time")));
                order.setDirectComment(rs.getString("direct_comment"));
                order.setReverseComment(rs.getString("reverse_comment"));
            }
            ArrayList<Ration>[] rationArray = getRations(connection, id);
            Drink[] drinks = getDrinks(connection, id);
            order.setRationDirectBusinessList(rationArray[0]);
            order.setRationDirectEconomList(rationArray[1]);
            order.setRationDirectCrewList(rationArray[2]);
            order.setRationDirectSpecialList(rationArray[3]);
            order.setRationReverseBusinessList(rationArray[4]);
            order.setRationReverseEconomList(rationArray[5]);
            order.setRationReverseCrewList(rationArray[6]);
            order.setRationReverseSpecialList(rationArray[7]);
            order.setDrinkDirect(drinks[0]);
            order.setDrinkReverse(drinks[1]);
            order.setFiles(getFilesByOrderId(connection, id, "on"));
            rs.close();
            psql.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    private ArrayList<UploadFile> getFilesByOrderId(Connection connection, Long id, String type) throws SQLException {
        String sql = "SELECT * FROM files " +
                "WHERE files.order_id = ? AND files.is_deleted = 0  AND files.file_type = ?";
        ArrayList<UploadFile> list = new ArrayList<UploadFile>();
        PreparedStatement psql = connection.prepareStatement(sql);
        psql.setLong(1, id);
        psql.setString(2, type);
        ResultSet rs = psql.executeQuery();
        while (rs.next()) {
            list.add(new UploadFile(rs.getLong("files.file_id"),
                    rs.getLong("files.order_id"),
                    rs.getString("files.file_name")));
        }
        rs.close();
        psql.close();
        return list;
    }

    public OrderShortResponse getShortOrders(String from, String to, Integer userId) {
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        OrderShortResponse response = new OrderShortResponse();
        String newFrom = parseDate(from);// parse in MySQL date formate
        String newTo = parseDate(to);// parse in MySQL date formate
        response.setOrders(new ArrayList<OrderShort>());
        try {
            String sql = "SELECT o.order_id, o.dep_airport_id, o.arr_airport_id, a.name, aa.name, " +
                    "o.departure_date_time, o.arrive_date_time, o.ready_date_time, o.inspection_date_time, o.work_date_time, o.change_date_time, b.board_number, " +
                    "b.aircraft_type, f.flight_number, ff.flight_number FROM orders o " +
                    "JOIN airports a ON o.dep_airport_id=a.airport_id " +
                    "JOIN airports aa ON o.arr_airport_id=aa.airport_id " +
                    "LEFT JOIN board_numbers b ON o.board_id=b.board_id " +
                    "JOIN flight_numbers f ON o.direct_flight_id=f.flight_id " +
                    "LEFT JOIN flight_numbers ff ON o.reverse_flight_id=ff.flight_id " +
                    "WHERE o.is_deleted = 0 AND o.departure_date_time >= ? AND o.departure_date_time <= ?";
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setString(1, newFrom);
            psql.setString(2, newTo);
            ResultSet rs = psql.executeQuery();
            ArrayList<OrderShort> orders = new ArrayList<OrderShort>();
            while (rs.next()) {
                String flightNumber = "-";
                if(rs.getString("ff.flight_number")!=null){
                    flightNumber = rs.getString("ff.flight_number");
                }
                orders.add(new OrderShort(
                        rs.getLong("o.order_id"),
                        rs.getLong("o.dep_airport_id"),
                        rs.getLong("o.arr_airport_id"),
                        rs.getString("a.name"),
                        rs.getString("aa.name"),
                        parseDateForShortList(rs.getString("o.arrive_date_time")),
                        parseDateForShortList(rs.getString("o.departure_date_time")),
                        parseDateForShortList(rs.getString("o.ready_date_time")),
                        parseDateForShortList(rs.getString("o.work_date_time")),
                        parseDateForShortList(rs.getString("o.inspection_date_time")),
                        parseDateForShortList(rs.getString("o.change_date_time")),
                        rs.getString("b.air_craft_type"),
                        rs.getString("f.flight_number"),
                        flightNumber,
                        rs.getString("b.board_number")
                ));
            }
            ArrayList<Airport> airports = new AirportDao().getAllAirportsByUserId(userId);
            if (airports != null) {
                ArrayList<Airport> lockedAirports = airportFilter(airports);
                response.setOrders(ordersWithoutLockedAirports(orders, lockedAirports));
            }
            response.setFrom(from);
            response.setTo(to);
            rs.close();
            psql.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }


    public OrderPreview getOrderPreviewById(Long id) {
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        OrderPreview orderPreview = new OrderPreview();
        String sql = "SELECT o.order_id, a.name, aa.name, f.flight_number, ff.flight_number,  b.board_number, b.aircraft_type," +
                "o.departure_date_time, o.arrive_date_time, o.inspection_date_time, o.work_date_time, o.ready_date_time, " +
                "o.direct_comment, o.reverse_comment " +
                "FROM orders o " +
                "JOIN airports a ON o.dep_airport_id=a.airport_id " +
                "JOIN airports aa ON o.arr_airport_id=aa.airport_id " +
                "LEFT JOIN board_numbers b ON o.board_id=b.board_id " +
                "JOIN flight_numbers f ON o.direct_flight_id=f.flight_id " +
                "LEFT JOIN flight_numbers ff ON o.reverse_flight_id=ff.flight_id " +
                "WHERE o.order_id = ?";
        try {
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setLong(1, id);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                orderPreview.setId(rs.getLong("o.order_id"));
                orderPreview.setDepAirportName(rs.getString("a.name"));
                orderPreview.setArrAirportName(rs.getString("aa.name"));
                orderPreview.setFlightNameDirect(rs.getString("f.flight_number"));
                orderPreview.setFlightNameReverse(rs.getString("ff.flight_number"));
                orderPreview.setBoardNumber(rs.getString("b.board_number"));
                orderPreview.setAircraftType(rs.getString("b.air_craft_type"));
                orderPreview.setArriveDateTime(parseDateForShortList(rs.getString("o.arrive_date_time")));
                orderPreview.setDepartureDateTime(parseDateForShortList(rs.getString("o.departure_date_time")));
                orderPreview.setReadyDateTime(parseDateForShortList(rs.getString("o.ready_date_time")));
                orderPreview.setInspectionDateTime(parseDateForShortList(rs.getString("o.inspection_date_time")));
                orderPreview.setWorkDateTime(parseDateForShortList(rs.getString("o.work_date_time")));
                orderPreview.setDirectComment(rs.getString("o.direct_comment"));
                orderPreview.setReverseComment(rs.getString("o.reverse_comment"));
            }
            Drink[] drinks = getDrinks(connection, id);
            if (drinks[0] != null) {
                orderPreview.setDrinkDirect(drinks[0].getDrink());
            } else {
                orderPreview.setDrinkDirect("");
            }
            if (drinks[1] != null) {
                orderPreview.setDrinkReverse(drinks[1].getDrink());
            } else {
                orderPreview.setDrinkReverse("");
            }
            ArrayList<Ration>[] rationArray = getRations(connection, id);
            for (int i = 0; i < rationArray.length; i++) {
                if (rationArray[i].size() == 0) {
                    rationArray[i].add(new Ration("0", "0"));
                }
            }
            orderPreview.setRationDirectBusinessList(rationArray[0]);
            orderPreview.setRationDirectEconomList(rationArray[1]);
            orderPreview.setRationDirectCrewList(rationArray[2]);
            orderPreview.setRationDirectSpecialList(rationArray[3]);
            orderPreview.setRationReverseBusinessList(rationArray[4]);
            orderPreview.setRationReverseEconomList(rationArray[5]);
            orderPreview.setRationReverseCrewList(rationArray[6]);
            orderPreview.setRationReverseSpecialList(rationArray[7]);
            orderPreview.setFiles(getFilesByOrderId(connection, id, "on"));
            orderPreview.setFilesFrom(getFilesByOrderId(connection, id, "from"));
            rs.close();
            psql.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderPreview;
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    //POST and PUT ORDERS

    public Long setNewOrEditOrderInDB(Order order) {
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        try {
            if (order.getId() == 0L) { //set New Order
                Long newOrderId = setOnlyOrderInDB(connection, order);
                setAllRationsAndDrinksInDB(connection, order, newOrderId);
                createDirectories(newOrderId);
                return newOrderId;
            } else { // update existent Order
                updateOrderInDB(connection, order);
                deleteRationsByOrderId(connection, order.getId());
                deleteDrinksByOrderId(connection, order.getId());
                setAllRationsAndDrinksInDB(connection, order, order.getId());
                return order.getId();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    private void createDirectories(Long newOrderId) {
        File dir = new File(SERVER_UPLOAD_LOCATION_FOLDER + newOrderId);
        boolean created = dir.mkdir();
        //dir = new File(SERVER_UPLOAD_LOCATION_FOLDER + newOrderId + "//on");
        dir = new File(SERVER_UPLOAD_LOCATION_FOLDER + newOrderId + "/on");
        created = dir.mkdir();
        //dir = new File(SERVER_UPLOAD_LOCATION_FOLDER + newOrderId + "//from");
        dir = new File(SERVER_UPLOAD_LOCATION_FOLDER + newOrderId + "/from");
        created = dir.mkdir();
    }

    private void updateOrderInDB(Connection connection, Order order) throws SQLException {
        String sql = "UPDATE orders " +
                "SET orders.dep_airport_id = ?" +
                ", orders.arr_airport_id = ?" +
                ", orders.board_id = ?" +
                ", orders.direct_flight_id = ?" +
                ", orders.reverse_flight_id = ?" +
                ", orders.departure_date_time = ?" +
                ", orders.arrive_date_time = ?" +
                ", orders.inspection_date_time = ?" +
                ", orders.work_date_time = ?" +
                ", orders.ready_date_time = ?" +
                ", orders.change_date_time = ?" +
                ", orders.direct_comment = ?" +
                ", orders.reverse_comment = ?" +
                " WHERE orders.order_id = ?";
        PreparedStatement psql = connection.prepareStatement(sql);
        psql.setLong(1, order.getDepAirportId());
        psql.setLong(2, order.getArrAirportId());
        if (order.getBoardId() == null || order.getBoardId() == 0) {
            psql.setNull(3, Types.INTEGER);
        } else {
            psql.setLong(3, order.getBoardId());
        }
        psql.setLong(4, order.getFlightIdDirect());
        if (order.getFlightIdReverse() == null || order.getFlightIdReverse() == 0) {
            psql.setNull(5, Types.INTEGER);
        } else {
            psql.setLong(5, order.getFlightIdReverse());
        }
        psql.setString(6, parseDate(order.getDepartureDateTime()));
        psql.setString(7, parseDate(order.getArriveDateTime()));
        psql.setString(8, parseDate(order.getInspectionDateTime()));
        psql.setString(9, parseDate(order.getWorkDateTime()));
        psql.setString(10, parseDate(order.getReadyDateTime()));
        psql.setString(11, getNow());
        psql.setString(12, order.getDirectComment());
        psql.setString(13, order.getReverseComment());
        psql.setLong(14, order.getId());
        psql.executeUpdate();
        psql.close();
    }

    private Long setOnlyOrderInDB(Connection connection, Order order) throws SQLException {
        String sql = "INSERT INTO orders (orders.dep_airport_id, orders.arr_airport_id, orders.board_id, " +
                "orders.direct_flight_id, orders.reverse_flight_id, " +
                "orders.departure_date_time, orders.arrive_date_time, orders.inspection_date_time, " +
                "orders.work_date_time, orders.ready_date_time, orders.change_date_time, " +
                "orders.direct_comment, orders.reverse_comment) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement psql = connection.prepareStatement(sql);
        psql.setLong(1, order.getDepAirportId());
        psql.setLong(2, order.getArrAirportId());
        if (order.getBoardId() == null || order.getBoardId() == 0) {
            psql.setNull(3, Types.INTEGER);
        } else {
            psql.setLong(3, order.getBoardId());
        }
        psql.setLong(4, order.getFlightIdDirect());
        if (order.getFlightIdReverse() == null || order.getFlightIdReverse() == 0) {
            psql.setNull(5, Types.INTEGER);
        } else {
            psql.setLong(5, order.getFlightIdReverse());
        }
        psql.setString(6, parseDate(order.getDepartureDateTime()));
        psql.setString(7, parseDate(order.getArriveDateTime()));
        psql.setString(8, parseDate(order.getInspectionDateTime()));
        psql.setString(9, parseDate(order.getWorkDateTime()));
        psql.setString(10, parseDate(order.getReadyDateTime()));
        psql.setString(11, getNow());
        psql.setString(12, order.getDirectComment());
        psql.setString(13, order.getReverseComment());
        psql.executeUpdate();
        Long newOrderId = null;
        sql = "SELECT MAX(orders.order_id) FROM orders";
        psql = connection.prepareStatement(sql);
        ResultSet rs = psql.executeQuery();
        while (rs.next()) {
            newOrderId = rs.getLong("MAX(orders.order_id)");
        }
        rs.close();
        psql.close();
        return newOrderId;
    }

    public String deleteOrderFromDB(int id) {
        Connection connection = ConnectionToMySQLDB.getConnaction();
        if (connection == null) {
            return null;
        }
        try {
            String sql = "UPDATE orders " +
                    "SET orders.is_deleted = 1 " +
                    "WHERE orders.order_id = ?";
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setInt(1, id);
            psql.executeUpdate();
            psql.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "ok";
    }

    ////////////////////////////////////////////////////////////////////////

    //DRINKS

    private Drink[] getDrinks(Connection connection, Long id) throws SQLException {
        Drink[] response = new Drink[2];
        String direct = "SELECT * FROM drinks " +
                "WHERE drinks.order_id = ? AND drinks.flight_type ='direct' " +
                "AND drinks.is_deleted = 0";
        String reverse = "SELECT * FROM drinks " +
                "WHERE drinks.order_id = ? AND drinks.flight_type ='reverse' " +
                "AND drinks.is_deleted = 0";
        PreparedStatement psql = connection.prepareStatement(direct);
        psql.setLong(1, id);
        ResultSet rs = psql.executeQuery();
        while (rs.next()) {
            response[0] = new Drink(
                    rs.getLong("drink_id"),
                    rs.getLong("order_id"),
                    rs.getString("drink_type"),
                    rs.getString("flight_type")
            );
        }
        psql = connection.prepareStatement(reverse);
        psql.setLong(1, id);
        rs = psql.executeQuery();
        while (rs.next()) {
            response[1] = new Drink(
                    rs.getLong("drink_id"),
                    rs.getLong("order_id"),
                    rs.getString("drink_type"),
                    rs.getString("flight_type")
            );
        }
        rs.close();
        psql.close();
        return response;
    }

    ////////////////////////////////////////////////////////////////////////

    //RATIONS

    private void deleteRationsByOrderId(Connection connection, Long orderId) throws SQLException {
        String sql = "DELETE FROM rations WHERE rations.order_id = ?";
        PreparedStatement psql = connection.prepareStatement(sql);
        psql.setLong(1, orderId);
        psql.executeUpdate();
        psql.close();
    }

    private void deleteDrinksByOrderId(Connection connection, Long orderId) throws SQLException {
        String sql = "DELETE FROM drinks WHERE drinks.order_id = ?";
        PreparedStatement psql = connection.prepareStatement(sql);
        psql.setLong(1, orderId);
        psql.executeUpdate();
        psql.close();
    }

    private void setDrinkInDB(Connection connection, Drink drink, Long newOrderId) throws SQLException {
        if (drink == null) {
            return;
        }
        String sql = "INSERT INTO drinks (drinks.order_id, drinks.drink_type, drinks.flight_type) " +
                "VALUES (?, ?, ?)";
        PreparedStatement psql = connection.prepareStatement(sql);
        psql.setLong(1, newOrderId);
        psql.setString(2, drink.getDrink());
        psql.setString(3, drink.getFlight());
        psql.executeUpdate();
        psql.close();
    }

    private void setAllRationsAndDrinksInDB(Connection connection, Order order, Long newOrderId) throws SQLException {
        for (Ration ration :
                order.getRationDirectBusinessList()) {
            setRationInDB(connection, ration, newOrderId);
        }
        for (Ration ration :
                order.getRationDirectEconomList()) {
            setRationInDB(connection, ration, newOrderId);
        }
        for (Ration ration :
                order.getRationDirectCrewList()) {
            setRationInDB(connection, ration, newOrderId);
        }
        for (Ration ration :
                order.getRationDirectSpecialList()) {
            setRationInDB(connection, ration, newOrderId);
        }
        for (Ration ration :
                order.getRationReverseBusinessList()) {
            setRationInDB(connection, ration, newOrderId);
        }
        for (Ration ration :
                order.getRationReverseEconomList()) {
            setRationInDB(connection, ration, newOrderId);
        }
        for (Ration ration :
                order.getRationReverseCrewList()) {
            setRationInDB(connection, ration, newOrderId);
        }
        for (Ration ration :
                order.getRationReverseSpecialList()) {
            setRationInDB(connection, ration, newOrderId);
        }
        setDrinkInDB(connection, order.getDrinkDirect(), newOrderId);
        setDrinkInDB(connection, order.getDrinkReverse(), newOrderId);
    }

    private void setRationInDB(Connection connection, Ration ration, Long newOrderId) throws SQLException {
        String sql = "INSERT INTO rations (rations.order_id, rations.ration_code, rations.amount, rations.class_type_id, rations.flight_type) " +
                "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement psql = connection.prepareStatement(sql);
        psql.setLong(1, newOrderId);
        psql.setString(2, ration.getCode());
        psql.setString(3, ration.getAmount());
        psql.setInt(4, ration.getClassTypeId());
        psql.setString(5, ration.getFlight());
        psql.executeUpdate();
        psql.close();
    }

    private ArrayList<Ration>[] getRations(Connection connection, Long id) throws SQLException {
        ArrayList<Ration> list = new ArrayList<Ration>();
        ArrayList<Ration>[] response = new ArrayList[8];
        String sql;
        sql = "SELECT * FROM rations " +
                "WHERE rations.order_id = ? AND rations.is_deleted = 0";
        PreparedStatement psql = connection.prepareStatement(sql);
        psql.setLong(1, id);
        ResultSet rs = psql.executeQuery();
        while (rs.next()) {
            list.add(new Ration(
                    rs.getLong("rationid"),
                    rs.getLong("order_id"),
                    rs.getString("ration_code"),
                    rs.getString("amount"),
                    rs.getInt("class_type_id"),
                    rs.getString("flight_type")
            ));
        }
        rs.close();
        ArrayList<Ration> businessDirect = new ArrayList<Ration>();
        ArrayList<Ration> economDirect = new ArrayList<Ration>();
        ArrayList<Ration> crewDirect = new ArrayList<Ration>();
        ArrayList<Ration> specialDirect = new ArrayList<Ration>();
        ArrayList<Ration> businessReverse = new ArrayList<Ration>();
        ArrayList<Ration> economReverse = new ArrayList<Ration>();
        ArrayList<Ration> crewReverse = new ArrayList<Ration>();
        ArrayList<Ration> specialReverse = new ArrayList<Ration>();
        for (int i = 0; i < list.size(); i++) {
            Ration current = list.get(i);
            if (current.getClassTypeId() == 1) {
                if (current.getFlight().equals("direct")) {
                    businessDirect.add(current);
                } else {
                    businessReverse.add(current);
                }
            } else if (current.getClassTypeId() == 2) {
                if (current.getFlight().equals("direct")) {
                    economDirect.add(current);
                } else {
                    economReverse.add(current);
                }
            } else if (current.getClassTypeId() == 3) {
                if (current.getFlight().equals("direct")) {
                    crewDirect.add(current);
                } else {
                    crewReverse.add(current);
                }
            } else if (current.getClassTypeId() == 4) {
                if (current.getFlight().equals("direct")) {
                    specialDirect.add(current);
                } else {
                    specialReverse.add(current);
                }
            }
        }
        response[0] = businessDirect;
        response[1] = economDirect;
        response[2] = crewDirect;
        response[3] = specialDirect;
        response[4] = businessReverse;
        response[5] = economReverse;
        response[6] = crewReverse;
        response[7] = specialReverse;
        psql.close();
        rs.close();
        return response;
    }

    ////////////////////////////////////////////////////////////////////////////////////

    //work with airports
    private ArrayList<OrderShort> ordersWithoutLockedAirports(ArrayList<OrderShort> orders, ArrayList<Airport> airports) {
        ArrayList<OrderShort> lockedOrders = new ArrayList<OrderShort>();
        for (OrderShort o : orders) {
            ;
            for (Airport a : airports) {
                if (o.getDepAirportId() == a.getId()) {
                    lockedOrders.add(o);
                    break;
                }
            }
        }
        orders.removeAll(lockedOrders);
        return orders;
    }

    private ArrayList<Airport> airportFilter(ArrayList<Airport> airports) {

        ArrayList<Airport> allAirports = new AirportDao().getAllAirports();
        Boolean b = allAirports.removeAll(airports);
        return allAirports;
    }


    ////////////////////////////////////////////////////////////////////////////////////

    //PARSE DATE

    private String parseDate(String date) {
        if (date == null || date.equals("")) {
            return null;
        }
        date = date.substring(6, 10) + "-" + date.substring(3, 5) + "-" + date.substring(0, 2) + " " + date.substring(11, 16).replaceAll(":", "-");
        return date;
    }

    private String parseDateForShortList(String date) {
        if(date==null){
            return null;
        }
        String s = date.substring(8, 10) + "-" + date.substring(5, 7) + "-" + date.substring(0, 4) + " "
                + date.substring(11, 13) + ":" + date.substring(14, 16);
        return s;
    }

    private String getNow() {
        Date d = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(d);
    }

}




