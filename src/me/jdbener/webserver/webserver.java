package me.jdbener.webserver;
 
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
 
/**
 * Simple multithreaded Webserver, that only supports the required GET request
 * and delievers hardcoded files.
 *
 * Used for getting an access token.
 *
 * @author tduva
 */
public class webserver implements Runnable {
	//public static Logger logger = LoggerFactory.getLogger(webserver.class);
   
    public static final int ERROR_COULD_NOT_LISTEN_TO_PORT = 0;
    private static final int SO_TIMEOUT = 10*1000;
   
    private static int port = 55555;
    private volatile boolean running = true;
    private volatile ServerSocket serverSocket = null;
    private final WebserverListener listener;
    private int connectionCount = 0;
   
    private final ArrayList<WebserverConnection> connections = new ArrayList<>();
   
    /**
     * Main method for testing the server on it's own.
     *
     * @param args
     */
 
    /**
     * Construct a new webserver with the given client where data is sent. Still
     * has to be started in a new Thread.
     *
     * @param listener Listener to return state information and data to
     */
    public webserver(WebserverListener listener) {
        this.listener = listener;
    }
   
    /**
     * Stops the server.
     *
     * If you call this after receiving the token, there should be small delay,
     * so the page that removes the token from the browser history can still be
     * delievered (token_received_no_redirect.html).
     */
    public void stop() {
        running = false;
        close();
    }
   
    /**
     * A debug message with "Webserver: " prepended.
     *
     * @param message
     */
    private void debug(String message) {
        //message = "Webserver: "+message;
        //logger.info(message);
    }
   
    /**
     * Starts the Thread and the Webserver
     */
    @Override
    public void run() {
        debug("Trying to start webserver at port "+port);
        try {
            serverSocket = new ServerSocket(port, 0, InetAddress.getLoopbackAddress());
        } catch (IOException ex) {
            debug("Could not listen to port "+port+" ("+ex.getLocalizedMessage()+")");
            if (listener != null) {
                listener.webserverError("Could not listen to port "+port);
            }
            stop();
            return;
        }
 
        if (listener != null) {
            listener.webserverStarted();
        }
       
        while (running) {
            debug("Waiting for connections");
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (SocketException ex) {
                debug("Accept interrupted: "+ex.getLocalizedMessage());
                // After breaking out of the loop, there is a close() anyway
                //close();
                break;
            } catch (IOException ex) {
                debug("ServerSocket accept failed: "+ex.getLocalizedMessage());
                // TODO: close() necessary?
                // Why return instead of break?
                //return;
                break;
            }
            // Connection established, work with it
            newConnection(clientSocket);
        }
       
        close();
       
        if (listener != null) {
            listener.webserverStopped();
        }
       
        debug("Stopped");
    }
   
    /**
     * Close the ServerSocket if necessary.
     */
    private void close() {
        closeConnections();
        // Try to close ServerSocket
        try {
            if (serverSocket != null) {
                serverSocket.close();
                serverSocket = null;
            }
        } catch (IOException ex) {
            debug("Error closing ServerSocket: "+ex.getLocalizedMessage());
        }
    }
 
    /**
     * Respond to a request by creating a new connection in a new Thread.
     *
     * @param clientSocket
     */
    private void newConnection(Socket clientSocket) {
        WebserverConnection connection =
                new WebserverConnection(clientSocket, connectionCount++);
        new Thread(connection).start();
        connections.add(connection);
    }
   
    /**
     * Close all connections.
     */
    private void closeConnections() {
        for (WebserverConnection c : connections) {
            c.close();
        }
    }
   
    /**
     * Handles a single connectino to a client in it's own Thread.
     */
    class WebserverConnection implements Runnable {
 
        /**
         * The connection number so the connection can be recognized in
         * debug messages.
         */
        private final int connectionNumber;
        /**
         * The Socket for this connection.
         */
        private final Socket connection;
       
        /**
         * Construct a new connection based on the Socket and the number of
         * this connection for debug purposes. Still has to be started in a new
         * Thread.
         *
         * @param connection
         * @param connectionNumber
         */
        WebserverConnection(Socket connection, int connectionNumber) {
            this.connectionNumber = connectionNumber;
            this.connection = connection;
        }
       
        /**
         * Debug message for the connection with the connection number prepended.
         *
         * @param message
         */
        private void debugConnection(String message) {
            debug("["+connectionNumber+"] "+message);
        }
       
