package main;

import java.util.ArrayList;

public class GetPeoples {
    private int numRows;
    private int pageSize;
    private int currentPage;
    private ArrayList<People> peoples;

    public GetPeoples(){}

    public GetPeoples(int numRows, int pageSize, int currentPage, ArrayList<People> peoples){
        this.numRows = numRows;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.peoples = peoples;
    }

    public int getNumRows(){return numRows;}
    public int getPageSize(){return pageSize;}
    public int getCurrentPage(){return currentPage;}
    public ArrayList<People> getPeoples(){return peoples;}
}
