package com.dawid;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import org.springframework.core.io.ClassPathResource;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Date;

/**
 * Created by DKacperc on 2017-02-02.
 */
public class ShiftChange extends JFrame {
    GroupLayout layout;
    JPanel mainPanel;
    JTextArea addNotesArea;
    JDatePickerImpl dFrom;
    JButton sendBtn;
    JButton cancelBtn;
    JSeparator mainSep;
    JSeparator addNSep;
    JLabel mainLab;
    JLabel dfLab;
    JLabel addNLab;
    JLabel warningLAb;
    JScrollPane scrollPane;
    ButtonGroup b1;
    JRadioButton b11;
    JRadioButton b12;

    public ShiftChange(){
        super("HolidayApp - Shift change");

        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        ShiftChange.super.setResizable(false);
        ShiftChange.super.setDefaultCloseOperation(EXIT_ON_CLOSE);

        Dimension dPanel = new Dimension(300, 350);
        mainPanel = new JPanel();
        mainPanel.setPreferredSize(dPanel);
        addNotesArea = new JTextArea();
        addNotesArea.setLineWrap(true);
        scrollPane = new JScrollPane(addNotesArea);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        b1 = new ButtonGroup();
        b11 = new JRadioButton("Late -> Normal");
        b12 = new JRadioButton("Normal -> Late");
        b1.add(b11);
        b1.add(b12);
        b11.setSelected(true);
        dFrom = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel()));
        dFrom.setSize(new Dimension(100, 5));
        sendBtn = new JButton("Send");
        cancelBtn = new JButton("Go Back");
        cancelBtn.addActionListener((ActionEvent a) -> {
            new RequestOptions();
            this.dispose();
        });
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
                        Date dF = (Date) dFrom.getModel().getValue();
                        Mail m = new Mail(Login.user, Login.pw);
                        String txt;
                        if (b11.isSelected()) {
                            txt = " change shift from late to normal ";
                        } else {
                            txt = " change shift from normal to late ";
                        }
                        m.sendShiftEmail(dF, txt, addNotesArea.getText());
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

        dFrom.addActionListener((ActionEvent ad)-> {
            Date dF = (Date) dFrom.getModel().getValue();
            if(dF.before(new Date())){
                warningLAb.setText("Date cannot be in the past!");
                warningLAb.setForeground(Color.RED);
                sendBtn.setEnabled(false);
            } else{
                sendBtn.setEnabled(true);
                warningLAb.setText(" ");
            }

        });

        mainSep = new JSeparator(SwingConstants.HORIZONTAL);
        mainSep.setMaximumSize(new Dimension(200, 1));
        addNSep = new JSeparator(SwingConstants.HORIZONTAL);
        addNSep.setMaximumSize(new Dimension(80, 1));

        mainLab = new JLabel("Please specify the date of the shift change");
        dfLab = new JLabel("Date");
        addNLab = new JLabel("Additional notes");
        warningLAb = new JLabel(" ");
        mainPanel.setLayout(setLayout());
        sendBtn.setEnabled(false);

        super.getContentPane().add(mainPanel);
        super.pack();
        super.setLocationRelativeTo(null);
        super.setVisible(true);
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
                                .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup()
                                                .addComponent(dfLab, GroupLayout.Alignment.CENTER)
                                                .addComponent(dFrom, GroupLayout.Alignment.CENTER)
                                        )
                                )
                                .addComponent(warningLAb, GroupLayout.Alignment.CENTER)
                                .addComponent(b11)
                                .addComponent(b12)
                                .addGap(10)
                                .addComponent(addNLab, GroupLayout.Alignment.CENTER)
                                .addComponent(addNSep, GroupLayout.Alignment.CENTER)
                                .addComponent(scrollPane)
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
                                                .addComponent(dfLab)
                                                .addComponent(dFrom)
                                        )
                                )
                                .addComponent(warningLAb)
                                .addComponent(b11)
                                .addComponent(b12)
                                .addGap(10)
                                .addComponent(addNLab)
                                .addComponent(addNSep)
                                .addComponent(scrollPane)
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(sendBtn)
                                        .addComponent(cancelBtn)
                                )
                        )
        );
        return layout;
    }
}
