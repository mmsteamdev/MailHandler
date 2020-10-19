import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailService {
    private String senderEmail;
    private String senderPassword;

    public MailService(){
        this.senderEmail = System.getenv("SENDER_MAIL");
        this.senderPassword = System.getenv("SENDER_PASSWORD");
    }

    public void run(MailObject obj) throws MessagingException {
        String to = obj.getValue("to");
        String subject = obj.getValue("subject");
        String text = obj.getValue("text");

        System.out.println("Sending email to " + to);

        Session session = createSession();
        session.setDebug(true);

        //create message using session
        MimeMessage message = new MimeMessage(session);
        prepareEmailMessage(message, to, subject, text);

        //sending message
        Transport.send(message);
        System.out.println("Done");
    }

    private void prepareEmailMessage(MimeMessage message, String to, String subject, String text)
            throws MessagingException {
        message.setContent(text, "text/html; charset=utf-8");
        message.setFrom(new InternetAddress(senderEmail));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
    }

    private Session createSession(){
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");//Outgoing server requires authentication
        props.put("mail.smtp.starttls.enable", "true");//TLS must be activated
        props.put("mail.smtp.host", "smtp.gmail.com"); //Outgoing server (SMTP) - change it to your SMTP server
        props.put("mail.smtp.port", "587");//Outgoing port
        props.put("mail.smtp.ssl.enable", "true");

        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
    }
}
