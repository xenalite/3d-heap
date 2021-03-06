package com.heap3d.utilities;

/**
 * Created by oskar on 23/12/14.
 */
public class Pair<P,Q> {

    public final P first;
    public final Q second;

    public Pair(P first, Q second) {
        this.first = first;
        this.second = second;
    }

    public static <A,B> Pair<A,B> create(A first, B second) {
        return new Pair<>(first, second);
    }
}
