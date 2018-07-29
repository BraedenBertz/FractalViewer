/*
 * @author Braeden Bertz
 * Program:
 * Purpose: Control all user events on JavaFx
 * Version:1.0

 * TODO:
 * Find a way to alter the mandelbrot & julia set equation (it changes the screen but wont display properly, mainly zoom)
 * Add apollonian Gasket 3d
 * Incorporate more mouse functionality:
 *              Zoom with middle mouse
 *              Drag with left down
 */

package ImageViews;

import AlertBoxes.HelpAlertBox;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class Controller{

    //Declare private variables
    private boolean AlreadySaved;
    private static int level = 5;
    private static ArrayList<String> arr = new ArrayList<>();
    private static ArrayList<Label> labels = new ArrayList<>();
    private static ArrayList<TextField> TextFields = new ArrayList<>();
    private static AtomicInteger ordinal = new AtomicInteger(-1);
    private static String msg = "";
    private File savedFile;
    private static int MaxIterations = 400;
    private static double Zoom = 1000.0;
    private static double MoveX = .132;
    private static double MoveY = .24;
    private static Color color = new Color(0,0,0,1.0);
    private double lineWidth = 1.0;

    //Declare Containers
    public VBox VBox1;
    public VBox VBox2;
    public MenuItem Save;
    public MenuItem SaveAs;
    public MenuItem Import;
    public MenuItem Close;
    public MenuItem HelpAbout;
    public Canvas TwoDCanvas;
    public ColorPicker PickColor;
    public TextField LineWeightTF;

    //Declare RadioButtons
    public RadioButton SierpinskiTriangleRB;
    public RadioButton SierpinskiCarpetRB;
    public RadioButton KochSnowflakeRB;
    public RadioButton MandelbrotSetRB;
    public RadioButton JuliaSetRB;
    public RadioButton MengerSpongeRB;

    //Declare ToggleGroups
    public ToggleGroup ToggleGroupPatterns;

    //Methods for MenuItems
    /*
     * @Param: The file we are writing to, the text we want to write to that file.
     * @Method: Write to the sent file using the String sent.
     */
    private void write(File savedFile, String fileTxt) throws IOException{
            FileWriter FileSaved = new FileWriter(savedFile.getAbsolutePath());
            PrintWriter print = new PrintWriter(FileSaved);
            print.print(fileTxt);
            print.close();
    }

    /*
     * @Param: null
     * @Method: Write to the sent file using the already sent String.
     */
    public void Save(){
        if(AlreadySaved) {
            try {
                msg = Equations();
                write(savedFile, msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            //save to the current state to be referenced later
            //if there is no current state already saved call SaveAs()
            SaveAs();
        }

    }

    /*
     * @Param: null
     * @Method: Open a save file window dialog box that allows the user to save a file. Write down the equations parameters currently being used.
     */
    public void SaveAs() {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Save File");
            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
            savedFile = chooser.showSaveDialog(null);
            msg = Equations();
            if (savedFile != null) {
                try {
                    write(savedFile, msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                AlreadySaved = true;
        }
    }

    /*
    * @Param: null
    * @Method: This was supposed to determine the current variables for each of the fractals however I ran out of time to properly implement it
    */
    private String Equations(){
        return level+" Right now this is the only thing that I have set up to be read and saved";
    }

    /*
     * @Param: null
     * @Method: Read the imported file's txt and then use that information to change the variables for drawing fractals
     */
    public void Import() {
        int numOfLines = 0;
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Resource File");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File openFile = chooser.showOpenDialog(null);
        if(openFile != null) {
            try {
                FileReader fr = new FileReader(openFile);
                BufferedReader textReader = new BufferedReader(fr);
                FileReader fileToRead = new FileReader(openFile);
                BufferedReader bf = new BufferedReader(fileToRead);

                while ((bf.readLine()) != null) {
                    numOfLines++;
                }
                bf.close();
                String[] textData = new String[numOfLines];

                for (int i = 0; i < numOfLines; i++) {
                    textData[i] = textReader.readLine();
                }
                String w = Character.toString(textData[0].charAt(0));
                level = Parse(w);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /*
     * @Param: null
     * @Method: Check to see if the state has already been saved, if yes save again and close, if no, ask if they would like to save then close
     */
    public void Close(){
        if(AlreadySaved){
            Save();
        }
        else{
            Alert j = new Alert(Alert.AlertType.CONFIRMATION);
            j.setTitle("Leave without saving?");
            j.setHeaderText("This will not allow you to import this state in the future");
            Optional<ButtonType> result = j.showAndWait();
                if(result.get() == ButtonType.CANCEL){
                SaveAs();
                 }
        }
        Platform.exit();
    }

    /*
     * @Param: null
     * @Method: Create a non-modular alert box that tells the user information about the program
     */
    public void helpAbout(){
        HelpAlertBox.display("Help");
    }

    /*
     * @Param: String
     * @Method: Determine if the string is an integer or a double value| if yes, return the parsed String | if no, return -1
     */
    private int Parse(String s){
        int f;
        double b;
        try{
            b = Double.parseDouble(s);
            return (int)b;
        }
        catch(NumberFormatException e){}

        try {
            f = Integer.parseInt(s);
            return f;
        } catch (NumberFormatException e) {
            f = -1;
            return f;
        }
    }

    /*
    * @Param: A string arrayList
    * @Method: Go through the string arrayList and create a label and TextField for each element. These labels and TextFields will be accessed later in ChangeEquation
    */
    private void Equation(ArrayList<String> j){
        TextFields.clear();
        VBox2.getChildren().clear();
        VBox1.getChildren().clear();
        //Create a new Label and TextField for each element in the sent array
        for(int i = 0; i < j.size(); i++){
            Label label = new Label(j.get(i));//this gets the string from the sent array to label the label correctly
            label.setPrefHeight(25);
            labels.add(label);

            TextField tx = new TextField();//this is what the user will input their changes into
            TextFields.add(tx);

            VBox1.getChildren().add(label);//add to left vbox
            VBox2.getChildren().add(tx);//add to right vbox
        }

        /*
        * This should, in theory, set each of the newly made textFields in TextFields to a specific integer value, denoted by the ordinal.
        * Each of these textFields, in addition to receiving inputs, will have an onAction event which calls the ChangeEquation method
         * with its unique call, denoted by the ordinal it got assigned
        * It should then do what it do.
        */
        ordinal.set(-1);//reset the ordinal
        TextFields.forEach(e -> {
            e.setOnAction(s -> ChangeEquation(ordinal.get()));
            ordinal.set(ordinal.get()+1);
        }
        );
    }

    /*
    * @Param: An identifying int, for what array element in the array was changed
    * @Method: Determine what element in the array was changed and set that corresponding value to the entered input
     */
    private void ChangeEquation(int i){
        switch(i){
            case 0:
                if(ToggleGroupPatterns.getSelectedToggle().getUserData().toString().equals("SierpinskiTriangleRB")
                        || ToggleGroupPatterns.getSelectedToggle().getUserData().toString().equals("SierpinskiCarpetRB")
                        || ToggleGroupPatterns.getSelectedToggle().getUserData().toString().equals("KochSnowflakeRB")){
                    if(Parse(TextFields.get(0).getText()) != -1) {
                        level = Parse(TextFields.get(0).getText());
                    }
                }
                else{Zoom = Parse(TextFields.get(0).getText());
                    System.out.println("hit");}
                Canvas(ToggleGroupPatterns.getSelectedToggle().getUserData().toString());
                break;
            case 1:
                MaxIterations = Parse(TextFields.get(1).getText());
                Canvas(ToggleGroupPatterns.getSelectedToggle().getUserData().toString());
                break;
            case 2:
                MoveX = Parse(TextFields.get(2).getText());
                Canvas(ToggleGroupPatterns.getSelectedToggle().getUserData().toString());
                break;
            case 3:
                MoveY = Parse(TextFields.get(3).getText());
                Canvas(ToggleGroupPatterns.getSelectedToggle().getUserData().toString());
                break;
            default:
        }
    }

    //Methods for Canvas
    /*
     * @Param: String UserData for the RadioButton that's calling on it
     * @Method: Determine what RadioButton is currently selected, using the sent String (in this case the UserData), and draw that fractal
     */
    private void Canvas(String pattern) {
        GraphicsContext gc = TwoDCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, TwoDCanvas.getWidth(), TwoDCanvas.getHeight());//this clears the graphics context
        //color = PickColor.getValue();
        //Add a switch statement that determines which radioButton is selected then draw based off of that
        switch(pattern) {
            case "SierpinskiTriangleRB":

                Point gP1 = new Point(50, 750);//lower left side
                Point gP2 = new Point(750, 750);//lower right side
                Point gP3 = new Point(400, 50);//middle top side
                DrawTwoDImages.drawSierpinskiTriangle(gc, level, gP1, gP2, gP3, color, lineWidth);
                //gets here the second equation change but doesn't change the picture
                if(arr.size() != 0){arr.clear();}
                arr.add("Level");
                Equation(arr);
                break;

            case "SierpinskiCarpetRB":

                DrawTwoDImages.drawSierpinskiCarpet(gc, 0, 0, 805, level, color);

                if(arr.size() != 0){arr.clear();}
                arr.add("Level");
                Equation(arr);
                break;

            case "KochSnowflakeRB":

                Point P1 = new Point(50, 550);//lower left side
                Point P2 = new Point(375, 50);
                Point P3 = new Point(750, 550);//lower right side

                DrawTwoDImages.drawKochSnowflake(gc, level, P1, P3, color);
                DrawTwoDImages.drawKochSnowflake(gc, level, P3, P2, color);
                DrawTwoDImages.drawKochSnowflake(gc, level, P2, P1, color);

                if(arr.size() != 0){arr.clear();}
                arr.add("Level");
                Equation(arr);
                break;

            case "MandelbrotSetRB":
                DrawTwoDImages.drawMandelbrotSet(gc, MaxIterations, Zoom);
                if(arr.size() != 0){arr.clear();}
                arr.add("Zoom");
                arr.add("MaxIterations");
                Equation(arr);
                break;

            case "JuliaSetRB":
                /*
                The more iterations the less black and the farther your can zoom in
                If a value is included in the set, its represented by a black pixel; any other value is outside of the set and the coloring determines
                how fast it converges to infinity -- the faster it converges the more red (in short)
                A higher number provides a greater opportunity for that value to go to infinity and therefore not be included into the set which is why
                more iterations leads to less black
                */
                int w = 805, h = 805;
                double ConstantX = -0.7, ConstantY = -0.27015;

                DrawTwoDImages.drawJuliaSet(gc, w, h, MaxIterations, ConstantX, ConstantY, MoveX, MoveY, Zoom);
                if(arr.size() != 0){arr.clear();}
                arr.add("Zoom");
                arr.add("Max Iterations");
                arr.add("Move X");
                arr.add("Move Y");
                Equation(arr);
                break;

            case "MengerSpongeRB":
                Stage j = new Stage();
                new ThreeD(j);
                break;

            default:
        }//end switch case
    }//end Canvas()

    /*
     *@Param: null
     *@Method: Set the global variable color to the newly selected color from the PickColor variable
     */
    public void Color(){
        color = PickColor.getValue();
        Canvas(ToggleGroupPatterns.getSelectedToggle().getUserData().toString());
    }

    /*
     * @Param: null
     * @Method: Set the global variable lineWidth to the newly selected double value from the text field and then redraw the pattern by calling on canvas.
     */
    public void LineWeight(){
        lineWidth = Parse(LineWeightTF.getText());
        Canvas(ToggleGroupPatterns.getSelectedToggle().getUserData().toString());
        LineWeightTF.clear();
    }

    //Methods for RadioButtons
    /*
     * @Param: null
     * @Method: Assign a String userData to each of the RadioButtons to identify them then, if the button is not already selected, send the newly selected RadioButton's UserData to canvas
     */
    public void RadioButtons(){
        //Set all the patterns to have a String as their UserData for reference in a switch statement in canvas to use
        SierpinskiTriangleRB.setUserData("SierpinskiTriangleRB");
        SierpinskiCarpetRB.setUserData("SierpinskiCarpetRB");
        KochSnowflakeRB.setUserData("KochSnowflakeRB");
        MandelbrotSetRB.setUserData("MandelbrotSetRB");
        JuliaSetRB.setUserData("JuliaSetRB");
        MengerSpongeRB.setUserData("MengerSpongeRB");

        //Send the UserData to canvas() to draw if the selected RadioButton is not already selected (so a change in radiobutton will trigger this)
        if(!ToggleGroupPatterns.getSelectedToggle().equals(ToggleGroupPatterns.getToggles())){
            Canvas(ToggleGroupPatterns.getSelectedToggle().getUserData().toString());
        }
    }//end RadioButtons
}//end Controller.java