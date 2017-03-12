# dimensional_search
POC searching nodes with multiple dimensions


# Instructions

1. Build it:

        mvn clean package

2. Copy target/dimensional-search-1.0-SNAPSHOT.jar to the plugins/ directory of your Neo4j server.

3. (Re)Start Neo4j server.
        
4. Create some data:

        CREATE (c1:Customer {name:'c1'})
        CREATE (c2:Customer {name:'c2'})
        CREATE (c3:Customer {name:'c3'})
        CREATE (c4:Customer {name:'c4'})
        CREATE (c5:Customer {name:'c5'})
        CREATE (c6:Customer {name:'c6'})
        CREATE (c7:Customer {name:'c7'})
        CREATE (c8:Customer {name:'c8'})
        CREATE (c9:Customer {name:'c9'})
        CREATE (c10:Customer {name:'c10'})
        CREATE (d1a:Dimension1 {value:'d1a'})
        CREATE (d1b:Dimension1 {value:'d1b'})
        CREATE (d1c:Dimension1 {value:'d1c'})
        CREATE (d2a:Dimension2 {value:'d2a'})
        CREATE (d2b:Dimension2 {value:'d2b'})
        CREATE (d2c:Dimension2 {value:'d2c'})
        CREATE (d3a:Dimension3 {value:'d3a'})
        CREATE (d3b:Dimension3 {value:'d3b'})
        CREATE (d3c:Dimension3 {value:'d3c'})
        CREATE (d4a:Dimension4 {value:'d4a'})
        CREATE (d4b:Dimension4 {value:'d4b'})
        CREATE (d4c:Dimension4 {value:'d5c'})
        CREATE (c1)-[:HAS]->(d1a)
        CREATE (c1)-[:HAS]->(d2a)
        CREATE (c1)-[:HAS]->(d3a)
        CREATE (c1)-[:HAS]->(d4a)
        CREATE (c2)-[:HAS]->(d1b)
        CREATE (c2)-[:HAS]->(d2b)
        CREATE (c2)-[:HAS]->(d3b)
        CREATE (c2)-[:HAS]->(d4b)
        CREATE (c3)-[:HAS]->(d1c)
        CREATE (c3)-[:HAS]->(d2c)
        CREATE (c3)-[:HAS]->(d3c)
        CREATE (c3)-[:HAS]->(d4c)
        CREATE (c4)-[:HAS]->(d1a)
        CREATE (c4)-[:HAS]->(d2b)
        CREATE (c4)-[:HAS]->(d3c)
        CREATE (c4)-[:HAS]->(d4a)
        CREATE (c5)-[:HAS]->(d1b)
        CREATE (c5)-[:HAS]->(d2c)
        CREATE (c5)-[:HAS]->(d3a)
        CREATE (c5)-[:HAS]->(d4b)
        CREATE (c6)-[:HAS]->(d1c)
        CREATE (c6)-[:HAS]->(d2c)
        CREATE (c6)-[:HAS]->(d3c)
        CREATE (c6)-[:HAS]->(d4c)
        CREATE (c7)-[:HAS]->(d1c)
        CREATE (c7)-[:HAS]->(d2c)
        CREATE (c7)-[:HAS]->(d3c)
        CREATE (c7)-[:HAS]->(d4a)
        CREATE (c8)-[:HAS]->(d1a)
        CREATE (c8)-[:HAS]->(d2c)
        CREATE (c8)-[:HAS]->(d3c)
        CREATE (c8)-[:HAS]->(d4a)
        CREATE (c9)-[:HAS]->(d1b)
        CREATE (c9)-[:HAS]->(d2a)
        CREATE (c9)-[:HAS]->(d3a)
        CREATE (c9)-[:HAS]->(d4b)
        CREATE (c10)-[:HAS]->(d1a)
        CREATE (c10)-[:HAS]->(d2c)
        CREATE (c10)-[:HAS]->(d3c)
        CREATE (c10)-[:HAS]->(d4a)
        
5. Call the procedure:
        
        CALL com.maxdemarzi.dimensionalSearch({Dimension1:"d1a",Dimension2:"d2c",Dimension3:"d3c",Dimension4:"d4a"})
        
6. For larger graphs, create a proper Schema:
        
        CREATE INDEX ON :Dimension1(value);
        CREATE INDEX ON :Dimension2(value);
        CREATE INDEX ON :Dimension3(value);
        CREATE INDEX ON :Dimension4(value);