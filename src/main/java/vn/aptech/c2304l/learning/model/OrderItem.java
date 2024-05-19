package vn.aptech.c2304l.learning.model;

import com.jfoenix.transitions.CachedTransition;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private int id;
    private String code;
    private Product product;
    private int quantity;
}
