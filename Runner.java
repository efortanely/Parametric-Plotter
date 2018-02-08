import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Runner extends Engine implements EquationTypes {
	final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;
	private ArrayList<Graph> objects;
	private double θ, φ;
	private Point cursor;
	private Point disp;
	private boolean[] dir;

	@Override
	public void first(Graphics2D g) {
		System.out.println("Running.");
		this.objects = new ArrayList<>();
		this.θ = -2.5132741228718345;
		this.φ = -3.9793506945470702;
		this.cursor = new Point(0, 0);
		this.disp = new Point(0, 0);
		this.dir = new boolean[4];

		Function swirl = (θ, φ) -> {
			double r = 2 + Math.sin(7 * θ + 5 * φ);
			return new Vector(r * Math.cos(θ) * Math.sin(φ), r * Math.sin(θ) * Math.sin(φ), r * Math.cos(φ));
		};
		Function tunnel = (θ, φ) -> new Vector(θ, φ, 1.0 / Math.sqrt(Math.pow(θ, 2) + Math.pow(φ, 2)));
		Function napkin = (θ, φ) -> new Vector(θ, φ, .5 * Math.sin(θ * φ));

		Graph trefoilTorus = new TorusKnot(1.5, 2, 4, 1, 3);
		Graph knot = new TorusKnot(.5, 2, 4, 5, 7);
		Graph hemisphere = new Sphere(100, new double[] { 0, Math.PI }, new double[] { 0, Math.PI });

		Graph twisty = new ParametricPlot(swirl, new double[] { 0, 2 * Math.PI },
				new double[] { 0, 2 * Math.PI }, Math.PI / 50, Math.PI / 50);
		Graph blackHole = new ParametricPlot(50, tunnel, new double[] { -Math.PI, Math.PI },
				new double[] { -Math.PI, Math.PI });
		Graph wrinkledSheet = new ParametricPlot(napkin, new double[] { -2 * Math.PI, 2 * Math.PI },
				new double[] { -2 * Math.PI, 2 * Math.PI });

		this.objects.add(trefoilTorus);
	}

	@Override
	public void loop(Graphics2D go) {
		Graphics2D g = (Graphics2D) go.create();
		g.setColor(new Color(30, 30, 30));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.translate(this.getWidth() / 2, this.getHeight() / 2);

		double camAngleθ = 3 * Math.PI / 2 - this.θ;
		double camAngleφ = Math.PI / 2 + this.φ;
		Renderer.setCameraAngle(camAngleθ, camAngleφ);
		Renderer.renderAll(g, this.objects, this.θ, this.φ);
		this.moveCamera();

		g.dispose();
	}

	public void moveCamera() {
		this.θ += this.disp.x * 0.1;
		if (this.dir[this.LEFT])
			this.θ += Math.PI / 15;
		if (this.dir[this.RIGHT])
			this.θ -= Math.PI / 15;

		this.φ += this.disp.y * 0.1;
		if (this.dir[this.UP])
			this.φ -= Math.PI / 15;
		if (this.dir[this.DOWN])
			this.φ += Math.PI / 15;

		for (int i = 0; i < this.dir.length; i++)
			this.dir[i] = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		this.disp.x = e.getX() - this.cursor.x;
		this.disp.y = e.getY() - this.cursor.y;
		this.cursor = e.getPoint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.cursor = e.getPoint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.disp.x = 0;
		this.disp.y = 0;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			this.dir[this.UP] = true;
			break;
		case KeyEvent.VK_DOWN:
			this.dir[this.DOWN] = true;
			break;
		case KeyEvent.VK_LEFT:
			this.dir[this.LEFT] = true;
			break;
		case KeyEvent.VK_RIGHT:
			this.dir[this.RIGHT] = true;
			break;
		}
	}
}
