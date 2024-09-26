package frank.credential_manager.Authentication;

import frank.credential_manager.Models.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSession {

    private static UserSession instance;
    private User usuario;

    private UserSession() {
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void cerrarSesion() {
        if (this.usuario != null) {
            this.usuario = null;
        }
    }
}
