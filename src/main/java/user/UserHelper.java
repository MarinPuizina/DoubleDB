package user;

import db.mysql.MySqlHelper;

import javax.crypto.SecretKey;
import java.sql.Connection;
import java.util.Base64;
import java.util.Scanner;

public class UserHelper {

    private static Scanner keyboardIn = new Scanner(System.in);

    private MySqlHelper mySqlHelper = new MySqlHelper();


    public void userMenu(Connection liveConnection) {

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


    private void userLogin(Connection liveConnection) {

        System.out.println("--> Login <--");
        System.out.println("");

        System.out.println("Please enter User Name: ");
        System.out.print("-->");
        String userName = keyboardIn.nextLine();

        System.out.println("Please enter User Password: ");
        System.out.print("-->");
        String userPassword = keyboardIn.nextLine();

        if (mySqlHelper.findUserByName(liveConnection, userName)) {

            int userID = mySqlHelper.getUserID(liveConnection, userName);

            String userSecretKey = mySqlHelper.getUserSecretKey(liveConnection, userID);
            SecretKey secretKey = mySqlHelper.convertStringToSecretKey(userSecretKey);
            mySqlHelper.setSecretEncryptKey(secretKey);

            byte[] encryptedPassword = mySqlHelper.encryptPasswordUsingSavedSecretKey(userPassword);

            userPassword = Base64.getEncoder().encodeToString(encryptedPassword);

            if(mySqlHelper.loginUser(liveConnection, userName, userPassword)) {
                System.out.println(userName + ", you have successfully logged in.");
            } else {
                loginOptions(liveConnection, true);
            }

        } else {
            loginOptions(liveConnection, true);
        }

    }

    private void loginOptions(Connection liveConnection, boolean optionText) {

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


    private void userRegistration(Connection liveConnection) {


        System.out.println("--> Registration <--");
        System.out.println("");

        System.out.println("Please enter User Name: ");
        System.out.print("-->");
        String userName = keyboardIn.nextLine();

        if (mySqlHelper.findUserByName(liveConnection, userName)) {
            registationOptions(liveConnection, true);
        } else {

            System.out.println("Please enter User Password: ");
            System.out.print("-->");
            String userPassword = keyboardIn.nextLine();

            String encryptedPassword = Base64.getEncoder().encodeToString(mySqlHelper.encryptPassword(userPassword));
            String encryptedSecretKey = mySqlHelper.convertSecretKeyToString(mySqlHelper.getSecretEncryptKey());

            mySqlHelper.insertUserData(liveConnection, userName, encryptedPassword, encryptedSecretKey);

        }

    }


    private void registationOptions(Connection liveConnection, boolean optionText) {

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
