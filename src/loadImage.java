import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import static org.bytedeco.javacpp.opencv_highgui.*;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.*;

public class loadImage {

	public static void main(String[] args) {

		//-----------------PRISE DE PHOTO---------------------//
		

		OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
		try{
			while(true){
				grabber.start();
				IplImage image = grabber.grab();
				@SuppressWarnings("deprecation")
				ByteBuffer rgbdata = image.getByteBuffer();
				int t = 0;
				for(int k=0;k<48;k++){
					for(int j=0;j<64;j++){
						t=t+rgbdata.get(k*1600 + j*5);
					}
				}
				if(t>5000 && t<10000) // eclairage a regler pour prise photo
				{
					cvSaveImage("cam.jpg", image);
					break;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		

		//-----------------DEBUT DU TRAITEMENT D'IMAGE---------------------//
			try{
				IplImage img = cvLoadImage("cam29.jpg");
				@SuppressWarnings("deprecation")
				ByteBuffer rgb_data = img.getByteBuffer();		
				int height = img.height();
				int width = img.width();

				//-----------------DEBUT DE L'IMAGE BINAIRE---------------------//

				int[][] matrice = new int[height][width];
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

				//-----------------DEBUT DETERMINATION COMPOSANTES---------------------//

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

				// Supprime les classes dont les pixels sont pas assez nombreux
				int C = 1;
				while(C<currentClass){
					int nbPixel = 0;
					for(int i=0; i<height;i++){
						for(int j=0;j<width;j++){
							if(comp[i][j]==C){
								nbPixel++;							
							}
						}
					}
					if(nbPixel<30){
						for(int i=0;i<height;i++){
							for(int j=0;j<width;j++){
								if(comp[i][j]==C){
									comp[i][j]=0;
								}
								if(comp[i][j]>C){
									comp[i][j]=comp[i][j]-1;
								}
							}
						}
						currentClass = currentClass - 1;
					}
					else{
						C=C+1;
					}
				}

				//recorrection d'erreur
				for(int i=0;i<height;i++){
					for(int j=0;j<width;j++){
						int k=0;
						while(comp[i][j+k]!=0 && j+k<width-1){
							k++;
						}
						if(k<5){
							for(int l=0;l<k+1;l++){
								comp[i][j+l]=0;
							}
						}
					}
				}

				currentClass = currentClass - 1;

				//-----------------AFFICHAGE MATRICE---------------------//
				/*
			for(int i=0;i<height;i++){
				for(int j=0;j<width;j++){
					System.out.print(comp[i][j]);
				}
				System.out.println("");
			}
				 */

				//-----------------DEBUT DE DETERMINATION DU CONTOUR---------------------//

				int[][] contour = new int[height][width];

				for(int i=0; i<height; i++){
					for(int j=0;j<width;j++){
						contour[i][j]=0;
					}
				}

				for(int i=1;i<height-1;i++){
					for(int j=1;j<width-1;j++){
						if(comp[i][j]!=0){
							if(comp[i-1][j]==0 || comp[i+1][j]==0 || comp[i][j+1]==0 || comp[i][j-1]==0){
								contour[i][j]=comp[i][j];
							}
						}
					}
				}

				//-----------------DEBUT DE HISTOGRAMME---------------------//

				int n=currentClass;

				double[][] histo = new double[n][101];
				//Mise � z�ro de la matrice
				for(int i=0;i<n;i++){
					for(int j=0;j<101;j++){
						histo[i][j]=0;
					}
				}

				for(int i=0;i<height;i++){
					for(int j=0;j<width;j++){
						if(comp[i][j]<=currentClass){
							int num = comp[i][j];
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

								int taux = Math.round(100*teinte);

								histo[num-1][taux] = histo[num-1][taux] + 1;
							}
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

				//-----------------DEBUT DE DETERMINATION DU SECOND ATTRIBUT---------------------//

				double[] area = new double[currentClass];

				for(int c=1; c<currentClass+1; c++){
					int cont=1;
					int aire=1;
					for(int i=0;i<height;i++){
						for(int j=0;j<width;j++){
							if(contour[i][j]==c)
								cont++;

							if(comp[i][j]==c)
								aire++;
						}

					}
					area[c-1] = 4*Math.PI*aire/Math.pow((double) cont, 2.0);
				}
				
				for(int i=0; i<height; i++){
					for(int j=0; j<width; j++){
						System.out.print(comp[i][j]);
					}
					System.out.println("");
				}
				
				
				
				
				

				System.out.println(currentClass); // Nombre de fruits
				for(n=0; n<currentClass + 1;n++){
					for(int c=0;c<100;c++){
						System.out.print(histo[n][c]+" "); // Affichage histogramme
					}
					System.out.println(area[0]); // Rapport aire perimetre
				}

				//-----------------ECRITURE DU FICHIER EN SORTIE-----------------//

				FileOutputStream file = new FileOutputStream(args[0]".test");
				file.write(histo[0][] + area[0]);
				file.close();

			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
	}
}