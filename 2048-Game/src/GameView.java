 
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
 
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
 
public class GameView implements IBaseData{
	private static final int jframeWidth = 400;
	private static final int jframeHeight = 530;
	private static int val = 0;// val stores the current highest value
	private static int score = 0;
	private JFrame jframeMain;
	private JLabel jlblTitle;
	private JLabel jlblScoreName;
	private JLabel jlblScore;
	private GameBoard gameBoard;
	
	
	private JLabel jlblTip;
 
	public GameView() {
		init();
	}
 
	@Override
	public void init() {
		jframeMain = new JFrame("2048 ");
		jframeMain.setSize(jframeWidth, jframeHeight);
		jframeMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframeMain.setLocationRelativeTo(null);
		jframeMain.setResizable(false);
		jframeMain.setLayout(null);
		
		jlblTitle = new JLabel("2048", JLabel.CENTER);
		jlblTitle.setFont(topicFont);
		jlblTitle.setForeground(Color.BLACK);
		jlblTitle.setBounds(50, 0, 150, 60);
		jframeMain.add(jlblTitle);
		
		
		 // fractional area
		jlblScoreName = new JLabel(" ", JLabel.CENTER);
		jlblScoreName.setFont(scoreFont);
		jlblScoreName.setForeground(Color.WHITE);
		jlblScoreName.setOpaque(true);
		jlblScoreName.setBackground(Color.GRAY);
		jlblScoreName.setBounds(250, 0, 120, 30);
		jframeMain.add(jlblScoreName);
		
		jlblScore = new JLabel("0", JLabel.CENTER);
		jlblScore.setFont(scoreFont);
		jlblScore.setForeground(Color.WHITE);
		jlblScore.setOpaque(true);
		jlblScore.setBackground(Color.GRAY);
		jlblScore.setBounds(250, 30, 120, 30);
		jframeMain.add(jlblScore);
		
		 // Description:
		 jlblTip = new JLabel("Press esc to restart",
				JLabel.CENTER);
		jlblTip.setFont(normalFont);
		jlblTip.setForeground(Color.DARK_GRAY);
		jlblTip.setBounds(0, 60, 400, 40);
		jframeMain.add(jlblTip);
		
		gameBoard = new GameBoard();
		gameBoard.setBounds(0, 100, 400, 400);
		gameBoard.setBackground(Color.GRAY);
		gameBoard.setFocusable(true);
		gameBoard.setLayout(new FlowLayout());
		jframeMain.add(gameBoard);
	}
	
	 // The game panel needs to listen for key values.
	 // This uses an inner class to inherit the JPanel class.
	 // and implement the keyPressed method in the interface KeyListener ,
	 // The square is passed
	@SuppressWarnings("serial")
	class GameBoard extends JPanel implements KeyListener {
		private static final int CHECK_GAP = 10;
		private static final int CHECK_ARC = 20;
		private static final int CHECK_SIZE = 86;
 
		private Check[][] checks = new Check[4][4];
		private boolean isadd = true;
		
