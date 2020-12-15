import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class MailService {
    private String senderEmail;
    private String senderPassword;

    public MailService(){
        this.senderEmail = System.getenv("SENDER_MAIL");
        this.senderPassword = System.getenv("SENDER_PASSWORD");
        System.out.println(this.senderEmail);
    }

    public void run(MailObject obj) throws MessagingException {
        String to = obj.getValue("to");
        String subject = obj.getValue("subject");
        String text = obj.getValue("text");
        String attachment = obj.getValue("attachment");

        System.out.println("Sending email to " + to);

        Session session = createSession();
        session.setDebug(true);

        //create message using session
        MimeMessage message = new MimeMessage(session);
        this.prepareEmailMessage(message, to, subject);

        if(attachment == null) {
            this.prepareMailWithoutAttachment(message, text);
        } else{
            this.prepareMailWithAttachment(message, text, attachment);
        }

        //sending message
        Transport.send(message);
        System.out.println("Done");
    }

    private void prepareEmailMessage(MimeMessage message, String to, String subject)
            throws MessagingException {
        message.setFrom(new InternetAddress(senderEmail));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
    }

    private void prepareMailWithoutAttachment(MimeMessage message, String text)
            throws MessagingException {
        message.setContent(text, "text/html; charset=utf-8");
    }

    private void prepareMailWithAttachment(MimeMessage message, String text, String attachment)
            throws MessagingException {
        Multipart multipart = new MimeMultipart();
        MimeBodyPart attachmentPart = new MimeBodyPart();
        MimeBodyPart textPart = new MimeBodyPart();

        try {
            File f =new File(attachment);
            attachmentPart.attachFile(f);

            textPart.setText(text);

            multipart.addBodyPart(textPart);
            multipart.addBodyPart(attachmentPart);
        } catch (IOException e) {
            e.printStackTrace();
        }
        message.setContent(multipart);
    }

    private Session createSession(){
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");//Outgoing server requires authentication
        props.put("mail.smtp.starttls.enable", "true");//TLS must be activated
        props.put("mail.smtp.host", "smtp.gmail.com"); //Outgoing server (SMTP)
        props.put("mail.smtp.port", "587");//Outgoing port
        props.put("mail.smtp.ssl.enable", "true");

        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
    }
}
