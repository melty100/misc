
/**
 * Author: Connor Melton
 * Account: masc0382
 */

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

public class BinaryHeapPriorityQueue<E> implements PriorityQueue<E>
{
    private Node[] pQueue;
    private int sequence;
    private int currentSize;
    private int maxSize;
    private int modificationCounter = 0;
    
    public BinaryHeapPriorityQueue(){
        modificationCounter = currentSize = sequence = 0;
        maxSize = DEFAULT_MAX_CAPACITY;
        pQueue = new Node[DEFAULT_MAX_CAPACITY];
    }
    
    public BinaryHeapPriorityQueue(int capacity){
        modificationCounter = currentSize = sequence = 0;
        maxSize = capacity;
        pQueue = new Node[capacity];
    }
    
    public boolean insert(E object){
        if(currentSize == maxSize)
            return false;
        
        int currentIndex = currentSize;
        int parentIndex = (currentIndex - 1)/2;
        Node key = new Node(object, sequence);
        pQueue[currentIndex] = key;
        if(currentSize != 0) {
            Node parent = pQueue[parentIndex];
            while((currentIndex != parentIndex) && key.compareTo(parent) < 0) {
                pQueue[parentIndex] = key;
                pQueue[currentIndex] = parent;
                currentIndex = parentIndex;
                parentIndex = (parentIndex - 1)/2;
                parent = pQueue[parentIndex];
            }
        }
        modificationCounter++;
        currentSize++;
        sequence++;
            
        return true;
    }
    
    public E remove(){
        if(currentSize == 0)
            return null;
        
        E ele = (E)(pQueue[0]).data;
        pQueue[0] = pQueue[currentSize - 1];
        modificationCounter++;
        currentSize--;
        int parentIndex = 0;
        int leftIndex = 1;
        int rightIndex = 2;
        int tmp = 0;
        Node parent = pQueue[parentIndex];
        Node leftChild = pQueue[leftIndex];
        Node rightChild = pQueue[rightIndex];
        while(rightIndex < currentSize && ((parent.compareTo(leftChild) > 0) ||
             (parent.compareTo(rightChild) > 0))) {
                  
            if(leftIndex < currentSize && rightIndex < currentSize)
                tmp = leftChild.compareTo(rightChild);
                
                //System.out.println(tmp);
            if(rightIndex > currentSize || tmp < 0){
                pQueue[parentIndex] = leftChild;
                pQueue[leftIndex] = parent;
                parentIndex = leftIndex;
            }            
            else if(tmp > 0) {
                pQueue[parentIndex] = rightChild;
                pQueue[rightIndex] = parent;
                parentIndex = rightIndex;
            }
            
            leftIndex = parentIndex*2 + 1;
            rightIndex = leftIndex + 1;
            if(rightIndex < maxSize) {
                leftChild = pQueue[leftIndex];
                rightChild = pQueue[rightIndex];
                parent = pQueue[parentIndex];
            }
            
        }
        
        
        return ele;
    }
    
    public E peek(){
        if(currentSize == 0) {
            return null;
        }
            
        return (E)pQueue[0].data;
    }
    
    public int size(){
        return currentSize;
    }
    
    public boolean contains(E object){
        int counter = 0;
        E ele = (E)pQueue[0].data;
        while(counter < pQueue.length && ((Comparable)ele).compareTo(object) != 0){
            counter++;
            ele = (E)pQueue[counter].data;
        }
        
        if(counter == pQueue.length){
            return false;
        }
        return true;
    }
    
    public Iterator<E> iterator(){
        return null;
    }
    
    public void clear(){
        currentSize = 0;
        sequence = 0;
    }
    
    public boolean isEmpty(){
        return currentSize == 0;
    }
    
    public boolean isFull(){
        return currentSize == maxSize;
    }
    
    private class Node<E>{
       public E data;
       public long seqNumber;
        
        public Node(){
            data = null;
            seqNumber = 0;
        }
        public Node(E obj, long sqNumber){
            data = obj;
            seqNumber = sqNumber;
        }
        public int compareTo(Node obj){
            int temp = ((Comparable)this.data).compareTo((E)obj.data);
            if(temp == 0)
                return (int)(this.seqNumber - obj.seqNumber);
                
            return temp;
        }
    }
    
    private class MyIterator implements Iterator<E> {
        private Node [] localArray;
        private int modVal;
        private int iterIndex;
        
        public MyIterator(){
            modVal = modificationCounter;
            localArray = new Node[maxSize];
            iterIndex = 0;
            for(int x = 0; x < pQueue.length; x++) 
                localArray[x] = pQueue[x];
        }
        public boolean hasNext() throws ConcurrentModificationException{
            if(modVal != modificationCounter)
                throw new ConcurrentModificationException();
             
            return iterIndex < currentSize;
        }
        public E next() throws ConcurrentModificationException, NoSuchElementException{
            if(modVal != modificationCounter)
                throw new ConcurrentModificationException();
            
            if(!hasNext())
                throw new NoSuchElementException();
                
            iterIndex++;    
            return (E)localArray[iterIndex - 1].data;
        }
        public void remove(){
        }
    }
}
