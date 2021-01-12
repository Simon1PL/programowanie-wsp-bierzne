package lab_10;

import org.jcsp.lang.*;

/**
 * Producer class: produces 100 random integers and sends them on* output channel, then sends -1 and terminates.* The random integers are in a given range [start...start+100)
 */
public class Producer implements CSProcess {
    private One2OneChannelInt channel;
    private int start;

    public Producer(final One2OneChannelInt out, int start) {
        channel = out;
        this.start = start;
    }

    public void run() {
        int item;
        for (int k = 0; k < 100; k++) {
            item = k + start;
            channel.out().write(item);
//            System.out.println("Produce: " + item + ".");
        }
        channel.out().write(-1);
        System.out.println("Producer" + start + " ended.");
    }
}