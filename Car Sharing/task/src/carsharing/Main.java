package carsharing;

public class Main {
    public static void main(String[] args) {
        ModelDB.getInstance().setInitParams(args);
        ModelDB.getInstance().setConnectWithDB();
        ModelDB.getInstance().initDataBase();

        UserPanel panel = new UserPanel();
        panel.startMenu();

        ModelDB.getInstance().closeConnection();
    }
}