package com.mrtecks.e2l.singleTopicPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("topic_name")
    @Expose
    private String topicName;
    @SerializedName("module")
    @Expose
    private String module;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("video")
    @Expose
    private String video;
    @SerializedName("atype")
    @Expose
    private String atype;
    @SerializedName("option1")
    @Expose
    private String option1;
    @SerializedName("option2")
    @Expose
    private String option2;
    @SerializedName("option3")
    @Expose
    private String option3;
    @SerializedName("option4")
    @Expose
    private String option4;
    @SerializedName("answer")
    @Expose
    private String answer;
    @SerializedName("yanswer")
    @Expose
    private String yanswer;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("filename")
    @Expose
    private String filename;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("level")
    @Expose
    private String level;
    @SerializedName("mark")
    @Expose
    private String mark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getAtype() {
        return atype;
    }

    public void setAtype(String atype) {
        this.atype = atype;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getYanswer() {
        return yanswer;
    }

    public void setYanswer(String yanswer) {
        this.yanswer = yanswer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLevel() {
        return level;
    }

    public String getMark() {
        return mark;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
