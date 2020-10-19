import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import javax.mail.MessagingException;
import java.io.*;
import java.sql.SQLException;

public class ServerHttpHandler implements HttpHandler {
    private MailService mailService;

    public ServerHttpHandler(MailService mailService){
        this.mailService = mailService;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestParamValue=null;
        if("GET".equals(httpExchange.getRequestMethod())) {
            requestParamValue = handleGetRequest(httpExchange);
        }else if("POST".equals(httpExchange.getRequestMethod())) {
            requestParamValue = handlePostRequest(httpExchange);
        }
        handleResponse(httpExchange,requestParamValue);
    }
    private String handleGetRequest(HttpExchange httpExchange) {
        return httpExchange.
                getRequestURI()
                .toString()
                .split("\\?")[1]
                .split("=")[1];
    }

    private String handlePostRequest(HttpExchange httpExchange) throws IOException {
        // get request
//        Headers reqHeaders = httpExchange.getRequestHeaders();
//        reqHeaders.entrySet().forEach(entry->{
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//        });

        String message = null;
        try (InputStream in = httpExchange.getRequestBody()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuilder msgbuilder = new StringBuilder();
            while (br.ready()) {
                msgbuilder.append((char) br.read());
            }
            message = msgbuilder.toString();
            System.out.println("Message: " + message);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    private void handleResponse(HttpExchange httpExchange, String requestParamValue)  throws  IOException {
        MailObject mail = this.convertJsonToForm(requestParamValue);
        boolean success = this.sendMail(mail);
//        Boolean success = this.insertIntoDB(form);

        OutputStream outputStream = httpExchange.getResponseBody();
        String htmlResponse;
        if(success) {
            htmlResponse = "success";
        } else {
            htmlResponse = "failure";
        }

        httpExchange.sendResponseHeaders(200, htmlResponse.length());

        outputStream.write(htmlResponse.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private MailObject convertJsonToForm(String msg){
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
