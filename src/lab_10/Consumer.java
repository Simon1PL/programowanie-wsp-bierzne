package lab_10;

import org.jcsp.lang.*;

/**
 * Consumer class: reads ints from input channel, displays them, then* terminates when a negative value is read.
 */
public class Consumer implements CSProcess {
    private static int consumerAmount = 0;
    private One2OneChannelInt in;
    private One2OneChannelInt req;
    private int id;
    private int consumedAmount = 0;

    public Consumer(final One2OneChannelInt req, final One2OneChannelInt in) {
        this.req = req;
        this.in = in;
        this.id = consumerAmount++;
    }

    public void run() {
        int item;
        while (true) {
            req.out().write(0);
            item = in.in().read();
            if (item < 0) break;
            System.out.println("Consum: " + item + " " + id);
            consumedAmount ++;
        }
        System.out.println("Consumer ended. " + consumedAmount);
    }
}
