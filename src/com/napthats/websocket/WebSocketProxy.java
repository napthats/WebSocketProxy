package com.napthats.websocket;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

/**
 * 
 * @author napthats
 *
 */
public final class WebSocketProxy {
	public static SpecialCommandSet itsSpecialCommandSet;
	private static final String MESSAGE_NO_PORT_SPECIFIED = "invalid properties(no port specified)";
	private static final String PROPERTY_PORT = "Port";
	private static final String PROPERTY_CHARSET = "Charset";
	private static final String FILENAME_PROPERTY = "WebSocketProxy.properties";
	
    public static void main(String[] args) {
    	//parse property file
    	final Properties prop = new Properties();
    	try {
    		FileInputStream fi = new FileInputStream(FILENAME_PROPERTY);
    		prop.load(fi);
    		fi.close();
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    		System.exit(1);
    	}
    	if (prop.getProperty(PROPERTY_PORT) == null) throw new RuntimeException(MESSAGE_NO_PORT_SPECIFIED);
    	
    	itsSpecialCommandSet = new SpecialCommandSet(prop);
    	
    	//init server
        Server server = new Server(Integer.parseInt(prop.getProperty(PROPERTY_PORT)));
        server.setStopAtShutdown(true);
        server.setGracefulShutdown(1000);
        ServletContextHandler root = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        root.setResourceBase("./");
        root.addServlet(DefaultServlet.class, "/*");
        root.addServlet(new ServletHolder(new WebSocketServlet() {
            private static final long serialVersionUID = 1L;
            @Override
            public WebSocket doWebSocketConnect(HttpServletRequest _, String __) {
                return new WebSocketBridge(itsSpecialCommandSet, prop.getProperty(PROPERTY_CHARSET, "ISO-8859-1"));
            }
        }), "/ws/*");

        //start server
        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
