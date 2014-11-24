package tests.system.testprograms.binary_tree;

import tests.system.testprograms.utilities.TreeNode;;

/**
 * Created by oskar on 21/11/14.
 */
public class Program {

    public static void main(String[] args) {
        // level 1
        TreeNode root = new TreeNode();

        // level 2
        root = new TreeNode(new TreeNode(), new TreeNode());

        // level 3
        root = new TreeNode(new TreeNode(new TreeNode(), new TreeNode()),
                new TreeNode(new TreeNode(), new TreeNode()));

        // level 4
        root = new TreeNode(new TreeNode(new TreeNode(new TreeNode(), new TreeNode()),
                                new TreeNode(new TreeNode(), new TreeNode())),
                new TreeNode(new TreeNode(new TreeNode(), new TreeNode()),
                        new TreeNode(new TreeNode(), new TreeNode())));

        // left-most & right-most only
        root = new TreeNode(new TreeNode(new TreeNode(new TreeNode(new TreeNode(new TreeNode(),
                null), null), null), null),
                new TreeNode(null, new TreeNode(null, new TreeNode(null, new TreeNode(null, new TreeNode())))));

        System.out.println(root);
    }
}
