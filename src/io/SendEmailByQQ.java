package io;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * 用到的jar包：
 * <dependency>
 * <groupId >com.sun.mail</groupId >
 * <artifactId >javax.mail</artifactId >
 * <version >1.5.5</version >
 * </dependency>
 * <dependency >
 * <groupId >javax.mail </groupId >
 * <artifactId >mail</artifactId >
 * <version >1.4.5</version >
 * </dependency >
 */
public class SendEmailByQQ {

    public static void main(String[] args) {
        /*
          收件人邮箱例子：123456789@qq.com
          发件人邮箱例子：987654321@qq.com
          验证邮箱例子：同上（987654321@qq.com）
          授权码：一串字母(在QQ邮箱中的>设置>帐户>POP3/SMTP服务 通过发送短信获取授权码）
          邮件标题例子：随便
          邮件内容例子：随便
         */
        SendEmailByQQ.sendEmail("收件人邮箱", "发件人邮箱",
                "验证邮箱", "一串字母",
                "邮件标题", "邮件内容");
    }

    public static void sendEmail(String toEmail, String fromEmail,
                                 final String authEmail, final String authPaw,
                                 String title, String text) {
        // 收件人电子邮箱
        String to = toEmail;

        // 发件人电子邮箱
        String from = fromEmail;

        // 指定发送邮件的主机为 localhost
        String host = "smtp.qq.com";  //QQ 邮件服务器

        // 获取系统属性
        Properties properties = System.getProperties();

        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);

        properties.put("mail.smtp.auth", "true");

        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.socketFactory.port", "465");

        // 获取默认session对象
        Session session = Session.getInstance(properties, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(authEmail, authPaw); //发件人邮件用户名、授权码
            }
        });

        try {
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);

            // Set From: 头部头字段
            message.setFrom(new InternetAddress(from));

            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            // Set Subject: 头部头字段
            //message.setSubject("I love u");
            message.setSubject(title);

            // 设置消息体
            message.setContent(text, "text/html;charset=UTF-8");
            // 发送 HTML 消息, 可以插入html标签
            // 发送消息
            Transport.send(message);
            System.out.println("Sent email successfully...");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
