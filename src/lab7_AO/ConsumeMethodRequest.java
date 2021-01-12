package lab7_AO;

public class ConsumeMethodRequest implements Request {
    private final Future future;
    private final Buffer buffer;
    private final int amount;
    private final int consumerID;

    public ConsumeMethodRequest(Future future, Buffer buffer, int amount, int consumerId) {
        this.amount = amount;
        this.buffer = buffer;
        this.future = future;
        this.consumerID = consumerId;
    }

    public boolean guard() {
        return buffer.getSize() >= amount;
    }

    public void execute() throws InterruptedException {
        buffer.takeItem(consumerID, amount);
        future.setResult(amount);
    }
}
