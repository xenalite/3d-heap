package tests.system.testprograms.reference_becomes_null;

import tests.system.testprograms.utilities.TreeNode;

/**
 * Created by oskar on 06/12/14.
 */
@SuppressWarnings("ALL")
public class Program {

    public static void main(String[] args) {
//        TreeNode a = null;
//        TreeNode b = new TreeNode(buildTree(), buildTree());
//        TreeNode c = buildTree();
//        a = c;
//        c = new TreeNode(c, new TreeNode(null, c));

        TreeNode a = new TreeNode(new TreeNode(), new TreeNode());
        TreeNode b = a;
        a.left.left = new TreeNode();
    }

    public static TreeNode buildTree() {
        return new TreeNode(new TreeNode(new TreeNode(), new TreeNode()), new TreeNode(new TreeNode(), new TreeNode()));
    }
}