package suijin;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.LineBorder;

import suijin.Dagym_main;
import suijin.Dagym_memoMain;

public class Dagym_memoDiary extends JFrame implements ActionListener {
	private Dagym_main dagymM = new Dagym_main();
	private Dagym_memoMain memoM = new Dagym_memoMain();
	Calendar calendar = Calendar.getInstance();
	JTextArea writeDiary;
	JComboBox<Integer> yearComboBox = new JComboBox<>();
	JComboBox<String> monthComboBox = new JComboBox<>();
	JComboBox<Integer> dayComboBox = new JComboBox<>();
	
	public Dagym_memoDiary() {
		
		//메인디자인
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
	
	

		
		
		
		//----------------------------------------------------------------
		
		SwingUtilities.invokeLater(() -> {

            // 현재 년,월,일 가져오기
			int currentYear = calendar.get(Calendar.YEAR);
	        int currentMonth = calendar.get(Calendar.MONTH) + 1;   // Month는 0부터 시작하므로 +1 해줍니다.
	        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

            // 년도 JComboBox 생성
            DefaultComboBoxModel<Integer> yearModel = new DefaultComboBoxModel<>();
            for (int i = currentYear; i >= 1900; i--) {
                yearModel.addElement(i);
            }
            yearComboBox.setModel(yearModel);
            yearComboBox.setSelectedItem(currentYear);

            // 월 JComboBox 생성
            DefaultComboBoxModel<String> monthModel = new DefaultComboBoxModel<>();
            for (int i = 1; i <= 12; i++) {
                monthModel.addElement(String.valueOf(i));
            }
            monthComboBox.setModel(monthModel);
            monthComboBox.setSelectedItem(String.valueOf(currentMonth)); 

            // 일 JComboBox 초기 생성
            DefaultComboBoxModel<Integer> dayModel = new DefaultComboBoxModel<>();
            for (int i = 1; i <= 31; i++) {
                dayModel.addElement(i);
            }
            dayComboBox.setModel(dayModel);
            dayComboBox.setSelectedItem(currentDay); // 현재 일로 초기값 설정

            // 년도, 월 선택에 따라 일수 업데이트
            ItemListener updateDays = new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        // 선택된 년도와 월을 가져오기
                    	Integer selectedYear = (Integer) yearComboBox.getSelectedItem();
                        String selectedMonthStr = (String) monthComboBox.getSelectedItem();
                        Integer selectedMonth = selectedMonthStr != null ? Integer.parseInt(selectedMonthStr) : null;

                        // 해당 년도, 월의 마지막 날짜 가져오기
                        Calendar cal = Calendar.getInstance();
                        cal.set(selectedYear, selectedMonth - 1, 1);
                        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

                        // 기존 일수 모델을 제거하고, 새로운 일수 모델을 설정
                        DefaultComboBoxModel<Integer> newDayModel = new DefaultComboBoxModel<>();
                        dayComboBox.setModel(newDayModel);

                        for (int i = 1; i <= maxDay; i++) {
                            newDayModel.addElement(i);
                        }
                    }
                }
            };

            yearComboBox.addItemListener(updateDays);
            monthComboBox.addItemListener(updateDays);
            
