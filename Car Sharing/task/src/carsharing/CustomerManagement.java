package carsharing;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class CustomerManagement {
    private static final Connection conn;

    static {
        conn = ModelDB.getInstance().getConnection();
    }

    static public Map<Integer, Customer> getAllCustomer() {
        HashMap<Integer, Customer> customers = new HashMap<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + TableName.CUSTOMER.getName() + ";");

            int index = 1;
            while (rs.next()) {
                String name = rs.getString("NAME");
                int rentedCarId = rs.getInt("RENTED_CAR_ID");
                int id = rs.getInt("ID");

                customers.put(index, new Customer(id, name, rentedCarId));
                index++;
            }

        } catch (SQLException e) {
            System.out.println("Data cannot be retrieved");
            e.printStackTrace();
        }
        return customers;
    }

    static  void addCustomer(Customer customer) {
        try {
            PreparedStatement prep = conn.prepareStatement("INSERT INTO "
                    + TableName.CUSTOMER.getName() + "(NAME) " +
                    "values(?)");

            prep.setString(1, customer.getName());
            prep.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Cant add car");
            e.printStackTrace();
        }
    }

    static Customer getCustomer(int customerId) {
        Customer customer = null;
        try (Statement stmt = conn.createStatement()) {
            ResultSet customerRes = stmt
                    .executeQuery("SELECT * FROM " + TableName.CUSTOMER.getName() +
                            " WHERE ID = " + customerId + ";");

            while (customerRes.next()) {
                String name = customerRes.getString("NAME");
                int rentedCarID = customerRes.getInt("RENTED_CAR_ID");
                int id = customerRes.getInt("ID");

                customer = new Customer(id, name, rentedCarID);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }
}
