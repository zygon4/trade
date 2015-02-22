
package com.zygon.trade.modules.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author zygon
 */
public class CliClient extends Thread {

    private static final int SERVER_PORT = 55555;

    private final DatagramSocket socket;
    
    public CliClient(DatagramSocket socket) {
        super();
        super.setDaemon(false);
        this.socket = socket;
    }
    
    @Override
    public void run() {
        try {
            byte[] dataBuffer = new byte[65535];
            while (true) {
                DatagramPacket dpkg = new DatagramPacket(dataBuffer, dataBuffer.length);
                this.socket.receive(dpkg);
                System.out.println(new String(dpkg.getData(), 0, dpkg.getLength()));
                
                
            }
        } catch (IOException io) {
            
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnknownHostException, IOException {
        DatagramSocket socket = new DatagramSocket();
        
        
        
        try {
            CliClient client = new CliClient(socket);
            client.start();
        } finally {
            socket.close();
        }
    }
}
