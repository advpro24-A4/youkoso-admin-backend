package id.ac.ui.cs.advprog.youkosoadmin.utils;

import id.ac.ui.cs.advprog.youkosoadmin.models.primary.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    private String message;
    private User user;
}
