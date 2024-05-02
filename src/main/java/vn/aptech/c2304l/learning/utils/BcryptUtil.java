
package vn.aptech.c2304l.learning.utils;

import org.mindrot.jbcrypt.BCrypt;


public class BcryptUtil {
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
    
    private BcryptUtil() {
    }
    
    private static BcryptUtil instance;
    
    public static BcryptUtil getInstance() {
        if(instance == null) {
            instance = new BcryptUtil();
        }
        return instance;
    }
}
