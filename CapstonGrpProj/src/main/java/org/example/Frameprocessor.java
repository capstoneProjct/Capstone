// File: FrameProcessor.java
public class FrameProcessor implements Runnable {
    private Frame frame;
    private SocketCommunication comm;

    public FrameProcessor(Frame frame, SocketCommunication comm) {
        this.frame = frame;
        this.comm = comm;
    }

    @Override
    public void run() {
        // Process the frame (e.g., convert to grayscale)
        Frame processedFrame = VideoUtils.processFrame(frame);

        // Send the processed frame back to the master node
        comm.sendProcessedFrame(processedFrame);
    }
}
