package vn.aptech.c2304l.learning.dal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import vn.aptech.c2304l.learning.context.DBContext;
import vn.aptech.c2304l.learning.model.Category;
import vn.aptech.c2304l.learning.model.Order;
import vn.aptech.c2304l.learning.model.Table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDAO extends DBContext {
    PreparedStatement stm;
    ResultSet rs;

//    public ObservableList<Order> findAll() {
//        ObservableList<Order> orders = FXCollections.observableArrayList();
//        try {
//            String sql = "SELECT * FROM orders o JOIN products p ON o.product_id = p.id" +
//                    "JOIN tables t ON o.table_id = t.id" +
//                    "JOIN users u ON o.user_id = u.id";
//            stm = connection.prepareStatement(sql);
//            rs = stm.executeQuery();
//            while (rs.next()) {
//                Order order = new Order();
//                order.setId(rs.getInt(1));
//
//                Table table = new Table();
//                table.set
//                order.setTable();
//            }
//            return categories;
//        } catch (Exception e) {
//            System.out.println("findAll(): " + e.getMessage());
//        } finally {
//            if(stm != null) {
//                try {
//                    stm.close();
//                } catch (SQLException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }
//        return null;
//    }
}
