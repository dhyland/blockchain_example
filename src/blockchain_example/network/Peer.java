/*
 * Copyright (C) 2018 Dilithium Team .
 *
 * The Dilithium library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */

package blockchain_example.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import blockchain_example.MyLogger;


public class Peer {
	
    private Thread peerThread;  
    public Socket socket;
    private static HashMap<String, NetworkUtils> commands = new HashMap<>();
    public DataOutputStream out;
    public DataInputStream in;
    
	public Peer(Socket socket)  {
		this.socket = socket;
        //initializeCommands();
		peerThread = new Thread(new Runnable() {
			public void run() {
                try {
                    listen();
                    MyLogger.log(Level.INFO, "Closing connection to " + socket.getInetAddress() + ":" + socket.getPort());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });	
		peerThread.start();
	}

	/*
	private void initializeCommands() {
        this.commands.put("ping", new PingCommandHandler());
       
    }*/
	
	public void listen() throws IOException {
		String command;
		while(true){
	    		//System.out.println("Listening for commands");
	    		try{
	    			DataInputStream in = new DataInputStream(this.socket.getInputStream());
        			DataOutputStream out = new DataOutputStream(this.socket.getOutputStream());
        			command = receive(in);
        			send(serve(command), out);
	    		} catch (SocketTimeoutException e) {
	    			e.printStackTrace();
	    		}
        

		}
	}
	
	public static String serve(String input) {
        List<String> list = new ArrayList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(input);
        while (m.find()) {
            list.add(m.group(1));
        }

        String command = list.remove(0); // Get the command and remove it from the list.

        if(!commands.containsKey(command)){
            return "'" + command + "' is not a command.";
        }

        String[] args = null;
        if (list.size() > 0){
            args = list.toArray(new String[list.size()]);
        }

        return NetworkUtils.execute(args);
    }

    public static void send(String data, DataOutputStream out){
    	MyLogger.log(Level.INFO, "Sending message: " + data);
        try {
            out.writeUTF(data);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receive(DataInputStream in){
        String data = null;
        try {
            data = in.readUTF();
            MyLogger.log(Level.INFO, "Received message: " + data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public String toString() {
        return String.format("[%s:%s]", socket.getInetAddress(), socket.getPort());
    }
}