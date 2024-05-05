package vn.aptech.c2304l.learning.model;


import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.aptech.c2304l.learning.constant.UserStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    int id;
    String role;
    String fullname;
    String username;
    String password;
    UserStatus status;
}
