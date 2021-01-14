package zad_dom_2;

import org.jcsp.lang.*;

import java.util.ArrayList;

/**
 * Main program class for Producer/Consumer example. Sets up channels, creates processes then executes them in parallel, using JCSP.
*/
public final class Main {
    public static final int BUFFER_SIZE = 20;
    public static final int MAX_ITEMS_AMOUNT_PER_ONE_CONSUME_ACTION = 10;
    public static final int MAX_ITEMS_AMOUNT_PER_ONE_PRODUCE_ACTION = 10;
    public static final int HOW_MANY_TIMES_EACH_PRODUCER_PRODUCED = 10;

    public static void main(String[] args) {
        int producerAmount = 5;
        int consumerAmount = 5;
        int buffersAmount = 5;

        ArrayList<CSProcess> processesList = new ArrayList<>();
        // Create channel objects:
        final One2OneChannelInt[] producersChannels = new One2OneChannelInt[producerAmount]; // Producers data
        final One2OneChannelInt[] producersEndsChannels = new One2OneChannelInt[producerAmount]; // Producers end
        for (int i = 0; i < producerAmount; i++) {
            producersChannels[i] = Channel.one2oneInt();
            producersEndsChannels[i] = Channel.one2oneInt();
            processesList.add(new Producer(producersChannels[i], producersEndsChannels[i]));
        }

        final One2OneChannelInt[] consumersRequestsChannels = new One2OneChannelInt[consumerAmount]; // Consumer requests
        final One2OneChannelInt[] consumersChannels = new One2OneChannelInt[consumerAmount]; // Consumer data
        for (int i = 0; i < consumerAmount; i++) {
            consumersRequestsChannels[i] = Channel.one2oneInt();
            consumersChannels[i] = Channel.one2oneInt();
            processesList.add(new Consumer(consumersRequestsChannels[i], consumersChannels[i]));
        }

        final One2OneChannelInt[] buffersChannels = new One2OneChannelInt[buffersAmount]; // Buffer channel, send index of consumer/producer channel
        final One2OneChannelInt[] buffersReadyChannels = new One2OneChannelInt[buffersAmount]; // Buffer channel, send info when buffer is ready or when end work
        final One2OneChannelInt[] endWorkChannels = new One2OneChannelInt[buffersAmount];
        for (int i = 0; i < buffersAmount; i++) {
            buffersChannels[i] = Channel.one2oneInt();
            buffersReadyChannels[i] = Channel.one2oneInt();
            endWorkChannels[i] = Channel.one2oneInt();
            processesList.add(new Buffer(producersChannels, consumersRequestsChannels, consumersChannels, buffersChannels[i], buffersReadyChannels[i], endWorkChannels[i]));
        }

        processesList.add(new BufferController(producersChannels, consumersRequestsChannels, consumersChannels, buffersChannels, buffersReadyChannels, endWorkChannels));
        processesList.add(new ProducersController(producersEndsChannels, endWorkChannels));

        Parallel par = new Parallel(processesList.toArray(new CSProcess[0])); // Parallel construct
        par.run(); // Execute processes in parallel
    }
}
