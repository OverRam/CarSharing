package carsharing;

import java.util.HashMap;
import java.util.Map;

public class Company {
    private String name;
    private int id;
    private Map<Integer, CarCompany> carsCompany = new HashMap<>();

    public Company() {
    }

    public Company(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Map<Integer, CarCompany> getCarsCompany() {
        return carsCompany;
    }

    public void setCarsCompany(Map<Integer, CarCompany> carsCompany) {
        this.carsCompany = carsCompany;
    }
}
