package lab6;

import static java.lang.Thread.sleep;

public class Producer implements Runnable {
    private final Monitor monitor;
    private final Buffer buffer;
    private static int producerAmount = 1;
    private final int id;

    public Producer(Monitor monitor, Buffer buffer) {
        this.id = producerAmount++;
        this.monitor = monitor;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            //long start=System.currentTimeMillis();
            int current = this.monitor.start_production();
            buffer.addItem(current);
            this.monitor.end_production(current, this.id);
            //long stop=System.currentTimeMillis();
            //System.out.println("Czas wykonania:"+(stop-start));
        }
    }
}
