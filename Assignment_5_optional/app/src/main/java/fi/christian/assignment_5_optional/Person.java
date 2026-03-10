package fi.christian.assignment_5_optional;

import java.util.ArrayList;

public class Person {
    public String firstName;
    public String lastName;
    public String phone;
    public String education;
    public ArrayList<String> hobbies;

    public Person(String firstName, String lastName, String phone, String education, ArrayList<String> hobbies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.education = education;
        this.hobbies = hobbies;
    }
}