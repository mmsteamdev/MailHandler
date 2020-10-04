import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailService {
    private String to;
    private String subject;
    private String text;
    private String senderEmail;
    private String senderPassword;

    public MailService(MailObject obj){
        this.to = obj.getEmail();
        this.subject = obj.getSubject();
        this.text = obj.getText();
        this.senderEmail = obj.getSenderEmail();
        this.senderPassword = obj.getSenderPassword();

    }

    public void run() throws MessagingException {
        System.out.println("Sending email to " + to);

        Session session = createSession();

        //create message using session
        MimeMessage message = new MimeMessage(session);
        prepareEmailMessage(message, to, subject, text);

        //sending message
        Transport.send(message);
        System.out.println("Done");
    }

    private  void prepareEmailMessage(MimeMessage message, String to, String title, String html)
            throws MessagingException {
        message.setContent(html, "text/html; charset=utf-8");
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(title);
    }

    private Session createSession(){
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");//Outgoing server requires authentication
        props.put("mail.smtp.starttls.enable", "true");//TLS must be activated
        props.put("mail.smtp.host", "smtp.gmail.com"); //Outgoing server (SMTP) - change it to your SMTP server
        props.put("mail.smtp.port", "587");//Outgoing port

        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
    }
}
