import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MailHandler {
    private MailService mailService;

    public MailHandler(){
        this.mailService = new MailService();
    }

    public void run(){
        int port = 587;
        try{
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);

            server.createContext("/mail", new  ServerHttpHandler(this.mailService));
            server.setExecutor(threadPoolExecutor);
            server.start();
            System.out.println("Server started on port " + port);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
