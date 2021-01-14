package zad_dom_2;

import org.jcsp.lang.*;

import java.util.Arrays;

/**
 * ProducersController class: reads producer's ends channels, when there are no more producers write on endWork channels
 */
public class ProducersController implements CSProcess {
    private int producerAmount;
    private final One2OneChannelInt[] producersEndsChannels;
    private final One2OneChannelInt[] endWorkChannels;
    private int producedItemsAmount = 0;

    public ProducersController(final One2OneChannelInt[] producersEndsChannels, final One2OneChannelInt[] endWorkChannels) {
        this.producersEndsChannels = producersEndsChannels;
        this.endWorkChannels = endWorkChannels;
        this.producerAmount = producersEndsChannels.length;
    }

    public void run() {
        final Guard[] guards = Arrays.stream(producersEndsChannels).map(One2OneChannelInt::in).toArray(Guard[]::new); // new Guard[producerAmount];
        final Alternative alt = new Alternative(guards);

        while (producerAmount > 0) {
            int index = alt.select();
            producedItemsAmount += producersEndsChannels[index].in().read();
            producerAmount--;
        }
        for (One2OneChannelInt endWorkChannel : endWorkChannels) {
            endWorkChannel.out().write(-1);
        }
        System.out.println("\u001B[37m" + "All producers ends, produced items: " + producedItemsAmount);
    }
}
