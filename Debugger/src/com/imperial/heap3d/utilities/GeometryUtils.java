package com.imperial.heap3d.utilities;

import com.graphics.shapes.Colour;
import com.graphics.shapes.Cube;
import com.graphics.shapes.Shape;
import org.lwjgl.util.vector.Vector3f;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

/**
 * Created by oskar on 27/12/14.
 */
public class GeometryUtils {

    //region Factory Methods
    private static final float STACK_NODE_SCALE = 5f;
    private static final float HEAP_NODE_SCALE = 0.5f;

    public static Shape createCubeForStackNode() {
        return new Cube(0, 0, 0, 0, 0, 0, STACK_NODE_SCALE, ColorConverter.randomColour());
    }

    public static Shape createCubeForObjectNode() {
        return new Cube(0, 0, 0, 0, 0, 0, HEAP_NODE_SCALE, Colour.ORANGE);
    }

    public static Shape createCubeForStringNode() {
        return new Cube(0, 0, 0, 0, 0, 0, HEAP_NODE_SCALE, Colour.RED);
    }

    public static Shape createCubeForArrayNode() {
        return new Cube(0, 0, 0, 0, 0, 0, HEAP_NODE_SCALE, Colour.YELLOW);
    }

    public static Shape createCubeForArrayElemNode() {
        return new Cube(0, 0, 0, 0, 0, 0, HEAP_NODE_SCALE, Colour.RED);
    }
    //endregion

    private static final float CUBE_WIDTH = 0.5F;

    /**
     * Returns a 2-element array for co-ordinates (dx,dz) representing the
     * point on the square where the line between 2 nodes will intersect.
     * (x1,z1) is the position of the FROM-node. (x2,z2) is the TO-node.
     * This only works for nodes on the same y-level.
     */
    public static float[] getIntersectionPoint(float x1, float z1, float x2, float z2, float scale) {
        float D = (float) Math.sqrt(Math.pow(z2 - z1, 2) + Math.pow(x2 - x1, 2));
        float angleRad = (float) Math.acos((x2 - x1) / D);
        float basicAngle = (float) Math.toDegrees(angleRad) % 90;

        float a = scale * CUBE_WIDTH;
        float d = (float) ((basicAngle <= 45) ? a / cos(toRadians(basicAngle)) : a / sin(toRadians(basicAngle)));
        float dx = (float) (x2 - d * cos(angleRad));
        float dz = (float) (z2 - ((z2 < z1) ? (-1) : 1) * d * sin(angleRad));

        return new float[]{dx, dz};
    }

    public static Vector3f add(Vector3f p, Vector3f q) {
        return new Vector3f(p.x + q.x, p.y + q.y, p.z + q.z);
    }

    public static Vector3f subtract(Vector3f p, Vector3f q) {
        return new Vector3f(p.x - q.x, p.y - q.y, p.z - q.z);
    }

    public static Vector3f multiplyScalar(Vector3f p, float s) {
        return new Vector3f(s * p.x, s * p.y, s * p.z);
    }

    public static Vector3f getIntersectionPoint(Vector3f from, Vector3f to, float scale) {
        float D = scale * CUBE_WIDTH;
        Vector3f minVertex = new Vector3f(to.x - D, to.y - D, to.z - D);
        Vector3f maxVertex = new Vector3f(to.x + D, to.y + D, to.z + D);
        return getIntersectionPoint(minVertex, maxVertex, from, to);
    }

    public static Vector3f getIntersectionPoint(Vector3f B1, Vector3f B2, Vector3f L1, Vector3f L2) {
        IntersectionUtils utils = new IntersectionUtils();
        return (utils.getIntersectionPoint(B1,B2,L1,L2) ? utils.intersection : null);
    }

    private static class IntersectionUtils {
        private Vector3f intersection = new Vector3f(0, 0, 0);

        private boolean getIntersection(float fDst1, float fDst2, Vector3f P1, Vector3f P2) {
            if ((fDst1 * fDst2) >= 0.0f) return false;
            if (Float.compare(fDst1, fDst2) == 0) return false;
            intersection = add(P1, multiplyScalar(subtract(P2, P1), (-fDst1 / (fDst2 - fDst1))));
            return true;
        }

        private boolean intersects(Vector3f B1, Vector3f B2, int Axis) {
            return Axis == 1 && intersection.z > B1.z && intersection.z < B2.z && intersection.y > B1.y && intersection.y < B2.y ||
                    Axis == 2 && intersection.z > B1.z && intersection.z < B2.z && intersection.x > B1.x && intersection.x < B2.x ||
                    Axis == 3 && intersection.x > B1.x && intersection.x < B2.x && intersection.y > B1.y && intersection.y < B2.y;
        }

        public boolean getIntersectionPoint(Vector3f B1, Vector3f B2, Vector3f L1, Vector3f L2) {
            if (L2.x < B1.x && L1.x < B1.x) return false;
            if (L2.x > B2.x && L1.x > B2.x) return false;
            if (L2.y < B1.y && L1.y < B1.y) return false;
            if (L2.y > B2.y && L1.y > B2.y) return false;
            if (L2.z < B1.z && L1.z < B1.z) return false;
            if (L2.z > B2.z && L1.z > B2.z) return false;
            if (L1.x > B1.x && L1.x < B2.x &&
                    L1.y > B1.y && L1.y < B2.y &&
                    L1.z > B1.z && L1.z < B2.z) {
                intersection = L1;
                return true;
            }

            return (getIntersection(L1.x - B1.x, L2.x - B1.x, L1, L2) && intersects(B1, B2, 1))
                    || (getIntersection(L1.y - B1.y, L2.y - B1.y, L1, L2) && intersects(B1, B2, 2))
                    || (getIntersection(L1.z - B1.z, L2.z - B1.z, L1, L2) && intersects(B1, B2, 3))
                    || (getIntersection(L1.x - B2.x, L2.x - B2.x, L1, L2) && intersects(B1, B2, 1))
                    || (getIntersection(L1.y - B2.y, L2.y - B2.y, L1, L2) && intersects(B1, B2, 2))
                    || (getIntersection(L1.z - B2.z, L2.z - B2.z, L1, L2) && intersects(B1, B2, 3));
        }
    }
}