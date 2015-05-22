package main.ui;

import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import main.ui.app.Controllers;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.logging.Logger;

public class HTTPServer {

    static final String NIC = "0.0.0.0";
    static final int PORT = 9898;
    static final String MAIN_CONTROLLER = "Home";

    public static Logger logger;
    
    private HttpServer server;
    private BasicAuthenticator auth;
    
    public HTTPServer() {
        try {
            logger = Logger.getLogger(this.getClass().getName());
            server = HttpServer.create(new InetSocketAddress(InetAddress.getByName(NIC), PORT), 0);
            auth = new BasicAuthenticator("admin") {
                @Override
                public boolean checkCredentials(String user, String pwd) {
                    return user.equals("root") && pwd.equals("password");
                }
            };

            Arrays.stream(Controllers.class.getDeclaredClasses()).forEach(c -> {
                try {
                    HttpHandler controller = (HttpHandler) c.newInstance();
                    server.createContext("/" + c.getSimpleName().toLowerCase(), controller).setAuthenticator(auth);

                    if (c.getSimpleName().equals(MAIN_CONTROLLER)) {
                        server.createContext("/", controller).setAuthenticator(auth);
                    }
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
            server.setExecutor(null);
            server.start();
            logger.info("Starting server on " + NIC + ":" + PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void stop() {
        server.stop(0);
    }
    
    public static void main(String[] args) {
        new HTTPServer();
    }
    
}