package org.gluecoders.multithreading.producerconsumer;

public class NaiveArrayBlockingQueue<T> extends NaiveBlockingQueue<T> {

    private final Object[] backingData;
    private int head;
    private int tail;


    public NaiveArrayBlockingQueue(int capacity) {
        super(capacity);
        this.backingData = new Object[capacity];
    }

    public boolean enqueue(T t) {
        if (t != null && size < capacity) {
            backingData[tail++] = t;
            tail %= capacity;
            size++;
            return true;
        }
        return false;
    }

    public T dequeue() {
        if (size == 0) {
            return null;
        }
        T t = (T) backingData[head];
        backingData[head++] = null;
        head %= capacity;
        size--;
        return t;
    }

    /**
     * Retrieves, but does not remove, the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    @Override
    public T peek() {
        return (T) backingData[head];
    }
}
