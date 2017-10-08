package backend.model;

public class OrderShort {

    private Long id;
    private Long depAirportId;
    private Long arrAirportId;
    private String depAirportName;
    private String arrAirportName;
    private String arriveDateTime;
    private String departureDateTime;
    private String readyDateTime;
    private String workDateTime;
    private String inspectionDateTime;
    private String changeDateTime;
    private String aircraftType;
    private String flightNumberDirect;
    private String flightNumberReverse;
    private String boardNumber;

    public OrderShort(Long id, Long depAirportId, Long arrAirportId, String depAirportName, String arrAirportName,
                      String arriveDateTime, String departureDateTime, String readyDateTime, String workDateTime,
                      String inspectionDateTime, String changeDateTime,
                      String aircraftType, String flightNumberDirect, String flightNumberReverse, String boardNumber) {
        this.id = id;
        this.depAirportId = depAirportId;
        this.arrAirportId = arrAirportId;
        this.depAirportName = depAirportName;
        this.arrAirportName = arrAirportName;
        this.arriveDateTime = arriveDateTime;
        this.departureDateTime = departureDateTime;
        this.readyDateTime = readyDateTime;
        this.workDateTime = workDateTime;
        this.inspectionDateTime = inspectionDateTime;
        this.changeDateTime = changeDateTime;
        this.aircraftType = aircraftType;
        this.flightNumberDirect = flightNumberDirect;
        this.flightNumberReverse = flightNumberReverse;
        this.boardNumber = boardNumber;
    }

    public OrderShort() {
    }

    public String getReadyDateTime() {
        return readyDateTime;
    }

    public void setReadyDateTime(String readyDateTime) {
        this.readyDateTime = readyDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDepAirportId() {
        return depAirportId;
    }

    public void setDepAirportId(Long depAirportId) {
        this.depAirportId = depAirportId;
    }

    public Long getArrAirportId() {
        return arrAirportId;
    }

    public void setArrAirportId(Long arrAirportId) {
        this.arrAirportId = arrAirportId;
    }

    public String getDepAirportName() {
        return depAirportName;
    }

    public void setDepAirportName(String depAirportName) {
        this.depAirportName = depAirportName;
    }

    public String getArrAirportName() {
        return arrAirportName;
    }

    public void setArrAirportName(String arrAirportName) {
        this.arrAirportName = arrAirportName;
    }

    public String getArriveDateTime() {
        return arriveDateTime;
    }

    public void setArriveDateTime(String arriveDateTime) {
        this.arriveDateTime = arriveDateTime;
    }

    public String getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(String departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public String getWorkDateTime() {
        return workDateTime;
    }

    public void setWorkDateTime(String workDateTime) {
        this.workDateTime = workDateTime;
    }

    public String getInspectionDateTime() {
        return inspectionDateTime;
    }

    public void setInspectionDateTime(String inspectionDateTime) {
        this.inspectionDateTime = inspectionDateTime;
    }

    public String getChangeDateTime() {
        return changeDateTime;
    }

    public void setChangeDateTime(String changeDateTime) {
        this.changeDateTime = changeDateTime;
    }

    public String getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    public String getFlightNumberDirect() {
        return flightNumberDirect;
    }

    public void setFlightNumberDirect(String flightNumberDirect) {
        this.flightNumberDirect = flightNumberDirect;
    }

    public String getFlightNumberReverse() {
        return flightNumberReverse;
    }

    public void setFlightNumberReverse(String flightNumberReverse) {
        this.flightNumberReverse = flightNumberReverse;
    }

    public String getBoardNumber() {
        return boardNumber;
    }

    public void setBoardNumber(String boardNumber) {
        this.boardNumber = boardNumber;
    }

    @Override
    public boolean equals(Object anObject) {
        if (!(anObject instanceof OrderShort)) {
            return false;
        }
        OrderShort otherMember = (OrderShort) anObject;
        return otherMember.getId().equals(getId());
    }
}
