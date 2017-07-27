package com.dawid;

import org.springframework.core.io.ClassPathResource;


import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Dawid on 2017-01-23.
 */
public class RequestOptions extends JFrame{


    JPanel mainPanel;
    JButton holidaysBtn;
    JButton hoursBtn;
    JButton shiftBtn;
    GroupLayout layout;
    JLabel holLabel;
    JLabel houLabel;
    JLabel shiftLabel;
    JSeparator sep;
    JSeparator sep2;
    JSeparator sep3;


    public RequestOptions(){
        super("HolidayApp - Options");

        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        RequestOptions.super.setResizable(false);
        RequestOptions.super.setDefaultCloseOperation(EXIT_ON_CLOSE);

        Dimension dPanel = new Dimension(490, 195);
        mainPanel = new JPanel();
        mainPanel.setPreferredSize(dPanel);
        holLabel = new JLabel("Holidays");
        houLabel = new JLabel("Hours change");
        shiftLabel = new JLabel("Shift change");
        sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setMaximumSize(new Dimension(100, 1));
        sep2 = new JSeparator(SwingConstants.HORIZONTAL);
        sep2.setMaximumSize(new Dimension(100, 1));
        sep3 = new JSeparator(SwingConstants.HORIZONTAL);
        sep3.setMaximumSize(new Dimension(100, 1));
        holidaysBtn = new JButton();
        hoursBtn = new JButton();
        shiftBtn = new JButton();
        try {
            URL holImg = new ClassPathResource("sunbed.png").getURL();
            URL clockImg = new ClassPathResource("clock.png").getURL();
            URL rotate = new ClassPathResource("rotate.png").getURL();
            holidaysBtn.setIcon(new ImageIcon(holImg));
            hoursBtn.setIcon(new ImageIcon(clockImg));
            shiftBtn.setIcon(new ImageIcon(rotate));
            holidaysBtn.setBorderPainted(false);
            hoursBtn.setBorderPainted(false);
            shiftBtn.setBorderPainted(false);

        } catch (IOException e) {
            e.printStackTrace();
        }

        holidaysBtn.addActionListener((ActionEvent a) -> {
            new Holiday();
            this.dispose();
        });
        hoursBtn.addActionListener((ActionEvent a) -> {
            new HourChange();
            this.dispose();
        });
        shiftBtn.addActionListener((ActionEvent a) -> {
            new ShiftChange();
            this.dispose();
        });

        mainPanel.setLayout(setLayout());

        super.getContentPane().add(mainPanel);
        super.pack();
        super.setLocationRelativeTo(null);
        super.setVisible(true);

    }

    GroupLayout setLayout(){
        layout = new GroupLayout(mainPanel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup()
                                .addComponent(holLabel, GroupLayout.Alignment.CENTER)
                                .addComponent(sep, GroupLayout.Alignment.CENTER)
                                .addComponent(holidaysBtn, GroupLayout.Alignment.CENTER)
                        )
                        .addGroup(layout.createParallelGroup()
                                .addComponent(houLabel, GroupLayout.Alignment.CENTER)
                                .addComponent(sep2, GroupLayout.Alignment.CENTER)
                                .addComponent(hoursBtn, GroupLayout.Alignment.CENTER)
                        )
                        .addGroup(layout.createParallelGroup()
                                .addComponent(shiftLabel, GroupLayout.Alignment.CENTER)
                                .addComponent(sep3, GroupLayout.Alignment.CENTER)
                                .addComponent(shiftBtn, GroupLayout.Alignment.CENTER)
                        )


        );

        layout.setVerticalGroup(
                layout.createParallelGroup()
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(holLabel)
                                .addComponent(sep)
                                .addComponent(holidaysBtn)

                        )
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(houLabel)
                                .addComponent(sep2)
                                .addComponent(hoursBtn)
                        )
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(shiftLabel)
                                .addComponent(sep3)
                                .addComponent(shiftBtn)
                        )
        );

        return layout;
    }
}
