package carsharing;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UserPanel {
    private final Scanner sc = new Scanner(System.in);

    public UserPanel() {
    }

    public void startMenu() {
        String choice;
        do {
            System.out.println("1. Log in as a manager\n" +
                    "2. Log in as a customer\n" +
                    "3. Create a customer\n" +
                    "0. Exit");

            choice = sc.nextLine();

            if (!choice.equals("0")) {
                System.out.println();
            }

            switch (choice) {
                case "0":
                    break;

                case "1":
                    managerMenu();
                    break;

                case "2":
                    Map<Integer, Customer> customerMap = CustomerManagement.getAllCustomer();

                    if (customerMap.size() < 1) {
                        System.out.println("The customer list is empty!");
                    } else {
                        customerMap.forEach((k, v) -> System.out.println(k + ". " + v.getName() + " customer"));
                        int customerNum = parseNumber(sc.nextLine());
                        System.out.println();
                        customerMenu(customerMap.get(customerNum).getId());
                    }
                    break;

                case "3":
                    System.out.println("Enter the customer name:");
                    String name = sc.nextLine();
                    CustomerManagement.addCustomer(new Customer(name));
                    System.out.println();
                    break;

                default:
                    System.out.println("Wrong choice try again");
                    startMenu();
            }
        } while (!"0".equals(choice));
    }

    private void managerMenu() {
        String choice;
        do {
            System.out.println("1. Company list\n" +
                    "2. Create a company\n" +
                    "0. Back");
            choice = sc.nextLine();
            System.out.println();

            switch (choice) {
                case "1":
                    Map<Integer, Company> companyMap = CompanyManagement.getAllCompany();

                    if (companyMap.size() < 1) {
                        System.out.println("The company list is empty!\n");
                    } else {
                        System.out.println("Choose the company:");
                        companyMap.forEach((k, v) -> System.out.println(k + ". " + v.getName() + " company"));
                        System.out.println("0. Back");

                        int choiceID = parseNumber(sc.nextLine());

                        if (choiceID > 0 && choiceID <= companyMap.size()) {
                            carCompanyMenu(companyMap.get(choiceID));
                        }
                    }
                    break;
                case "2":
                    System.out.println("Enter the company name:");
                    CompanyManagement.addCompany(TableName.COMPANY.getName(), "NAME", sc.nextLine());
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

    private void customerMenu(int customerID) {
        int choice;
        do {
            System.out.println("1. Rent a car\n" +
                    "2. Return a rented car\n" +
                    "3. My rented car\n" +
                    "0. Back");
            choice = parseNumber(sc.nextLine());
            System.out.println();
            Customer customer = CustomerManagement.getCustomer(customerID);

            switch (choice) {
                case 1:
                    if (customer.getRentedCarID() == 0) {
                        System.out.println("Choose a company:");

                        // get all company
                        Map<Integer, Company> availableCompany = CompanyManagement.getAllCompany();
                        availableCompany.forEach((n, c) -> System.out.println(n + ". " + c.getName()));
                        System.out.println("0. Back");

                        int companyNum = parseNumber(sc.nextLine());
                        //choice company
                        if (companyNum != 0) {
                            int companyID = availableCompany.get(companyNum).getId();
                            System.out.println();

                            //get all cars
                            HashMap<Integer, CarCompany> availableCars = CarManagement
                                    .getFreeCarsFromCompany(companyID);

                            if (availableCars.size() < 1) {
                                System.out.println("No available cars in the '" + availableCompany.get(companyNum).getName()
                                        + "' company");


                            } else {
                                //choice car
                                availableCars.forEach((n, c) -> System.out.println(n + ". " + c.getName()));
                                System.out.println("0. Back");

                                int carNum = parseNumber(sc.nextLine());
                                System.out.println();
                                if (carNum != 0) {
                                    CarCompany car = availableCars.get(carNum);
                                    CarManagement.rentCar(car, customer);
                                    System.out.println("You rented '" + car.getName() + "'\n");
                                }
                            }
                        }


                    } else {
                        System.out.println("You've already rented a car!\n");
                    }
                    break;

                case 2:
                    if (customer.getRentedCarID() == 0) {
                        System.out.println("You didn't rent a car!\n");
                    } else {
                        CarManagement.returnCar(customer.getId());
                        System.out.println("You've returned a rented car!\n");
                    }
                    break;

                case 3:
                    if (customer.getRentedCarID() == 0) {
                        System.out.println("You didn't rent a car!\n");

                    } else {
                        CarCompany car = CarManagement.getCar(customer.getRentedCarID());
                        if (car != null) {
                            System.out.println("Your rented car:\n" +
                                    car.getName() +
                                    "\nCompany:\n" +
                                    CompanyManagement.getCompanyName(car.getCompanyID()) + "\n");
                        } else {
                            System.out.println("Car don't exist!");
                        }

                    }
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Wrong choice");
            }
        } while (choice != 0);
    }

    private void carCompanyMenu(Company company) {
        String choice;
        System.out.println("'" + company.getName() + "' company");

        do {
            System.out.println("1. Car list\n" +
                    "2. Create a car\n" +
                    "0. Back");
            choice = sc.nextLine();
            System.out.println();

            int choiceNumber = parseNumber(choice);

            switch (choiceNumber) {
                case 0:
                    break;

                case 1:
                    Company companyInfo = CompanyManagement.getCompany(company.getId());

                    if (companyInfo.getCarsCompany().size() > 0) {

                        companyInfo.getCarsCompany()
                                .forEach((i, c) -> System.out.println(i + ". " + c.getName()));
                    } else {
                        System.out.println("The car list is empty!");
                    }
                    break;

                case 2:
                    System.out.println("Enter the car name:");

                    String carName = sc.nextLine();
                    CarManagement.addCarToCompany(new CarCompany(carName, company.getId()));
                    System.out.println("The car was added!");
                    break;

                default:
                    System.out.println("Wrong choice");
            }
            System.out.println();
        } while (!"0".equals(choice));
    }

    private int parseNumber(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            System.out.println("This is not number! Try again.");
        }
        return -1;
    }
}
