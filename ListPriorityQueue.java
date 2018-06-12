
/** 
 * Author: Connor Melton
 * Account: masc0382
 */

import java.util.Iterator;

public class ListPriorityQueue<E> implements PriorityQueue<E>{
    private int maxSize;
    private int currentSize;
    private Node tail;
    
    public ListPriorityQueue(){
        maxSize = DEFAULT_MAX_CAPACITY;
        tail = new Node();
        tail.next = tail;
    }
    
    public ListPriorityQueue(int capacity){
        maxSize = capacity;
        tail = new Node();
        tail.next = tail;
    }

    public boolean insert(E object){
        if(currentSize == maxSize)
            return false;
            
        Node current = tail.next;
        Node previous = tail;
        while(current.data != null &&
             ((Comparable)current.data).compareTo(object) <= 0) {
            current = current.next;
            previous = previous.next;
        }
        
        Node newNode = new Node(object);
        newNode.next = current;
        previous.next = newNode;
        currentSize++;
        return true;
    }
    
    public E remove(){
        if(currentSize == 0)
            return null;
        
        E data = (E)tail.next.data;
        tail.next = tail.next.next;
        currentSize--;
        return data;
    }
    
    public E peek(){
        if(currentSize == 0)
            return null;
        
        return (E)tail.next.data;
    }
    
    public int size(){
        return currentSize;
    }
    
    public boolean contains(E object){
        Node current = tail.next;
        while(current.data != null && ((Comparable)current.data).compareTo(object) != 0)
            current = current.next;
        
        if(current == tail)
            return false;
    
        return true;
    }
    
    public Iterator<E> iterator(){
        return new MyIterator();
    }
    
    public void clear(){
        currentSize = 0;
        tail.next = tail;
    }
    
    public boolean isEmpty(){
        return currentSize == 0;
    }
    
    public boolean isFull(){
        return currentSize == DEFAULT_MAX_CAPACITY;
    }
    
    private class Node<E> {     
        E data;
        Node next;
        
        public Node() {
        }
        
        public Node(E obj) {
            data = obj;
        }
    }
    private class MyIterator implements Iterator{
        private Node front;
        private Node last;
        private Node removal;
        
        public MyIterator(){
            front = tail.next;
            last = null;
            removal = null;
        }
        public boolean hasNext(){
            if(isEmpty())
                return false;
            
            return front.next != tail;
        }
        public E next(){
            if(!hasNext())
                return null;
            
            E data = (E)front.data;
            removal = last;
            last = front;
            front = front.next;
            return data;
        }
        public void remove(){
            if(removal == null && last != null){
                tail.next = front;
                last = null;
                currentSize--;
            }
            else if(removal != null && last != null && removal.next != front){
                removal.next = front;
                last = removal;
                removal = null;
                currentSize--;
            }            
            else
                System.out.println(
                "Error: remove called twice before next, or remove called on emptyList");
        }
    }
}
