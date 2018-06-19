package com.uniben.attsys.singletons;

import com.uniben.attsys.models.FaceRecognitionResponse;

/**
 * Created by ILENWABOR DAVID on 14/06/2018.
 */

public class SaveObject {
    private static final SaveObject ourInstance = new SaveObject();
    private FaceRecognitionResponse faceRecognitionResponse;

    public static SaveObject getInstance() {
        return ourInstance;
    }

    private SaveObject() {
    }

    public void setFaceObject(FaceRecognitionResponse faceRecognitionResponse){

        this.faceRecognitionResponse = faceRecognitionResponse;
    }

    public FaceRecognitionResponse getFaceRecognitionResponse() {
        return faceRecognitionResponse;
    }
}
