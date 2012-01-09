package com.napthats.websocket;

import java.io.IOException;
import org.eclipse.jetty.websocket.WebSocket;
import java.util.ArrayList;
import com.napthats.websocket.SpecialCommandSet.Status;

final class WebSocketBridge implements WebSocket.OnTextMessage {
    private Connection itsJsConnection = null;
    private ForwardingSocket itsForwardingSocket = null;
    private final SpecialCommandSet itsSpecialCommandSet;
    
    public WebSocketBridge(SpecialCommandSet scSet) {
    	super();
    	itsSpecialCommandSet = scSet;
    }
    
    @Override
    public void onOpen(Connection con) {
        itsJsConnection = con;
    }

    @Override
    public void onClose(int arg0, String arg1) {
    	itsForwardingSocket.close();
    	itsForwardingSocket = null;
    	itsJsConnection.disconnect();
    	itsJsConnection = null;
    }
    
    @Override
    public void onMessage(String msg) {
    	if (itsForwardingSocket != null && !itsForwardingSocket.isClosed()) {
    		ArrayList<String> sc = itsSpecialCommandSet.findSpecialCommand(Status.ACTIVE, msg);
       		if (sc == null) itsForwardingSocket.sendToServer(msg);
       		else if (sc.get(0).equals("disconnect")) disconnectServer();
    	}
    	else {
    		itsForwardingSocket = null;
    		ArrayList<String> sc = itsSpecialCommandSet.findSpecialCommand(Status.NONACTIVE, msg);
    		if (sc == null) return;
    		else if (sc.get(0).equals("connect")) connectServer(sc.get(1), Integer.parseInt(sc.get(2)));
    	}
    }
    
    private void disconnectServer() {
    	itsForwardingSocket.close();
    	itsForwardingSocket = null;    	
    }

	private void connectServer(String host, int port) {
		itsForwardingSocket = new ForwardingSocket(host, port, new ForwardingSocket.RecvFromServer() {
			@Override
			public void recvFromServer(String msg) throws IOException {
				send(msg);
			}
		});
	}

    private void send(String msg) throws IOException {
        itsJsConnection.sendMessage(msg);
    }
}
