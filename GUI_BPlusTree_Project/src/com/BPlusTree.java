package com;

import java.util.*;

public class BPlusTree<Key extends Comparable<? super Key>, Value> {

    int Psplits = 0;
    int Csplits = 0;
    int Pfusions = 0;
    int Cfusions = 0;
    int TotalNodes = 0;
    int height=0;
	
    public enum RangePolicy {
        EXCLUSIVE, INCLUSIVE
    }

    private static final int DEFAULT_BRANCHING_FACTOR = 128;
    private int branchingFactor;
    private Node root;

    public BPlusTree() {
        this(DEFAULT_BRANCHING_FACTOR);
    }

    public BPlusTree(int branchingFactor) {
        if (branchingFactor <= 2)
        	
            throw new IllegalArgumentException("Illegal branching factor: " + branchingFactor);
        this.branchingFactor = branchingFactor;
        root = new LeafNode();
    }

    Value search(Key key) {
        return root.getValue(key);
    }

    public List<Value> searchRange(Key key1, RangePolicy policy1, Key key2,RangePolicy policy2) {
        return root.getRange(key1, policy1, key2, policy2);
    }
  
    public List<Key> searchRange(Key key1, RangePolicy policy1) {
        return root.getNext10Key(key1, policy1);
    }

    void insert(Key key, Value value) {
        root.insertValue(key, value);
    }

    public void delete(Key key) {
        root.deleteValue(key);
    }

    public Map<Key, Value> getData() {
        return root.getData(root.getFirstLeafKey(), RangePolicy.INCLUSIVE);
    }

    public Key getFirstLeafKey() {
        return root.getFirstLeafKey();
    }

    public String toString() {
        Queue<List<Node>> queue = new LinkedList<List<Node>>();
        queue.add(Collections.singletonList(root));
        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            Queue<List<Node>> nextQueue = new LinkedList<List<Node>>();
            while (!queue.isEmpty()) {
                List<Node> nodes = queue.remove();
                sb.append('{');
                Iterator<Node> it = nodes.iterator();
                while (it.hasNext()) {
                    Node node = it.next();
                    sb.append(node.toString());
                    if (it.hasNext())
                        sb.append(", ");
                    if (node instanceof BPlusTree.InternalNode)
                        nextQueue.add(((InternalNode) node).children);
                }
                sb.append('}');
                if (!queue.isEmpty())
                    sb.append(", ");
                else
                    sb.append('\n');
            }
            queue = nextQueue;
        }

