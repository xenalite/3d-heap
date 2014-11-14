package com.imperial.heap3d.variables.implementation;

import com.imperial.heap3d.variables.IFieldValue;
import com.sun.jdi.Field;
import com.sun.jdi.Value;

import java.util.List;

/**
 * Created by om612 on 14/11/14.
 */
public class ConcreteFieldValue implements IFieldValue {

    private final Value _value;

    public ConcreteFieldValue(Field entry, Value value) {
        _value = value;
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public boolean isPrivate() {
        return false;
    }

    @Override
    public boolean isProtected() {
        return false;
    }

    @Override
    public boolean isPublic() {
        return false;
    }

    @Override
    public boolean isFinal() {
        return false;
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public String typeName() {
        return null;
    }

    @Override
    public List<IFieldValue> drillDown() {
        return null;
    }
}
