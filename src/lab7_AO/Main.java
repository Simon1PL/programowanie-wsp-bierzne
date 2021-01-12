package lab7_AO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Main {
    public static int TAKING_ONE_ITEM_TIME = 100; // milliseconds
    public static int ADDING_ONE_ITEM_TIME = 100; // milliseconds
    public static int PRODUCER_AMOUNT = 15;
    public static int CONSUMER_AMOUNT = 15;
    public static int MAX_PRODUCT_AMOUNT_ON_ONE_OPERATION = 50;
    //public final static int ADDITIONAL_WORK_TIME = 5000; // milliseconds
    public static int ADDITIONAL_WORK_TIME_PER_ITEM = 200; // milliseconds
    public static int PROGRAM_RUNNING_TIME = 300_000; // milliseconds
    public static FileWriter fileWriter;

    public static void main(String[] args) {
        try {
            File file = new File("results.txt");
            file.createNewFile();
            fileWriter = new FileWriter(file, true);
            saveParametersToFile();

            run(0);

            /*fileWriter.write("ASYNC " + "ADDITIONAL_WORK_TIME_PER_ITEM " + "daneTasks" + "\n");
            for (TAKING_ONE_ITEM_TIME = 10; TAKING_ONE_ITEM_TIME < 200; TAKING_ONE_ITEM_TIME += 20) {
                ADDING_ONE_ITEM_TIME = TAKING_ONE_ITEM_TIME;
                run(TAKING_ONE_ITEM_TIME);
            }
            TAKING_ONE_ITEM_TIME = 40;
            ADDING_ONE_ITEM_TIME = 40;*/

            /*fileWriter.write("ASYNC " + "PRODUCER_AMOUNT/CONSUMER_AMOUNT " + "daneTasks" + "\n");
            for (PRODUCER_AMOUNT = 1; PRODUCER_AMOUNT < 10; PRODUCER_AMOUNT += 1) {
                CONSUMER_AMOUNT = PRODUCER_AMOUNT;
                run(PRODUCER_AMOUNT);
            }
            for (PRODUCER_AMOUNT = 10; PRODUCER_AMOUNT < 80; PRODUCER_AMOUNT += 15) {
                CONSUMER_AMOUNT = PRODUCER_AMOUNT;
                run(PRODUCER_AMOUNT);
            }
            PRODUCER_AMOUNT = 4;
            CONSUMER_AMOUNT = 4;*/

            /*fileWriter.write("ASYNC " + "ADDITIONAL_WORK_TIME_PER_ITEM " + "daneTasks" + "\n");
            for (ADDITIONAL_WORK_TIME_PER_ITEM = 10; ADDITIONAL_WORK_TIME_PER_ITEM < 2500; ADDITIONAL_WORK_TIME_PER_ITEM += 150) {
                run(ADDITIONAL_WORK_TIME_PER_ITEM);
            }
            ADDITIONAL_WORK_TIME_PER_ITEM = 300;*/

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void run(int parameter) throws IOException {
        Buffer buffer = new Buffer(TAKING_ONE_ITEM_TIME, ADDING_ONE_ITEM_TIME);
        Buffer.takenItemsAmount = 0;
        Buffer.addedItemsAmount = 0;
        QueueMonitor queueMonitor = new QueueMonitor();
        Scheduler scheduler = new Scheduler(queueMonitor);
        Proxy proxy = new Proxy(queueMonitor, buffer);

        List<Thread> threads = new LinkedList<>();

        Consumer[] consumers = new Consumer[CONSUMER_AMOUNT];
        for(int i = 0; i < consumers.length; i++) {
            consumers[i] = new Consumer(proxy);
            Thread thread = new Thread(consumers[i]);
            threads.add(thread);
            thread.start();
        }

        Producer[] producers = new Producer[PRODUCER_AMOUNT];
        for(int i = 0; i < producers.length; i++) {
            producers[i] = new Producer(proxy);
            Thread thread = new Thread(producers[i]);
            threads.add(thread);
            thread.start();
        }

        Thread thread = new Thread(scheduler);
        threads.add(thread);
        thread.start();

        try {
            sleep(PROGRAM_RUNNING_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int sum = Buffer.takenItemsAmount + Buffer.addedItemsAmount;
        fileWriter.write("\t" + parameter + "\t\t" + sum + "\n");

        threads.forEach(Thread::interrupt);
    }

    public static void saveParametersToFile() throws IOException {
        fileWriter.write("parameters:\n" +
                "TAKING_ONE_ITEM_TIME: " + TAKING_ONE_ITEM_TIME + "\n" +
                "ADDING_ONE_ITEM_TIME: " + ADDING_ONE_ITEM_TIME + "\n" +
                "PRODUCER_AMOUNT: " + PRODUCER_AMOUNT + "\n" +
                "CONSUMER_AMOUNT: " + CONSUMER_AMOUNT + "\n" +
                "MAX_PRODUCT_AMOUNT_ON_ONE_OPERATION: " + MAX_PRODUCT_AMOUNT_ON_ONE_OPERATION + "\n" +
                "ADDITIONAL_WORK_TIME_PER_ITEM: " + ADDITIONAL_WORK_TIME_PER_ITEM + "\n" +
                "PROGRAM_RUNNING_TIME: " + PROGRAM_RUNNING_TIME + "\n");
    }
}
