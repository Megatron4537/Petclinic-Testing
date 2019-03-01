package org.springframework.samples.petclinic.owner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;


/**
 * Test class for {@link Owner}
 *
 * @author Nicolas Horta-Adam
 */
@RunWith(MockitoJUnitRunner.class)
public class OwnerTests {


    private Owner george;
    private Owner julia;

    @Mock
    private Pet fido = mock(Pet.class);

    @Before
    public void setup() {

        when(fido.isNew()).thenReturn(true);
        when(fido.getName()).thenReturn("Fido");

        george = new Owner();
        george.setId(1);
        george.setFirstName("George");

        julia = new Owner();
        julia.setId(2);
        julia.setFirstName("Julia");
        julia.addPet(fido);

    }

    @Test
    public void georgesPetShouldReturnNull() {

        Pet georgesDog = george.getPet("Fido",false);

        assertEquals(null,georgesDog);
    }


    @Test
    public void getOwnersPetsIncludingNewPets() {

        Pet juliasPet = julia.getPet("Fido",false);

        assertEquals("Fido",juliasPet.getName());
    }

    @Test
    public void getOwnerPetsExcludingNewPets() {

        Pet juliasPet = julia.getPet("Fido",true);

        assertEquals(null,juliasPet);
    }
}
