import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONException;

import javax.mail.MessagingException;
import java.io.*;

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

    private String handlePostRequest(HttpExchange httpExchange){
        // get request
//        Headers reqHeaders = httpExchange.getRequestHeaders();
//        reqHeaders.forEach((key, value) -> System.out.println(key + ": " + value));

        String message = null;
        try (InputStream in = httpExchange.getRequestBody()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuilder msgbuilder = new StringBuilder();
            int c;
            while ((c = br.read()) > -1) {
                msgbuilder.append((char) c);
            }
            message = msgbuilder.toString();
            System.out.println("Message: " + message);
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    private void handleResponse(HttpExchange httpExchange, String requestParamValue) throws IOException {
        boolean success = this.convertMsgAndSendMail(requestParamValue);

        String htmlResponse = "failure";
        if(success) {
            htmlResponse = "success";
        }

        httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers","x-prototype-version,x-requested-with");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods","GET,POST");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin","*");

        // Create http response
        httpExchange.sendResponseHeaders(200, htmlResponse.length());
        OutputStream outputStream = httpExchange.getResponseBody();

        outputStream.write(htmlResponse.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Boolean convertMsgAndSendMail(String requestParamValue) {
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
