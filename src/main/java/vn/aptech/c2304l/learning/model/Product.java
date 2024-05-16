package vn.aptech.c2304l.learning.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.aptech.c2304l.learning.constant.ProductStatus;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    int id;
    Category category;
    String name;
    BigDecimal price;
    String image;
    ProductStatus status;
    String description;

}
