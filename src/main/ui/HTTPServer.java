package main.ui;

import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import main.ui.app.Controllers;
import main.ui.core.components.Helper;
import main.ui.core.controllers.Handler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/*
 * TODO Global settings page
 * TODO Media specific settings
 * TODO Media specific lists
 */
public class HTTPServer {

    public static final String NIC = "0.0.0.0";
    public static final int PORT = 9898;
    public static final String MAIN_CONTROLLER = "Home";
    public static final boolean USE_AUTH = true;

    public static Logger logger;
    public static Map<String, HttpHandler> controllers;
    private HttpServer server;
    private BasicAuthenticator auth;
    
    public HTTPServer() {
        try {
            logger = Logger.getLogger(this.getClass().getName());
            server = HttpServer.create(new InetSocketAddress(InetAddress.getByName(NIC), PORT), 0);
            controllers = new HashMap<>();
            loadAuthentication();
            createRoute("/", new Handler());
            loadControllers(Controllers.class.getDeclaredClasses());
            server.setExecutor(null);
            server.start();
            logger.info("Starting server on " + NIC + ":" + PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createRoute(String path, HttpHandler handler) {
        HttpContext hc = server.createContext(path, handler);

        if (USE_AUTH) {
            hc.setAuthenticator(auth);
        }
    }

    public void loadControllers(Class[] classes) {
        Arrays.stream(classes).forEach(c -> {
            try {
                HttpHandler controller = (HttpHandler) c.newInstance();
                controllers.put(c.getSimpleName(), controller);
                createRoute("/" + c.getSimpleName().toLowerCase(), controller);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    public void loadAuthentication() {
        if (USE_AUTH) {
            auth = new BasicAuthenticator("admin") {
                @Override
                public boolean checkCredentials(String user, String pwd) {
                    return Helper.sha1("003fBd=h9Ev1Epq" + user).equals("a2e0fee40029310f8c61d83c37f9f6ad3da7576c") && 
                            Helper.sha1("9n1802KjuYZ7^6Y" + pwd).equals("d5c307af6c77cc6cfc5aad6f8dd12e58e7dff5b3");
                }
            };
        }
    }
    
    public void stop() {
        server.stop(0);
    }
    
    public static void main(String[] args) {
        new HTTPServer();
    }
    
}