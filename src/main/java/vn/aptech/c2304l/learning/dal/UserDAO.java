package vn.aptech.c2304l.learning.dal;

import vn.aptech.c2304l.learning.constant.UserRole;
import vn.aptech.c2304l.learning.constant.UserStatus;
import vn.aptech.c2304l.learning.context.DBContext;
import vn.aptech.c2304l.learning.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserDAO extends DBContext {

    PreparedStatement stm = null;
    ResultSet rs = null;

    public boolean registerUser(User user) {
        try {
            String sql = "INSERT INTO users (role, fullname, username, password, status) VALUES (?, ?, ?, ?, ?);";
            stm = connection.prepareStatement(sql);
            stm.setString(1, user.getRole().toString());
            stm.setString(2, user.getFullname());
            stm.setString(3, user.getUsername());
            stm.setString(4, user.getPassword());
            stm.setString(5, user.getStatus().toString());
            stm.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("registerUser(): " + e.getMessage());
            return false;
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public boolean checkUsernameExists(String username) {
        try {
            String sql = "SELECT * FROM users WHERE username = ?";
            stm = connection.prepareStatement(sql);
            stm.setString(1, username);
            ResultSet resultSet = stm.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println("checkUsernameExists(): " + e.getMessage());
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return false;
    }


}

