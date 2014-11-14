package com.imperial.heap3d.variables.implementation;

import com.imperial.heap3d.variables.ILocalVariable;
import com.imperial.heap3d.variables.IFieldValue;
import com.sun.jdi.LocalVariable;

import java.util.List;

/**
 * Created by om612 on 14/11/14.
 */
public class ConcreteLocalVariable implements ILocalVariable {
    @Override
    public boolean isArgument() {
        return false;
    }
}
