package suijin;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class Dagym_main extends JFrame {
	
	public Dagym_main() {	
		//하단 버튼에 이미지 삽입
		JButton calendarB = createButton("images/001.png");
        JButton memoB = createButton("images/002.png");
        JButton addB = createButton("images/003.png");
        JButton graphB = createButton("images/004.png");
        JButton userB = createButton("images/005.png");
        
        //버튼 눌렀을 때 이벤트 지정
        memoB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Dagym_memoMain.main(null);
			}
		});
		
		//패널 생성 및 바탕색 흰색 지정
        JPanel center = new JPanel(new FlowLayout());
		JPanel bottom = new JPanel(new GridLayout());
	
		
		Container ct = getContentPane();
		ct.setLayout(new BorderLayout());
		ct.add(center, BorderLayout.CENTER);
		ct.add(bottom, BorderLayout.SOUTH);
		
		//패널들 바탕색 지정
		center.setOpaque(true);
        center.setBackground(Color.WHITE);
        bottom.setOpaque(true);
        bottom.setBackground(Color.WHITE);
        
		bottom.add(calendarB);
		bottom.add(memoB);
		bottom.add(addB);
		bottom.add(graphB);
		bottom.add(userB);
	}
	
	//버튼에 이미지 삽입하는 메소드
	protected JButton createButton(String imageName) {
        JButton button = new JButton(new ImageIcon(imageName));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(100, 100));
        return button;
    }
	
	
	
	public static void main(String[] args) {
		Dagym_main win = new Dagym_main();
		win.setTitle("dagym");
		win.setSize(540, 960);
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.setVisible(true);
		
	}

}
