package user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UserHelper {


    public static final String DB_TABLE = "doubledb.userdata";


    public static void userMenu(Connection liveConnection) {

        System.out.println();
        System.out.println("***********************");
        System.out.println("***********************");
        System.out.println("**                   **");
        System.out.println("**   Choose option   **");
        System.out.println("** ----------------- **");
        System.out.println("**  1) User Login    **");
        System.out.println("**  2) Registration  **");
        System.out.println("**  3) Exit          **");
        System.out.println("**                   **");
        System.out.println("***********************");
        System.out.println("***********************");
        System.out.println();

        // TODO - logic for checking for user option

        userRegistration(liveConnection);

    }

    public static void userRegistration(Connection liveConnection) {


        Scanner keyboardIn = new Scanner(System.in);

        System.out.println("--> Registration <--");
        System.out.println("");

        System.out.println("Please enter User Name: ");
        String userName = keyboardIn.nextLine();

        if (findUserByName(liveConnection, userName)) {
            registationOptions(liveConnection, keyboardIn, true);
        }

//        System.out.println("Please enter Password:");
//        String userPassword = keyboardIn.nextLine();

    }

    private static void registationOptions(Connection liveConnection, Scanner keyboardIn, boolean optionText) {

        if (optionText) {
            System.out.println("Registration failed, that User Name is take.");
        }

        System.out.println("Press 1 to enter new name or 2 to return to menu.");


        int regOption = keyboardIn.nextInt();

        if (regOption == 1) {
            userRegistration(liveConnection);
        } else if (regOption == 2) {
            userMenu(liveConnection);
        } else {
            System.out.println("Please choose valid option.");
            registationOptions(liveConnection, keyboardIn, false);
        }

    }

    private static boolean findUserByName(Connection liveConnection, String userName) {

        PreparedStatement findUserName = null;
        ResultSet resultSet;
        boolean check = false;

        String findUserNameQuery = "SELECT * FROM " + DB_TABLE + " WHERE userName = ? COLLATE UTF8_GENERAL_CI";

        try {

            liveConnection.setAutoCommit(false);

            findUserName = liveConnection.prepareStatement(findUserNameQuery);
            findUserName.setString(1, userName);

            System.out.println(findUserName);
            resultSet = findUserName.executeQuery();

            if (resultSet != null) {
                check = true;
            }

            liveConnection.commit();

        } catch (SQLException e) {
            System.out.println("Prepare statement execution failed!");
            e.printStackTrace();

            if (liveConnection != null) {

                System.out.println("Connection is still alive, trying to rolled back transaction.");

                try {
                    liveConnection.rollback();
                } catch (SQLException e1) {
                    System.out.println("Transaction roll back failed!");
                    e1.printStackTrace();
                }

            }

        } finally {

//            No need to close db connection here,
//            we are doing it later with disconnect method

            try {

                if (findUserName != null) {
                    findUserName.close();
                }

            } catch (SQLException e) {
                System.out.println("Failed to close prepare statement and/or connection!");
                e.printStackTrace();
            }

        }

        return check;

    }

    public void userLogin(String userName, String userPassword) {

    }

}
