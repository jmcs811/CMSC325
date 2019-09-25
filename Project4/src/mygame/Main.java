package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    
    protected Node playerNode;
    protected Spatial player;

    @Override
    public void simpleInitApp() {
        
        Node ground = new Node("ground");
        
        // world
        Spatial terrain = assetManager.loadModel("Models/Terrain/Terrain.mesh.xml");
        Material mat_terrain = new Material(assetManager, "Common/MatDefs/Terrain/Terrain.j3md");
        mat_terrain.setTexture("Alpha", assetManager.loadTexture("Textures/Terrain/splat/alpha1.png"));
        Texture dirt = assetManager.loadTexture("Textures/Terrain/splat/dirt.jpg");
        Texture grass = assetManager.loadTexture("Textures/Terrain/splat/grass.jpg");
        grass.setWrap(WrapMode.Repeat);
        dirt.setWrap(WrapMode.Repeat);
        mat_terrain.setTexture("Tex1", grass);
        mat_terrain.setFloat("Tex1Scale", 64f);
        mat_terrain.setTexture("Tex1", grass);
        mat_terrain.setFloat("Tex2Scale", 64f);
        mat_terrain.setTexture("Tex3", dirt);
        mat_terrain.setFloat("Tex3Scale", 128f);
        terrain.setMaterial(mat_terrain);
        terrain.scale(50);
        terrain.setLocalTranslation(0, -1, 0);
        ground.attachChild(terrain);
        
        rootNode.attachChild(ground);
        
        // player
        playerNode = new Node("playerNone");
        
        player = assetManager.loadModel("Models/Buggy/Buggy.j3o");
        player.scale(0.1f);
        
        Quaternion q = new Quaternion();
        q.fromAngles(0, 1.5708f, 0);
        
        player.setLocalTranslation(0, -0.5f, 0);
        player.setLocalRotation(q);
        
        playerNode.attachChild(player);
        
        // camera
        flyCam.setEnabled(false);
        CameraNode camNode = new CameraNode("Camera Node", cam);
        camNode.setControlDir(ControlDirection.SpatialToCamera);
        playerNode.attachChild(camNode);
        
        camNode.setLocalTranslation(new Vector3f(-4,.35f,0));
        camNode.setLocalRotation(q);
        
        playerNode.setLocalTranslation(new Vector3f(0,0,10));
        
        rootNode.attachChild(playerNode);

        initKeys();
        
        
        
//        Spatial teapot = assetManager.loadModel("Models/Teapot/Teapot.obj");
//        Material mat_default = new Material(
//            assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
//        teapot.setMaterial(mat_default);
//        rootNode.attachChild(teapot);
//        
//        Box box = new Box(2.5f, 2.5f, 1.0f);
//        Spatial wall = new Geometry("Box", box);
//        Material mat_brick = new Material(
//            assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//        mat_brick.setTexture("ColorMap",
//                    assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg"));
//        wall.setMaterial(mat_brick);
//        wall.setLocalTranslation(2.0f, -2.5f, 0.0f);
//        rootNode.attachChild(wall);
//        
//        guiNode.detachAllChildren();
//        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
//        BitmapText helloText = new BitmapText(guiFont, false);
//        helloText.setSize(guiFont.getCharSet().getRenderedSize());
//        helloText.setText("Hello World");
//        helloText.setLocalTranslation(300, helloText.getLineHeight(), 0);
//        guiNode.attachChild(helloText);
//        
//        Spatial ninja = assetManager.loadModel("Models/Ninja/Ninja.mesh.xml");
//        ninja.scale(0.05f, 0.05f, 0.05f);
//        ninja.rotate(0.0f, -3.0f, 0.0f);
//        ninja.setLocalTranslation(0.0f, -5.0f, -2.0f);
//        rootNode.attachChild(ninja);
//        // You must add a light to make the model visible
//        DirectionalLight sun = new DirectionalLight();
//        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
//        rootNode.addLight(sun);
    }
    
    private void initKeys() {
        inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Back", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_D));
        
        inputManager.addListener(analogListener, "Forward", "Right", "Back", "Left");
    }
    
    private final AnalogListener analogListener = new AnalogListener() {
       @Override
       public void onAnalog(String name, float value, float tpf) {
            Vector3f v = playerNode.getLocalRotation().getRotationColumn(0);
            speed = 10;
            
            if (name.equals("Forward")) {
                playerNode.move(v.mult(tpf).mult(speed));
            }
            if (name.equals("Back")) {
                playerNode.move(v.mult(tpf).mult(-speed));
            }
            if (name.equals("Left")) {
                playerNode.rotate(0, value*speed, 0);
            }
            if (name.equals("Right")) {
                playerNode.rotate(0, -value*speed, 0);
            }
        }    
    };

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code

    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