		public GameBoard() {
			initGame();
			addKeyListener(this);
		}
 
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				initGame();
				break;
			case KeyEvent.VK_LEFT:
				moveLeft();
				createCheck();
				break;
			case KeyEvent.VK_RIGHT:
				moveRight();
				createCheck();
				break;
			case KeyEvent.VK_UP:
				moveUp();
				createCheck();
				break;
			case KeyEvent.VK_DOWN:
				moveDown();
				createCheck();
				break;
			default:
				break;
			}
			repaint();
		}
		
		private void initGame() {
            score = 0;
			for (int indexRow = 0; indexRow < 4; indexRow++) {
				for (int indexCol = 0; indexCol < 4; indexCol++) {
					checks[indexRow][indexCol] = new Check();
				}
			}
			 // generate two numbers
			isadd = true;
			createCheck();
			isadd = true;
			createCheck();
		}
		
		private void createCheck() {
			List<Check> list = getEmptyChecks();
			
			if (!list.isEmpty() && isadd) {
				Random random = new Random();
				int index = random.nextInt(list.size());
				Check check = list.get(index);
				 // 2, 4 probability of occurrence 1:1, can be modified to make game harder or easier
				check.value = (random.nextInt(2)==0) ? 2 : 4;
				isadd = false;
			} 
		}
		
		 // Get a blank square
		private List<Check> getEmptyChecks() {
			List<Check> checkList = new ArrayList<>();
			for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (checks[i][j].value == 0) {
                    	checkList.add(checks[i][j]);
                    }
                }
            }
			
			return checkList;
		}
		
		//Check winning condition
		private boolean isGameWon() {
			return val==2048?true:false;
		}
		
		private boolean judgeGameOver() {
			jlblScore.setText(score + "");
			
			if (!getEmptyChecks().isEmpty()) {
				return false;
			}
			
			for (int i = 0; i < 3; i++) {
	            for (int j = 0; j < 3; j++) {
	                 // Determine whether there are mergeable squares
	                if (checks[i][j].value == checks[i][j + 1].value
	                		|| checks[i][j].value == checks[i + 1][j].value) {
	                    return false;
	                }
	            }
	        }
			
		    return true;
		}
		
		private boolean moveLeft() {
			for (int i = 0; i < 4; i++) {
				for (int j = 1, index = 0; j < 4; j++) {
					if (checks[i][j].value > 0) {
						if (checks[i][j].value == checks[i][index].value) {
							val= checks[i][index++].value <<= 1;
							score+=val;
							checks[i][j].value = 0;
							isadd = true;
						} else if (checks[i][index].value == 0) {
							checks[i][index].value = checks[i][j].value;
							checks[i][j].value = 0;
							isadd = true;
						} else if (checks[i][++index].value == 0) {
							checks[i][index].value = checks[i][j].value;
							checks[i][j].value = 0;
							isadd = true;
						}
					} 
				}
			}
			return isadd;
		}
		
		private boolean moveRight() {
			for (int i = 0; i < 4; i++) {
				for (int j = 2, index = 3; j >= 0; j--) {
					if (checks[i][j].value > 0) {
						if (checks[i][j].value == checks[i][index].value) {
							val= checks[i][index--].value <<= 1;
							score+=val;
							checks[i][j].value = 0;
							isadd = true;
						} else if (checks[i][index].value == 0) {
							checks[i][index].value = checks[i][j].value;
							checks[i][j].value = 0;
							isadd = true;
						} else if (checks[i][--index].value == 0) {
							checks[i][index].value = checks[i][j].value;
							checks[i][j].value = 0;
							isadd = true;
						}
					}
				}
			}
 
			return isadd;
		}
		
		private boolean moveUp() {
			for (int i = 0; i < 4; i++) {
				for (int j = 1, index = 0; j < 4; j++) {
					if (checks[j][i].value > 0) {
						if (checks[j][i].value == checks[index][i].value) {
							val= checks[index++][i].value <<= 1;
							score+=val;
							checks[j][i].value = 0;
							isadd = true;
						} else if (checks[index][i].value == 0) {
							checks[index][i].value = checks[j][i].value;
							checks[j][i].value = 0;
							isadd = true;
						} else if (checks[++index][i].value == 0){
							checks[index][i].value = checks[j][i].value;
								checks[j][i].value = 0;
								isadd = true;
						}
					} 
				}
			}
 
			return isadd;
		}
		
		private boolean moveDown() {
			for (int i = 0; i < 4; i++) {
				for (int j = 2, index = 3; j >= 0; j--) {
					if (checks[j][i].value > 0) {
						if (checks[j][i].value == checks[index][i].value) {
							val= checks[index--][i].value <<= 1;
							score+=val;
							checks[j][i].value = 0;
							isadd = true;
						} else if (checks[index][i].value == 0) {
							checks[index][i].value = checks[j][i].value;
							checks[j][i].value = 0;
							isadd = true;
						} else if (checks[--index][i].value == 0) {
							checks[index][i].value = checks[j][i].value;
							checks[j][i].value = 0;
							isadd = true;
						}
					} 
				}
			}
 
			return isadd;
		}
		
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					drawCheck(g, i, j);
				}
			}
			
			// GameOver
			if (judgeGameOver()) {
				g.setColor(new Color(64, 64, 64, 150));
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(Color.WHITE);
				g.setFont(topicFont);
				FontMetrics fms = getFontMetrics(topicFont);
				String value = "Game Over!";
				g.drawString(value,
						(getWidth()-fms.stringWidth(value)) / 2,
						getHeight() / 2);
			}
			
			//Game Won
			if (isGameWon()) {
				g.setColor(new Color(64, 64, 64, 150));
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(Color.WHITE);
				g.setFont(topicFont);
				FontMetrics fms = getFontMetrics(topicFont);
				String value = "Congrats";
				g.drawString(value,
						(getWidth()-fms.stringWidth(value)) / 2,
						getHeight() / 2-20);
				value="You've Won";
				g.drawString(value,
						(getWidth()-fms.stringWidth(value)) / 2,
						getHeight() / 2+20);
			}
		}
		
		 // draw a square
		 // The Graphics2D class extends the Graphics class.
		 // Provides more sophisticated control over geometry, coordinate transformation, color management, and text layout
		private void drawCheck(Graphics g, int i, int j) {
			Graphics2D gg = (Graphics2D) g;
			gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			gg.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                    RenderingHints.VALUE_STROKE_NORMALIZE);
			Check check = checks[i][j];
			gg.setColor(check.getBackground());
			 // draw a fillet
			 // x - the x coordinate of the rectangle to be filled.
			 // y - the y coordinate of the rectangle to be filled.
			 // width - the width of the rectangle to be filled.
			 // height - the height to fill the rectangle.
			 // arcwidth - the horizontal diameter of 4 arcs.
			 // archeight - the vertical diameter of 4 arcs.
			gg.fillRoundRect(CHECK_GAP + (CHECK_GAP + CHECK_SIZE) * j,
					CHECK_GAP + (CHECK_GAP + CHECK_SIZE) * i,
					CHECK_SIZE, CHECK_SIZE, CHECK_ARC, CHECK_ARC);
			gg.setColor(check.getForeground());
			gg.setFont(check.getCheckFont());
			
			 // Measure the length, width and height of the text.
			FontMetrics fms = getFontMetrics(check.getCheckFont());
			String value = String.valueOf(check.value);
			 //Use the current color of this graphics context to draw the text given by the specified iterator.
			 //getAscent() is a method in FontMetrics.
			 //It returns the distance between the baseline of a font to the ascender of most characters in the font.
			 //getDescent is the lower part
			gg.drawString(value,
					CHECK_GAP + (CHECK_GAP + CHECK_SIZE) * j +
					(CHECK_SIZE - fms.stringWidth(value)) / 2,
					CHECK_GAP + (CHECK_GAP + CHECK_SIZE) * i +
					(CHECK_SIZE - fms.getAscent() - fms.getDescent()) / 2
					+ fms.getAscent());
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
		}
 
		@Override
		public void keyTyped(KeyEvent e) {
		}
		
	}
 
	@Override
	public void showView() {
		jframeMain.setVisible(true);
	}
	
}
