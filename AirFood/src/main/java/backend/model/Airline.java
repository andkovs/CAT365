package backend.model;

public class Airline {

    private Long id;
    private String name;
    private String fullname;
    private String iata;
    private String phone;
    private String email;

    public Airline(){}

    public  Airline(Long id, String name, String fullname, String iata, String phone, String email){
        setId(id);
        setName(name);
        setFullname(fullname);
        setIata(iata);
        setPhone(phone);
        setEmail(email);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getIata() {
        return iata;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
