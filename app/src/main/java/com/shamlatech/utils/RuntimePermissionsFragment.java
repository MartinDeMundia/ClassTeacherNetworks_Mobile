package com.shamlatech.utils;

import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

/**
 * Created by Dharmalingam Sekar on 24-05-2017.
 */

public abstract class RuntimePermissionsFragment extends Fragment {

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            onPermissionsGranted(requestCode);
        } else {
            onPermissionsDenied(requestCode);
        }
    }

    public boolean IsPermissionGranted(String request) {
        return (ContextCompat.checkSelfPermission(getActivity(), request)
                == PackageManager.PERMISSION_GRANTED);
    }

    public void requestAppPermissions(final String[] requestedPermissions, final int requestCode) {
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(getActivity(), permission);
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale || shouldShowRequestPermissionRationale(permission);
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale) {
                requestPermissions(requestedPermissions, requestCode);
            } else {
                requestPermissions(requestedPermissions, requestCode);
            }
        } else {
            onPermissionsGranted(requestCode);
        }
    }


    public abstract void onPermissionsGranted(int requestCode);

    public abstract void onPermissionsDenied(int requestCode);



}