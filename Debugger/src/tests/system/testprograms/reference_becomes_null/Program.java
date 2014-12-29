package tests.system.testprograms.reference_becomes_null;

import tests.system.testprograms.utilities.TreeNode;

/**
 * Created by oskar on 06/12/14.
 */
@SuppressWarnings("ALL")
public class Program {

    public static void main(String[] args) {
        TreeNode a = new TreeNode(new TreeNode(), new TreeNode());
        TreeNode b = new TreeNode(a, a.left);
        TreeNode c = new TreeNode(new TreeNode(new TreeNode(), new TreeNode()), new TreeNode(new TreeNode(), new TreeNode()));
    }
}