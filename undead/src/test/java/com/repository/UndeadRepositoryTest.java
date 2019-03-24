package com.repository;

import static org.junit.Assert.*;

import com.undeadcrud.domain.Undead;
import com.undeadcrud.repository.UndeadRepository;
import com.undeadcrud.repository.UndeadRepositoryFactory;
import com.undeadcrud.repository.UndeadRepositoryImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.SQLException;

@RunWith(JUnit4.class)
public class UndeadRepositoryTest {

    private UndeadRepository undeadRepository;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void initRepository() {
        undeadRepository = UndeadRepositoryFactory.getInstance();
        Undead szwedacz = new Undead("Szkielet Wojownik", "Szkielet", 5, "Cmentarz", "Demoralizacja");
        Undead ghoul = new Undead("Szkielet Wojownik", "Szkielet", 5, "Cmentarz", "Demoralizacja");
        Undead ozywieniec = new Undead("Szkielet Wojownik", "Szkielet", 5, "Cmentarz", "Demoralizacja");
        Undead lisz = new Undead("Szkielet Wojownik", "Szkielet", 5, "Cmentarz", "Demoralizacja");

//        szwedacz.setId(1);
//        szwedacz.setTyp("Szwedacz");
//
//        ghoul.setId(2);
//        ghoul.setTyp("Ghoul");
//
//        ozywieniec.setId(3);
//        ozywieniec.setTyp("Ozywieniec");
//
//        lisz.setId(4);
//        lisz.setTyp("Lisz");

        undeadRepository.addUndead(szwedacz);
        undeadRepository.addUndead(ghoul);
        undeadRepository.addUndead(ozywieniec);
        undeadRepository.addUndead(lisz);
    }

    @Before
    public void setup() throws SQLException {
        undeadRepository  = new UndeadRepositoryImpl();
    }

    @After
    public void cleanup() throws SQLException {
    }

    @Test
    public void getAll() throws Exception {
        assertNotNull(undeadRepository.getAll());
    }

    @Test
    public void getUndeadById() throws Exception {

        Undead zombie = new Undead("Szkielet Wojownik", "Szkielet", 5, "Cmentarz", "Demoralizacja");
        zombie.setId(10);
        zombie.setTyp("Zgnilec");
        undeadRepository.addUndead(zombie);
        assertNotNull(undeadRepository.getUndeadById(zombie.getId()));
        undeadRepository.getConnection().close();
    }

    @Test
    public void addUndead() throws Exception {
        Undead zombie = new Undead("Szkielet Wojownik", "Szkielet", 5, "Cmentarz", "Demoralizacja");
        zombie.setId(10);
        zombie.setTyp("Zgnilec");
        undeadRepository.addUndead(zombie);
        assertNotNull(undeadRepository.getUndeadById(zombie.getId()));
        undeadRepository.deleteUndead(zombie);
    }

    @Test
    public void deleteUndead() throws Exception {
        Undead zombie = undeadRepository.getUndeadById(1);
        undeadRepository.deleteUndead(zombie);
        ////if (undeadRepository.getAll().size() > 0) {
        //    assertNotNull(undeadRepository.getAll());
        ///} else {
        assertNull(undeadRepository.getUndeadById(zombie.getId()));
       // }
    }

    @Test
    public void checkConnection() {
        assertNotNull(undeadRepository.getConnection());
    }


    @Test
    public void updateUndead() throws Exception {
        Undead wywloka = new Undead("Szkielet Wojownik", "Szkielet", 5, "Cmentarz", "Demoralizacja");
        wywloka.setTyp("wywloka");
        int zombieToUpdate = 1;
        undeadRepository.updateUndead(1, wywloka);
        assertEquals(1, undeadRepository.getUndeadById(zombieToUpdate).getId());

        for (Undead undead : undeadRepository.getAll()) {
            if (wywloka.getId() == zombieToUpdate) {
                //if(wywloka.getId().equals((long)zombieToUpdate)) {
                assertNotEquals(undead.getTyp(), wywloka.getTyp());
            }
        }
    }


}