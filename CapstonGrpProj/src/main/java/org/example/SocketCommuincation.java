// File: SocketCommunication.java
import java.io.*;
import java.net.*;

public class SocketCommunication {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    // Constructor for master node usage (server side could have a different implementation)
    public SocketCommunication(String host, int port) {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendProcessedFrame(Frame frame) {
        try {
            out.writeObject(frame);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Frame receiveFrame() {
        try {
            return (Frame) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
