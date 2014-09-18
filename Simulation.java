import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.*;

public class Simulation extends JPanel implements Runnable{

	public float g = -9.81f;//gravity
	
	public float velx=0f;
	public float vely=0f;
	public float velocity = 0f;
	public float force=0f;
	public float angle=0f;
	public float momentum = 0f;
	public float mass = 0f;
	public float time= 0f;
	public float energy = 0f;
	public double posx = 10;
	public double posy = 520;
	public float energyLost = 0f;
	
	float tempvelx=0;
	float tempvely=0;
	
	
	double lastx=0;
	double lasty=0;
	
	public boolean paused=false;
	
	
	JLabel forceLab;
	JLabel angleLab;
	JLabel massLab;
	JLabel timeLab;
	JLabel energyLab;
	
	JLabel velLab;
	JLabel velxLab;
	JLabel velyLab;
	JLabel energyLabel;
	JLabel momentumLab;
	
	JTextField velOut;
	JTextField velxOut;
	JTextField velyOut;
	JTextField energyOut;
	JTextField momentumOut;	
	
	JButton go;
	JButton stop;
	JTextField angleIn;
	
	//using momentum (j=Ft=mv)
	JTextField forceIn;
	JTextField massIn;
	JTextField timeIn;
	
	JSlider energyIn;

	FlowLayout flo;
	BorderLayout master;
	
	SimulationEvent event;
	
	Rectangle2D.Double background;
	Ellipse2D.Double ball ;
	ArrayList<Line2D> lines = new ArrayList<Line2D>();
	
	public Simulation(){
		event = new SimulationEvent(this);
		master = new BorderLayout();
		flo= new FlowLayout();
		JFrame frame = new JFrame("Physics Simulation");
		JPanel input = new JPanel();
		JPanel updates = new JPanel();
		frame.setLayout(master);
		frame.setSize(1350,700);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(input, BorderLayout.NORTH);
		input.setLayout(flo);
		frame.add(this, BorderLayout.CENTER);
		frame.add(updates, BorderLayout.SOUTH);
		updates.setLayout(flo);
			
		forceLab= new JLabel("Force(N):");
		angleLab = new JLabel("Angle:");
		massLab = new JLabel("Mass(kg):");
		timeLab = new JLabel("Time(s):");
		energyLab = new JLabel("Energy Loss(%):");
		go =  new JButton("GO!");
		stop = new JButton("STOP");
		angleIn = new JTextField("60",5);
		forceIn = new JTextField("500",5);
		massIn = new JTextField("1",5);
		timeIn = new JTextField("0.1",5);
		energyIn = new JSlider(0,100);
		
		velLab= new JLabel("Velocity(m/s):");
		velxLab = new JLabel("X-direction Velocity (m/s):");
		velyLab= new JLabel("Y-direction Velocity (m/s):");;
		energyLabel = new JLabel("Kinetic Energy (J):");
		momentumLab = new JLabel("Momentum (N*s):");
		
		velOut= new JTextField(5);
		velxOut= new JTextField(5);
		velyOut= new JTextField(5);
		energyOut= new JTextField(5);
		momentumOut= new JTextField(5);
		
		updates.add(velLab);
		updates.add(velOut);
		velOut.setEditable(false);
		
		updates.add(velxLab);
		updates.add(velxOut);
		velxOut.setEditable(false);
		
		updates.add(velyLab);
		updates.add(velyOut);
		velyOut.setEditable(false);
		
		updates.add(energyLabel);
		updates.add(energyOut);
		energyOut.setEditable(false);
		
		updates.add(momentumLab);
		updates.add(momentumOut);
		momentumOut.setEditable(false);
		
		input.add(forceLab);
		input.add(forceIn);
				
		input.add(angleLab);
		input.add(angleIn);
		
		
		input.add(massLab);
		input.add(massIn);
		
		input.add(timeLab);
		input.add(timeIn);
		
		input.add(energyLab);
		input.add(energyIn);
		energyIn.setMajorTickSpacing(20);
		energyIn.setPaintTicks(true);
		energyIn.setPaintLabels(true);
		
		input.add(go);
		input.add(stop);
		go.addActionListener(event);
		stop.addActionListener(event);
		
		background = new Rectangle2D.Double(0,0,1300,550);
		ball = new Ellipse2D.Double(posx,posy,25,25);
		
		frame.revalidate();
		repaint();
	}
	
