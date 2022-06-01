package carsharing;

import java.sql.*;
import java.util.HashMap;

public class CarManagement {
    private static final Connection conn;
    
    static {
        conn = ModelDB.getInstance().getConnection();
    }

    static HashMap<Integer, CarCompany> getCarsCompany(int carCompanyId) {
        HashMap<Integer, CarCompany> cars = new HashMap<>();
        try {
            ResultSet carRes = conn.createStatement()
                    .executeQuery("SELECT * FROM " + TableName.CAR.getName()
                            + " WHERE COMPANY_ID = " + carCompanyId + ";");

            int index = 1;

            //set car data to object CarCompany
            while (carRes.next()) {
                int carId = carRes.getInt("ID");
                String name = carRes.getString("NAME");
                int companyId = carRes.getInt("COMPANY_ID");

                ResultSet carData = conn.createStatement()
                        .executeQuery("SELECT NAME FROM " + TableName.COMPANY.getName()
                                + " WHERE ID = " + carCompanyId + ";");

                carData.next();

                cars.put(index, new CarCompany(carId, name, companyId));

                index++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    static CarCompany getCar(int carID) {
        CarCompany car = null;

        if (carID == 0) {
            return null;
        }

        try {
            ResultSet carRes = conn.createStatement()
                    .executeQuery("SELECT * FROM " + TableName.CAR.getName()
                            + " WHERE ID = " + carID + ";");

            if (carRes.next()) {
                String name = carRes.getString("NAME");
                int companyIdInTable = carRes.getInt("COMPANY_ID");

                car = new CarCompany(carID, name, companyIdInTable);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return car;
    }

    static void rentCar(CarCompany car, Customer customer) {
        String rentCarQuery = "UPDATE " + TableName.CUSTOMER.getName()
                + " SET RENTED_CAR_ID = " + car.getId() +
                " WHERE ID = " + customer.getId() + ";";
        try {
            ModelDB.makeStatement(rentCarQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Consumer cant rent this car.");
        }
    }

    static void returnCar(int customerID) {
        String returnCarQuery = "UPDATE " + TableName.CUSTOMER.getName()
                + " SET RENTED_CAR_ID = NULL " +
                "WHERE ID = " + customerID + ";";
        try {
            ModelDB.makeStatement(returnCarQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Consumer cant return this car.");
        }
    }

    static void addCarToCompany(CarCompany car) {
        try {
            PreparedStatement prep = conn.prepareStatement("INSERT INTO "
                    + TableName.CAR.getName() + "(NAME, COMPANY_ID) " +
                    "values(?,?)");
            prep.setString(1, car.getName());
            prep.setInt(2, car.getCompanyID());
            prep.executeUpdate();
            prep.close();
        } catch (SQLException e) {
            System.out.println("Cant add car");
            e.printStackTrace();
        }

    }

    static HashMap<Integer, CarCompany> getFreeCarsFromCompany(int companyId) {
        HashMap<Integer, CarCompany> freeCars = new HashMap<>();

        try {
            Statement st = conn.createStatement();
            ResultSet freeCarsDb = st.executeQuery("SELECT " +
                    "ID, NAME, COMPANY_ID " +
                    "FROM " +
                    "CAR " +
                    "WHERE " +
                    "COMPANY_ID = " + companyId +
                    " AND " +
                    "ID NOT IN (" +
                    "SELECT RENTED_CAR_ID " +
                    "FROM CUSTOMER " +
                    "WHERE RENTED_CAR_ID IS NOT NULL)");

            int increment = 1;
            while (freeCarsDb.next()) {
                String name = freeCarsDb.getString("NAME");
                int id = freeCarsDb.getInt("ID");
                int companyID = freeCarsDb.getInt("COMPANY_ID");
                freeCars.put(increment, new CarCompany(id, name, companyID));
                increment++;
            }

            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return freeCars;
    }
}
