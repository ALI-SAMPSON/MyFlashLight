package com.io.zentechhotelbooker.my_flash_light;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class FlashLightActivity extends AppCompatActivity {

    // Creating a ToggleButton
    private ToggleButton mToggleButton;

    // Creating a CameraManager
    private CameraManager mCameraManager;

    /* Creating a String to CameraId
    since phones have multiple cameras*/
    private String mCameraId;

    private boolean doublePressBackToExitApp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_light);

        boolean isFlashAvailable = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        // if flash is not available on phone then it displays error message
        if(!isFlashAvailable){
            // Method call to display error message if device does not have flash
            showNoFlashError();
        }

        // instantiating the CameraManager
        mCameraManager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try {
            // getting the id of the camera from phone using CameraManager
            mCameraId = mCameraManager.getCameraIdList()[0];
        }
        catch (Exception e){
            // print exception error
            e.printStackTrace();
        }

        mToggleButton = findViewById(R.id.toggleButton);

        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                // method to switch on flash
                switchFlashLight(isChecked);
            }
        });

    }


    // Display error message and closes app if device has no flash
    public void showNoFlashError(){
        AlertDialog alertDialog = new AlertDialog.Builder(FlashLightActivity.this).create();
        alertDialog.setTitle("Oops!");
        alertDialog.setMessage(getString(R.string.flash_not_available));
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // closes the application
                finish();
            }
        });
        alertDialog.show();

    }

    // Method to switch on Flash Light
    public void switchFlashLight(boolean status){
        try {
            /*sets the touchMode to the CameraId of the device
            to be switched on and turns it on
             */
            mCameraManager.setTorchMode(mCameraId,status);
        }
        catch (CameraAccessException e){
            // print exception error
            e.printStackTrace();
        }

    }

    // Closes application on double tap
    @Override
    public void onBackPressed() {
        // closes application
       if(doublePressBackToExitApp){
           super.onBackPressed();
           return;
        }
        doublePressBackToExitApp = true;
       // display a toast to user
        Toast.makeText(FlashLightActivity.this,getString(R.string.exit_app_message),Toast.LENGTH_SHORT).show();

        // Create a delay of 2 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doublePressBackToExitApp = false;
            }
        },2000);
    }
}
