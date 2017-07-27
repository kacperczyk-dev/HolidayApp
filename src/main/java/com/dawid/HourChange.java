package com.dawid;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import org.springframework.core.io.ClassPathResource;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;


/**
 * Created by Dawid on 2017-01-25.
 */
public class HourChange extends JFrame {

    GroupLayout layout;
    JPanel mainPanel;
    JTextArea addNotesArea;
    JDatePickerImpl drOn;
    JDatePickerImpl dnOn;
    JComboBox hoursR;
    JComboBox minutesR;
    JComboBox hoursM;
    JComboBox minutesM;
    JButton sendBtn;
    JButton cancelBtn;
    ButtonGroup b1;
    ButtonGroup b2;
    JRadioButton b11;
    JRadioButton b12;
    JRadioButton b21;
    JRadioButton b22;
    JSeparator mainSep;
    JSeparator addNSep;
    JSeparator rnSep;
    JLabel mainLab;
    JLabel rdLab;
    JLabel rhLab;
    JLabel rnLab;
    JLabel ndLab;
    JLabel nhLab;
    JLabel nnLab;
    JLabel addNLab;
    JLabel infoLab;
    JLabel wantLab;
    JLabel makeUpLab;
    JScrollPane scrollPane;

    public HourChange() {
        super("HolidayApp - Hours change");

        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        HourChange.super.setResizable(false);
        HourChange.super.setDefaultCloseOperation(EXIT_ON_CLOSE);

        Dimension dPanel = new Dimension(460, 350);
        mainPanel = new JPanel();
        mainPanel.setPreferredSize(dPanel);

        addNotesArea = new JTextArea();
        addNotesArea.setLineWrap(true);
        scrollPane = new JScrollPane(addNotesArea);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        String[] hours = new String[24];
        String[] minutes = new String[60];
        for(int i = 0; i < 24; i++){
            if(i < 10) {
                hours[i] = new String("0" + i);
            }
            else{
                hours[i] = Integer.toString(i);
            }
        }
        for(int i = 0; i < 60; i++){
            if(i < 10) {
                minutes[i] = new String("0" + i);
            }
            else{
                minutes[i] = Integer.toString(i);
            }
        }
        hoursR = new JComboBox(hours);
        minutesR = new JComboBox(minutes);
        hoursM = new JComboBox(hours);
        minutesM = new JComboBox(minutes);
        hoursR.setMaximumSize(new Dimension(60, 1));
        minutesR.setMaximumSize(new Dimension(60, 1));
        hoursM.setMaximumSize(new Dimension(60, 1));
        minutesM.setMaximumSize(new Dimension(60, 1));

        b1 = new ButtonGroup();
        b2 = new ButtonGroup();
        b11 = new JRadioButton();
        b12 = new JRadioButton();
        b21 = new JRadioButton();
        b22 = new JRadioButton();
        b1.add(b11);
        b1.add(b12);
        b2.add(b21);
        b2.add(b22);
        b11.setText("Come later");
        b21.setText("Come earlier");
        b12.setText("Leave earlier");
        b22.setText("Leave later");
        b11.setSelected(true);
        b21.setSelected(true);
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        drOn = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel()));
        dnOn = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel()));
        drOn.setMaximumSize(new Dimension(150, 1));
        dnOn.setMaximumSize(new Dimension(150, 1));

        drOn.addActionListener( (ActionEvent a) -> {
                    check();
                }
        );
        dnOn.addActionListener( (ActionEvent a) -> {
                    check();
                }
        );

        sendBtn = new JButton("Send");
        sendBtn.addActionListener((ActionEvent a) -> {
            try {
                new SwingWorker<Object, Object>() {
                    @Override
                    protected Object doInBackground() throws Exception {
                            URL url = null;
                            url = new ClassPathResource("balls.gif").getURL();
                            ImageIcon loading = new ImageIcon(url);
                            sendBtn.setText("");
                            sendBtn.setIcon(loading);
                            sendBtn.setEnabled(false);
                            cancelBtn.setEnabled(false);
                            Date dF = (Date) drOn.getModel().getValue();
                            Date dT = (Date) dnOn.getModel().getValue();
                            Mail mail = new Mail(Login.user, Login.pw);
                            String r;
                            String m;
                            if (b11.isSelected()) {
                                r = "come later (at " + hoursR.getSelectedItem().toString() + ":" + minutesR.getSelectedItem().toString() + ")";
                            } else {
                                r = "leave earlier (at " + hoursR.getSelectedItem().toString() + ":" + minutesR.getSelectedItem().toString() + ")";
                            }
                            if (b21.isSelected()) {
                                m = "come earlier (at " + hoursM.getSelectedItem().toString() + ":" + minutesM.getSelectedItem().toString() + ")";
                            } else {
                                m = "leave later (at " + hoursM.getSelectedItem().toString() + ":" + minutesM.getSelectedItem().toString() + ")";
                            }
                            mail.sendChangeEmail(dF, dT, r, m, addNotesArea.getText());
                            JOptionPane.showMessageDialog(new JFrame("Message"), "Email sent successfully!");
                            cancelBtn.setEnabled(true);
                            sendBtn.setEnabled(true);
                            sendBtn.setText("Send");
                            sendBtn.setIcon(null);
                            return null;
                    }
                }.execute();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });

        cancelBtn = new JButton("Go Back");
        cancelBtn.addActionListener((ActionEvent a) -> {
            new RequestOptions();
            this.dispose();
        });


        mainSep = new JSeparator(SwingConstants.HORIZONTAL);
        mainSep.setMaximumSize(new Dimension(200, 1));
        addNSep = new JSeparator(SwingConstants.HORIZONTAL);
        addNSep.setMaximumSize(new Dimension(80, 1));
        rnSep = new JSeparator(SwingConstants.VERTICAL);
        rnSep.setPreferredSize(new Dimension(1, 80));

        mainLab = new JLabel("Please specify the details of the requested change");
        rdLab = new JLabel("Date");
        rhLab = new JLabel("Hour");
        rnLab = new JLabel("Minutes");
        ndLab = new JLabel("Date");
        nhLab = new JLabel("Hour");
        nnLab = new JLabel("Minutes");
        addNLab = new JLabel("State the reason below");
        infoLab = new JLabel(" ");
        wantLab = new JLabel("I need a change on: ");
        makeUpLab = new JLabel("I will make up for it on: ");
        mainPanel.setLayout(setLayout());
        sendBtn.setEnabled(false);

        super.getContentPane().add(mainPanel);
        super.pack();
        super.setLocationRelativeTo(null);
        super.setVisible(true);
    }

    void check() {

        Date dF = (Date) drOn.getModel().getValue();
        Date dT = (Date) dnOn.getModel().getValue();
        if(dF != null && dT != null){
            sendBtn.setEnabled(true);
        }else{
            sendBtn.setEnabled(false);
        }

    }

    GroupLayout setLayout() {
        layout = new GroupLayout(mainPanel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup()
                                .addComponent(mainLab, GroupLayout.Alignment.CENTER)
                                .addComponent(mainSep, GroupLayout.Alignment.CENTER)
                                .addGap(25)
                                .addGroup(GroupLayout.Alignment.CENTER, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup()
                                                .addComponent(wantLab)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(rdLab)
                                                        .addComponent(drOn)
                                                )
                                                .addGroup(layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup()
                                                                .addComponent(b11)
                                                                .addComponent(b12)
                                                        )
                                                        .addGroup(layout.createParallelGroup()
                                                                .addComponent(rhLab, GroupLayout.Alignment.CENTER)
                                                                .addComponent(hoursR)
                                                        )
                                                        .addGroup(layout.createParallelGroup()
                                                                .addComponent(rnLab, GroupLayout.Alignment.CENTER)
                                                                .addComponent(minutesR)
                                                        )
                                                )
                                        )
                                        .addComponent(rnSep)
                                        .addGroup(layout.createParallelGroup()
                                                .addComponent(makeUpLab)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(ndLab)
                                                        .addComponent(dnOn)
                                                )
                                                .addGroup(layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup()
                                                                .addComponent(b21)
                                                                .addComponent(b22)
                                                        )
                                                        .addGroup(layout.createParallelGroup()
                                                                .addComponent(nhLab, GroupLayout.Alignment.CENTER)
                                                                .addComponent(hoursM)
                                                        )
                                                        .addGroup(layout.createParallelGroup()
                                                                .addComponent(nnLab, GroupLayout.Alignment.CENTER)
                                                                .addComponent(minutesM)
                                                        )
                                                )
                                        )
                                )
                                .addComponent(addNLab, GroupLayout.Alignment.CENTER)
                                .addComponent(addNSep, GroupLayout.Alignment.CENTER)
                                .addComponent(addNotesArea)
                                .addGroup(GroupLayout.Alignment.CENTER, layout.createSequentialGroup()
                                        .addComponent(sendBtn)
                                        .addComponent(cancelBtn)
                                )
                        )

        );

        layout.setVerticalGroup(
                layout.createParallelGroup()
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(mainLab)
                                .addComponent(mainSep)
                                .addGap(25)
                                .addGroup(layout.createParallelGroup()
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(wantLab)
                                                .addGroup(layout.createParallelGroup()
                                                        .addComponent(rdLab)
                                                        .addComponent(drOn)
                                                )
                                                .addGroup(layout.createParallelGroup()
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(b11)
                                                                .addComponent(b12)
                                                        )
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(rhLab)
                                                                .addComponent(hoursR)
                                                        )
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(rnLab)
                                                                .addComponent(minutesR)
                                                        )
                                                )
                                        )
                                        .addComponent(rnSep)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(makeUpLab)
                                                .addGroup(layout.createParallelGroup()
                                                        .addComponent(ndLab)
                                                        .addComponent(dnOn)
                                                )
                                                .addGroup(layout.createParallelGroup()
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(b21)
                                                                .addComponent(b22)
                                                        )
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(nhLab)
                                                                .addComponent(hoursM)
                                                        )
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(nnLab)
                                                                .addComponent(minutesM)
                                                        )
                                                )
                                        )
                                )
                                .addComponent(addNLab)
                                .addComponent(addNSep)
                                .addComponent(addNotesArea)
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(sendBtn)
                                        .addComponent(cancelBtn)
                                )
                        )
        );
        return layout;
    }
}

