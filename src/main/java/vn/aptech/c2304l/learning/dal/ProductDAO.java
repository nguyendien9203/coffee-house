package vn.aptech.c2304l.learning.dal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import vn.aptech.c2304l.learning.constant.ProductStatus;
import vn.aptech.c2304l.learning.context.DBContext;
import vn.aptech.c2304l.learning.model.Category;
import vn.aptech.c2304l.learning.model.Product;
import vn.aptech.c2304l.learning.model.Table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDAO extends DBContext {
    PreparedStatement stm;
    ResultSet rs;

    public ObservableList<Product> findAll() {
        ObservableList<Product> products = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM products p JOIN categories c ON p.category_id = c.id";
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt(1));

                Category category = new Category();
                category.setId(rs.getInt(2));
                category.setName(rs.getString(11));
                product.setCategory(category);

                product.setName(rs.getString(3));
                product.setPrice(rs.getBigDecimal(4));
                product.setImage(rs.getString(5));
                product.setDescription(rs.getString(6));
                product.setStatus(ProductStatus.valueOf(rs.getString(7)));
                products.add(product);
            }
            return products;
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
    public boolean insert(Product product) {
        try {
            String sql = "INSERT INTO `products`\n" +
                    "(`category_id`,\n" +
                    "`name`,\n" +
                    "`price`,\n" +
                    "`product_image`,\n" +
                    "`description`,\n" +
                    "`status`)\n" +
                    "VALUES\n" +
                    "(?,\n" +
                    "?,\n" +
                    "?,\n" +
                    "?,\n" +
                    "?,\n" +
                    "?);";
            stm = connection.prepareStatement(sql);
            stm.setInt(1, product.getCategory().getId());
            stm.setString(2, product.getName());
            stm.setBigDecimal(3, product.getPrice());
            stm.setString(4, product.getImage());
            stm.setString(5, product.getDescription());
            stm.setString(6, String.valueOf(product.getStatus()));
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

    public boolean update(Product product) {
        try {
            String sql = "UPDATE `products`\n" +
                    "SET\n" +
                    "`category_id` = ?,\n" +
                    "`name` = ?,\n" +
                    "`price` = ?,\n" +
                    "`product_image` = ?,\n" +
                    "`description` = ?,\n" +
                    "`status` = ?\n" +
                    "WHERE `id` = ?;";
            stm = connection.prepareStatement(sql);
            stm.setInt(1, product.getCategory().getId());
            stm.setString(2, product.getName());
            stm.setBigDecimal(3, product.getPrice());
            stm.setString(4, product.getImage());
            stm.setString(5, product.getDescription());
            stm.setString(6, String.valueOf(product.getStatus()));
            stm.setInt(7, product.getId());
            stm.executeUpdate();
            return true;
        }catch (Exception e) {
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
            String sql = "DELETE FROM `products`\n" +
                    "WHERE id = ?;";
            stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            stm.executeUpdate();
            return true;
        }catch (Exception e) {
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

    public boolean findByName(String name) {
        try {
            String sql = "SELECT * FROM products WHERE name = ?";
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

    public ObservableList<Product> findAllByCategory(int cateId) {
        ObservableList<Product> products = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM products p JOIN categories c ON p.category_id = c.id WHERE c.id = ?";
            stm = connection.prepareStatement(sql);
            stm.setInt(1, cateId);
            rs = stm.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt(1));
                product.setName(rs.getString(3));
                product.setPrice(rs.getBigDecimal(4));
                product.setImage(rs.getString(5));
                product.setDescription(rs.getString(6));
                product.setStatus(ProductStatus.valueOf(rs.getString(7)));

                Category category = new Category();
                category.setId(rs.getInt(2));
                category.setName(rs.getString(11));
                product.setCategory(category);

                products.add(product);
            }
            return products;
        } catch (Exception e) {
            System.out.println("findAllByCategory(): " + e.getMessage());
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

    public ObservableList<Product> search(String keyWord) {
        if(keyWord == null) {
            return findAll();
        }

        ObservableList<Product> products = FXCollections.observableArrayList();
        try {

            String sql = "SELECT * FROM products p JOIN categories c ON p.category_id = c.id " +
                    "WHERE p.name LIKE ? OR p.price LIKE ? OR c.name LIKE ? OR p.description LIKE ?";
            stm = connection.prepareStatement(sql);
            stm.setString(1, "%" + keyWord + "%");
            stm.setString(2, "%" + keyWord + "%");
            stm.setString(3, "%" + keyWord + "%");
            stm.setString(4, "%" + keyWord + "%");
            rs = stm.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt(1));

                Category category = new Category();
                category.setId(rs.getInt(2));
                category.setName(rs.getString(11));
                product.setCategory(category);

                product.setName(rs.getString(3));
                product.setPrice(rs.getBigDecimal(4));
                product.setImage(rs.getString(5));
                product.setDescription(rs.getString(6));
                product.setStatus(ProductStatus.valueOf(rs.getString(7)));
                products.add(product);
            }
            return products;
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

    public ObservableList<Product> search(String keyWord, String priceType, ObservableList<Product> filtedResult) {



        ObservableList<Product> filteredProducts = FXCollections.observableArrayList();

        try {
            String sortOrder = priceType.equals("Giá tăng dần") ? "ASC" : "DESC";
            String sql = "SELECT * FROM products p JOIN categories c ON p.category_id = c.id " +
                    "WHERE p.name LIKE ? OR p.price LIKE ? OR c.name LIKE ? OR p.description LIKE ? " +
                    "ORDER BY p.price " + sortOrder;
            stm = connection.prepareStatement(sql);
            stm.setString(1, "%" + keyWord + "%");
            stm.setString(2, "%" + keyWord + "%");
            stm.setString(3, "%" + keyWord + "%");
            stm.setString(4, "%" + keyWord + "%");
            rs = stm.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt(1));

                Category category = new Category();
                category.setId(rs.getInt(2));
                category.setName(rs.getString(11));
                product.setCategory(category);

                product.setName(rs.getString(3));
                product.setPrice(rs.getBigDecimal(4));
                product.setImage(rs.getString(5));
                product.setDescription(rs.getString(6));
                product.setStatus(ProductStatus.valueOf(rs.getString(7)));

                if (filtedResult.contains(product)) {
                    filteredProducts.add(product);
                }
            }
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

        return filteredProducts;
    }


    public ObservableList<Product> filter(String priceType) {

        ObservableList<Product> products = FXCollections.observableArrayList();
        try {
            String sortOrder = priceType.equals("Giá tăng dần") ? "ASC" : "DESC";
            String sql = "SELECT * FROM products p JOIN categories c ON p.category_id = c.id ORDER BY p.price " + sortOrder;
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt(1));

                Category category = new Category();
                category.setId(rs.getInt(2));
                category.setName(rs.getString(11));
                product.setCategory(category);

                product.setName(rs.getString(3));
                product.setPrice(rs.getBigDecimal(4));
                product.setImage(rs.getString(5));
                product.setDescription(rs.getString(6));
                product.setStatus(ProductStatus.valueOf(rs.getString(7)));
                products.add(product);
            }
            return products;
        } catch (Exception e) {
            System.out.println("filter(): " + e.getMessage());
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

    public ObservableList<Product> filter(String priceType, ObservableList<Product> searchResult) {
        ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
        try {
            String sortOrder = priceType.equals("Giá tăng dần") ? "ASC" : "DESC";
            String sql = "SELECT * FROM products p JOIN categories c ON p.category_id = c.id ORDER BY price " + sortOrder;
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt(1));

                Category category = new Category();
                category.setId(rs.getInt(2));
                category.setName(rs.getString(11));
                product.setCategory(category);

                product.setName(rs.getString(3));
                product.setPrice(rs.getBigDecimal(4));
                product.setImage(rs.getString(5));
                product.setDescription(rs.getString(6));
                product.setStatus(ProductStatus.valueOf(rs.getString(7)));

                if (searchResult.contains(product)) {
                    filteredProducts.add(product);
                }
            }
            return filteredProducts;
        } catch (Exception e) {
            System.out.println("filter(): " + e.getMessage());
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


    public static void main(String[] args) {
        ProductDAO pdao = new ProductDAO();
        ObservableList<Product> products = pdao.findAllByCategory(1);
        for(Product p : products) {
            System.out.println(p);
        }
    }



}
