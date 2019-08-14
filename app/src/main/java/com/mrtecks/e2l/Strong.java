package com.mrtecks.e2l;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Strong {
    @SerializedName("topic")
    @Expose
    private String topic;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
