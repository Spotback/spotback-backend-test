package com.spotback.pojo;

public class User {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Car car;

    public User(String email, String firstname, String lastname, String password, Car car) {
        this.email = email;
        this.firstName = firstname;
        this.lastName = lastname;
        this.password = password;
        this.car = car;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}
