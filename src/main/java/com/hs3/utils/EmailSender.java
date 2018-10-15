package com.hs3.utils;

import java.io.File;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;

public class EmailSender {
    private static Logger logger = LoggerFactory.getLogger(EmailSender.class);

    public static boolean send(String host, String from, String to, String user, String password, String subject, String content, File attach) {
        Properties props = new Properties();
        if (host.indexOf("smtp.gmail.com") >= 0) {
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.port", "587");
        }
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true");
        try {
            Session mailSession = Session.getInstance(props);
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(from));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            message.setSubject(subject);


            Multipart multipart = new MimeMultipart();

            BodyPart bodyPartTxt = new MimeBodyPart();
            bodyPartTxt.setContent(content, "text/html;charset=utf8");
            multipart.addBodyPart(bodyPartTxt);
            if (attach != null) {
                BodyPart bodyPartAttach = new MimeBodyPart();
                DataSource dataSource = new FileDataSource(attach);
                bodyPartAttach.setDataHandler(new DataHandler(dataSource));
                bodyPartAttach.setFileName(attach.getName());
                bodyPartAttach.setHeader("Content-ID", "<" + attach.getName() + ">");
                multipart.addBodyPart(bodyPartAttach);
            }
            message.setContent(multipart);


            message.setSentDate(new Date());
            message.saveChanges();
            Transport transport = mailSession.getTransport("smtp");
            transport.connect(host, user, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    public static boolean send(String host, String from, String to, String user, String password, String subject, String content) {
        return send(host, from, to, user, password, subject, content, null);
    }
}
