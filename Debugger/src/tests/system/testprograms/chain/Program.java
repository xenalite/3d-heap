package tests.system.testprograms.chain;

import tests.system.testprograms.utilities.TreeNode;

/**
 * Created by oskar on 28/11/14.
 */
public class Program {

    public static void main(String[] args) {
        TreeNode link1 = new TreeNode();
        TreeNode link2 = new TreeNode();
        TreeNode root = new TreeNode();
        root.left = new TreeNode(null, link1);
        root.right = new TreeNode(link1, null);
        link1.left = new TreeNode(null, link2);
        link1.right = new TreeNode(link2, null);

        print(root);
    }

    public static void print(TreeNode root) {
        System.out.println(root);
    }
}
