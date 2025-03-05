package org.example;

import org.jcodec.api.SequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.common.model.Rational;
import org.jcodec.common.Format;
import org.jcodec.common.Codec;
import org.jcodec.common.model.ColorSpace;
import org.jcodec.scale.AWTUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class VideoReconstructor {
    public static void main(String[] args) {
        String processedDir = "processed_frames";
        String outputVideo = "output.mp4";

        File folder = new File(processedDir);
        File[] frameFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg"));
        if (frameFiles == null || frameFiles.length == 0) {
            System.err.println("No frames found in " + processedDir);
            return;
        }

        // Sort frames by filename to maintain the correct order
        Arrays.sort(frameFiles, Comparator.comparing(File::getName));

        try {
            // Create a SequenceEncoder with additional parameters:
            // - A writable channel for the output file,
            // - Frame rate set to 30 FPS,
            // - MOV format and H264 video codec,
            // - null for audio codec.
            SequenceEncoder encoder = new SequenceEncoder(
                    NIOUtils.writableChannel(new File(outputVideo)),
                    Rational.R(30, 1),
                    Format.MOV,
                    Codec.H264,
                    null
            );

            for (File frame : frameFiles) {
                BufferedImage image = ImageIO.read(frame);
                // Convert BufferedImage to a JCodec Picture using the RGB ColorSpace
                Picture picture = AWTUtil.fromBufferedImage(image, ColorSpace.RGB);
                // Encode the frame using encodeNativeFrame
                encoder.encodeNativeFrame(picture);
                System.out.println("Encoded: " + frame.getName());
            }
            encoder.finish();
            System.out.println("Video reconstruction completed: " + outputVideo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
