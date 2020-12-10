package main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class People {
    private static final Logger logger = LogManager.getLogger();

    private int id;
    private String firstName;
    private String lastName;
    LocalDate dateOfBirth;
    private int age;
    private LocalDateTime lastModified;

    public People() {
    }

    public People(int id, String firstName, String lastName, LocalDate dateOfBirth, int age, LocalDateTime lastModified){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.lastModified = lastModified;

        //logger.debug("CREATING " + firstName + " " + lastName);
    }

    // throw an exception if anything bad happens during save process
    // wrap all other exceptions in a widget exception (unchecked so caller is NOT required to try/catch it)
    public void save() throws PeopleException {
        try {
            logger.debug("Saving Person");
        } catch(Exception e) {
            logger.error(e);
            throw new PeopleException(e);
        }
    }

    // validators

    //accessors
    public String getFirstName(){ return firstName; }

    //TODO: Add validator
    public String setFirstName(String firstName){ return this.firstName = firstName; }

    public String getLastName(){ return lastName; }

    //TODO: Add validator
    public String setLastName(String lastName){ return this.lastName = lastName; }

    public int getAge(){ return age; }

    //TODO: Add validator
    public int setAge(int age){ return this.age = age; }

    public LocalDate getDateOfBirth(){ return dateOfBirth; }

    //TODO: Add validator
    public LocalDate setLocalDate(LocalDate dateOfBirth){ return this.dateOfBirth = dateOfBirth; }

    public int getId(){ return id; }

    //TODO: Add validator
    public int setId(int id){ return this.id = id; }

    public LocalDateTime getLastModified(){ return lastModified;}

    @Override
    public String toString() {
        return firstName +" "+ lastName;
    }
}
