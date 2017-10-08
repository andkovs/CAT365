package backend.model;

public class UploadFile {

    private Long id;
    private Long orderId;
    private String name;

    public UploadFile(Long id, Long orderId, String name){
        setId(id);
        setOrderId(orderId);
        setName(name);
    }

    public UploadFile(){}

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
