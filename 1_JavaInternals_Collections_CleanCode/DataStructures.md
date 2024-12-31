
# **Data Structures in Java**

This document provides a structured guide to implementing common data structures in Java, focusing on core concepts, Java utilities, and custom implementations.

---

## **1. Linked List**

### **a) Singly Linked List**
- A linear data structure where each element (node) contains:
  - **Data**: The value stored in the node.
  - **Next**: A pointer to the next node in the list.

#### **Implementation**
```java
class Node {
    int data;
    Node next;

    Node(int data) {
        this.data = data;
        this.next = null;
    }
}

class SinglyLinkedList {
    private Node head;

    public void add(int data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    public void printList() {
        Node current = head;
        while (current != null) {
            System.out.print(current.data + " -> ");
            current = current.next;
        }
        System.out.println("null");
    }
}
```

### **b) Doubly Linked List**
- Each node has:
  - **Data**: The value stored.
  - **Next**: A pointer to the next node.
  - **Prev**: A pointer to the previous node.

#### **Implementation**
```java
class DoublyNode {
    int data;
    DoublyNode next, prev;

    DoublyNode(int data) {
        this.data = data;
        this.next = this.prev = null;
    }
}

class DoublyLinkedList {
    private DoublyNode head;

    public void add(int data) {
        DoublyNode newNode = new DoublyNode(data);
        if (head == null) {
            head = newNode;
        } else {
            DoublyNode current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
            newNode.prev = current;
        }
    }

    public void printList() {
        DoublyNode current = head;
        while (current != null) {
            System.out.print(current.data + " <-> ");
            current = current.next;
        }
        System.out.println("null");
    }
}
```

---

## **2. Stack (Stiva)**

- **LIFO (Last In, First Out)** structure.

#### **Using Java’s `Stack` Class**
```java
import java.util.Stack;

class StackExample {
    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<>();
        stack.push(10);
        stack.push(20);
        stack.push(30);
        
        System.out.println(stack.pop()); // 30
        System.out.println(stack.peek()); // 20
    }
}
```

---

## **3. Queue (Coada)**

- **FIFO (First In, First Out)** structure.

#### **Using Java’s `Queue` Interface**
```java
import java.util.LinkedList;
import java.util.Queue;

class QueueExample {
    public static void main(String[] args) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(10);
        queue.add(20);
        queue.add(30);
        
        System.out.println(queue.poll()); // 10
        System.out.println(queue.peek()); // 20
    }
}
```

---

## **4. Heap**

- A complete binary tree where each parent node satisfies the **heap property**:
  - **Min-Heap**: Parent ≤ Children.
  - **Max-Heap**: Parent ≥ Children.

#### **Using PriorityQueue for Min-Heap**
```java
import java.util.PriorityQueue;

class HeapExample {
    public static void main(String[] args) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        minHeap.add(30);
        minHeap.add(10);
        minHeap.add(20);

        System.out.println(minHeap.poll()); // 10
    }
}
```

#### **Custom Max-Heap**
```java
import java.util.PriorityQueue;

class MaxHeapExample {
    public static void main(String[] args) {
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
        maxHeap.add(10);
        maxHeap.add(30);
        maxHeap.add(20);

        System.out.println(maxHeap.poll()); // 30
    }
}
```

---

## **5. Binary Search Tree (BST)**

- Each node has:
  - Left child ≤ Parent.
  - Right child ≥ Parent.

#### **Implementation**
```java
class TreeNode {
    int data;
    TreeNode left, right;

    TreeNode(int data) {
        this.data = data;
        this.left = this.right = null;
    }
}

class BinarySearchTree {
    private TreeNode root;

    public void insert(int data) {
        root = insertRec(root, data);
    }

    private TreeNode insertRec(TreeNode root, int data) {
        if (root == null) return new TreeNode(data);
        if (data < root.data) root.left = insertRec(root.left, data);
        else if (data > root.data) root.right = insertRec(root.right, data);
        return root;
    }

    public void inOrderTraversal() {
        inOrderRec(root);
    }

    private void inOrderRec(TreeNode root) {
        if (root != null) {
            inOrderRec(root.left);
            System.out.print(root.data + " ");
            inOrderRec(root.right);
        }
    }
}
```

---

## **6. Graphs**

- Represented using:
  1. **Adjacency Matrix**.
  2. **Adjacency List**.

#### **Adjacency List**
```java
import java.util.*;

class Graph {
    private Map<Integer, List<Integer>> adjList = new HashMap<>();

    public void addEdge(int u, int v) {
        adjList.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
        adjList.computeIfAbsent(v, k -> new ArrayList<>()).add(u); // For undirected graph
    }

    public void printGraph() {
        for (var entry : adjList.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}
```

---

## **7. Circular Linked List**

- A linked list where the last node points back to the first.

#### **Implementation**
```java
class CircularNode {
    int data;
    CircularNode next;

    CircularNode(int data) {
        this.data = data;
        this.next = null;
    }
}

class CircularLinkedList {
    private CircularNode head;

    public void add(int data) {
        CircularNode newNode = new CircularNode(data);
        if (head == null) {
            head = newNode;
            head.next = head;
        } else {
            CircularNode current = head;
            while (current.next != head) {
                current = current.next;
            }
            current.next = newNode;
            newNode.next = head;
        }
    }

    public void printList() {
        if (head == null) return;
        CircularNode current = head;
        do {
            System.out.print(current.data + " -> ");
            current = current.next;
        } while (current != head);
        System.out.println("(back to head)");
    }
}
```

---

## **8. HashMap**

- Stores key-value pairs with **O(1)** average-time complexity for get/put.

#### **Usage**
```java
import java.util.HashMap;

class HashMapExample {
    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        System.out.println(map.get("A")); // 1
    }
}
```
