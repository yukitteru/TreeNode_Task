package com.javarush.task.task20.task2028;

import java.io.Serializable;
import java.util.*;

public class CustomTree extends AbstractList<String> implements Cloneable, Serializable {
    private static final long serialVersionUID = -4119900599457645130L;
    Entry<String> root = new Entry<>("0");

    @Override
    public String get(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String set(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends String> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(String s) {
        //Создаем очередь
        Queue<Entry<String>> addQueue = new LinkedList<>();
        //Добавляем самый верхний элемент
        addQueue.add(root);
        //Пока очередь не пуста: 1) Получаем элемент из начала очереди и удаляем его из очереди; 2) Проверяем соседей текущей ноды
        while (!addQueue.isEmpty()) {
            Entry<String> node = addQueue.poll();
            node.checkChildren();
            // Если возможность породить ветку = true - создаем инстанс новой ветки
            if (node.isAvailableToAddChildren()) {
                if (node.availableToAddLeftChildren) {
                    node.leftChild = new Entry<>(s);
                    node.leftChild.parent = node;
                    return true;
                }
                if (node.availableToAddRightChildren) {
                    node.rightChild = new Entry<>(s);
                    node.rightChild.parent = node;
                    return true;
                }
            } else {
                //Если ноды потомков не пусты - добавляем в конец очереди
                if (node.leftChild != null) {
                    addQueue.offer(node.leftChild);
                }
                if (node.rightChild != null) {
                    addQueue.offer(node.rightChild);
                }
            }
        }
        return false;
    }

    @Override
    public int size() {
        int size = -1;
        Queue<Entry<String>> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Entry<String> node = queue.poll();
            size++;
            if (node.leftChild != null) {
                queue.offer(node.leftChild);
            }
            if (node.rightChild != null) {
                queue.offer(node.rightChild);
            }
        }
        return size;
    }

    public String getParent(String s) {
        Queue<Entry<String>> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Entry<String> node = queue.poll();
            if (node.elementName.equals(s)) {
                return node.parent.elementName;
            } else {
                if (node.leftChild != null) {
                    queue.offer(node.leftChild);
                }
                if (node.rightChild != null) {
                    queue.offer(node.rightChild);
                }
            }
        }
        return null;
    }

    @Override
    public boolean remove(Object o) {
        if (o instanceof String) {
            String str = (String) o;
            Queue<Entry<String>> removeQueue = new LinkedList<>();
            removeQueue.add(root);
            while (!removeQueue.isEmpty()) {
                Entry<String> node = removeQueue.poll();
                if (node.leftChild != null) {
                    if (node.leftChild.elementName.equals(str)) {
                        node.leftChild = null;
                        node.availableToAddLeftChildren = true;
                        return true;
                    }
                    removeQueue.offer(node.leftChild);
                }
                if (node.rightChild != null) {
                    if (node.rightChild.elementName.equals(str)) {
                        node.rightChild = null;
                        node.availableToAddRightChildren = true;
                        return true;
                    }
                    removeQueue.offer(node.rightChild);
                }
            }
        } else {
            throw new UnsupportedOperationException();
        }
        return false;
    }

    static class Entry<T> implements Serializable {
        private static final long serialVersionUID = 1331998523540150170L;

        String elementName;
        boolean availableToAddLeftChildren, availableToAddRightChildren;
        Entry<T> parent, leftChild, rightChild;

        public Entry(String elementName) {
            this.elementName = elementName;
            this.availableToAddLeftChildren = true;
            this.availableToAddRightChildren = true;
        }

        void checkChildren() {
            if (leftChild != null) {
                availableToAddLeftChildren = false;
            }
            if (rightChild != null) {
                availableToAddRightChildren = false;
            }
        }

        public boolean isAvailableToAddChildren() {
            return availableToAddLeftChildren || availableToAddRightChildren;
        }
    }

}
