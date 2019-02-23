package org.springframework.samples.petclinic.owner;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link PetTypeFormatter}
 *
 * @author Colin But
 */
@RunWith(MockitoJUnitRunner.class)
public class PetTypeFormatterTestsNew {

    @Mock
    private PetRepository pets;
    
    private List<PetType> petTypes;

    private PetTypeFormatter petTypeFormatter;

    @Before
    public void setup() {
        this.petTypeFormatter = new PetTypeFormatter(pets);
        //cant mock inside of thenReturn() so do it before
        this.petTypes = makePetTypes();
        when(this.pets.findPetTypes()).thenReturn(this.petTypes);
    }

    @Test
    public void testPrint() {
    	//We're not testing if the PetType class gets the proper Name, we're testing if the print method
    	//calls the getName to retrieve it. Other tests should exist to test the getName method.
        PetType petType = mock(PetType.class);
        when(petType.getName()).thenReturn("hamster");
        String petTypeName = this.petTypeFormatter.print(petType, Locale.ENGLISH);
        verify(petType).getName();
        assertEquals(petTypeName, "hamster");
    }
    
    @Test
    public void shouldParseFirstItem() throws ParseException {
    	PetType petType = petTypeFormatter.parse("Dog", Locale.ENGLISH);
        assertEquals("Dog", petType.getName());
    }

    @Test
    public void shouldParseSecondItem() throws ParseException {
        PetType petType = petTypeFormatter.parse("Bird", Locale.ENGLISH);
        assertEquals("Bird", petType.getName());
    }

    @Test(expected = ParseException.class)
    public void shouldThrowParseException() throws ParseException {
        petTypeFormatter.parse("Fish", Locale.ENGLISH);
    }

    /**
     * Helper method to produce some sample pet types just for test purpose
     *
     * @return {@link Collection} of {@link PetType}
     */
    private List<PetType> makePetTypes() {
        List<PetType> petTypes = new ArrayList<>();
        PetType dog = mock(PetType.class);
        PetType bird = mock(PetType.class);
        when(dog.getName()).thenReturn("Dog");
        when(bird.getName()).thenReturn("Bird");
        
        petTypes.add(dog);
        petTypes.add(bird);
        
        return petTypes;
    }

}
