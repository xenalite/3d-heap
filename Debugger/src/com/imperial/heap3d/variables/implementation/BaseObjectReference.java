package com.imperial.heap3d.variables.implementation;

import com.imperial.heap3d.variables.IFieldValue;
import com.imperial.heap3d.variables.IValue;
import com.sun.jdi.Field;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by om612 on 14/11/14.
 */
public class BaseObjectReference implements IValue {

    private final ObjectReference _objectReference;
    private ReferenceType _referenceType;

    public BaseObjectReference(ObjectReference objectReference) {
        _objectReference = objectReference;
        _referenceType = _objectReference.referenceType();
    }

    @Override
    public String value() {
        return _objectReference.toString();
    }

    @Override
    public String type() {
        return _objectReference.type().toString();
    }

    @Override
    public boolean isReference() {
        return true;
    }

    @Override
    public List<IFieldValue> drillDown() {
        List<Field> fields = _referenceType.fields();
        List<IFieldValue> fieldValues = new ArrayList<>();

        for(Field entry : fields) {
            Value entryValue = _objectReference.getValue(entry);
            fieldValues.add(new ConcreteFieldValue(entry, entryValue));
        }
        return fieldValues;
    }
}
