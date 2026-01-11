package main;

import javax.swing.SwingUtilities;
import view.LoginFrame; // Kita import LoginFrame dari package 'view'

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginFrame().setVisible(true);
            }
        });
    }
}