package app.wagnercsfilho.com.whatsapp.helper;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class Preference {

    private Context context;
    private SharedPreferences preferences;
    private final String FILE_NAME = "whatsapp.preferences";
    private final int MODE = 0;
    private SharedPreferences.Editor editor;

    public static final String KEY_PHONE = "phone";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_NAME = "name";
    public static final String KEY_AVATAR = "avatar";

    public Preference(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(FILE_NAME, MODE);
        this.editor = this.preferences.edit();
    }

    public void saveUserPreference(String phone, String token) {
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }

    public void saveUserName(String name) {
        editor.putString(KEY_NAME, name);
        editor.commit();
    }

    public HashMap<String, String> getPhoneUserData() {
        HashMap<String, String> userData = new HashMap<>();

        userData.put(KEY_PHONE, this.preferences.getString(KEY_PHONE, null));
        userData.put(KEY_TOKEN, this.preferences.getString(KEY_TOKEN, null));
        userData.put(KEY_NAME, this.preferences.getString(KEY_NAME, null));
        userData.put(KEY_AVATAR, this.preferences.getString(KEY_AVATAR, null));

        return userData;
    }

    public String getUserIdEnrypted() {
       return Encrypter.convertToBase64(preferences.getString(KEY_PHONE, null));
    }

    public String getUserName() {
        return preferences.getString(KEY_NAME, null);
    }

}
