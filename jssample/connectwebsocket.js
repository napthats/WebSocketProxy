var com;
if (!com) com = {};
if (!com.napthats) com.napthats = {};
if (!com.napthats.websocket) com.napthats.websocket = {};

(function() {
    var MESSAGE_WEBSOCKET_DISABLE = 'WebsSocket disable';
    var URL_WS = 'ws://localhost:8888/ws/';
    var ns = com.napthats.websocket;

    ns.connectWebSocket = function(onMessageListener) {
        //check WebSocket enable
        if (!window.WebSocket && !window.MozWebSocket) {
            alert(MESSAGE_WEBSOCKET_DISABLE);
            return;
        }

        //connect WebSocket
        var wsurl = URL_WS;
        var ws = window.MozWebSocket ? new MozWebSocket(wsurl) : new WebSocket(wsurl);

        //add 'message' EventListener
        if (onMessageListener) {
            ws.addEventListener('message', onMessageListener, false);
        }

        //for finalize WebSocket
        $(window).unload(function(){
            ws.close();
            ws = null;
        });

        return ws;
    }
})();
