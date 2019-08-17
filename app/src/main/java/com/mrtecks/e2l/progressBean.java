package com.mrtecks.e2l;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class progressBean {
    @SerializedName("attempted")
    @Expose
    private String attempted;
    @SerializedName("correct")
    @Expose
    private String correct;
    @SerializedName("incorrect")
    @Expose
    private String incorrect;
    @SerializedName("average")
    @Expose
    private String average;
    @SerializedName("max")
    @Expose
    private String max;
    @SerializedName("strong")
    @Expose
    private List<Strong> strong = null;
    @SerializedName("weak")
    @Expose
    private List<Strong> weak = null;
    @SerializedName("marks")
    @Expose
    private List<Mark> marks = null;

    public String getAttempted() {
        return attempted;
    }

    public void setAttempted(String attempted) {
        this.attempted = attempted;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public String getIncorrect() {
        return incorrect;
    }

    public void setIncorrect(String incorrect) {
        this.incorrect = incorrect;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public List<Strong> getStrong() {
        return strong;
    }

    public void setStrong(List<Strong> strong) {
        this.strong = strong;
    }

    public List<Strong> getWeak() {
        return weak;
    }

    public void setWeak(List<Strong> weak) {
        this.weak = weak;
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public void setMarks(List<Mark> marks) {
        this.marks = marks;
    }
}
