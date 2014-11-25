package com.imperial.heap3d.application;

import com.imperial.heap3d.snapshot.*;
import com.sun.jdi.*;

import java.util.*;

/**
 * Created by oskar on 24/11/14.
 */
public class NodesBuilder {

    private final StackFrame _stackFrame;
    private Map<Long, Node> _uniqueNodes;

    public NodesBuilder(StackFrame stackFrame) {
        _stackFrame = stackFrame;
        _uniqueNodes = new HashMap<>();
    }

    public Collection<StackNode> build() {
        _uniqueNodes.clear();
        Collection<StackNode> stackNodes = new LinkedList<>();
        ObjectReference thisObject = _stackFrame.thisObject();

        if (thisObject != null) {
            String name = "this";
            stackNodes.add(new StackNode(name, drillDown(name, thisObject)));
        }
        List<LocalVariable> localVariables;
        try {
            localVariables = _stackFrame.visibleVariables();
        } catch (AbsentInformationException ignored) {
            return stackNodes;
        }

        for (Map.Entry<LocalVariable, Value> entry : _stackFrame.getValues(localVariables).entrySet()) {
            String name = entry.getKey().name();
            Value value = entry.getValue();

            if (value == null || value instanceof PrimitiveValue) {
                stackNodes.add(new StackNode(name, value));
            } else {
                stackNodes.add(new StackNode(name, drillDown(name, value)));
            }
        }
        return stackNodes;
    }

    private Node drillDown(String name, Value value) {
        ObjectReference reference = (ObjectReference) value;
        long id = reference.uniqueID();
        if(_uniqueNodes.containsKey(id))
            return _uniqueNodes.get(id);
        System.out.println(String.format("name: %s, id:%s", name, id));

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
            node = arrayNode;
        } else if (reference instanceof StringReference) {
            StringReference stringReference = (StringReference) reference;
            node = new StringNode(name, id, stringReference.toString());
            _uniqueNodes.put(id, node);
        } else {
            ObjectNode objectNode = new ObjectNode(name, id);
            _uniqueNodes.put(id, objectNode);
            for (Map.Entry<Field, Value> entry : reference.getValues(reference.referenceType().allFields()).entrySet()) {
                String fieldName = entry.getKey().name();
                Value fieldValue = entry.getValue();
                addValueToObjectNode(objectNode, fieldName, fieldValue);
            }
            node = objectNode;
        }
        return node;
    }

    private Node createArrayElemNode(String name, Value value, int index) {
        if (value == null || value instanceof PrimitiveValue) {
            return new ArrayElemNode(index, value);
        }
        // TODO -- discuss if it should just be drillDown without wrapping in AEN
        return new ArrayElemNode(index, drillDown(name + "[" + index + "]", value));
    }

    private void addValueToObjectNode(ObjectNode node, String name, Value value) {
        if (value == null || value instanceof PrimitiveValue)
            node.addPrimitive(name, value);
        else
            node.addReference(drillDown(name, value));
    }
}