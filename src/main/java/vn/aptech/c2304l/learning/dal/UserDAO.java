package vn.aptech.c2304l.learning.dal;

import vn.aptech.c2304l.learning.context.DBContext;
import vn.aptech.c2304l.learning.model.User;
import vn.aptech.c2304l.learning.utils.BcryptUtil;

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
            rs = stm.executeQuery();
            if(rs.next()) {
                return true;
            }

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

    BcryptUtil bcryptUtil = BcryptUtil.getInstance();

    public boolean checkPassword(String username, String password, String status) {
        try {
            String sql = "SELECT * FROM users WHERE username = ? AND status = ?";
            stm =connection.prepareStatement(sql);
            stm.setString(1, username);
            stm.setString(2, status);
            rs = stm.executeQuery();
            if(rs.next()) {
                if(bcryptUtil.checkPassword(password, rs.getString("password"))) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("checkPassword(): " + e.getMessage());
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



    public void changePassword(String username, String password) {

        try {
            String sql = "UPDATE users SET password = ? WHERE username = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, password);
            stm.setString(2, username);

            int rowsAffected = stm.executeUpdate(); // Thực hiện câu lệnh UPDATE và trả về số dòng bị ảnh hưởng

            // Kiểm tra xem có dòng nào bị ảnh hưởng hay không
            if (rowsAffected > 0) {
            }
        } catch (SQLException e) {
            System.out.println("changePassword(): " + e.getMessage());
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


}


