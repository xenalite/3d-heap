package com.graphics.text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	private static final int ASCII_NUMBER_0 = 48;
	private static final int ASCII_NUMBER_9 = 57;
	private static final int ASCII_RIGHT_SQUARE_BRACKET = 93;
	private static final int ASCII_LEFT_SQUARE_BRACKET = 91;
	private static final int ASCII_RIGHT_CURLY_BRACKET = 125;
	private static final int ASCII_LEFT_CURLY_BRACKET = 123;
	private static final int ASCII_DOT = 46;
	private static final int ASCII_COLON = 58;
	
	private Map<Character, RawModel> asciiToModel;
	private RenderEngine re;
	private Colour colour;
	private List<Model> models;
	
	public Text3D(Loader loader, RenderEngine re, Colour c){
		this.re = re;
		asciiToModel = new HashMap<Character, RawModel>();
		this.colour = c;
		initModels(loader);
		models = new ArrayList<Model>();
	}

	// TODO models need to be generated
	private void initModels(Loader loader) {
		
		String prefix = "letters/";
		
		asciiToModel.put((char)ASCII_DOLLAR, OBJLoader.loadObjModel(true, prefix + "special/dollar.obj", loader, colour));
		asciiToModel.put((char)ASCII_UNDERSCORE, OBJLoader.loadObjModel(true, prefix + "special/underscore.obj", loader, colour));
		asciiToModel.put((char)ASCII_LEFT_SQUARE_BRACKET, OBJLoader.loadObjModel(true, prefix + "special/left_square_bracket.obj", loader, colour));
		asciiToModel.put((char)ASCII_RIGHT_SQUARE_BRACKET, OBJLoader.loadObjModel(true, prefix + "special/right_square_bracket.obj", loader, colour));
		asciiToModel.put((char)ASCII_LEFT_CURLY_BRACKET, OBJLoader.loadObjModel(true, prefix + "special/left_curly_bracket.obj", loader, colour));
		asciiToModel.put((char)ASCII_RIGHT_CURLY_BRACKET, OBJLoader.loadObjModel(true, prefix + "special/right_curly_bracket.obj", loader, colour));
		asciiToModel.put((char)ASCII_DOT, OBJLoader.loadObjModel(true, prefix + "special/dot.obj", loader, colour));
		asciiToModel.put((char)ASCII_COLON, OBJLoader.loadObjModel(true, prefix + "special/colon.obj", loader, colour));
		
		for(int i = ASCII_LOWER_A; i <= ASCII_LOWER_Z; i++)
			asciiToModel.put((char)i, OBJLoader.loadObjModel(true, prefix + "lower/" + (char)i + ".obj", loader, colour));
		
		for(int i = ASCII_UPPER_A; i <= ASCII_UPPER_Z; i++)
			asciiToModel.put((char)i, OBJLoader.loadObjModel(true, prefix + "upper/" + (char)i+".obj", loader, colour));
		
		for(int i = ASCII_NUMBER_0; i <= ASCII_NUMBER_9; i++)
			asciiToModel.put((char)i, OBJLoader.loadObjModel(true, prefix + "numbers/" + (char)i+".obj", loader, colour));
		
	}
	
	public void print(float x, float y, float z, float rotX, float rotY, float rotZ, float scale, String message) throws Exception{
		
		char[] chars = message.toCharArray();
		
		for(char c : chars){
			
			RawModel model = asciiToModel.get(c);
			
			if(model == null){
				if(c == ' '){
					x+=3.8*scale;
					continue;
				}
				throw new Exception("Invalid char: " + c);
			}
				
			
			Model l = new Model(x, y, z, rotX, rotY, rotZ, scale, colour, model);
			models.add(l);
			re.addShapeTo3DSpace(l);
			x+=3.8*scale;
		}
	}
	
	public void clearText(){
		for(Model m : models)
			re.removeShapeFrom3DSpace(m);
		models.clear();
	}
	
}
