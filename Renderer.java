import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;

public class Renderer {
	private static boolean sceneHasChanged;
	public static Vector camera;
	private static double cameraAngleθ;
	private static double cameraAngleφ;
	private static double cameraDistanceFromOrigin;
	private static Plane[] renderedScene;

	public static void setCameraAngle(double camAngleθ, double camAngleφ) {
		if (Renderer.cameraAngleθ != camAngleθ && Renderer.cameraAngleφ != camAngleφ) {
			Renderer.sceneHasChanged = true;
			Renderer.cameraAngleθ = camAngleθ;
			Renderer.cameraAngleφ = camAngleφ;
		} else {
			Renderer.sceneHasChanged = false;
		}
	}

	public static void setCameraDistance(ArrayList<Graph> worldObjects) {
		double maxDist = Double.MIN_VALUE;
		for (Graph object : worldObjects) {
			object.generateGraph();
			maxDist = Math.max(maxDist, object.distanceToOriginSquared);
		}
		Renderer.cameraDistanceFromOrigin = 10 * Math.sqrt(maxDist);
	}

	public static void renderAll(Graphics2D g, ArrayList<Graph> worldObjects, double renderAngleθ,
			double renderAngleφ) {
		if (Renderer.renderedScene == null) {
			Renderer.setCameraDistance(worldObjects);
			Renderer.renderedScene = Renderer.renderByDistance(worldObjects, renderAngleθ,
					renderAngleφ);
		} else if (Renderer.sceneHasChanged) {
			Renderer.renderedScene = Renderer.renderByDistance(worldObjects, renderAngleθ,
					renderAngleφ);
		}

		for (Plane polygon : Renderer.renderedScene) {
			polygon.draw(g);
		}
	}

	public static Plane[] renderByDistance(ArrayList<Graph> objects, double renderAngleθ, double renderAngleφ) {
		Renderer.camera = new Vector("spherical", Renderer.cameraDistanceFromOrigin, Renderer.cameraAngleθ,
				Renderer.cameraAngleφ);
		Plane.setCamera(Renderer.camera);

		ArrayList<Plane> polygons = new ArrayList<>();
		for (Graph object : objects) {
			polygons.addAll(object.getPolygons(Renderer.camera, renderAngleθ, renderAngleφ));
		}

		// sort from back to front (Painter's algorithm)
		Plane[] sortedPoints = polygons.toArray(new Plane[polygons.size()]);
		Arrays.sort(sortedPoints,
				(pt1, pt2) -> (int) (pt2.getSquaredDistanceFromCamera() - pt1.getSquaredDistanceFromCamera()));

		return sortedPoints;
	}
}
