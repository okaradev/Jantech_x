package com.krypt.Hubble.HUBBLEMODELS;

public class FeedbackModel {
    String id;
    String comment;
    String reply;

    public FeedbackModel(String id, String comment, String reply) {
        this.id = id;
        this.comment = comment;
        this.reply = reply;
    }

    public String getId() {
        return id;
    }

    public FeedbackModel(String comment, String reply) {
        this.comment = comment;
        this.reply = reply;

    }

    public String getComment() {
        return comment;
    }

    public String getReply() {
        return reply;
    }

}
