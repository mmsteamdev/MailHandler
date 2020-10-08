import org.json.JSONObject;

public class MailObject {
    private String email;
    private String subject;
    private String text;
    private String senderEmail;
    private String senderPassword;

    public MailObject(String msg){
        JSONObject obj = new JSONObject(msg);
        this.email = obj.getString("email");
        this.subject = obj.getString("subject");
        this.text = obj.getString("text");
        this.senderEmail = obj.getString("senderEmail");
        this.senderPassword = obj.getString("senderPassword");
    }

    public String getEmail() {
        return email;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getSenderPassword() {
        return senderPassword;
    }
}
