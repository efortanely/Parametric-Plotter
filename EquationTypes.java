public interface EquationTypes {
	public interface Function {
		Vector funct(double θ, double φ);
	}

	public interface Knot {
		Vector funct(double r, double a, double d, double p, double q, double θ, double φ);
	}

	public interface Spheroid {
		Vector funct(double ρ, double θ, double φ);
	}
}
