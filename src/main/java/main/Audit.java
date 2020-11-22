package main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;

public class Audit {
    private static final Logger logger = LogManager.getLogger();

    private String change_msg;
    private String changed_by;
    private java.sql.Timestamp when_occurred;
    private int person_id;

    public Audit() {
    }

    public Audit(String change_msg, String changed_by, java.sql.Timestamp when_occurred, int person_id){
        this.change_msg = change_msg;
        this.changed_by = changed_by;
        this.when_occurred = when_occurred;
        this.person_id = person_id;

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
    public String getChange_msg(){ return change_msg; }

    //TODO: Add validator
    public String setChange_msg(String change_msg){ return this.change_msg = change_msg; }

    public String getChanged_by(){ return changed_by; }

    //TODO: Add validator
    public String setChanged_by(String changed_by){ return this.changed_by = changed_by; }

    public int getPerson_id(){ return person_id; }

    //TODO: Add validator
    public int setPerson_id(int person_id){ return this.person_id = person_id; }

    public java.sql.Timestamp getWhen_occurred(){ return when_occurred; }

    //TODO: Add validator
    public java.sql.Timestamp getWhen_occurred(java.sql.Timestamp when_occurred){ return this.when_occurred = when_occurred; }

    @Override
    public String toString() {
        return change_msg +" "+ changed_by;
    }
}