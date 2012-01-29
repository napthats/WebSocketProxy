ABSTRACT
========

WebSocketProxy: Jetty server with WebSocket that relay messages between (JavaScript/HTML) clients and sockets.


BUILD
=====

Import to the Eclipse and put the Jetty libraries to Build Path.

  http://www.eclipse.org/jetty/


USAGE
=====

The "WebSocketProxy.properties" file format is following. 

  Port=PORT_NUMBER
  Connect=CONNECT_COMMAND:HOST_NAME:PORT_NUMBER, ...
  Disconnect=DISCONNECT_COMMAND, ...
  Charset=CHARSET_NAME_IN_JAVA, ...

For example,

  Port=8888
  Connect=$open:localhost:20017,$open2:localhost:20018
  Disconnect=$close,$close2
  Charset=UTF-8

WebSocketProxy handles clients' special commands as above. It connect and disconnect clients to the host when they send specified commands ("$open", "$open2", "$close" or "$close2" in this case). Connect command isn't available if they are already connected (the same applies to disconnect command). It relay all other messages to/from clients.
Additionally, JavaScript client samples are in the "jssample" directory.


LICENSE
=======

The MIT License (MIT)
Copyright (c) 2012, napthats

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
