import java.awt.*;

import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.swing.*;
import javax.swing.event.*;


enum stages {menu,select,game,over}

public class GamePanel extends JPanel implements ActionListener, MouseListener, ChangeListener, Runnable{

	final int screen_width = 1200;
	final int screen_height = 700;
	final int scoreboard_height = 60;
	final int game_height=640;
	static final int ball_diameter = 20;
	static final int paddle_width = 25;
	static final int paddle_height = 100;
	int maxscore = 5;
	Thread gameThread;
	Random random;
	Paddle paddle1;
	Paddle paddle2;
	Ball ball;
	Score SCORE;
	
	
	Integer score=5;
	
	URL tennisPath = getClass().getResource("Tennis.jpg");
	URL pingPath = getClass().getResource("Ping.jpg");
	URL airPath = getClass().getResource("Air Hockey.png");
	ImageIcon icon= new ImageIcon(tennisPath);
	Image field = icon.getImage();
	char ball_color = 't';
	
	stages stage;
	
	JLabel label;
	JLabel title;
	JLabel start;
	JLabel tennis;
	JLabel ping;
	JLabel air;
	JLabel temp;
	JLabel stemp;
	JLabel playerwin;
	JButton rematch;
	JButton menu;
	JSlider slider;
	JPanel panel;
	
	GamePanel(){
		// the top panel having the title and then changed to scoreboard
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(screen_width,scoreboard_height));
		panel.setBackground(Color.black);
		panel.setFocusable(true);
		panel.setLayout(null);
		panel.setBounds(0, 0, screen_width, scoreboard_height);
		
