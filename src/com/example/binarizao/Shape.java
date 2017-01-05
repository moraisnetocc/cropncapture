package com.example.binarizao;

import android.util.Pair;

public class Shape {
	private int k;
	private double[][] M;
	//private Complexo[] z0;
	private Pair[][] ws;
	
	public Shape(int k){
		
		
	}
	
	
	/*public double[][] prod_MxZ( double[][] in ){
		int m = 39;
		int n = 40;
		double saida [][] = new double[2][m];
	    
        for (int j = 0; j < m; j++) {
          double sr = 0;
          double si = 0;
          for (int l = 0; l < n; l++) {
            sr = sr + (M[j][l] * in[0][l]);
            si = si + (M[j][l] * in[1][l]);
          }
          saida[0][j] = sr;
          saida[1][j] = sr;
        }  
		return saida;
	}
	
	
	public Pair[] gerarW(Pair[] z0){
		Pair[] W = new Pair[k-1];
		
		for(int i = 0; i < k-1; i++){
			double count = 0;
			double countIm = 0;
			for(int j = 0; j < k; j++){
				count += (double)z0[j].first * M[j][i];
				countIm += (double)z0[j].second * M[j][i];
			}
			W[i].create(count, countIm);
		}
		return W;
	}*/
	private Pair produto(Pair z1, Pair z2){
		return new Pair((((double)z1.first * (double)z2.first) - ((double)z1.second * (double)z2.second)),
							 (((double)z1.first * (double)z2.second) + ((double)z1.second * (double)z2.first)));

	}
	private Pair soma(Pair z1, Pair z2){
		return new Pair((double)z1.first + (double)z2.first, (double)z1.second + (double)z2.second);
	}
	
	public Pair dist(Pair []z1){
		Pair ret = null;
	//	d(w,y) = (1-[c(y) w c(w) y]/[c(w) w c(y) y ])
		Pair num1, num2, den1, den2;
		for(int j = 0; j < 36)
		num1.create(0,0);
		num2.create(0,0);
		den1.create(0,0);
		den2.create(0,0);
		for(int i = 0; i < 39; i++){
		       num1 = num1 + produto(conj(z1[i]),ws[0][i]);
		       num2 = num2 + conj(ws(i,j))*w0(i);

		       den1 = den1 + conj(ws(i,j))* ws(i,j);
		       den2 = den2 + conj(w0(i))* w0(i);
		}
		
		
		return ret;
	}
	
	

}
