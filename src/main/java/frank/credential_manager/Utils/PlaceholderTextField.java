package frank.credential_manager.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class PlaceholderTextField extends JTextField {

    private String placeholder;
    private Color placeholderColor;

    // Constructor
    public PlaceholderTextField(String placeholder) {
        this.placeholder = placeholder;
        this.placeholderColor = Color.GRAY; // Color del placeholder por defecto
        initPlaceholder();
    }

    // Inicializar el comportamiento del placeholder
    private void initPlaceholder() {
        setForeground(placeholderColor); // Texto en gris
        super.setText(placeholder); // Mostrar el placeholder

        // Agregar FocusListener para manejar el placeholder
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // Si el texto es el placeholder, al ganar el foco lo eliminamos
                if (getText().equals(placeholder)) {
                    setText("");
                    setForeground(Color.BLACK); // Cambiar a color de texto normal
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                // Si el campo está vacío al perder el foco, volvemos a mostrar el placeholder
                if (getText().isEmpty()) {
                    setForeground(placeholderColor); // Texto en gris
                    setText(placeholder);
                }
            }
        });
    }

    @Override
    public String getText() {
        // Si el texto es el placeholder, devolver una cadena vacía en lugar del placeholder
        String text = super.getText();
        if (text.equals(placeholder)) {
            return "";
        }
        return text;
    }

    @Override
    public void setText(String t) {
        // Si el texto es vacío, mostrar el placeholder
        if (t.isEmpty()) {
            setForeground(placeholderColor); // Texto en gris
            super.setText(placeholder);
        } else {
            setForeground(Color.BLACK); // Texto en negro
            super.setText(t);
        }
    }

    // Método para cambiar el texto del placeholder
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        if (getText().isEmpty()) {
            setText(placeholder);
            setForeground(placeholderColor);
        }
    }

    // Método para cambiar el color del placeholder
    public void setPlaceholderColor(Color color) {
        this.placeholderColor = color;
        if (super.getText().equals(placeholder)) {
            setForeground(color);
        }
    }
}
