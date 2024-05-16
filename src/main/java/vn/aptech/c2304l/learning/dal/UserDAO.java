package vn.aptech.c2304l.learning.dal;

import vn.aptech.c2304l.learning.constant.UserRole;
import vn.aptech.c2304l.learning.constant.UserStatus;
import vn.aptech.c2304l.learning.context.DBContext;
import vn.aptech.c2304l.learning.model.User;
import vn.aptech.c2304l.learning.utils.BcryptUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserDAO extends DBContext {

    PreparedStatement stm = null;
    ResultSet rs = null;

    private BcryptUtil bcryptUtil = BcryptUtil.getInstance();
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


    public User findByUsername(String username) {
        try {
            String sql = "SELECT * FROM users WHERE username = ?";
            stm = connection.prepareStatement(sql);
            stm.setString(1, username);
            rs = stm.executeQuery();
            if(rs.next()) {
                User user = new User();
                user.setId(rs.getInt(1));
                user.setRole(UserRole.valueOf(rs.getString(2)));
                user.setFullname(rs.getString(3));
                user.setUsername(rs.getString(4));
                user.setPassword(rs.getString(5));
                return user;
            }
        } catch (SQLException e) {
            System.out.println("findByUsername(): " + e.getMessage());
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

    public boolean login(String username, String password) {
        try {
            User user = findByUsername(username);
            if (user != null) {
                return bcryptUtil.checkPassword(password, user.getPassword());
            }
        } catch (Exception e) {
            System.out.println("login(): " + e.getMessage());
        } finally {
            if(stm != null) {
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

