package carsharing;

public enum TableName {
    COMPANY("COMPANY"),
    CAR("CAR"),
    CUSTOMER("CUSTOMER");

    private final String name;

    TableName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
