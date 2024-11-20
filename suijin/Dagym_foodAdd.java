package suijin;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

import chuntaek.Add_food;



public class Dagym_foodAdd extends JFrame implements ActionListener {
		private Dagym_main dagymM = new Dagym_main();
		private boolean isPlus0 = true, isPlus1 = true, isPlus2 = true, isPlus3 = true, isPlus4 = true;
		JButton foodAdd_final;
		JButton foodAdd[] = new JButton[5];
		JTextField foodSerch;
		String foodName2;
		String foodInfo[] = new String[5];
		JPanel center_c1, center_c2, center_c3, center_c4, center_c5;
		JLabel foodName;
		
	
		public Dagym_foodAdd() {
		//메인디자인
		//하단 버튼에 이미지 삽입
		JButton calendarB = dagymM.createButton("images/001.png");
        JButton memoB = dagymM.createButton("images/002.png");
        JButton addB = dagymM.createButton("images/003.png");
        JButton graphB = dagymM.createButton("images/004.png");
        JButton userB = dagymM.createButton("images/005.png");
        
		
        //패널 생성
        JPanel center = new JPanel(new FlowLayout());
		JPanel bottom = new JPanel(new GridLayout());
	
		
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
		
		Container ct = getContentPane();
		ct.setLayout(new BorderLayout());
		ct.add(center, BorderLayout.CENTER);
		ct.add(bottom, BorderLayout.SOUTH);
		
		
		//----------------------------------------------------------------
		
		
		//center 패널 안 배치할 패널들
		JPanel top_add = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel center_add = new JPanel(new BorderLayout());
		JPanel bottom_add = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel center_top = new JPanel(new FlowLayout(FlowLayout.LEFT, 17, 50));
		JPanel center_c = new JPanel(new GridLayout(5,1));

		
		//검색 자료 넣을 패널들
		center_c1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
		center_c2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
		center_c3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
		center_c4 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
		center_c5 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
		
		
		//필요한 JFrame 선언
		JLabel foodTitle = new JLabel("음식 추가");
		foodSerch = new JTextField("음식 이름");
		JButton serchB = new JButton("검색");
		JButton deleteB = new JButton("취소");
		
		for(int i=0; i<5; i++) {
			foodAdd[i] = dagymM.createButton("images/plus.jpg");
			foodAdd[i].setPreferredSize(new Dimension(50, 50));
			foodAdd[i].setActionCommand("plus"+i);
			foodAdd[i].addActionListener(this);
		}
		
		
		foodAdd_final = new JButton("음식 추가하기");
		foodAdd_final.setEnabled(false);

	
		//크기 지정
		foodAdd_final.setPreferredSize(new Dimension(500, 50));
		foodSerch.setPreferredSize(new Dimension(340, 30));
		
		
		
		//바탕색 설정
        backgroundSet(top_add);
        backgroundSet(center_top);
        backgroundSet(bottom_add);
        backgroundSet(center_c1);
        backgroundSet(center_c2);
        backgroundSet(center_c3);
        backgroundSet(center_c4);
        backgroundSet(center_c5);
        
		
		//배치
		center.setLayout(new BorderLayout());
		center.add(top_add, BorderLayout.NORTH);
		center.add(center_add, BorderLayout.CENTER);
		center.add(bottom_add, BorderLayout.SOUTH);
		center_add.add(center_top, BorderLayout.NORTH);
		center_add.add(center_c, BorderLayout.CENTER);
		
		top_add.add(foodTitle);
		center_top.add(foodSerch);
		center_top.add(serchB);
		center_top.add(deleteB);
		bottom_add.add(foodAdd_final);
		

		
		center_c1.setVisible(false);
		center_c2.setVisible(false);
		center_c3.setVisible(false);
		center_c4.setVisible(false);
		center_c5.setVisible(false);
		center_c.add(center_c1);
		center_c.add(center_c2);
		center_c.add(center_c3);
		center_c.add(center_c4);
		center_c.add(center_c5);
		
		
		//-------------------------------------------------------------
        //버튼 눌렀을 때 이동 이벤트
		memoB.setActionCommand("memoB");
		serchB.setActionCommand("검색");
		deleteB.setActionCommand("취소");
		foodAdd_final.setActionCommand("음식 추가하기");
		memoB.addActionListener(this);
		serchB.addActionListener(this);
		deleteB.addActionListener(this);
		foodAdd_final.addActionListener(this);
	}
		
