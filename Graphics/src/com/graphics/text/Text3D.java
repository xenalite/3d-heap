package com.graphics.text;

import java.util.HashMap;
import java.util.Map;
import com.graphics.RenderEngine;
import com.graphics.models.Loader;
import com.graphics.models.RawModel;
import com.graphics.shapes.Colour;
import com.graphics.shapes.Model;
import com.graphics.utils.OBJLoader;

public class Text3D {

	private static final int ASCII_UPPER_A = 65;
	private static final int ASCII_UPPER_Z = 90;
	private static final int ASCII_LOWER_A = 97;
	private static final int ASCII_LOWER_Z = 122;
	private static final int ASCII_UNDERSCORE = 95;
	private static final int ASCII_DOLLAR = 36;
	
	private Map<Character, RawModel> asciiToModel;
	private RenderEngine re;
	
	public Text3D(Loader loader, RenderEngine re){
		this.re = re;
		asciiToModel = new HashMap<Character, RawModel>();
		initModels(loader);
	}

	// TODO models need to be generated
	private void initModels(Loader loader) {
		
		String prefix = "letters/";
		
		asciiToModel.put((char)ASCII_DOLLAR, OBJLoader.loadObjModel(true, prefix + "special/dollar.obj", loader, Colour.AQUA));
		asciiToModel.put((char)ASCII_UNDERSCORE, OBJLoader.loadObjModel(true, prefix + "special/underscore.obj", loader, Colour.AQUA));
		
		for(int i = ASCII_LOWER_A; i <= ASCII_LOWER_Z; i++)
			asciiToModel.put((char)i, OBJLoader.loadObjModel(true, prefix + "lower/" + (char)i + ".obj", loader, Colour.AQUA));
		
		for(int i = ASCII_UPPER_A; i <= ASCII_UPPER_Z; i++)
			asciiToModel.put((char)i, OBJLoader.loadObjModel(true, prefix + "upper/" + (char)i+".obj", loader, Colour.AQUA));
		
	}
	
	public void print(float x, float y, float z, float rotX, float rotY, float rotZ, float scale, Colour col, String message) throws Exception{
		
		char[] chars = message.toCharArray();
		
		for(char c : chars){
			
			RawModel model = asciiToModel.get(c);
			
			if(model == null)
				throw new Exception("Invalid char: " + c);
			
			Model l = new Model(x, y, z, rotX, rotY, rotZ, scale, col, model);
			re.addShapeTo3DSpace(l);
			x+=3.8*scale;
		}
	}
	
}
