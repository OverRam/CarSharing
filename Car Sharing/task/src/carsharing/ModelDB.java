package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ModelDB {
    private static ModelDB model;
    private static Connection connection;
    private String nameOfDataBase = "carsharing";

    private ModelDB() {
        registerDriverClass();
    }

    /**
     * After creating an instance, use the setConnection method
     */
    static ModelDB getInstance() {
        if (model == null) {
            model = new ModelDB();
        }
        return model;
    }

    /**
     * If there is no database, it creates it together with a table named COMPANY.
     */
    public void initDataBase() {
        String companyColumns = "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                "NAME VARCHAR(50) UNIQUE NOT NULL";
        createTable(TableName.COMPANY.getName(), companyColumns);

        String carColumns = "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                "NAME VARCHAR(50) UNIQUE NOT NULL, " +
                "COMPANY_ID INT NOT NULL, " +
                "FOREIGN KEY (COMPANY_ID) REFERENCES " + TableName.COMPANY.getName() + "(ID)";
        createTable(TableName.CAR.getName(), carColumns);

        String consumerColumns = "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                "NAME VARCHAR(50) UNIQUE NOT NULL , " +
                "RENTED_CAR_ID INT DEFAULT NULL, " +
                "FOREIGN KEY (RENTED_CAR_ID) REFERENCES " + TableName.CAR.getName() + " (ID) " +
                "ON DELETE SET NULL";
        createTable(TableName.CUSTOMER.getName(), consumerColumns);
    }

    private void createTable(String tableName, String columnsQuery) {
        String statement = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + columnsQuery + ");";
        try {
            makeStatement(statement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("A table with the given name = " + tableName + " exists");
        }
    }

    static void makeStatement(String query) throws SQLException {
        Statement st = connection.createStatement();
        st.executeUpdate(query);
        st.close();
    }

    void setConnectWithDB() {
        String patchDataBase = ".\\src\\carsharing\\db\\";
        String URL_DataBase = patchDataBase + nameOfDataBase;
        try {
            connection = DriverManager.getConnection("jdbc:h2:" + URL_DataBase, "", "");
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    Connection getConnection() {
        if (connection == null) {
            setConnectWithDB();
        }
        return connection;
    }

    private void registerDriverClass() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    void setInitParams(String[] arr) {

        for (int i = 0; i < arr.length; i += 2) {
            System.out.println(arr[i]);
            if (arr[i].equals("-databaseFileName")) {
                nameOfDataBase = arr[i + 1];
            }
        }
    }

    void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}