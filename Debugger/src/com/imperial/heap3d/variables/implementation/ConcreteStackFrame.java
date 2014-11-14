package com.imperial.heap3d.variables.implementation;

import com.imperial.heap3d.variables.ILocalVariable;
import com.imperial.heap3d.variables.IStackFrame;
import com.imperial.heap3d.variables.IFieldValue;
import com.imperial.heap3d.variables.IValue;
import com.sun.jdi.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by om612 on 14/11/14.
 */
public class ConcreteStackFrame implements IStackFrame {

    private StackFrame _stackFrame;
    private ReferenceType _referenceType;
    private ThreadReference _threadReference;

    public ConcreteStackFrame(StackFrame stackFrame) {
        _stackFrame = stackFrame;
        _threadReference = _stackFrame.thread();
        _referenceType = _threadReference.referenceType();
    }

    @Override
    public List<ILocalVariable> getLocalVariables() {
        List<ILocalVariable> localVariables = new ArrayList<>();
        try {
            List<LocalVariable> jdiLocalVariables = _stackFrame.visibleVariables();
//            localVariables.addAll(jdiLocalVariables.stream().map(ConcreteLocalVariable::new)
//                    .collect(Collectors.toList()));

        } catch (AbsentInformationException ignored) {}
        return localVariables;
    }

    @Override
    public IValue getThisObject() {
        return new BaseObjectReference(_stackFrame.thisObject());
    }

    @Override
    public boolean isStaticContext() {
        return _stackFrame.thisObject() == null;
    }

    @Override
    public String referenceType() {
        return _referenceType.name();
    }

    @Override
    public List<IFieldValue> getStaticVariables() {
        List<IFieldValue> variables = new ArrayList<>();
        List<Field> fields = _referenceType.fields();
        fields.stream().filter(TypeComponent::isStatic)
                .forEach(entry -> variables.add(new ConcreteFieldValue(entry, _referenceType.getValue(entry))));

        return variables;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public String threadName() {
        return _threadReference.name();
    }
}
