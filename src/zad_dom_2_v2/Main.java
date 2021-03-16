package zad_dom_2_v2;

import org.jcsp.lang.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Main program class for Producer/Consumer example. Sets up channels, creates processes then executes them in parallel, using JCSP.
*/
public final class Main {
    public static final int BUFFER_SIZE = 20;
    public static final int MAX_ITEMS_AMOUNT_PER_ONE_CONSUME_ACTION = 10;
    public static final int MAX_ITEMS_AMOUNT_PER_ONE_PRODUCE_ACTION = 10;
    public static final int HOW_MANY_TIMES_EACH_PRODUCER_PRODUCED = 10;

    public static void main(String[] args) {
        int producerAmount = 2;
        int consumerAmount = 2;
        int buffersAmount = 2;

        ArrayList<CSProcess> processesList = new ArrayList<>();

        // Create channel objects:
        final Any2OneChannel[] buffersChannels = new Any2OneChannel[buffersAmount]; // Consumer/Producer/EndController-Buffer channel, send id of consumer/producer and product amount or id of endController if there are no more producers

        final One2AnyChannelInt[] buffersReadyChannels = new One2AnyChannelInt[buffersAmount]; // Buffer-Consumer/Producer channel, send "1" when buffer is ready or "-1" when program ends

        final One2OneChannelInt[] producersChannels = new One2OneChannelInt[producerAmount]; // Buffer-Producer send info how many items was produced

        final One2OneChannelInt endsChannel = Channel.one2oneInt(); // Producer/Buffer-EndController send "1" when producer ends "2" when buffer ends

        final One2OneChannelInt[] consumersChannels = new One2OneChannelInt[consumerAmount]; //  Buffer-Consumer send info how many items was consumed

        for (int i = 0; i < buffersAmount; i++) {
            buffersChannels[i] = Channel.any2one();
            buffersReadyChannels[i] = Channel.one2anyInt();
            processesList.add(new Buffer(producersChannels, consumersChannels, buffersChannels[i], buffersReadyChannels[i], endsChannel));
        }

        for (int i = 0; i < producerAmount; i++) {
            producersChannels[i] = Channel.one2oneInt();
            processesList.add(new Producer(buffersChannels, buffersReadyChannels, producersChannels[i], endsChannel));
        }

        for (int i = 0; i < consumerAmount; i++) {
            consumersChannels[i] = Channel.one2oneInt();
            processesList.add(new Consumer(buffersChannels, buffersReadyChannels, consumersChannels[i]));
        }

        processesList.add(new EndsController(buffersChannels, buffersReadyChannels, endsChannel, producerAmount));

        Parallel par = new Parallel(processesList.toArray(new CSProcess[0])); // Parallel construct
        par.run(); // Execute processes in parallel
    }
}
