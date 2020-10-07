package main;

public class PeopleParameters {
    private static People peopleParm;

    public static People getPeopleParm() { return peopleParm; }

    public static void setPeopleParm(People peopleParm){ PeopleParameters.peopleParm = peopleParm; }
}
