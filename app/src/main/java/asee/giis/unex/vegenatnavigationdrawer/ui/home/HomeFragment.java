package asee.giis.unex.vegenatnavigationdrawer.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import asee.giis.unex.vegenatnavigationdrawer.AppContainer;
import asee.giis.unex.vegenatnavigationdrawer.R;
import asee.giis.unex.vegenatnavigationdrawer.MyApplication;

/**
 * Fragment para la vista de inicio de usuario
 **/
public class HomeFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        TextView bienvenido = root.findViewById(R.id.bienvenido);

        //Obtenemos el appContainer
        AppContainer appContainer = ((MyApplication) getActivity().getApplication()).appContainer;

        //Ponemos el texto de Bienvenida al usuario con el username
        bienvenido.setText("¡Hola, " + appContainer.username + "!\n" + bienvenido.getText());
        return root;
    }
}