import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class main {

	private JLabel labelP1,labelP2,labelP3,labelP4,labelP5,labelf1,labelf2,labelf3,labelf4,labelf5;
	private JLabel labelth1,labelth2,labelth3,labelth4,labelth5;
	private ImageIcon p1,p2,p3,p4,p5,f1,f2,f3,f4,f5,th1,th2,th3,th4,th5;
	private JFrame frame;
	private Semaphore mutex,sem[];
	private char state[];
	private Thread t1,t2,t3,t4,t5;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					main window = new main();
					window.frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/*class Semaphore{
		public int s;
		public Semaphore() {
			s=0;
		}
		public void waitt(){
			while(s<0){
				/*try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println();
			}
			s--;
		}
		public void signal(){
			System.out.println(s);
			s++;
		}
	}*/
	class philospher extends Thread{
		private int num;
		public philospher(int i){
			num=i;
		}
		public void run(){
			for(int i=0;i<5;i++){
				this.getfork(num);
				try {
					Thread.sleep(550);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				this.test(num);
				try {
					Thread.sleep(550);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				this.putfork(num);
				try {
					Thread.sleep(550);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		public synchronized void getfork(int i){ 
		//    System.out.println("GetFork " + i + " called");
			try {
				mutex.acquire();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		//    System.out.println("Fork " + i + " entered in the lock");
			state[i] = 'H';
			switch(i){
        	case 0:labelf1.setVisible(false);labelth1.setVisible(false);break;
        	case 1:labelf2.setVisible(false);labelth2.setVisible(false);break;
        	case 2:labelf3.setVisible(false);labelth3.setVisible(false);break;
        	case 3:labelf4.setVisible(false);labelth4.setVisible(false);break;
        	case 4:labelf5.setVisible(false);labelth5.setVisible(false);break;
        	}
		//	System.out.println(i+" is hungry");
			test(i);
		//    System.out.println("Fork " + i + " going out of the lock");
			mutex.release();
			try {
				sem[i].acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public synchronized void putfork(int i){
		    try {
				mutex.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    state[i] = 'T';
		    switch(i){
        	case 0:labelf1.setVisible(false);labelth1.setVisible(true);break;
        	case 1:labelf2.setVisible(false);labelth2.setVisible(true);break;
        	case 2:labelf3.setVisible(false);labelth3.setVisible(true);break;
        	case 3:labelf4.setVisible(false);labelth4.setVisible(true);break;
        	case 4:labelf5.setVisible(false);labelth5.setVisible(true);break;
        	}
		  //  System.out.println(i+" is Thinking");
		    //System.out.println(((i+1)%5) + "   ,   " + (i-1)%5);
		    test((i+1)%5);
		    if(i!=0)
		    	test((i-1)%5);
		    else
		    	test(4%5);
		    mutex.release();
		}
		public synchronized void test(int i){
		 //   System.out.println("Test called for " + ((i+1)%5) + "   ,   " + (i-1)%5);
			
		    if(i!=0){
		     //   System.out.println("GetFork " + state[(i+1)%5]+state[i-1] + " "+i);
		        if (state[i] == 'H' && state[(i-1)%5] != 'E' && state[(i+1)%5] != 'E'){ 
		        	state[i] = 'E'; 
		        	switch(i){
		        	case 0:labelf1.setVisible(true);labelth1.setVisible(false);break;
		        	case 1:labelf2.setVisible(true);labelth2.setVisible(false);break;
		        	case 2:labelf3.setVisible(true);labelth3.setVisible(false);break;
		        	case 3:labelf4.setVisible(true);labelth4.setVisible(false);break;
		        	case 4:labelf5.setVisible(true);labelth5.setVisible(false);break;
		        	}
		        	System.out.println(i+ " is eating");
		        	try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		        	
		        	sem[i].release();
		        }
		    }
		    else{
		    //	System.out.println("GetFork " + state[4]+state[1] + " " + i);
		    	if (state[i] == 'H' && state[4] != 'E' && state[(i+1)%5] != 'E'){ 
		    		state[i] = 'E';
		    		System.out.println(i+ " is eating");
		    		try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		    		sem[i].release();
		    	}
		    }
		}
	}
	public main() {
		initialize();
		mutex = new Semaphore(1);
		/*mutex.waitt();
		mutex.waitt();
		mutex.signal();*/
		sem = new Semaphore[5];
		sem[0] = new Semaphore(1);
		sem[1] = new Semaphore(1);
		sem[2] = new Semaphore(1);
		sem[3] = new Semaphore(1);
		sem[4] = new Semaphore(1);
		state = new char[5];
		t1 = new philospher(0);
		t2 = new philospher(1);
		t3 = new philospher(2);
		t4 = new philospher(3);
		t5 = new philospher(4);
		for(int i=0;i<5;i++){
			state[i]='T';
			labelth1.setVisible(true);
			labelth2.setVisible(true);
			labelth3.setVisible(true);
			labelth4.setVisible(true);
			labelth5.setVisible(true);
		}
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
	}
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.getContentPane().setLayout(null);
		
		
		java.net.URL imageURL = getClass().getResource("images/philospher.png");
		p1 = new ImageIcon(imageURL);
		java.net.URL imageURL1 = getClass().getResource("images/fork2.png");
		f1 = new ImageIcon(imageURL1);
		java.net.URL imageURL2 = getClass().getResource("images/think_right.png");
		th1 = new ImageIcon(imageURL2);
		System.out.println(imageURL2);
		
		
		labelP1 = new JLabel(p1,JLabel.CENTER);
		labelP1.setBounds(913, 427, 220, 160);
		frame.getContentPane().add(labelP1);

		labelf1 = new JLabel(f1,JLabel.CENTER);
		labelf1.setBounds(863, 427, 220, 160);
		frame.getContentPane().add(labelf1);
		
		labelth1 = new JLabel(th1,JLabel.CENTER);
		labelth1.setBounds(913, 387, 220, 160);
		frame.getContentPane().add(labelth1);
		
		
		

		labelP2 = new JLabel(p1,JLabel.CENTER);
		labelP2.setBounds(602, 63, 225, 160);
		frame.getContentPane().add(labelP2);
		
		labelf2 = new JLabel(f1,JLabel.CENTER);
		labelf2.setBounds(552, 63, 220, 160);
		frame.getContentPane().add(labelf2);
		
		labelth2 = new JLabel(th1,JLabel.CENTER);
		labelth2.setBounds(580, 13, 220, 160);
		frame.getContentPane().add(labelth2);

		
		
		labelP3 = new JLabel(p1,JLabel.CENTER);
		labelP3.setBounds(1031, 203, 220, 160);
		frame.getContentPane().add(labelP3);

		labelf3 = new JLabel(f1,JLabel.CENTER);
		labelf3.setBounds(981, 203, 220, 160);
		frame.getContentPane().add(labelf3);
		
		labelth3 = new JLabel(th1,JLabel.CENTER);
		labelth3.setBounds(1031, 153, 220, 160);
		frame.getContentPane().add(labelth3);

		
		
		labelP4 = new JLabel(p1,JLabel.CENTER);
		labelP4.setBounds(235, 480, 220, 160);
		frame.getContentPane().add(labelP4);
		
		labelf4 = new JLabel(f1,JLabel.CENTER);
		labelf4.setBounds(185, 480, 220, 160);
		frame.getContentPane().add(labelf4);
		
		labelth4 = new JLabel(th1,JLabel.CENTER);
		labelth4.setBounds(235, 430, 220, 160);
		frame.getContentPane().add(labelth4);

		
		
		labelP5 = new JLabel(p1,JLabel.CENTER);
		labelP5.setBounds(247, 166, 220, 160);
		frame.getContentPane().add(labelP5);

		labelf5 = new JLabel(f1,JLabel.CENTER);
		labelf5.setBounds(197, 166, 220, 160);
		frame.getContentPane().add(labelf5);

		labelth5 = new JLabel(th1,JLabel.CENTER);
		labelth5.setBounds(247, 126, 220, 160);
		frame.getContentPane().add(labelth5);
		
		labelf1.setVisible(false);
		labelf2.setVisible(false);
		labelf3.setVisible(false);
		labelf4.setVisible(false);
		labelf5.setVisible(false);
		
		labelth1.setVisible(false);
		labelth2.setVisible(false);
		labelth3.setVisible(false);
		labelth4.setVisible(false);
		labelth5.setVisible(false);
	}
}