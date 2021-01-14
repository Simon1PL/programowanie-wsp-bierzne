package zad_dom_2;

import org.jcsp.lang.*;

import java.util.Random;

/**
 * Producer class: produces random amount of items X times and sends them on producer channel, then sends -1 to producerEndsChannel and terminates.
 */
public class Producer implements CSProcess {
    private static int producerAmount = 0;
    private final One2OneChannelInt producersChannel;
    private final One2OneChannelInt producersEndsChannel;
    private final int id;
    private int producedItemsAmount = 0;

    public Producer(final One2OneChannelInt producersChannel, final One2OneChannelInt producersEndsChannel) {
        this.producersChannel = producersChannel;
        this.producersEndsChannel = producersEndsChannel;
        this.id = producerAmount++;
    }

    public void run() {
        int itemsAmount;
        for (int k = 0; k < Main.HOW_MANY_TIMES_EACH_PRODUCER_PRODUCED; k++) { // eg. produce 10 times, next end work
            itemsAmount = new Random().nextInt(Main.MAX_ITEMS_AMOUNT_PER_ONE_PRODUCE_ACTION) + 1; // produce random amount of items (eg. 1-10)
            while (itemsAmount > 0) {
                producersChannel.out().write(itemsAmount);
                int itemsAdded = producersChannel.in().read();
                String notEnoughSpace = itemsAdded != itemsAmount ? " Wanted amount: " + itemsAmount : "";
                System.out.println("\u001B[36m" + "Produce: " + itemsAdded + " by P " + id + "." + notEnoughSpace);
                itemsAmount -= itemsAdded;
                producedItemsAmount += itemsAdded;
            }
        }
        producersEndsChannel.out().write(producedItemsAmount);
        System.out.println("\u001B[36m" + "Producer " + id + " ended. Produce: " + producedItemsAmount);
    }
}