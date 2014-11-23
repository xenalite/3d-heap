package com.graphics.raycasting;

import org.lwjgl.util.vector.Vector3f;

import com.graphics.entities.Entity;
import com.graphics.shapes.Shape;

public class RayCastUtil {

	public static Vector3f triag1 = new Vector3f();
	public static Vector3f triag2 = new Vector3f();
	public static Vector3f triag3 = new Vector3f();
	public static Vector3f normal = new Vector3f();
	public static final float ACCURACY = 0.00001f;

	public static Vector3f rayTest(Vector3f rayOrigin, Vector3f rayDirection,
			Shape shape) {
		
		Entity e = shape.getEntity();
		float[] vertexes = shape.getVertices();
		float[] normals = shape.getNormals();
		Vector3f pos = e.getPosition();
		Vector3f result = null;
		float resultDist = Float.MAX_VALUE;

		for (int i = 0; i < vertexes.length;) {
			// fix we need to adjust the triangle coords by the model position
			Vector3f normal = new Vector3f(normals[i + 0], normals[i + 1],
					normals[i + 2]);
			Vector3f point1 = new Vector3f(vertexes[i] + pos.x, vertexes[i + 1]
					+ pos.y, vertexes[i + 2] + pos.z);
			Vector3f point2 = new Vector3f(vertexes[i + 3] + pos.x,
					vertexes[i + 4] + pos.y, vertexes[i + 5] + pos.z);
			Vector3f point3 = new Vector3f(vertexes[i + 6] + pos.x,
					vertexes[i + 7] + pos.y, vertexes[i + 8] + pos.z);
			Vector3f tempResult = rayTest(rayOrigin, rayDirection, normal,
					point1, point2, point3);
			if (tempResult != null) {
			//	float xd = tempResult.x-rayOrigin.x;
			//	float yd = tempResult.y-rayOrigin.y;
			//	float zd = tempResult.z-rayOrigin.z;
			//	float d = (float)Math.sqrt(xd*xd + yd*yd + zd*zd);
			//	if (d < resultDist) {
			//		resultDist = d;
					result = tempResult;
			//	}
			}
			i += 9;
		}
		return result;
	}

	public static Vector3f rayTest(Vector3f rayOrigin, Vector3f rayDirection,
			Vector3f planeNormal, Vector3f trianglePoint1,
			Vector3f trianglePoint2, Vector3f trianglePoint3) {
		System.out.println("RayStart = " + rayOrigin);
		System.out.println("RayDir = " + rayDirection);
		System.out.println("IntPoint");

		triag1 = trianglePoint1;
		triag2 = trianglePoint2;
		triag3 = trianglePoint3;
		normal = planeNormal;

		// nx, ny, nz : normal vector of the plane
		// x0, y0, z0 = a point on the plane

		// RAY properties...
		// x = xs+t*xd
		// y = ys+t*yd
		// z = zs+t*zd
		// xs, ys, zs = starting point of ray
		// xd, yd, zd = direction of ray

		// the raw equation we use.
		// t=(x0*nx+y0*ny+z0*nz-nx*nx-ny*ya-nz*zs) / (xd*nx+yd*ny+zd*nz)
		// / <this part is the dot factor

		Vector3f pointOnPLane = trianglePoint1; // any point should work as long
												// as its on the plane.
		// a= xd * nx + yd * ny + zd * nz;
		float dot = rayDirection.x * planeNormal.x + rayDirection.y
				* planeNormal.y + rayDirection.z * planeNormal.z;
		System.out.println("DOT = " + dot);
		float t = 0;
		// if dot = 0 then the ray is pararel and will never intersect the
		// plane. T should = 0 in this case.
		// if dot < 0 then the ray intersects but it does so in the opposite
		// direction of the cast. so basically this should be treated as a miss.
		// leave t equal to 0
		if (dot == 0) {
			return null; // this line is parrel and will never touch
		}
		// (p1.x * nx + p1.y * ny + p1.z * nz - nx * xs - ny * ys - nz * zs)
		float coordRatio = pointOnPLane.x * planeNormal.x + pointOnPLane.y
				* planeNormal.y + pointOnPLane.z * planeNormal.z
				- planeNormal.x * rayOrigin.x - planeNormal.y * rayOrigin.y
				- planeNormal.z * rayOrigin.z;
		t = coordRatio / dot;
		System.out.println("T = " + t);
		if (t < 0) { // if t<0 then they intersect but the direction of
						// intersection is opposite the cast direction.
			return null;
		}
		// t can be thought of like a distance factor down the ray. Using it we
		// can resolve the exact points (x,y,z) where the ray hits the plane.
		// now lets figure out the intersect point

		float intX = rayOrigin.x + t * rayDirection.x;
		float intY = rayOrigin.y + t * rayDirection.y;
		float intZ = rayOrigin.z + t * rayDirection.z;

		Vector3f intPoint = new Vector3f(intX, intY, intZ);
		System.out.println("IntPoint = " + intPoint);

		float fullArea = calculateTriangleArea(trianglePoint1, trianglePoint2,
				trianglePoint3);
		System.out.println("Full Area = " + fullArea);

		float subTriangle1 = calculateTriangleArea(trianglePoint1,
				trianglePoint2, intPoint);
		System.out.println("Area1 = " + subTriangle1);

		float subTriangle2 = calculateTriangleArea(trianglePoint2,
				trianglePoint3, intPoint);
		System.out.println("Area2 = " + subTriangle2);

		float subTriangle3 = calculateTriangleArea(trianglePoint1,
				trianglePoint3, intPoint);
		System.out.println("Area3 = " + subTriangle3);

		float totalSubAreas = subTriangle1 + subTriangle2 + subTriangle3;
		System.out.println("Area Total = " + totalSubAreas);

		// have an accuracy approach is import cause rounding errors will happen
		// when doing Math.sqr. !
		if (Math.abs(fullArea - totalSubAreas) < ACCURACY) {
			System.out.println("HIT");
			return intPoint;
		} else {
			System.out.println("MISS");
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
