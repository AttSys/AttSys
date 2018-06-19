package com.uniben.attsys;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.uniben.attsys.api.AttSysApi;
import com.uniben.attsys.api.ServiceGenerator;
import com.uniben.attsys.dialogs.LoadingDialog;
import com.uniben.attsys.models.Attendance;
import com.uniben.attsys.models.User;
import com.uniben.attsys.singletons.SaveObject;
import com.uniben.attsys.models.FaceRecognitionResponse;
import com.uniben.attsys.utils.NotificationUtils;
import com.uniben.attsys.utils.PermissionHelpers;
import com.uniben.attsys.viewmodels.UserViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import id.zelory.compressor.Compressor;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FacialRecognitionActivity extends AppCompatActivity {
    public static final String ATTENDACE_KEY= "attendance";
    private String currentImagePath;
    private SweetAlertDialog loadingDialog;

    @BindView(R.id.root)
    View rootView;

    @BindView(R.id.tv_hold_on)
    TextView takePicTextView;


    private User user;
    private Attendance attendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facial_recognition);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getUserListLiveData().observe(this, users -> {
            if (users != null) {
                if(users.size() >0){
                    user = users.get(0);
                }
            }
        });

        attendance = getIntent().getParcelableExtra(ATTENDACE_KEY);
        if(PermissionHelpers.isCameraPermissionGranted(this)){
            callCamera();
        }else{
            PermissionHelpers.requestCameraPermission(this);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.tv_hold_on)
    public void takePic(){
        callCamera();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionHelpers.REQUEST_PERMISSION_CODE2) {
            if (grantResults.length <= 0) {
                PermissionHelpers.requestCameraPermission(this);
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               callCamera();
            } else {
                PermissionHelpers.requestCameraPermission(this);
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public Observer<FaceRecognitionResponse> getFacialResults() {
        return new DisposableObserver<FaceRecognitionResponse>() {
            @Override
            public void onNext(FaceRecognitionResponse faceRecognitionResponse) {
                takePicTextView.setEnabled(false);
                SaveObject.getInstance().setFaceObject(faceRecognitionResponse);
                loadingDialog.dismissWithAnimation();
                Log.v("TAG", "Message is " + faceRecognitionResponse.getMessage());
                Log.v("TAG", "Similarity is " + faceRecognitionResponse.getSimilarity());
                NotificationUtils.notifyUser(rootView, faceRecognitionResponse.getMessage());
                startActivity(new Intent(FacialRecognitionActivity.this, AttendanceTakenActivity.class));
                finish();

            }

            @Override
            public void onError(Throwable e) {
                takePicTextView.setEnabled(true);
                FaceRecognitionResponse faceRecognitionResponse = SaveObject.getInstance().getFaceRecognitionResponse();
                if (faceRecognitionResponse != null) {
                    loadingDialog.show();
                    loadingDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    loadingDialog.setContentText(faceRecognitionResponse.getMessage());
                    loadingDialog.setConfirmText("Return");
                    loadingDialog.setConfirmClickListener(sweetAlertDialog -> {
                        loadingDialog.dismissWithAnimation();
                    });
                    NotificationUtils.notifyUser(rootView, faceRecognitionResponse.getMessage());
                } else {
                    loadingDialog.show();
                    loadingDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    loadingDialog.setContentText("An error occurred, your attendance has probably been taken already");
                    loadingDialog.setConfirmText("Retry");
                    loadingDialog.setConfirmClickListener(sweetAlertDialog -> {
                        callCamera();
                    });
                    NotificationUtils.notifyUser(rootView, "Error occurred");

                    e.printStackTrace();
                }

            }

            @Override
            public void onComplete() {

            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 120){

            if ( resultCode == RESULT_OK) {
                takePicTextView.setEnabled(false);
                loadingDialog = new LoadingDialog(this, "Processing Image..");
                loadingDialog.show();
                File file = new File(currentImagePath);
                Log.v("TAG", "Original image size is " + file.length() / 1000);

                try {
                    File compressedImage = new Compressor(this).compressToFile(file);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), compressedImage);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("file", compressedImage.getName(), requestBody);
                    AttSysApi attSysApi = ServiceGenerator.createService(AttSysApi.class);
                    attSysApi.verifyPicture("Token " + user.getToken().getToken(), body, attendance.getId())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(getFacialResults());
                    Log.v("TAG", "compressed size is " + compressedImage.length() / 1000);
                } catch (IOException e) {
                    e.printStackTrace();
                    NotificationUtils.notifyUser(this, "Image too large to be compressed, retry");
                }

//            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), compressedImage);
//            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
//            AttSysApi attSysApi = ServiceGenerator.createService(AttSysApi.class);
//            attSysApi.verifyPicture("Token "+ user.getToken().getToken(), body,mAttendance.getId())
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(getFacialResults());
            }else{
                takePicTextView.setEnabled(true);
            }
        }

    }

    private void callCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.uniben.attsys.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 120);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES); //no permission needed for SDK > 18
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        currentImagePath = image.getPath();
//            Bitmap bitmap;
//            try {
//                bitmap = BitmapFactory.decodeFile(image.getPath ());
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, new FileOutputStream(image));
//                Log.v("TAG", "File Compressed");
//            }
//            catch (Throwable t) {
//                Log.v("TAG", "Error compressing file." + t.toString ());
//                t.printStackTrace ();
//            }
        //Log.v("TAG", "Old image size is " + image.length()/1000);

        return image;
    }

}