package data_structures;


/** 
 * Author: Connor Melton 
 * Account: masc0382
 * Class: CS310, Data Structures
 * Instructor: Riggins, Alan
 * Citations:
 * 1)Goodrich & Tamassia. Data Structures and Algorithms 5th Edition
 * 2)Recorded Lectures 13 - 18
 * 3)http://www.algolist.net/Algorithms/Sorting/Quicksort 
 */

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

public class Hashtable<K,V> implements DictionaryADT<K,V>
{
    private int currentSize;
    private int maxSize;
    private int tableSize;
    private long modCounter;
    private LinkedListDS<Node<K,V>> [] list;
    
    public Hashtable(int size){
        maxSize = size;
        currentSize = 0;
        tableSize = (int)(maxSize*1.3f);
        modCounter = 0;
        list = new LinkedListDS[tableSize];
        for(int x = 0; x < list.length; x++)
            list[x] = new LinkedListDS<Node<K,V>>();
        
    }
    
    // Returns true if the dictionary has an object identified by
    // key in it, otherwise false.
    public boolean contains(K key){
        int index = key.hashCode()%tableSize;
        Node K = new Node(key, null);
        return list[index].contains(K);
    }
    
    // Adds the given key/value pair to the dictionary.  Returns
    // false if the dictionary is full, or if the key is a duplicate.
    // Returns true if addition succeeded.
    public boolean insert(K key, V value){
        if(currentSize == maxSize || contains(key))
            return false;
            
        int index = key.hashCode()%tableSize;
        Node newNode = new Node(key, value);
        list[index].addFirst(newNode);
        currentSize++;
        modCounter++;
        return true;
    }
    
    // Deletes the key/value pair identified by the key parameter.
    // Returns true if the key/value pair was found and removed,
    // otherwise false.
    public boolean remove(K key){
        int index = key.hashCode()%tableSize;
        Node n = new Node(key, null);
        currentSize--;
        modCounter++;
        return list[index].remove(n);
    }
    
    // Returns the value associated with the parameter key.  Returns
    // null if the key is not found or the dictionary is empty.
    public V getValue(K key){
        int index = key.hashCode()%tableSize;
        Node n = new Node(key, null);
        n = list[index].find(n);
        if(n == null)
            return null;
            
        return (V)list[index].find(n).item;
    }
    
    // Returns the key associated with the parameter value.  Returns
    // null if the value is not found in the dictionary.  If more
    // than one key exists that matches the given value, returns the
    // first one found.
    public K getKey(V value){
        for(LinkedListDS<Node<K,V>> bin: list){
            for(Node n : bin){
                if(((Comparable)value).compareTo(n.item) == 0)
                    return (K)n.key;
            }
        }
        return null;
    }
    
    // Returns the number of key/value pairs currently stored
    // in the dictionary
    public int size(){
        return currentSize;
    }
    
    // Returns true if the dictionary is at max capacity
    public boolean isFull(){
        return currentSize == maxSize;
    }
    
    // Returns true if the dictionary is empty
    public boolean isEmpty(){
        return currentSize == 0;
    }
    
    // Returns the Dictionary object to an empty state.
    public void clear(){
        currentSize = 0;
        modCounter = 0;
        list = new LinkedListDS[tableSize];
        for(int x = 0; x < list.length; x++)
            list[x] = new LinkedListDS<Node<K,V>>();
        
    }
    
    // Returns an Iterator of the keys in the dictionary, in ascending
    // sorted order.  The iterator must be fail-fast.
    public Iterator<K> keys(){
        return new MyKeyIterator();
    }
    
    // Returns an Iterator of the values in the dictionary.  The
    // order of the values must match the order of the keys.
    // The iterator must be fail-fast.
    public Iterator<V> values(){
        return new MyItemIterator();
    }
    
    private class Node<K, V> implements Comparable<Node<K,V>>
    {   
        public K key;
        public V item;
        
        public Node(K k, V v){
            key = k;
            item = v;
        }
        public int compareTo(Node n){
            return ((Comparable<K>)this.key).compareTo((K)n.key);
        }         
    }
    
    public class MyKeyIterator implements Iterator{
        private long currentMod;
        private int currentIndex;
        private Node<K,V> [] localArray;
        
        public MyKeyIterator(){
            currentIndex = 0;
            currentMod = modCounter;
            localArray = new Node[currentSize];
            int counter = 0;
            for(LinkedListDS<Node<K,V>> i : list){
                for(Node n : i){
                    localArray[counter] = n;
                    counter++;
                }
            }
            if(localArray.length > 1)
                quickSort(localArray, 0, localArray.length - 1);
        }
        public boolean hasNext(){
            if(currentMod != modCounter)
                throw new ConcurrentModificationException();

            return currentIndex != localArray.length;
        }
        public K next(){
            if(currentMod != modCounter)
                throw new ConcurrentModificationException();
            if(!hasNext())
                throw new NoSuchElementException();
            
            currentIndex++;
            return (K)localArray[currentIndex - 1].key;
        }
        public void remove(){
            throw new UnsupportedOperationException();
        }
    }
    
    public class MyItemIterator implements Iterator{
        private long currentMod;
        private int currentIndex;
        private Node<K,V> [] localArray;
        
        public MyItemIterator(){
            currentIndex = 0;
            currentMod = modCounter;
            localArray = new Node[currentSize];
            int counter = 0;
            for(LinkedListDS<Node<K,V>> i : list){
                for(Node n : i){
                    localArray[counter] = n;
                    counter++;
                }
            }
            if(localArray.length > 1)
                quickSort(localArray, 0, localArray.length - 1);
        }
        public boolean hasNext(){
            if(currentMod != modCounter)
                throw new ConcurrentModificationException();

            return currentIndex != localArray.length;
        }
        public V next(){
            if(currentMod != modCounter)
                throw new ConcurrentModificationException();
            if(!hasNext())
                throw new NoSuchElementException();
            
            return (V)localArray[currentIndex++].item;
        }
        public void remove(){
            throw new UnsupportedOperationException();
        }
    }
    
    private int partition(Node[] arr, int left, int right){
        int i = left, j = right;
        Node tmp;
        Node pivot = arr[(left + right) / 2];
     
        while (i <= j) {
            while (((Comparable)arr[i].key).compareTo(pivot.key) < 0){
                  i++;
            }
            while (((Comparable)arr[j].key).compareTo(pivot.key) > 0){
                  j--;
            }
            if (i <= j) {
                  tmp = arr[i];
                  arr[i] = arr[j];
                  arr[j] = tmp;
                  i++;
                  j--;
            }
        }
      
      return i;
    }
 
    private void quickSort(Node[] arr, int left, int right) {
        int index = partition(arr, left, right);
        if (left < index - 1)
            quickSort(arr, left, index - 1);
        if (index < right)
            quickSort(arr, index, right);
    }
}
