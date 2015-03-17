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
		int i = 1;
		try{
			while(true){
			grabber.start();
			IplImage img = grabber.grab();
			if(img!=null)
			{
				cvSaveImage("cam"+i+".jpg", img);
				System.out.println("Fait.");
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
