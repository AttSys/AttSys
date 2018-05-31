package com.uniben.attsys.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Cyberman on 5/31/2018.
 */

public class FaceRecognitionResponse {
    @SerializedName("similarity")
    private String similarity;

    @SerializedName("message")
    private String message;


    @SerializedName("errors")
    private List<String> errorsList;


    public String getSimilarity() {
        return similarity;
    }

    public void setSimilarity(String similarity) {
        this.similarity = similarity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getErrorsList() {
        return errorsList;
    }

    public void setErrorsList(List<String> errorsList) {
        this.errorsList = errorsList;
    }
}
