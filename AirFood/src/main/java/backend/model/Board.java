package backend.model;

public class Board {

    private Long id;
    private Long airlineId;
    private String number;
    private String aircraft;

    public Board(){}

    public Board(Long id, Long airlineId, String number, String aircraft){
        setId(id);
        setAircraft(aircraft);
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

    public void setNumber(String board) {
        this.number = board;
    }

    public String getAircraft() {
        return aircraft;
    }

    public void setAircraft(String aircraft) {
        this.aircraft = aircraft;
    }
}
