package user;

import db.mysql.MySqlHelper;

import java.sql.Connection;
import java.util.Scanner;

public class UserHelper {

    private static Scanner keyboardIn = new Scanner(System.in);


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

        System.out.print("-->");
        String menuOption = keyboardIn.nextLine();

        if ("1".equals(menuOption)) {
            userLogin(liveConnection);
        } else if ("2".equals(menuOption)) {
            userRegistration(liveConnection);
        } else if ("3".equals(menuOption)) {
            return;
        } else {
            System.out.println("Please choose valid option from the menu.");
            userMenu(liveConnection);
        }

    }


    public static void userLogin(Connection liveConnection) {

        System.out.println("--> Login <--");
        System.out.println("");

        System.out.println("Please enter User Name: ");
        System.out.print("-->");
        String userName = keyboardIn.nextLine();

        System.out.println("Please enter User Password: ");
        System.out.print("-->");
        String userPassword = keyboardIn.nextLine();

        if(MySqlHelper.loginUser(liveConnection, userName, userPassword)) {
            System.out.println(userName + ", you have successfully logged in.");
        } else {
            loginOptions(liveConnection, true);
        }

    }

    private static void loginOptions(Connection liveConnection, boolean optionText) {

        if (optionText) {
            System.out.println("You have entered wrong credentials.");
        }
        System.out.println("Press 1 to try again or 2 to return to menu.");

        String loginOption = keyboardIn.nextLine();

        if ("1".equals(loginOption)) {
            userLogin(liveConnection);
        } else if ("2".equals(loginOption)) {
            userMenu(liveConnection);
        } else {
            System.out.println("Please choose valid option.");
            loginOptions(liveConnection, false);
        }

    }


    public static void userRegistration(Connection liveConnection) {


        System.out.println("--> Registration <--");
        System.out.println("");

        System.out.println("Please enter User Name: ");
        System.out.print("-->");
        String userName = keyboardIn.nextLine();

        if (MySqlHelper.findUserByName(liveConnection, userName)) {
            registationOptions(liveConnection, true);
        } else {

            System.out.println("Please enter User Password: ");
            System.out.print("-->");
            String userPassword = keyboardIn.nextLine();

            MySqlHelper.insertUserData(liveConnection, userName, userPassword);

        }

    }


    private static void registationOptions(Connection liveConnection, boolean optionText) {

        if (optionText) {
            System.out.println("Registration failed, that User Name is taken.");
        }

        System.out.println("Press 1 to enter new name or 2 to return to menu.");

        String regOption = keyboardIn.nextLine();

        if ("1".equals(regOption)) {
            userRegistration(liveConnection);
        } else if ("2".equals(regOption)) {
            userMenu(liveConnection);
        } else {
            System.out.println("Please choose valid option.");
            registationOptions(liveConnection, false);
        }

    }

}
