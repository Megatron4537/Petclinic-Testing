package org.springframework.samples.petclinic;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

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

        featureToggle = mock(FeatureToggle.class);
    }

    @Test
    public void testRollbackFeatureA() {
        // feature A is off
        FeatureToggle.featureA = false;
        assertEquals("error", featureToggle.visitFeatureA(model, visit1.getPetId()));

        // feature A is on
        FeatureToggle.featureA = true;
        assertEquals("", featureToggle.visitFeatureA(model, visit1.getPetId()));

        // rollback feature A
        FeatureToggle.featureA = false;
        assertEquals("error", featureToggle.visitFeatureA(model, visit1.getPetId()));
    }

    @Test
    public void testRollbackFeatureB() {
        // feature B is off
        FeatureToggle.featureB = false;
        assertEquals("error", featureToggle.visitFeatureA(model, visit2.getPetId()));

        // feature B is on
        FeatureToggle.featureB = true;
        assertEquals("", featureToggle.visitFeatureB(model, visit2.getPetId()));

        // rollback feature B
        FeatureToggle.featureB = false;
        assertEquals("error", featureToggle.visitFeatureB(model, visit2.getPetId()));

    }

    @Test
    public void testRandom() {
        int iterations = 1000;

        AssignRandomFeatures random = new AssignRandomFeatures();

        for (int i = 0; i < iterations; i++) {
            FeatureToggle featureToggle = new FeatureToggle(visits);

            // rollout number of users who see feature A
            FeatureToggle.featureA = random.getFeatureA(50);
            FeatureToggle.featureA = true;

            // rollout number of users who see feature B
            FeatureToggle.featureB = random.getFeatureB(75);
            FeatureToggle.featureB = true;
        }
    }
}
