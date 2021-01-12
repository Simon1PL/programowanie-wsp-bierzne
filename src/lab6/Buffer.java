package lab6;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.lang.Thread.sleep;

public class Buffer {
//    private final Map<Integer, Integer> buffer = new HashMap<>();
    private final ConcurrentMap<Integer, Integer> buffer = new ConcurrentHashMap<>();

    public void addItem(int current) {
        try {
            sleep(new Random().nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (buffer.containsKey(current) && buffer.get(current) != -1)
            System.out.println("\u001B[32m" + "Produced " + buffer.get(current));
        // nieudana próba pokazania błędu przy użyciu zwykłej mapy, wątek w javie ma własną pamięć, wątek A zmienia wartość w HashMapie, ale tej we własnej pamięci i zwalnia locka, po czym wchodzi kolejny wątek B, który znów zmienia wartość w HashMapie, zanim wątek A zsynchronizuję HashMape ze swojej pamięci z HashMapą z głównego wątku, co oznacza, że wątek B dostanie starą wartość z HashMapy
        this.buffer.put(current, 1);
    }

    public void takeItem(int current) {
        try {
            sleep(new Random().nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (buffer.get(current) != 1)
            System.out.println("\u001B[32m" + "Consumed " + buffer.get(current));
        this.buffer.put(current, -1);
        this.buffer.remove(current);
    }

}
