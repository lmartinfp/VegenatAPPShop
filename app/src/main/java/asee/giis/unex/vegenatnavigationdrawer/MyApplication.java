package asee.giis.unex.vegenatnavigationdrawer;

import android.app.Application;

/**
 * Objeto MyApplication para el contenedor de dependencias
 **/

public class MyApplication extends Application { //Extendiente de Application

    //Contenedor de dependencias
    public AppContainer appContainer;

    @Override
    public void onCreate() {
        super.onCreate(); //Como extiende de Application llamamos al suepr
        appContainer = new AppContainer(this); //El contexto es necesario para la BD de room y tengo que pasárselo
    }
}
