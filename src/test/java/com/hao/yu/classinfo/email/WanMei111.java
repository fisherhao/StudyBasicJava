package com.hao.yu.classinfo.email;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class WanMei111 {

    private static final String password = "hMYzTrfvbAAWiGfU";

    private static final String smtp = "smtp.111.com";

    private static final String port = "465";

    private static final Properties properties = new Properties();

    private static final String userName = "hao.yu@111.com"; // 你的邮箱用户名

    private static final Authenticator auth;

    private static final Session session;

    static {

        properties.put("mail.smtp.host", smtp);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        // 新建一个认证器
        auth = new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };

        // 新建一个会话
        session = Session.getInstance(properties, auth);
    }

    /**
     * 发送邮件
     *
     * @param toAddress
     *         接收人
     * @param subject
     *         主题
     * @param emailBody
     *         内容
     * @param path
     *         附件路径
     *
     * @throws MessagingException
     *         异常
     */
    private static void sendmail(String toAddress, String subject, String emailBody, String path)
            throws MessagingException {
        // 新建一个消息
        MimeMessage message = new MimeMessage(session);

        // 设置发件人
        message.setFrom(new InternetAddress(userName));

        // 设置收件人
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));

        // 设置邮件主题
        message.setSubject(subject);

        // 创建邮件正文
        BodyPart contentPart = new MimeBodyPart();
        contentPart.setContent(emailBody, "text/html; charset=utf-8");

        // 创建附件部分
        BodyPart attachmentPart = new MimeBodyPart();
        DataSource source = new FileDataSource(path);
        attachmentPart.setDataHandler(new DataHandler(source));
        attachmentPart.setFileName(source.getName());

        // 创建附件列表
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(contentPart);
        multipart.addBodyPart(attachmentPart);

        // 设置邮件内容（包含附件）
        message.setContent(multipart);

        // 发送邮件
        Transport.send(message);
    }

    private static void exe1() throws IOException, MessagingException {
        List<String> fileName = EmailFileUtil.getFileName();

        List<String> files = EmailFileUtil.getFiles();

        sendmail("xxxx@qq.com", "【" + fileName.get(new Random().nextInt(fileName.size())) + "】",
                "【" + fileName.get(new Random().nextInt(fileName.size())) + "】",
                files.get(new Random().nextInt(files.size())));

    }

    public static void main(String[] args) throws IOException, MessagingException {
        exe1();
    }
}
