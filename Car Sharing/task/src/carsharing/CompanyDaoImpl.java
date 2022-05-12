package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompanyDaoImpl implements CompanyDao {
    private static CompanyDaoImpl companyDaoImpl;
    private Connection conn;

    private CompanyDaoImpl() {
    }

    public void setConnection(Connection connectionWithDB) {
        conn = connectionWithDB;
    }

    /**
     * After creating an instance, use the setConnection method
     */
    static CompanyDaoImpl getInstance() {
        if (companyDaoImpl == null) {
            companyDaoImpl = new CompanyDaoImpl();
        }
        return companyDaoImpl;
    }

    /**
     * If there is no database, it creates it together with a table named COMPANY.
     *
     * @param dataBaseName Database name.
     */
    void initDataBase(String dataBaseName) {
        String tableName = "COMPANY";
        String columns = "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                "NAME VARCHAR(200) UNIQUE NOT NULL";
        createTable(tableName, columns);
    }

    @Override
    public void createTable(String tableName, String columnsQuery) {
        String statement = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + columnsQuery + ");";
        try {
            makeStatement(statement);
        } catch (SQLException e) {
            System.out.println("A table with the given name exists");
        }
    }

    private void makeStatement(String query) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate(query);
        st.close();
    }

    /**
     * @param tableName - Table name
     * @return - true if exist
     */
    boolean isExistTable(String tableName) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT count(*) "
                    + "FROM information_schema.tables "
                    + "WHERE table_name = ?"
                    + "LIMIT 1;");

            preparedStatement.setString(1, tableName);

            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            return resultSet.getInt(1) != 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Company> getAllCompany(String tableName) {
        List<Company> companies = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + ";");

            while (rs.next()) {
                int idNum = rs.getInt("ID");
                String name = rs.getString("NAME");
                companies.add(new Company(idNum, name));
            }

        } catch (SQLException e) {
            System.out.println("Data cannot be retrieved");
            e.printStackTrace();
            return null;
        }
        return companies;
    }

    @Override
    public boolean addCompany(String tableName, String nameColumns, String valueOfColumnsInOrderNameColumns) {
        String prepareValues = Arrays.stream(valueOfColumnsInOrderNameColumns.split(","))
                .map(x -> "'" + x + "'")
                .reduce((a, b) -> a + ", " + b).orElse("errorString");

        String addQuery = "INSERT INTO " + tableName + " (" + nameColumns + ") " +
                "VALUES (" + prepareValues + ");";
        try {
            makeStatement(addQuery);
        } catch (SQLException e) {
            System.out.println("A new company cannot be added");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void updateCompany(String tableName, int id, String dataToChange) {
        String updateQuery = "UPDATE " + tableName + " SET " + dataToChange + " WHERE ID = " + id + ";";
        try {
            makeStatement(updateQuery);
        } catch (SQLException e) {
            System.out.println("The company details cannot be updated");
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCompany(String tableName, int id) {
        String deleteQuery = "DELETE FROM " + tableName + " WHERE ID = " + id;
        try {
            makeStatement(deleteQuery);
        } catch (SQLException e) {
            System.out.println("The company cannot be removed");
            e.printStackTrace();
        }
    }

    @Override
    public Company getCompany(String tableName, int id) {
        Company company = null;
        Statement stmt;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " WHERE ID = " + id + " ;");

            while (rs.next()) {
                int idNum = rs.getInt("ID");
                String name = rs.getString("NAME");
                company = new Company(idNum, name);
            }

        } catch (SQLException e) {
            System.out.println("Company data cannot be retrieved");
            e.printStackTrace();
        }

        return company;
    }
}