package vn.aptech.c2304l.learning.dal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import vn.aptech.c2304l.learning.context.DBContext;
import vn.aptech.c2304l.learning.model.Table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TableDAO extends DBContext {
    PreparedStatement stm;
    ResultSet rs;

    public ObservableList<Table> findAll() {
        ObservableList<Table> tables = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM tables";
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                Table table = new Table();
                table.setId(rs.getInt(1));
                table.setTableNumber(rs.getInt(2));
                table.setNumOfSeats(rs.getInt(3));
                tables.add(table);
            }
            return tables;
        }catch(Exception e) {
            System.out.println("findAll(): " + e.getMessage());
        }finally {
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

    public boolean findByTableNumber(int tableNumber) {
        try {
            String sql = "SELECT * FROM tables WHERE table_number = ?";
            stm = connection.prepareStatement(sql);
            stm.setInt(1, tableNumber);
            rs = stm.executeQuery();
            if(rs.next()) {
                return true;
            }
        }catch (Exception e) {
            System.out.println("findById(): " + e.getMessage());
        }finally {
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

    public boolean findByTableNumberAndSeats(int tableNumber, int numOfSeats) {
        try {
            String sql = "SELECT * FROM tables WHERE table_number = ? AND num_of_seats = ?";
            stm = connection.prepareStatement(sql);
            stm.setInt(1, tableNumber);
            stm.setInt(2, numOfSeats);
            rs = stm.executeQuery();
            if(rs.next()) {
                return true;
            }
        }catch (Exception e) {
            System.out.println("findByTableNumberAndSeats(): " + e.getMessage());
        }finally {
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

    public boolean insert(Table table) {
        try {
            String sql = "INSERT INTO `tables`\n" +
                    "(`table_number`,\n" +
                    "`num_of_seats`)\n" +
                    "VALUES\n" +
                    "(?,\n" +
                    "?);";
            stm = connection.prepareStatement(sql);
            stm.setInt(1, table.getTableNumber());
            stm.setInt(2, table.getNumOfSeats());
            stm.executeUpdate();
            return true;
        }catch(Exception e) {
            System.out.println("insert(): " + e.getMessage());
        }finally {
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
            String sql = "DELETE FROM `tables`\n" +
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

    public boolean update(Table table) {
        try {
            String sql = "UPDATE `tables`\n" +
                    "SET\n" +
                    "`table_number` = ?,\n" +
                    "`num_of_seats` = ?\n" +
                    "WHERE `id` = ?;";
            stm = connection.prepareStatement(sql);
            stm.setInt(1, table.getTableNumber());
            stm.setInt(2, table.getNumOfSeats());
            stm.setInt(3, table.getId());
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

    public ObservableList<Table> search(String keyWord) {
        ObservableList<Table> tables = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM tables WHERE table_number LIKE ? OR num_of_seats LIKE ?";
            stm = connection.prepareStatement(sql);
            stm.setString(1, "%" + keyWord + "%");
            stm.setString(2, "%" + keyWord + "%");
            rs = stm.executeQuery();
            while (rs.next()) {
                Table table = new Table();
                table.setId(rs.getInt(1));
                table.setTableNumber(rs.getInt(2));
                table.setNumOfSeats(rs.getInt(3));
                tables.add(table);
            }
            return tables;
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

    public ObservableList<Table> filter(int numOfSeats) {
        ObservableList<Table> tables = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM tables WHERE num_of_seats = ?";
            stm = connection.prepareStatement(sql);
            stm.setInt(1, numOfSeats);
            rs = stm.executeQuery();
            while (rs.next()) {
                Table table = new Table();
                table.setId(rs.getInt(1));
                table.setTableNumber(rs.getInt(2));
                table.setNumOfSeats(rs.getInt(3));
                tables.add(table);
            }
            return tables;
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
        TableDAO tdao = new TableDAO();
        ObservableList<Table> list = tdao.findAll();
        for(Table t : list) {
            System.out.println(t);
        }
    }
}
