package carsharing;

public class CarCompany {
    private int id;
    private final String name;
    private final int companyID;

    public CarCompany(String name, Integer companyID) {
        this.name = name;
        this.companyID = companyID;
    }

    public CarCompany(int id, String name, int companyID) {
        this.id = id;
        this.name = name;
        this.companyID = companyID;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCompanyID() {
        return companyID;
    }
}
