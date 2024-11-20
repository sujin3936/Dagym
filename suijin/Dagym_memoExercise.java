package suijin;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Dagym_memoExercise extends JFrame implements ActionListener {
	private Dagym_main dagymM = new Dagym_main();
	Dagym_memoMain memoM = new Dagym_memoMain();
	JTextField textArray[][] = new JTextField[1][3];
	JPanel top_e2;
	int panelCount = 1;
	private ArrayList<ArrayList<String>> dbTextArray = new ArrayList<>();
	Calendar calendar = Calendar.getInstance();
	JComboBox<Integer> yearComboBox = new JComboBox<>();
	JComboBox<String> monthComboBox = new JComboBox<>();
	JComboBox<Integer> dayComboBox = new JComboBox<>();
	
	Dagym_memoExercise() {
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
            
            
            ItemListener updateDays1 = new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        // 선택된 년도와 월을 가져오기
                        Integer selectedYear = (Integer) yearComboBox.getSelectedItem();
                        String selectedMonthStr = (String) monthComboBox.getSelectedItem();
                        Integer selectedMonth = selectedMonthStr != null ? Integer.parseInt(selectedMonthStr) : null;
                        Integer selectedDay = (Integer) dayComboBox.getSelectedItem();

                        // 해당 날짜의 운동 데이터를 데이터베이스에서 조회
                        loadExerciseFromDatabase();
                    }
                }
            };

            yearComboBox.addItemListener(updateDays1);
            monthComboBox.addItemListener(updateDays1);
            
            dayComboBox.addItemListener(e -> {
                // 일 선택 시 diary 데이터 불러오기
            	loadExerciseFromDatabase();
            });
		
		});
		
		
		//----------------------------------------------------------------
		
	
		//center 패널 안 배치할 패널들
		JPanel top_e1 = new JPanel();
		top_e2 = new JPanel();
		JPanel bottom_e1 = new JPanel();
		JPanel calendarPanel = new JPanel();
		// 오른쪽 공백을 위한 빈 패널 추가
		JPanel rightSpacePanel = new JPanel();
		rightSpacePanel.setOpaque(false); // 배경을 투명하게 설정
		
		
		//필요한 JFrame 선언
		JLabel exercise_journal = new JLabel("운동일지");
		JTextField exercise_name = createHintTextField("입력하세요", 25);
        JTextField exercise_kg = createHintTextField("무게", 13);
        JTextField exercise_num = createHintTextField("횟수", 5);
		textArray[0][0] = exercise_name;
		textArray[0][1] = exercise_kg;
		textArray[0][2] = exercise_num;
		
		
		JButton delete = new JButton("삭제");
		JButton addText = new JButton("추가");
		JButton save = new JButton("저장");
		JButton exeMoveBtn1 = new JButton("<");
		
		
		//배경색, 보더 설정
		center.setBorder(new LineBorder(Color.BLACK, 3, true));
		exercise_journal.setBorder(new LineBorder(Color.BLACK, 3, true));
		top_e2.setBorder(new LineBorder(Color.GRAY, 3, true));
		top_e1.setBackground(Color.WHITE);
		bottom_e1.setBackground(Color.WHITE);
		calendarPanel.setOpaque(true);
        calendarPanel.setBackground(Color.WHITE);
		
		
		//크기, 정렬 설정
		exercise_journal.setHorizontalAlignment(JLabel.CENTER);
		exercise_journal.setVerticalAlignment(JLabel.CENTER);
		rightSpacePanel.setPreferredSize(new Dimension(35, 70)); 
		delete.setPreferredSize(new Dimension(70, 40));
		addText.setPreferredSize(new Dimension(70, 40));
		save.setPreferredSize(new Dimension(70, 40));

		
		//배치
		center.setLayout(new BorderLayout());
		center.add(top_e1, BorderLayout.CENTER);
		center.add(bottom_e1, BorderLayout.SOUTH);
		
		top_e1.setLayout(null);
		exercise_journal.setBounds(85, 40, 390, 40);
		exeMoveBtn1.setBounds(35, 40, 50, 40);
		calendarPanel.setBounds(10, 95, 200, 30);
		top_e2.setBounds(35, 140, 440, 600);
		top_e1.add(exercise_journal);
		top_e1.add(exeMoveBtn1);
		top_e1.add(calendarPanel);
		top_e1.add(top_e2);
		top_e2.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel newLinePanel = new JPanel();
		for(int j=0; j<3; j++) {
			newLinePanel.add(textArray[0][j]);
		}
		top_e2.add(newLinePanel);
		
		
		bottom_e1.setLayout(new FlowLayout(FlowLayout.RIGHT));
		bottom_e1.setPreferredSize(new Dimension(540, 70));
		bottom_e1.add(delete);
		bottom_e1.add(addText);
		bottom_e1.add(save);
		bottom_e1.add(rightSpacePanel);
		
		calendarPanel.add(yearComboBox);
        calendarPanel.add(monthComboBox);
        calendarPanel.add(dayComboBox);
		
		
		//----------------------------------------------------------------
		//버튼 눌렀을 때 이동 이벤트
		exeMoveBtn1.setActionCommand("exeMoveBtn1");
		save.setActionCommand("save");
		addText.setActionCommand("addText");
		delete.setActionCommand("delete");
		exeMoveBtn1.addActionListener(this);
		save.addActionListener(this);
		addText.addActionListener(this);
		delete.addActionListener(this);
		
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
	
	//줄 추가 메소드 
	public void addTextClick(JPanel top_e2, JTextField[][] textArray) {
		JPanel newLinePanel1 = new JPanel();
		if(panelCount <= 16) {
			panelCount++;
			for (int j = 0; j < 3; j++) {
				if(j==0) {
					JTextField newText = createHintTextField("입력하세요", 25);
		            textArray[0][j] = newText;
		            newLinePanel1.add(newText);
				} else if(j==1) {
					JTextField newText = createHintTextField("무게", 13);
					textArray[0][j] = newText;
					newLinePanel1.add(newText);
				} else {
					JTextField newText = createHintTextField("횟수", 5);
					textArray[0][j] = newText;
					newLinePanel1.add(newText);
				}
	            
		    }
		    top_e2.add(newLinePanel1);
		    top_e2.revalidate();
		    top_e2.repaint();
		} else 
			JOptionPane.showMessageDialog(this, "더 이상 추가할 수 없습니다");
		
	}
	
	//줄 삭제 메소드
	public void deleteTextClick(JPanel top_e2, JTextField[][] textArray) {
		if(panelCount == 1) {
			JOptionPane.showMessageDialog(this, "더 이상 삭제할 수 없습니다");
		} else {
			Component lastComponent = top_e2.getComponent(panelCount - 1);
	        top_e2.remove(lastComponent);
	        top_e2.revalidate();
	        top_e2.repaint();
	        panelCount--;

            // 삭제 후 다시 그림
            top_e2.revalidate();
            top_e2.repaint();
		}	
	}
	
	//글자 힌트 메소드
	private JTextField createHintTextField(String hint, int columns) {
        JTextField textField = new JTextField(hint, columns);
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(hint)) {
                    textField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(hint);
                }
            }
        });
        return textField;
    }
	
	//해당 날짜 데이터 가져오는 메소드
	private void loadExerciseFromDatabase() {
	    // 현재 선택된 날짜 가져오기
	    Integer selectedYear = (Integer) yearComboBox.getSelectedItem();
	    String selectedMonthStr = (String) monthComboBox.getSelectedItem();
	    Integer selectedMonth = selectedMonthStr != null ? Integer.parseInt(selectedMonthStr) : null;
	    Integer selectedDay = (Integer) dayComboBox.getSelectedItem();

	    if (selectedYear != null && selectedMonth != null && selectedDay != null) {
	        String selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth, selectedDay);

	        try {
	            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dagym?serverTimezone=UTC", "root", "1234");
	            System.out.println("DB 연결 완료");

	            String selectQuery = "SELECT ExerCategory, EquipWeight, Frequency FROM exerjournal_tbl WHERE Calendar = ?";
	            try (PreparedStatement preparedStatement = con.prepareStatement(selectQuery)) {
	                preparedStatement.setString(1, selectedDate);
	                try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                    // 조회된 데이터를 리스트에 저장
	                    dbTextArray.clear();
	                    while (resultSet.next()) {
	                        ArrayList<String> rowData = new ArrayList<>();
	                        rowData.add(resultSet.getString("ExerCategory"));
	                        rowData.add(resultSet.getString("EquipWeight"));
	                        rowData.add(resultSet.getString("Frequency"));
	                        dbTextArray.add(rowData);
	                    }
	                }
	            }

	            // 조회된 데이터를 화면에 표시
	            displayExerciseData();

	            // 데이터가 없을 때 기본 입력 칸을 추가
	            if (dbTextArray.isEmpty()) {
	                addDefaultLinePanel();
	            }
	            
	            con.close();
	        } catch (SQLException e) {
	            System.out.println("SQLException:" + e.getMessage());
	        }
	    }
	}
	
	private void displayExerciseData() {
	    // 운동 데이터가 저장된 개수만큼 입력 칸 추가 및 데이터 표시
	    top_e2.removeAll();
	    for (ArrayList<String> rowData : dbTextArray) {
	        addLinePanel(rowData);
	    }

	    // 추가된 입력 칸을 화면에 표시
	    top_e2.revalidate();
	    top_e2.repaint();
	}

	// 데이터에 맞게 입력 칸을 추가하는 메소드
	private void addLinePanel(ArrayList<String> rowData) {
	    JPanel newLinePanel = new JPanel();
	    for (int j = 0; j < 3; j++) {
	        JTextField newText = createHintTextField(rowData.get(j), (j == 0) ? 25 : (j == 1) ? 13 : 5);
	        textArray[0][j] = newText;
	        newLinePanel.add(newText);
	    }
	    top_e2.add(newLinePanel);
	}

	// 데이터가 없을 때 기본 입력 칸을 추가하는 메소드
	private void addDefaultLinePanel() {
	    JPanel newLinePanel = new JPanel();
	    for (int j = 0; j < 3; j++) {
	        JTextField newText = createHintTextField("입력하세요", (j == 0) ? 25 : (j == 1) ? 13 : 5);
	        textArray[0][j] = newText;
	        newLinePanel.add(newText);
	    }
	    top_e2.add(newLinePanel);
	}
	
	
	//저장하는 데이터베이스
	public void saveExerciseToDatabase() {
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
			
			// 현재 날짜 가져오기
            Integer selectedYear = (Integer) yearComboBox.getSelectedItem();
            String selectedMonthStr = (String) monthComboBox.getSelectedItem();
            Integer selectedMonth = selectedMonthStr != null ? Integer.parseInt(selectedMonthStr) : null;
            Integer selectedDay = (Integer) dayComboBox.getSelectedItem();
            
            String selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth, selectedDay);

			boolean isEmpty = true;

			for (Component component : top_e2.getComponents()) {
                if (component instanceof JPanel) {
                    JPanel panel = (JPanel) component;
                    JTextField[] textFields = new JTextField[3];
                    int index = 0;

                    // 패널 내의 텍스트 필드를 배열에 저장
                    for (Component innerComponent : panel.getComponents()) {
                        if (innerComponent instanceof JTextField) {
                            textFields[index++] = (JTextField) innerComponent;
                        }
                    }

                    // 가져온 값으로 데이터베이스에 저장
                    String exerciseName = textFields[0].getText();
                    String weightStr = textFields[1].getText();
                    String numStr = textFields[2].getText();

                    // weightStr, numStr 등을 숫자로 변환
                    int t_weight = Integer.parseInt(weightStr);
                    int t_num = Integer.parseInt(numStr);

                    // 데이터베이스에 저장
                    String strSql = "INSERT INTO exerjournal_tbl (Calendar, ExerCategory, EquipWeight, Frequency) VALUES (?, ?, ?, ?)";
                    PreparedStatement pstmt = con.prepareStatement(strSql);
                    pstmt.setString(1, selectedDate);
                    pstmt.setString(2, exerciseName);
                    pstmt.setInt(3, t_weight);
                    pstmt.setInt(4, t_num);
                    pstmt.executeUpdate();
                    
                    System.out.println("데이터 삽입 완료");
                }
			}
			
			dbSt.close();
			con.close();
		} catch (SQLException e3) {
			System.out.println("SQLException:"+e3.getMessage());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		
		if(s.equals("memoB")) {
			memoM.mainCall();
			dispose();
		} else if(s.equals("exeMoveBtn1")) {
			memoM.mainCall();
			dispose();
		} else if(s.equals("save")) {
			saveExerciseToDatabase();
			memoM.mainCall();
			dispose();
		} else if(s.equals("addText")) {
			addTextClick(top_e2, textArray);
		} else if(s.equals("delete")) {
			deleteTextClick(top_e2, textArray);
		} else if(s.equals("calendarB")) {
		
		} else if(s.equals("addB")) {
			
		} else if(s.equals("graphB")) {
			
		} else if(s.equals("userB")) {
			
		}
		
	}

	public static void main(String[] args) {
		Dagym_memoExercise memoE = new Dagym_memoExercise();
		
		memoE.setTitle("dagym");
		memoE.setSize(540, 960);
		memoE.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		memoE.setVisible(true);

	}
}
