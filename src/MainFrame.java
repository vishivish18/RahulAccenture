package com.facequality;

import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.filechooser.FileNameExtensionFilter;



















import com.cognitec.jfrsdk.*;




public class FaceQualityCheck extends JFrame implements ActionListener {
	
	
	JFrame mainbody;
	JButton photo;
	JButton cta;
	JFileChooser filechooser;
	JLabel photobox;
	JLabel resultbox;
	
	Container c; 
	String fullPath;
	public String [] result;
	
	  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	  /////////////////////////Starting Cognitech part
	  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private static FaceFinder ff;
	    private static EyesFinder ef;
	    private static PortraitAnalyzer pa;

	    private static float minRelativeEyeDistance =
	        FaceFinder.DEF_MIN_REL_EYE_DIST;
	    private static float maxRelativeEyeDistance =
	        FaceFinder.DEF_MAX_REL_EYE_DIST;

	    private static ArrayList<AnnotatedImage> getAnnotatedFaceImages(
	        Image image,
	        ArrayList<FaceFinder.Location> ffLocations
	    )
	    {
	        ArrayList<AnnotatedImage> annotatedFaceImages = 
	            new ArrayList<AnnotatedImage>();

	        Iterator<FaceFinder.Location> it = ffLocations.iterator();
	        while ( it.hasNext() )
	        {
	            FaceFinder.Location ffLocation = it.next();
	            EyesFinder.Location locationEye0 = new EyesFinder.Location();
	            EyesFinder.Location locationEye1 = new EyesFinder.Location();
	            
	            EyesFinder.Result efResult = ef.find( image,
	                                                  ffLocation,
	                                                  locationEye0,
	                                                  locationEye1 );
	                     
	            if ( efResult.getIsFoundEye0() && efResult.getIsFoundEye1() )
	            {
	              AnnotatedImage annotatedImage =
	                  new AnnotatedImage( image, locationEye0, locationEye1 );
	              annotatedFaceImages.add( annotatedImage );
	            }
	        }

	        return annotatedFaceImages;
	    }


	    private static void print( PortraitCharacteristics pch )
	    {
	    	    	
	    	Map<String, Float> map = new HashMap<String, Float>();
	    	map.put("Image width",(float) pch.width());
	    	map.put("Image height",(float) pch.height());
	    	map.put("Image sharpness",(float) pch.sharpness());
	    	map.put("Image exposure",(float) pch.exposure());
	    	map.put("Image gray scale density",(float) pch.grayScaleDensity());
	    	map.put("Eye distance",(float)  pch.eyeDistance());
	    	map.put("Width of head",(float) pch.widthOfHead());
	    	map.put("Length of head",(float) pch.lengthOfHead());
	    	map.put("Pose angle roll",(float) pch.poseAngleRoll());
	    	map.put("Eye0 gaze frontal",(float) pch.eye0GazeFrontal());
	    	map.put("Eye1 gaze frontal",(float) pch.eye1GazeFrontal());
	    	map.put("Eye0 is open",(float) pch.eye0Open());
	    	map.put("Eye1 is open",(float) pch.eye1Open());
	    	//map.put("Eye0 is red",(float) pch.eye0Red());
	    	//map.put("Eye1 is red",(float) pch.eye1Red());
	    	map.put("Wear glasses",(float) pch.glasses());
	    	map.put("Eye0 is tinted",(float) pch.eye0Tinted());
	    	map.put("Eye1 is tinted",(float) pch.eye1Tinted());
	    	map.put("Mouth is closed",(float) pch.mouthClosed());
	    	map.put("Chin distance",(float) pch.chin());
	    	map.put("Crown distance",(float) pch.crown());
	    	map.put("Ear0 distance",(float) pch.ear0());
	    	map.put("Ear1 distance",(float) pch.ear1());
	    	map.put("Is male",(float) pch.isMale());
	    	map.put("Estimated age",(float) pch.age());
	    	map.put("Ear1 distance",(float) pch.ear1());
	    	map.put("Deviation from frontal pose",(float) pch.deviationFromFrontalPose());
	    	map.put("Deviation from uniform lighting",(float) pch.deviationFromUniformLighting());
	    	//map.put("Natural skin color",(float) pch.naturalSkinColour());
	    	map.put("Hot spots",(float) pch.hotSpots());
	    	map.put("Background uniformity",(float) pch.backgroundUniformity());
	    	
	    
	    	System.out.println(map);
	    	//resultbox = map;
	    	
	    	
	    
	    	
	    }


