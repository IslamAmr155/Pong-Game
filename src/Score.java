import java.awt.*;

public class Score extends Rectangle{

	static int game_width;
	static int game_height;
	int player1;
	int player2;
	
	Score(int width,int height){
		game_width = width;
		game_height = height;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("Consolas",Font.PLAIN,60));
		g.drawString(String.valueOf(player1/10)+String.valueOf(player1%10),game_width/2 - 85,50);
		g.drawString(String.valueOf(player2/10)+String.valueOf(player2%10),game_width/2 + 20,50);
		g.drawLine(game_width/2, 0, game_width/2, 60);
	}
}
