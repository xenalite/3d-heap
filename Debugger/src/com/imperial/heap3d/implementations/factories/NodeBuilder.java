package com.imperial.heap3d.implementations.factories;

import com.imperial.heap3d.implementations.snapshot.*;
import com.imperial.heap3d.interfaces.factories.INodeBuilder;
import com.imperial.heap3d.utilities.Pair;
import com.sun.jdi.*;
import com.sun.tools.jdi.ArrayReferenceImpl;

import java.util.*;

/**
 * Created by oskar on 24/11/14.
 */
public class NodeBuilder implements INodeBuilder {

    private Map<Long, Node> _uniqueNodes = new HashMap<>();
    private Map<String, StaticStackNode> _staticStackNodesMap = new HashMap<>();

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

        for (StaticStackNode ssn : _staticStackNodesMap.values()) {
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
        } else if (reference instanceof StringReference) {
            StringReference stringReference = (StringReference) reference;
            node = new StringNode(id, stringReference.toString());
            _uniqueNodes.put(id, node);
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
        return Pair.create(node, name);
    }

    private void staticDrillDown(String name, ObjectReference reference, Field field) {
        String referenceTypeName = reference.referenceType().name();
        StaticStackNode ssn;

        if (_staticStackNodesMap.containsKey(referenceTypeName)) {
            ssn = _staticStackNodesMap.get(referenceTypeName);
        } else {
            ssn = new StaticStackNode(referenceTypeName, referenceTypeName.hashCode());
            _staticStackNodesMap.put(referenceTypeName, ssn);
        }

        ssn.addReferencingVar(name);
        Value fieldValue = reference.getValue(field);

        if (fieldValue instanceof StringReference) {
            StringReference stringReference = (StringReference) fieldValue;
            long id = ((StringReference) fieldValue).uniqueID();
            Node node = new StringNode(id, stringReference.toString());
            ssn.addReference(node, field.name());
        } else if (fieldValue instanceof ObjectReference) {
            // TODO: handle this case.
        } else if (fieldValue instanceof ArrayReference) {
            // TODO: handle this case.
        } else if (fieldValue == null || fieldValue instanceof PrimitiveValue) {
            // TODO: handle this case.
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