$(document).ready(function() {
    var ws = com.napthats.websocket.connectWebSocket(function(msg) {
    	alert(msg);
    });
    ws.send('$open');
    ws.send('message to a server');
    ws.send('$close');
});
