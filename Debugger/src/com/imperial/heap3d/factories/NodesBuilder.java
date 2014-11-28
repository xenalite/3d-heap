package com.imperial.heap3d.factories;

import com.imperial.heap3d.snapshot.*;
import com.sun.jdi.*;

import java.util.*;

/**
 * Created by oskar on 24/11/14.
 */
public class NodesBuilder {

    private Map<Long, Node> _uniqueNodes = new HashMap<>();

    public Collection<StackNode> build(StackFrame stackFrame) {
        Collection<StackNode> stackNodes = new LinkedList<>();
        if (stackFrame == null)
            return stackNodes;

        ObjectReference thisObject = stackFrame.thisObject();

        if (thisObject != null) {
            String name = "this";
            stackNodes.add(new StackNode(name, drillDown(name, thisObject)));
        }
        List<LocalVariable> localVariables;
        try {
            localVariables = stackFrame.visibleVariables();
        } catch (AbsentInformationException ignored) {
            return stackNodes;
        }

        for (LocalVariable localVariable : localVariables) {
            String name = localVariable.name();
            Value value = stackFrame.getValue(localVariable);

            if (value == null || value instanceof PrimitiveValue) {
                stackNodes.add(new StackNode(name, value));
            } else {
                stackNodes.add(new StackNode(name, drillDown(name, value)));
            }
        }
        _uniqueNodes.clear();
        return stackNodes;
    }

    private Node drillDown(String name, Value value) {
        ObjectReference reference = (ObjectReference) value;
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
                arrayNode.addElement(createArrayElemNode(name, arrayValue, index));
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
                addValueToObjectNode(objectNode, fieldName, fieldValue);
            }
            return objectNode;
        }
        return node;
    }

    private Node createArrayElemNode(String name, Value value, int index) {
        if (value == null || value instanceof PrimitiveValue) {
            return new ArrayElemNode(index, value);
        }
        return drillDown(name + "[" + index + "]", value);
    }

    private void addValueToObjectNode(ObjectNode node, String name, Value value) {
        if (value == null || value instanceof PrimitiveValue)
            node.addPrimitive(name, value);
        else
            node.addReference(drillDown(name, value));
    }
}