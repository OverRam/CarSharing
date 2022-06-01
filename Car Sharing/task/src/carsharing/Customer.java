package carsharing;

public class Customer {
    private int id;
    private final String name;
    private Integer rentedCarID;

    //create customer
    public Customer(String name) {
        this.name = name;
    }

    //get from database
    public Customer(int id, String name, Integer rentedCarID) {
        this.name = name;
        this.rentedCarID = rentedCarID;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getRentedCarID() {
        return rentedCarID;
    }
}
