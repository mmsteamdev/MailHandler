import javax.mail.MessagingException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class MailHandler {
    private DataBaseHandler dbHandler;

    public MailHandler(){
        this.dbHandler = new DataBaseHandler();
    }

    public void run(){
        System.out.println("DUPA");
        MailObject mail = this.convertJsonToForm(this.listenOnPorts());
        this.insertIntoDB(mail);
        this.sendMail(mail);
    }

    private String listenOnPorts(){
        String message = null;
        try{
            int port = 4000;
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            message = (String) ois.readObject();
            System.out.println("Message: " + message);

            ois.close();
            serverSocket.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return message;
    }

    private MailObject convertJsonToForm(String msg){
        return new MailObject(msg);
    }

    private void insertIntoDB(MailObject obj){
        try {
            this.dbHandler.insertIntoDB(obj);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sendMail(MailObject obj){
        MailService mailService = new MailService(obj);
        try {
            mailService.run();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
