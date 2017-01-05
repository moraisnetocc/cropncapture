package com.example.binarizao;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Divisor {
	private Bitmap img;
	private boolean [][] visited;
	
	public Divisor(Bitmap img) {
		this.img = img;
		visited = new boolean [img.getWidth()][img.getHeight()];
	}


	public static ArrayList<Bitmap> dividir(Bitmap imagem){
		ArrayList<Bitmap> letras = new ArrayList<Bitmap>();
		int width = imagem.getWidth();
		int height = imagem.getHeight();
		int yMin = 0;
		int yMax = height-1;
		for(int i = height/2; i < height; i++){
			boolean found = false;
			for(int j = 0; j < width; j++){
				if(Color.red(imagem.getPixel(i, j)) == 255)
					found = true;
				
			}
			if(!found){
				yMax = i;
				break;
			}
		}
		for(int i = height/2; i >= 0; i--){
			boolean found = false;
			for(int j = 0; j < width; j++){
				if(Color.red(imagem.getPixel(i, j)) == 255)
					found = true;
			}
			if(!found){
				yMin = i;
				break;
			}
		}
		
		int initLetra = 0;
		boolean isLetter = false;
		boolean found;
		
		for(int i = initLetra; i < width; i++){
			found = false;
			for(int j = yMin; j < yMax; j++){
				if(Color.red(imagem.getPixel(i, j)) == 255){
					isLetter = true;
					found = true;
				}
					
			}
			if(!found && isLetter){
				letras.add(Bitmap.createBitmap(imagem, initLetra,yMin, i-initLetra, yMax - yMin));
				initLetra = i;
				isLetter = false;
			}
		}
		return letras;
	}
	
	
	public static ArrayList<Bitmap> dividirMEAN(Bitmap imagem){
		Bitmap letras = null;
		ArrayList<Bitmap> letra = new ArrayList<Bitmap>();
		letra.clear();
		double mean = 0;
		double meanB = 0; 
		for(int i = 0; i < imagem.getHeight(); i++){
			for(int j = 0; j < imagem.getWidth(); j++){
				if(Color.green(imagem.getPixel(j, i)) > 0)
					mean++;
				else
					meanB++;
			}
		}
		letras = imagem;
		if(mean < meanB){ //ta de preto pro branco
			mean = 0;
			for(int i = 0; i < letras.getHeight(); i++){
				for(int j = 0; j < letras.getWidth(); j++){
					if(Color.green(letras.getPixel(j, i)) > 0)
						mean++;					
				}
				if((double)mean / letras.getWidth() < 0.27)
					for(int j = 0; j < letras.getWidth(); j++)
						letras.setPixel(j, i, Color.rgb(255, 255, 255));
				mean = 0;	
				
			}
		}
		else{//ta de branco pro preto
			mean = 0;
			for(int i = 0; i < letras.getHeight(); i++){
				for(int j = 0; j < letras.getWidth(); j++){
					if(Color.green(letras.getPixel(j, i)) == 0)
						mean++;					
				}
				if((double)mean / letras.getWidth() < 0.27)
					for(int j = 0; j < letras.getWidth(); j++)
						letras.setPixel(j, i, Color.rgb(0,0,0));
				mean = 0;	
				
			}
		}
		// recontagem de brancos e pretos. 
		mean = meanB = 0;
		for(int i = 0; i < letras.getHeight(); i++){
			for(int j = 0; j < letras.getWidth(); j++){
				if(Color.green(letras.getPixel(j, i)) > 0)
					mean++;
				else
					meanB++;
			}
		}

		int yMin = 0;
		int yMax = 0;
		int minTemp = 0;
		int maxTemp = 0;
		boolean flag = false;
		boolean found = false;
		if(mean > meanB){ // ta branco com objetos pretos
			for(int i = 0; i < letras.getHeight(); i++){
				for(int j = 0; j < letras.getWidth(); j++){
					found = false;
					if(Color.green(letras.getPixel(j, i)) == 0){
						found = true;
						break;
					}
				}
				if(found){
					if(!flag){
						flag = true;
						minTemp = i;
						maxTemp = i;
					}
					else{
						maxTemp = i;
					}
				}
				else{
					if(flag){
						flag = false;
						if(yMax - yMin < maxTemp - minTemp){
							yMax = maxTemp;
							yMin = minTemp;
						}
					}
				}
			}
		}else{
			for(int i = 0; i < letras.getHeight(); i++){
				for(int j = 0; j < letras.getWidth(); j++){
					found = false;
					if(Color.green(letras.getPixel(j, i)) > 0){
						found = true;
						break;
					}
				}
				if(found){
					if(!flag){
						flag = true;
						minTemp = i;
						maxTemp = i;
					}
					else{
						maxTemp = i;
					}
				}
				else{
					if(flag){
						flag = false;
						if(yMax - yMin < maxTemp - minTemp){
							yMax = maxTemp;
							yMin = minTemp;
						}
					}
				}
			}
		}
		/////// Recontando novamente
		mean = meanB = 0;
		for(int i = 0; i < letras.getHeight(); i++){
			for(int j = 0; j < letras.getWidth(); j++){
				if(Color.green(letras.getPixel(j, i)) > 0)
					mean++;
				else
					meanB++;
			}
		}
		if(meanB >  mean || Color.green(letras.getPixel(0, yMin)) == 0){
			for(int i = 0; i < letras.getHeight(); i++)
				for(int j = 0; j < letras.getWidth(); j++){
					if(Color.green(letras.getPixel(j, i)) == 0 )
						letras.setPixel(j, i, Color.rgb(255,255,255));
					else
						letras.setPixel(j, i, Color.rgb(0,0,0));
				}
		}

	////////////////////////////////////////////////////////////////////////////////////////////////	
			//////				//*	AGORA DIVIDINDO AS LETRAS	*//				///////
	///////////////////////////////////////////////////////////////////////////////////////////////
		
		flag = found = false;
		int xMin = 0;
		int xMax = 0;
		maxTemp = minTemp = 0;
		for(int i = 0; i < letras.getWidth(); i++){
			for(int j = 0; j < letras.getHeight(); j++){
				found = false;
				if(Color.green(letras.getPixel(i, j)) == 0){
					found = true;
					break;
				}
			}
			if(found){
				if(!flag){
					flag = true;
					xMin = i;
				}
			}
			else{
				if(flag){
					flag = false;
					xMax = i;
					if(xMax-xMin > 0)
						letra.add(Bitmap.createBitmap(letras,xMin,yMin,xMax-xMin,yMax-yMin));
				}
			}
		}
		if(flag)
			letra.add(Bitmap.createBitmap(letras,xMin,yMin,letras.getWidth()-1-xMin,yMax-yMin));
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////
				///////			//*	Retirando só as sete maiores areas. *//		////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		if(letra.size()>8){
			ArrayList seq = new ArrayList();
			seq.add(0);
			seq.add(1);
			seq.add(2);
			seq.add(3);
			seq.add(4);
			seq.add(5);
			seq.add(6);
			seq.add(7);
			int temp;
			for(int i = 8; i < letra.size(); i++){
				int minus = 0;
				for (int j = 1; j < 8; j++) {
					if(letra.get((int)seq.get(j)).getWidth() < letra.get(minus).getWidth())
						minus = j;
				}
				if(letra.get(i).getWidth()>letra.get(minus).getWidth()){
					seq.remove(minus);
					seq.add(i);
				}
			}
			
			
			ArrayList <Bitmap> retorno = new ArrayList<Bitmap>();
			retorno.add(letra.get((int)seq.get(0)));
			retorno.add(letra.get((int)seq.get(1)));
			retorno.add(letra.get((int)seq.get(2)));
			retorno.add(letra.get((int)seq.get(3)));
			retorno.add(letra.get((int)seq.get(4)));
			retorno.add(letra.get((int)seq.get(5)));
			retorno.add(letra.get((int)seq.get(6)));
			retorno.add(letra.get((int)seq.get(7)));
			return retorno;
		}
		else if(letra.size()>0)
			return letra;
		letra.add(letras);
		return letra;
	/*	if(xMax-xMin>0)
			return Bitmap.createBitmap(letras,xMin,yMin,xMax-xMin,yMax-yMin);
		return Bitmap.createBitmap(letras, 0, yMin, letras.getWidth(), yMax-yMin);
	/*	if(yMax > 0)
			return Bitmap.createBitmap(letras, 0, yMin, letras.getWidth(), yMax-yMin);
		return Bitmap.createBitmap(letras, 0, yMin, letras.getWidth(), letras.getHeight()-yMin);
	*/}
	
	
	
	
	
	
	
	
	
	
	
	
	
	private int prox(int x, int y, int ant){
		{
			try{
				if(!visited[x-1][y-1] && Color.red(img.getPixel(x-1, y-1)) > 0 && ant != 5)
					return 1;
			}catch(Exception e){}
			try{
				if(!visited[x][y-1] && Color.red(img.getPixel(x, y-1)) > 0 && ant != 6)
					return 2;
			}catch(Exception e){}
			try{
				if(!visited[x+1][y-1] && Color.red(img.getPixel(x+1, y-1)) > 0 && ant != 7)
					return 3;
			}catch(Exception e){}
			try{
				if(!visited[x+1][y] && Color.red(img.getPixel(x+1, y)) > 0 && ant != 8)
					return 4;
			}catch(Exception e){}
			try{
				if(!visited[x+1][y+1] && Color.red(img.getPixel(x+1, y+1)) > 0 && ant != 1)
					return 5;
			}catch(Exception e){}
			try{
				if(!visited[x][y+1] && Color.red(img.getPixel(x, y+1)) > 0 && ant != 2)
					return 6;
			}catch(Exception e){}
			try{
				if(!visited[x-1][y+1] && Color.red(img.getPixel(x-1, y+1)) > 0 && ant != 3)
					return 7;
			}catch(Exception e){}
			try{
				if(!visited[x-1][y] && Color.red(img.getPixel(x-1, y)) > 0 && ant != 4)
					return 8;
			}catch(Exception e){}
			
		}
		
		return 0;
	}
	
	
	
	
	public ArrayList<Bitmap> dividirBT(){
		ArrayList<Bitmap> letras = new ArrayList<Bitmap>();
	//	boolean [][] visited = new boolean[img.getWidth()][img.getHeight()];
		int y = 0;
		int x = 0;
		int xMin, xMax, yMin, yMax, xAtual = 0, yAtual = 0;
		boolean found = false;
		while(true){
			for(; x < img.getWidth(); x++){
				for(; y < img.getHeight(); y++){
						if(!visited[x][y] && Color.red(img.getPixel(x, y)) == 255){
							found = true;
							break;
						}
				}
				if(found) break;
				else y = 0;
			}
			if (!found)
				return letras;
			found = false;
			xMax = xMin = xAtual = x;
			yMax = yMin = yAtual = y;
			int passo = -1;
			do{
				passo = prox(xAtual, yAtual, passo);
				
				if(passo == 0){ 
					visited[x][y] = true;
					break;
				}
				else{
					switch (passo) {
					case 1:
						xAtual--;
						yAtual--;
						break;
					case 2:
						yAtual--;
						break;
					case 3:
						xAtual++;
						yAtual--;
						break;
					case 4:
						xAtual++;
						break;
					case 5:
						xAtual++;
						yAtual++;
						break;
					case 6:
						yAtual++;
						break;
					case 7:
						xAtual--;
						yAtual++;
						break;
					case 8:
						xAtual--;
						break;
					}
					
					visited[xAtual][yAtual] = true;
					
					if(xMin > xAtual)
						xMin = xAtual;
					if(yMin > yAtual)
						yMin = yAtual;
					
					if(xMax < xAtual)
						xMax = xAtual;
					if(yMax < yAtual)
						yMax = yAtual;
					
					if(x == xAtual && y == yAtual){
						xMin = (xMin-1 < 0)? 0 : xMin-10;
					//	yMin = (yMin-25 < 0)? 0 : yMin-25;
						letras.add(Bitmap.createBitmap(img, xMin, 0, xMax+1<img.getWidth()?xMax-xMin+1:img.getWidth()-xMin, img.getHeight()-1));
						x = xMax+1;
						break;
					}
				}
			}while(true);
		}
	}
	
	
}
