package tests.system.testprograms.java_hashset;

import tests.system.testprograms.utilities.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by oskar on 30/12/14.
 */
public class Program {

    public static void main(String[] args) {
        Collection<String> hashSet = Utils.populateWithStringElems(new HashSet<>());
        System.out.println(hashSet);
    }
}
