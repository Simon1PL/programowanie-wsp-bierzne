package lab6;

public class Consumer implements Runnable {
    private static int consumerAmount = 1;
    private final int id;
    private final Monitor monitor;
    private final Buffer buffer;

    public Consumer(Monitor monitor, Buffer buffer) {
        this.monitor = monitor;
        this.buffer = buffer;
        this.id = consumerAmount++;
    }

    @Override
    public void run() {
        while (true) {
            int current = this.monitor.start_consumption();
            buffer.takeItem(current);
            this.monitor.end_consumption(current, this.id);
        }
    }
}