	    private static void analyzeFace( AnnotatedImage face,
	                                     final String faceDisplayName )
	    {
	        PortraitCharacteristics pch;
	        System.out.print( "\nAnalyzing " + faceDisplayName + "... " );
	        pch = pa.analyze( face );
	        System.out.println( "Done." );
	        System.out.println( "***** " + faceDisplayName +
	                            " characteristics: *****" );
	        print( pch );
	        
	        
	        // Estimating face position from the camera
	        if(pch.eyeDistance() >= 120){
	        	System.out.println("Too Close ");
	        	JOptionPane.showMessageDialog(null, "Face is too close.");
	        }
	        else if(pch.eyeDistance() < 120){
	        	System.out.println("Too Far ");
	        	JOptionPane.showMessageDialog(null, "Face is too far.");
	        }
	        
	        
	        
	        
	        // Estimation left and right orientation of face
	        Position faceCenter = pch.faceCenter();
	        
	        if(faceCenter.getX() >= (pch.width()/2)+(pch.width()/8) && faceCenter.getX() < pch.width() )
	        {
	        	System.out.println("Too Right");	
	        	JOptionPane.showMessageDialog(null, "Too Right");
	        	
	        }
	        else if(faceCenter.getX() <= (pch.width()/2)-(pch.width()/8) && faceCenter.getX() > 0 )
	        {
	        	System.out.println("Too Left");	
	        	JOptionPane.showMessageDialog(null, "Too Left");
	        	
	        }
	        else
	        {
	        	System.out.println("Face is in Center");	
	        	JOptionPane.showMessageDialog(null, "Face is in Center");
	        }
	        
	        
	        // Estimating glasses on face
	        if(pch.glasses()>0)
	        {
	        	System.out.println("Wearing glasses");	
	        	JOptionPane.showMessageDialog(null, "Wearing glasses");
	        }
	        else
	        {
	        	System.out.println("No Glasses");	
	        	JOptionPane.showMessageDialog(null, "No glasses");

	        }
	        
	        
	    }


	    private static void analyzeFaces( ArrayList<AnnotatedImage> faces )
	    {
	        int imageNo = 0;
	        Iterator<AnnotatedImage> it = faces.iterator();
	        while ( it.hasNext() )
	        {
	            AnnotatedImage portrait = it.next();
	            String faceDisplayName = "Face N# " + ++imageNo;
	            analyzeFace( portrait, faceDisplayName );
	        }
	    }
	    
	    
	    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////Ending Cognitec part
	    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	// Constructor 
	public FaceQualityCheck()
	{
	 mainbody = new JFrame("Face Analysis");
	 photo = new JButton("Upload photo");
	 cta = new JButton("Process");
	 photobox = new JLabel("");
	 resultbox = new JLabel("");
	}
	
	// Implementing look and feel and setting layout 
	public void addon()
	{
	Container c= mainbody.getContentPane();
		
	try {
	    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
	        if ("Windows".equals(info.getName())) {
	            UIManager.setLookAndFeel(info.getClassName());
	            break;
	        }
	    }
	} 
	
	catch (Exception e) 
	{
		System.out.println("IO problem!, No look n feel possible");
		e.printStackTrace();
	}
		mainbody.setSize(1024,640);
		mainbody.setLocation(200,50);
		mainbody.setVisible(true);
		mainbody.setLayout(null); 
		mainbody.setResizable(true);
		mainbody.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		mainbody.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   
		
		photo.setBounds(30, 50, 300, 30);
		photo.setVisible(true);
		mainbody.add(photo);
	
		
		cta.setBounds(430, 50, 300, 30);
		cta.setVisible(true);
		mainbody.add(cta);
		
		
		photobox.setBounds(10,100,1000,1000);
		photobox.setBackground(Color.WHITE);
		photobox.setVisible(true);
		mainbody.add(photobox);
		
