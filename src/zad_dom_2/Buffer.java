package zad_dom_2;

import org.jcsp.lang.*;

import java.util.Arrays;

import static java.lang.Math.min;

/**
 * Buffer class: Manages communication between Producer and Consumer classes.
 */

public class Buffer implements CSProcess {
    private static int bufferAmount = 0;
    private final int id;
    private final One2OneChannelInt[] in; // Inputs from Producer
    private final One2OneChannelInt[] req; // Requests for data from Consumer
    private final One2OneChannelInt[] out; // Outputs to Consumer
    private final One2OneChannelInt[] buffers;
    private final One2OneChannelInt[] buffersReady;
    private final One2OneChannelInt endWork;
    private final int[] buffer = new int[Main.BUFFER_SIZE]; // The buffer itself // Subscripts for buffer
    int addItemIndex = 0;
    int takeItemIndex = 0;

    public Buffer(final One2OneChannelInt[] in, final One2OneChannelInt[] req, final One2OneChannelInt[] out, final One2OneChannelInt[] buffers, final One2OneChannelInt[] buffersReady, final One2OneChannelInt endWork) {
        this.in = in;
        this.req = req;
        this.out = out;
        this.buffers = buffers;
        this.buffersReady = buffersReady;
        this.endWork = endWork;
        this.id = bufferAmount++;
    }

    private void takeProducts(int consumerIndex) {
        if (takeItemIndex < addItemIndex) // Item(s) available
        {
            int wantedItemsAmount = req[consumerIndex].in().read(); // Read and discard request
            int availableItemsAmount = addItemIndex - takeItemIndex;
            int takenItemsAmount = min(wantedItemsAmount, availableItemsAmount);
            for (int i = takeItemIndex; i < takeItemIndex + takenItemsAmount; i++) {
                if (buffer[i % buffer.length] == -1) {
                    System.err.println("Take nonexistent product!");
                }
                buffer[i % buffer.length] = -1;
            }
            takeItemIndex += takenItemsAmount;
            out[consumerIndex].out().write(takenItemsAmount);
        }
    }

    private void addProducts(int producerIndex) {
        if (addItemIndex - takeItemIndex < Main.BUFFER_SIZE) { // Space available
            // DODAC WARUNEK SPRAWDZAJACY CZY ZMIESCI SIE N ITEMOW
            // zrobic rozproszone buffery, gdy w jednym bufforze nie ma miejsca to przekazujemy do innego
            int itemsAmount = in[producerIndex].in().read();
            for (int i = addItemIndex; i < addItemIndex + itemsAmount; i++) {
                if (buffer[i % buffer.length] == -1) {
                    System.err.println("Add product to the occupied space!");
                }
                buffer[i % buffer.length] = itemsAmount;
            }
            addItemIndex+=itemsAmount;
        }
    }

    public void run() {
        final Guard[] guards = Arrays.stream(buffers).map(One2OneChannelInt::in).toArray(Guard[]::new);
        final Alternative alt = new Alternative(guards);
        while (true) {
            buffersReady[id].out().write(1); // signal ready for work
            int index = buffers[id].in().read();

            if (index < in.length) { // A Producer is ready to send
                addProducts(index);
            }
            else if (index < in.length + req.length) { // A Consumer is ready to read
                int consumerIndex = index - in.length;
                takeProducts(consumerIndex);
            }
            else { // If there are no working producers: use accumulated products, next signal endWork
                while (takeItemIndex < addItemIndex) {
                    int consumerIndex = alt.select() - in.length;
                    takeProducts(consumerIndex);
                }
                if (endWork.in().read() == -1) {
                    break;
                } else {
                    System.err.println("Buffer's end channel should send only value \"-1\"!");
                }
            }
        }
        buffersReady[id].out().write(-1); // signal end work
        System.out.println("\u001B[35m" + "Buffer " + id + " ended.");
    }
}
