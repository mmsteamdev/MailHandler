import org.json.JSONException;

import javax.mail.MessagingException;

public class ServerPOST {
    private MailService mailService;

    public ServerPOST(MailService mailService){
        this.mailService = mailService;
    }

    public Boolean convertMsgAndSendMail(String requestParamValue) {
        try {
            MailObject mail = this.convertJsonToForm(requestParamValue);
            return this.sendMail(mail);
        } catch (JSONException e){
            e.printStackTrace();
            return false;
        }
    }

    private MailObject convertJsonToForm(String msg) throws JSONException {
        return new MailObject(msg);
    }

    private Boolean sendMail(MailObject obj){
        try {
            this.mailService.run(obj);
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