        /**
         * Starting the connection thread here by reading in the request.
         */
        @Override
        public void run() {
            debugConnection("Handling connection");
            try (
                    BufferedReader input = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()))
            ) {
                connection.setSoTimeout(SO_TIMEOUT);
                String request = input.readLine();
                if (request != null) {
                    respond(request);
                }
               
            } catch (SocketTimeoutException ex) {
                debugConnection("SoTimeout: "+ex.getLocalizedMessage());
            } catch (IOException ex) {
                debugConnection("Error reading: "+ex.getLocalizedMessage());
            }
            debugConnection("Closed");
        }
        
        public String toLowerCase(String s) {
            return s != null ? s.toLowerCase(Locale.ENGLISH) : null;
        }
       
        /**
         * Respond to the given request. This opens an outgoing stream and
         * sends the appropriate response.
         *
         * @param request
         */
        private void respond(String request) {
            debugConnection("Making response for "+removeToken(request));
            try (OutputStream output = connection.getOutputStream()) {
                String response = "";
                // Check if there should be a token in there
                if (toLowerCase(request).startsWith("get /token/")) {
                    String token = getToken(request);
                    if (token.isEmpty()) {
                        // No token, so show redirect page
                        response = makeResponse("token_redirect.html");
                        debugConnection("Token redirect");
                    } else {
                        // Token available, so show done page and send token
                        // to the client
                        response = makeResponse("token_received.html");
                        debugConnection("Token received");
                        if (listener != null) {
                            listener.webserverTokenReceived(token);
                        }
                    }
                }
                else if (toLowerCase(request).startsWith("get /tokenreceived/")) {
                    response = makeResponse("token_received_no_redirect.html");
                } else if (toLowerCase(request).startsWith("GET /favicon.ico")){
                	response = "HTTP/1.1 200 OK\n"+
                				"Server: BennerBotWebserver\n"+
                				"Content-Type: text/plain; charset=WINDOWS-1251\n\n"+
                				StreamToString(getClass().getResourceAsStream("favicon.ico"));
                }
                else {
                    response = makeResponse(null);
                }
                // Send the response
                output.write(response.getBytes("UTF-8"));
            } catch (IOException ex) {
                debugConnection("Error responding: "+ex.getLocalizedMessage());
            }
        }
       
        /**
         * Closes this connection prematurely.
         */
        public void close() {
            try {
                connection.close();
            } catch (IOException ex) {
                debugConnection("Failed closing connection: "+ex.getLocalizedMessage());
            }
        }
       
        /**
         * Removes the token from the request string, if there is any.
         *
         * @param request
         * @return
         */
        private String removeToken(String request) {
            if (getToken(request).isEmpty()) {
                return request;
            }
            return request.replace(getToken(request), "<token>");
        }
       
        /**
         * Gets the token from the request string. It is expected to be between
         * "/token/" and the next "/" or the next space. Usually it should be
         * like "/token/<token> "
         *
         * @param request
         * @return The token or an empty String
         */
        private String getToken(String request) {
            // Check if token might be in there
            int start = request.indexOf("/token/");
            if (start == -1) {
                // if not, return immediately
                return "";
            }
            start += "/token/".length();
            int end = request.indexOf(" ", start);
            int end2 = request.indexOf("/", start);
            // If a / comes earlier than a space, use that
            if (end2 != -1 && end2 < end) {
                end = end2;
            }
            if (end == -1) {
                return "";
            }
            return request.substring(start, end).trim();
        }
       
        
        
        /**
         * Make header and read the given file.
         *
         * @param fileName
         * @return
         */
        private String makeResponse(String fileName) {
        	 
            if (fileName == null) {
                return makeHeader(false);
            }
 
            String content = "";
 
            try {
                // Read file to send back as content
                InputStream input = getClass().getResourceAsStream(fileName);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
                StringBuilder buffer = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line);
                    buffer.append("\n");
                }
                content = buffer.toString();
 
            } catch (Exception ex) {
                debug(ex.getMessage());
                content = "<html><body>An error occured (couldn't read file)</body></html>";
            }
            return makeHeader(true) + content;
        }
       
        /**
         * Make the http response header.
         *
         * @param ok Whether it was a valid request.
         * @return
         */
        private String makeHeader(boolean ok) {
            String header = "";
            if (ok) {
                header += "HTTP/1.0 200 OK\n";
            } else {
                header += "HTTP/1.0 403 Forbidden\n";
            }
            header += "Server: BennerBotWebserver\n";
            header += "Content-Type: text/html; charset=UTF-8\n\n";
           
            return header;
        }
 
    }
   
    public static interface WebserverListener {
        public void webserverStarted();
        public void webserverStopped();
       
        /**
         * An error occured with the webserver. The webserver will already have
         * stopped when this happens.
         *
         * @param error A message describing the error
         */
        public void webserverError(String error);
       
        /**
         * The token has been received.
         *
         * @param token The token
         */
        public void webserverTokenReceived(String token);
    }
    
    private static String StreamToString(java.io.InputStream is) {
	    @SuppressWarnings("resource")
		Scanner s = new Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
    
    public boolean isRunning(){
    	return running;
    }
   
}