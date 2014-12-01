package tests.system.testprograms.linked_list_string_elements;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by om612 on 25/11/14.
 */
@SuppressWarnings("ALL")
public class Program {

    private List<String> strings = new LinkedList<>();

    public static void main(String[] args) {
        Program p = new Program();

        List<String> list = p.createNewList(5);

        System.out.println(list);
    }

    public List<String> createNewList(int i) {
        if(i <= 0)
            return strings;

        String element = String.valueOf(i);
        List<String> newlist = addElement(strings, element);

        strings = newlist;

        return createNewList(i - 1);
    }

    public List<String> addElement(List<String> list, String element) {
        list.add(element);
        return list;
    }
}
