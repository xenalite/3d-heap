package tests.system.testprograms.utilities;

/**
 * Created by oskar on 21/11/14.
 */
public class TreeNode {

	private static int inc = 0;
    public TreeNode left;
    public TreeNode right;
    public int value = inc++;

    public TreeNode() {}

    public TreeNode(TreeNode left, TreeNode right) {
        this.left = left;
        this.right = right;
    }
}
