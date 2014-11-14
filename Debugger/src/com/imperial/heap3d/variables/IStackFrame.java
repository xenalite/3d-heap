package com.imperial.heap3d.variables;

import java.util.List;

/**
 * Created by oskar on 13/11/14.
 */
public interface IStackFrame {

    public List<ILocalVariable> getLocalVariables();

    public IValue getThisObject();

    public boolean isStaticContext();

    public String referenceType();

    public List<IFieldValue> getStaticVariables();

    public boolean isValid();

    public String threadName();
}
