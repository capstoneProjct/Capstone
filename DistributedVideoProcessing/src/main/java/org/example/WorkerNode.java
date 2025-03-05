package org.example;

import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinPluginLoader;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class WorkerNode {
    public static void main(String[] args) {
        int workerId = 1; // Example worker ID
        int port = 5000 + workerId; // This will be 5001 if workerId=1

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Worker " + workerId + " listening on port " + port + "...");

            while (true) {
                Socket socket = serverSocket.accept();
                try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String framePath = in.readLine();
                    processFrame(framePath);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    socket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processFrame(String framePath) {
        try {
            // Load the image with Marvin
            MarvinImage image = MarvinImageIO.loadImage(framePath);

            // Use Marvin's grayscale plugin
            MarvinImagePlugin grayScale = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.color.grayScale");
            grayScale.process(image.clone(), image); // apply to 'image'

            // Save processed frame to 'processed_frames' folder
            String outputPath = framePath.replace("frames", "processed_frames");
            MarvinImageIO.saveImage(image, outputPath);

            System.out.println("Processed: " + framePath + " -> " + outputPath);
        } catch (Exception e) {
            System.err.println("Error processing frame: " + framePath);
            e.printStackTrace();
        }
    }
}
