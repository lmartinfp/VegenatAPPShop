package asee.giis.unex.vegenatnavigationdrawer.glide_adapter;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Clase que tiene la anotación de GlideModule para cargar las imágenes con Glide
 */
@GlideModule
public class ImageAdapter extends AppGlideModule {

    /**
     * Método que carga la URL de una imagen en un ImageView
     */
    public static void loadUrl (Activity activity, String url, ImageView imagen) {
        Glide.with(activity).load(url).into(imagen);

    }
}
