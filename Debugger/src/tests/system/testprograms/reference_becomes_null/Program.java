package tests.system.testprograms.reference_becomes_null;

import tests.system.testprograms.utilities.ListNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oskar on 06/12/14.
 */
@SuppressWarnings("ALL")
public class Program {

    public static void main(String[] args) {
        List<ListNode> list = new ArrayList<>();
        createList(list);
    }

    private static void createList(List<ListNode> list) {
        for(int i = 0; i < 10; ++i) {
            list.add(new ListNode());
        }
    }
}