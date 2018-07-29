/*
 * @author Braeden Bertz
 * Purpose: Setup and display a help menu
 * Version: 1.0
 */
package AlertBoxes;

import javafx.geometry.Pos;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

public class HelpAlertBox{

    public static void display(String title){

        TitledPane about = new TitledPane();
        String AboutText = "This application is a fractal viewer made by Braeden N. Bertz for the APCSA final project (2018).";
        TextArea tx = new TextArea();
        tx.setText(AboutText);
        about.setText("About");
        about.setAnimated(true);
        about.setContent(tx);
        about.setPrefWidth(700);

        TitledPane HowTo = new TitledPane();
        HowTo.setText("How to use the application");
        String HowToText = "Click on either the 2D or 3D images accordion drop down and then select a desired pattern. " +
                "\nOnce a pattern is selected, you can change the equation and create a customized fractal." +
                "\nDue to difficulties, the Animate accordion drop down is defunct, so is the Movement Controls drop down, and so is the setting MenuBar item." +
                "\n\nControl scheme for the 3D viewer:" +
                "\nMiddle Click: Dragging Middle Click will move the camera the specified direction" +
                "\nLeft Click: Rotate around the origin" +
                "\nRight Click: While holding down, move leftwards to zoom out, move rightwards to zoom in" +
                "\nShift: Holding down Shift while using left or right click will increase your movement speed" +
                "\nControl: Holding down control while using left or right click will decrease your movement speed" +
                "\nD: Pressing D will get you the next level of the Menger Sponge (based on what is currently being shown)" +
                "\nT: Pressing T will get you the next level of the Vicsek Cross (based on what is currently being shown)" +
                "\nZ: Pressing Z will reset your viewing angle" +
                "\nF1: Pressing F1 will reset the level of your current fractal (you must press either D or T to see the effects)" +
                "\n\nKeyBoard Short Cuts:" +
                "\nCtrl+s: save" +
                "\nAlt+i: import" +
                "\nAlt+c: close" +
                "\nCtrl+h: help";
        TextArea ts = new TextArea();
        ts.setText(HowToText);
        HowTo.setAnimated(true);
        HowTo.setContent(ts);
        HowTo.setPrefWidth(700);

        TitledPane troubleshoot = new TitledPane();
        String TroubleshootText = "The equations changer for fractals doesn't work as intended; it only works sometimes. Sorry." +
                "\nImport not changing the fractal? Not implemented yet, sorry." +
                "\nAny Other Problems? Please contact Braeden Bertz: 'bb19889@students.mcpasd.k12.wi.us' for further assistance.";
        TextArea ta = new TextArea();
        ta.setText(TroubleshootText);
        troubleshoot.setText("Troubleshooting");
        troubleshoot.setAnimated(true);
        troubleshoot.setContent(ta);
        troubleshoot.setPrefWidth(700);


        Accordion accordion = new Accordion();
        accordion.getPanes().addAll(about,HowTo,troubleshoot);


        HBox hBox = new HBox();
        hBox.getChildren().addAll(accordion);

        StackPane layout = new StackPane();
        layout.getChildren().addAll(hBox);
        layout.setAlignment(Pos.CENTER);

        Stage window = new Stage();
        window.setTitle(title);
        window.setMinWidth(690);
        window.setMinHeight(500);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setResizable(false);
        window.setScene(scene);
        window.showAndWait();
    }
}
