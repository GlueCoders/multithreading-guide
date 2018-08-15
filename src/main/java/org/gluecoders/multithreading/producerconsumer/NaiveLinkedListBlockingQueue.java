package org.gluecoders.multithreading.producerconsumer;

public class NaiveLinkedListBlockingQueue<T> extends NaiveBlockingQueue<T> {

    private static final class Node<T> {
        private T t;
        private Node<T> next;

        public Node(T t) {
            this.t = t;
        }
    }

    private Node<T> head;
    private Node<T> tail;

    public NaiveLinkedListBlockingQueue(int capacity) {
        super(capacity);
    }

    public boolean enqueue(T t) {
        if (t == null || size == capacity) {
            return false;
        }
        if (tail == null) {
            tail = new Node<>(t);
            head = tail;
        } else {
            tail.next = new Node<>(t);
            tail = tail.next;
        }
        size++;
        return true;
    }

    public T dequeue() {
        if (size == 0 || head == null) {
            return null;
        }
        Node<T> node = head;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        size--;
        return node.t;
    }

    /**
     * Retrieves, but does not remove, the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    @Override
    public T peek() {
        return head != null ? head.t : null;
    }

}