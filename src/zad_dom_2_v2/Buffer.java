package zad_dom_2_v2;

import org.jcsp.lang.*;

import java.util.Arrays;

import static java.lang.Math.min;

/**
 * Buffer class: Manages communication between Producer and Consumer classes. JUST A BUFFER :)
 */

public class Buffer implements CSProcess {
    private static int bufferAmount = 0;
    private final int id;
    private final One2OneChannelInt[] producersChannels; // Outputs to Producers
    private final One2OneChannelInt[] consumersChannels; // Outputs to Consumers
    private final Any2OneChannel bufferChannel;
    private final One2AnyChannelInt bufferReadyChannel;
    private final One2OneChannelInt endsChannel;
    private final int[] buffer = new int[Main.BUFFER_SIZE]; // The buffer itself
    int nextItemToProduceIndex = 0;
    int nextItemToConsumeIndex = 0;

    public Buffer(final One2OneChannelInt[] producersChannels, final One2OneChannelInt[] consumersChannels, final Any2OneChannel bufferChannel, final One2AnyChannelInt bufferReadyChannel, final One2OneChannelInt endsChannel) {
        this.producersChannels = producersChannels;
        this.consumersChannels = consumersChannels;
        this.bufferChannel = bufferChannel;
        this.bufferReadyChannel = bufferReadyChannel;
        this.endsChannel = endsChannel;
        this.id = bufferAmount++;
        Arrays.fill(this.buffer, -1);
    }

    private void consumeProducts(int wantedItemsAmount, int consumerIndex) {
        int availableItemsAmount = nextItemToProduceIndex - nextItemToConsumeIndex;
        int takenItemsAmount = min(wantedItemsAmount, availableItemsAmount);
        for (int i = nextItemToConsumeIndex; i < nextItemToConsumeIndex + takenItemsAmount; i++) {
            if (buffer[i % buffer.length] == -1) {
                System.err.println("Sth goes wrong! Take nonexistent product!");
            }
            buffer[i % buffer.length] = -1;
        }
        String notEnoughAmount = takenItemsAmount != wantedItemsAmount ? " Wanted amount: " + wantedItemsAmount : "";
        System.out.println("\u001B[32m" + "Buffer" + id + ": consume " + takenItemsAmount + " by C" + id + "." + notEnoughAmount);
        nextItemToConsumeIndex += takenItemsAmount;
        consumersChannels[consumerIndex].out().write(takenItemsAmount);
    }

    private void addProducts(int wantedSpace, int producerIndex) {
        int itemsInBuffer = nextItemToProduceIndex - nextItemToConsumeIndex;
        int availableSpace = Main.BUFFER_SIZE - itemsInBuffer;
        int addedItemsAmount = min(wantedSpace, availableSpace);
        for (int i = nextItemToProduceIndex; i < nextItemToProduceIndex + addedItemsAmount; i++) {
            if (buffer[i % buffer.length] != -1) {
                System.err.println("Sth goes wrong! Add product to the occupied space!");
            }
            buffer[i % buffer.length] = addedItemsAmount;
        }
        String notEnoughSpace = addedItemsAmount != wantedSpace ? " Wanted amount: " + wantedSpace : "";
        System.out.println("\u001B[36m" + "Buffer" + id + ": produce " + addedItemsAmount + " by P" + producerIndex + "." + notEnoughSpace);
        nextItemToProduceIndex +=addedItemsAmount;
        producersChannels[producerIndex].out().write(addedItemsAmount);
    }

    public void run() {
        while (true) {
            bufferReadyChannel.out().write(1); // signal ready for work
            DataForBuffer dataForBuffer = (DataForBuffer)bufferChannel.in().read(); // Read and discard request

            if (dataForBuffer.senderID().startsWith("P")) { // the sender is a producer
                addProducts(dataForBuffer.itemsAmount(), Integer.parseInt(dataForBuffer.senderID().substring(1)));
            }
            else if (dataForBuffer.senderID().startsWith("C")) { // the sender is an consumer
                consumeProducts(dataForBuffer.itemsAmount(), Integer.parseInt(dataForBuffer.senderID().substring(1)));
            }
            else if (dataForBuffer.senderID().startsWith("End")) { // the sender is the endsController: use accumulated products, next signal buffer ends
                while (nextItemToConsumeIndex < nextItemToProduceIndex) {
                    bufferReadyChannel.out().write(1); // signal ready for work
                    dataForBuffer = (DataForBuffer)bufferChannel.in().read(); // Read and discard request

                    if (dataForBuffer.senderID().startsWith("C")) { // the sender is an customer
                        consumeProducts(dataForBuffer.itemsAmount(), Integer.parseInt(dataForBuffer.senderID().substring(1)));
                    }
                    else if (dataForBuffer.senderID().startsWith("End")) {
                        System.err.println("Skip cause end was already read!");
                    }
                    else {
                        System.err.println("Wrong sender id!");
                    }
                }
                break;
            }
            else {
                System.err.println("Unknown sender!");
            }
        }
        endsChannel.out().write(2); // signal end work
        System.out.println("\u001B[35m" + "Buffer " + id + " ended. Items produced in this buffer: " + nextItemToProduceIndex + ", consumed from this buffer: " + nextItemToConsumeIndex);
    }
}
