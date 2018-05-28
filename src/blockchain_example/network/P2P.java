package blockchain_example.network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.logging.Level;

import blockchain_example.MyLogger;




//class to implement basic peer-to-peer 
public class P2P {

	private int port;
	private DataOutputStream outputStream;
	private ArrayList<Peer> peerList;
	public Thread serverThread;
	private boolean serverIsOn;
	private ServerSocket serverSocket;
	private Socket socket = null;
	
	 //Node with access to blockchain
    public P2P(int port0){
        this.port = port0;
        
        peerList = new ArrayList<>();
        serverThread = new Thread(new Runnable(){
	        public void run(){
	        	try{
	        		listen();
	        		MyLogger.log(Level.INFO, "Connection Ended");
	        	}
	        	catch (IOException e){
	        	}
	        }
        });		
        //initializeCommands();
    }
    
    public void start(){
        if(serverThread.isAlive()){
            MyLogger.log(Level.INFO, "Server is already running.");
            return;
        }
        serverIsOn = true;
        serverThread.start();
    }

    public void stop() throws IOException{
    		serverIsOn = false;
    		try {
        		serverThread.interrupt();
    			socket.close();
        } catch (NullPointerException n){
        	MyLogger.log(Level.WARNING, "Null pointer when closing server socket");
        }
    		
    	MyLogger.log(Level.INFO, "Server Stopped");
    }

    public void listen() throws IOException, SocketTimeoutException{
    	serverSocket = new ServerSocket(this.port);
		MyLogger.log(Level.INFO, "Starting server on port: " + this.port);
			
		Peer peer;
		serverSocket.setSoTimeout(10000);
        while(serverIsOn){
			try{
				socket = serverSocket.accept();
				MyLogger.log(Level.INFO, "Passed Accept");
				peer = new Peer(socket);
				MyLogger.log(Level.INFO, "Connection received from: " + peer.toString());
				peerList.add(peer);
				MyLogger.log(Level.INFO, "New peer: " + peer.toString());
			}
			catch (SocketTimeoutException e){
        			
        	}
        }
    }

    public void connect(Socket socket){
		try{
			outputStream = new DataOutputStream(socket.getOutputStream());
			Peer.send("ping", outputStream);		
		}
        catch (IOException e){
            
        }
    }
}
