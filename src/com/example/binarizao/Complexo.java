package com.example.binarizao;

public class Complexo {
	private double r1,r2;
	private double imagi;
	
	
	public Complexo(double v1, double v2, double imaginario){
		setR1(v1);
		setR2(v2);
		setImagi(imaginario);
	}
	public Complexo(double v1, double v2){
		setR1(v1);
		setR2(v2);
		setImagi(-1);
	}
	
	public double getR2() {
		return r1;
	}
	public void setR2(double real) {
		this.r2 = real;
	}
	public double getR1() {
		return r1;
	}
	public void setR1(double real) {
		this.r1 = real;
	}
	public double getImagi() {
		return imagi;
	}
	public void setImagi(double imagi) {
		this.imagi = imagi;
	}

	public static double[] multiWW(Complexo a, Complexo b){
		double[] saida = new double[3];
		saida[1] = a.getR1()*a.getR1();
		saida[2] = b.getR2()*b.getR2();
		saida[0] = saida[1] + saida[2];
		return saida;
	}
	
	
	
	
}
