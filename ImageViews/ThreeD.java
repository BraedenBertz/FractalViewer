/*
 * @author Braeden Bertz With help from @cmcastil in XForm and with the camera methods
 * Purpose: Create a 3D viewing environment and implement the methods that will draw the 3D Fractals
 * Version:1.0
 */
package ImageViews;

import com.sun.istack.internal.NotNull;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.DrawMode;
import javafx.stage.Stage;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import java.util.ArrayList;

class ThreeD{

    //Declare Container Objects
    private final Group root = new Group();
    private final Xform shapeGroup = new Xform();
    private final Xform world = new Xform();

    //Declare Camera Objects
    private final PerspectiveCamera camera = new PerspectiveCamera(true);//False value sets it to the absolute position, true value centers it.
    private final Xform cameraXform = new Xform();
    private final Xform cameraXform2 = new Xform();
    private final Xform cameraXform3 = new Xform();

    //Declare Color Variables
    private final PhongMaterial orangeMaterial = new PhongMaterial();

    //Declare Camera Variables
    private static final double cameraInitialDistance = -1800;//in the z axis
    private static final double cameraInitialXAngle = 0;
    private static final double cameraInitialYAngle = 180;
    private static final double cameraNearClip = 0.1;
    private static final double cameraFarClip = 10000.0;

    //Declare UserEvent Variables
    private static final double controlModifier = 0.1;//When holding Control, the speed at which User Events happen get multiplied by this
    private static final double shiftModifier = 7.0;//When holding Shift, the speed at which User Events happen get multiplied by this
    private static final double mouseSpeed = 0.1;
    private static final double rotationSpeed = 2.0;
    private static final double trackSpeed = 1;

    //Declare Mouse Variables
    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;

    //Declare Fractal Variables
    private static int level = 0;
    private static int VicsekCrossIterations = 0;
    private static int MengerSpongeIterations = 0;
    private static int ApollonianGasketIteraions = 0;
    private static int size = 729;//exponents of 3 are preferred because its easy to determine what the lowest setting will be; i.Boxes., this one will be the smallest after log3(729)
    private ArrayList<Box> Boxes = new ArrayList<>();

    /**
     * @author @cmcastil
     */
    /*
     * @Param: Null
     * @Method: Set up the camera
     */
    private void buildCamera() {
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(cameraXform3);
        cameraXform3.getChildren().add(camera);
        cameraXform3.setRotateZ(180.0);

        camera.setNearClip(cameraNearClip);
        camera.setFarClip(cameraFarClip);
        camera.setTranslateZ(cameraInitialDistance);
        cameraXform.ry.setAngle(cameraInitialYAngle);
        cameraXform.rx.setAngle(cameraInitialXAngle);
    }//End buildCamera

    /*
     * @Param: The scene for the MouseEvents
     * @Method: Every MouseEvent will trigger this method and it will check for what input was used.
     */
    private void handleMouse(@NotNull Scene scene) {
        scene.setOnMousePressed(MouseEvent -> {

            mousePosX = MouseEvent.getSceneX();
            mousePosY = MouseEvent.getSceneY();

        });
        scene.setOnMouseDragged(MouseEvent -> {
            {
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = MouseEvent.getSceneX();
                mousePosY = MouseEvent.getSceneY();
                mouseDeltaX = (mousePosX - mouseOldX);
                mouseDeltaY = (mousePosY - mouseOldY);

                double modifier = 1.0;

                if (MouseEvent.isControlDown()) {
                    modifier = controlModifier;
                }
                if (MouseEvent.isShiftDown()) {
                    modifier = shiftModifier;
                }
                if (MouseEvent.isPrimaryButtonDown()) {//Left click: rotate the camera
                    cameraXform.ry.setAngle(cameraXform.ry.getAngle() - mouseDeltaX * mouseSpeed * modifier * rotationSpeed);
                    cameraXform.rx.setAngle(cameraXform.rx.getAngle() + mouseDeltaY * mouseSpeed * modifier * rotationSpeed);
                }
                else if (MouseEvent.isSecondaryButtonDown()) {//Right click: zoom in with camera, change the z coordinate
                    double z = camera.getTranslateZ();
                    double newZ = z + mouseDeltaX* mouseSpeed * modifier;
                    camera.setTranslateZ(newZ);
                }
                else if (MouseEvent.isMiddleButtonDown()) {//This moves the camera towards the mouse way
                    cameraXform2.t.setX(cameraXform2.t.getX() + mouseDeltaX * mouseSpeed * modifier * trackSpeed);
                    cameraXform2.t.setY(cameraXform2.t.getY() + mouseDeltaY * mouseSpeed * modifier * trackSpeed);
                }
            }
        });
    }//End handleMouse

    /**
     * @author @BraedenBertz*/
    /*
     * @Param: The scene that this will be in
     * @Method: Gets the input of each KeyEvent and finds the corresponding key in a switch
     */
    private void handleKeyboard(@NotNull Scene scene) {
        scene.setOnKeyPressed(KeyEvent -> {
            switch (KeyEvent.getCode())/*gets the source of keyPress*/ {
                case Z://Reset the viewpoint
                    cameraXform2.t.setX(0.0);
                    cameraXform2.t.setY(0.0);
                    camera.setTranslateZ(cameraInitialDistance);
                    cameraXform.ry.setAngle(cameraInitialYAngle);
                    cameraXform.rx.setAngle(cameraInitialXAngle);
                    break;
                case V://Vicsek Cross
                    Check(MengerSpongeIterations, ApollonianGasketIteraions, VicsekCrossIterations);
                    IncludeVicsekCross();
                    break;
                case D://Menger Sponge
                    Check(ApollonianGasketIteraions, VicsekCrossIterations, MengerSpongeIterations);
                    IncludeMengerSponge();
                    break;
                case A://Apollonian Gasket
                    Check(MengerSpongeIterations, VicsekCrossIterations, ApollonianGasketIteraions);

                    break;
                case F1:
                    Reset();
                    break;
            }
        });
    }//End handleKeyboard

