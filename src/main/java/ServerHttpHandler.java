import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;

public class ServerHttpHandler implements HttpHandler {
    private ServerPOST serverPOST;

    public ServerHttpHandler(MailService mailService){
        this.serverPOST = new ServerPOST(mailService);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String htmlResponse;
        switch(httpExchange.getRequestMethod()) {
            case "GET":
                htmlResponse = handleGetRequest(httpExchange);
                break;
            case "POST":
                htmlResponse = handlePostRequest(httpExchange);
                break;
            default:
                htmlResponse = "{\"response\" : \"failure\"}";
                break;
        }
        handleResponse(httpExchange, htmlResponse);
    }
    private String handleGetRequest(HttpExchange httpExchange) {
        String uri = httpExchange.
                getRequestURI()
                .toString()
                .split("\\?")[1]
                .split("=")[1];
        System.out.println(uri);
        return "{\"response\" : \"failure\"}";
    }

    private String handlePostRequest(HttpExchange httpExchange){
        // Get request body
        String msg = this.parseMsg(httpExchange);
        boolean success = this.serverPOST.convertMsgAndSendMail(msg);

        String htmlResponse = "failure";
        if(success) {
            htmlResponse = "success";
        }

        return htmlResponse;
    }

    private void handleResponse(HttpExchange httpExchange, String htmlResponse) throws IOException {
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

    private String parseMsg(HttpExchange httpExchange) {
        String message = null;
        try (InputStream in = httpExchange.getRequestBody()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuilder msg_builder = new StringBuilder();
            int c;
            while ((c = br.read()) > -1) {
                msg_builder.append((char) c);
            }
            message = msg_builder.toString();
            System.out.println("Message: " + message);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }



}
