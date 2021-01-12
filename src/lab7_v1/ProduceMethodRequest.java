package lab7_v1;

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
        return true;
    }

    public void execute() {
        buffer.addItem(producerId, amount);
        future.setResult(amount);
    }
}
