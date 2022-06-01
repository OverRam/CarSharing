package carsharing;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class CompanyManagement {
    private static final Connection conn;

    static {
        conn = ModelDB.getInstance().getConnection();
    }

    static public Map<Integer, Company> getAllCompany() {
        HashMap<Integer, Company> companies = new HashMap<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + TableName.COMPANY.getName() + ";");

            int index = 1;
            while (rs.next()) {
                int idNum = rs.getInt("ID");
                String name = rs.getString("NAME");
                companies.put(index, new Company(idNum, name));
                index++;
            }

        } catch (SQLException e) {
            System.out.println("Data cannot be retrieved");
            e.printStackTrace();
        }
        return companies;
    }

    static public void addCompany(String companyTable, String nameColumns, String nameCompany) {

        String addQuery = "INSERT INTO " + companyTable + " (" + nameColumns + ") " +
                "VALUES ('" + nameCompany + "');";

        try {
            ModelDB.makeStatement(addQuery);
        } catch (SQLException e) {
            System.out.println("Data cannot be added");
            e.printStackTrace();
        }
    }

    static public Company getCompany(int companyId) {
        Company company = new Company();

        try (Statement stmt = conn.createStatement()) {
            //get company data from db
            ResultSet companyRes = stmt
                    .executeQuery("SELECT * FROM " + TableName.COMPANY.getName() + " WHERE ID = " + companyId + ";");

            //set company data to object company
            while (companyRes.next()) {
                company.setId(companyRes.getInt("ID"));
                company.setName(companyRes.getString("NAME"));
            }

            //get car data from db
            company.setCarsCompany(CarManagement.getCarsCompany(companyId));

        } catch (SQLException e) {
            System.out.println("Company data cannot be retrieved");
            e.printStackTrace();
        }

        return company;
    }

    static String getCompanyName(int companyId) {
        try {
            ResultSet result = conn.createStatement()
                    .executeQuery("SELECT NAME FROM " + TableName.COMPANY.getName()
                            + " WHERE ID = " + companyId + ";");

            result.next();

            return result.getString("NAME");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Error";
    }
}
