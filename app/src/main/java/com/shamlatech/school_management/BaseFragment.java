package com.shamlatech.school_management;

import android.app.Activity;

import com.shamlatech.database.DBAdapter;
import com.shamlatech.utils.RuntimePermissionsFragment;

/**
 * Created by Dharmalingam Sekar on 07-05-2018.
 */

public class BaseFragment extends RuntimePermissionsFragment {

    public DBAdapter db;


    public void declareDb(Activity act) {
        try {
            db = new DBAdapter(act, act.getFilesDir().getAbsolutePath());
            db.prepareDatabase();
        } catch (Exception e) {
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    public void onPermissionsDenied(int requestCode) {

    }
}
