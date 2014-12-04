package com.graphics;

import com.graphics.entities.Camera;
import com.graphics.entities.Light;
import com.graphics.models.Loader;
import com.graphics.models.RawModel;
import com.graphics.raycasting.RayCastUtil;
import com.graphics.rendering.MasterRenderer;
import com.graphics.shapes.Colour;
import com.graphics.shapes.Line;
import com.graphics.shapes.Model;
import com.graphics.shapes.Shape;
import com.graphics.text.Text3D;
import com.graphics.utils.OBJLoader;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import java.awt.Canvas;
import java.util.ArrayList;
import java.util.List;

public abstract class RenderEngine implements Runnable {

	private MasterRenderer renderer;
	private Light sun = new Light(new Vector3f(40, 40, 40), new Vector3f(1, 1, 1));
	private long lastFps;
	private int fpsInc, fps;
	private List<Shape> shapes = new ArrayList<Shape>();
	private boolean breakFromLoop;
	private Screenshot screenshot = new Screenshot();

	private String title = "";
	private int width, height;
	private boolean resizable;
	private final Canvas canvas;
	private float r, g, b, a;
	private Camera camera;

	private boolean debugLines;
	private boolean mouseDown;
	private Shape selectedShape;
	
	private Text3D text3d;

	public RenderEngine(String title, int width, int height, boolean resizable) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.resizable = resizable;
		canvas = null;
	}

	public RenderEngine(final Canvas canvas) {
		this.canvas = canvas;
	}

	protected void setMainLight(float x, float y, float z, float r, float g, float b, float damper, float reflecivity) {
		sun = new Light(new Vector3f(x, y, z), new Vector3f(r, g, b));
		renderer.setLightVars(damper, reflecivity);
	}

	@Override
	public void run() {
		if (canvas == null)
			DisplayManager.createDisplay(title, width, height, resizable);
		else
			DisplayManager.createDisplay(canvas);

		renderer = new MasterRenderer();
		camera = new Camera();

		lastFps = getTime();

		beforeLoop();

		while (!Display.isCloseRequested() && !breakFromLoop) {

			renderer.setBackR(r);
			renderer.setBackG(g);
			renderer.setBackB(b);
			renderer.setBackA(a);

			if (Keyboard.isKeyDown(Keyboard.KEY_P))
				screenshot.capture();

			camera.move();

			inLoop();

			for (Shape e : shapes) {
				renderer.processEntity(e.getEntity());
			}

			renderer.render(sun, camera);
			DisplayManager.updateDisplay();
			updateFPS();

			// TODO move to own class
			if(!Mouse.isButtonDown(1))
				mouseDown = false;

			if(Mouse.isButtonDown(1) && !mouseDown){
				mouseDown = true;
				selectedShape = null;

				Vector3f vec = RayCastUtil.rayTest(camera, shapes);
				if (vec != null){
					System.out.println("Ray cast complete: Found shape :)");
					if(debugLines){
						Line l = new Line(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z, vec.x, vec.y, vec.z, Colour.GREEN);
						addShapeTo3DSpace(l);
					}
					selectedShape = RayCastUtil.selectedShape;
				}
			}
		}
		
		afterLoop();
		cleanUp();
	}

	protected abstract void beforeLoop();

	protected abstract void inLoop();

	protected abstract void afterLoop();

	protected int getFPS() {
		return fps;
	}

	protected Shape getSelectedShape(){
		return selectedShape;
	}
	
	protected void breakOutOfLoop() {
		breakFromLoop = true;
	}

	public void addShapeTo3DSpace(Shape e) {
		shapes.add(e);
	}
	
	public Shape modelToShape(String pathToModel, float x, float y, float z, float scale, Colour col) {
		RawModel rm = OBJLoader.loadObjModel(false, pathToModel, Loader.getInstance(), col);
		Shape s = new Model(x, y, z, 0, 0, 0, scale, col, rm);
		return s;
	}

	protected void removeShapeFrom3DSpace(Shape e) {
		shapes.remove(e);
	}

	protected void clearShapesFrom3DSpace() {
		shapes.clear();
	}

	protected int getNumberOfShapes() {
		return shapes.size();
	}

	private void cleanUp() {
		renderer.cleanUp();
		DisplayManager.closeDisplay();
	}

	private void updateFPS() {
		if (getTime() - lastFps > 1000) {
			fps = fpsInc;
			if (canvas != null)
				Display.setTitle("FPS: " + fps);
			fpsInc = 0;
			lastFps += 1000;
		}
		fpsInc++;
	}

	private long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	protected void setBackgroundColour(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	protected void captureScreen(String pathFromWorkspace, String filenameWithoutExtention) {
		screenshot.capture(pathFromWorkspace, filenameWithoutExtention);
	}

	protected void setCameraPosition(float x, float y, float z) {
		if (camera == null)
			return;
		camera.setPosition(new Vector3f(x, y, z));
	}

	protected void moveCameraPosition(float dx, float dy, float dz) {
		if (camera == null)
			return;
		Vector3f oldPos = camera.getPosition();
		camera.setPosition(new Vector3f(oldPos.x + dx, oldPos.y + dy, oldPos.z + dz));
	}
	
	protected void setRayPickDebugLines(boolean flag){
		debugLines = flag;
	}
	
	protected Text3D getText3D(){
		if(text3d == null)
			text3d = new Text3D(Loader.getInstance(), this);
		return text3d;
	}
}
