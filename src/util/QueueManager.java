package util;

import model.Service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

public class QueueManager {

    private final Queue<Service> serviceQueue;

    public QueueManager() {
        serviceQueue = new LinkedList<>();
    }

    /**
     * Mengganti isi antrean dengan data terbaru.
     */
    public void load(Collection<Service> services) {
        serviceQueue.clear();

        if (services == null) {
            return;
        }

        for (Service service : services) {
            enqueue(service);
        }
    }

    /**
     * Menambahkan service ke antrean.
     */
    public void enqueue(Service service) {
        if (service != null) {
            serviceQueue.offer(service);
        }
    }

    /**
     * Mengambil dan menghapus service pertama dari antrean.
     */
    public Service dequeue() {
        return serviceQueue.poll();
    }

    /**
     * Melihat service pertama tanpa menghapusnya.
     */
    public Service peek() {
        return serviceQueue.peek();
    }

    /**
     * Mengecek apakah antrean kosong.
     */
    public boolean isEmpty() {
        return serviceQueue.isEmpty();
    }

    /**
     * Mengembalikan jumlah antrean.
     */
    public int size() {
        return serviceQueue.size();
    }

    /**
     * Menghapus seluruh antrean.
     */
    public void clear() {
        serviceQueue.clear();
    }

    /**
     * Mengembalikan isi antrean.
     */
    public Queue<Service> getQueue() {
        return new LinkedList<>(serviceQueue);
    }

}
