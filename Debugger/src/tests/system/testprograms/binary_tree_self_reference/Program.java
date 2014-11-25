package tests.system.testprograms.binary_tree_self_reference;

import tests.system.testprograms.utilities.TreeNode;

/**
 * Created by oskar on 21/11/14.
 */
public class Program {

    public static void main(String[] args) {
        // level 1
        TreeNode root = new TreeNode();

        // level 2
        root = new TreeNode(root, root);

        // level 3
        root = new TreeNode(root, root);

        // level 4
        root = new TreeNode(root, root);

        System.out.println(root);
    }
}
