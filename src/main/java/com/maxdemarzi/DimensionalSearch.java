package com.maxdemarzi;

import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.*;
import org.neo4j.kernel.api.exceptions.EntityNotFoundException;
import org.neo4j.logging.Log;
import org.neo4j.procedure.*;
import org.roaringbitmap.IntConsumer;
import org.roaringbitmap.RoaringBitmap;

import java.util.*;
import java.util.stream.Stream;

public class DimensionalSearch {
    // This field declares that we need a GraphDatabaseService
    // as context when any procedure in this class is invoked
    @Context
    public GraphDatabaseService db;

    // This gives us a log instance that outputs messages to the
    // standard log, normally found under `data/log/console.log`
    @Context
    public Log log;

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Description("com.maxdemarzi.dimensionalSearch(map) | Find Distinct Customers by many dimensions")
    @Procedure(name = "com.maxdemarzi.dimensionalSearch", mode = Mode.READ)
    public Stream<NodeResult> performDimensionalSearch(@Name("map") Map input) throws EntityNotFoundException {
        // Find the dimensional attributes
        ArrayList<Node> attributes = new ArrayList<>();
        HashSet<Node> attributeSet = new HashSet<>();
        Iterator it = input.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Node attribute = db.findNode(Label.label((String)pair.getKey()), "value", pair.getValue());
            attributes.add(attribute);
        }
        attributeSet.addAll(attributes);

        // Sort by lowest degree
        attributes.sort(Comparator.comparingInt(Node::getDegree));

        // Get the lowest set
        RoaringBitmap nodeIds = new RoaringBitmap();
        Node firstAttribute = attributes.remove(0);
        for (Relationship r : firstAttribute.getRelationships(Direction.INCOMING)) {
            nodeIds.add(((Number)r.getStartNode().getId()).intValue());
        }
        // AND the next lowest sets until a limit is found
        RoaringBitmap nextNodeIds = new RoaringBitmap();
        it = attributes.iterator();
        while (it.hasNext()){
            Node attribute = (Node)it.next();
            for (Relationship r : attribute.getRelationships(Direction.INCOMING)) {
                nextNodeIds.add(((Number)r.getStartNode().getId()).intValue());
            }
            nodeIds.and(nextNodeIds);
            attributes.remove(attribute);
            // break if cardinality is less than x
            if (nodeIds.getCardinality() < 100 ) {
                break;
            }
        }

        // Get nodes for the remaining set
        ArrayList<Node> remaining = new ArrayList<>();
        nodeIds.forEach((IntConsumer) id -> {
            remaining.add(db.getNodeById((long)id));
        });

        // For the rest of these traverse.
        Iterator<Node> iterator = remaining.iterator();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            for (Relationship r : node.getRelationships(Direction.OUTGOING)) {
                if(!attributeSet.contains(r.getEndNode())) {
                    iterator.remove();
                    break;
                }
            }
        }

        return remaining.stream().map(NodeResult::new);
    }
}
