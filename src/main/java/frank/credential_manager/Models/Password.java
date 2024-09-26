package frank.credential_manager.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Password {

    private Long id;
    private String serviceName;
    private String username;
    private String password;
    private String category;

    public Password(String serviceName, String username, String password, String category) {
        this.serviceName = serviceName;
        this.username = username;
        this.password = password;
        this.category = category;
    }

}
