package tests.system.testprograms.java_hashmap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by oskar on 30/12/14.
 */
public class Program {

    public static void main(String[] args) {
        Map<String,String> map = populate(new HashMap<>());
        System.out.println(map);
    }

    private static Map<String, String> populate(Map<String, String> map) {
        for(int i = 0; i < 100; ++i) {
            map.put(String.valueOf(i), String.valueOf(i));
        }
        return map;
    }
}
