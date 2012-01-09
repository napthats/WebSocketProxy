package com.napthats.websocket;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

public final class WebSocketProxy {
    public static void main(String[] args) {
        Server server = new Server(8888);
        server.setStopAtShutdown(true);
        server.setGracefulShutdown(1000);
        ServletContextHandler root = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);

        root.setResourceBase("./");
        root.addServlet(DefaultServlet.class, "/*");

        root.addServlet(new ServletHolder(new WebSocketServlet() {
            private static final long serialVersionUID = 1L;
            @Override
            public WebSocket doWebSocketConnect(HttpServletRequest arg0, String arg1) {
                return new WebSocketBridge();
            }
        }), "/ws/*");

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