    /*
    * @Param: 3 ints, one each for what pattern that needs to be checked to see if it needs to be Reset.
    * @Method: Check for each sent int and, if it is true, Reset
    */
    private void Check(int a, int b, int c){
        if(a != 0){
            Reset();}
        if(b != 0){
            Reset();}
        if(c == 7){
            Reset();}
    }//End Check

    /*
    * @Param: null
    * @Method: clear the container of what it currently has and add the Menger Sponge pattern into the container
    */
    private void IncludeMengerSponge(){
        world.getChildren().remove(shapeGroup);
        shapeGroup.getChildren().clear();
        Comparators comp = Comparators.DoesntEquals;
        Boxes = VicsekCrossOrMengerSponge(Boxes, level, comp, "MengerSponge");
        shapeGroup.getChildren().addAll(Boxes);
        world.getChildren().add(shapeGroup);
    }//End IncludeMengerSponge

    /*
    * @Param: null
    * @Method: clear the container of what it currently has and add the Vicsek Cross pattern into the container
    */
    private void IncludeVicsekCross(){
        world.getChildren().remove(shapeGroup);
        shapeGroup.getChildren().clear();
        Comparators isEqual = Comparators.DoesEquals;
        Boxes = VicsekCrossOrMengerSponge(Boxes, level, isEqual, "VicsekCross");
        shapeGroup.getChildren().addAll(Boxes);
        world.getChildren().add(shapeGroup);
    }//End IncludeVicsekCross

    /*
     * @Param: the current boxes, level currently at, the Comparator used (ie, != or ==) and the Pattern for the endProcesses
     * @Method: Create a 3d box then split it up into 27 separate ones that are only built if they are centered on the X & Y, X & Z, or Z & Y axes
     */
    private ArrayList<Box> VicsekCrossOrMengerSponge(@NotNull ArrayList<Box> f, int l, Comparators isEqual, String pattern){
        ArrayList<Box> NewBoxes = new ArrayList<>();

        int u = size / 3;
        if (f.size() != 0) {
            if (l != 0) {
                for (Box gg: f) {
                    double tx = gg.getTranslateX();
                    double ty = gg.getTranslateY();
                    double tz = gg.getTranslateZ();
                    for (int x = -1; x < 2; x++) {
                        for (int y = -1; y < 2; y++) {
                            for (int z = -1; z < 2; z++) {
                                if (isEqual.e(y,0) && isEqual.e(x,0) || isEqual.e(y,0) && isEqual.e(z,0) || isEqual.e(z,0) && isEqual.e(x,0)) {
                                    Box b = new Box(u, u, u);
                                    b.relocate(0,0);
                                    b.setTranslateZ(z * u + tz);
                                    b.setTranslateX(x * u + tx);
                                    b.setTranslateY(y * u + ty);
                                    b.setMaterial(orangeMaterial);
                                    b.setDrawMode(DrawMode.FILL);
                                    NewBoxes.add(b);
                                }
                            }
                        }
                    }
                }
                f = NewBoxes;
            }//end (l!=0) if
        }//end (f.size !=0) if
        else {
            for (int x = -1; x < 2; x++) {
                for (int y = -1; y < 2; y++) {
                    for (int z = -1; z < 2; z++) {
                        if (isEqual.e(y,0) && isEqual.e(x,0) || isEqual.e(y,0) && isEqual.e(z,0) || isEqual.e(z,0) && isEqual.e(x,0)) {
                            Box b = new Box(u, u, u);
                            b.setTranslateZ(z * u);
                            b.setTranslateX(x * u);
                            b.setTranslateY(y * u);
                            b.setMaterial(orangeMaterial);
                            b.setDrawMode(DrawMode.FILL);
                            NewBoxes.add(b);
                        }
                    }
                }
                f = NewBoxes;
            }
        }//end else
        doEndProcesses(pattern);
        return f;
    }//End VicsekCrossOrMengerSponge

    /*
    * @Param: String pattern (the current pattern)
    * @Method: sets the size to 1/3 of the current, adds another step to the level counter, add 1 to the iterations step counter
    */
    private void doEndProcesses(String pattern){
        size = size/3;
        level++;
        switch (pattern){
            case "VicsekCross":
                VicsekCrossIterations++;
                break;
            case "MengerSponge":
                MengerSpongeIterations++;
                break;
        }//End Switch case
    }//End doEndProcesses

    /*
    * @Param: Null
    * @Method: Set all fractal variables back to their original state
    */
    private void Reset(){
        level = 0;
        VicsekCrossIterations = 0;
        MengerSpongeIterations = 0;
        Boxes.clear();
        size = 729;
    }//End Reset

    /*
     * @Param: A stage that will be used for the 3D scene
     * @Method: Create a 3D environment
     */
    ThreeD(@NotNull Stage primaryStage) {
        orangeMaterial.setDiffuseColor(Color.ORANGERED);
        orangeMaterial.setSpecularColor(Color.ORANGERED);

        root.getChildren().add(world);
        root.setDepthTest(DepthTest.ENABLE);//This says that whatever is in another shape will not be shown

        buildCamera();

        Scene scene = new Scene(root, 1024, 768, true);//without depthBuffer true the boxes will act transparent
        scene.setFill(Color.LIGHTBLUE);//set the background color
        handleKeyboard(scene);
        handleMouse(scene);

        primaryStage.setScene(scene);
        primaryStage.show();

        scene.setCamera(camera);
    }//End ThreeD Constructor
}//End ThreeD Class