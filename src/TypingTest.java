import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TypingTest {
    private static List<String> WORDS = new ArrayList<>();
    private static String randomWord;
    private static int score = 0;
    private static JProgressBar progressBar;

    public static void main(String[] args) {

        try {
            File file = new File("src/kelimeler.txt");
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()){
                WORDS.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Katibim");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        ImageIcon img = new ImageIcon("src/pencil.png"); // İkon dosyasının yolunu belirtin
        frame.setIconImage(img.getImage());
        frame.setLayout(new GridLayout(6, 1));

        randomWord = WORDS.get((int) (Math.random() * WORDS.size()));

        JLabel wordLabel = new JLabel(randomWord, SwingConstants.CENTER);
        wordLabel.setFont(new Font("Serif", Font.PLAIN, 24)); // font boyutunu artır

        JTextField userInput = new JTextField();
        JButton startButton = new JButton("Testi Başlat");

        JLabel scoreLabel = new JLabel("Doğru Kelime: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Serif", Font.PLAIN, 24)); // font boyutunu artır

        progressBar = new JProgressBar();
        progressBar.setMaximum(60);

        JLabel authorLabel = new JLabel("Ahmet Serdıl HATİPOĞLU tarafından yazılmıştır.", SwingConstants.RIGHT);
        authorLabel.setFont(new Font("Serif", Font.PLAIN, 12));

        userInput.setEnabled(false);

        userInput.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String inputText = userInput.getText().trim();
                if (randomWord.startsWith(inputText)) {
                    wordLabel.setForeground(Color.BLACK); // Doğru yazıldıysa kelimeyi yeşil yap
                } else {
                    wordLabel.setForeground(Color.RED); // Yanlış yazıldıysa kelimeyi kırmızı yap
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (inputText.equals(randomWord)) {
                        score++;
                        scoreLabel.setText("Doğru Kelime: " + score);
                        randomWord = WORDS.get((int) (Math.random() * WORDS.size()));
                        wordLabel.setText(randomWord);
                        userInput.setText("");
                        wordLabel.setForeground(Color.BLACK); // Yeni kelime için rengi siyaha çevir
                    } else if (!inputText.equals("")) { // Eğer inputText boş değilse ve randomWord'e eşit değilse yanlıştır.
                        e.consume(); // Space tuş basımını iptal et
                    }
                }
            }
        });

        int duration = 60; // Süre (saniye)

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startButton.setEnabled(false);
                userInput.setEnabled(true);
                progressBar.setValue(0);
                score = 0;
                scoreLabel.setText("Doğru Kelime: " + score);
                Timer timer = new Timer(1000, new ActionListener() {
                    int timeLeft = duration;

                    public void actionPerformed(ActionEvent e) {
                        timeLeft--;
                        progressBar.setValue(duration - timeLeft);
                        if (timeLeft <= 0) {
                            ((Timer)e.getSource()).stop();
                            userInput.setEnabled(false);
                            startButton.setEnabled(true);
                            JOptionPane.showMessageDialog(frame, "Test Bitti! Doğru yazılan kelime sayısı: " + score);

                            try {
                                File soundFile = new File("src/alkis.wav"); // Alkış ses dosyasının konumunu belirtin.
                                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
                                Clip clip = AudioSystem.getClip();
                                clip.open(audioInputStream);
                                clip.start();
                            } catch (Exception ex) {
                                System.err.println(ex.getMessage());
                            }
                        }
                    }
                });
                timer.start();
            }
        });

        frame.add(wordLabel);
        frame.add(userInput);
        frame.add(startButton);
        frame.add(scoreLabel);
        frame.add(progressBar);
        frame.add(authorLabel);
        frame.setVisible(true);
    }
}
