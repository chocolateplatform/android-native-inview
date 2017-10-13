package sample.inline.utils;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Bindings {
    
   @android.databinding.BindingAdapter({"bind:imageUrl"})
   public static void setImageUrl(ImageView imageView, String url) {
      Glide.with((Activity) imageView.getContext()).load(url).into(imageView);
   }

}
