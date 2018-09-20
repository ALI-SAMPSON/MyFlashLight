package com.io.zentechhotelbooker.my_flash_light;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class FlashLightActivity extends AppCompatActivity {

    // Creating a ToggleButton
    private ToggleButton toggleButton;

    private CameraManager mCameraManager;
    private String mCameraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_light);

        boolean isFlashAvailable = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if(!isFlashAvailable){
            // display message if device has no flash
            showNoFlashError();
            //Toast.makeText(FlashLightActivity.this, getString(R.string.flash_not_available),Toast.LENGTH_LONG).show();
        }

        // getting the cameraManager and camera Id
        mCameraManager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
        }
        catch (Exception e){
            e.printStackTrace();
        }

        // Assigning id to toggleButton
        toggleButton = findViewById(R.id.toggleButton);

        // called when the status button is called
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //we will call this method to switch the flash
                switchFlashLight(isChecked);
            }
        });

    }


    // Display error message and closes app if device has no flash
    public void showNoFlashError(){
       // AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Oops!");
        builder.setMessage(getString(R.string.flash_not_available));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // finishes the activity (closes the application)
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void switchFlashLight(boolean status){
        try {
            mCameraManager.setTorchMode(mCameraId,status);
        }
        catch (CameraAccessException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // closes application
        AlertDialog.Builder builder = new AlertDialog.Builder(FlashLightActivity.this);
        builder.setTitle(getString(R.string.exit_title));
        builder.setMessage(getString(R.string.exit_message));
        builder.setCancelable(false);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