            dayComboBox.addItemListener(e -> {
                // 일 선택 시 diary 데이터 불러오기
                loadDiaryFromDatabase();
            });
		
		});

		
		loadDiaryFromDatabase();
		
		//----------------------------------------------------------------
		//center안
		JPanel calendarPanel = new JPanel();
		JLabel exercise_diary = new JLabel("운동일기");
        JButton diaryMoveBtn1 = new JButton("<");
        JButton saveBtn = new JButton("저장");
        writeDiary = createHintTextArea("입력하세요", 12, 80);
        JScrollPane scrollPane = new JScrollPane(writeDiary);

        writeDiary.setLineWrap(true);
        writeDiary.setWrapStyleWord(true);
        
       
        // 운동일기 텍스트 위치 조정
        exercise_diary.setHorizontalAlignment(JLabel.CENTER);
        exercise_diary.setVerticalAlignment(JLabel.CENTER);

     
        //Label 배경색 지정 및 보더 설정
        exercise_diary.setOpaque(true);
        exercise_diary.setBackground(Color.WHITE);
        exercise_diary.setBorder(new LineBorder(Color.BLACK, 3, true));
        writeDiary.setBorder(new LineBorder(Color.GRAY, 3, true));
        calendarPanel.setOpaque(true);
        calendarPanel.setBackground(Color.WHITE);
      
        
        // 배치
        center.setLayout(null);
        center.add(diaryMoveBtn1);
        center.add(exercise_diary);
        center.add(saveBtn);
        center.add(scrollPane);
        center.add(calendarPanel);
        calendarPanel.add(yearComboBox);
        calendarPanel.add(monthComboBox);
        calendarPanel.add(dayComboBox);

        // 크기와 위치 설정
        center.setBorder(new LineBorder(Color.BLACK, 3, true));
        exercise_diary.setBounds(93, 40, 390, 40);
        diaryMoveBtn1.setBounds(43, 40, 50, 40);
        saveBtn.setBounds(413, 740, 70, 40);
        scrollPane.setBounds(43, 150, 440, 570);
        calendarPanel.setBounds(20, 100, 200, 30);
        
        
        
        
	    //----------------------------------------------------------------
	    //버튼 눌렀을 때 이동 이벤트
        //이전 버튼
        diaryMoveBtn1.setActionCommand("diaryMoveBtn1");
        saveBtn.setActionCommand("saveBtn");
		diaryMoveBtn1.addActionListener(this);
		saveBtn.addActionListener(this);
		
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
	
	private void loadDiaryFromDatabase() {
        try {
            // 현재 날짜 가져오기
	    	Integer selectedYear = (Integer) yearComboBox.getSelectedItem();
	        String selectedMonthStr = (String) monthComboBox.getSelectedItem();
	        Integer selectedMonth = selectedMonthStr != null ? Integer.parseInt(selectedMonthStr) : null;
	        Integer selectedDay = (Integer) dayComboBox.getSelectedItem();
            
            String selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth, selectedDay);

            // 데이터베이스 연결
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dagym?serverTimezone=UTC", "root",
                    "1234");
            Statement stmt = con.createStatement();

            // 쿼리 실행
            String query = "SELECT Diary FROM exerdiary_tbl WHERE Calendar = '" + selectedDate + "'";
            ResultSet rs = stmt.executeQuery(query);

            // 결과가 있다면 일기를 불러와 writeDiary에 설정
            if (rs.next()) {
                String diary = rs.getString("Diary");
                writeDiary.setText(diary);
            }

            // 자원 닫기
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	
	private JTextArea createHintTextArea(String hint, int columns, int columns2) {
        JTextArea textarea = new JTextArea(hint, columns, columns2);
        textarea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textarea.getText().equals(hint)) {
                	textarea.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textarea.getText().isEmpty()) {
                	textarea.setText(hint);
                }
            }
        });
        return textarea;
    }
	
	
	private void saveDiaryToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("JDBC 드라이버를 정상적으로 로드함");
		} catch(ClassNotFoundException e1) {
			System.out.println("로드 실패");
		}
        
        try {
	            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dagym?serverTimezone=UTC", "root",
	                    "1234");
	            System.out.println("DB 연결 완료");
			
	            Statement stmt = con.createStatement();
	            System.out.println("JDBC 드라이버가 정상적으로 연결되었습니다");
	
	            String t_diary = writeDiary.getText();
	            // 현재 날짜 가져오기
	            Integer selectedYear = (Integer) yearComboBox.getSelectedItem();
	            String selectedMonthStr = (String) monthComboBox.getSelectedItem();
	            Integer selectedMonth = selectedMonthStr != null ? Integer.parseInt(selectedMonthStr) : null;
	            Integer selectedDay = (Integer) dayComboBox.getSelectedItem();
	            
	            String selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth, selectedDay);
	
	
	            String checkQuery = "SELECT COUNT(*) AS count FROM exerdiary_tbl WHERE Calendar = '" + selectedDate + "'";
	            ResultSet checkResult = stmt.executeQuery(checkQuery);
	            checkResult.next();
	            int count = checkResult.getInt("count");
	
	            if (count > 0) {
	                String updateQuery = "UPDATE exerdiary_tbl SET Diary = '" + t_diary + "' WHERE Calendar = '" + selectedDate + "'";
	                stmt.executeUpdate(updateQuery);
	            } else {
	                String insertQuery = "INSERT INTO exerdiary_tbl(Calendar, Diary) VALUES ('" + selectedDate + "','" + t_diary + "');";
	                stmt.executeUpdate(insertQuery);
	            }
				
				System.out.println("데이터 삽입 완료");
	
	            checkResult.close();
	            stmt.close();
	            con.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
    }
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		
        if(s.equals("diaryMoveBtn1")) {
        	writeDiary.setText("");
        	memoM.mainCall();
        	dispose();
        } else if(s.equals("memoB")) {
        	writeDiary.setText("");
        	memoM.mainCall();
        	dispose();
        } else if(s.equals("saveBtn")) {
        	saveDiaryToDatabase();
			memoM.mainCall();
			dispose();
        } else if(s.equals("calendarB")) {
    		
    	} else if(s.equals("addB")) {
    			
		} else if(s.equals("graphB")) {
			
		} else if(s.equals("userB")) {
			
		}
		
	}


	public static void main(String[] args) {
		Dagym_memoDiary memoD = new Dagym_memoDiary();
		
		memoD.setTitle("dagym");
		memoD.setSize(540, 960);
		memoD.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		memoD.setVisible(true);

	}
}
