package com.heap3d.implementations.factories;

import com.heap3d.implementations.node.*;
import com.heap3d.interfaces.factories.INodeBuilder;
import com.heap3d.utilities.Pair;
import com.sun.jdi.*;

import java.util.*;

/**
 * Created by oskar on 24/11/14.
 */
public class NodeBuilder implements INodeBuilder {

    private Map<Long, Node> _uniqueNodes = new HashMap<>();
    private Collection<StackNode> _savedNodes = new ArrayList<>();
    private Map<String, StaticNode> _staticNodesMap = new HashMap<>();

    public Collection<StackNode> build(StackFrame stackFrame) {
        Collection<StackNode> stackNodes = new ArrayList<>();
        if (stackFrame == null)
            return stackNodes;

        List<LocalVariable> localVariables;
        try {
            localVariables = stackFrame.visibleVariables();
        } catch (AbsentInformationException ignored) {
            return _savedNodes;
            // return stackNodes;
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
            } else if (value instanceof ObjectReference) {
                stackNodes.add(new StackNode(name, drillDown(name, (ObjectReference) value)));
            }
        }

        for (StaticNode ssn : _staticNodesMap.values()) {
            stackNodes.add(ssn);
        }

        _uniqueNodes.clear();
        _staticNodesMap.clear();
        _savedNodes = stackNodes;

        return stackNodes;
    }

    private Pair<Node,String> drillDown(String name, ObjectReference reference) {
        long id = reference.uniqueID();
        if (_uniqueNodes.containsKey(id)) {
            return Pair.create(_uniqueNodes.get(id), name);
        }

        Node node;
        if (reference instanceof ArrayReference) {
            ArrayReference arrayReference = (ArrayReference) reference;
            ArrayNode arrayNode = new ArrayNode(id);
            _uniqueNodes.put(id, arrayNode);
            int index = 0;
            for (Value arrayValue : arrayReference.getValues()) {
                addValueToArrayNode(arrayNode, name, arrayValue, index);
                ++index;
            }
            return Pair.create(arrayNode, name);
        } else {
            ObjectNode objectNode = new ObjectNode(id);
            _uniqueNodes.put(id, objectNode);
            for (Field field : reference.referenceType().fields()) {
                String fieldName = field.name();
                Value fieldValue = reference.getValue(field);
                if (!field.isStatic()) {
                    addValueToObjectNode(objectNode, fieldName, fieldValue);
                } else {
                    staticDrillDown(name, reference, field);
                }
            }
            return Pair.create(objectNode, name);
        }
    }

    private void staticDrillDown(String name, ObjectReference reference, Field field) {
        String referenceTypeName = reference.referenceType().name();
        StaticNode sn;

        if (_staticNodesMap.containsKey(referenceTypeName)) {
            sn = _staticNodesMap.get(referenceTypeName);
        } else {
            sn = new StaticNode(referenceTypeName, referenceTypeName.hashCode());
            _staticNodesMap.put(referenceTypeName, sn);
        }

        sn.addReferencingVar(name);
        Value fieldValue = reference.getValue(field);

        if (fieldValue == null || fieldValue instanceof PrimitiveValue) {
            sn.addPrimitive(field.name(), fieldValue);
        } else if (fieldValue instanceof ArrayReference) {
            ArrayReference arrayReference = (ArrayReference) fieldValue;
            long id = arrayReference.uniqueID();
            ArrayNode arrayNode = new ArrayNode(id);
            sn.addReference(arrayNode, field.name());
            int index = 0;

            for (Value arrayValue : arrayReference.getValues()) {
                addValueToArrayNode(arrayNode, field.name(), arrayValue, index);
                ++index;
            }
        } else if (fieldValue instanceof ObjectReference) {
            ObjectReference objectReference = (ObjectReference) fieldValue;
            long id = objectReference.uniqueID();
            ObjectNode node = new ObjectNode(id);
            sn.addReference(node, field.name());

            for (Field f : objectReference.referenceType().fields()) {
                String fName = f.name();
                Value fValue = objectReference.getValue(f);

                if (f.isStatic()) {
                    // This breaks for a "Random" object... Gets stuck in an infinite loop.
                    // staticDrillDown(field.name(), objectReference, f);
                    // System.out.println("====>   " + fName + " " + fValue);
                } else {
                    // System.out.println("****>   " + fName + " " + fValue);
                    addValueToObjectNode(node, fName, fValue);
                }
            }
        }
    }

    private void addValueToArrayNode(ArrayNode node, String name, Value value, int index) {
        if (value == null || value instanceof PrimitiveValue) {
            node.addPrimitive(index, value);
        } else if (value instanceof ObjectReference) {
            Pair<Node,String> pair = drillDown(name + "[" + index + "]", (ObjectReference) value);
            node.addReference(pair.first, pair.second);
        }
    }

    private void addValueToObjectNode(ObjectNode node, String name, Value value) {
        if (value == null || value instanceof PrimitiveValue) {
            node.addPrimitive(name, value);
        } else if (value instanceof ObjectReference) {
            Pair<Node, String> pair = drillDown(name, (ObjectReference) value);
            node.addReference(pair.first, pair.second);
        }
    }
}