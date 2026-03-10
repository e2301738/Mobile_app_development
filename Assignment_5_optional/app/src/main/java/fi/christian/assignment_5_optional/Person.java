package fi.christian.assignment_5_optional;

import java.util.ArrayList;

public class Person {
    private String firstName;
    private String lastName;
    private String phone;
    private String education;
    private ArrayList<String> hobbies;

    public Person(String firstName, String lastName, String phone, String education, ArrayList<String> hobbies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.education = education;
        this.hobbies = hobbies;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEducation() {
        return education;
    }

    public ArrayList<String> getHobbies() {
        return hobbies;
    }
}