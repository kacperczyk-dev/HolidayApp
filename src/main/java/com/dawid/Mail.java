package com.dawid;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by Dawid on 2017-01-23.
 */
public class Mail {
    final String username;
    final String password;

    public Mail(String user, String password)
    {
        this.username = user;
        this.password = password;
    }

    public void sendHolidayEmail(Date dateFrom, Date dateTo, String days, String additional)
    {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

        try {
            String lowerName = username.substring(0, username.indexOf('.'));
            String upperName = lowerName.substring(0, 1).toUpperCase() + lowerName.substring(1);
            Message message = new MimeMessage(Main.session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(username));
            message.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(username));
            message.setRecipients(Message.RecipientType.BCC,
                    InternetAddress.parse(username)); //Sender
            message.setSubject("Holiday request // { " + format.format(dateFrom) + " - " + format.format(dateTo) + " }");
            message.setText(
                    "Hello,\n" + "\n"
                            + "I would like to request " + days
                            + " day(s) off from " + format.format(dateFrom) + " through " + format.format(dateTo)
                            + "\n" + "\n" + "\n" + "Additional notes: " + "\n"
                            + additional
                            + " \n" + "\n" + "\n" + "Regards,"
                            + " \n" +
                            upperName);

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendChangeEmail(Date dateR, Date dateM, String r, String m, String reason)
    {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

        try {
            String lowerName = username.substring(0, username.indexOf('.'));
            String upperName = lowerName.substring(0, 1).toUpperCase() + lowerName.substring(1);
            Message message = new MimeMessage(Main.session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(username));
            message.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(username));
            message.setRecipients(Message.RecipientType.BCC,
                    InternetAddress.parse(username)); //Sender
            message.setSubject("Hour change request // { " + format.format(dateR) + "}");
            message.setText(
                    "Hello,\n" + "\n"
                            + "I would like to " + r + " on " + format.format(dateR) + "." + "\n"
                            + "I will " + m + " on " + format.format(dateM) + "." + "\n" + "\n"
                            + "Reason:" + "\n" + reason
                            + " \n" + "\n" + "\n" + "Regards,"
                            + " \n" +
                            upperName);

            Transport.send(message);
        }
        catch (MessagingException e){
            e.printStackTrace();
        }
    }

    public void sendShiftEmail(Date dateR, String r, String reason)
    {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

        try {
            String lowerName = username.substring(0, username.indexOf('.'));
            String upperName = lowerName.substring(0, 1).toUpperCase() + lowerName.substring(1);
            Message message = new MimeMessage(Main.session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(username));
            message.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(username));
            message.setRecipients(Message.RecipientType.BCC,
                    InternetAddress.parse(username)); //Sender
            message.setSubject("Hour change request // { " + format.format(dateR) + "}");
            message.setText(
                    "Hello,\n" + "\n"
                            + "I would like to" + r + "on " + format.format(dateR) + "." + "\n" + "\n"
                            + "Reason:" + "\n" + reason
                            + " \n" + "\n" + "\n" + "Regards,"
                            + " \n" +
                            upperName);

            Transport.send(message);
        }
        catch (MessagingException e){
            e.printStackTrace();
        }
    }
}

