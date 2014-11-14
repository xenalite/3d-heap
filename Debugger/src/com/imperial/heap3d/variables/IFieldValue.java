package com.imperial.heap3d.variables;

import java.util.List;

/**
 * Created by oskar on 13/11/14.
 */
public interface IFieldValue {

    public boolean isStatic();

    public boolean isPrivate();

    public boolean isProtected();

    public boolean isPublic();

    public boolean isFinal();

    public String name();

    public String typeName();

    public List<IFieldValue> drillDown();
}
