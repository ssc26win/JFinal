package com.shangsc.platform.mail;

import com.shangsc.platform.mail.core.JavaMailSender;
import com.shangsc.platform.mail.core.MimeMessageHelper;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.*;

/**
 * 发送邮件工具
 *
 * @author farmer
 */
public class MailKit {

    static Map<String, MailPro> proMap = new HashMap<String, MailPro>();

    static MailPro mailPro = null;

    /**
     * @param configName
     * @param mailPro
     */
    static void init(String configName, MailPro mailPro) {
        if (proMap.get(configName) != null) {
            throw new RuntimeException(configName + "配置的Mail已经存在！");
        }
        proMap.put(configName, mailPro);
        if (MailPlugin.MAIN_CONFIG.equals(configName)) {
            MailKit.mailPro = mailPro;
        }
    }

    /**
     * @param configName
     * @return
     */
    public static MailPro use(String configName) {
        MailPro mailPro = proMap.get(configName);
        if (mailPro == null) {
            throw new RuntimeException(configName + "配置的Mail不存在！");
        }
        return mailPro;
    }

    /**
     * 发送邮件
     *
     * @param to      收件人
     * @param cc      发件人
     * @param subject 主题
     * @param text    内容
     */
    public static void send(String to, List<String> cc, String subject, String text) {
        mailPro.send(to, cc, subject, text);
    }

    final static String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

    public static String sendEmail465(String subject, String content, final String toEmailAddress) {

        final String fromEmailAddress = "ssc23win@163.com";

        // Properties properties = System.getProperties();// 获取系统属性
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", "smtp.163.com"); // 设置邮件服务器
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");// 发送服务器需要身份验证
        final String username = "ssc23win@163.com";
        final String password = "shichao1990";

        Session session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        MimeMessage msg = new MimeMessage(session);// 创建默认的 MimeMessage 对象
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(msg, "UTF-8");
            mimeMessageHelper.setText(content, true);

            msg.setFrom(new InternetAddress(fromEmailAddress));//设置发件
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmailAddress));//设置收件
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.saveChanges();
            Transport.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "发送邮件成功";
    }

    /**
     * 发送邮件
     *
     * @param to          收件人
     * @param cc          发件人
     * @param subject     主题
     * @param text        内容
     * @param attachments 附件
     */
    public static void send(String to, List<String> cc, String subject, String text, List<File> attachments) {
        mailPro.send(to, cc, subject, text, attachments);
    }

    /**
     * @param to
     * @param cc
     * @param subject
     * @param viewpath
     * @param dataMap
     */
    public static void send(String to, List<String> cc, String subject, String viewpath, Map<String, Object> dataMap) {
        mailPro.send(to, cc, subject, viewpath, dataMap);
    }

    /**
     * @param to
     * @param cc
     * @param subject
     * @param viewpath
     * @param dataMap
     * @param attachments
     */
    public static void send(String to, List<String> cc, String subject, String viewpath, Map<String, Object> dataMap, List<File> attachments) {
        mailPro.send(to, cc, subject, viewpath, dataMap, attachments);
    }

    /**
     * 获取JavaMailSender
     *
     * @return 当前实例的JavaMailSender，用来定制化更复杂的邮件发送需求
     */
    public static JavaMailSender getMailSender() {
        return mailPro.getMailSender();
    }

}
