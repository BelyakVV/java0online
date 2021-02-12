package aabyodj.epamgrow.java0online.m6t1;

import jakarta.mail.Address;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import static aabyodj.console.CommandLineInterface.printErrorMsg;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author aabyodj
 */
public class Util {
    
    static final String APP_NAME = "Учёт книг в домашней библиотеке";

    //TODO переделать архитектуру приложения в клиент/сервер и вынести отправку email на сервер
    static final String SMTP_LOGIN = "epam-grow@aab.by";
    static final String SMTP_PASSWORD = "J99oSLrt)jsf}mw8";
    static final String SMTP_HOST = "smtp.yandex.com";
    static final String SMTP_PORT = "465";

    static void sendMail(List<Address> recipients, String subject, String text) {
        System.out.print("Отправка email...");
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.socketFactory.port", SMTP_PORT);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", SMTP_PORT);
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_LOGIN, SMTP_PASSWORD);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            try {
                message.setFrom(new InternetAddress(SMTP_LOGIN, APP_NAME));
            } catch (UnsupportedEncodingException ex) {
                message.setFrom(new InternetAddress(SMTP_LOGIN));
            }
            for (Address recipient : recipients) {
                message.addRecipient(Message.RecipientType.TO, recipient);
            }
            message.setSubject(subject);
            message.setText(text);
            Transport.send(message);
            System.out.println("ok");
        } catch (MessagingException e) {
            printErrorMsg(e.getMessage());
        }
    }

    public static void createIfNeeded(File file) throws IOException {
        if (file.exists()) {
            return;
        }
        File parent = file.getParentFile();
        if (parent != null) {
            if (!parent.exists()) {
                parent.mkdirs();
            } else if (!parent.isDirectory()) {
                throw new RuntimeException('\"' + parent.getCanonicalPath() + '\"'
                                           + " не является каталогом");
            }
        }
        file.createNewFile();
    }
}
