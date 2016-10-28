package launch4jtest;

import javax.swing.*;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

@Component
public class HeyGuys {
	public static void main(String[] args) {
		JOptionPane.showMessageDialog(null, "Hey, you guys!");
		System.exit(0);
	}

	@Activate
	public void activate() {
		JOptionPane.showMessageDialog(null, "Hey, you guys!");
	}
	
	@Deactivate
	public void deactivate() {
		System.exit(0);
	}

}