package vn.aptech.c2304l.learning.model;


import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.aptech.c2304l.learning.constant.UserRole;
import vn.aptech.c2304l.learning.constant.UserStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    int id;
    UserRole role;
    String username;
    String password;
    UserStatus status;
}
