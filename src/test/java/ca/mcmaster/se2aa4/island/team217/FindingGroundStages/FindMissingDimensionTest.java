package ca.mcmaster.se2aa4.island.team217.FindingGroundStages;

import org.junit.jupiter.api.Test;

import ca.mcmaster.se2aa4.island.team217.GridSearchStages.ScanAndFly;
import ca.mcmaster.se2aa4.island.team217.MapRepresentation.MapRepresenter;
import ca.mcmaster.se2aa4.island.team217.Drone;
import ca.mcmaster.se2aa4.island.team217.ResponseStorage;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class FindMissingDimensionTest {

        FindMissingDimension find;
        MapInitializer map;

        @BeforeEach
        void initialize() {
                map = new MapInitializer(new Drone(1000, "N",
                                new MapRepresenter()),
                                new MapRepresenter());
                find = new FindMissingDimension(map);
        }

        @Test
        void testReachedEndCase1() {

                find.nextDecision(new Drone(1000, "N",
                                new MapRepresenter()), new MapRepresenter());

                assertEquals(false, find.reachedEnd());

        }

        @Test
        void testReachedEndCase2() {

                find.foundDimension = true;

                find.nextDecision(new Drone(1000, "N",
                                new MapRepresenter()), new MapRepresenter());

                assertEquals(true, find.reachedEnd());

        }

        @Test
        void testGetNextPhase() {
                assertEquals(ScanAndFly.class, find.getNextPhase().getClass());

        }

        @Test
        void testIsFinal() {
                assertEquals(false, find.isFinal());

        }

        @Test
        void testNextDecisionCase1() {
                Drone drone = new Drone(1000, "N",
                                new MapRepresenter());

                find.foundDimension = true;

                assertEquals(drone.scan(),
                                find.nextDecision(drone,
                                                new MapRepresenter()));

                assertEquals(true, find.reachedEnd);

        }

        @Test
        void testNextDecisionCase2() {
                Drone drone = new Drone(1000, "N",
                                new MapRepresenter());

                find.foundDimension = false;

                find.counter = 0;

                assertEquals(drone.fly(),
                                find.nextDecision(drone,
                                                new MapRepresenter()));
        }

        @Test
        void testNextDecisionCase3() {
                Drone drone = new Drone(1000, "N",
                                new MapRepresenter());

                find.foundDimension = false;
                find.counter = 1;

                assertEquals(drone.echo(drone
                                .getCurrentHeading()),
                                find.nextDecision(drone,
                                                new MapRepresenter()));

        }

        @Test
        void testProcessResponse() {
                MapInitializer mapInitializer = new MapInitializer(new Drone(1000, "N", new MapRepresenter()),
                                new MapRepresenter());
                FindMissingDimension instance = new FindMissingDimension(mapInitializer);
                ResponseStorage responseStorage = new ResponseStorage();
                Drone drone = new Drone(1000, "N", new MapRepresenter());
                MapRepresenter map = new MapRepresenter();
                responseStorage.setFound("OUT_OF_RANGE");
                responseStorage.setRange(0);
                drone.decisionTaken("echo", "E");
                mapInitializer.topRows = 0;
                mapInitializer.bottomRows = 0;
                mapInitializer.rightColumns = 52;
                mapInitializer.leftColumns = 0;
                instance.processResponse(responseStorage, drone, map);
                assertEquals(true, instance.foundDimension);
        }

}
