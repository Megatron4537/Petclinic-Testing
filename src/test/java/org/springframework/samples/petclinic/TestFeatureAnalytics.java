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
import org.springframework.samples.petclinic.features.FeatureToggle;
import org.springframework.samples.petclinic.visit.Visit;
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
        FeatureToggle.featureA = false;
        assertEquals("error", featureToggle.visitFeatureA(model, visit1.getPetId()));

        FeatureToggle.featureA = true;
        assertEquals("", featureToggle.visitFeatureA(model, visit1.getPetId()));
    }

    @Test
    public void testRollbackFeatureB() {
        FeatureToggle.featureB = false;
        assertEquals("error", featureToggle.visitFeatureA(model, visit2.getPetId()));

        FeatureToggle.featureB = true;
        assertEquals("", featureToggle.visitFeatureB(model, visit2.getPetId()));

    }
}
