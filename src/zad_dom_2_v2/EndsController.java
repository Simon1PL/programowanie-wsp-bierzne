package zad_dom_2_v2;

import org.jcsp.lang.*;

import java.util.Arrays;

/**
 * ProducersController class: reads producer's ends channels, when there are no more producers write on endWork channels
 */
public class EndsController implements CSProcess {
    private int producersAmount;
    private int buffersAmount;
    private final Any2OneChannel[] buffersChannels;
    private final One2AnyChannelInt[] buffersReadyChannels;
    private final One2OneChannelInt endsChannel;

    public EndsController(final Any2OneChannel[] buffersChannels, final One2AnyChannelInt[] buffersReadyChannels, final One2OneChannelInt endsChannel, int producersAmount) {
        this.buffersChannels = buffersChannels;
        this.buffersReadyChannels = buffersReadyChannels;
        this.endsChannel = endsChannel;
        this.producersAmount = producersAmount;
        this.buffersAmount = buffersChannels.length;
    }

    public void run() {
        final Guard[] buffersGuards = Arrays.stream(buffersReadyChannels).map(One2AnyChannelInt::in).toArray(Guard[]::new);
        final Alternative readyBuffersAlt = new Alternative(buffersGuards);
        while (producersAmount > 0) {
            int endInfo = endsChannel.in().read();
            if (endInfo == 1) {
                producersAmount--;
            }
            else if (endInfo == 2) {
                System.err.println("Buffer ends before all producers end!");
            }
            else {
                System.err.println("Unknown end info!");
            }
        }
        System.out.println("\u001B[37m" + "All producers ends");
        for (int i = 0; i < buffersChannels.length; i++) {
            int bufferIndex = readyBuffersAlt.select();
            buffersReadyChannels[bufferIndex].in().read();
            buffersChannels[bufferIndex].out().write(new DataForBuffer("End", 0));
        }
        while (buffersAmount > 0) {
            int endInfo = endsChannel.in().read();
            if (endInfo == 2) {
                buffersAmount--;
            }
            else if (endInfo == 1) {
                System.err.println("Producers ends too many times!");
            }
            else {
                System.err.println("Unknown end info!");
            }
        }
        System.out.println("\u001B[37m" + "All buffers ends");
        for (One2AnyChannelInt bufferReadyChannel : buffersReadyChannels) {
            bufferReadyChannel.out().write(-1);
        }
        System.out.println("\u001B[37m" + "EndsController ends");
    }
}
