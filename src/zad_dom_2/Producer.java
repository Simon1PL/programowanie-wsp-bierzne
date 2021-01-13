package zad_dom_2;

import org.jcsp.lang.*;

import java.util.Random;

/**
 * Producer class: produces random amount of items 10 times and sends them on output channel, then sends -1 and terminates.
 */
public class Producer implements CSProcess {
    private static int producerAmount = 0;
    private final One2OneChannelInt channel;
    private final One2OneChannelInt producerEnd;
    private final int id;
    private int producedAmount = 0;

    public Producer(final One2OneChannelInt out, final One2OneChannelInt producerEnd) {
        channel = out;
        this.producerEnd = producerEnd;
        this.id = producerAmount++;
    }

    public void run() {
        int itemsAmount;
        for (int k = 0; k < 10; k++) { // produce 10 times
            itemsAmount = new Random().nextInt(10) + 1;
            channel.out().write(itemsAmount);
            System.out.println("\u001B[36m" + "Produce: " + itemsAmount + " by P " + id + ".");
            producedAmount += itemsAmount;
        }
        producerEnd.out().write(-1);
        System.out.println("\u001B[36m" + "Producer " + id + " ended. Produce: " + producedAmount);
    }
}