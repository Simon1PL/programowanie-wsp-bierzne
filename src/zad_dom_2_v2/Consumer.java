package zad_dom_2_v2;

import org.jcsp.lang.*;

import java.util.Arrays;
import java.util.Random;

/**
 * Consumer class: consumes random amount of items - sends them to one of ready buffer, when bufferReady send -1 terminates.
 */
public class Consumer implements CSProcess {
    private static int consumerAmount = 0;
    private final One2OneChannelInt consumerChannel;
    private final Any2OneChannel[] buffersChannels;
    private final One2AnyChannelInt[] buffersReadyChannels;
    private final String id;
    private int consumedItemsAmount = 0;

    public Consumer(final Any2OneChannel[] buffersChannels, final One2AnyChannelInt[] buffersReadyChannels, final One2OneChannelInt consumerChannel) {
        this.buffersChannels = buffersChannels;
        this.buffersReadyChannels = buffersReadyChannels;
        this.consumerChannel = consumerChannel;
        this.id = "C" + consumerAmount++;
    }

    public void run() {
        final Guard[] buffersGuards = Arrays.stream(buffersReadyChannels).map(One2AnyChannelInt::in).toArray(Guard[]::new); // new Guard[buffersReadyChannels];
        final Alternative readyBuffersAlt = new Alternative(buffersGuards);
        boolean endWork = false;
        int itemsAmount;
        while (!endWork) {
            itemsAmount = new Random().nextInt(Main.MAX_ITEMS_AMOUNT_PER_ONE_CONSUME_ACTION) + 1; // consume random amount of items (eg. 1-10)
            while (itemsAmount > 0) {

                System.out.println("Aaaaa1");
                int bufferIndex = readyBuffersAlt.select();
                System.out.println("Aaaaa2");

                int bufferReadyStatus = buffersReadyChannels[bufferIndex].in().read();
                if (bufferReadyStatus == 1) {
                    buffersChannels[bufferIndex].out().write(new DataForBuffer(this.id, itemsAmount));
                    int itemsTaken = consumerChannel.in().read();
                    itemsAmount -= itemsTaken;
                    consumedItemsAmount += itemsTaken;
                }
                else if (bufferReadyStatus == -1) {
                    endWork = true;
                    break;
                }
                else {
                    System.err.println("Buffer ready status in consumer process should have only value \"1\" or \"-1\"!");
                }
            }
        }
        System.out.println("\u001B[35m" + "Consumer " + id + " ended. Consume: " + consumedItemsAmount);
    }
}
