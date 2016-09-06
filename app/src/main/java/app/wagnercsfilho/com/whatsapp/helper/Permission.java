package app.wagnercsfilho.com.whatsapp.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;


public class Permission {

    public static boolean setPermission(int requestCode, Activity activity, String[] permissions) {

        List<String> permissionList = new ArrayList<String>();

        if (Build.VERSION.SDK_INT >= 23) {
            for (String permission : permissions) {
                if (!(ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED)) {
                    permissionList.add(permission);
                }
            }

            if (permissionList.isEmpty()) return true;

            // Request Permission
            String[] requestPermissions = new String[ permissionList.size() ];
            permissionList.toArray(requestPermissions);
            ActivityCompat.requestPermissions(activity, requestPermissions, requestCode);
        }

        return true;
    }
}
