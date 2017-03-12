package com.maxdemarzi;

import org.junit.Rule;
import org.junit.Test;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.test.server.HTTP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DimensionalSearchTest {
    @Rule
    public final Neo4jRule neo4j = new Neo4jRule()
            .withFixture(MODEL_STATEMENT)
            .withProcedure(DimensionalSearch.class);

    @Test
    public void shouldPerformDimensionalSearch() {
        HTTP.Response response = HTTP.POST(neo4j.httpURI().resolve("/db/data/transaction/commit").toString(), QUERY);
        ArrayList row = getResultRow(response);
        assertEquals(EXPECTED, row);
    }

    private static final String MODEL_STATEMENT =
            "CREATE (c1:Customer {name:'c1'})" +
            "CREATE (c2:Customer {name:'c2'})" +
            "CREATE (c3:Customer {name:'c3'})" +
            "CREATE (c4:Customer {name:'c4'})" +
            "CREATE (c5:Customer {name:'c5'})" +
            "CREATE (c6:Customer {name:'c6'})" +
            "CREATE (c7:Customer {name:'c7'})" +
            "CREATE (c8:Customer {name:'c8'})" +
            "CREATE (c9:Customer {name:'c9'})" +
            "CREATE (c10:Customer {name:'c10'})" +
            "CREATE (d1a:Dimension1 {value:'d1a'})" +
            "CREATE (d1b:Dimension1 {value:'d1b'})" +
            "CREATE (d1c:Dimension1 {value:'d1c'})" +
            "CREATE (d2a:Dimension2 {value:'d2a'})" +
            "CREATE (d2b:Dimension2 {value:'d2b'})" +
            "CREATE (d2c:Dimension2 {value:'d2c'})" +
            "CREATE (d3a:Dimension3 {value:'d3a'})" +
            "CREATE (d3b:Dimension3 {value:'d3b'})" +
            "CREATE (d3c:Dimension3 {value:'d3c'})" +
            "CREATE (d4a:Dimension4 {value:'d4a'})" +
            "CREATE (d4b:Dimension4 {value:'d4b'})" +
            "CREATE (d4c:Dimension4 {value:'d5c'})" +
            "CREATE (c1)-[:HAS]->(d1a)" +
            "CREATE (c1)-[:HAS]->(d2a)" +
            "CREATE (c1)-[:HAS]->(d3a)" +
            "CREATE (c1)-[:HAS]->(d4a)" +
            "CREATE (c2)-[:HAS]->(d1b)" +
            "CREATE (c2)-[:HAS]->(d2b)" +
            "CREATE (c2)-[:HAS]->(d3b)" +
            "CREATE (c2)-[:HAS]->(d4b)" +
            "CREATE (c3)-[:HAS]->(d1c)" +
            "CREATE (c3)-[:HAS]->(d2c)" +
            "CREATE (c3)-[:HAS]->(d3c)" +
            "CREATE (c3)-[:HAS]->(d4c)" +
            "CREATE (c4)-[:HAS]->(d1a)" +
            "CREATE (c4)-[:HAS]->(d2b)" +
            "CREATE (c4)-[:HAS]->(d3c)" +
            "CREATE (c4)-[:HAS]->(d4a)" +
            "CREATE (c5)-[:HAS]->(d1b)" +
            "CREATE (c5)-[:HAS]->(d2c)" +
            "CREATE (c5)-[:HAS]->(d3a)" +
            "CREATE (c5)-[:HAS]->(d4b)" +
            "CREATE (c6)-[:HAS]->(d1c)" +
            "CREATE (c6)-[:HAS]->(d2c)" +
            "CREATE (c6)-[:HAS]->(d3c)" +
            "CREATE (c6)-[:HAS]->(d4c)" +
            "CREATE (c7)-[:HAS]->(d1c)" +
            "CREATE (c7)-[:HAS]->(d2c)" +
            "CREATE (c7)-[:HAS]->(d3c)" +
            "CREATE (c7)-[:HAS]->(d4a)" +
            "CREATE (c8)-[:HAS]->(d1a)" +
            "CREATE (c8)-[:HAS]->(d2c)" +
            "CREATE (c8)-[:HAS]->(d3c)" +
            "CREATE (c8)-[:HAS]->(d4a)" +
            "CREATE (c9)-[:HAS]->(d1b)" +
            "CREATE (c9)-[:HAS]->(d2a)" +
            "CREATE (c9)-[:HAS]->(d3a)" +
            "CREATE (c9)-[:HAS]->(d4b)" +
            "CREATE (c10)-[:HAS]->(d1a)" +
            "CREATE (c10)-[:HAS]->(d2c)" +
            "CREATE (c10)-[:HAS]->(d3c)" +
            "CREATE (c10)-[:HAS]->(d4a)";

    private static final HashMap<String, Object> PARAMS = new HashMap<String, Object>(){{
        put("map", new HashMap<String, Object>(){{
            put("Dimension1", "d1a");
            put("Dimension2", "d2c");
            put("Dimension3", "d3c");
            put("Dimension4", "d4a");
        }});
    }};

    private static final HashMap<String, Object> QUERY = new HashMap<String, Object>(){{
        put("statements", new ArrayList<Map<String, Object>>() {{
            add(new HashMap<String, Object>() {{
                put("statement", "CALL com.maxdemarzi.dimensionalSearch({map})");
                put("parameters", PARAMS);
            }});
        }});
    }};

    private static final Map<String, Object> CUSTOMER8 = new HashMap<String, Object>(){{
        put("name", "c8");
    }};
    private static final Map<String, Object> CUSTOMER10 = new HashMap<String, Object>(){{
        put("name", "c10");
    }};

    private static final List<Map> EXPECTED = new ArrayList<Map>(){{
        add(CUSTOMER8);
        add(CUSTOMER10);
    }};

    static ArrayList getResultRow(HTTP.Response response) {
        Map actual = response.content();
        ArrayList results = (ArrayList)actual.get("results");
        HashMap result = (HashMap)results.get(0);
        ArrayList<Map> data = (ArrayList)result.get("data");
        ArrayList<Map> values = new ArrayList();
        data.forEach((value) -> values.add((Map)((ArrayList) value.get("row")).get(0)));
        return values;
    }

}
