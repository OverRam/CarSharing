package carsharing;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDaoImpl implements CompanyDao {
    private static CompanyDaoImpl companyDaoImpl;
    Connection conn;

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
        if (!Files.exists(Paths.get("./src/carsharing/db/" + dataBaseName + ".mv.db"))) {
            String tableName = "COMPANY";
            String columns = "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "NAME VARCHAR(200) UNIQUE NOT NULL";
            createTable(tableName, columns);
        }
    }

    @Override
    public boolean createTable(String tableName, String columnsQuery) {
        String statement = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + columnsQuery + ");";
        System.out.println(makeStatement(statement));
        return makeStatement(statement);
    }

    private boolean makeStatement(String query) {
        boolean isDone = true;
        try {
            Statement st = conn.createStatement();
            st.executeUpdate(query);
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            isDone = false;
        }

        return isDone;
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
            e.printStackTrace();
            return null;
        }
        return companies;
    }

    @Override
    public boolean addCompany(String tableName, String nameColumns, String valueOfColumnsInOrderNameColumns) {
        String addQuery = "INSERT INTO " + tableName + " (" + nameColumns + ") " +
                "VALUES (" + valueOfColumnsInOrderNameColumns + ");";
        return makeStatement(addQuery);
    }

    @Override
    public void updateCompany(String tableName, int id, String dataToChange) {
        String updateQuery = "UPDATE " + tableName + " SET " + dataToChange + " WHERE ID = " + id + ";";
        makeStatement(updateQuery);
    }

    @Override
    public void deleteCompany(String tableName, int id) {
        String deleteQuery = "DELETE FROM " + tableName + " WHERE ID = " + id;
        makeStatement(deleteQuery);
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
            e.printStackTrace();
        }

        return company;
    }
}