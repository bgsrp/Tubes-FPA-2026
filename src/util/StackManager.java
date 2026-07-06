package util;

import model.Service;

import java.util.Stack;

public class StackManager {

    private final Stack<Service> serviceStack;

    public StackManager() {
        serviceStack = new Stack<>();
    }

    /**
     * Menambahkan data service ke dalam riwayat.
     */
    public void push(Service service) {
        if (service != null) {
            serviceStack.push(service);
        }
    }

    /**
     * Mengambil sekaligus menghapus data service
     * yang terakhir dimasukkan.
     */
    public Service pop() {

        if (serviceStack.isEmpty()) {
            return null;
        }

        return serviceStack.pop();
    }

    /**
     * Melihat data service terakhir
     * tanpa menghapusnya.
     */
    public Service peek() {

        if (serviceStack.isEmpty()) {
            return null;
        }

        return serviceStack.peek();
    }

    /**
     * Mengecek apakah stack kosong.
     */
    public boolean isEmpty() {
        return serviceStack.isEmpty();
    }

    /**
     * Mengembalikan jumlah data dalam stack.
     */
    public int size() {
        return serviceStack.size();
    }

    /**
     * Menghapus seluruh riwayat.
     */
    public void clear() {
        serviceStack.clear();
    }

    /**
     * Mengembalikan salinan isi stack.
     */
    public Stack<Service> getStack() {
        Stack<Service> copy = new Stack<>();
        copy.addAll(serviceStack);
        return copy;
    }

}