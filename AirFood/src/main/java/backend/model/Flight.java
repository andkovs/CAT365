package backend.model;

public class Flight {

    private Long id;
    private Long airlineId;
    private String number;

    public Flight(){}

    public Flight(Long id, Long airlineId, String number){
        setId(id);
        setAirlineId(airlineId);
        setNumber(number);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAirlineId() {
        return airlineId;
    }

    public void setAirlineId(Long airlineId) {
        this.airlineId = airlineId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
