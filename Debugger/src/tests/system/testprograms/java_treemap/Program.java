package tests.system.testprograms.java_treemap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by oskar on 30/12/14.
 */
public class Program {

    public static void main(String[] args) {
        Map<String,String> map = populate(new HashMap<>());
    }

    private static Map<String, String> populate(Map<String,String> map) {
        for(int i = 0; i < 100; ++i) {
            int j = (int) (1 + Math.random() * 100);
            map.put(String.valueOf(j), String.valueOf(j));
        }
        return map;
    }
}
