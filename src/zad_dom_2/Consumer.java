package zad_dom_2;

import org.jcsp.lang.*;

import java.util.Random;

/**
 * Consumer class: reads ints from input channel, displays them, then* terminates when a negative value is read.
 */
public class Consumer implements CSProcess {
    private static int consumerAmount = 0;
    private final One2OneChannelInt in;
    private final One2OneChannelInt req;
    private final int id;
    private int consumedAmount = 0;

    public Consumer(final One2OneChannelInt req, final One2OneChannelInt in) {
        this.req = req;
        this.in = in;
        this.id = consumerAmount++;
    }

    public void run() {
        int itemsAmount;
        boolean endWork = false;
        while (!endWork) {
            itemsAmount = new Random().nextInt(10) + 1;
            while (itemsAmount > 0) {
                req.out().write(itemsAmount);
                int itemsTaken = in.in().read();
                if (itemsTaken < 0) {
                    endWork = true;
                    break;
                }
                String notEnoughAmount = itemsTaken != itemsAmount ? " Wanted amount: " + itemsAmount : "";
                System.out.println("\u001B[32m" + "Consume: " + itemsTaken + " by C " + id + "." + notEnoughAmount);
                itemsAmount -= itemsTaken;
                consumedAmount += itemsTaken;
            }
        }
        System.out.println("\u001B[32m" + "Consumer " + id + " ended. Consume: " + consumedAmount);
    }
}
