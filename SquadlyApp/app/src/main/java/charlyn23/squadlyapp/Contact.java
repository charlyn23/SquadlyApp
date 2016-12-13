package charlyn23.squadlyapp;

/**
 * Created by charlynbuchanan on 12/11/16.
 */

public class Contact {
    private String id;
    private String name;
    private String phoneNumber;

    public Contact(){
    }

    public Contact(String id, String name, String phoneNumber){
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public Contact(String name, String phoneNumber){
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

