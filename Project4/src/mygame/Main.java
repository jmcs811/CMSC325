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
import com.jme3.util.SkyFactory;

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
    
    Node playerNode;
    Spatial player;

    @Override
    public void simpleInitApp() {
        
        // sky
        rootNode.attachChild(SkyFactory.createSky(getAssetManager(),
                "Textures/Sky/Bright/FullskiesBlueClear03.dds",
                SkyFactory.EnvMapType.CubeMap));
        
        Node ground = new Node("ground");
        
        // ground
        Spatial terrain = assetManager.loadModel("Models/Terrain/Terrain.mesh.xml");
        Material terrain_material = new Material(assetManager, "Common/MatDefs/Terrain/Terrain.j3md");
        terrain_material.setTexture("Alpha", assetManager.loadTexture("Textures/Terrain/splat/alpha1.png"));
        Texture dirt_texture = assetManager.loadTexture("Textures/Terrain/splat/dirt.jpg");
        Texture grass_texture = assetManager.loadTexture("Textures/Terrain/splat/grass.jpg");
        grass_texture.setWrap(WrapMode.Repeat);
        dirt_texture.setWrap(WrapMode.Repeat);
        terrain_material.setTexture("Tex1", grass_texture);
        terrain_material.setFloat("Tex1Scale", 64f);
        terrain_material.setTexture("Tex2", dirt_texture);
        terrain_material.setFloat("Tex2Scale", 128f);
        terrain_material.setTexture("Tex3", dirt_texture);
        terrain_material.setFloat("Tex3Scale", 64f);
        terrain.setMaterial(terrain_material);
        terrain.scale(100);
        terrain.setLocalTranslation(0, -1, 0);
        ground.attachChild(terrain);
        
        rootNode.attachChild(ground);
        
        // player
        playerNode = new Node("playerNode");
        
        player = assetManager.loadModel("Models/Buggy/Buggy.j3o");
        Material buggy_material = new Material(
            assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        buggy_material.setTexture("ColorMap",
                    assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg"));
        player.setMaterial(buggy_material);
        player.scale(0.2wf);
        
        player.setLocalTranslation(0, -0.5f, 0);
        
        // middle of screen
        Quaternion q = new Quaternion();
        q.fromAngles(0, 1.57f, 0);
        player.setLocalRotation(q);
        
        playerNode.attachChild(player);
        
        // camera
        flyCam.setEnabled(false);
        CameraNode camNode = new CameraNode("Camera", cam);
        camNode.setControlDir(ControlDirection.SpatialToCamera);
        playerNode.attachChild(camNode);
        
        camNode.setLocalTranslation(new Vector3f(-3,.4f,0));
        camNode.setLocalRotation(q);
        
        playerNode.setLocalTranslation(new Vector3f(0,0,10));
        
        rootNode.attachChild(playerNode);

        // setting up controls
        initKeys();
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
