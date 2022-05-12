package carsharing;

import java.util.List;

public interface CompanyDao {

    /**
     * @return a list of all objects that are in the table or null if is error
     */
    List<Company> getAllCompany(String tableName);

    /**
     * @param tableName                        The name of the table in which we will make changes
     * @param nameColumns                      The columns we want to add value to. Exp: name, id, code
     * @param valueOfColumnsInOrderNameColumns Column values in the order given in "nameColumns". Exp: 'mark', 10, 23
     *                                         * We give varchar values in single quotes. Exp 'Tom'
     * @return true if done else false
     */
    boolean addCompany(String tableName, String nameColumns, String valueOfColumnsInOrderNameColumns);

    /**
     * @param tableName    The name of the table in which we will make changes
     * @param id           ID number where we want to update
     * @param dataToChange You can enter any number of existing columns separated by a comma Exp: ID = 1, NAME = 'Tom'
     */
    void updateCompany(String tableName, int id, String dataToChange);

    /**
     * @param id The ID number we want to delete
     */
    void deleteCompany(String tableName, int id);

    /**
     * @param id The ID number we want to access
     * @return returns the column values of the given id
     */
    Company getCompany(String tableName, int id);

    /**
     * @param tableName    The name of the new table
     * @param columnsQuery The values of the new table, example: ID INT, NAME VARCHAR(20),
     *                     CITY VARCHAR(10) NOT NULL UNIQUE,
     * @return If table exist return false
     */
    boolean createTable(String tableName, String columnsQuery);
}
