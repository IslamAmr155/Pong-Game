import java.awt.BorderLayout;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class GameFrame extends JFrame{
	URL iconPath = getClass().getResource("pongIcon.png");
	
	GameFrame(){
		this.setContentPane(new GamePanel());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setTitle("Pong Game");
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setIconImage(new ImageIcon(iconPath).getImage());
	}
}
