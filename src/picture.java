import java.io.File;
import java.nio.ByteBuffer;

import org.bytedeco.javacv.*;
import org.bytedeco.javacpp.*;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;

import javax.swing.JFrame;
import javax.swing.JRootPane;

import java.awt.GridLayout;












public class picture {
	
	public static void main(String[] args) {
		OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
		int i = 506;
		try{
			int n=1;
			while(true){
			grabber.start();
			IplImage img = grabber.grab();
			ByteBuffer rgbdata = img.getByteBuffer();
			int t = 0;
			for(int k=0;k<48;k++){
				for(int j=0;j<64;j++){
					t=t+rgbdata.get(k*1600 + j*5);
				}
			}
			System.out.println(n);
			n++;
			if(t>5000 && t<10000)
			{
				cvSaveImage("cam"+i+".jpg", img);
				System.out.println("Fait. "+t);
				break;
			}
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
