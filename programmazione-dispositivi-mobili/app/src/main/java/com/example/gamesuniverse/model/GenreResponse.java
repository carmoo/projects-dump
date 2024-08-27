package com.example.gamesuniverse.model;

import java.util.List;

public class GenreResponse {
    private int count;
    private String next;
    private String previous;
    private List<Genre> results;

    public GenreResponse() {
    }

    public GenreResponse(int count, String next, String previous, List<Genre> results) {
        this.count = count;
        this.next = next;
        this.previous = previous;
        this.results = results;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<Genre> getResults() {
        return results;
    }

    public void setResults(List<Genre> results) {
        this.results = results;
    }
}
