package org.javaee6.sample;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PersonDatabase {

    List<Person> persons;

    @PostConstruct
    public void init() {
    	System.out.println("PersonDatabase Init called...");
        persons = Arrays.asList(
                new Person("Penny"), 
                new Person("Leonard"), 
                new Person("Sheldon"), 
                new Person("Amy"), 
                new Person("Howard"), 
                new Person("Bernadette"), 
                new Person("Raj"), 
                new Person("Priya"));
    }

    public Person[] currentList() {
        return persons.toArray(new Person[0]);
    }

    public Person getPerson(int id) throws Exception {
        if (id < persons.size()) {
            return persons.get(id);
        }

        throw new Exception("Person with id \"" + id + "\" not found.");
    }
}
