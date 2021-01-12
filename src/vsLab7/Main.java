package vsLab7;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Thread.sleep;
import static lab7_AO.Main.*;

public class Main {
    public static void main(String[] args) {
        try {
            File file = new File("results.txt");
            file.createNewFile();
            fileWriter = new FileWriter(file, true);
            saveParametersToFile();

            run(1);

            /*fileWriter.write("SYNC " + "ADDITIONAL_WORK_TIME_PER_ITEM " + "daneTasks" + "\n");
            for (TAKING_ONE_ITEM_TIME = 10; TAKING_ONE_ITEM_TIME < 200; TAKING_ONE_ITEM_TIME += 20) {
                ADDING_ONE_ITEM_TIME = TAKING_ONE_ITEM_TIME;
                run(TAKING_ONE_ITEM_TIME);
            }
            TAKING_ONE_ITEM_TIME = 40;
            ADDING_ONE_ITEM_TIME = 40;*/

            /*fileWriter.write("SYNC " + "PRODUCER_AMOUNT/CONSUMER_AMOUNT " + "daneTasks" + "\n");
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

            /*fileWriter.write("SYNC " + "ADDITIONAL_WORK_TIME_PER_ITEM " + "daneTasks" + "\n");
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
        List<Thread> threads = new LinkedList<>();

        Buffer buffer = new Buffer();
        Buffer.takenItemsAmount = 0;
        Buffer.addedItemsAmount = 0;

        Consumer[] consumers = new Consumer[CONSUMER_AMOUNT];
        for(int i = 0; i < consumers.length; i++) {
            consumers[i] = new Consumer(buffer);
            Thread thread = new Thread(consumers[i]);
            threads.add(thread);
            thread.start();
        }

        Producer[] producers = new Producer[PRODUCER_AMOUNT];
        for(int i = 0; i < producers.length; i++) {
            producers[i] = new Producer(buffer);
            Thread thread = new Thread(producers[i]);
            threads.add(thread);
            thread.start();
        }

        try {
            sleep(PROGRAM_RUNNING_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int sum = Buffer.takenItemsAmount + Buffer.addedItemsAmount;
        fileWriter.write("\t" + parameter + "\t\t" + sum + "\n");

        threads.forEach(Thread::interrupt);
    }

}
