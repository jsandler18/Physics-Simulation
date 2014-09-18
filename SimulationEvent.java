import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SimulationEvent implements ActionListener{

	Simulation main;

	
	public SimulationEvent(Simulation in){
		main=in;
	}
	
	public void actionPerformed(ActionEvent e){
		String pushed = e.getActionCommand();
	
		if(pushed=="GO!"){
			new Thread(main).start();
		}
		if(pushed=="STOP"){
			main.tempvelx=main.velx;
			main.tempvely=main.vely;
			main.velx=0;
			main.vely=0;
			main.stop.setText("CONTINUE");
			main.paused=true;
		}
		if(pushed=="CONTINUE"){
			main.velx=main.tempvelx;
			main.vely=main.tempvely;
			main.tempvelx=0;
			main.tempvely=0;
			main.stop.setText("STOP");
			main.paused=false;
		}
	}
}