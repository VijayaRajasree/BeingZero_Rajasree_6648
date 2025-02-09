//LRU Cache using Doubly LinkedList and Hashing
import java.util.*;
class LRUCache {
    private class Node {
        int key, value;
        Node prev, next;
        public Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }
    private final int capacity;  //maximum capacity of the cache
    private final Map<Integer, Node> cache; // hashmap to store key-node pairs
    private final Node head, tail; // temporary head and tail for easy list management
    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        // temporary head and tail nodes
        head = new Node(0, 0);
        tail = new Node(0, 0);
        head.next = tail;
        tail.prev = head;
    }
    // get the value of a key from the cache
    public int get(int key) {
        if (!cache.containsKey(key)) return -1;  // key not found
        Node node = cache.get(key); // retrieve the node
        moveToHead(node); // most recently used
        return node.value; 
    }
    // put a key-value pair into the cache
    public void put(int key, int value) {
        if (cache.containsKey(key)) {
            // if key exists, update the value and move to front
            Node node = cache.get(key);
            node.value = value;
            moveToHead(node);
        } else {
            // if key is new then check the cache is full or not
            if (cache.size() >= capacity) {
                removeLeastRecentlyUsed(); // remove the least recently used item
            }
            Node newNode = new Node(key, value); // create a new node
            cache.put(key, newNode); // add to hashmap
            addToHead(newNode); // move to the front
        }
    }
    // marking as most recently used
    private void moveToHead(Node node) {
        removeNode(node); // remove from its current position
        addToHead(node); // add it at the front
    }
    // add a node to front 
    private void addToHead(Node node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }
    // remove the node from DLL
    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
    // remove Least Recently used node
    private void removeLeastRecentlyUsed() {
        Node lru = tail.prev; // last node
        removeNode(lru); // remove from the list
        cache.remove(lru.key); // remove from hashmap
    }
}
public class Main {
    public static void main(String[] args) {
        LRUCache cache = new LRUCache(2); // Cache capacity 2
        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(cache.get(1)); // O/p: 1
        cache.put(3, 3); // Evicts key 2 (LRU)
        System.out.println(cache.get(2)); // O/p: -1 (not found)
        cache.put(4, 4); // Evicts key 1 (LRU)
        System.out.println(cache.get(1)); // O/p: -1 (not found)
        System.out.println(cache.get(3)); // O/p: 3
        System.out.println(cache.get(4)); // O/p: 4
    }
}
