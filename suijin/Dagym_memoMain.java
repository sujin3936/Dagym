package suijin;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

import chayoung.Dagym_main2;
import chuntaek.Total;
import jihyun.Dagym_record;
import minjun.Commonbanner;
import suijin.Dagym_memoDiary;

import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dagym_memoMain extends JFrame implements ActionListener{
	private Dagym_main dagymM = new Dagym_main();
	private JTextArea preview1;
	private JPanel top_d1,preview2;
	private int num=0;
	JLabel exercise_name, exercise_kg, exercise_num;
	
	public Dagym_memoMain() {
		//----------------------------메인디자인---------------------------------
		
		
		//하단 버튼에 이미지 삽입
		JButton calendarB = dagymM.createButton("images/001.png");
        JButton memoB = dagymM.createButton("images/002.png");
        JButton addB = dagymM.createButton("images/003.png");
        JButton graphB = dagymM.createButton("images/004.png");
        JButton userB = dagymM.createButton("images/005.png");
		      
		
		//패널 생성 및 바탕색 흰색 지정
        JPanel center = new JPanel();
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
	
	

	
		
		//----------------------------center 안 디자인------------------------------------
		
		
		//center안
		//운동일지,일기 큰 패널
		JPanel diaryP1 = new JPanel(new BorderLayout());
		JPanel diaryP2 = new JPanel(new BorderLayout());
		diaryP1.setBorder(new LineBorder(Color.BLACK, 3, true));
		diaryP2.setBorder(BorderFactory.createMatteBorder(0, 3, 3, 3, Color.BLACK));
		
		
		//큰 패널 안 배치할 패널들
		top_d1 = new JPanel();
		JPanel bottom_d1 = new JPanel();
		JPanel top_d2 = new JPanel();
		JPanel bottom_d2 = new JPanel();
		preview2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
		top_d1.setBackground(Color.WHITE);
		top_d2.setBackground(Color.WHITE);
		bottom_d1.setBackground(Color.WHITE);
		bottom_d2.setBackground(Color.WHITE);
		
		
		//필요한 JFrame 선언
		JLabel exercise_diary = new JLabel("운동일기");
        JLabel exercise_diary2 = new JLabel("운동일지");
        
        preview1 = new JTextArea("미리보기 화면");
        preview1.setLayout(new BorderLayout());
        
        JButton diaryMoveBtn1 = new JButton(">");
        JButton diaryMoveBtn2 = new JButton(">");
        
        exercise_name = new JLabel("입력하세요");
        exercise_kg = new JLabel("무게");
        exercise_num = new JLabel("횟수");
        
        
        //미리보기 화면 보더 지정
        preview1.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, Color.BLACK));
        preview1.setLineWrap(true); // 자동 줄 바꿈 활성화
        preview1.setWrapStyleWord(true);

        preview2.setOpaque(true);
        preview2.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, Color.BLACK));
        
        loadPreviewData();
        

        //배치
        center.setLayout(new GridLayout(2,1));
        center.add(diaryP1);
        center.add(diaryP2);
        diaryP1.add(top_d1, BorderLayout.CENTER);
        diaryP1.add(bottom_d1, BorderLayout.SOUTH);
        diaryP2.add(top_d2, BorderLayout.CENTER);
        diaryP2.add(bottom_d2, BorderLayout.SOUTH);
        
        top_d1.setLayout(new BorderLayout(0,5));
        top_d1.add(exercise_diary, BorderLayout.NORTH);
        top_d1.add(preview1, BorderLayout.CENTER);
        bottom_d1.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottom_d1.add(diaryMoveBtn1);
        
        preview2.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        exercise_name.setBorder(new LineBorder(Color.BLACK, 2, true));
        exercise_kg.setBorder(new LineBorder(Color.BLACK, 2, true));
        exercise_num.setBorder(new LineBorder(Color.BLACK, 2, true));
        
        exercise_name.setPreferredSize(new Dimension(250, 30));
        exercise_kg.setPreferredSize(new Dimension(70, 30));
        exercise_num.setPreferredSize(new Dimension(70, 30));
        
        preview2.add(exercise_name);
        preview2.add(exercise_kg);
        preview2.add(exercise_num);
  
        top_d2.setLayout(new BorderLayout(0,5));
        top_d2.add(exercise_diary2, BorderLayout.NORTH);
        top_d2.add(preview2, BorderLayout.CENTER);
        bottom_d2.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottom_d2.add(diaryMoveBtn2);
        
        
		
        
        //-------------------------------------------------------------
        //버튼 눌렀을 때 이동 이벤트
        diaryMoveBtn1.setActionCommand("운동일기");
        diaryMoveBtn2.setActionCommand("운동일지");
        diaryMoveBtn1.addActionListener(this);
		diaryMoveBtn2.addActionListener(this);
		
        calendarB.setActionCommand("calendarB");
        memoB.setActionCommand("memoB");
        addB.setActionCommand("addB");
        graphB.setActionCommand("graphB");
        userB.setActionCommand("userB");
		
		calendarB.addActionListener(this);
        memoB.addActionListener(this);
        addB.addActionListener(this);
        graphB.addActionListener(this);
        userB.addActionListener(this);
	}
	
	public void updatePreview(String text) {
			preview1.setText(text);
	        preview1.revalidate();
	        preview1.repaint();
    }
	
	public void updatePreview2(String text) {
		 if (num == 0) {
	        System.out.println("Update exercise_name: " + text);
	        exercise_name.setText(text);
		 } else if (num == 1) {
	        System.out.println("Update exercise_kg: " + text);
	        exercise_kg.setText(text);
		 } else {
	        System.out.println("Update exercise_num: " + text);
	        exercise_num.setText(text);
		 }
		 preview2.revalidate();
		 preview2.repaint();
	}
	
	private void loadPreviewData() {
        //데이터베이스에서 값을 가져와서 preview1, preview2에 업데이트
        String diaryText = fetchDataFromDatabase("exerdiary_tbl", 4);
        updatePreview(diaryText);

        for(int i=0; i<3; i++, num++) {
        	String exerciseText = fetchDataFromDatabase("exerjournal_tbl", num);
            updatePreview2(exerciseText);
        }
    }

	
    private String fetchDataFromDatabase(String tableName, int num) {
    	String selectQuery = null, fetchedData = null;
    	Date d = new Date();
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	String nowTime = dateFormat.format(d);

    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("JDBC 드라이버를 정상적으로 로드함");
		} catch(ClassNotFoundException e1) {
			System.out.println("로드 실패");
		}
		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dagym?serverTimezone=UTC", "root", "1234");
			System.out.println("DB 연결 완료");
			Statement dbSt = con.createStatement();
			System.out.println("JDBC 드라이버가 정상적으로 연결되었습니다");
			
			if(tableName.equals("exerdiary_tbl")) {
				selectQuery = "SELECT Diary FROM exerdiary_tbl WHERE Calendar = '" + nowTime + "'";
			} else if(tableName.equals("exerjournal_tbl") && num == 0) {
				selectQuery = "SELECT ExerCategory FROM exerjournal_tbl WHERE Calendar = '" + nowTime + "'";
			} else if(tableName.equals("exerjournal_tbl") && num == 1) {
				selectQuery = "SELECT EquipWeight FROM exerjournal_tbl WHERE Calendar = '" + nowTime + "'";
			} else if(tableName.equals("exerjournal_tbl") && num == 2) {
				selectQuery = "SELECT Frequency FROM exerjournal_tbl WHERE Calendar = '" + nowTime + "'";
			} 
				
			 
			try (PreparedStatement preparedStatement = con.prepareStatement(selectQuery)) {
	                try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                    while (resultSet.next()) {
	                    	if(num == 0) {
	                    		String exerciseC1 = resultSet.getString("ExerCategory");
		                        fetchedData = exerciseC1;
	                    	} else if(num == 1) {
	                    		String exerciseC2 = resultSet.getString("EquipWeight");
		                        fetchedData = exerciseC2;
	                    	} else if(num == 2){
	                    		String exerciseC3 = resultSet.getString("Frequency");
	                        	fetchedData = exerciseC3;
	                    	} else if(num == 4) {
	                    		String diaryContent = resultSet.getString("Diary");
		                        fetchedData = diaryContent;
	                    	}
	                    		
	                        
	                    }
	                }
	            }
	
			System.out.println("데이터 검색 완료");
			
		} catch (SQLException e3) {
			System.out.println("SQLException:"+e3.getMessage());
		}
        return fetchedData;
    }
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		
		if(s.equals("운동일기")) {
			Dagym_memoDiary memoD = new Dagym_memoDiary();
			memoD.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			memoD.setSize(540,960);
			memoD.show();
			dispose();
		} else if(s.equals("운동일지")) {
			Dagym_memoExercise memoE = new Dagym_memoExercise();
            memoE.main(null);
            memoE.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            memoE.setSize(540,960);
            memoE.show();
			dispose();
		} else if(s.equals("calendarB")) {
			Dagym_main2.main(null);
		} else if(s.equals("memoB")) {
			Dagym_memoMain.main(null);
		} else if(s.equals("addB")) {
			Dagym_record.main(null);
		} else if(s.equals("graphB")) {
			Total.main(null);
		} else if(s.equals("userB")) {
			Commonbanner.main(null);
		}
		
	}
	
	//memoMain 호출
	public void mainCall() {
		Dagym_memoMain memoM = new Dagym_memoMain();
		memoM.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		memoM.setSize(540,960);
		memoM.show();
	}

	public static void main(String[] args) {
		Dagym_memoMain memoM = new Dagym_memoMain();
		
		memoM.setTitle("dagym");
		memoM.setSize(540, 960);
		memoM.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		memoM.setVisible(true);
	}

}
