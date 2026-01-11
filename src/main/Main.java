package main; // <--- Ini wajib 'main' karena filenya ada di folder 'main'

import javax.swing.SwingUtilities;
import view.LoginFrame; // Kita import LoginFrame dari package 'view'

public class Main {
    public static void main(String[] args) {
        // Swing harus dijalankan di Event Dispatch Thread agar aman
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Memanggil LoginFrame yang ada di package view
                new LoginFrame().setVisible(true);
            }
        });
    }
}