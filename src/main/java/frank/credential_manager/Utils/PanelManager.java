package frank.credential_manager.Utils;

import frank.credential_manager.Models.Password;
import frank.credential_manager.UI.AgregarPassPNL;
import frank.credential_manager.UI.DashboardPNL;
import frank.credential_manager.UI.EditPassPNL;
import frank.credential_manager.UI.IniciarSesionPNL;
import frank.credential_manager.UI.RegistrarPNL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;

public class PanelManager {

    private static PanelManager instance;
    private JPanel mainPanel;
    private Map<String, JPanel> panels = new HashMap<>();

    private PanelManager() {
    }

    public static PanelManager getInstance() {
        if (instance == null) {
            instance = new PanelManager();
        }
        return instance;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public void changePanel(String panelName, Object param) throws Exception {
        if (mainPanel == null) {
            throw new IllegalStateException("Main panel not set");
        }

        JPanel p = panels.get(panelName);
        if (p == null) {
            // Crear panel solo si no existe y agregarlo al Map
            switch (panelName) {
                case "Login":
                    p = new IniciarSesionPNL();
                    break;
                case "Register":
                    p = new RegistrarPNL();
                    break;
                case "Dashboard":
                    p = new DashboardPNL();
                    break;
                case "Add":
                    p = new AgregarPassPNL();
                    break;
                case "Edit":
                    p = new EditPassPNL((Password) param);
                    break;
            }
            panels.put(panelName, p);
        }

        // Actualizar los datos si el panel necesita información actualizada
        if (p instanceof DashboardPNL) {
            ((DashboardPNL) p).initializeDashboard();
        }

        p.setSize(mainPanel.getWidth(), mainPanel.getHeight());
        p.setLocation(0, 0);

        mainPanel.removeAll();
        mainPanel.add(p);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}
