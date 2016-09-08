package app.wagnercsfilho.com.whatsapp.helper;

import android.util.Base64;

public class Encrypter {

    public static String convertToBase64(String txt) {
        return Base64.encodeToString( txt.getBytes(), Base64.DEFAULT ).trim();
    }

    public static String decodeBase64(String txt) {
        return new String(Base64.decode( txt, Base64.DEFAULT ));
    }

}
