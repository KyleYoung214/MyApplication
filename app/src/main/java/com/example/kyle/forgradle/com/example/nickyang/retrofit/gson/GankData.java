package com.example.kyle.forgradle.com.example.kyle.retrofit.gson;

import java.util.List;

/**
 * Created by nick.yang on 2017/7/19.
 */

public class GankData {
    public boolean error;
    public List<Result> results;

    @Override
    public String toString() {
        String resultsStr = "";
        for (Result result : results) {
            resultsStr += result.toString() + "\n---------------------\n";
        }
        return "error: " + error + "\n" + resultsStr;
    }
}
