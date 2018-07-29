/*
 * @author Braeden Bertz
 * Program: Take in the .fxml layout and set it to a root then set that root to a scene and show it
 * Purpose: Launch the Program
 * Version:1.0
 */

package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FractalViewer extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));//This takes all the code in GUI.fxml uses it to construct root
        primaryStage.setTitle("Braeden's Fractal Viewer");
        primaryStage.setScene(new Scene(root, 990, 815));
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e ->
            primaryStage.close()
        );
        primaryStage.show();
    }
}
