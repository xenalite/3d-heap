package tests.system.testprograms.reference_becomes_null;

import tests.system.testprograms.utilities.TreeNode;

import java.util.*;

/**
 * Created by oskar on 06/12/14.
 */
@SuppressWarnings("ALL")
public class Program {

    public static void main(String[] args) {
        Collection<String> reallyLongName = createCollection();
    }

    private static Collection<String> createCollection() {
        Collection<String> collection = new HashSet<>();
        for(int i = 0; i < 100; ++i)
            collection.add(String.valueOf(i));
        return collection;
    }
}