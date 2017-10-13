package sample.inline.utils;

import android.content.Context;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class FileUtil {

   public static final String TAG = "FileUtil";

   public JsonObject getJSONResource(Context context, String filename) throws IOException {
      InputStream is = null;
      try {
         is = context.getAssets().open(filename);
         JsonParser parser = new JsonParser();
         return parser.parse(new InputStreamReader(is)).getAsJsonObject();
      } finally {
         if (is != null)
            is.close();
      }
   }
}
