package zad_dom_2_v2;

import org.jcsp.lang.*;

import java.util.Arrays;
import java.util.Random;

/**
 * Producer class: produces random amount of items X times - sends them to one of ready buffer, after all sends 1 to endsChannel and terminates.
 */
public class Producer implements CSProcess {
    private static int producerAmount = 0;
    private final One2OneChannelInt producerChannel;
    private final One2OneChannelInt producerEndsChannel;
    private final Any2OneChannel[] buffersChannels;
    private final One2AnyChannelInt[] buffersReadyChannels;
    private final String id;
    private int producedItemsAmount = 0;

    public Producer(final Any2OneChannel[] buffersChannels, final One2AnyChannelInt[] buffersReadyChannels, final One2OneChannelInt producerChannel, final One2OneChannelInt producerEndsChannel) {
        this.buffersChannels = buffersChannels;
        this.buffersReadyChannels = buffersReadyChannels;
        this.producerChannel = producerChannel;
        this.producerEndsChannel = producerEndsChannel;
        this.id = "P" + producerAmount++; // P0, P1, ...
    }

    public void run() {
        final Guard[] buffersGuards = new Guard[buffersReadyChannels.length];
        for (int i = 0; i < buffersReadyChannels.length; i++) {
            buffersGuards[i] = (Guard) buffersReadyChannels[i].in();
        }
        final Alternative readyBuffersAlt = new Alternative(buffersGuards);
        int itemsAmount;
        for (int k = 0; k < Main.HOW_MANY_TIMES_EACH_PRODUCER_PRODUCED; k++) { // eg. produce 10 times, next end work
            itemsAmount = new Random().nextInt(Main.MAX_ITEMS_AMOUNT_PER_ONE_PRODUCE_ACTION) + 1; // produce random amount of items (eg. 1-10)
            while (itemsAmount > 0) {
                int bufferIndex = readyBuffersAlt.select();
                int bufferReadyStatus = buffersReadyChannels[bufferIndex].in().read();
                if (bufferReadyStatus == 1) {
                    buffersChannels[bufferIndex].out().write(new DataForBuffer(this.id, itemsAmount));
                    int itemsAdded = producerChannel.in().read();
                    itemsAmount -= itemsAdded;
                    producedItemsAmount += itemsAdded;
                }
                else {
                    System.err.println("Buffer ready status in producer process should have only value \"1\"!");
                }
            }

        }
        producerEndsChannel.out().write(1);
        System.out.println("\u001B[35m" + "Producer " + id + " ended. Produce: " + producedItemsAmount);
    }
}