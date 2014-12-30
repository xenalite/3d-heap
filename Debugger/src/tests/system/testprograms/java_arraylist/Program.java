package tests.system.testprograms.java_arraylist;

import tests.system.testprograms.utilities.Utils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by oskar on 30/12/14.
 */
public class Program {

    public static void main(String[] args) {
        Collection<String> arrayList = Utils.populateWithStringElems(new ArrayList<>());
        System.out.println(arrayList);
    }
}
