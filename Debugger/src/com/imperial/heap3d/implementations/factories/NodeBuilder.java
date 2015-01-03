package com.imperial.heap3d.implementations.factories;

import com.imperial.heap3d.implementations.snapshot.*;
import com.imperial.heap3d.interfaces.factories.INodeBuilder;
import com.imperial.heap3d.utilities.Pair;
import com.sun.jdi.*;

import java.util.*;

/**
 * Created by oskar on 24/11/14.
 */
public class NodeBuilder implements INodeBuilder {

    private Map<Long, Node> _uniqueNodes = new HashMap<>();
    private Map<String, StaticNode> _staticNodesMap = new HashMap<>();

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
            } else if (value instanceof ObjectReference) {
                stackNodes.add(new StackNode(name, drillDown(name, (ObjectReference) value)));
            }
        }

        for (StaticNode ssn : _staticNodesMap.values()) {
            stackNodes.add(ssn);
        }

        _uniqueNodes.clear();
        return stackNodes;
    }

    private Pair<Node,String> drillDown(String name, ObjectReference reference) {
        long id = reference.uniqueID();
        if (_uniqueNodes.containsKey(id))
            return Pair.create(_uniqueNodes.get(id), name);

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

        /* Remove StringNodes for the moment as they are not really needed.
         * } else if (reference instanceof StringReference) {
         * StringReference stringReference = (StringReference) reference;
         * node = new StringNode(id, stringReference.toString());
         * _uniqueNodes.put(id, node);
         */

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
        // return Pair.create(node, name);
    }

    private void staticDrillDown(String name, ObjectReference reference, Field field) {
        String referenceTypeName = reference.referenceType().name();
        StaticNode ssn;

        if (_staticNodesMap.containsKey(referenceTypeName)) {
            ssn = _staticNodesMap.get(referenceTypeName);
        } else {
            ssn = new StaticNode(referenceTypeName, referenceTypeName.hashCode());
            _staticNodesMap.put(referenceTypeName, ssn);
        }

        ssn.addReferencingVar(name);
        Value fieldValue = reference.getValue(field);

        if (fieldValue == null || fieldValue instanceof PrimitiveValue) {
            ssn.addPrimitive(field.name(), fieldValue);
        } else if (fieldValue instanceof ArrayReference) {
            ArrayReference arrayReference = (ArrayReference) fieldValue;
            long id = arrayReference.uniqueID();
            ArrayNode arrayNode = new ArrayNode(id);
            ssn.addReference(arrayNode, field.name());
            int index = 0;

            for (Value arrayValue : arrayReference.getValues()) {
                addValueToArrayNode(arrayNode, field.name(), arrayValue, index);
                ++index;
            }
        } else if (fieldValue instanceof ObjectReference) {
            ObjectReference objectReference = (ObjectReference) fieldValue;
            long id = objectReference.uniqueID();
            ObjectNode node = new ObjectNode(id);
            ssn.addReference(node, field.name());

            for (Field f : objectReference.referenceType().fields()) {
                String fName = f.name();
                Value fValue = objectReference.getValue(f);

                if (f.isStatic()) {
                    // TODO: Handle this.
                    // drillDown(fName, objectReference);
                    // staticDrillDown(fName, objectReference, f);
                } else {
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