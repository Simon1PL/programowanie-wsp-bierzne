package lab7_v1;

public class Proxy {
    private QueueMonitor queueMonitor;
    private Buffer buffer;

    Proxy(QueueMonitor queueMonitor, Buffer buffer) {
        this.queueMonitor = queueMonitor;
        this.buffer = buffer;
    }

    public Future takeItem(int consumerId, int amount) {
        Future future = new Future();
        ConsumeMethodRequest consumeMethodRequest = new ConsumeMethodRequest(future, buffer, amount, consumerId);
        queueMonitor.addToQueue(consumeMethodRequest);
        return future;
    }

    public Future addItem(int producerId, int amount) {
        Future future = new Future();
        ProduceMethodRequest produceMethodRequest = new ProduceMethodRequest(future, buffer, amount, producerId);
        queueMonitor.addToQueue(produceMethodRequest);
        return future;
    }

}
