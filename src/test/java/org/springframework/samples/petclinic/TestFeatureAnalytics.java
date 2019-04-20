package org.springframework.samples.petclinic;

import static org.junit.Assert.*;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.time.LocalDate;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.samples.petclinic.features.AssignRandomFeatures;
import org.springframework.samples.petclinic.features.FeatureToggle;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Test class for {@link FeatureToggle}
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(FeatureToggle.class)
public class TestFeatureAnalytics {

    @Mock
    Visit visit1;

    @Mock
    Visit visit2;

    @Mock
    Map<String, Object> model;

    @Mock
    FeatureToggle featureToggle;

    @Mock
    VisitRepository visits;

    @Before
    public void setUp() throws Exception {
        visit1 = new Visit();
        visit1.setPetId(1);
        visit1.setDescription("Visit description");
        visit1.setDate(LocalDate.now());
        model.put("visit", visit1);

        visit2 = new Visit();
        visit2.setPetId(2);
        visit2.setDescription("Visit description");
        visit2.setDate(LocalDate.now());

        visits.save(visit1);
        visits.save(visit2);

        featureToggle = new FeatureToggle(visits);
    }

    @Test
    public void testRollbackFeatureA() {
        // feature A is off
        FeatureToggle.setFeatureA(false);
        assertEquals("error", featureToggle.visitFeatureA(model, visit1.getPetId()));

        // feature A is on
        FeatureToggle.setFeatureA(true);
        assertEquals("", featureToggle.visitFeatureA(model, visit1.getPetId()));

        // rollback feature A
        FeatureToggle.setFeatureA(false);
        assertEquals("error", featureToggle.visitFeatureA(model, visit1.getPetId()));
    }

    @Test
    public void testRollbackFeatureB() {
        // feature B is off
        FeatureToggle.setFeatureB(false);

        assertEquals("error", featureToggle.visitFeatureB(model, visit2.getPetId()));

        // feature B is on
        FeatureToggle.setFeatureB(true);

        assertEquals("pets/createOrUpdateVisitForm", featureToggle.visitFeatureB(model, visit2.getPetId()));

        // rollback feature B
        FeatureToggle.setFeatureB(false);

        assertEquals("error", featureToggle.visitFeatureB(model, visit2.getPetId()));

    }

    @Test
    public void testRandom() {
        int iterations = 1000;

        AssignRandomFeatures random = new AssignRandomFeatures();

        for (int i = 0; i < iterations; i++) {
            FeatureToggle featureToggle = new FeatureToggle(visits);

            // rollout number of users who see feature A
            FeatureToggle.setFeatureA(random.getFeatureA(50));
            FeatureToggle.setFeatureA(true);


            // rollout number of users who see feature B
            FeatureToggle.setFeatureB(random.getFeatureB(75));
            FeatureToggle.setFeatureB(true);

        }
    }

    @Test
    public void fakeCollectedData() {
        int iterations = 500;

        Logger logger = LogManager.getLogger("FeatureToggle");


        for (int i = 0; i < iterations; i++) {
            int randomVisitID = ThreadLocalRandom.current().nextInt(1, 1000000000);
            if(ThreadLocalRandom.current().nextBoolean())logger.info(String.format("B, %d",randomVisitID));
            else logger.info(String.format("A, %d",randomVisitID));


        }
    }
}
