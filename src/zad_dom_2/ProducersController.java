package zad_dom_2;

import org.jcsp.lang.*;

import java.util.Arrays;

/**
 * ProducersController class: reads producer's end channels, when there are no more producers write on endWork channel
 */
public class ProducersController implements CSProcess {
    private int producerAmount;
    private final One2OneChannelInt[] producersEnd;
    private final One2OneChannelInt[] endWork;

    public ProducersController(final One2OneChannelInt[] producersEnd, final One2OneChannelInt[] endWork) {
        this.producersEnd = producersEnd;
        this.endWork = endWork;
        this.producerAmount = producersEnd.length;
    }

    public void run() {
        final Guard[] guards = Arrays.stream(producersEnd).map(One2OneChannelInt::in).toArray(Guard[]::new); // new Guard[producerAmount];
        final Alternative alt = new Alternative(guards);

        while (producerAmount > 0) {
            int index = alt.select();
            producersEnd[index].in().read();
            producerAmount--;
        }
        Arrays.stream(endWork).forEach(channel -> channel.out().write(-1));
        System.out.println("\u001B[37m" + "All producers ends");
    }
}
