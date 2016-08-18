package org.drools.core.management;

import java.util.Iterator;


public class SynchronizedFastTakeAllList<E> {
    
    private static class Entry<E> {
        E item;
        Entry<E> next;
        public Entry(E item) {
            this.item = item;
        }
    }

    protected volatile Entry<E> head;
    protected volatile Entry<E> tail;
    protected int size;
    
    public synchronized void add( E item ) {
        addEntry(new Entry<E>(item));
    }

    protected synchronized void addEntry( Entry<E> entry ) {
        if ( head == null ) {
            head = entry;
        } else {
            tail.next = entry ;
        }
        tail = entry;
        ++size;
    }

    public synchronized Iterator<E> takeAll() {
        Entry<E> currentHead = head;
        reset();
        return new EntryIterator<E>(currentHead);
    }
    
    public synchronized void reset() {
        head = null;
        tail = null;
        size = 0;
    }
    
    public int size() {
        return this.size;
    }

    public synchronized boolean isEmpty() {
        return this.size == 0;
    }
    
    public synchronized Iterator<E> iterator() {
        return new EntryIterator<E>(head);
    }

    public static class EntryIterator<E> implements Iterator<E> {

        private Entry<E> next;

        public EntryIterator(Entry<E> head) {
            this.next = head;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public E next() {
            Entry<E> current = next;
            next = current.next;
            return current.item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
