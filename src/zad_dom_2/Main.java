package zad_dom_2;

import org.jcsp.lang.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Main program class for Producer/Consumer example.* Sets up channels, creates processes then* executes them in parallel, using JCSP.
*/
public final class Main {
    public static final int BUFFER_SIZE = 1000;

    public static void main(String[] args) {
        int producerAmount = 2;
        int consumerAmount = 2;
        int buffersAmount = 2;

        ArrayList<CSProcess> procList = new ArrayList<>();
        // Create channel objects:
        final One2OneChannelInt[] prodChan = new One2OneChannelInt[producerAmount]; // Producers data
        final One2OneChannelInt[] producersEndChan = new One2OneChannelInt[producerAmount]; // Producers end
        for (int i = 0; i < producerAmount; i++) {
            prodChan[i] = Channel.one2oneInt();
            producersEndChan[i] = Channel.one2oneInt();
            procList.add(new Producer(prodChan[i], producersEndChan[i]));
        }
        final One2OneChannelInt[] consReq = new One2OneChannelInt[consumerAmount]; // Consumer requests
        final One2OneChannelInt[] consChan = new One2OneChannelInt[consumerAmount]; // Consumer data
        for (int i = 0; i < consumerAmount; i++) {
            consReq[i] = Channel.one2oneInt();
            consChan[i] = Channel.one2oneInt();
            procList.add(new Consumer(consReq[i], consChan[i]));
        }
        final One2OneChannelInt[] bufferChannels = new One2OneChannelInt[buffersAmount]; // Buffer channel, send index of consumer/producer channel
        final One2OneChannelInt[] bufferReadyChannels = new One2OneChannelInt[buffersAmount]; // Buffer channel, send info when buffer is ready or when end work
        final One2OneChannelInt[] endWorkChan = new One2OneChannelInt[buffersAmount];
        for (int i = 0; i < buffersAmount; i++) {
            bufferChannels[i] = Channel.one2oneInt();
            bufferReadyChannels[i] = Channel.one2oneInt();
            endWorkChan[i] = Channel.one2oneInt();
            procList.add(new Buffer(prodChan, consReq, consChan, bufferChannels, bufferReadyChannels, endWorkChan[i]));
        }

        procList.add(new BufferController(prodChan, consReq, bufferChannels, bufferReadyChannels, endWorkChan));
        procList.add(new ProducersController(producersEndChan, endWorkChan));
        Parallel par = new Parallel(procList.toArray(new CSProcess[0])); // Parallel construct
        par.run(); // Execute processes in parallel
    }
}
