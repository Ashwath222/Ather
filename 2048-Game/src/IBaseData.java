import java.awt.Font;

public interface IBaseData {
	Font topicFont = new Font("Roboto", Font.BOLD, 50);
	 Font scoreFont = new Font("Roboto", Font.BOLD, 30);
	 Font normalFont = new Font(" ", Font.PLAIN, 20);
	 Font font1 = new Font(" ", Font.BOLD, 46);
	 Font font2 = new Font(" ", Font.BOLD, 40);
	 Font font3 = new Font(" ", Font.BOLD, 34);
	 Font font4 = new Font(" ", Font.BOLD, 28);
	 Font font5 = new Font(" ", Font.BOLD, 22);
	int normalFontdata = 20;
	int topicFontdata = 30;
	
	void init();
	void showView();
}