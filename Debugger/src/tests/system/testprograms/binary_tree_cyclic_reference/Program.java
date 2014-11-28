package tests.system.testprograms.binary_tree_cyclic_reference;

import tests.system.testprograms.utilities.TreeNode;

/**
 * Created by oskar on 28/11/14.
 */
public class Program {

    public static void main(String[] args) {
        TreeNode left = new TreeNode();
        TreeNode right = new TreeNode();
        TreeNode root = new TreeNode(new TreeNode(left, new TreeNode()),
                new TreeNode(new TreeNode(), right));

        left.left = root;
        right.right = root;

        System.out.println(root);
    }
}
