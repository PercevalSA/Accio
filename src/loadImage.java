import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;

import org.bytedeco.javacv.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.helper.opencv_core.AbstractIplImage;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JRootPane;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;



public class loadImage {

	public static void main(String[] args) {
		try{
			IplImage img = cvLoadImage("cam15.jpg");
			ByteBuffer rgb_data = img.getByteBuffer();		
			int height = img.height();
			int width = img.width();

			int[][] matrice = new int[height][width];
			matrice = getBinaryImage(img);
			int[][] comp;
			comp = getComposantes(matrice);
			int[][]contour;
			contour = contour(comp);
			histogramme(img);
			int[][] bary = barycentre(comp);
			for(int i=21;i<29;i++){
				IplImage img1 = cvLoadImage("cam"+i+".jpg");
				histogramme(img1);
			}

			
			for(int i=0;i<height;i++){
				for(int j=0; j<width;j++){
					System.out.print(matrice[i][j]);
				}
				System.out.println(".");
			}
			
			System.out.println("Done");
			System.out.println(bary.length);
			System.out.println(bary[0][0]+","+bary[1][0]);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	
	public static int[][] getBinaryImage(IplImage img){
		int height = img.height();
		int width = img.width();
		int[][] matrice = new int[height][width];
		ByteBuffer rgb_data = img.getByteBuffer();
		BufferedImage bw = new BufferedImage(width,height,BufferedImage.TYPE_3BYTE_BGR);
		int max = (255<<16)|(255<<8)|255;

		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				int red = rgb_data.get(3*j + 3*i*width + 2);
				if(red<0){
					red = red + 256;
				}
				int green = rgb_data.get(3*j + 3*i*width + 1);
				if(green<0){
					green = green + 256;
				}
				int blue = rgb_data.get(3*j + 3*i*width);
				if(blue<0){
					blue = blue + 256;
				}

				float[] tsl = Color.RGBtoHSB(red, green, blue, null);
				float teinte = tsl[0];
				float saturation = tsl[1];
				float luminance = tsl[2];

				if(red<20 && blue<20 && green<20){
					bw.setRGB(j,i,max);
					matrice[i][j]=0;
				}
				else{
					bw.setRGB(j,i, 2);
					matrice[i][j]=1;
				}		
			}
		}


		// Partie correction d'erreurs

		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				int k=0;
				while(matrice[i][j+k]==1 && j+k<width-1){
					k++;
				}
				if(k<10){
					for(int l=0;l<k+1;l++){
						matrice[i][j+l]=0;
						bw.setRGB(j+l, i, max);
					}
				}
			}
		}

		File f = new File("bw.png");
		try {
			ImageIO.write(bw, "PNG", f);
		} catch (IOException e) {
		}
		return matrice;
	}
	
	public static int[][] contour(int[][] matrice){
		int height = matrice.length;
		int width = matrice[0].length;
		int[][] contour = new int[height][width];
		
		for(int i=0; i<height; i++){
			for(int j=0;j<width;j++){
				contour[i][j]=0;
			}
		}
			
		for(int i=1;i<height-1;i++){
			for(int j=1;j<width-1;j++){
				if(matrice[i][j]!=0){
					if(matrice[i-1][j]==0 || matrice[i+1][j]==0 || matrice[i][j+1]==0 || matrice[i][j-1]==0){
						contour[i][j]=matrice[i][j];
					}
				}
			}
		}
		return contour;
	}

	public static int[][] getComposantes(int[][] matrice){
		int height = matrice.length;
		int width = matrice[0].length;
		int[][] comp = new int[height][width];

		//Initialisation de la matrice a zero
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				comp[i][j]=0;
			}
		}

		int currentClass = 1;


		for(int i=1;i<height;i++){
			for(int j=1;j<width;j++){
				if(matrice[i][j]!=0){
					if(comp[i-1][j]==0 && comp[i][j-1]==0){
						comp[i][j]=currentClass;
						currentClass = currentClass + 1;
					}
					else{
						if(comp[i-1][j]==0){
							comp[i][j]=comp[i][j-1];
						}
						else{
							if(comp[i][j-1]==0){
								comp[i][j]=comp[i-1][j];
							}
							else{
								comp[i][j]=Math.min(comp[i-1][j], comp[i][j-1]);
								currentClass = comp[i][j] + 1;
								int k = j;

								while (comp[i][k] != 0) {
									comp[i][k] = comp[i][j];
									k = k - 1;
								}
								k = i;
								while (comp[k][j] != 0) {
									comp[k][j] = comp[i][j];
									k = k - 1;
								}
							}
						}
					}
				}
			}
		}
		return comp ;
	}

	public static double[][] histogramme(IplImage image){
		int[][] matrice = getComposantes(getBinaryImage(image));
		int height = matrice.length;
		int width = matrice[0].length;

		ByteBuffer rgb_data = image.getByteBuffer();

		//Recherche du plus grand numéro de classe
		int n=0;
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				if(matrice[i][j]>n){
					n=matrice[i][j];
				}
			}
		}

		double[][] histo = new double[n][101];
		//Mise à zéro de la matrice
		for(int i=0;i<n;i++){
			for(int j=0;j<101;j++){
				histo[i][j]=0;
			}
		}

		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				int num = matrice[i][j];
				if(num!=0){
					int blue = rgb_data.get(3*j + 3*width*i);
					if(blue<0)
						blue = blue + 256;

					int green = rgb_data.get(3*j + 3*width*i + 1);
					if(green<0)
						green = green + 256;

					int red = rgb_data.get(3*j + 3*width*i + 2);
					if(red<0)
						red = red + 256;

					float[] tsl = Color.RGBtoHSB(red, green, blue, null);
					float teinte = tsl[0];
					float saturation = tsl[1];
					float luminance = tsl[2];
					
					int taux = Math.round(100*teinte);

					histo[num-1][taux] = histo[num-1][taux] + 1;
				}
			}
		}
		
		//normalisation

		for(int i=0;i<n;i++){
			double somme = 0;
			for(int j=0;j<100;j++){
				somme = somme + histo[i][j];
			}
			
			for(int j=0;j<100;j++){
				histo[i][j]=histo[i][j]/somme;
				histo[i][j]=(int)(10000*histo[i][j]);
			}
		}
	
		
		/*
		for(int i=0;i<n;i++){
			System.out.println("Histogramme de teinte de la classe numéro "+(i+1)+" : ["+histo[i][0]+", "+histo[i][1]+", "+histo[i][2]+", "+histo[i][3]+", "+histo[i][4]+", "+histo[i][5]+", "+histo[i][6]+", "+histo[i][7]+", "+histo[i][8]+", "+histo[i][9]+"].");
		}
		*/
	//	for(int i=0;i<n;i++){
		//	System.out.println("Histogramme de teinte de la classe numéro "+(i+1)+":");
			for(int j=0;j<100;j++){
				System.out.print(histo[0][j]+" ");
			}
			System.out.println("banane");
	//	}
		
		
		return histo;
	}
}