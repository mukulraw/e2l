package com.mrtecks.e2l;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Strong {
    @SerializedName("topic")
    @Expose
    private String topic;

    @SerializedName("marks")
    @Expose
    private String marks;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }
}
