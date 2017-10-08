package backend.model;

public class Drink {

    private Long id;
    private Long orderId;
    private String drink;
    private String flight;

    public Drink(){}

    public Drink(Long id, Long orderId, String drink, String flight){
        setId(id);
        setOrderId(orderId);
        setDrink(drink);
        setFlight(flight);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }

    public String getFlight() {
        return flight;
    }

    public void setFlight(String flight) {
        this.flight = flight;
    }
}
