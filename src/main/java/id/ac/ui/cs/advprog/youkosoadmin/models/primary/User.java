package id.ac.ui.cs.advprog.youkosoadmin.models.primary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    private String email;
    private String role;
    private Profile profile;
}
