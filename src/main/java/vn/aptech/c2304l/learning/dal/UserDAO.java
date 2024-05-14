package vn.aptech.c2304l.learning.dal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import vn.aptech.c2304l.learning.constant.UserRole;
import vn.aptech.c2304l.learning.constant.UserStatus;
import vn.aptech.c2304l.learning.context.DBContext;
import vn.aptech.c2304l.learning.model.Category;
import vn.aptech.c2304l.learning.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserDAO extends DBContext {

    PreparedStatement stm = null;
    ResultSet rs = null;

    public ObservableList<User> findAll() {
        ObservableList<User> users = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM users";
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt(1));
                String roleString = rs.getString(2);
                if (roleString != null) {
                    user.setRole(UserRole.valueOf(roleString.toUpperCase()));
                }
                user.setFullname(rs.getString(3));
                user.setUsername(rs.getString(4));
                user.setPassword(rs.getString(5));
                String statusString = rs.getString(6);
                if (statusString != null) {
                    user.setStatus(UserStatus.valueOf(statusString.toUpperCase()));
                }
                users.add(user);
            }
            return users;
        } catch (Exception e) {
            System.out.println("findAll(): " + e.getMessage());
        } finally {
            if(stm != null) {
                try {
                    stm.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

    public ObservableList<User> findByUsernameAndStatus(String username, String status) {
        ObservableList<User> users = FXCollections.observableArrayList();

        try {
            String sql = "";

            if(status == "ACTIVE" || status == "INACTIVE") {
                sql = "SELECT * FROM users WHERE username LIKE ? and status = ?";
                stm = connection.prepareStatement(sql);
                stm.setString(1, "%" + username + "%");
                stm.setString(2, status);
            }
            else {
                sql = "SELECT * FROM users WHERE username LIKE ?";
                stm = connection.prepareStatement(sql);
                stm.setString(1, "%" + username + "%");
            }
            rs = stm.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt(1));
                String roleString = rs.getString(2);
                if (roleString != null) {
                    user.setRole(UserRole.valueOf(roleString.toUpperCase()));
                }
                user.setFullname(rs.getString(3));
                user.setUsername(rs.getString(4));
                user.setPassword(rs.getString(5));
                String statusString = rs.getString(6);
                if (statusString != null) {
                    user.setStatus(UserStatus.valueOf(statusString.toUpperCase()));
                }
                users.add(user);
            }
            return users;
        } catch (Exception e) {
            System.out.println("handleSearchAction(): " + e.getMessage());
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

    public String checkLogin(String username, String pass) {
        try {
            String sql = "SELECT role FROM users WHERE username = ? AND password = ?";
            stm = connection.prepareStatement(sql);
            stm.setString(1, username);
            stm.setString(2, pass);
            ResultSet resultSet = stm.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("role");
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
        return null;
    }

    public boolean update(User user) {
        try {
            String sql = "UPDATE `users`\n" +
                    "SET\n" +
                    "`fullname` = ?,\n" +
                    "`role` = ?,\n" +
                    "`status` = ?\n" +
                    "WHERE `username` = ?;";
            stm = connection.prepareStatement(sql);
            stm.setString(1, user.getFullname());
            stm.setString(2, String.valueOf(user.getRole()));
            stm.setString(3, String.valueOf(user.getStatus()));
            stm.setString(4, user.getUsername());
            stm.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("update(): " + e.getMessage());
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


    public boolean delete(int id) {
        try {
            String sql = "DELETE FROM `categories`\n" +
                    "WHERE id = ?;\n";
            stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            stm.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("delete(): " + e.getMessage());
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

