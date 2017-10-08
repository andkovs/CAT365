package backend.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private Integer id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private Boolean isEnabled;
    private List<String> roles = new ArrayList<String>();
    private List<Airport> airports = new ArrayList<Airport>();

    public User(){}

    public User(Integer id, String login, String password, String firstName,
                   String lastName){
        this.setId(id);
        this.setLogin(login);
        this.setPassword(password);
        this.setFirstName(firstName);
        this.setLastName(lastName);
    }

    public User(Integer id, String login, String password, String firstName,
                   String lastName, String email){
        this.setId(id);
        this.setLogin(login);
        this.setPassword(password);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setEmail(email);
    }

    public User(Integer id, String login, String password, String firstName,
                   String lastName, String middleName,
                   String email, ArrayList<String> roles) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.email = email;
        this.roles = roles;
    }

    public User(Integer id, String login, String password,
                   String firstName, String lastName, String middleName,
                   String email, Boolean isEnabled) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.email = email;
        this.isEnabled = isEnabled;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<String> roles) {
        this.roles = roles;
    }

    public List<Airport> getAirports() {
        return airports;
    }

    public void setAirports(List<Airport> airports) {
        this.airports = airports;
    }
}