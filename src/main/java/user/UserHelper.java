package user;

public class UserHelper {

    public static void userMenu() {

        System.out.println();
        System.out.println("***********************");
        System.out.println("***********************");
        System.out.println("**                   **");
        System.out.println("**   Choose option   **");
        System.out.println("** ----------------- **");
        System.out.println("**  1) User Login    **");
        System.out.println("**  2) Registration  **");
        System.out.println("**                   **");
        System.out.println("***********************");
        System.out.println("***********************");
        System.out.println();

    }

    public void userRegistration(String userName, String userPassword) {

        // TODO - query to search if we have user with that name in db
        


        // TODO - if we don't then register the user if we do, ask for correct name

    }

    public void  userLogin(String userName, String userPassword) {

    }

}
