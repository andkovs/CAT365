package backend.model;

import java.util.ArrayList;

public class Account {

    private ArrayList<User> users = new ArrayList<User>();
    private ArrayList<Role> roles = new ArrayList<Role>();

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<Role> roles) {
        this.roles = roles;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
