package lab7_AO;

import static lab7_AO.Main.MAX_PRODUCT_AMOUNT_ON_ONE_OPERATION;

public class ProduceMethodRequest implements Request {
    private final Future future;
    private final Buffer buffer;
    private final int amount;
    private final int producerId;

    public ProduceMethodRequest(Future future, Buffer buffer, int amount, int producerId) {
        this.amount = amount;
        this.buffer = buffer;
        this.future = future;
        this.producerId = producerId;
    }

    public boolean guard() {
        return buffer.getSize() + amount <= 2 * MAX_PRODUCT_AMOUNT_ON_ONE_OPERATION;
    }

    public void execute() throws InterruptedException {
        buffer.addItem(producerId, amount);
        future.setResult(amount);
    }
}
