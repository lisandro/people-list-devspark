package com.lopez.espada.falconi.people_list_devspark;

import java.util.List;

/**
 * Created by gabriel on 31/07/15.
 */
public interface PersonDAO {
    Person savePerson(Person person);

    List<Person> getAllPersons();

    void updatePerson(Person person);
}
