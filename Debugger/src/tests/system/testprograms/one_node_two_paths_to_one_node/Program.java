package tests.system.testprograms.one_node_two_paths_to_one_node;
import tests.system.testprograms.utilities.Pair;

import java.util.Objects;

/**
 * Created by zhouyou_robert on 25/11/14.
 */
public class Program {
    public static void main(String[] args) {
        Object o = new Object();
        Pair<Object,Object> p = new Pair<Object, Object>(o,o);
        System.out.printf(p.toString());
    }
}
