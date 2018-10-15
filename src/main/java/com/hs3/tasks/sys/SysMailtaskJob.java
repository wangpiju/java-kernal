package com.hs3.tasks.sys;

import com.hs3.db.Page;
import com.hs3.entity.sys.ResettleMail;
import com.hs3.entity.sys.SysMailtask;
import com.hs3.service.sys.ResettleMailService;
import com.hs3.service.sys.SysMailtaskService;
import com.hs3.web.utils.SpringContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
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
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SysMailtaskJob
        implements Job {
    private static final Logger logger = LoggerFactory.getLogger(SysMailtaskJob.class);
    private static final int THREADPOOL_SIZE = 10;
    private ResettleMailService resettleMailService = (ResettleMailService) SpringContext.getBean(ResettleMailService.class);
    private SysMailtaskService sysMailtaskService = (SysMailtaskService) SpringContext.getBean(SysMailtaskService.class);

    public void execute(JobExecutionContext paramJobExecutionContext)
            throws JobExecutionException {
        List<SysMailtask> sysMailtaskList2 = new ArrayList<>();
        List<SysMailtask> sysMailtaskList3 = new ArrayList<>();
        for (SysMailtask sysMailtask : this.sysMailtaskService.listByStatus(0, new Page(1, 10))) {
            if (sysMailtask.getType() == 2) {
                sysMailtaskList2.add(sysMailtask);
            } else if (sysMailtask.getType() == 3) {
                sysMailtaskList3.add(sysMailtask);
            }
        }
        if (!sysMailtaskList2.isEmpty()) {
            for (ResettleMail mail : this.resettleMailService.listByStatusAndType(2, 0)) {
                send(mail.getHost(), mail.getSendAddress(), mail.getAddress(), mail.getUser(), mail.getPassword(), sysMailtaskList2);
            }
        }
        if (!sysMailtaskList3.isEmpty()) {
            for (ResettleMail mail : this.resettleMailService.listByStatusAndType(3, 0)) {
                send(mail.getHost(), mail.getSendAddress(), mail.getAddress(), mail.getUser(), mail.getPassword(), sysMailtaskList3);
            }
        }
    }

    private void send(String host, String from, String to, String user, String password, List<SysMailtask> sysMailtaskList) {
        Transport transport = null;
        try {
            Properties props = new Properties();
            if (host.indexOf("smtp.gmail.com") >= 0) {
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.port", "587");
            }
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.auth", "true");

            Session mailSession = Session.getInstance(props);
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));


            message.setSentDate(new Date());
            transport = mailSession.getTransport("smtp");
            transport.connect(host, user, password);
            for (SysMailtask sysMailtask : sysMailtaskList) {
                message.setSubject(sysMailtask.getTitle());
                Multipart multipart = new MimeMultipart();

                BodyPart bodyPartTxt = new MimeBodyPart();
                bodyPartTxt.setContent(sysMailtask.getContent(), "text/html;charset=utf8");
                multipart.addBodyPart(bodyPartTxt);

                message.setContent(multipart);
                message.saveChanges();

                transport.sendMessage(message, message.getAllRecipients());

                logger.info("send email(" + sysMailtask.getType() + ") success" + ",title{" + sysMailtask.getTitle() + "}, content{" + sysMailtask.getContent() + "}");

                this.sysMailtaskService.updateByStatus(sysMailtask.getId(), 1, 0);
            }
        } catch (Exception e) {
            logger.error("send email exception:" + e.getMessage(), e);
            if (transport != null) {
                try {
                    transport.close();
                } catch (Exception localException1) {
                }
            }
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (Exception localException2) {
                }
            }
        }
    }
}
