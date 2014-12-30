package tests.system.testprograms.utilities;

import java.util.Collection;

/**
 * Created by oskar on 30/12/14.
 */
public class Utils {

    public static Collection<String> populateWithStringElems(Collection<String> collection) {
        for(int i = 0; i < 100; ++i) {
            collection.add(String.valueOf(i));
        }
        return collection;
    }
}
