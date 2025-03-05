package org.example;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MasterNode {
    public static void main(String[] args) {
        String videoPath = "input.mp4";        // Input video file
        String framesDir = "frames";           // Directory to store extracted frames

        // Ensure frames directory exists
        File dir = new File(framesDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        int frameNumber = 0;
        try {
            // Extract frames using JCodec
            FrameGrab grab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(new File(videoPath)));
            while (true) {
                Picture picture = grab.getNativeFrame();
                if (picture == null) {
                    break; // No more frames
                }

                // Convert JCodec Picture to BufferedImage
                BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);

                // Save the frame as a JPEG
                File outputFile = new File(framesDir, "frame_" + frameNumber + ".jpg");
                ImageIO.write(bufferedImage, "jpg", outputFile);
                System.out.println("Saved frame: " + outputFile.getAbsolutePath());

                frameNumber++;
            }
        } catch (IOException | JCodecException e) {
            e.printStackTrace();
        }

        System.out.println("Total frames extracted: " + frameNumber);

        // Distribute frames to worker nodes
        distributeFrames(frameNumber);
    }

    private static void distributeFrames(int totalFrames) {
        int numWorkers = 2; // e.g., two worker nodes
        int framesPerWorker = totalFrames / numWorkers;

        for (int i = 0; i < numWorkers; i++) {
            int startFrame = i * framesPerWorker;
            int endFrame = (i == numWorkers - 1) ? totalFrames : startFrame + framesPerWorker;

            // Using the 2-argument constructor
            new Thread(new WorkerClient(startFrame, endFrame)).start();
        }
    }
}
