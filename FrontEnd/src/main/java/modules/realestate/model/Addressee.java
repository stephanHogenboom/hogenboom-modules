package main.java.modules.realestate.model;

import java.util.Optional;

public class Addressee {

    private String name;
    private Address address;
    private Optional<String> phoneNumber;
    private Optional<String> email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Optional<String> getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Optional<String> phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public void setEmail(Optional<String> email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Addressee{" +
                "name='" + name + '\'' +
                ", address=" + address +
                ", phoneNumber=" + phoneNumber +
                ", email=" + email +
                '}';
    }
}
