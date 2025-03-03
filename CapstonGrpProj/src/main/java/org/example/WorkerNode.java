package org.example;

// File: WorkerNode.java
public class WorkerNode {
    public static void main(String[] args) {
        // Connect to the master node (using the Communication module)
        SocketCommunication comm = new SocketCommunication("localhost", 5000);

        while (true) {
            // Receive a frame from the master node
            Frame frame = comm.receiveFrame();
            if (frame == null) break; // exit loop if no more frames

            // Process the frame in a new thread
            new Thread(new FrameProcessor(frame, comm)).start();
        }
    }
}
