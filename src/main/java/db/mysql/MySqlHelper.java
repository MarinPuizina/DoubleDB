package db.mysql;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

public class MySqlHelper {

    private SecretKey secretEncryptKey;

    private static final String AES = "AES";
    private static final String DB_USERDATA_TABLE = "doubledb.userdata";
    private static final String DB_KEYS_TABLE = "doubledb.keys";
    private static final String QUERY_FIND_USER_NAME = "SELECT * FROM " + DB_USERDATA_TABLE + " WHERE userName = ? COLLATE UTF8_GENERAL_CI";
    private static final String QUERY_LOGIN_USER = "SELECT * FROM " + DB_USERDATA_TABLE + " WHERE userName = ? AND userPassword = ?";
    private static final String QUERY_GET_USERID = "SELECT userID FROM " + DB_USERDATA_TABLE + " WHERE userName = ? AND userPassword = ?";
    private static final String QUERY_INSERT_USER_DATA = "INSERT INTO " + DB_USERDATA_TABLE + " (userName, userPassword) VALUES (?,?)";
    private static final String QUERY_INSERT_SECRET_KEY = "INSERT INTO " + DB_KEYS_TABLE + " (userID, secretKey) VALUES (?,?)";
    private static final String QUERY_GET_USERID_BY_NAME = "SELECT userID FROM " + DB_USERDATA_TABLE + " WHERE userName = ?";
    private static final String QUERY_GET_USER_SECRETKEY = "SELECT secretKey FROM " + DB_KEYS_TABLE + " WHERE userID = ?";

