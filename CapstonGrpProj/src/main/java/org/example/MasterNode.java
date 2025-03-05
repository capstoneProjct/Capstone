// File: MasterNode.java
import java.util.List;

public class MasterNode {
    public static void main(String[] args) {
        // Step 1: Extract frames from the video file
        List<Frame> frames = VideoUtils.extractFrames("input.mp4");

        // Step 2: Distribute frames to worker nodes
        TaskDistributor distributor = new TaskDistributor("localhost", 5000); // host & port for communication
        distributor.distribute(frames);

        // Step 3: Collect processed frames
        List<Frame> processedFrames = distributor.collectProcessedFrames(frames.size());

        // Step 4: Reconstruct video from processed frames
        VideoUtils.createVideo(processedFrames, "output.mp4");

        System.out.println("Video processing completed. Output saved as output.mp4");
    }
}
