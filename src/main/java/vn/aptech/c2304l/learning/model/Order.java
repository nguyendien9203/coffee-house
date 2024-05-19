package vn.aptech.c2304l.learning.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.aptech.c2304l.learning.constant.OrderStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String code;
    private User user;
    private Table table;
    private String paymentMethod;
    private Timestamp orderStartTime;
    private Timestamp orderEndTime;
    private String note;
    private OrderStatus status;
    private List<OrderItem> orderItems;

    public BigDecimal calculateTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            BigDecimal itemTotal = item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity()));
            total = total.add(itemTotal);
        }
        return total;
    }

    public boolean containsProduct(Product product) {
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getProduct().equals(product)) {
                return true;
            }
        }
        return false;
    }

    public void updateProductQuantity(Product product, int quantity) {
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getProduct().equals(product)) {
                orderItem.setQuantity(quantity);
                return;
            }
        }
    }

    public void addOrderItems(List<OrderItem> newOrderItems) {
        orderItems.addAll(newOrderItems);
    }
}
