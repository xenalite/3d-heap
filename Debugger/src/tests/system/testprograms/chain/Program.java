package tests.system.testprograms.chain;

import tests.system.testprograms.utilities.TreeNode;

/**
 * Created by oskar on 28/11/14.
 */
public class Program {

    public static void main(String[] args) {
        TreeNode left = new TreeNode();
        buildNextChainLink(buildNextChainLink(buildNextChainLink(left)));
        TreeNode right = new TreeNode();
        buildNextChainLink(buildNextChainLink(buildNextChainLink(right)));

        TreeNode root = new TreeNode(left, right);
        print(root);
    }

    public static TreeNode buildNextChainLink(TreeNode node) {
        TreeNode link = new TreeNode();
        node.left = new TreeNode(null, new TreeNode(null, link));
        node.right = new TreeNode(new TreeNode(link, null), null);
        return link;
    }

    public static void print(TreeNode root) {
        System.out.println(root);
    }
}
