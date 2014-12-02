package com.graphics.text;

import java.util.HashMap;
import java.util.Map;

import com.graphics.RenderEngine;
import com.graphics.models.Loader;
import com.graphics.models.RawModel;
import com.graphics.shapes.Colour;
import com.graphics.shapes.Cube;
import com.graphics.shapes.Letter;
import com.graphics.utils.OBJLoader;

public class Text3D {

	private static final int ASCII_UPPER_A = 65;
	private static final int ASCII_UPPER_Z = 90;
	private static final int ASCII_LOWER_A = 97;
	private static final int ASCII_LOWER_Z = 122;
	private static final int ASCII_UNDERSCORE = 95;
	private static final int ASCII_DOLLAR = 36;
	
	private Map<Character, RawModel> asciiToModel;
	private Map<Character, Cube> asciiToModelTemp;
	private RenderEngine re;
	
	public Text3D(Loader loader, RenderEngine re){
		this.re = re;
		//asciiToModel = new HashMap<Character, RawModel>();
		asciiToModelTemp = new HashMap<Character, Cube>();
		//initModels(loader);
		initModelsTemp(loader);
	}

	// TODO models need to be generated
	private void initModels(Loader loader) {
		
		asciiToModel.put((char)ASCII_DOLLAR, OBJLoader.loadObjModel("dollar.obj", loader));
		asciiToModel.put((char)ASCII_UNDERSCORE, OBJLoader.loadObjModel("dollar.obj", loader));
		
		for(int i = ASCII_LOWER_A; i <= ASCII_LOWER_Z; i++)
			asciiToModel.put((char)i, OBJLoader.loadObjModel((char)i+".obj", loader));
		
		for(int i = ASCII_UPPER_A; i <= ASCII_UPPER_Z; i++)
			asciiToModel.put((char)i, OBJLoader.loadObjModel((char)i+".obj", loader));
		
	}
	
	// Use until models are done
	private void initModelsTemp(Loader loader) {
		
		asciiToModelTemp.put((char)ASCII_DOLLAR, new Cube(0, 0, 0, 0, 0, 0, 1, Colour.AQUA));
		asciiToModelTemp.put((char)ASCII_UNDERSCORE, new Cube(0, 0, 0, 0, 0, 0, 1, Colour.ORANGE));
		
		for(int i = ASCII_LOWER_A; i <= ASCII_LOWER_Z; i++)
			asciiToModelTemp.put((char)i, new Cube(0, 0, 0, 0, 0, 0, 1, Colour.RED));
		
		for(int i = ASCII_UPPER_A; i <= ASCII_UPPER_Z; i++)
			asciiToModelTemp.put((char)i, new Cube(0, 0, 0, 0, 0, 0, 1, Colour.YELLOW));
		
	}
	
	public void print(float x, float y, float z, float rotX, float rotY, float rotZ, float scale, Colour col, String message) throws Exception{
		
		char[] chars = message.toCharArray();
		
		for(char c : chars){
			
			RawModel model = asciiToModel.get(c);
			
			if(model == null)
				throw new Exception("Not a valid String");
			
			Letter l = new Letter(x, y, z, rotX, rotY, rotZ, scale, col, model);
			re.addShapeTo3DSpace(l);
		}
	}
	
	public void printTemp(float x, float y, float z, float rotX, float rotY, float rotZ, float scale, Colour col, String message) throws Exception{
		
		char[] chars = message.toCharArray();
		
		for(char c : chars){
			
			Cube model = asciiToModelTemp.get(c);
			
			if(model == null)
				throw new Exception("Not a valid String");
			
			model.setPosition(x, y, z);
			model.getEntity().setScale(scale);
			
			re.addShapeTo3DSpace(model);
			x++;
		}
	}
	
}
