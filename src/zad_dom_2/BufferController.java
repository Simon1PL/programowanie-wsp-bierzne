package zad_dom_2;

import org.jcsp.lang.*;

import java.util.Arrays;

/**
 * BufferController class: Manages communication between Producers/Consumers and Buffers classes.
 */

public class BufferController implements CSProcess {
    private final One2OneChannelInt[] in; // Inputs from Producer
    private final One2OneChannelInt[] req; // Requests for data from Consumer
    private final One2OneChannelInt[] buffers;
    private final One2OneChannelInt[] buffersReady;
    private final One2OneChannelInt[] endWorkChan;

    public BufferController(final One2OneChannelInt[] in, final One2OneChannelInt[] req, final One2OneChannelInt[] buffers, final One2OneChannelInt[] buffersReady, final One2OneChannelInt[] endWorkChan) {
        this.in = in;
        this.req = req;
        this.buffers = buffers;
        this.buffersReady = buffersReady;
        this.endWorkChan = endWorkChan;
    }

    public void run() {
        final Guard[] guards = new Guard[in.length + req.length + buffers.length]; // = [[in.in()], [req.in()], [endWork.in()]]
        final Guard[] buffersGuards = Arrays.stream(buffersReady).map(One2OneChannelInt::in).toArray(Guard[]::new); // new Guard[buffers.length];
        for (int i = 0; i < in.length; i++) {
            guards[i] = in[i].in();
        }
        for (int i = 0; i < req.length; i++) {
            guards[i + in.length] = req[i].in();
        }
        for (int i = 0; i < buffers.length; i++) {
            guards[i + in.length + req.length] = endWorkChan[i].in();
        }
        final Alternative alt = new Alternative(guards);
        final Alternative buffersAlt = new Alternative(buffersGuards);
        int buffersAmount = buffers.length; // Number of buffers running
        while (buffersAmount > 0) {
            int bufferIndex = buffersAlt.select();
            int bufferInfo = buffersReady[bufferIndex].in().read();
            if (bufferInfo == -1) {
                buffersAmount--;
            }
            else if (bufferInfo == 1) {
                int index = alt.select(); // alt.fairSelect();
                buffers[bufferIndex].out().write(index);
            }
            else {
                System.err.println("Buffers channel should send only value \"-1\" or \"1\"!");
            }
        }
    }
}
