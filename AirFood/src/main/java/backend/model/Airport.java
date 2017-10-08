package backend.model;

public class Airport {

    private Long id;
    private String name;

    public Airport() {
    }

    public Airport(Long id, String name){
        this.setId(id);
        this.setName(name);
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

    @Override
    public boolean equals(Object anObject) {
        if (!(anObject instanceof Airport)) {
            return false;
        }
        Airport otherMember = (Airport)anObject;
        return otherMember.getId().equals(getId());
    }
}
