package com.dawid;



import org.springframework.core.io.ClassPathResource;

import javax.mail.*;
import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Properties;

/**
 * Created by Dawid on 2017-01-22.
 */

public class Login extends JFrame{
    static String user;
    static String pw;

    StringBuilder stb;
    Base64.Encoder encoder;
    Base64.Decoder decoder;
    Path dir;


    GroupLayout layout;
    JPanel mainPanel;
    JTextField emailTxt;
    JPasswordField pwTxt;
    JCheckBox remChck;
    JButton loginBtn;
    JLabel infoLab;
    JLabel errorLab;
    JSeparator sep;


    public Login() {

        super("HolidayApp - Login");

        encoder = java.util.Base64.getEncoder();
        decoder = java.util.Base64.getDecoder();
        dir = new File("C:\\Users\\" + System.getProperty("user.name").toLowerCase() + "\\HolidayApp\\Login").toPath();

        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        Login.super.setResizable(false);
        Login.super.setDefaultCloseOperation(EXIT_ON_CLOSE);

        Dimension dPanel = new Dimension(283, 200);
        mainPanel = new JPanel();
        mainPanel.setPreferredSize(dPanel);
        Dimension dTxt = new Dimension(270, 30);
        emailTxt = new JTextField();
        pwTxt = new JPasswordField();
        emailTxt.setMaximumSize(dTxt);
        pwTxt.setMaximumSize(dTxt);
        loginBtn = new JButton("Log in");
        remChck = new JCheckBox("Remember me");
        errorLab = new JLabel(" ");
        errorLab.setForeground(Color.RED);
        infoLab = new JLabel("Please enter your credentials ");
        sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setMaximumSize(new Dimension(200, 1));
        mainPanel.setLayout(setLayout());

        if (Files.exists(dir)) {
            try {
                byte[] bytes;
                bytes = Files.readAllBytes(dir);
                if (bytes != null) {
                    String s = new String(decoder.decode(bytes));
                    String[] arr = s.split(System.lineSeparator());
                    if (arr[0] != null && arr[1] != null) {
                        emailTxt.setText(arr[0]);
                        pwTxt.setText(arr[1]);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.getContentPane().add(mainPanel);
        super.pack();
        super.setLocationRelativeTo(null);
        super.setVisible(true);

        loginBtn.addActionListener((ActionEvent a) -> {

            try {
                new SwingWorker<Object, Object>() {
                    @Override
                    protected Object doInBackground() throws Exception {
                            URL url = null;
                            url = new ClassPathResource("balls.gif").getURL();
                            ImageIcon loading = new ImageIcon(url);
                            loginBtn.setText("");
                            loginBtn.setIcon(loading);
                            loginBtn.setEnabled(false);
                            if (authenticate()) {
                                Login.user = emailTxt.getText();
                                Login.pw = new String(pwTxt.getPassword());
                                errorLab.setText(" ");
                                if (remChck.isSelected()) {
                                    stb = new StringBuilder();
                                    try {
                                        if (!Files.exists(dir)) {
                                            Files.createDirectories(dir.getParent());
                                            Files.createFile(dir);
                                        }
                                        stb.append(emailTxt.getText());
                                        stb.append(System.lineSeparator());
                                        stb.append(new String(pwTxt.getPassword()));
                                        Files.write(dir, encoder.encode(stb.toString().getBytes()));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                new RequestOptions();
                                Login.super.dispose();
                            } else {
                                errorLab.setText("Email and/or password incorrect!");
                            }
                            loginBtn.setIcon(null);
                            loginBtn.setText("Log in");
                            loginBtn.setEnabled(true);
                            return null;
                    }
                }.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    GroupLayout setLayout() {
        layout = new GroupLayout(mainPanel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);


        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup()
                                .addComponent(infoLab, GroupLayout.Alignment.CENTER)
                                .addComponent(sep, GroupLayout.Alignment.CENTER)
                                .addComponent(errorLab, GroupLayout.Alignment.CENTER)
                                .addComponent(emailTxt, GroupLayout.Alignment.CENTER)
                                .addComponent(pwTxt, GroupLayout.Alignment.CENTER)
                                .addComponent(remChck, GroupLayout.Alignment.CENTER)
                                .addComponent(loginBtn, GroupLayout.Alignment.CENTER))
        );

        layout.setVerticalGroup(
                layout.createParallelGroup()
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(infoLab)
                                .addComponent(sep)
                                .addGap(10)
                                .addComponent(errorLab)
                                .addComponent(emailTxt)
                                .addComponent(pwTxt)
                                .addComponent(remChck)
                                .addGap(10)
                                .addComponent(loginBtn))
        );

        return layout;
    }

    boolean authenticate() {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "outlook.office365.com");
        props.put("mail.smtp.port", "587");

        Main.session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailTxt.getText(), new String(pwTxt.getPassword()));
                    }
                });
        Store store = null;
        boolean isConn = false;
        try {
            store = Main.session.getStore("pop3s");
            store.connect("outlook.office365.com", emailTxt.getText(), new String(pwTxt.getPassword()));
            isConn = store.isConnected();
            store.close();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return isConn;
    }
}
