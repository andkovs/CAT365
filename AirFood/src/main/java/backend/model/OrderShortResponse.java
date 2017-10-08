package backend.model;

import java.util.ArrayList;

public class OrderShortResponse {
    private String from;
    private String to;
    private ArrayList<OrderShort> orders;

    public OrderShortResponse(){
        orders = new ArrayList<OrderShort>();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public ArrayList<OrderShort> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<OrderShort> orders) {
        this.orders = orders;
    }

}
