package asee.giis.unex.vegenatnavigationdrawer.ui.preguntasfrecuentes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import asee.giis.unex.vegenatnavigationdrawer.R;

/**
 * Fragment para preguntas frecuentes
 */
public class PreguntasFrecuentesFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.preguntas_frecuentes, container, false);
        return root;
    }
}
