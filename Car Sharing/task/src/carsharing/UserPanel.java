package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class UserPanel {
    private Connection connection;
    private String nameOfDataBase = "carsharing";
    private final CompanyDaoImpl companyDaoImpl;
    private final Scanner sc = new Scanner(System.in);

    public UserPanel() {
        String patchDataBase = ".\\src\\carsharing\\db\\";
        String URL_DataBase = patchDataBase + nameOfDataBase;
        registerDriverClass();
        companyDaoImpl = CompanyDaoImpl.getInstance();
        companyDaoImpl.setConnection(connectWithDB(URL_DataBase));
        companyDaoImpl.initDataBase(nameOfDataBase);
    }

    void setInitParams(String[] arr) {

        for (int i = 0; i < arr.length; i += 2) {
            System.out.println(arr[i]);
            if (arr[i].equals("-databaseFileName")) {
                nameOfDataBase = arr[i + 1];
            }
        }
    }

    private void registerDriverClass() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection connectWithDB(String URL_DataBase) {
        try {
            connection = DriverManager.getConnection("jdbc:h2:" + URL_DataBase, "", "");
            connection.setAutoCommit(true);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void startMenu() {
        String choice;
        do {
            System.out.println("1. Log in as a manager\n" +
                    "0. Exit");
            choice = sc.nextLine();
            switch (choice) {
                case "1":
                    System.out.println();
                    managerMenu();
                    break;
                case "0":
                    break;
                default:
                    System.out.println("Wrong choice try again");
                    startMenu();
            }
        } while (!"0".equals(choice));

        closeConnection();
    }

    private void managerMenu() {
        String choice;
        do {
            System.out.println("1. Company list\n" +
                    "2. Create a company\n" +
                    "0. Back");
            choice = sc.nextLine();
            System.out.println();
            String companyTable = "COMPANY";

            switch (choice) {
                case "1":
                    List<Company> companyList = companyDaoImpl.getAllCompany(companyTable);
                    if (companyList.size() < 1) {
                        System.out.println("The company list is empty!");
                    } else {
                        System.out.println("Company list:");
                        companyList.forEach(c -> System.out.println(c.getId() + ". " + c.getName() + " company"));
                    }
                    System.out.println();
                    break;
                case "2":
                    System.out.println("Enter the company name:");
                    companyDaoImpl.addCompany(companyTable, "NAME", sc.nextLine());
                    System.out.println("The company was created!\n");
                    break;
                case "0":
                    break;
                default:
                    System.out.println("Wrong choice try again.");
                    managerMenu();
            }
        } while (!"0".equals(choice));

    }
}
