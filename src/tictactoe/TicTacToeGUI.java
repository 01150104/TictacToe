package tictactoe;

import java.util.List;
import java.util.ArrayList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.*;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;



public class TictacToeGUI extends JFrame {
	
	// 파일 생성 코드 추가
	File file = new File("ranking.txt");
	if (!file.exists()) {
	    try {
	        file.createNewFile();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	// GUI 실행 코드 수정
	SwingUtilities.invokeLater(() -> new TictacToeGUI(p1, p2));

	// 입력값 검증
	if (p1.trim().isEmpty() || p2.trim().isEmpty()) {
	    System.out.println("플레이어 이름은 비워둘 수 없습니다.");
	    return;
	}


	JLabel[][] BoardNow = new JLabel[3][3];
    int[][] game = new int[3][3];
    int turn = 1;
    String p1;
    String p2;
    JLabel p1Move;
    JLabel p2Move;
    JLabel currentPlayerLabel;
    List<int[]> Move = new ArrayList<>();
    final String RANKING_FILE = "ranking.txt";

    TictacToeGUI(String p1, String p2) {
        this.p1 = p1;
        this.p2 = p2;
        setTitle("Tic Tac Toe");
        setSize(600, 400);
        setLocation(100, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

     

        JPanel gamePanel = new JPanel(new GridLayout(3, 3, 5, 5));
        gamePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        gamePanel.setBackground(Color.WHITE);
      
        // 틱택토 판 디자인
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                BoardNow[i][j] = new JLabel("", SwingConstants.CENTER);
                BoardNow[i][j].setFont(new Font("Arial", Font.BOLD, 40));
                BoardNow[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                gamePanel.add(BoardNow[i][j]);
     
                int row = i;
                int col = j;

                // 클릭
                BoardNow[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (game[row][col] == 0) {
                            if (turn == 1) {
                                game[row][col] = 1;
                                BoardNow[row][col].setText("O");
                       
                                turn = 2;
                                currentPlayerLabel.setText(p2 + "'s turn.");
                            } else {
                                game[row][col] = 2;
                                BoardNow[row][col].setText("X");
                          
                                turn = 1;
                                currentPlayerLabel.setText(p1 + "'s turn.");
                            }
                            Move.add(new int[]{row, col});

                            if (Move.size() > 7) {
                                int[] oldMove = Move.remove(0);
                                game[oldMove[0]][oldMove[1]] = 0;
                                BoardNow[oldMove[0]][oldMove[1]].setText("");
                            }
                            checkGameEnd();
                        }
                    }
                });
            }
        }
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        //게임의 버튼 추가

        // 수 움직임
        p1Move = new JLabel(p1);
        p2Move = new JLabel(p2);
        JLabel vs = new JLabel ("vs");
        p1Move.setFont(new Font("Arial", Font.PLAIN, 16));
        p2Move.setFont(new Font("Arial", Font.PLAIN, 16));
        p1Move.setAlignmentX(Component.CENTER_ALIGNMENT);
        p2Move.setAlignmentX(Component.CENTER_ALIGNMENT);

        //누구 차례인지
        currentPlayerLabel = new JLabel(p1 + "'s turn.");
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        currentPlayerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 버튼
        JButton resetButton = new JButton("다시하기");
        JButton exitButton = new JButton("    종료    ");
        JButton rankingButton = new JButton("랭킹 보기");
        JButton howtoplay = new JButton ("게임 방법");
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rankingButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        howtoplay.setAlignmentX(Component.CENTER_ALIGNMENT);
       
        JFrame howtoframe = new JFrame("게임 방법");
        
      
        howtoframe.setVisible(false);
        howtoframe.setSize(500, 500);
        howtoframe.setLocationRelativeTo(null);
        JLabel label = new JLabel("<html>1. 플레이어 1은 O를 가지고 플레이어 2는 X를 가집니다.<br>2. 가로, 세로, 대각선 방향으로 같은 모양 3개를 연결해야 승리할 수 있습니다.<br>3. 7번째 수를 두는 순간 가장 처음에 뒀던 수가 사라집니다. <br>4. 파일의 ranking.txt 파일은 건드리지 말아 주세요. 컴퓨터의 랭킹 데이터가 저장됩니다. <br>5. 클릭이 잘 안돼면 더블클릭을 해보세요. <br> 6. 플레이는 항상 p1(O)부터 시작합니다.</html>");
        label.setHorizontalAlignment(SwingConstants.CENTER); // 텍스트 가운데 정렬
        howtoframe.getContentPane().add(label); 
      
        //마우스 클릭 시 실행하는 것
        resetButton.addActionListener(e -> ResetGame());
        exitButton.addActionListener(e -> System.exit(0));
        rankingButton.addActionListener(e -> showRankingBetween(p1, p2));
        howtoplay.addActionListener(e ->  howtoframe.setVisible(true));
        
        infoPanel.add(p1Move);
        infoPanel.add(vs);
        infoPanel.add(p2Move);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20))); 
        infoPanel.add(currentPlayerLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20))); 
        infoPanel.add(resetButton);
        infoPanel.add(rankingButton);
        infoPanel.add(howtoplay);
        infoPanel.add(exitButton);
        //버튼 생성한거

       
        add(gamePanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.EAST);

        //디자인 끝
        setVisible(true);
    }


	void ResetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                game[i][j] = 0;
                BoardNow[i][j].setText("");
            }
        }

        Move.clear();
        turn = 1;
        currentPlayerLabel.setText(p1 + "'s turn.");

        System.out.println("게임이 리셋되었습니다! player1 부터 다시 시작합니다.");
    }



    void checkGameEnd() {
        int winner = End(game);
        if (winner != 0) {
            if (winner == 1) {
                JOptionPane.showMessageDialog(this, p1 + " 승리!");
                saveRanking(p1);
            } else {
                JOptionPane.showMessageDialog(this, p2 + " 승리!");
                saveRanking(p2);
            }
            ResetGame();
        }
    }

    //승리 조건 체크
    //(가로,세로,대각선)
    static int End(int[][] game) {
        for (int i = 0; i < 3; i++) {
            if (game[i][0] == game[i][1] && game[i][1] == game[i][2] && game[i][0] != 0) {
                return game[i][0];
            }
            if (game[0][i] == game[1][i] && game[1][i] == game[2][i] && game[0][i] != 0) {
                return game[0][i];
            }
        }
        if (game[0][0] == game[1][1] && game[1][1] == game[2][2] && game[0][0] != 0) {
            return game[0][0];
        }
        if (game[0][2] == game[1][1] && game[1][1] == game[2][0] && game[0][2] != 0) {
            return game[0][2];
        }
        return 0;
    }
 

    void saveRanking(String winner) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(RANKING_FILE, true), "UTF-8"))) {
            writer.write(winner + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void showRankingBetween(String player1, String player2) {
        // 랭킹 데이터를 읽고 player1과 player2 간의 승리 기록을 비교하여 보여주는 메서드
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(RANKING_FILE), "UTF-8"))) {
            // RANKING_FILE을 UTF-8로 읽기 위해 BufferedReader 생성 (try-with-resources로 자동 닫힘)
            StringBuilder ranking = new StringBuilder(); // 최종 랭킹 결과를 저장할 StringBuilder
            String line; // 파일에서 읽은 각 줄을 저장할 변수
            Map<String, Integer> scores = new HashMap<>(); // 플레이어 이름과 승리 횟수를 저장할 Map

            while ((line = reader.readLine()) != null) { // 파일을 한 줄씩 끝까지 읽기
                if (line.equals(player1) || line.equals(player2)) { 
                    // 읽은 줄이 player1 또는 player2라면
                    scores.put(line, scores.getOrDefault(line, 0) + 1); 
                    // 해당 플레이어 이름의 승리 횟수를 1 증가 (없으면 기본값 0)
                }
            }

            if (scores.isEmpty()) { 
                // 두 플레이어의 기록이 없으면
                JOptionPane.showMessageDialog(this, "두 플레이어 간 기록이 없습니다.", "랭킹", JOptionPane.INFORMATION_MESSAGE);
                // 기록 없음 팝업 메시지 띄움
                return; // 메서드 종료
            }

            scores.entrySet().stream() 
                // Map의 엔트리(플레이어 이름과 승리 횟수)를 스트림으로 처리
                .sorted((a, b) -> b.getValue() - a.getValue()) 
                // 승리 횟수 기준으로 내림차순 정렬
                .forEach(entry -> ranking.append(entry.getKey()) // 각 플레이어 이름과
                                        .append(" wins: ").append(entry.getValue()) // 승리 횟수를 문자열로 추가
                                        .append("\n"));

            JOptionPane.showMessageDialog(this, ranking.toString(), "랭킹 (" + player1 + " vs " + player2 + ")", JOptionPane.INFORMATION_MESSAGE);
            // 최종 랭킹 결과를 팝업 메시지로 표시 (제목에 player1과 player2 이름 포함)
        } catch (IOException e) {
            // 랭킹 파일을 읽다가 오류가 발생하면
            JOptionPane.showMessageDialog(this, "랭킹 데이터를 읽을 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            // 오류 메시지를 팝업으로 표시
        }
    }


  
    public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

        System.out.print("플레이어 1의 이름을 영어로 입력하세요: ");
        String p1 = sc.nextLine();

        System.out.print("플레이어 2의 이름을 영어로 입력하세요: ");
        String p2 = sc.nextLine();

        new TictacToeGUI(p1, p2);
    }
}
