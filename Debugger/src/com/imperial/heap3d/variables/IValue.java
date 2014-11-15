package com.imperial.heap3d.variables;

import java.util.List;

/**
 * Created by om612 on 14/11/14.
 */
public interface IValue {

    public String value();

    public String type();

    public boolean isReference();

    public List<IFieldValue> drillDown();
}
