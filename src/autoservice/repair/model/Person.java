package autoservice.repair.model;

public class Person {

    private final String name;
    private final String phone;


    public Person(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
