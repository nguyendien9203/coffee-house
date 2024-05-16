package vn.aptech.c2304l.learning.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.aptech.c2304l.learning.constant.OrderStatus;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    int id;
    User user;
    Map<Product, Integer> products = new HashMap<>();
    Table table;
    String paymentMethod;
    Timestamp orderDate;
    String note;
    OrderStatus status;
}
