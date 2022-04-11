// CLASSES: PokerTableDisplay, HandDisplay, CardDisplay
//
// Author: Dr. Olivier Tremblay-Savard
//
// REMARKS: You should not modify this file. This class handles all the GUI elements.
//
//-----------------------------------------


import javax.swing.*;
import java.awt.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.event.*;

public class PokerTableDisplay
{
	//Constants
	private String PATH_CARD_BACK = "./Images/backCard2150.png";
	private Color POKER_TABLE_COLOR = new Color(50, 175, 50);
	private int NUM_MESSAGE_ROWS = 4;
	
	//Instance variables
	private JLabel[] messageLabels;  //to display messages on the status of the game, we have different rows
	private JButton actionButton;  //button that will serve different purposes depending on the status of the game
	private Dimension screenSize;  //dimensions of the JFrame
	private JPanel contentPanel;  //the whole content panel
	
	private GameLogicable gameLogic;  //the game logic runs the game
	
	private boolean waitForUserInput;  //while this is true, we have to wait, and the button is enabled
	
	private HandDisplay humanHD;  //HandDisplay for the human player hand
	private HandDisplay cpuHD;  //HandDisplay for the cpu player hand
	
	private Object lock;  //to wait
	
	
	//Constructor:
	public PokerTableDisplay(GameLogicable gl)
	{
		gameLogic = gl;
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();  //get screen size
		initJFrame();
		
		waitForUserInput = true;  //will start at true --> human player makes the first move

		lock = new Object();
		this.runGame();  //starting the game
	}
	
	
	//Initialization of the JFrame
	private void initJFrame()
	{
		JPanel centerPanel = new JPanel(new GridLayout(5, 1));
		centerPanel.setBackground(POKER_TABLE_COLOR);
		
		messageLabels = new JLabel[NUM_MESSAGE_ROWS];
		//Initializing the JLabels
		for (int i = 0; i < NUM_MESSAGE_ROWS; i++)
		{
			messageLabels[i] = new JLabel("");
			messageLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
			messageLabels[i].setFont(new Font("Serif", Font.BOLD, convertSize(0.025, true)));
			centerPanel.add(messageLabels[i]);
		}
		
		//Initializing the JButton
		JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPane.setBackground(POKER_TABLE_COLOR);
		actionButton = new JButton("Proceed");
		actionButton.setPreferredSize(new Dimension(convertSize(0.15, true), convertSize(0.075, false)));
		actionButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) 
			{ 
				waitForUserInput = false;
				synchronized (lock){
					lock.notifyAll();  //unlocking
				}
			}
		} );
		buttonPane.add(actionButton);
		
		centerPanel.add(buttonPane);
		
		//Full content panel
		contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBackground(POKER_TABLE_COLOR);
		
		contentPanel.add(centerPanel, BorderLayout.CENTER);
		
		//Adding the CPU hand:
		humanHD = new HandDisplay(gameLogic.getCPUHand(), false);
		contentPanel.add(humanHD, BorderLayout.NORTH);
		
		//Adding the human hand:
		cpuHD = new HandDisplay(gameLogic.getHumanHand(), true);
		contentPanel.add(cpuHD, BorderLayout.SOUTH);
		
		//Creating the frame
		JFrame frame = new JFrame("5-card draw Poker");
		frame.setSize(screenSize.width, screenSize.height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);

		frame.setContentPane(contentPanel);
	}
	
	//To run the game and update the interface according to the game logic
	public void runGame()
	{
		String[] messages;
		while(true) //to turn off the game, click on the X to close the window
		{
			messages = new String[NUM_MESSAGE_ROWS]; //emptying the array by creating a new one
			waitForUserInput = gameLogic.nextState(messages);
			
			showMessages(messages);
			
			humanHD.updateHandDisplay();
			cpuHD.updateHandDisplay();
			
			if (waitForUserInput)
			{
				actionButton.setEnabled(true);
				try
				{
					synchronized (lock)
					{
						lock.wait();
					}
				}
				catch(InterruptedException ie){} 
			}
			else
			{
				actionButton.setEnabled(false);
				wait(2);
			}
		}
	}
	
	//To display the messages
	private void showMessages(String[] messages)
	{
		for (int i = 0; i < NUM_MESSAGE_ROWS; i++)
		{
			if (messages[i] == null)
				messageLabels[i].setText("");
			else
				messageLabels[i].setText(messages[i]);
		}
	}
	
	
	//Method to convert sizes, from percentages (0-1) to actual pixels, relative to the width or height
	private int convertSize(double fraction, boolean width)
	{
		if(width)  //fraction of the width
			return (int)(fraction * screenSize.width);
		return (int)(fraction * screenSize.height);  //otherwise, return fraction of the height)
	}
	
	
	//To pause the game
	private static void wait(int seconds)
	{
		try
		{
			Thread.sleep(seconds * 1000);  //required milliseconds
		}
		catch(InterruptedException ie){ie.printStackTrace();}
	}
	
	
	
	//
	//Inner class: HandDisplay
	//
	private class HandDisplay extends JPanel
	{
		//Instance variables
		private Handable theHand;
		private boolean playable; //playable is true if it's the user's hand, false for CPU 
		
		//Constructor
		public HandDisplay(Handable h, boolean playable)
		{
			super(new FlowLayout(FlowLayout.CENTER, 25, 5));
			theHand = h;
			this.playable = playable;
			this.setBackground(POKER_TABLE_COLOR);
			updateCardDisplays();
		}
		
		//Call this method after changing the hand
		private void updateHandDisplay()
		{
			removeAll();
			updateCardDisplays();
			revalidate();
			repaint();
		}
		
		//Adding the CardDisplay objects based on the hand
		private void updateCardDisplays()
		{
			for (int i = 0; i < Hand.HAND_SIZE; i++)
			{
				CardDisplay cd = new CardDisplay(theHand.getCard(i));
				if (playable)
				{
					cd.addMouseListener(new MouseAdapter()
					{
						public void mousePressed(MouseEvent e)
						{
							CardDisplay clicked = (CardDisplay)e.getSource();
							clicked.theCard.switchSelectedState();
							clicked.repaint();
						}
					});
				}
				this.add(cd);
			}
		}
	}
	
	
	//
	//Inner class: CardDisplay
	//
	private class CardDisplay extends JPanel
	{
		//Instance variables
		private Cardable theCard;
		private int width;
		private int height;
		
		//Constructor
		public CardDisplay(Cardable theCard)
		{
			this.theCard = theCard;
			width = convertSize(0.05, true);
			height = convertSize(0.12, false);
			this.setPreferredSize(new Dimension(width+1, height+1));
		}
		
		public void paintComponent(Graphics g)
		{
			if (theCard == null)  //to show a card that has been discarded (null) from the hand
			{
				g.setColor(POKER_TABLE_COLOR);
				g.fillRoundRect(0, 0, width, height, 5, 5);
				g.setColor(Color.BLACK);
				g.drawRoundRect(0, 0, width, height, 5, 5);	
				return;
			}
				
			if (theCard.getSelected())
				g.setColor(Color.LIGHT_GRAY);
			else
				g.setColor(Color.WHITE);
			g.fillRoundRect(0, 0, width, height, 5, 5);
			g.setColor(Color.BLACK);
			g.drawRoundRect(0, 0, width, height, 5, 5);
			
			if (theCard.getFaceUp())
			{
				Cardable.Suit theSuit = theCard.getSuit();
				if(theSuit == Cardable.Suit.HEART || theSuit == Cardable.Suit.DIAMOND)
					g.setColor(Color.RED);
				else //either club of spade
					g.setColor(Color.BLACK);

				g.setFont(new Font("TimesRoman", Font.BOLD, convertSize(0.01, true)));
				g.drawString(theCard.toString(), 7, 20);
			}
			else
			{
				BufferedImage img = null;
				try{
					img = ImageIO.read(new File(PATH_CARD_BACK));
				}
				catch(IOException ioe){
					System.err.println("Image file not found! It should be here: " + PATH_CARD_BACK);
					System.exit(0);
				}
				
				if (img != null)
				{
					g.drawImage(img, 0, 0, width, height, null);
				}
			}
		}
	}
}