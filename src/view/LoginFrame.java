package view;

import javax.swing.*;
import java.awt.*;
import module.DatabaseManager;
import module.User;
import module.Admin;
import module.Cashier;

public class LoginFrame extends JFrame {
    private JTextField txtUsername = new JTextField();
    private JPasswordField txtPassword = new JPasswordField();
    private JButton btnLogin = new JButton("Login Masuk");
    private JButton btnExit = new JButton("Tutup Aplikasi");
    private DatabaseManager db;

    public LoginFrame() {
        // 1. Setup Dasar Window
        setTitle("Login - MiraiMono Hobby Shop");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout()); 

        // Koneksi Database
        db = new DatabaseManager();

        // 2. Bagian Judul (ATAS)
        JLabel lblTitle = new JLabel("MiraiMono Hobby Shop", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Verdana", Font.BOLD, 20));
        lblTitle.setForeground(new Color(50, 50, 150)); 
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10)); 
        add(lblTitle, BorderLayout.NORTH);

        // 3. Bagian Form Input (TENGAH)
        JPanel panelForm = new JPanel(new GridLayout(4, 1, 10, 10)); 
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40)); 
        
        panelForm.add(new JLabel("Username:"));
        panelForm.add(txtUsername);
        panelForm.add(new JLabel("Password:"));
        panelForm.add(txtPassword);
        
        add(panelForm, BorderLayout.CENTER);

        // 4. Bagian Tombol (BAWAH)
        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        
        // Styling Tombol Login
        btnLogin.setPreferredSize(new Dimension(140, 40)); 
        btnLogin.setBackground(new Color(50, 150, 50));
        btnLogin.setForeground(Color.WHITE); 
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnLogin.setFocusPainted(false);
        
        // Styling Tombol Exit
        btnExit.setPreferredSize(new Dimension(140, 40));
        btnExit.setBackground(new Color(200, 50, 50)); 
        btnExit.setForeground(Color.WHITE);
        btnExit.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnExit.setFocusPainted(false);

        panelButton.add(btnLogin);
        panelButton.add(btnExit);
        
        add(panelButton, BorderLayout.SOUTH);

        // --- LOGIC LOGIN ---
        btnLogin.addActionListener(e -> {
            String user = txtUsername.getText();
            String pass = new String(txtPassword.getPassword());
            
            User loggedUser = db.validateLogin(user, pass);

            if (loggedUser != null) {
                JOptionPane.showMessageDialog(this, "Selamat Datang di MiraiMono, " + loggedUser.getNama() + "!");
                dispose(); 
                
                if (loggedUser instanceof Admin) {
                     new AdminDashboardFrame(db).setVisible(true);
                } else if (loggedUser instanceof Cashier) {
                     new CashierDashboardFrame(db, (Cashier) loggedUser).setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Username atau Password Salah!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
            }
        });

        // --- LOGIC EXIT ---
        btnExit.addActionListener(e -> {
            // Konfirmasi sebelum keluar
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menutup aplikasi?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0); // Perintah untuk mematikan program total
            }
        });
    }
}