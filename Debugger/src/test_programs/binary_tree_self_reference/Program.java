package test_programs.binary_tree_self_reference;

import test_programs.utilities.TreeNode;

/**
 * Created by oskar on 21/11/14.
 */
@SuppressWarnings("ALL")
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
