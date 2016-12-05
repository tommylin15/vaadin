package com.scsb.vaadin.r.temp;


public class RVector {

	public enum Type {
		INTEGER, NUMERIC, CHARACTER
	};

	private Type type;

	int[] iv = null;
	double[] dv = null;
	String[] sv = null;

	public RVector(int[] dv) {
		this.iv = dv;
		type = Type.INTEGER;
	}

	public RVector(double[] dv) {
		this.dv = dv;
		type = Type.NUMERIC;
	}

	public RVector(String[] sv) {
		this.sv = sv;
		type = Type.CHARACTER;
	}

	public Type type() {
		return type;
	}

	public int length() {
		if (type == Type.INTEGER) {
			return iv.length;
		}
		if (type == Type.NUMERIC) {
			return dv.length;
		}
		if (type == Type.CHARACTER) {
			return sv.length;
		
		} else {
			// An error
			return -1;
		}
	}

	/**
	 * Get value with no implicit conversion in case of incompatible type.
	 * 
	 * @return int[]
	 */
	public int[] getInts() {
		if (type == Type.INTEGER) {
			return iv;
		}
		return null;
	}

	/**
	 * Get value with no implicit conversion in case of incompatible type.
	 * 
	 * @return double[]
	 */
	public double[] getdoubles() {
		if (type == Type.NUMERIC) {
			return dv;
		}
		return null;
	}

	/**
	 * Get value with no implicit conversion in case of incompatible type.
	 * 
	 * @return String[]
	 */
	public String[] getStrings() {
		if (type == Type.CHARACTER) {
			return sv;
		}
		return null;
	}
}
