package com.creatorsn.fabulous.util;

import org.springframework.web.multipart.MultipartFile;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * 邮箱发送
 */
public class EmailSender {
    /**
     * 邮箱登陆的主机
     */
    final private String host;
    /**
     * 邮箱的端口
     */
    final private int port;
    /**
     * 邮箱的用户名
     */
    final private String username;
    /**
     * 邮箱的密码
     */
    final private String password;
    /**
     * 是否使用ssl加密
     */
    final private boolean ssl;

    /**
     * 基础属性
     */
    final private Properties properties;

    public EmailSender(
            String host,
            int port,
            String username,
            String password,
            boolean ssl
    ) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.ssl = ssl;
        this.properties = getProperties();
    }

    private Properties getProperties(){
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        if (ssl) {
            properties.put("mail.smtp.ssl.enable", "true");
        }
        return properties;
    }

    private Session getSession(){
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public void send(String to, String subject, String content, List<MultipartFile> files) throws MessagingException,IOException{
        Session session = getSession();
        var message = new javax.mail.internet.MimeMessage(session);
        message.setFrom(username);
        message.setRecipients(javax.mail.Message.RecipientType.TO, to);
        Multipart multipart = new MimeMultipart();
        var htmlPart = new javax.mail.internet.MimeBodyPart();
        htmlPart.setContent(content, "text/html;charset=UTF-8");
        message.setSubject(subject);
        multipart.addBodyPart(htmlPart);
        if (files!=null)
            for (var file:files){
                var bodyPart = new javax.mail.internet.MimeBodyPart();
                bodyPart.setContent(file.getBytes(), Objects.requireNonNull(file.getContentType()));
                bodyPart.setFileName(file.getOriginalFilename());
                multipart.addBodyPart(bodyPart);
            }
        message.setContent(multipart);
        javax.mail.Transport.send(message);

    }

    public void send(String to, String subject, String content) throws MessagingException,IOException{
        send(to, subject, content, new ArrayList<>());
    }

    public void send(List<String> to, String subject, String content, List<MultipartFile> files) throws MessagingException,IOException{
        send(String.join(",", to), subject, content, files);
    }

    public void send(List<String> to, String subject, String content) throws MessagingException,IOException{
        send(String.join(",", to), subject, content,new ArrayList<>());
    }
}
