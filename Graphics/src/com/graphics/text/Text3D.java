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
	private List<Character> needBiggerGap, needSmallerGap;
	
	// Magic number constants to make text look correct
	private static final float GAP = 4f, BIGGER = 1.4f, SMALLER = 0.7f, UPPER_GAP = 5f, BRACKET_SCALE = 0.6f, EXTRA_GAP = 2f;
	
	public Text3D(Loader loader, RenderEngine re, Colour c){
		this.re = re;
		asciiToModel = new HashMap<Character, RawModel>();
		this.colour = c;
		initModels(loader);
		models = new ArrayList<Model>();
		
		needBiggerGap = new ArrayList<Character>();
		needSmallerGap = new ArrayList<Character>();
		
		needSmallerGap.add('i');
		needSmallerGap.add('f');
		needSmallerGap.add('j');
		needSmallerGap.add('r');
		needSmallerGap.add('t');
		needSmallerGap.add('l');
		needSmallerGap.add('I');
		needSmallerGap.add('J');
		
		needBiggerGap.add('m');
		needBiggerGap.add('M');
		needBiggerGap.add('w');
		needBiggerGap.add('W');
		needBiggerGap.add('[');
		needBiggerGap.add('{');
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
					x+=GAP*scale;
					continue;
				}
				throw new Exception("Invalid char: " + c);
			}
				
			if(c == '{' || c == '[' || c == '}' || c == ']')
				x-=GAP*BRACKET_SCALE*scale;
			
			Model l = new Model(x, y, z, rotX, rotY, rotZ, scale, colour, model);
			models.add(l);
			re.addShapeTo3DSpace(l);
			
			if(c == '{' || c == '[' || c == 'W')
				x+=GAP*EXTRA_GAP*scale;
			else if(needBiggerGap.contains(c))
				x+=GAP*BIGGER*scale;
			else if(needSmallerGap.contains(c))
				x+=GAP*SMALLER*scale*(c == 'l' ? 0.7f : 1f);
			else if(Character.isUpperCase(c))
				x+=UPPER_GAP*scale;
			else
				x+=GAP*scale;
		}
	}
	
	public void clearText(){
		for(Model m : models)
			re.removeShapeFrom3DSpace(m);
		models.clear();
	}
	
}
