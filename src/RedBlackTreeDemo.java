import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class RedBlackTreeDemo {

    private static class Node {
        int data;
        Node parent;
        Node left;
        Node right;
        boolean color; // true = RED, false = BLACK

        public Node(int data) {
            this.data = data;
        }
    }

    private Node root;
    private Node TNULL; // Листовий вузол (Sentinel)

    // Константи кольорів для зручності
    private final boolean RED = true;
    private final boolean BLACK = false;

    public RedBlackTreeDemo() {
        TNULL = new Node(0);
        TNULL.color = BLACK;
        TNULL.left = null;
        TNULL.right = null;
        root = TNULL;
    }


    private void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != TNULL) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rightRotate(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != TNULL) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    private void fixInsert(Node k) {
        Node u; // дядько (uncle)
        while (k.parent.color == RED) {
            if (k.parent == k.parent.parent.right) {
                u = k.parent.parent.left;
                if (u.color == RED) {
                    // Випадок 1: Дядько червоний
                    u.color = BLACK;
                    k.parent.color = BLACK;
                    k.parent.parent.color = RED;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.left) {
                        // Випадок 2: Дядько чорний, k - ліва дитина (трикутник)
                        k = k.parent;
                        rightRotate(k);
                    }
                    // Випадок 3: Дядько чорний, k - права дитина (лінія)
                    k.parent.color = BLACK;
                    k.parent.parent.color = RED;
                    leftRotate(k.parent.parent);
                }
            } else {
                u = k.parent.parent.right;
                if (u.color == RED) {
                    // Випадок 1 дзеркальний
                    u.color = BLACK;
                    k.parent.color = BLACK;
                    k.parent.parent.color = RED;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.right) {
                        // Випадок 2 дзеркальний
                        k = k.parent;
                        leftRotate(k);
                    }
                    // Випадок 3 дзеркальний
                    k.parent.color = BLACK;
                    k.parent.parent.color = RED;
                    rightRotate(k.parent.parent);
                }
            }
            if (k == root) {
                break;
            }
        }
        root.color = BLACK;
    }

    public void insert(int key) {
        Node node = new Node(key);
        node.parent = null;
        node.data = key;
        node.left = TNULL;
        node.right = TNULL;
        node.color = RED;

        Node y = null;
        Node x = this.root;

        while (x != TNULL) {
            y = x;
            if (node.data < x.data) {
                x = x.left;
            } else {
                x = x.right;
            }
        }

        node.parent = y;
        if (y == null) {
            root = node;
        } else if (node.data < y.data) {
            y.left = node;
        } else {
            y.right = node;
        }

        if (node.parent == null) {
            node.color = BLACK;
            return;
        }

        if (node.parent.parent == null) {
            return;
        }

        fixInsert(node);
    }

    public void printTree() {
        if (root == TNULL) {
            System.out.println("Дерево порожнє.");
            return;
        }
        printHelper(this.root, "", true);
    }

    private void printHelper(Node root, String indent, boolean last) {
        if (root != TNULL) {
            System.out.print(indent);
            if (last) {
                System.out.print("R----");
                indent += "   ";
            } else {
                System.out.print("L----");
                indent += "|  ";
            }

            String sColor = root.color == RED ? "RED" : "BLACK";
            System.out.println(root.data + "(" + sColor + ")");
            printHelper(root.left, indent, false);
            printHelper(root.right, indent, true);
        }
    }

    public void inorderTraversal() {
        inorderHelper(this.root);
        System.out.println();
    }

    private void inorderHelper(Node node) {
        if (node != TNULL) {
            inorderHelper(node.left);
            System.out.print(node.data + " ");
            inorderHelper(node.right);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RedBlackTreeDemo bst = new RedBlackTreeDemo();
        Random rand = new Random();

        System.out.println("=== Червоно-Чорне Дерево (Red-Black Tree) ===");

        while (true) {
            System.out.println("\nОберіть дію:");
            System.out.println("1. Заповнити випадковими числами (Random)");
            System.out.println("2. Заповнити впорядкованими числами (Sorted) - демонстрація балансування");
            System.out.println("3. Додати число вручну");
            System.out.println("4. Показати дерево");
            System.out.println("5. Очистити дерево (створити нове)");
            System.out.println("0. Вихід");
            System.out.print("Ваш вибір: ");

            String choice = scanner.next();

            switch (choice) {
                case "1":
                    bst = new RedBlackTreeDemo();
                    System.out.print("Введіть кількість елементів: ");
                    int count = scanner.nextInt();
                    int[] randomArr = new int[count];
                    System.out.print("Масив додавання: ");
                    for (int i = 0; i < count; i++) {
                        randomArr[i] = rand.nextInt(100);
                        System.out.print(randomArr[i] + " ");
                        bst.insert(randomArr[i]);
                    }
                    System.out.println("\nГотово! Дерево побудовано.");
                    break;

                case "2":
                    bst = new RedBlackTreeDemo();
                    System.out.print("Введіть кількість елементів: ");
                    int sortedCount = scanner.nextInt();
                    int[] sortedArr = new int[sortedCount];
                    for (int i = 0; i < sortedCount; i++) {
                        sortedArr[i] = rand.nextInt(100);
                    }
                    Arrays.sort(sortedArr); // Сортуємо масив
                    System.out.print("Додаємо в порядку зростання: ");
                    for (int num : sortedArr) {
                        System.out.print(num + " ");
                        bst.insert(num);
                    }
                    break;

                case "3":
                    System.out.print("Введіть ціле число: ");
                    if (scanner.hasNextInt()) {
                        int val = scanner.nextInt();
                        bst.insert(val);
                        System.out.println("Вузол " + val + " додано.");
                    } else {
                        System.out.println("Помилка вводу.");
                        scanner.next();
                    }
                    break;

                case "4":
                    System.out.println("\n--- Структура Дерева ---");
                    bst.printTree();
                    System.out.println("\n--- In-order обхід (перевірка сортування) ---");
                    bst.inorderTraversal();
                    break;

                case "5":
                    bst = new RedBlackTreeDemo();
                    System.out.println("Дерево очищено.");
                    break;

                case "0":
                    System.out.println("Завершення роботи.");
                    return;

                default:
                    System.out.println("Невірний вибір. Спробуйте ще раз.");
            }
        }
    }
}