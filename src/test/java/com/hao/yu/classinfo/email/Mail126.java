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
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Mail126 {

    private static final Properties properties = new Properties();

    private static final String host = "smtp.126.com"; // SMTP服务器地址
    private static final String port = "465"; // SMTP服务器端口
    private static final String userName = "wittyhao@126.com"; // 你的邮箱用户名
    private static final String password = "XF5uQYK2paQBqqLA"; // 你的邮箱密码

    private static final Authenticator auth;

    private static final Session session;

    static {
        // 设置SMTP服务器属性

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        // 新建一个认证器
        auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };

        // 新建一个会话
        session = Session.getInstance(properties, auth);
    }

    public static void sendEmailWithAttachment(String toAddress, String subject, String emailBody,
                                               String attachmentPath) throws MessagingException {

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
        DataSource source = new FileDataSource(attachmentPath);
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

    public static void main(String[] args) throws IOException {

        System.out.println("开始执行");
        exe3();
        System.out.println("结束执行");

    }

    private static void exe2() throws IOException {

        List<String> files = getFiles();

        List<String> fileNames = getFileName();

        ExecutorService executorService = Executors.newFixedThreadPool(60);
        for (int i = 0; i < 30; i++) {
            executorService.execute(() -> {
                try {

                    System.out.println(Thread.currentThread().getName() + "开始执行");
                    TimeUnit.SECONDS.sleep(20 + new Random().nextInt(300));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String sub = "【" + fileNames.get(new Random().nextInt(fileNames.size())) + "】";
                String body = "【发送文件过来：" + fileNames.get(new Random().nextInt(fileNames.size())) + "】";
                String path = files.get(new Random().nextInt(files.size()));
                sendmail(sub, body, path);
            });
        }

    }

    private static void exe3() throws IOException {

        List<String> files = getFiles();

        List<String> fileNames = getFileName();

        String sub = "【" + fileNames.get(new Random().nextInt(fileNames.size())) + "】";
        String body = "【发送文件过来：" + fileNames.get(new Random().nextInt(fileNames.size())) + "】";
        String path = files.get(new Random().nextInt(files.size()));
        System.out.println("sub:" + sub + ",body:" + body + ",path:" + path);
        sendmail(sub, body, path);
    }

    private static List<String> getFiles() throws IOException {

        String folder = "/Users/Witty·Kid Fisher/Downloads/06网盘/";

        List<String> collect = Files
                .list(Paths.get(folder))
                .map(t -> t.getFileName().toString())
                .filter(t -> !t.startsWith("."))
                .map(t -> folder + t)
                .collect(Collectors.toList());

        return collect;
    }

    private static List<String> getFileName() throws IOException {

        String folder = "/Users/Witty·Kid Fisher/Downloads/06网盘/";

        List<String> collect = Files
                .list(Paths.get(folder))
                .map(t -> t.getFileName().toString())
                .filter(t -> !t.startsWith("."))
                .collect(Collectors.toList());

        return collect;
    }

    private static void exe() {

        ExecutorService executorService = Executors.newFixedThreadPool(30);
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                try {

                    System.out.println(Thread.currentThread().getName() + "开始执行");
                    TimeUnit.SECONDS.sleep(5 + new Random().nextInt(30));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendmail();
                System.out.println(Thread.currentThread().getName() + "结束执行");
            });
        }

    }

    private static void sendmail(String subject, String emailBody, String attachmentPath) {

        try {
            String toAddress = "xxxx@qq.com"; // 收件人邮箱

            long l = System.currentTimeMillis();

            sendEmailWithAttachment(toAddress, subject, emailBody, attachmentPath);
            System.out.println("耗时：" + (System.currentTimeMillis() - l) / 1000 + "ms");
            System.out.println("Email sent successfully.");
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static void sendmail(String attachmentPath) {

        // SMTP服务器地址
        // SMTP服务器端口
        // 你的邮箱用户名
        // 你的邮箱密码
        String toAddress = "xxxx@qq.com"; // 收件人邮箱
        String subject = "Email with Attachment" + System.currentTimeMillis(); // 邮件主题
        String emailBody = "This is an email with an attachment."; // 邮件正文

        try {
            long l = System.currentTimeMillis();

            sendEmailWithAttachment(toAddress, subject, emailBody, attachmentPath);
            System.out.println("耗时：" + (System.currentTimeMillis() - l) / 1000 + "ms");
            System.out.println("Email sent successfully.");
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static void sendmail() {
        String attachmentPath = "/Users/Witty·Kid Fisher/Downloads/06网盘/02.5.2 案例：滴滴的用户激励体系.mp4"; // 附件路径
        sendmail(attachmentPath);
    }


}