	//바탕색 설정 메소드
	public void backgroundSet(JPanel p) {
		p.setOpaque(true);
		p.setBackground(Color.WHITE);
	}
		
	//해당 컬럼 값 가져오는 메소드
	private List<String> fetchDataFromDatabase(String foodName) {
	    List<String> fetchedDataList = new ArrayList<>();
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

				String selectQuery = "SELECT * FROM food_tbl WHERE FoodName LIKE ?";

		        try (PreparedStatement preparedStatement = con.prepareStatement(selectQuery)) {
		            preparedStatement.setString(1, "%" + foodName + "%");
		            try (ResultSet resultSet = preparedStatement.executeQuery()) {
		                while (resultSet.next()) {
		                    String foodNameResult = resultSet.getString("FoodName");
		                    fetchedDataList.add(foodNameResult);
		                }
		            }
		        }
		
				System.out.println("데이터 검색 완료");
				
				dbSt.close();
		        con.close();
				
			} catch (SQLException e3) {
				JOptionPane.showMessageDialog(this, "해당 음식이 존재하지 않습니다");
			}
	        return fetchedDataList;
	    }
	
	//식단 테이블에 값 넘기는 데이터베이스
	public void DietData(String foodName) {
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

			String foodIdSql = "SELECT * FROM food_tbl WHERE FoodName = '"+foodName+"';";
			ResultSet rs = dbSt.executeQuery(foodIdSql);
			
			if (rs.next()) {
	            int foodId = rs.getInt("FoodID");
	            int kcal = rs.getInt("Calorie");
	            int carbohydrate = rs.getInt("Carbon");
	            int protein = rs.getInt("Protein");
	            int fat = rs.getInt("Fat");

	            // 데이터 삽입
	            String insertQuery = "INSERT INTO diet_tbl(FoodID, FoodName, Calorie, Protein, Fat) VALUES ('" + foodId + "','" + foodName + "','" + kcal + "','" + protein + "','" + fat + "')";
	            dbSt.executeUpdate(insertQuery);

			} else {
	            System.out.println("음식 데이터가 존재하지 않습니다.");
	        }

	        dbSt.close();
	        con.close();
			
		} catch (SQLException e3) {
			System.out.println("SQLException:"+e3.getMessage());
			e3.printStackTrace();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		
		String t_foodNameSerch ="";
		
		if(s.equals("memoB")) {
			Dagym_memoMain memoMain = new Dagym_memoMain();
			memoMain.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			memoMain.setSize(540,960);
			memoMain.show();
			dispose();
		} else if(s.equals("검색")) {
			List<String> fetchedDataList = fetchDataFromDatabase(foodSerch.getText());

		    // 각 center_c 패널에 레이블 추가
		    for (int i = 0; i < fetchedDataList.size(); i++) {
		        JLabel foodNameLabel = new JLabel(fetchedDataList.get(i));
		        foodNameLabel.setPreferredSize(new Dimension(420, 100));
		        

		        switch (i) {
		            case 0:
		            	foodInfo[i] = fetchedDataList.get(i);
		            	center_c1.removeAll();
		                center_c1.add(foodNameLabel);
		        		center_c1.add(foodAdd[0]);
		                center_c1.setVisible(true);
		                break;
		            case 1:
		            	foodInfo[i] = fetchedDataList.get(i);
		            	center_c2.removeAll();
		                center_c2.add(foodNameLabel);
		                center_c2.add(foodAdd[1]);
		                center_c2.setVisible(true);
		                break;
		            case 2:
		            	foodInfo[i] = fetchedDataList.get(i);
		            	center_c3.removeAll();
		                center_c3.add(foodNameLabel);
		        		center_c3.add(foodAdd[2]);
		                center_c3.setVisible(true);
		                break;
		            case 3:
		            	foodInfo[i] = fetchedDataList.get(i);
		            	center_c4.removeAll();
		                center_c4.add(foodNameLabel);
		        		center_c4.add(foodAdd[3]);
		                center_c4.setVisible(true);
		                break;
		            case 4:
		            	foodInfo[i] = fetchedDataList.get(i);
		            	center_c5.removeAll();
		                center_c5.add(foodNameLabel);
		        		center_c5.add(foodAdd[4]);
		        		center_c5.setVisible(true);
		                break;
		            //필요한 만큼 계속 추가 가능
		        }
		    }
		
		} else if(s.equals("취소")) {
			foodSerch.setText("");
		} else if(s.equals("plus0")) {
			if (isPlus0) {
	            foodAdd[0].setIcon(new ImageIcon("images/check.png"));
	            foodAdd_final.setEnabled(true);
	        } else {
	        	foodAdd[0].setIcon(new ImageIcon("images/plus.png"));
	        	if(isPlus1 && isPlus2 && isPlus3 && isPlus4) {
	        		foodAdd_final.setEnabled(false);
	        	}
	        	
	        }
			isPlus0 = !isPlus0;
		} else if(s.equals("plus1")) {
			if (isPlus1) {
	            foodAdd[1].setIcon(new ImageIcon("images/check.png"));
	            foodAdd_final.setEnabled(true);
	        } else {
	        	foodAdd[1].setIcon(new ImageIcon("images/plus.png"));
	        	if(isPlus0 && isPlus2 && isPlus3 && isPlus4) {
	        		foodAdd_final.setEnabled(false);
	        	}
	        }
			isPlus1 = !isPlus1;
		} else if(s.equals("plus2")) {
			if (isPlus2) {
	            foodAdd[2].setIcon(new ImageIcon("images/check.png"));
	            foodAdd_final.setEnabled(true);
	        } else {
	        	foodAdd[2].setIcon(new ImageIcon("images/plus.png"));
	        	if(isPlus0 && isPlus1 && isPlus3 && isPlus4) {
	        		foodAdd_final.setEnabled(false);
	        	}
	        }
			isPlus2 = !isPlus2;
		} else if(s.equals("plus3")) {
			if (isPlus3) {
	            foodAdd[3].setIcon(new ImageIcon("images/check.png"));
	            foodAdd_final.setEnabled(true);
	        } else {
	        	foodAdd[3].setIcon(new ImageIcon("images/plus.png"));
	        	if(isPlus1 && isPlus2 && isPlus0 && isPlus4) {
	        		foodAdd_final.setEnabled(false);
	        	}
	        }
			isPlus3 = !isPlus3;
		} else if(s.equals("plus4")) {
			if (isPlus4) {
	            foodAdd[4].setIcon(new ImageIcon("images/check.png"));
	            foodAdd_final.setEnabled(true);
	        } else {
	        	foodAdd[4].setIcon(new ImageIcon("images/plus.png"));
	        	if(isPlus1 && isPlus2 && isPlus3 && isPlus0) {
	        		foodAdd_final.setEnabled(false);
	        	}
	        }
			isPlus4 = !isPlus4;
		} else if(s.equals("음식 추가하기")) {
			String imgName = "images/check.png";
			
			for(int i=0; i<foodAdd.length; i++) {
				Icon icon = foodAdd[i].getIcon();
				ImageIcon imageIcon = (ImageIcon) icon;
				String imageName = imageIcon.getDescription(); 
				if(imageName.equals("images/check.png")) {
					//해당 버튼의 해당 패널 안 가지고 있는 데이터를 가져온다
					String dataMove = foodInfo[i];
					DietData(dataMove);
				}
			}
			
			Add_food addFoodClass = new Add_food();
			addFoodClass.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			addFoodClass.setSize(540,960);
			addFoodClass.show();
			dispose();
		}
	}
	
	public static void main(String[] args) {
		Dagym_foodAdd memoF = new Dagym_foodAdd();
		
		memoF.setTitle("dagym");
		memoF.setSize(540, 960);
		memoF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		memoF.setVisible(true);

	}

}
