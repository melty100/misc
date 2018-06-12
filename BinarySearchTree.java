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

public class BinarySearchTree<K,V> implements DictionaryADT<K,V>
{
    int currentSize;
    long modCounter;
    private TreeNode root;

    public BinarySearchTree(){
        currentSize = 0;
        modCounter = 0;
        root = null;
    }
    
    // Returns true if the dictionary has an object identified by
    // key in it, otherwise false.
    public boolean contains(K key){           
        return (findNode(key, root) != null);
    }
    
    // Adds the given key/value pair to the dictionary.  Returns
    // false if the dictionary is full, or if the key is a duplicate.
    // Returns true if addition succeeded.
    public boolean insert(K key, V value){
        System.out.println(currentSize);
        if(root == null)
            root = new TreeNode(key, value);
        else{
            if(contains(key)) return false;
            insertHelper(key, value, root, null, false);
        }
        
        modCounter++;
        currentSize++;
        return true;
    }
    
    private void insertHelper(K key, V value, TreeNode current, TreeNode parent, boolean leftChild){
        if(current == null){
            if(leftChild)
                parent.left = new TreeNode(key, value);
            if(!leftChild)
                parent.right = new TreeNode(key, value);
        }
        else if(((Comparable<K>)key).compareTo((K)current.key) > 0)
            insertHelper(key, value, current.right, current, false);
        else
            insertHelper(key, value, current.left, current, true);
    }
    
    // Deletes the key/value pair identified by the key parameter.
    // Returns true if the key/value pair was found and removed,
    // otherwise false.
    public boolean remove(K key){
        if(currentSize == 0)
            return false;
                
        return removeHelper(key, root, null, false);
    }
    
    private boolean removeHelper(K key, TreeNode n, TreeNode p, boolean rightChild){
        if(n == null)
            return false;
        if(((Comparable)key).compareTo(n.key) > 0)
            removeHelper(key, n.right, n, true);
        if(((Comparable)key).compareTo(n.key) < 0)
            removeHelper(key, n.left, n, false);
            
        if(n.left == null && n.right == null){
            if(rightChild)
                p.right = null;
            if(!rightChild)
                p.left = null;
        }        
        else if(n.right == null && n.left != null){
            n.data = n.left.data;
            n.left = null;
        }
        else{
            n.data = inorderFinder(n.right, n, true);
        }
        modCounter++;
        currentSize--;
        return true;
    }
    
    private V inorderFinder(TreeNode n, TreeNode p, boolean rightChild){
        if(n.left == null){
            V data = (V)n.data;
            if(n.right == null){
                if(rightChild)
                    p.right = null;
                if(!rightChild)
                    p.left = null;
            }
            if(n.right != null){
                if(rightChild)
                    p.right = n.right;
                if(!rightChild)
                    p.left = n.right;
            }
            return (V)data;
        }
        inorderFinder(n.left, n, false);        
        return null;
    }
    
    // Returns the value associated with the parameter key.  Returns
    // null if the key is not found or the dictionary is empty.
    public V getValue(K key){
        return (V)findNode(key, root).data;
    }

    private TreeNode findNode(K key, TreeNode node){
        if(node == null)
            return null;
        if(((Comparable)key).compareTo(node.key) > 0)
            findNode(key, node.right);
        if(((Comparable)key).compareTo(node.key) < 0)
            findNode(key, node.left);
        
        return node;
    }
    
    
    // Returns the key associated with the parameter value.  Returns
    // null if the value is not found in the dictionary.  If more
    // than one key exists that matches the given value, returns the
    // first one found.
    public K getKey(V value){
        return getKeyHelper(value, root);
    }
    
    private K getKeyHelper(V value, TreeNode n){
        if(n == null) 
            return null;
        if(n.left != null)
            getKeyHelper(value, n.left);
        if(((Comparable<V>)n.data).compareTo(value) == 0)
            return (K)n.key;
        if(n.right != null)
            getKeyHelper(value, n.right);
            
        return null;
    }
    // Returns the number of key/value pairs currently stored
    // in the dictionary
    public int size(){
        return currentSize;
    }
    
    // Returns true if the dictionary is at max capacity
    public boolean isFull(){
        return false;
    }
    
    // Returns true if the dictionary is empty
    public boolean isEmpty(){
        return currentSize == 0;
    }
    
    // Returns the Dictionary object to an empty state.
    public void clear(){
        root = null;
        currentSize = 0;
    }
    
    // Returns an Iterator of the keys in the dictionary, in ascending
    // sorted order.  The iterator must be fail-fast.
    public Iterator<K> keys(){
        return new MyKeysIterator();
    }
    
    // Returns an Iterator of the values in the dictionary.  The
    // order of the values must match the order of the keys.
    // The iterator must be fail-fast.
    public Iterator<V> values(){
        return new MyValueIterator();
    }
    
    private class TreeNode<K, V>{
        public V data;
        public K key;
        public TreeNode left;
        public TreeNode right;
        
        public TreeNode(K k, V v){
            key = k;
            data = v;
        }
    }
    
    private class MyKeysIterator implements Iterator{        
        private long currentMod;
        private int currentIndex;
        private TreeNode<K,V>[] localArray;
        
        public MyKeysIterator(){
            currentIndex = 0;
            currentMod = modCounter;
            localArray = new TreeNode[currentSize];
            fillLocalArray(localArray, root);
        }
        
        public boolean hasNext(){
            if(currentMod != modCounter)
                throw new ConcurrentModificationException();
                
            return currentSize > currentIndex;
        }
        
        public K next(){
            if(currentMod != modCounter)
                throw new ConcurrentModificationException();
            if(!hasNext())
                throw new NoSuchElementException();
            
            return (K)localArray[currentIndex++].key;
        }
        
        public void remove(){
            throw new UnsupportedOperationException();
        }
    }
    
    private class MyValueIterator implements Iterator{        
        private long currentMod;
        private int currentIndex;
        private TreeNode<K,V>[] localArray;
        
        public MyValueIterator(){
            currentIndex = 0;
            currentMod = modCounter;
            localArray = new TreeNode[currentSize];
            fillLocalArray(localArray, root);
        }
        
        public boolean hasNext(){
            if(currentMod != modCounter)
                throw new ConcurrentModificationException();
                
            return currentSize > currentIndex;
        }
        public K next(){
            if(currentMod != modCounter)
                throw new ConcurrentModificationException();
            if(!hasNext())
                throw new NoSuchElementException();
            
            return (K)localArray[currentIndex++].data;
        }
        public void remove(){
            throw new UnsupportedOperationException();
        }
    }
    
    private void fillLocalArray(TreeNode<K,V> []ary, TreeNode n){
        if(n == null) return;
        if(n.left != null)
            fillLocalArray(ary, n.left);
            
        ary[ary.length - 1] = n;
        if(n.right != null)
            fillLocalArray(ary, n.right);
            
    }
}
