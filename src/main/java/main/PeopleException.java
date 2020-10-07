package main;

public class PeopleException extends RuntimeException {
    public PeopleException(Exception e) {
        super(e);
    }

    public PeopleException(String msg) {
        super(msg);
    }
}
