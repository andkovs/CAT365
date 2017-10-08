package backend.model;

public class Ration {

    private Long id;
    private Long orderId;
    private String code;
    private String amount;
    private int classTypeId;
    private String flight;

    public Ration(){}

    public Ration(String code, String amount){
        setCode(code);
        setAmount(amount);
    }

    public Ration(Long id, Long orderId, String code, String amount, int classTypeId, String flight){
        setId(id);
        setOrderId(orderId);
        setCode(code);
        setAmount(amount);
        setClassTypeId(classTypeId);
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFlight() {
        return flight;
    }

    public void setFlight(String flight) {
        this.flight = flight;
    }

    public int getClassTypeId() {
        return classTypeId;
    }

    public void setClassTypeId(int classTypeId) {
        this.classTypeId = classTypeId;
    }
}