	public void run(){
		posx = 10;
		posy = 520;
		lines.clear();
		
		energyLost=1-(energyIn.getValue()/100f);
		force=Float.parseFloat(forceIn.getText());
		angle=(float)((Float.parseFloat(angleIn.getText()))*Math.PI/180);
		mass=Float.parseFloat(massIn.getText());
		time=Float.parseFloat(timeIn.getText());
		momentum=force * time;
		velocity= momentum/mass;
		energy=(float).5*mass*velocity*velocity;
		velx= (float)(velocity*Math.cos(angle));
		vely= -(float)(velocity*Math.sin(angle));
		
		while(velocity>.05 || posy<500 || paused){
			System.out.println(paused);
			
			if(paused){
				continue;
			}
			
			if(posy<0){
				
				posy=0;
				energy*=energyLost;
				angle=(float)Math.atan(-vely/velx);//incoming angle
				if(velx<0){
					angle+=Math.PI;
				}
				angle=(float)(2*Math.PI)-angle;//reflects angle
				velocity=(float)Math.sqrt(2*energy/mass);
				velx=(float)(velocity*Math.cos(angle));
				vely=(float)(-velocity*Math.sin(angle));
				
			}
			if(posx<0){
				posx=0;
				energy*=energyLost;
				angle=(float)Math.atan(-vely/velx);//incoming angle
				if(vely<0){//q2->q1
					angle+=Math.PI; 
				}
				if(vely>0){//q3->q4
					angle-=Math.PI;
				}
				angle=(float)(Math.PI)-angle;//reflects angle
				velocity=(float)Math.sqrt(2*energy/mass);
				velx=(float)(velocity*Math.cos(angle));
				vely=(float)(-velocity*Math.sin(angle));
			}
			if(posy>525){
				
				posy=525;
				energy*=energyLost;
				angle=(float)Math.atan(-vely/velx);//incoming angle
				if(velx<0){
					angle+=Math.PI;
				}
				angle=(float)(2*Math.PI)-angle;//reflects angle

				velocity=(float)Math.sqrt(2*energy/mass);
				velx=(float)(velocity*Math.cos(angle));
				vely=(float)(-velocity*Math.sin(angle));
			}
			if(posx>1275){
				posx=1275;
				energy*=energyLost;
				angle=(float)Math.atan(-vely/velx);//incoming angle
				angle=(float)Math.PI-angle;
				velocity=(float)Math.sqrt(2*energy/mass);
				velx=(float)(velocity*Math.cos(angle));
				vely=(float)(-velocity*Math.sin(angle));
			}
			

			
			lastx=posx;
			lasty=posy;
			
			posx+=velx/30f;
			posy+=vely/30f;
			repaint();
			
			try{
				Thread.sleep(34);
			}catch(Exception a){System.out.println("error");}
			vely-=g/30f;

			
			velocity=(float)Math.sqrt((velx*velx)+(vely*vely));
			energy=.5f*mass*velocity*velocity;
			momentum=mass*velocity;
			
			velOut.setText(Float.toString(velocity));
			velxOut.setText(Float.toString(velx));
			velyOut.setText(Float.toString(-vely));
			energyOut.setText(Float.toString(energy));
			momentumOut.setText(Float.toString(momentum));
		}
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d= (Graphics2D) g; 
		g2d.setColor(Color.black);
		g2d.fill(background);
		
		g2d.setColor(Color.white);
		ball.setFrame(posx, posy, 25, 25);
		g2d.fill(ball);

		g2d.setColor(Color.red);
		Line2D.Double temp = new Line2D.Double((lastx+13),(lasty+13), posx+13,posy+13);
		lines.add(temp);
		
		for(int x = 0; x < lines.size(); x++){
			g2d.draw(lines.get(x));
		}
	}

	public static void main(String[] args){
		Simulation go = new Simulation();
	}

}