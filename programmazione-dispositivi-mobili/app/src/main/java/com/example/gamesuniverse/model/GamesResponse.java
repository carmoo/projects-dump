package com.example.gamesuniverse.model;

import java.util.List;

public class GamesResponse {
    private int count;
    private String next;
    private String previous;
    private List<Game> results;

    public GamesResponse() {
    }

    public GamesResponse(int count, String next, String previous, List<Game> results) {
        this.count = count;
        this.next = next;
        this.previous = previous;
        this.results = results;
    }

    public int getCount() {
        return count;
    }

    public String getNext() {
        return next;
    }

    public String getPrevious() {
        return previous;
    }

    public List<Game> getResults() {
        return results;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public void setResults(List<Game> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "GamesResponse{" +
                "count=" + count +
                ", next='" + next + '\'' +
                ", previous='" + previous + '\'' +
                ", results=" + results +
                '}';
    }
}
