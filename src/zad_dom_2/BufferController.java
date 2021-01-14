package zad_dom_2;

import org.jcsp.lang.*;

import java.util.Arrays;

/**
 * BufferController class: Manages communication between Producers/Consumers and Buffers classes.
 */

public class BufferController implements CSProcess {
    private final One2OneChannelInt[] producersChannels; // Inputs from Producer
    private final One2OneChannelInt[] consumersRequestsChannels; // Requests for data from Consumer
    private final One2OneChannelInt[] consumersChannels; // Output for Consumer, need here to end consumers work
    private final One2OneChannelInt[] buffersChannels;
    private final One2OneChannelInt[] buffersReadyChannels;
    private final One2OneChannelInt[] endWorkChannels;
    private int consumedItemsAmount = 0;

    public BufferController(final One2OneChannelInt[] producersChannels, final One2OneChannelInt[] consumersRequestsChannels, final One2OneChannelInt[] consumersChannels, final One2OneChannelInt[] buffersChannels, final One2OneChannelInt[] buffersReadyChannels, final One2OneChannelInt[] endWorkChannels) {
        this.producersChannels = producersChannels;
        this.consumersRequestsChannels = consumersRequestsChannels;
        this.consumersChannels = consumersChannels;
        this.buffersChannels = buffersChannels;
        this.buffersReadyChannels = buffersReadyChannels;
        this.endWorkChannels = endWorkChannels;
    }

    public void run() {
        final Guard[] guards = new Guard[producersChannels.length + consumersRequestsChannels.length + buffersChannels.length]; // = [...producersChannels, ...consumersRequestsChannels, ...endWorkChannels]
        for (int i = 0; i < producersChannels.length; i++) {
            guards[i] = producersChannels[i].in();
        }
        for (int i = 0; i < consumersRequestsChannels.length; i++) {
            guards[i + producersChannels.length] = consumersRequestsChannels[i].in();
        }
        for (int i = 0; i < buffersChannels.length; i++) {
            guards[i + producersChannels.length + consumersRequestsChannels.length] = endWorkChannels[i].in();
        }
        final Alternative alt = new Alternative(guards);

        final Guard[] buffersGuards = Arrays.stream(buffersReadyChannels).map(One2OneChannelInt::in).toArray(Guard[]::new); // new Guard[buffers.length];
        final Alternative buffersAlt = new Alternative(buffersGuards);

        int buffersAmount = buffersChannels.length; // Number of buffers running
        while (buffersAmount > 0) {
            int bufferIndex = buffersAlt.select();
            int bufferReadyStatus = buffersReadyChannels[bufferIndex].in().read();
            if (bufferReadyStatus == -1) {
                buffersAmount--;
            }
            else if (bufferReadyStatus == 1) {
                int index = alt.select(); // alt.fairSelect();
                buffersChannels[bufferIndex].out().write(index);
            }
            else {
                System.err.println("Buffer ready status should have only value \"-1\" or \"1\"!");
            }
        }
        System.out.println("\u001B[37m" + "All buffers ends");

        for (int i = 0; i < consumersChannels.length; i++) {
            consumersRequestsChannels[i].in().read();
            consumersChannels[i].out().write(-1);
            consumedItemsAmount += consumersRequestsChannels[i].in().read();
        }
        System.out.println("\u001B[37m" + "All consumers ends, consumed items: " + consumedItemsAmount);
    }
}
