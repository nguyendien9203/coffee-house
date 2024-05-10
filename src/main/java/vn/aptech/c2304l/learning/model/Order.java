package vn.aptech.c2304l.learning.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.aptech.c2304l.learning.constant.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    int id;
    User user;
    List<Product> products;
    Table table;
    String paymentMethod;
    LocalDateTime orderDate;
    int qty;
    String note;
    OrderStatus status;

}
