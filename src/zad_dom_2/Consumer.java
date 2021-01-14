package zad_dom_2;

import org.jcsp.lang.*;

import java.util.Random;

/**
 * Consumer class: write on request channel then reads productAmount from consumer channel, terminates when a negative value is read.
 */
public class Consumer implements CSProcess {
    private static int consumerAmount = 0;
    private final One2OneChannelInt consumerChannel;
    private final One2OneChannelInt consumerRequestChannel;
    private final int id;
    private int consumedItemsAmount = 0;

    public Consumer(final One2OneChannelInt consumerRequestChannel, final One2OneChannelInt consumerChannel) {
        this.consumerRequestChannel = consumerRequestChannel;
        this.consumerChannel = consumerChannel;
        this.id = consumerAmount++;
    }

    public void run() {
        int itemsAmount;
        boolean endWork = false;
        while (!endWork) {
            itemsAmount = new Random().nextInt(Main.MAX_ITEMS_AMOUNT_PER_ONE_CONSUME_ACTION) + 1; // consume random amount of items (eg. 1-10)
            while (itemsAmount > 0) {
                consumerRequestChannel.out().write(itemsAmount);
                int itemsTaken = consumerChannel.in().read();
                if (itemsTaken < 0) {
                    endWork = true;
                    break;
                }
                String notEnoughAmount = itemsTaken != itemsAmount ? " Wanted amount: " + itemsAmount : "";
                System.out.println("\u001B[32m" + "Consume: " + itemsTaken + " by C " + id + "." + notEnoughAmount);
                itemsAmount -= itemsTaken;
                consumedItemsAmount += itemsTaken;
            }
        }
        consumerRequestChannel.out().write(consumedItemsAmount);
        System.out.println("\u001B[32m" + "Consumer " + id + " ended. Consume: " + consumedItemsAmount);
    }
}