        return sb.toString();
    }

    private abstract class Node {
        List<Key> keys;

        int splits=0;
        int fusions=0;
        int depth=0;

        int keyNumber() {
            return keys.size();
        }

        abstract Value getValue(Key key);
        abstract void deleteValue(Key key);
        abstract void insertValue(Key key, Value value);
        abstract Key getFirstLeafKey();
        abstract List<Value> getRange(Key key1, RangePolicy policy1, Key key2,RangePolicy policy2);
        abstract List<Key> getNext10Key(Key key1, RangePolicy policy);
        abstract Map<Key, Value> getData(Key key1, RangePolicy policy);
        abstract void merge(Node sibling);
        abstract Node split();
        abstract boolean isOverflow();
        abstract boolean isUnderflow();
        public String toString() 
        {
            return keys.toString();
        }     
    }

    private class InternalNode extends Node {
        List<Node> children;

        InternalNode() {
            this.keys = new ArrayList<Key>();
            this.children = new ArrayList<Node>();
        }
        
        @Override
        //Retrieves the value of the key
        Value getValue(Key key) {
            return getChild(key).getValue(key);
        }

        @Override
        //Deletes a internal node with the given key if found
        void deleteValue(Key key) {
            Node child = getChild(key);
            child.deleteValue(key);
            if (child.isUnderflow()) {
                Node childLeftSibling = getChildLeftSibling(key);
                Node childRightSibling = getChildRightSibling(key);
                Node left = childLeftSibling != null ? childLeftSibling : child;
                Node right = childLeftSibling != null ? child : childRightSibling;
                left.merge(right);
                deleteChild(right.getFirstLeafKey());
                fusions++;
                if(height>0)
                {
                	height=height-1;
                }
                if (left.isOverflow()) {
                    Node sibling = left.split();
                    insertChild(sibling.getFirstLeafKey(), sibling);
                }
                
                if (root.keyNumber() == 0)
                    root = left;
            }
        }

        @Override
        //Inserts a new node with the given key and value
        void insertValue(Key key, Value value) {
            Node child = getChild(key);
            child.insertValue(key, value);
            if (child.isOverflow()) {
                Node sibling = child.split();
                insertChild(sibling.getFirstLeafKey(), sibling);
            }

            if (!root.isOverflow()) {
                return;
            }
            //splits++;
            //depth++;
            height=height+1;
            Node sibling = split();
            InternalNode newRoot = new InternalNode();
            newRoot.keys.add(sibling.getFirstLeafKey());
            newRoot.children.add(this);
            newRoot.children.add(sibling);
            root = newRoot;
        }

        @Override
        Key getFirstLeafKey() {
            return children.get(0).getFirstLeafKey();
        }

        @Override
        List<Value> getRange(Key key1, RangePolicy policy1, Key key2,
                    RangePolicy policy2) {
            return getChild(key1).getRange(key1, policy1, key2, policy2);
        }

        @Override
        //Displays the next 10 keys
        List<Key> getNext10Key(Key key1, RangePolicy policy) {
            return getChild(key1).getNext10Key(key1, policy);
        }
        

        @Override
        Map<Key, Value> getData(Key key1, RangePolicy policy) {
            return getChild(key1).getData(key1, policy);
        }

        @Override
        //Merges the node
        void merge(Node sibling) {
            @SuppressWarnings("unchecked")
            InternalNode node = (InternalNode) sibling;
            Pfusions = Pfusions +1;
            //height = height-1;
            keys.add(node.getFirstLeafKey());
            keys.addAll(node.keys);
            children.addAll(node.children);
        }

        @Override
        //Splits the node
        Node split() {
            int from = keyNumber() / 2 + 1, to = keyNumber();
            InternalNode sibling = new InternalNode();
            Psplits = Psplits +1;
            sibling.keys.addAll(keys.subList(from, to));
            sibling.children.addAll(children.subList(from, to + 1));

            keys.subList(from - 1, to).clear();
            children.subList(from, to + 1).clear();
            return sibling;
            
        }

        @Override
        boolean isOverflow() {
            return children.size() > branchingFactor;
        }

        @Override
        boolean isUnderflow() {
            return children.size() < (branchingFactor + 1) / 2;
        }

        Node getChild(Key key) {
            int loc = Collections.binarySearch(keys, key);
            int childIndex = loc >= 0 ? loc + 1 : -loc - 1;
            return children.get(childIndex);
        }

        void deleteChild(Key key) {
            int loc = Collections.binarySearch(keys, key);
            if (loc >= 0) {
                keys.remove(loc);
                children.remove(loc + 1);
            }
        }

        void insertChild(Key key, Node child) {
            int loc = Collections.binarySearch(keys, key);
            int childIndex = loc >= 0 ? loc + 1 : -loc - 1;
            if (loc >= 0) {
                children.set(childIndex, child);
            } else {
                keys.add(childIndex, key);
                children.add(childIndex + 1, child);
            }
        }

        Node getChildLeftSibling(Key key) {
            int loc = Collections.binarySearch(keys, key);
            int childIndex = loc >= 0 ? loc + 1 : -loc - 1;
            if (childIndex > 0)
                return children.get(childIndex - 1);

            return null;
        }

        Node getChildRightSibling(Key key) {
            int loc = Collections.binarySearch(keys, key);
            int childIndex = loc >= 0 ? loc + 1 : -loc - 1;
            if (childIndex < keyNumber())
                return children.get(childIndex + 1);
            return null;
        }
    }

    private class LeafNode extends Node {
        List<Value> values;
        LeafNode next;

        LeafNode() {
            keys = new ArrayList<Key>();
            values = new ArrayList<Value>();
        }

        @Override
        Value getValue(Key key) {
            int loc = Collections.binarySearch(keys, key);
            return loc >= 0 ? values.get(loc) : null;
        }

        @Override
        //Deletes a leaf node with the given key
        void deleteValue(Key key) {
            int loc = Collections.binarySearch(keys, key);
            if (loc >= 0) {
                keys.remove(loc);
                values.remove(loc);
            }
        }

        @Override
        //Inserts a new node with the given key and value
        void insertValue(Key key, Value value) {
            int loc = Collections.binarySearch(keys, key);
            int valueIndex = loc >= 0 ? loc : -loc - 1;
            if (loc >= 0) {
                values.set(valueIndex, value);
            } else {
                keys.add(valueIndex, key);
                values.add(valueIndex, value);
            }
            if (root.isOverflow()) {
                //splits++;
                //System.out.println("Split2: " + splits);
            	height=height+1;
                Node sibling = split();
                InternalNode newRoot = new InternalNode();
                newRoot.keys.add(sibling.getFirstLeafKey());
                newRoot.children.add(this);
                newRoot.children.add(sibling);
                root = newRoot;
            }
        }

        @Override
        Key getFirstLeafKey() {
            return keys.get(0);
        }

        @Override
        List<Value> getRange(Key key1, RangePolicy policy1, Key key2,
                         RangePolicy policy2) {
            List<Value> result = new LinkedList<Value>();
            LeafNode node = this;
            while (node != null) {
                Iterator<Key> kIt = node.keys.iterator();
                Iterator<Value> vIt = node.values.iterator();
                while (kIt.hasNext()) {
                    Key key = kIt.next();
                    Value value = vIt.next();
                    int cmp1 = key.compareTo(key1);
                    int cmp2 = key.compareTo(key2);
                    if (((policy1 == RangePolicy.EXCLUSIVE && cmp1 > 0) || (policy1 == RangePolicy.INCLUSIVE && cmp1 >= 0))
                            && ((policy2 == RangePolicy.EXCLUSIVE && cmp2 < 0) || (policy2 == RangePolicy.INCLUSIVE && cmp2 <= 0)))
                        result.add(value);
                    else if ((policy2 == RangePolicy.EXCLUSIVE && cmp2 >= 0)
                            || (policy2 == RangePolicy.INCLUSIVE && cmp2 > 0))
                        return result;
                }
                node = node.next;
            }
            return result;
        }

        @Override
        List<Key> getNext10Key(Key key1, RangePolicy policy1) {
            List<Key> result = new LinkedList<Key>();
            LeafNode node = this;
            int number = 0;
            while (node!= null) {
                Iterator<Key> kIt = node.keys.iterator();
                Iterator<Value> vIt = node.values.iterator();
                while (kIt.hasNext()) {
                    Key key = kIt.next();
                    int cmp1 = key.compareTo(key1);
                    if (number == 10) {
                        return result;
                    } else if (((policy1 == RangePolicy.EXCLUSIVE && cmp1 > 0) || (policy1 == RangePolicy.INCLUSIVE && cmp1 >= 0))) {
                        number++;
                        result.add(key);
                    }
                }
                node = node.next;
            }
            return result;
        }

        @Override
        Map<Key, Value> getData(Key key1, RangePolicy policy1) {
            Map<Key, Value> result = new HashMap<Key, Value>();
            LeafNode node = this;
            while (node!= null) {
                Iterator<Key> kIt = node.keys.iterator();
                Iterator<Value> vIt = node.values.iterator();

                while (kIt.hasNext()) {
                    Key key = kIt.next();
                    Value value = vIt.next();

                    int cmp1 = key.compareTo(key1);

                    if (((policy1 == RangePolicy.EXCLUSIVE && cmp1 > 0) || (policy1 == RangePolicy.INCLUSIVE && cmp1 >= 0))) {
                        result.put(key, value);
                    } else {
                        return result;
                    }
                }
                node = node.next;
            }
            return result;
        }

        @Override
        void merge(Node sibling) {
        	Cfusions = Cfusions +1;
            @SuppressWarnings("unchecked")
            LeafNode node = (LeafNode) sibling;
            keys.addAll(node.keys);
            values.addAll(node.values);
            next = node.next;
        }

        @Override
        Node split() {
        	Csplits = Csplits + 1;
            LeafNode sibling = new LeafNode();
            int from = (keyNumber() + 1) / 2, to = keyNumber();
            sibling.keys.addAll(keys.subList(from, to));
            sibling.values.addAll(values.subList(from, to));

            keys.subList(from, to).clear();
            values.subList(from, to).clear();

            sibling.next = next;
            next = sibling;
            return sibling;
        }

        @Override
        boolean isOverflow() {
            return values.size() > branchingFactor - 1;
        }

        @Override
        boolean isUnderflow() {
            return values.size() < branchingFactor / 2;
        }
    }

}