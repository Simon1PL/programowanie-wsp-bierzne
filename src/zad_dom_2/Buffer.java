package zad_dom_2;

import org.jcsp.lang.*;

import java.util.Arrays;

import static java.lang.Math.min;

/**
 * Buffer class: Manages communication between Producer and Consumer classes. JUST A BUFFER :)
 */

public class Buffer implements CSProcess {
    private static int bufferAmount = 0;
    private final int id;
    private final One2OneChannelInt[] producersChannels; // Inputs from Producer
    private final One2OneChannelInt[] consumersRequestsChannels; // Requests for data from Consumer
    private final One2OneChannelInt[] consumersChannels; // Outputs to Consumer
    private final One2OneChannelInt bufferChannel;
    private final One2OneChannelInt bufferReadyChannel;
    private final One2OneChannelInt endWorkChannel;
    private final int[] buffer = new int[Main.BUFFER_SIZE]; // The buffer itself
    int nextItemToProduceIndex = 0;
    int nextItemToConsumeIndex = 0;

    public Buffer(final One2OneChannelInt[] producersChannels, final One2OneChannelInt[] consumersRequestsChannels, final One2OneChannelInt[] consumersChannels, final One2OneChannelInt bufferChannel, final One2OneChannelInt bufferReadyChannel, final One2OneChannelInt endWorkChannel) {
        this.producersChannels = producersChannels;
        this.consumersRequestsChannels = consumersRequestsChannels;
        this.consumersChannels = consumersChannels;
        this.bufferChannel = bufferChannel;
        this.bufferReadyChannel = bufferReadyChannel;
        this.endWorkChannel = endWorkChannel;
        this.id = bufferAmount++;
        Arrays.fill(this.buffer, -1);
    }

    private void consumeProducts(int consumerIndex) {
        if (nextItemToConsumeIndex < nextItemToProduceIndex) // There are item(s) available
        {
            int wantedItemsAmount = consumersRequestsChannels[consumerIndex].in().read(); // Read and discard request
            int availableItemsAmount = nextItemToProduceIndex - nextItemToConsumeIndex;
            int takenItemsAmount = min(wantedItemsAmount, availableItemsAmount);
            for (int i = nextItemToConsumeIndex; i < nextItemToConsumeIndex + takenItemsAmount; i++) {
                if (buffer[i % buffer.length] == -1) {
                    System.err.println("Sth goes wrong! Take nonexistent product!");
                }
                buffer[i % buffer.length] = -1;
            }
            nextItemToConsumeIndex += takenItemsAmount;
            consumersChannels[consumerIndex].out().write(takenItemsAmount);
        }
    }

    private void addProducts(int producerIndex) {
        if (nextItemToProduceIndex - nextItemToConsumeIndex < Main.BUFFER_SIZE) { // There are space available
            int wantedSpace = producersChannels[producerIndex].in().read();
            int itemsInBuffer = nextItemToProduceIndex - nextItemToConsumeIndex;
            int availableSpace = Main.BUFFER_SIZE - itemsInBuffer;
            int addedItemsAmount = min(wantedSpace, availableSpace);
            for (int i = nextItemToProduceIndex; i < nextItemToProduceIndex + addedItemsAmount; i++) {
                if (buffer[i % buffer.length] != -1) {
                    System.err.println("Sth goes wrong! Add product to the occupied space!");
                }
                buffer[i % buffer.length] = addedItemsAmount;
            }
            nextItemToProduceIndex +=addedItemsAmount;
            producersChannels[producerIndex].out().write(addedItemsAmount);
        }
    }

    public void run() {
        while (true) {
            bufferReadyChannel.out().write(1); // signal ready for work
            int index = bufferChannel.in().read(); // index from array: [...producersChannels, ...consumersRequestsChannels, ...endWorkChannels]

            if (index < producersChannels.length) { // Index of a producer (the producer is ready to send)
                addProducts(index);
            }
            else if (index < producersChannels.length + consumersRequestsChannels.length) { // Index of an consumer (the consumer is ready to read)
                int consumerIndex = index - producersChannels.length;
                consumeProducts(consumerIndex);
            }
            else { // If there are no working producers: use accumulated products, next signal buffer ends
                int endWorkStatus = endWorkChannel.in().read();
                while (nextItemToConsumeIndex < nextItemToProduceIndex) {
                    bufferReadyChannel.out().write(1); // signal ready for work
                    int tmpIndex = bufferChannel.in().read();
                    if (tmpIndex < producersChannels.length+ consumersRequestsChannels.length ) { // If this is customer's channel index (not endWork)
                        int consumerIndex = tmpIndex - producersChannels.length;
                        consumeProducts(consumerIndex);
                    }
                }
                if (endWorkStatus == -1) {
                    break;
                } else {
                    System.err.println("Buffer's end channel should send only value \"-1\"!");
                }
            }
        }
        bufferReadyChannel.out().write(-1); // signal end work
        System.out.println("\u001B[35m" + "Buffer " + id + " ended. Items produced in this buffer: " + nextItemToProduceIndex + ", consumed from this buffer: " + nextItemToConsumeIndex);
    }
}