		//label for title
		title = new JLabel();
		title.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.1f));
		title.setBounds(550, 0, 500, 60);
		title.setText("Pong");
		title.setFont(new Font("Bauhaus 93",Font.BOLD,40));
		title.setForeground(Color.cyan);
		
		
		panel.add(title);
		this.setPreferredSize(new Dimension(screen_width,screen_height));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		this.setLayout(null);
		this.add(panel);
		stage = stages.menu;
		selectMenu();
	}
	
	public void selectMenu() {
		maxscore =5;
		
		//the start label
		start = new JLabel();
		start.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.1f));
		start.setBounds(535, 600, 300, 70);
		start.setText("START");
		start.setFont(new Font("Bauhaus 93",Font.BOLD,50));
		start.setForeground(Color.red);
		start.addMouseListener(this);
		
		//the slider
		slider = new JSlider(5,20,5);
		slider.setBackground(new Color(0.0f, 0.0f, 0.0f, 1.0f));
		slider.setPaintTrack(true);
		slider.setMajorTickSpacing(1);
		slider.setPaintLabels(true);
		slider.setFont(new Font("Bauhaus 93",Font.PLAIN,30));
		slider.addChangeListener(this);
		slider.setBounds(250, 400, 700, 100);
		
		//options for image of field
		temp=new JLabel();
		temp.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.1f));
		temp.setBounds(450, 110, 600, 100);
		temp.setText("Select the field theme");
		temp.setFont(new Font("Bauhaus 93",Font.BOLD,30));
		temp.setForeground(Color.red);
		
		stemp=new JLabel();
		stemp.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.1f));
		stemp.setBounds(320, 300, 650, 100);
		stemp.setText("Select the max score for a player to win");
		stemp.setFont(new Font("Bauhaus 93",Font.BOLD,30));
		stemp.setForeground(Color.red);
		
		tennis = new JLabel();
		tennis.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.1f));
		tennis.setBounds(290, 200, 200, 100);
		tennis.setText("TENNIS");
		tennis.setFont(new Font("Bauhaus 93",Font.BOLD,30));
		tennis.setForeground(Color.green);
		tennis.addMouseListener(this);
		
		ping = new JLabel();
		ping.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.1f));
		ping.setBounds(570, 200, 200, 100);
		ping.setText("PING");
		ping.setFont(new Font("Bauhaus 93",Font.BOLD,30));
		ping.setForeground(Color.blue);
		ping.addMouseListener(this);
		
		air = new JLabel();
		air.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.1f));
		air.setBounds(790, 200, 200, 100);
		air.setText("AIR HOCKEY");
		air.setFont(new Font("Bauhaus 93",Font.BOLD,30));
		air.setForeground(Color.pink);
		air.addMouseListener(this);
		
		
		this.add(start);
		this.add(slider);
		this.add(temp);
		this.add(stemp);
		this.add(tennis);
		this.add(ping);
		this.add(air);
		repaint();
	}
	
	public void startGame() {
		this.remove(start);
		this.remove(slider);
		this.remove(tennis);
		this.remove(ping);
		this.remove(air);
		this.remove(temp);
		this.remove(stemp);
		panel.remove(title);
		panel.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.1f));
		newPaddles();
		newBall();
		SCORE = new Score(screen_width,screen_height);
		gameThread = new Thread(this);
		gameThread.start();
		repaint();
	}
	
	public void gameOver() {
		playerwin = new JLabel();
		playerwin.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.1f));
		playerwin.setBounds(350, 300, 800, 60);
		String text;
		if (SCORE.player1 == maxscore)
		{
			playerwin.setForeground(Color.blue);
			text = "Player 1 Wins";
		}
		else
		{
			playerwin.setForeground(Color.red);
			text = "Player 2 Wins";
		}
		playerwin.setText("GAME OVER: " + text);
		playerwin.setFont(new Font("Bauhaus 93",Font.BOLD,40));
		
		rematch= new JButton();
		rematch.setBounds(300, 400, 220, 60);
		rematch.addActionListener(this);
		rematch.setText("Rematch");
		rematch.setFocusable(false);
		rematch.setHorizontalTextPosition(JButton.CENTER);
		rematch.setVerticalTextPosition(JButton.CENTER);
		rematch.setFont(new Font("Bauhaus 93",Font.BOLD,40));
		rematch.setForeground(Color.white);
		rematch.setBackground(Color.gray);
		
		menu= new JButton();
		menu.setBounds(700, 400, 200, 60);
		menu.addActionListener(this);
		menu.setText("Menu");
		menu.setFocusable(false);
		menu.setHorizontalTextPosition(JButton.CENTER);
		menu.setVerticalTextPosition(JButton.CENTER);
		menu.setFont(new Font("Bauhaus 93",Font.BOLD,40));
		menu.setForeground(Color.white);
		menu.setBackground(Color.gray);
		
		this.add(playerwin);
		this.add(rematch);
		this.add(menu);
		repaint();
	}
	
	public void newBall() {
		random = new Random();
		ball = new Ball(screen_width/2 - ball_diameter/2,random.nextInt(scoreboard_height,screen_height-ball_diameter),ball_diameter,ball_diameter);
	}
	
	public void newPaddles() {
		paddle1 = new Paddle(0,(game_height/2) - (paddle_height/2),paddle_width,paddle_height,1);
		paddle2 = new Paddle(screen_width - paddle_width,(game_height/2) - (paddle_height/2),paddle_width,paddle_height,2);
	}
	
	public void move() {
		paddle1.move();
		paddle2.move();
		ball.move();
	}
	
	public void checkCollision() {
		//stopping paddles
		if (paddle1.y<=scoreboard_height)
			paddle1.y=scoreboard_height;
		if(paddle1.y >= screen_height - paddle_height)
			paddle1.y=screen_height - paddle_height;
		if (paddle2.y<=scoreboard_height)
			paddle2.y=scoreboard_height;
		if(paddle2.y >= screen_height - paddle_height)
			paddle2.y=screen_height - paddle_height;
		
		//bouncing the ball off of top and bottom of screen
		if (ball.y <= scoreboard_height)
			ball.setYDirection(-ball.yVelocity);
		if(ball.y >= screen_height - ball_diameter)
			ball.setYDirection(-ball.yVelocity);
		
		//bouncing ball off the paddles
		if(ball.intersects(paddle1)) {
			ball.xVelocity = -ball.xVelocity;
			ball.xVelocity++;
			if(ball.yVelocity > 0)
				ball.yVelocity++;
			else
				ball.yVelocity--;
			ball.setXDirection(ball.xVelocity);
			ball.setYDirection(ball.yVelocity);
		}
		if(paddle2.intersects(ball)) {
			ball.xVelocity = Math.abs(ball.xVelocity);
			ball.xVelocity++;
			if(ball.yVelocity > 0)
				ball.yVelocity++;
			else
				ball.yVelocity--;
			ball.setXDirection(-ball.xVelocity);
			ball.setYDirection(ball.yVelocity);
		}
		
		//scoring points 
		if(ball.x <= 0) {
			SCORE.player2++;
			if(SCORE.player2 < maxscore) {
				newPaddles();
				newBall();
			}
			else {
				stage = stages.over;
				gameOver();
			}
		}
		if(ball.x >= screen_width) {
			SCORE.player1 ++;
			if(SCORE.player1 < maxscore) {
				newPaddles();
				newBall();
			}
			else {
				stage = stages.over;
				gameOver();
			}
		}
	}
	
	public void run() {
		//game loop
		long lastTime = System.nanoTime();
		double amountofTicks = 60.0;
		double ns = 1000000000 / amountofTicks;
		double delta =0;
		while(stage == stages.game) {
			long now = System.nanoTime();
			delta += (now - lastTime)/ns;
			lastTime = now;
			if (delta >= 1) {
				move();
			checkCollision();
			repaint();
			delta--;
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(field, 0,60,this);
		repaint();
		if(stage == stages.game)
			draw(g);
	}
	
	public void draw(Graphics g) {
		paddle1.draw(g);
		paddle2.draw(g);
		ball.draw(g, ball_color);
		SCORE.draw(g);
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			paddle1.keyPressed(e);
			paddle2.keyPressed(e);
		}
		
		public void keyReleased(KeyEvent e) {
			paddle1.keyReleased(e);
			paddle2.keyReleased(e);
		}
	}

	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == tennis) {
			icon = new ImageIcon(tennisPath);
			ball_color = 't';
		}
		else if (e.getSource() == ping) {
			icon = new ImageIcon(pingPath);
			ball_color = 'p';
		}
			else if(e.getSource() == air) {
			icon = new ImageIcon(airPath);
			ball_color = 'a';
		}
		else
		{
			stage = stages.game;
			startGame();
		}
		field = icon.getImage();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub	
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if(e.getSource() == tennis)
			tennis.setForeground(Color.yellow);
		else if (e.getSource() == ping)
			ping.setForeground(Color.yellow);
		else if (e.getSource() == air)
			air.setForeground(Color.yellow);
		else
			start.setForeground(Color.yellow);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if(e.getSource() == tennis)
			tennis.setForeground(Color.green);
		else if (e.getSource() == ping)
			ping.setForeground(Color.blue);
		else if(e.getSource() == air)
			air.setForeground(Color.pink);
		else
			start.setForeground(Color.red);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.remove(rematch);
		this.remove(menu);
		this.remove(playerwin);
		if(e.getSource() == rematch)
		{
			stage = stages.game;
			startGame();
		}
		else
		{
			stage = stages.select;
			panel.add(title);
			selectMenu();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		maxscore = slider.getValue();
		
	}

}
