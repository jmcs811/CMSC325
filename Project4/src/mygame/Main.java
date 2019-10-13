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
import com.jme3.bullet.BulletAppState;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.Geometry;
import com.jme3.math.ColorRGBA;
import com.jme3.bullet.control.RigidBodyControl;
import static com.jme3.bullet.PhysicsSpace.getPhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.font.BitmapText;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.MouseInput;





/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication implements PhysicsCollisionListener{

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    
    // nodes
    Node playerNode = new Node("playerNode");
    Spatial player;
    Node ground = new Node("ground");

    // rigidbody physics objs
    private RigidBodyControl targetPhysics;
    private RigidBodyControl bullet;
    
    private BulletAppState bulletState;
    
    // target objects
    private static final Box box;
    private static final Sphere sphere;
    
    // number of targets
    int numOfTargets = 5;
    
    // materials
    Material targetMaterial;
    Material targetHitMaterial;
    
    static {
        box = new Box(0.25f, 0.25f, 0.25f);
        sphere = new Sphere(32,32,0.1f,true,false);
    }

    @Override
    public void simpleInitApp() {
        
        // do not show fps stats
        setDisplayStatView(false);
        setDisplayFps(false);
        
        // physics
        bulletState = new BulletAppState();
        stateManager.attach(bulletState);
        
        // sky
        rootNode.attachChild(SkyFactory.createSky(getAssetManager(),
                "Textures/Sky/Bright/FullskiesBlueClear03.dds",
                SkyFactory.EnvMapType.CubeMap));
        
        // initialize the ground
        initGroundAndMats();
        
        // initialize the player object
        initPlayer();
        
        // setting up controls
        initKeys();
        
        generateTargets(numOfTargets);
        
        initGui();
        
        getPhysicsSpace().addCollisionListener(this);
    }
    
    private void initKeys() {
        inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Back", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        
        inputManager.addListener(analogListener, "Forward", "Right", "Back", "Left");
        inputManager.addListener(actionListener, "Shoot");
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
                playerNode.rotate(0, -value*speed, 0);
            }
            if (name.equals("Right")) {
                playerNode.rotate(0, value*speed, 0);
            }
        }    
    };
    
    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
          if (name.equals("Shoot") && !keyPressed) {
            shoot();
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

    private void initGroundAndMats() {
        // ground
        Spatial terrain = assetManager.loadModel("Models/Terrain/Terrain.mesh.xml");
        Material terrain_material = new Material(assetManager, "Common/MatDefs/Terrain/Terrain.j3md");
        terrain_material.setTexture("Alpha", assetManager.loadTexture("Textures/Terrain/splat/alpha1.png"));
        Texture dirt_texture = assetManager.loadTexture("Textures/Terrain/splat/dirt.jpg");
        Texture grass_texture = assetManager.loadTexture("Textures/Terrain/splat/grass.jpg");
        grass_texture.setWrap(WrapMode.Repeat);
        dirt_texture.setWrap(WrapMode.Repeat);
        terrain_material.setTexture("Tex1", grass_texture);
        terrain_material.setFloat("Tex1Scale", 256f);
        terrain_material.setTexture("Tex2", grass_texture);
        terrain_material.setFloat("Tex2Scale", 256f);
        terrain_material.setTexture("Tex3", grass_texture);
        terrain_material.setFloat("Tex3Scale", 256f);
        terrain.setMaterial(terrain_material);
        terrain.scale(100);
        terrain.setLocalTranslation(0, -1, 0);
        ground.attachChild(terrain);
        
        rootNode.attachChild(ground);
        
        terrain.addControl(new RigidBodyControl(0));
        bulletState.getPhysicsSpace().addAll(terrain);
        
        // other materials
        targetMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        targetMaterial.setColor("Color", ColorRGBA.Yellow);
        
        targetHitMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        targetHitMaterial.setColor("Color", ColorRGBA.Red);
    }

    private void initPlayer() {
        // player        
        player = assetManager.loadModel("Models/Buggy/Buggy.j3o");
        Material buggy_material = new Material(
            assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        buggy_material.setTexture("ColorMap",
                    assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg"));
        player.setMaterial(buggy_material);
        player.scale(0.2f);
        
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
        
        player.addControl(new RigidBodyControl(0));
        bulletState.getPhysicsSpace().addAll(player);
    }
    
    private void generateTargets(int targets) {
        for (int i = 0; i <= targets; i++) {
            int x = generateNumber(-15, 10);
            int z = generateNumber(-15, 10);
            
//            Node targetNode = new Node("target");
//            
//            Spatial target = assetManager.loadModel("Models/Buggy/Buggy.j3o");
//            Material target_material = new Material(
//            assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//            target_material.setTexture("ColorMap",
//                    assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg"));
//            target.setMaterial(target_material);
//            target.scale(0.2f);
            
            Geometry targetGeometry = new Geometry("target", box);
            targetGeometry.setMaterial(targetMaterial);
            rootNode.attachChild(targetGeometry);
            targetGeometry.setLocalTranslation(x, 1f, z);
            targetPhysics = new RigidBodyControl(1.0f);
            targetGeometry.addControl(targetPhysics);
//            targetNode.attachChild(target);
//            target.setLocalTranslation(x, 1f, z);
//            rootNode.attachChild(targetNode);
//            targetPhysics = new RigidBodyControl(1.0f);
//            target.addControl(targetPhysics);
            bulletState.getPhysicsSpace().add(targetPhysics);
        }
    }
    
    private int generateNumber(int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }

    private void initGui() {
        guiNode.detachAllChildren();
        
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText score = new BitmapText(guiFont, false);
        score.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        score.setLocalTranslation(10, score.getLineHeight() + 15, 0);
        
        score.setText(numOfTargets + " left");
        
        if (numOfTargets <= 0) {
            BitmapText winText = new BitmapText(guiFont, false);
            winText.setSize(guiFont.getCharSet().getRenderedSize() * 3);
            winText.setLocalTranslation(
                    settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2,
                    settings.getHeight() / 2 + winText.getLineHeight() / 2, 0
            );
            winText.setText("You Won!");
        }
        
        guiNode.attachChild(score);
    }
    
    private void shoot() {
        Geometry bulletGeometry = new Geometry("bullet", sphere);
        bulletGeometry.setMaterial(targetMaterial);
        rootNode.attachChild(bulletGeometry);
        bulletGeometry.setLocalTranslation(player.getWorldTranslation().add(0, .3f,0));
        bullet = new RigidBodyControl(1.0f);
        bulletGeometry.addControl(bullet);
        bulletState.getPhysicsSpace().add(bullet);
        bullet.setLinearVelocity(cam.getDirection().mult(30));
    }
    
    @Override
    public void collision(PhysicsCollisionEvent event) {
        if ("target".equals(event.getNodeA().getName())) {
            if ("bullet".equals(event.getNodeB().getName())) {
                event.getNodeA().setMaterial(targetHitMaterial);
                event.getNodeA().setName("targetHit");
                numOfTargets--;
                initGui();
                fpsText.setText(event.getNodeA().getName());
            }
        }
        if ("target".equals(event.getNodeB().getName())) {
            if ("bullet".equals(event.getNodeA().getName())) {
                event.getNodeB().setMaterial(targetHitMaterial);
                event.getNodeB().setName("targetHit");
                numOfTargets--;
                initGui();
                fpsText.setText(event.getNodeA().getName());
            }
        }
        initGui();
    }
}
