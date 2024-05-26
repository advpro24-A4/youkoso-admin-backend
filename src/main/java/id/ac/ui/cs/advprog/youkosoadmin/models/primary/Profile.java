package id.ac.ui.cs.advprog.youkosoadmin.models.primary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Profile {
    private String name;
    private String username;
    private String address;
    private String birth_date;
    private String phone_number;
}
