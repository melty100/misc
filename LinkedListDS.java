package data_structures;

/**
 * Connor Melton
 * masc0382
 * CS 320, Alan Riggins
 */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListDS<E> implements ListADT<E> {

    private Node tail;
    private Node head;
    private int size;
    
    public LinkedListDS() {
        head = null;
        tail = null;
        size = 0;
    }

//  Adds the Object obj to the beginning of the list.
    public void addFirst(E obj) {
        Node newNode = new Node(obj);
        if(head == null) {
            head = newNode;
            head.next = tail;
        }
        else if(tail == null) {
            tail = head;
            tail.next = null;
            newNode.next = tail;
            head = newNode;
        }
        else {
            newNode.next = head;
            head = newNode;
        }
        size++;
    }

//  Adds the Object obj to the end of the list.
    public void addLast(E obj) {
        Node newNode = new Node(obj);
        if(head == null) {
            head = newNode;
            head.next = tail;
        }
        else if(tail == null) {
            tail = newNode;
            head.next = tail;
            tail.next = null;
        }
        else {
            tail.next = newNode;
            newNode.next = null;
            tail = newNode;
        }
        size++;        
    }

//  Removes the first Object in the list and returns it.
//  Returns null if the list is empty.
    public E removeFirst() { 
        if(head == null)
            return null;
        
        E data = (E)head.data;
        if(tail == null) {
            head = null;
        }
        else if(size == 2) {
            head = tail;
            tail = null;
        }
        else
            head = head.next;
            
        size--;    
        return data;
    }

//  Removes the last Object in the list and returns it.
//  Returns null if the list is empty.
    public E removeLast(){

        if(head == null)
           return null;
           
        E data;
        if(tail == null) {
            data = (E)head.data;
            head = null;
        }
        else if(size == 2) {
            data = (E)tail.data;
            tail = null;
        }
        else {
            Node current = head.next;
            Node previous = head;
            while(current.next != null) {
                current = current.next;
                previous = previous.next;
            }
        
            data = (E)current.data;
            previous.next = null;
            tail = previous;
        }
        size--;
        return data;
    }

//  Returns the first Object in the list, but does not remove it.
//  Returns null if the list is empty.
    public E peekFirst() {
        if(head == null)
            return null;
            
        return (E)head.data;
    }

//  Returns the last Object in the list, but does not remove it.
//  Returns null if the list is empty.
    public E peekLast() {
        if(head == null)
            return null;
        if(tail == null)
           return (E)head.data;
        
        return (E)tail.data;
    }
    
//  Finds and returns the Object obj if it is in the list, otherwise
//  returns null.  Does not modify the list in any way.
    public E find(E obj) { 
        Node current = head;
        while(current != null) {
           if(((Comparable)current.data).compareTo(obj) == 0)
               return (E)current.data;
           else
               current = current.next;
        }                              
        return null;
    }

//  Removes the first instance of thespecific Object obj from the list, if it exists.
//  Returns true if the Object obj was found and removed, otherwise false.
    public boolean remove(E obj) {  
        Node current = head;
        Node previous = head;
        while(current != null && ((Comparable)current.data).compareTo(obj) != 0) {
           if(current == previous)
               current = current.next;
           else {
               current = current.next;
               previous = previous.next;
            }
        }
        
        if(current != null && ((Comparable)current.data).compareTo(obj) == 0) {
            if(current == head)
                removeFirst();
            else if(current == tail)
                removeLast();
            else {
                previous.next = current.next;
                size--;
            }
            return true;
        }
        return false;
    }

//  The list is returned to an empty state.
    public void makeEmpty() {       
        size = 0;
        head = null;
        tail = null;
    }

//  Returns true if the list contains the Object obj, otherwise false.
    public boolean contains(E obj) {
        Node current = head;
        while(current != null) {
           if(((Comparable)current.data).compareTo(obj) == 0)
               return true;
           else
               current = current.next;
        }                              
        return false;
    }

//  Returns true if the list is empty, otherwise false.
    public boolean isEmpty() {        
        return (size == 0);
    }

//  Returns true if the list is full, otherwise false.
    public boolean isFull() {       
        return false;
    }

//  Returns the number of Objects currently in the list.
    public int size() {
        return size;
    }

//  Returns an Iterator of the values in the list, presented in
//  the same order as the list.
    public Iterator<E> iterator() {
        Iterator i = new MyIterator();
        return i;
    }
    
//  A container that holds an element and a reference to the next element
//  in LinkedListADT.   
    private class Node<E> {
        Node next;
        E data;
        
        public Node(E d) {
           data = d;
        }
    }

    public class MyIterator implements Iterator { 
        Node front;
        Node last;
        Node remove;
        
        public MyIterator() {
            front = head;
            last = null;
            remove = null;
        }
        
//      Returns true if there is a next element, false otherwise.
        public boolean hasNext() {
            if(front == null)
                return false;
            return true;
        }
        
//      Returns the next element. Throws NoSuchElementException if their isnt a next element.
        public E next() throws NoSuchElementException {
            if(!hasNext())
                throw new NoSuchElementException();
             
            if(last != null && remove != null) {
                last = last.next;
                remove = remove.next;
            }
            else if(last != null && remove == null) {
                remove = last;
                last = last.next;
            }
            else if(last == null)
                last = front;
                            
            front = front.next;
            return (E)last.data;            
        }
        
//      Removes the last element returned by next(). Throws an excpetion if remove() is
//      called before next(), or if remove() is called twice in a row.
        public void remove() throws IllegalStateException {
            if(last == null)
                throw new IllegalStateException();
                
            if(remove == null) {
                last = null;
                removeFirst();
            }
            else {
                remove.next = front;
                last = null;
                size--;
            }
        }
    }        
}
