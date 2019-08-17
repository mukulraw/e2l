package com.mrtecks.e2l;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mark {

    @SerializedName("topic_name")
    @Expose
    private String topicName;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("marks")
    @Expose
    private String marks;
    @SerializedName("yans")
    @Expose
    private String yans;
    @SerializedName("rans")
    @Expose
    private String rans;
    @SerializedName("atype")
    @Expose
    private String atype;

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getYans() {
        return yans;
    }

    public void setYans(String yans) {
        this.yans = yans;
    }

    public String getRans() {
        return rans;
    }

    public void setRans(String rans) {
        this.rans = rans;
    }

    public String getAtype() {
        return atype;
    }

    public void setAtype(String atype) {
        this.atype = atype;
    }


}
