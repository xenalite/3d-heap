package com.imperial.heap3d.implementations.factories;

import com.imperial.heap3d.implementations.snapshot.*;
import com.imperial.heap3d.interfaces.factories.INodeBuilder;
import com.sun.jdi.*;
import com.sun.tools.jdi.ArrayReferenceImpl;

import java.util.*;

/**
 * Created by oskar on 24/11/14.
 */
public class NodeBuilder implements INodeBuilder {

    private Map<Long, Node> _uniqueNodes = new HashMap<>();

    public Collection<StackNode> build(StackFrame stackFrame) {
        Collection<StackNode> stackNodes = new LinkedList<>();
        if (stackFrame == null)
            return stackNodes;

        List<LocalVariable> localVariables;
        try {
            localVariables = stackFrame.visibleVariables();
        } catch (AbsentInformationException ignored) {
            return stackNodes;
        }

        ObjectReference thisObject = stackFrame.thisObject();

        if (thisObject != null) {
            String name = "this";
            stackNodes.add(new StackNode(name, drillDown(name, thisObject)));
        }

        for (LocalVariable localVariable : localVariables) {
            String name = localVariable.name();
            Value value = stackFrame.getValue(localVariable);

            if (value == null || value instanceof PrimitiveValue) {
                stackNodes.add(new StackNode(name, value));
            } else if(value instanceof ObjectReference){
                stackNodes.add(new StackNode(name, drillDown(name, (ObjectReference)value)));
            }
        }
        _uniqueNodes.clear();
        return stackNodes;
    }

    private Node drillDown(String name, ObjectReference reference) {
        long id = reference.uniqueID();
        if (_uniqueNodes.containsKey(id))
            return _uniqueNodes.get(id);

        Node node;
        if (reference instanceof ArrayReference) {
            ArrayReference arrayReference = (ArrayReference) reference;
            ArrayNode arrayNode = new ArrayNode(name, id);
            _uniqueNodes.put(id, arrayNode);
            int index = 0;
            for (Value arrayValue : arrayReference.getValues()) {
                addValueToArrayNode(arrayNode, name, arrayValue, index);
                ++index;
            }
            return arrayNode;
        } else if (reference instanceof StringReference) {
            StringReference stringReference = (StringReference) reference;
            node = new StringNode(name, id, stringReference.toString());
            _uniqueNodes.put(id, node);
        } else {
            ObjectNode objectNode = new ObjectNode(name, id);
            _uniqueNodes.put(id, objectNode);
            for (Field field : reference.referenceType().fields()) {
                String fieldName = field.name();
                Value fieldValue = reference.getValue(field);
                if(!field.isStatic())
                    addValueToObjectNode(objectNode, fieldName, fieldValue);
            }
            return objectNode;
        }
        return node;
    }

    private void addValueToArrayNode(ArrayNode node, String name, Value value, int index) {
        if (value == null || value instanceof PrimitiveValue) {
            node.addPrimitive(index, value);
        } else if(value instanceof ObjectReference) {
            node.addReference(drillDown(name + "[" + index + "]", (ObjectReference) value) );
        }
    }

    private void addValueToObjectNode(ObjectNode node, String name, Value value) {
        if (value == null || value instanceof PrimitiveValue)
            node.addPrimitive(name, value);
        else if(value instanceof ObjectReference)
            node.addReference(drillDown(name, (ObjectReference)value));
    }
}