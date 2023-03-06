import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

public class Controller extends JDialog implements ActionListener {

	private View v;
	private Model m;
	
	public Controller(View view, Model model) {
		v = view;
		m = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