    /**
     * Prepared statement for finding if user exists in database.
     *
     * @param liveConnection connection to MySQL DB.
     * @param userName       name of the user that we want to find.
     */
    public boolean findUserByName(Connection liveConnection, String userName) {

        PreparedStatement findUserName = null;
        ResultSet resultSet;
        boolean check = false;

        try {

            liveConnection.setAutoCommit(false);

            findUserName = liveConnection.prepareStatement(QUERY_FIND_USER_NAME);
            findUserName.setString(1, userName);

            resultSet = findUserName.executeQuery();

            if (resultSet.next()) {
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

            // No need to close db connection here,
            // we are doing it later with disconnect method

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


    /**
     * Prepared statement for inserting User Name and Password.
     *
     * @param liveConnection connection to MySQL DB.
     * @param userName       name of the user that we are storing in DB.
     * @param userPassword   user's password that we are storing in DB, make sure this is encrypted.
     * @param secretKey      the key used for decrypting the password.
     */
    public static void insertUserData(Connection liveConnection, String userName, String userPassword, String secretKey) {

        PreparedStatement insertUserData = null;
        PreparedStatement getUserID = null;
        PreparedStatement insertSecretKey = null;

        ResultSet resultSet;

        try {

            liveConnection.setAutoCommit(false);

            insertUserData = liveConnection.prepareStatement(QUERY_INSERT_USER_DATA);
            insertUserData.setString(1, userName);
            insertUserData.setString(2, userPassword);
            insertUserData.execute();

            getUserID = liveConnection.prepareStatement(QUERY_GET_USERID);
            getUserID.setString(1, userName);
            getUserID.setString(2, userPassword);
            resultSet = getUserID.executeQuery();

            int userID = 0;
            while (resultSet.next()) {
                userID = resultSet.getInt("userID");
            }
            if (userID == 0) {
                throw new UserIdIsZeroExeption("UserID is zero.");
            }

            insertSecretKey = liveConnection.prepareStatement(QUERY_INSERT_SECRET_KEY);
            insertSecretKey.setInt(1, userID);
            insertSecretKey.setString(2, secretKey);
            insertSecretKey.execute();

            liveConnection.commit();

        } catch (SQLException e) {
            System.out.println("Prepare statement execution failed!");
            e.printStackTrace();

            if (liveConnection != null) {

                System.out.println("Connection is still alive, trying to rolled back transaction.");

                try {
                    liveConnection.rollback();
                } catch (SQLException e2) {
                    System.out.println("Transaction roll back failed!");
                    e2.printStackTrace();
                }

            }

        } catch (UserIdIsZeroExeption userIdIsZeroExeption) {
            userIdIsZeroExeption.printStackTrace();
        } finally {

            // No need to close db connection here,
            // we are doing it later with disconnect method

            try {

                if (insertUserData != null) {
                    insertUserData.close();
                }
                if (getUserID != null) {
                    getUserID.close();
                }
                if (insertSecretKey != null) {
                    insertSecretKey.close();
                }

            } catch (SQLException e) {
                System.out.println("Failed to close prepare statement and/or connection!");
                e.printStackTrace();
            }

        }

    }


    /**
     * Prepared statement for finding if user exists in database.
     *
     * @param liveConnection connection to MySQL DB
     * @param userName       name of the user that we want to find
     * @return Boolean - validating if you managed to find the user.
     */
    public boolean loginUser(Connection liveConnection, String userName, String userPassword) {

        PreparedStatement findLoginUser = null;
        ResultSet resultSet;
        boolean check = false;

        try {

            liveConnection.setAutoCommit(false);

            findLoginUser = liveConnection.prepareStatement(QUERY_LOGIN_USER);
            findLoginUser.setString(1, userName);
            findLoginUser.setString(2, userPassword);

            resultSet = findLoginUser.executeQuery();

            if (resultSet.next()) {
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

            // No need to close db connection here,
            // we are doing it later with disconnect method

            try {

                if (findLoginUser != null) {
                    findLoginUser.close();
                }

            } catch (SQLException e) {
                System.out.println("Failed to close prepare statement and/or connection!");
                e.printStackTrace();
            }

        }

        return check;

    }


    /**
     * Generating secretKey for password encryption then encrypting the password.
     * Use this method when you want to store a password in DB for first time.
     *
     * @param userPassword User's password.
     * @return Encrypted password.
     */
    public byte[] encryptPassword(String userPassword) {

        byte[] encryptedPassword = null;

        try {

            KeyGenerator keyGen = KeyGenerator.getInstance(AES);
            this.secretEncryptKey = keyGen.generateKey();

            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE, this.secretEncryptKey);

            encryptedPassword = cipher.doFinal(userPassword.getBytes());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return encryptedPassword;

    }


    /**
     * Method for encrypting user's password using AES encryption.
     *
     * @param userPassword User's password for encryption.
     * @return Encrypted password using AES encryption.
     */
    public byte[] encryptPasswordUsingSavedSecretKey(String userPassword) {

        byte[] encryptedPassword = null;

        try {

            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE, this.secretEncryptKey);

            encryptedPassword = cipher.doFinal(userPassword.getBytes());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return encryptedPassword;

    }


    /**
     * Method for decrypting user's password using AES encryption.
     *
     * @param encryptedPassword encrypted user's password.
     * @param key               secret key used in decryption.
     * @return Decrypted password.
     */
    public String decryptPassword(byte[] encryptedPassword, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(AES);

        byte[] keyBytes = key.getEncoded();
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, AES);

        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] plainPassword = cipher.doFinal(encryptedPassword);
        String decryptedPassword = new String(plainPassword);
        System.out.println(decryptedPassword);

        return decryptedPassword;

    }


    /**
     * Method for converting String to SecretKey.
     *
     * @param stringToConvert String used for converting to SecretKey.
     * @return String converted to SecretKey.
     */
    public SecretKey convertStringToSecretKey(String stringToConvert) {

        byte[] decodedKey = Base64.getDecoder().decode(stringToConvert);

        SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, AES);

        return secretKey;

    }


    /**
     * Method for converting SecretKey to String.
     *
     * @param secretKeyToConvert SecretKey used for converting to String.
     * @return SecretKey converted to String.
     */
    public String convertSecretKeyToString(SecretKey secretKeyToConvert) {

        byte[] encodedSecretKey = secretKeyToConvert.getEncoded();

        String secretKey = Base64.getEncoder().encodeToString(encodedSecretKey);

        return secretKey;

    }


    /**
     * Method for finding user's ID.
     *
     * @param liveConnection connection to MySQL DB.
     * @param userName       for finding the user's ID.
     * @return user's ID.
     */
    public int getUserID(Connection liveConnection, String userName) {

        int userID = 0;

        PreparedStatement findUserID = null;
        ResultSet resultSet;

        try {

            liveConnection.setAutoCommit(false);

            findUserID = liveConnection.prepareStatement(QUERY_GET_USERID_BY_NAME);
            findUserID.setString(1, userName);

            resultSet = findUserID.executeQuery();

            while (resultSet.next()) {
                userID = resultSet.getInt("userID");
            }
            if (userID == 0) {
                throw new UserIdIsZeroExeption("UserID is zero.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UserIdIsZeroExeption userIdIsZeroExeption) {
            userIdIsZeroExeption.printStackTrace();
        }

        return userID;

    }


    /**
     * Method for finding user's SecretKey.
     *
     * @param liveConnection connection to MySQL DB.
     * @param userID         user's ID.
     * @return User's SecretKey used for password decryption.
     */
    public String getUserSecretKey(Connection liveConnection, int userID) {

        String userSecretKey = null;

        PreparedStatement findUserID;
        ResultSet resultSet;

        try {

            liveConnection.setAutoCommit(false);

            findUserID = liveConnection.prepareStatement(QUERY_GET_USER_SECRETKEY);
            findUserID.setInt(1, userID);

            resultSet = findUserID.executeQuery();

            while (resultSet.next()) {
                userSecretKey = resultSet.getString("secretKey");
            }
            if ("".equals(userSecretKey)) {
                throw new SecretKeyIsExeption("secretKey is empty or null");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (SecretKeyIsExeption secretKeyIsExeption) {
            secretKeyIsExeption.printStackTrace();
        }

        return userSecretKey;

    }


    // CUSTOM EXEPCTION
    private static class UserIdIsZeroExeption extends Exception {

        public UserIdIsZeroExeption(String message) {
            super(message);
        }

    }


    private static class SecretKeyIsExeption extends Exception {

        public SecretKeyIsExeption(String message) {
            super(message);
        }

    }


    // GETTERS AND SETTERS
    public SecretKey getSecretEncryptKey() {
        return secretEncryptKey;
    }

    public void setSecretEncryptKey(SecretKey secretEncryptKey) {
        this.secretEncryptKey = secretEncryptKey;
    }


}
