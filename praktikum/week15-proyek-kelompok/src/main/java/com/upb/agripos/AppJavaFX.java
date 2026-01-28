package com.upb.agripos;
import com.upb.agripos.view.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class AppJavaFX extends Application{
    public void start(Stage s){
        s.setScene(new Scene(new LoginView(s),300,250));
        s.show();
    }
    public static void main(String[] a){launch(a);}
}