package vn.aptech.c2304l.learning.dal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import vn.aptech.c2304l.learning.context.DBContext;
import vn.aptech.c2304l.learning.model.Category;
import vn.aptech.c2304l.learning.model.Table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryDAO extends DBContext {
    PreparedStatement stm;
    ResultSet rs;

    public ObservableList<Category> findAll() {
        ObservableList<Category> categories = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM categories";
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt(1));
                category.setName(rs.getString(2));
                categories.add(category);
            }
            return categories;
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

    public Category findById(int id) {
        try {
            String sql = "SELECT * FROM categories WHERE id = ?";
            stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            rs = stm.executeQuery();
            if(rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt(1));
                category.setName(rs.getString(2));
                return category;
            }
        } catch (Exception e) {
            System.out.println("findById(): " + e.getMessage());
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

    public boolean findByName(String name) {
        try {
            String sql = "SELECT * FROM categories WHERE name = ?";
            stm = connection.prepareStatement(sql);
            stm.setString(1, name);
            rs = stm.executeQuery();
            if(rs.next()) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("findByName(): " + e.getMessage());
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

    public boolean insert(Category category) {
        try {
            String sql = "INSERT INTO `categories`\n" +
                    "(`name`)\n" +
                    "VALUES\n" +
                    "(?);\n";
            stm = connection.prepareStatement(sql);
            stm.setString(1, category.getName());
            stm.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("insert(): " + e.getMessage());
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

    public boolean update(Category category) {
        try {
            String sql = "UPDATE `categories`\n" +
                    "SET\n" +
                    "`name` = ?\n" +
                    "WHERE `id` = ?;";
            stm = connection.prepareStatement(sql);
            stm.setString(1, category.getName());
            stm.setInt(2, category.getId());
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

    public ObservableList<Category> search(String keyWord) {
        ObservableList<Category> categories = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM categories WHERE name LIKE ?";
            stm = connection.prepareStatement(sql);
            stm.setString(1, "%" + keyWord + "%");
            rs = stm.executeQuery();
            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt(1));
                category.setName(rs.getString(2));
                categories.add(category);
            }
            return categories;
        } catch (Exception e) {
            System.out.println("search(): " + e.getMessage());
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

}
