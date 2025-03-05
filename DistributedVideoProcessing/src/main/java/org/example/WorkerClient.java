package org.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class WorkerClient implements Runnable {
    private final int startFrame;
    private final int endFrame;
    private final String workerHost;
    private final int workerPort;

    // Option 2: Two-argument constructor
    // Defaults to "localhost" and port 5001
    public WorkerClient(int startFrame, int endFrame) {
        this(startFrame, endFrame, "localhost", 5001);
    }

    // Full constructor if you need custom host/port
    public WorkerClient(int startFrame, int endFrame, String workerHost, int workerPort) {
        this.startFrame = startFrame;
        this.endFrame = endFrame;
        this.workerHost = workerHost;
        this.workerPort = workerPort;
    }

    @Override
    public void run() {
        for (int i = startFrame; i < endFrame; i++) {
            String framePath = "frames/frame_" + i + ".jpg";
            try (Socket socket = new Socket(workerHost, workerPort);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                out.println(framePath);
                System.out.println("Sent frame: " + framePath);
            } catch (IOException e) {
                System.err.println("Error sending frame: " + framePath);
                e.printStackTrace();
            }
        }
    }
}
