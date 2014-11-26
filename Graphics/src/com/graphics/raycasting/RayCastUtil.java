package com.graphics.raycasting;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import com.graphics.entities.Entity;
import com.graphics.shapes.Shape;
import com.graphics.utils.Maths;

public class RayCastUtil {

	public static final float ACCURACY = 0.00001f;

	private static Vector3f transform4fTo3f(Entity e, int p, float[] vertexes, int zeroOrOne) {
		
		Vector4f point_4 = new Vector4f(vertexes[p], vertexes[p + 1], vertexes[p + 2], zeroOrOne);
		point_4 = Matrix4f.transform(Maths.createTransformationMatrix(e.getPosition(), 0, 0, 0, e.getScale()), point_4, point_4);
		Vector3f point = new Vector3f(point_4.x, point_4.y, point_4.z);

		return point;
	}

	public static Vector3f rayTest(Vector3f rayOrigin, Vector3f rayDirection, Shape shape) {
		
		Entity e = shape.getEntity();
		float[] vertexes = shape.getVertices();
		float[] normals = shape.getNormals();
		int[] indicies = shape.getIndices();
		Vector3f result = null;
		float resultDist = Float.MAX_VALUE;

		for (int i = 0; i < indicies.length;) {
			
			int indNorm1 = indicies[i]*3;

			Vector3f normal = transform4fTo3f(e, indNorm1, normals, 0);

			int indPoint1 = indicies[i] * 3;
			Vector3f point1 = transform4fTo3f(e, indPoint1, vertexes, 1);

			int indPoint2 = indicies[i+1] * 3;		
			Vector3f point2 = transform4fTo3f(e, indPoint2, vertexes, 1);

			int indPoint3 = indicies[i+2] * 3;			
			Vector3f point3 = transform4fTo3f(e, indPoint3, vertexes, 1);		

			Vector3f tempResult = rayTestOnTriangle(rayOrigin, rayDirection, normal, point1, point2, point3);

			if (tempResult != null) {
				float xd = tempResult.x-rayOrigin.x;
				float yd = tempResult.y-rayOrigin.y;
				float zd = tempResult.z-rayOrigin.z;
				float d = (float)Math.sqrt(xd*xd + yd*yd + zd*zd);
				
				if (d < resultDist) {
					resultDist = d;
					result = tempResult;
				}
			}

			i += 3;
		}
		return result;
	}

	private static Vector3f rayTestOnTriangle(Vector3f rayOrigin, Vector3f rayDirection,
			Vector3f planeNormal, Vector3f trianglePoint1,
			Vector3f trianglePoint2, Vector3f trianglePoint3) {

		Vector3f pointOnPLane = trianglePoint1;
		
		float dot = rayDirection.x * planeNormal.x + rayDirection.y
				* planeNormal.y + rayDirection.z * planeNormal.z;
		
		/*
		 * if dot = 0 then the ray is pararel and will never intersect the
		 * plane. T should = 0 in this case. if dot < 0 then the ray intersects
		 * but it does so in the opposite direction of the cast. so basically
		 * this should be treated as a miss. leave t equal to 0
		 */
		if (dot <= 0) 
			return null;
		
		float coordRatio = pointOnPLane.x * planeNormal.x + pointOnPLane.y
				* planeNormal.y + pointOnPLane.z * planeNormal.z
				- planeNormal.x * rayOrigin.x - planeNormal.y * rayOrigin.y
				- planeNormal.z * rayOrigin.z;

		float t = (coordRatio / dot);

		// if t<0 then they intersect but the direction of intersection is opposite the cast direction.
		if (t < 0)
			return null;

		float intX = rayOrigin.x + t * rayDirection.x;
		float intY = rayOrigin.y + t * rayDirection.y;
		float intZ = rayOrigin.z + t * rayDirection.z;

		Vector3f intPoint = new Vector3f(intX, intY, intZ);

		float fullArea = calculateTriangleArea(trianglePoint1, trianglePoint2,
				trianglePoint3);

		float subTriangle1 = calculateTriangleArea(trianglePoint1,
				trianglePoint2, intPoint);

		float subTriangle2 = calculateTriangleArea(trianglePoint2,
				trianglePoint3, intPoint);

		float subTriangle3 = calculateTriangleArea(trianglePoint1,
				trianglePoint3, intPoint);

		float totalSubAreas = subTriangle1 + subTriangle2 + subTriangle3;

		if (Math.abs(fullArea - totalSubAreas) < ACCURACY) {
			// Hit
			return intPoint;
		} else {
			//Miss
			return null;
		}

	}

	private static float calculateTriangleArea(Vector3f p1, Vector3f p2,
			Vector3f p3) {
		float a = (float) Math
				.sqrt((p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y)
						* (p2.y - p1.y) + (p2.z - p1.z) * (p2.z - p1.z));
		float b = (float) Math
				.sqrt((p3.x - p2.x) * (p3.x - p2.x) + (p3.y - p2.y)
						* (p3.y - p2.y) + (p3.z - p2.z) * (p3.z - p2.z));
		float c = (float) Math
				.sqrt((p3.x - p1.x) * (p3.x - p1.x) + (p3.y - p1.y)
						* (p3.y - p1.y) + (p3.z - p1.z) * (p3.z - p1.z));
		float s = (a + b + c) / 2;
		float result = (float) Math.sqrt(s * (s - a) * (s - b) * (s - c));
		return result;
	}

}
