package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    Connection connection;

    public static void main(String[] args) {
        Main main = new Main();

        main.registerDriverClass();
        String nameOfDataBase = "DataBaseCar";
        String patchDataBase = ".\\src\\carsharing\\db\\";

        for (int i = 0; i < args.length; i += 2) {
            System.out.println(args[i]);
            if (args[i].equals("-databaseFileName")) {
                nameOfDataBase = args[i + 1];
            }
        }

        String URL_DataBase = patchDataBase + nameOfDataBase;

        CompanyDaoImpl companyDaoImpl = CompanyDaoImpl.getInstance();
        companyDaoImpl.setConnection(main.getConnectionWithDB(URL_DataBase));
        companyDaoImpl.initDataBase(nameOfDataBase);
        //  companyDaoImpl.addCompany("COMPANY", "NAME", "'EWF32133'");
        companyDaoImpl.getAllCompany("COMPANY").forEach(x -> System.out.println("name = " + x.getName() +
                " id = " + x.getId()));

        System.out.println("--------------");
        companyDaoImpl.deleteCompany("COMPANY", 1);
        companyDaoImpl.getAllCompany("COMPANY").forEach(x -> System.out.println("name = " + x.getName() +
                " id = " + x.getId()));

        main.closeConnection();
    }

    boolean closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    Connection getConnectionWithDB(String URL_DataBase) {
        Connection connection;
        try {
            connection = DriverManager.getConnection("jdbc:h2:" + URL_DataBase, "sa", "");
            connection.setAutoCommit(true);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    void registerDriverClass() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}