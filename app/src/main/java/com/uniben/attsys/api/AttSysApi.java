package com.uniben.attsys.api;



import com.uniben.attsys.models.FaceRecognitionResponse;
import com.uniben.attsys.models.Student;
import com.uniben.attsys.models.Token;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Cyberman on 5/30/2018.
 */

public interface AttSysApi {
    @FormUrlEncoded
    @POST("login/")
    Observable<Token> authUser(@Field("username") String username,
                               @Field("password") String password);

    @GET("student/")
    Observable<Student> getStudent(@Header("Authorization") String token);


    @Multipart
    @POST("verify_pic/")
    Observable<FaceRecognitionResponse> verifyPicture(@Header("Authorization") String token,
                                                      @Part("file") MultipartBody.Part file,
                                                      @Part("pk") int id);

}
