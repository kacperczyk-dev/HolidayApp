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
import java.util.concurrent.TimeUnit;


/**
 * Created by DKacperc on 2017-01-24.
 */
public class Holiday extends JFrame {

    GroupLayout layout;
    JPanel mainPanel;
    JTextArea addNotesArea;
    JDatePickerImpl dFrom;
    JDatePickerImpl dTo;
    JButton sendBtn;
    JButton cancelBtn;
    JSeparator mainSep;
    JSeparator addNSep;
    JLabel mainLab;
    JLabel dfLab;
    JLabel dtLab;
    JLabel addNLab;
    JLabel daysLab;
    JScrollPane scrollPane;

    public Holiday() {
        super("HolidayApp - Holidays");

        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        Holiday.super.setResizable(false);
        Holiday.super.setDefaultCloseOperation(EXIT_ON_CLOSE);

        Dimension dPanel = new Dimension(460, 350);
        mainPanel = new JPanel();
        mainPanel.setPreferredSize(dPanel);
        addNotesArea = new JTextArea();
        addNotesArea.setLineWrap(true);
        scrollPane = new JScrollPane(addNotesArea);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        dFrom = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel()));
        dTo = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel()));
        dFrom.addActionListener( (ActionEvent a) -> {
                    check();
                }
        );
        dTo.addActionListener( (ActionEvent a) -> {
                    check();
                }
        );

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
                                String dddays = Integer.toString(check());
                                sendBtn.setText("");
                                sendBtn.setIcon(loading);
                                sendBtn.setEnabled(false);
                                cancelBtn.setEnabled(false);
                                Date dF = (Date) dFrom.getModel().getValue();
                                Date dT = (Date) dTo.getModel().getValue();
                                Mail m = new Mail(Login.user, Login.pw);
                                m.sendHolidayEmail(dF, dT, dddays, addNotesArea.getText());
                                JOptionPane.showMessageDialog(new JFrame("Message"), "Email sent successfully!");
                                cancelBtn.setEnabled(true);
                                sendBtn.setEnabled(true);
                                sendBtn.setText("Send");
                                sendBtn.setIcon(null);
                                daysLab.setForeground(Color.getColor("16BA16"));
                                daysLab.setText("Message successfully sent");
                                return null;
                    }
                }.execute();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });


        mainSep = new JSeparator(SwingConstants.HORIZONTAL);
        mainSep.setMaximumSize(new Dimension(200, 1));
        addNSep = new JSeparator(SwingConstants.HORIZONTAL);
        addNSep.setMaximumSize(new Dimension(80, 1));

        mainLab = new JLabel("Please specify the exact dates of absence");
        dfLab = new JLabel("Date from");
        dtLab = new JLabel("Date to");
        addNLab = new JLabel("Additional notes");
        daysLab = new JLabel(" ");
        sendBtn.setEnabled(false);
        mainPanel.setLayout(setLayout());

        super.getContentPane().add(mainPanel);
        super.pack();
        super.setLocationRelativeTo(null);
        super.setVisible(true);
    }

    int check() {

        Date dF = (Date) dFrom.getModel().getValue();
        Date dT = (Date) dTo.getModel().getValue();
        int minus = 0;
        int days = 0;
        int notice;
        if(dF != null && dT != null){
            for(int i = 0; i<105; i++){
                Date d = Main.weekends[i];
                if(d != null){
                    if(d.after(dF) && d.before(dT)){
                        minus++;
                    }
                }
            }
            sendBtn.setEnabled(true);
            days = (int) TimeUnit.DAYS.convert(dT.getTime() - dF.getTime(), TimeUnit.MILLISECONDS) + 1 - minus;
            notice = (int) TimeUnit.DAYS.convert(dF.getTime() - new Date().getTime(), TimeUnit.MILLISECONDS);
            if((dT.before(dF)) || ((dF.before(new Date())) || (dT.before(new Date())))){
                daysLab.setText("Dates cannot start or end before today / date to cannot be before date from");
                daysLab.setForeground(Color.RED);
                sendBtn.setEnabled(false);
            }
            else if(days > 5){
                if(notice < 30){
                    daysLab.setText(Integer.toString(days) + " days total (Late: You must give at least a month of notice!)");
                    daysLab.setForeground(Color.RED);
                }
                else{
                    daysLab.setText(Integer.toString(days) + " days total");
                    daysLab.setForeground(Color.BLACK);
                }
            }
            else if(days > 2 && days <= 5) {
                if(notice < 7){
                    daysLab.setText(Integer.toString(days) + " days total (Late: You must give at least a week of notice!)");
                    daysLab.setForeground(Color.RED);
                }
                else{
                    daysLab.setText(Integer.toString(days) + " days total");
                    daysLab.setForeground(Color.BLACK);
                }
            }
            else if(days <= 2){
                if(notice < 3){
                    daysLab.setText(Integer.toString(days) + " days total (Late: You must give at least 3 days of notice!)");
                    daysLab.setForeground(Color.RED);
                }
                else{
                    daysLab.setText(Integer.toString(days) + " days total");
                    daysLab.setForeground(Color.BLACK);
                }
            }

        }
        else{
            daysLab.setText(" ");
            sendBtn.setEnabled(false);
        }
        return days;
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
                                                .addComponent(dfLab)
                                                .addComponent(dFrom)
                                        )
                                        .addGroup(layout.createParallelGroup()
                                                .addComponent(dtLab)
                                                .addComponent(dTo)
                                        )
                                )
                                .addComponent(daysLab)
                                .addGap(15)
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
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(dtLab)
                                                .addComponent(dTo)
                                        )
                                )
                                .addComponent(daysLab)
                                .addGap(15)
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

