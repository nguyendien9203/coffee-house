package vn.aptech.c2304l.learning.dal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import vn.aptech.c2304l.learning.constant.OrderStatus;
import vn.aptech.c2304l.learning.constant.UserRole;
import vn.aptech.c2304l.learning.context.DBContext;
import vn.aptech.c2304l.learning.model.*;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class OrderDAO extends DBContext {
    PreparedStatement stm;
    ResultSet rs;

    public ObservableList<Order> findAll() {
        ObservableList<Order> orders = FXCollections.observableArrayList();
        try {
            String sql = "SELECT o.code, o.payment_method, o.order_start_time, o.order_end_time, o.order_note, o.status, \n" +
                    "t.id as table_id, t.table_number, t.num_of_seats,\n" +
                    "u.id as user_id, u.role, u.fullname, u.username,\n" +
                    "oi.product_id, oi.qty, \n" +
                    "p.id as product_id, p.name, p.price, p.product_image, p.description, p.status as product_status\n" +
                    "FROM orders o \n" +
                    "JOIN tables t ON o.table_id = t.id \n" +
                    "JOIN users u ON o.user_id = u.id \n" +
                    "JOIN order_items oi ON o.code = oi.code \n" +
                    "JOIN products p ON oi.product_id = p.id";
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();

            Map<String, Order> orderMap = new HashMap<>();

            while (rs.next()) {
                String orderCode = rs.getString(1);
                Order order = orderMap.get(orderCode);

                if (order == null) {
                    order = new Order();
                    order.setCode(orderCode);
                    order.setPaymentMethod(rs.getString(2));
                    order.setOrderStartTime(rs.getTimestamp(3));
                    order.setOrderEndTime(rs.getTimestamp(4));
                    order.setNote(rs.getString(5));
                    order.setStatus(OrderStatus.valueOf(rs.getString(6)));

                    Table table = new Table();
                    table.setId(rs.getInt(7));
                    table.setTableNumber(rs.getInt(8));
                    table.setNumOfSeats(rs.getInt(9));
                    order.setTable(table);

                    User user = new User();
                    user.setId(rs.getInt(10));
                    user.setRole(UserRole.valueOf(rs.getString(11)));
                    user.setFullname(rs.getString(12));
                    user.setUsername(rs.getString(13));
                    order.setUser(user);

                    order.setOrderItems(new ArrayList<>());
                    orderMap.put(orderCode, order);
                    orders.add(order);
                }

                Product product = new Product();
                product.setId(rs.getInt(16));
                product.setName(rs.getString(17));
                product.setPrice(rs.getBigDecimal(18));
                product.setImage(rs.getString(19));
                product.setDescription(rs.getString(20));
                product.setStatus(rs.getString(21));

                OrderItem orderItem = new OrderItem();
                orderItem.setCode(order.getCode());
                orderItem.setProduct(product);
                orderItem.setQuantity(rs.getInt(15));
                order.getOrderItems().add(orderItem);
            }

            return orders;
        } catch (Exception e) {
            System.out.println("findAll(): " + e.getMessage());
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


    public Order findByCode(String code) {
        try {
            String sql = "SELECT o.code, o.payment_method, o.order_start_time, o.order_end_time, o.order_note, o.status, \n" +
                    "t.id as table_id, t.table_number, t.num_of_seats,\n" +
                    "u.id as user_id, u.role, u.fullname, u.username,\n" +
                    "oi.product_id, oi.qty, \n" +
                    "p.id as product_id, p.name, p.price, p.product_image, p.description, p.status as product_status\n" +
                    "FROM orders o \n" +
                    "JOIN tables t ON o.table_id = t.id \n" +
                    "JOIN users u ON o.user_id = u.id \n" +
                    "JOIN order_items oi ON o.code = oi.code \n" +
                    "JOIN products p ON oi.product_id = p.id\n" +
                    "WHERE o.code = ?";
            stm = connection.prepareStatement(sql);
            stm.setString(1, code);
            rs = stm.executeQuery();

            Order order = null;
            List<OrderItem> orderItems = new ArrayList<>();

            while (rs.next()) {
                if (order == null) {
                    order = new Order();
                    order.setCode(rs.getString(1));
                    order.setPaymentMethod(rs.getString(2));
                    order.setOrderStartTime(rs.getTimestamp(3));
                    order.setOrderEndTime(rs.getTimestamp(4));
                    order.setNote(rs.getString(5));
                    order.setStatus(OrderStatus.valueOf(rs.getString(6)));

                    Table table = new Table();
                    table.setId(rs.getInt(7));
                    table.setTableNumber(rs.getInt(8));
                    table.setNumOfSeats(rs.getInt(9));
                    order.setTable(table);

                    User user = new User();
                    user.setId(rs.getInt(10));
                    user.setRole(UserRole.valueOf(rs.getString(11)));
                    user.setFullname(rs.getString(12));
                    user.setUsername(rs.getString(13));
                    order.setUser(user);
                }

                Product product = new Product();
                product.setId(rs.getInt(16));
                product.setName(rs.getString(17));
                product.setPrice(rs.getBigDecimal(18));
                product.setImage(rs.getString(19));
                product.setDescription(rs.getString(20));
                product.setStatus(rs.getString(21));

                OrderItem orderItem = new OrderItem();
                orderItem.setCode(order.getCode());
                orderItem.setProduct(product);
                orderItem.setQuantity(rs.getInt(15));
                orderItems.add(orderItem);
            }

            if (order != null) {
                order.setOrderItems(orderItems);
            }

            return order;
        } catch (Exception e) {
            System.out.println("findByCode(): " + e.getMessage());
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

    // tìm những order chưa thanh toán theo bàn
    public ObservableList<Order> findUnpaidOrdersByTable(int tableNumber) {
        ObservableList<Order> allOrders = findAll();
        ObservableList<Order> unpaidOrders = FXCollections.observableArrayList();

        for (Order order : allOrders) {
            if (order.getTable().getTableNumber() == tableNumber && order.getStatus() == OrderStatus.UNPAID) {
                unpaidOrders.add(order);
            }
        }

        return unpaidOrders;
    }

    //tìm order chưa thanh toán theo bàn
    public Order findUnpaidOrderByTable(int tableNumber) {
        ObservableList<Order> allOrders = findAll();
        for (Order order : allOrders) {
            if (order.getTable().getTableNumber() == tableNumber && order.getStatus() == OrderStatus.UNPAID) {
                return order;
            }
        }
        return null;
    }

    //insert nếu chỉ lưu mỗi hóa đơn
    public boolean saveOrder(Order order) {
        PreparedStatement orderStm = null;
        PreparedStatement orderItemStm = null;
        try {
            String orderSql = "INSERT INTO `orders` " +
                    "(`code`, `user_id`, `table_id`, `payment_method`, `order_start_time`, `order_note`, `status`) " +
                    "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?);";
            orderStm = connection.prepareStatement(orderSql);
            orderStm.setString(1, order.getCode());
            orderStm.setInt(2, order.getUser().getId());
            orderStm.setInt(3, order.getTable().getId());
            orderStm.setString(4, order.getPaymentMethod());
            orderStm.setString(5, order.getNote());
            orderStm.setString(6, order.getStatus().name());

            int orderResult = orderStm.executeUpdate();
            if (orderResult == 0) {
                return false;
            }

            String orderItemSql = "INSERT INTO `order_items` (`code`, `product_id`, `qty`) VALUES (?, ?, ?);";
            orderItemStm = connection.prepareStatement(orderItemSql);
            for (OrderItem orderItem : order.getOrderItems()) {

                orderItemStm.setString(1, order.getCode());
                orderItemStm.setInt(2, orderItem.getProduct().getId());
                orderItemStm.setInt(3, orderItem.getQuantity());

                orderItemStm.addBatch();
            }

            int[] orderItemResults = orderItemStm.executeBatch();
            for (int result : orderItemResults) {
                if (result == 0) {
                    return false;
                }
            }

            return true;
        } catch (SQLException e) {
            System.out.println("insertOrderStart(): " + e.getMessage());
            return false;
        } finally {
            try {
                if (orderStm != null) {
                    orderStm.close();
                }
                if (orderItemStm != null) {
                    orderItemStm.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //nếu đã lưu thì chỉ update lại order
    public boolean updateSavaOrder(int tableNumber, Order order) {
        try {
            String getOrderCodeSql = "SELECT o.code\n" +
                    "FROM orders o \n" +
                    "JOIN tables t ON o.table_id = t.id \n" +
                    "JOIN users u ON o.user_id = u.id \n" +
                    "JOIN order_items oi ON o.code = oi.code \n" +
                    "JOIN products p ON oi.product_id = p.id\n" +
                    "WHERE t.table_number = ? AND o.status = 'UNPAID';";
            PreparedStatement getOrderCodeStm = connection.prepareStatement(getOrderCodeSql);
            getOrderCodeStm.setInt(1, tableNumber);
            ResultSet rs = getOrderCodeStm.executeQuery();

            String orderCode = null;
            if (rs.next()) {
                orderCode = rs.getString("code");
            }
            rs.close();
            getOrderCodeStm.close();

            if (orderCode == null) {
                System.out.println("No unpaid order found for table number: " + tableNumber);
                return false;
            }

            String deleteSql = "DELETE FROM `order_items` WHERE `code` = ?;";
            PreparedStatement deleteStm = connection.prepareStatement(deleteSql);
            deleteStm.setString(1, orderCode);
            deleteStm.executeUpdate();
            deleteStm.close();

            String updateOrderSql = "UPDATE `orders` SET " +
                    "`user_id` = ?, " +
                    "`payment_method` = ?, " +
                    "`order_note` = ? " +
                    "WHERE `code` = ?;";
            PreparedStatement updateOrderStm = connection.prepareStatement(updateOrderSql);
            updateOrderStm.setInt(1, order.getUser().getId());
            updateOrderStm.setString(2, order.getPaymentMethod());
            updateOrderStm.setString(3, order.getNote());
            updateOrderStm.setString(4, orderCode);
            updateOrderStm.executeUpdate();
            updateOrderStm.close();

            String insertOrderItemSql = "INSERT INTO `order_items` (`code`, `product_id`, `qty`) VALUES (?, ?, ?);";
            PreparedStatement insertOrderItemStm = connection.prepareStatement(insertOrderItemSql);
            for (OrderItem orderItem : order.getOrderItems()) {
                insertOrderItemStm.setString(1, orderCode);
                insertOrderItemStm.setInt(2, orderItem.getProduct().getId());
                insertOrderItemStm.setInt(3, orderItem.getQuantity());
                insertOrderItemStm.addBatch();
            }
            insertOrderItemStm.executeBatch();
            insertOrderItemStm.close();

            return true;
        } catch (SQLException e) {
            System.out.println("updateOrderProductByTableNumber(): " + e.getMessage());
        }
        return false;
    }

    //thanh toán hóa đơn đã lưu
    public boolean checkoutSavedOrder(int tableNumber, Order order) {
        try {
            String getOrderCodeSql = "SELECT o.code\n" +
                    "FROM orders o \n" +
                    "JOIN tables t ON o.table_id = t.id \n" +
                    "JOIN users u ON o.user_id = u.id \n" +
                    "JOIN order_items oi ON o.code = oi.code \n" +
                    "JOIN products p ON oi.product_id = p.id\n" +
                    "WHERE t.table_number = ? AND o.status = 'UNPAID';";
            PreparedStatement getOrderCodeStm = connection.prepareStatement(getOrderCodeSql);
            getOrderCodeStm.setInt(1, tableNumber);
            ResultSet rs = getOrderCodeStm.executeQuery();

            String orderCode = null;
            if (rs.next()) {
                orderCode = rs.getString("code");
            }
            rs.close();
            getOrderCodeStm.close();

            if (orderCode == null) {
                System.out.println("No unpaid order found for table number: " + tableNumber);
                return false;
            }

            String updateOrderSql = "UPDATE `orders` SET " +
                    "`user_id` = ?, " +
                    "`payment_method` = ?, " +
                    "`order_note` = ?, " +
                    "`order_end_time` = CURRENT_TIMESTAMP, " +
                    "`status` = ? " +
                    "WHERE `code` = ?;";
            PreparedStatement updateOrderStm = connection.prepareStatement(updateOrderSql);
            updateOrderStm.setInt(1, order.getUser().getId());
            updateOrderStm.setString(2, order.getPaymentMethod());
            updateOrderStm.setString(3, order.getNote());
            updateOrderStm.setString(4, order.getStatus().toString());
            updateOrderStm.setString(5, orderCode);
            updateOrderStm.executeUpdate();
            updateOrderStm.close();

            return true;
        } catch (SQLException e) {
            System.out.println("checkoutSavedOrder(): " + e.getMessage());
        }
        return false;
    }

    //Thanh toán luôn hóa đơn
    public boolean checkoutUnsavedOrder(Order order) {
        PreparedStatement orderStm = null;
        PreparedStatement orderItemStm = null;
        try {
            String orderSql = "INSERT INTO `orders` " +
                    "(`code`, `user_id`, `table_id`, `payment_method`, `order_start_time`, `order_end_time`, `order_note`, `status`) " +
                    "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, ?, ?);";
            orderStm = connection.prepareStatement(orderSql);
            orderStm.setString(1, order.getCode());
            orderStm.setInt(2, order.getUser().getId());
            orderStm.setInt(3, order.getTable().getId());
            orderStm.setString(4, order.getPaymentMethod());
            orderStm.setString(5, order.getNote());
            orderStm.setString(6, order.getStatus().name());

            int orderResult = orderStm.executeUpdate();
            if (orderResult == 0) {
                return false;
            }

            String orderItemSql = "INSERT INTO `order_items` (`code`, `product_id`, `qty`) VALUES (?, ?, ?);";
            orderItemStm = connection.prepareStatement(orderItemSql);
            for (OrderItem orderItem : order.getOrderItems()) {

                orderItemStm.setString(1, order.getCode());
                orderItemStm.setInt(2, orderItem.getProduct().getId());
                orderItemStm.setInt(3, orderItem.getQuantity());

                orderItemStm.addBatch();
            }

            int[] orderItemResults = orderItemStm.executeBatch();
            for (int result : orderItemResults) {
                if (result == 0) {
                    return false;
                }
            }

            return true;
        } catch (SQLException e) {
            System.out.println("checkoutUnsavedOrder(): " + e.getMessage());
            return false;
        } finally {
            try {
                if (orderStm != null) {
                    orderStm.close();
                }
                if (orderItemStm != null) {
                    orderItemStm.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //tìm kiếm theo codeBill
    public ObservableList<Order> search(String keyWord) {
        if(keyWord == null) {
            return findAll();
        }

        ObservableList<Order> orders = FXCollections.observableArrayList();
        try {
            String sql = "SELECT o.code, o.payment_method, o.order_start_time, o.order_end_time, o.order_note, o.status, \n" +
                    "t.id as table_id, t.table_number, t.num_of_seats,\n" +
                    "u.id as user_id, u.role, u.fullname, u.username,\n" +
                    "oi.product_id, oi.qty, \n" +
                    "p.id as product_id, p.name, p.price, p.product_image, p.description, p.status as product_status\n" +
                    "FROM orders o \n" +
                    "JOIN tables t ON o.table_id = t.id \n" +
                    "JOIN users u ON o.user_id = u.id \n" +
                    "JOIN order_items oi ON o.code = oi.code \n" +
                    "JOIN products p ON oi.product_id = p.id WHERE o.code LIKE ?";
            stm = connection.prepareStatement(sql);
            stm.setString(1, "%" + keyWord + "%");
            rs = stm.executeQuery();

            Map<String, Order> orderMap = new HashMap<>();

            while (rs.next()) {
                String orderCode = rs.getString(1);
                Order order = orderMap.get(orderCode);

                if (order == null) {
                    order = new Order();
                    order.setCode(orderCode);
                    order.setPaymentMethod(rs.getString(2));
                    order.setOrderStartTime(rs.getTimestamp(3));
                    order.setOrderEndTime(rs.getTimestamp(4));
                    order.setNote(rs.getString(5));
                    order.setStatus(OrderStatus.valueOf(rs.getString(6)));

                    Table table = new Table();
                    table.setId(rs.getInt(7));
                    table.setTableNumber(rs.getInt(8));
                    table.setNumOfSeats(rs.getInt(9));
                    order.setTable(table);

                    User user = new User();
                    user.setId(rs.getInt(10));
                    user.setRole(UserRole.valueOf(rs.getString(11)));
                    user.setFullname(rs.getString(12));
                    user.setUsername(rs.getString(13));
                    order.setUser(user);

                    order.setOrderItems(new ArrayList<>());
                    orderMap.put(orderCode, order);
                    orders.add(order);
                }

                Product product = new Product();
                product.setId(rs.getInt(16));
                product.setName(rs.getString(17));
                product.setPrice(rs.getBigDecimal(18));
                product.setImage(rs.getString(19));
                product.setDescription(rs.getString(20));
                product.setStatus(rs.getString(21));

                OrderItem orderItem = new OrderItem();
                orderItem.setCode(order.getCode());
                orderItem.setProduct(product);
                orderItem.setQuantity(rs.getInt(15));
                order.getOrderItems().add(orderItem);
            }

            return orders;
        } catch (Exception e) {
            System.out.println("search(): " + e.getMessage());
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

    // tìm kiếm trong danh sách lọc
    public ObservableList<Order> search(String keyWord, String statusType, ObservableList<Order> filteredResult) {

        ObservableList<Order> searchResult = FXCollections.observableArrayList();

        try {
            String sql = "SELECT o.code, o.payment_method, o.order_start_time, o.order_end_time, o.order_note, o.status, \n" +
                    "t.id as table_id, t.table_number, t.num_of_seats,\n" +
                    "u.id as user_id, u.role, u.fullname, u.username,\n" +
                    "oi.product_id, oi.qty, \n" +
                    "p.id as product_id, p.name, p.price, p.product_image, p.description, p.status as product_status\n" +
                    "FROM orders o \n" +
                    "JOIN tables t ON o.table_id = t.id \n" +
                    "JOIN users u ON o.user_id = u.id \n" +
                    "JOIN order_items oi ON o.code = oi.code \n" +
                    "JOIN products p ON oi.product_id = p.id WHERE o.code LIKE ?";
            stm = connection.prepareStatement(sql);
            stm.setString(1, "%" + keyWord + "%");
            rs = stm.executeQuery();

            Map<String, Order> orderMap = new HashMap<>();

            while (rs.next()) {
                String orderCode = rs.getString(1);
                Order order = orderMap.get(orderCode);

                if (order == null) {
                    order = new Order();
                    order.setCode(orderCode);
                    order.setPaymentMethod(rs.getString(2));
                    order.setOrderStartTime(rs.getTimestamp(3));
                    order.setOrderEndTime(rs.getTimestamp(4));
                    order.setNote(rs.getString(5));
                    order.setStatus(OrderStatus.valueOf(rs.getString(6)));

                    Table table = new Table();
                    table.setId(rs.getInt(7));
                    table.setTableNumber(rs.getInt(8));
                    table.setNumOfSeats(rs.getInt(9));
                    order.setTable(table);

                    User user = new User();
                    user.setId(rs.getInt(10));
                    user.setRole(UserRole.valueOf(rs.getString(11)));
                    user.setFullname(rs.getString(12));
                    user.setUsername(rs.getString(13));
                    order.setUser(user);

                    order.setOrderItems(new ArrayList<>());
                    orderMap.put(orderCode, order);
                }

                Product product = new Product();
                product.setId(rs.getInt(16));
                product.setName(rs.getString(17));
                product.setPrice(rs.getBigDecimal(18));
                product.setImage(rs.getString(19));
                product.setDescription(rs.getString(20));
                product.setStatus(rs.getString(21));

                OrderItem orderItem = new OrderItem();
                orderItem.setCode(order.getCode());
                orderItem.setProduct(product);
                orderItem.setQuantity(rs.getInt(15));
                order.getOrderItems().add(orderItem);
            }

            filteredResult.addAll(orderMap.values());

            for (Order order : orderMap.values()) {
                if (statusType != null && order.getStatus().toString().equals(statusType) && filteredResult.contains(order)) {
                    searchResult.add(order);
                }
            }

            return searchResult;

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

        return searchResult;
    }



    public ObservableList<Order> filter(String statusType) {
        if(statusType.equals("Xóa bộ lọc")) {
            return findAll();
        }

        ObservableList<Order> orders = FXCollections.observableArrayList();
        try {
            String sql = "SELECT o.code, o.payment_method, o.order_start_time, o.order_end_time, o.order_note, o.status, \n" +
                    "t.id as table_id, t.table_number, t.num_of_seats,\n" +
                    "u.id as user_id, u.role, u.fullname, u.username,\n" +
                    "oi.product_id, oi.qty, \n" +
                    "p.id as product_id, p.name, p.price, p.product_image, p.description, p.status as product_status\n" +
                    "FROM orders o \n" +
                    "JOIN tables t ON o.table_id = t.id \n" +
                    "JOIN users u ON o.user_id = u.id \n" +
                    "JOIN order_items oi ON o.code = oi.code \n" +
                    "JOIN products p ON oi.product_id = p.id WHERE o.status = ?";
            stm = connection.prepareStatement(sql);
            stm.setString(1, statusType);
            rs = stm.executeQuery();
            Map<String, Order> orderMap = new HashMap<>();

            while (rs.next()) {
                String orderCode = rs.getString(1);
                Order order = orderMap.get(orderCode);

                if (order == null) {
                    order = new Order();
                    order.setCode(orderCode);
                    order.setPaymentMethod(rs.getString(2));
                    order.setOrderStartTime(rs.getTimestamp(3));
                    order.setOrderEndTime(rs.getTimestamp(4));
                    order.setNote(rs.getString(5));
                    order.setStatus(OrderStatus.valueOf(rs.getString(6)));

                    Table table = new Table();
                    table.setId(rs.getInt(7));
                    table.setTableNumber(rs.getInt(8));
                    table.setNumOfSeats(rs.getInt(9));
                    order.setTable(table);

                    User user = new User();
                    user.setId(rs.getInt(10));
                    user.setRole(UserRole.valueOf(rs.getString(11)));
                    user.setFullname(rs.getString(12));
                    user.setUsername(rs.getString(13));
                    order.setUser(user);

                    order.setOrderItems(new ArrayList<>());
                    orderMap.put(orderCode, order);
                    orders.add(order);
                }

                Product product = new Product();
                product.setId(rs.getInt(16));
                product.setName(rs.getString(17));
                product.setPrice(rs.getBigDecimal(18));
                product.setImage(rs.getString(19));
                product.setDescription(rs.getString(20));
                product.setStatus(rs.getString(21));

                OrderItem orderItem = new OrderItem();
                orderItem.setCode(order.getCode());
                orderItem.setProduct(product);
                orderItem.setQuantity(rs.getInt(15));
                order.getOrderItems().add(orderItem);
            }

            return orders;
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

    public ObservableList<Order> filter(String statusType, ObservableList<Order> searchResult) {
        ObservableList<Order> filteredProducts = FXCollections.observableArrayList();
        ObservableList<Order> orders = FXCollections.observableArrayList();
        try {
            String sql = "SELECT o.code, o.payment_method, o.order_start_time, o.order_end_time, o.order_note, o.status, \n" +
                    "t.id as table_id, t.table_number, t.num_of_seats,\n" +
                    "u.id as user_id, u.role, u.fullname, u.username,\n" +
                    "oi.product_id, oi.qty, \n" +
                    "p.id as product_id, p.name, p.price, p.product_image, p.description, p.status as product_status\n" +
                    "FROM orders o \n" +
                    "JOIN tables t ON o.table_id = t.id \n" +
                    "JOIN users u ON o.user_id = u.id \n" +
                    "JOIN order_items oi ON o.code = oi.code \n" +
                    "JOIN products p ON oi.product_id = p.id WHERE o.status = ?";
            stm = connection.prepareStatement(sql);
            stm.setString(1, statusType);
            rs = stm.executeQuery();

            Map<String, Order> orderMap = new HashMap<>();

            while (rs.next()) {
                String orderCode = rs.getString(1);
                Order order = orderMap.get(orderCode);

                if (order == null) {
                    order = new Order();
                    order.setCode(orderCode);
                    order.setPaymentMethod(rs.getString(2));
                    order.setOrderStartTime(rs.getTimestamp(3));
                    order.setOrderEndTime(rs.getTimestamp(4));
                    order.setNote(rs.getString(5));
                    order.setStatus(OrderStatus.valueOf(rs.getString(6)));

                    Table table = new Table();
                    table.setId(rs.getInt(7));
                    table.setTableNumber(rs.getInt(8));
                    table.setNumOfSeats(rs.getInt(9));
                    order.setTable(table);

                    User user = new User();
                    user.setId(rs.getInt(10));
                    user.setRole(UserRole.valueOf(rs.getString(11)));
                    user.setFullname(rs.getString(12));
                    user.setUsername(rs.getString(13));
                    order.setUser(user);

                    order.setOrderItems(new ArrayList<>());
                    orderMap.put(orderCode, order);
                    orders.add(order);
                }

                Product product = new Product();
                product.setId(rs.getInt(16));
                product.setName(rs.getString(17));
                product.setPrice(rs.getBigDecimal(18));
                product.setImage(rs.getString(19));
                product.setDescription(rs.getString(20));
                product.setStatus(rs.getString(21));

                OrderItem orderItem = new OrderItem();
                orderItem.setCode(order.getCode());
                orderItem.setProduct(product);
                orderItem.setQuantity(rs.getInt(15));
                order.getOrderItems().add(orderItem);
            }

            if (searchResult != null) {
                for (Order order : searchResult) {
                    if (order.getStatus().toString().equals(statusType)) {
                        filteredProducts.add(order);
                    }
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


    // tổng hóa đơn trong 1 ngày
    public int totalOrderInDay() {
        try {
            String sql = "SELECT COUNT(*) FROM orders WHERE DATE(order_start_time) = CURDATE();";
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("totalOrderInDay(): " + e.getMessage());
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return 0;
    }

    //Tổng doanh thu trong 1 ngày
    public BigDecimal totalRevenueInDay() {
        try {
            String sql = "SELECT SUM(p.price * oi.qty) FROM orders o JOIN order_items oi ON o.code = oi.code JOIN products p ON oi.product_id = p.id WHERE DATE(o.order_start_time) = CURDATE();";
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        } catch (Exception e) {
            System.out.println("totalRevenueInDay(): " + e.getMessage());
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return BigDecimal.ZERO;
    }

    //Tổng doanh thu trong 1 tháng
    public BigDecimal totalRevenueInMonth() {
        BigDecimal result = BigDecimal.ZERO;
        try {
            String sql = "SELECT SUM(p.price * oi.qty) FROM orders o JOIN order_items oi ON o.code = oi.code JOIN products p ON oi.product_id = p.id WHERE MONTH(o.order_start_time) = MONTH(CURDATE());";
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();
            if (rs.next()) {
                result = rs.getBigDecimal(1);
                if (result == null) {
                    result = BigDecimal.ZERO;
                }
            }
        } catch (Exception e) {
            System.out.println("totalRevenueInMonth(): " + e.getMessage());
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println("Total revenue in month: " + result); // Debugging output
        return result;
    }

    //thống kê doanh thu hằng ngày qua line chart
    public Map<String, BigDecimal> getDailyRevenueData() {
        Map<String, BigDecimal> dailyRevenueData = new LinkedHashMap<>();

        String sql = "SELECT DATE(order_start_time) as order_date, SUM(p.price * oi.qty) as total_revenue " +
                "FROM orders o " +
                "JOIN order_items oi ON o.code = oi.code " +
                "JOIN products p ON oi.product_id = p.id " +
                "GROUP BY DATE(order_start_time) " +
                "ORDER BY DATE(order_start_time);";

        try (PreparedStatement stm = connection.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                String date = rs.getString("order_date");
                BigDecimal totalRevenue = rs.getBigDecimal("total_revenue");
                dailyRevenueData.put(date, totalRevenue);
            }

        } catch (Exception e) {
            System.out.println("getDailyRevenueData(): " + e.getMessage());
        }

        return dailyRevenueData;
    }



    public static void main(String[] args) {
        OrderDAO orderDAO = new OrderDAO();
        BigDecimal total = orderDAO.totalRevenueInMonth();
        System.out.println(total);


    }

}
