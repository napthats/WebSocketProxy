package com.napthats.websocket;

import java.io.IOException;
import org.eclipse.jetty.websocket.WebSocket;

final class WebSocketBridge implements WebSocket.OnTextMessage {
    private Connection itsJsConnection = null;
    private ForwardingSocket itsForwardingSocket = null;
    
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
       		itsForwardingSocket.sendToServer(msg);
    	}
    	else {
    		itsForwardingSocket = null;
    		String[] msgList = msg.split(":");
    		if (msgList.length != 2) return;
    		initializeSocket(msgList[0], Integer.parseInt(msgList[1]));
    	}
    }

	private void initializeSocket(String host, int port) {
		//test for connect fixed server
		host = "napthats.com";
		port = 20017;
		//end test
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
