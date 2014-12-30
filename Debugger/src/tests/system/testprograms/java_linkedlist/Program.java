package tests.system.testprograms.java_linkedlist;

import tests.system.testprograms.utilities.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by oskar on 30/12/14.
 */
public class Program {

    public static void main(String[] args) {
        Collection<String> linkedList = Utils.populateWithStringElems(new LinkedList<>());
        System.out.println(linkedList);
    }
}
