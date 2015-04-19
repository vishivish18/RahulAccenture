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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;



public class MainFrame extends JFrame implements ActionListener {

	JFrame mainbody;
	JButton photo;
	JButton cta;
	JFileChooser filechooser;
	JLabel photobox;
	Container c; 
	
	
	
	
	
	public MainFrame()
	{
	 mainbody = new JFrame("TestBody");
	 photo = new JButton("Upload photo");
	 cta = new JButton("Click to Action");
	 photobox = new JLabel("");
	 
	 
	 
		
		
	}
	
	
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
	} catch (Exception e) 
	{
	    // If Nimbus is not available, you can set the GUI to another look and feel.
	}
		mainbody.setSize(971,640);
		mainbody.setLocation(200,50);
		mainbody.setVisible(true);
		mainbody.setLayout(null); 
		mainbody.setResizable(false);
		mainbody.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   
		
		photo.setBounds(30, 50, 300, 30);
		photo.setVisible(true);
		mainbody.add(photo);
	
		
		cta.setBounds(430, 50, 300, 30);
		cta.setVisible(true);
		mainbody.add(cta);
		cta.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try
				{
					Desktop.getDesktop().browse(new URI("www.specta.in"));
				}
				catch (URISyntaxException | IOException ex)
				{
					// It looks like there's a problem
				}
			}
		});
		
		
		photobox.setBounds(200,200,700,600);
		photobox.setBackground(Color.RED);
		photobox.setVisible(true);
		mainbody.add(photobox);
		
		
		photo.addActionListener(this);
	
	}
	
	

	
	public static void main(String [] args)
	{
		MainFrame mf = new MainFrame();
		mf.addon();
		
		
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==photo)
		{
			
			filechooser= new JFileChooser("F:\\Live_WorkSpaces\\Java_WorkSpace\\MMB_GUI\\MMB_GUI\\images");
		    filechooser.setDialogTitle("Upload Your Palmprint Sample 5");
		    filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		    //below codes for select  the file 
		    int returnval=filechooser.showOpenDialog(this);
		    if(returnval==JFileChooser.APPROVE_OPTION)
		    {
		        File file = filechooser.getSelectedFile();
		        
		      
		        BufferedImage bi;
		        try
		        {   //display the image in jlabel
		        	bi=ImageIO.read(file);
		            photobox.setIcon(new ImageIcon(bi));
		           
		        }
		        catch(IOException e1)
		        {

		        }
		        this.pack();
		    }
			
  File f = new File(filechooser.getSelectedFile().toString());
	        

            try { 
  	
    	File sourceFile=new File(filechooser.getSelectedFile().toString());
    	BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile), 4096);
          File targetFile = new File("C:\\mmb_test\\"+456+"foot1.png"); ////////////////////////////////////////////("images\\"+t8.getText()+".png")
          BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targetFile), 4096);
          int theChar;
          while ((theChar = bis.read()) != -1) {
             bos.write(theChar);
          }
       bos.close();
       bis.close();
       System.out.println ("copy done!");
       //JOptionPane.showMessageDialog(null, "Photograph Saved");
 }
 catch (Exception ex) 
 {
  ex.printStackTrace();
  //if(uid.getText()==null)
  //{   
  JOptionPane.showMessageDialog(null, "Error in saving pgotograph!,Make sure aadhaar number is genrated before saving");
  //}
 }
			System.out.println("clicked");
		}
		
		
	}
	
}