		resultbox.setBounds(510,100,400,600);
		resultbox.setBackground(Color.WHITE);
		resultbox.setVisible(true);
		mainbody.add(resultbox);
		
		
		photo.addActionListener(this);
		cta.addActionListener(this);
	
	}
	
	

	
	public static void main(String [] args)
	{
		FaceQualityCheck mf = new FaceQualityCheck();
		mf.addon();
		
		
		
	}


	
	// button click events
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==photo)
		{
			
			filechooser= new JFileChooser("C:\\FVSDK_8_8_0\\examples\\images");
		    filechooser.setDialogTitle("Select Face image to process.");
		    filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		    FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "png");
		    filechooser.setFileFilter(filter);
		    
		    //below codes for select  the file 
		    int returnval=filechooser.showOpenDialog(this);
		    if(returnval==JFileChooser.APPROVE_OPTION)
		    {	
		    	
		    	
		        File file = filechooser.getSelectedFile();

		        System.out.println("File loaded.");
		        
		        try {
					fullPath = file.getCanonicalPath();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
		      
		        BufferedImage bi;
		        try
		        {   //display the image in jlabel
		        	bi=ImageIO.read(file);
		            photobox.setIcon(new ImageIcon(bi));
		            
		        }
		        catch(IOException e1)
		        {
		        	e1.printStackTrace();
		        	System.out.println("File not loded.");
		        	
		        }
		        this.pack();
		    }
			System.out.println("clicked");
		
		
		
		}
		
		if(e.getSource() == cta)
		{
			
			try
			{
				
				System.out.println("Executed...");
                              
			   JFRSdk.init();
			   minRelativeEyeDistance = (float) 0.015625;
			   maxRelativeEyeDistance = (float) 0.75;
			   
			   Configuration configuration;
			   
			   try
	            {
	               
	                configuration = new Configuration( "C:\\FVSDK_8_8_0\\etc\\frsdk.cfg" );
	                System.out.println( "Done." );
	            }
	            catch ( Exception ex )
	            {
	                System.out.println( "Fail!" );          
	                throw ex;
	            }
			   
			   try
	            {
	                System.out.print( "Creating a FaceFinder... " );
	                ff = new FaceFinder( configuration );
	                System.out.println( "Done." );
	            }
	            catch( Exception ex )
	            {
	                System.out.println( "Fail!" );
	                throw ex;
	            }
			   
			   try
	            {
	                System.out.print( "Creating a EyesFinder... " );
	                ef = new EyesFinder( configuration );
	                System.out.println( "Done." );
	            }
	            catch( Exception ex )
	            {
	                System.out.println( "Fail!" );
	                throw ex;
	            }

	            try
	            {
	                System.out.print( "Creating a PortraitAnalyzer... " );
	                pa = new PortraitAnalyzer( configuration );
	                System.out.println( "Done." );
	            }
	            catch( Exception ex )
	            {
	                System.out.println( "Fail!" );
	                throw ex;
	            }

	            Image image;
	            final String imageDisplayName = "input image";
	            try
	            {	
	            	
	                System.out.print( "\nReading " + imageDisplayName + " from \"" + fullPath + "\" file... " );
	               
	                image = ImageFactory.create( fullPath );
	                System.out.println( "Done." );
	            }
	            catch( Exception ex )
	            {
	                System.out.println( "Fail!" );
	                throw ex;
	            }

	            ArrayList<FaceFinder.Location> ffLocations = 
	                    ff.find( image,
	                             minRelativeEyeDistance,
	                             maxRelativeEyeDistance );
	            
	            if ( ffLocations == null || ffLocations.isEmpty() )
	            {
	              System.out.println( "No faces where found in the " +
	                                  imageDisplayName + "!" );
	              System.exit( 0 );
	            }
	            
	            ArrayList<AnnotatedImage> annotatedFaceImages =
	                    getAnnotatedFaceImages( image, ffLocations );

	                System.out.println( annotatedFaceImages.size() + " face(s) " + 
	                                    "where annotated with both eyes." );

	                analyzeFaces( annotatedFaceImages );
			
			}
			catch ( Exception ex )
	        {
	            System.out.println( "Exception thrown: " + ex );
	            ex.printStackTrace();
	            System.out.println( "Program is aborting." );
	            System.exit( 1 );
	        }
			
		}
		
	}

	
	
	// getter for stream wrapper
	public StreamWrapper getStreamWrapper(InputStream is, String type){
        return new StreamWrapper(is, type);
}
	
	
	private class StreamWrapper extends Thread {
	    InputStream is = null;
	    String type = null;          
	    String message = null;
	    
	    
	    public String getMessage() {
	            return message;
	    }

	    StreamWrapper(InputStream is, String type) {
	        this.is = is;
	        this.type = type;
	    }

	    public void run() {
	        try {
	            BufferedReader br = new BufferedReader(new InputStreamReader(is));
	            StringBuffer buffer = new StringBuffer();
	            String line = null;
	            while ( (line = br.readLine()) != null) {
	                buffer.append(line);//.append("\n");
	            }
	            message = buffer.toString();
	        } catch (IOException ioe) {
	            ioe.printStackTrace();  
	        }
	    }
	}

	
	
}
