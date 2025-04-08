import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class MatchCards {
    class Card{
        String cardName;
        ImageIcon cardImageIcon;
        
        Card(String cardName, ImageIcon cardImageIcon) {
            this.cardName = cardName;
            this.cardImageIcon = cardImageIcon;

        }

        public String toString(){
            return cardName;
        }
    }

    String[] cardList = {   //track cardNames
        "country cousin",
        "donna duck",
        "mickey mouse",
        "mickeys amateur",
        "moth and flame",
        "postman",
        "snow white",
        "the giant",
        "three little pigs",
        "witch"
    };

    int rows =4;
    int columns = 5;
    int cardWidth = 90;
    int cardHeight = 128;

    ArrayList<Card> cardSet; // create a deck of cards with cardNames and cardImageIcons
    ImageIcon cardBackImageIcon;

    int boardWidth = columns*cardWidth; //5*128= 640px
    int boardHeight = rows* caardHeight; // 4*90 =360

    JFrame frame = new JFrame("Disney Match Cards");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel restartGamePanel = new JPanel();
    JButton restartButton = new JButton();

    int errorCount = 0;
    ArrayList<Button> board;
    Timer hideCardTimer;
    boolean gameReady= false;
    JButton card1Selected;
    JButton card2Selected;

    MatchCards(){
        setupCards();
        shuffleCards();

        frame.setVisible(true);
        frame.setLayout(new BorderLayout());
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        textLabel.setFront(new Font("Arial" , Font.PLAIN, 20));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Errors: " + Integer.toString(errorCount));
        
        textPanel.setPreferredSize(new Dimension(boardWidth, 30));
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        //card game board
        board = new ArrayList<Button>();
        boardPanel.setLayout(new GridBagLayout(rows, columns));
        for (int i =0; i< cardSet.size(); i++) {
            JButton tile = new JButton();
            tile.setPreferredSize(new Dimension(cardWidth, cardHeight));
            tile.setOpaque(true);
            tile.setIcon(cardSet.get(i).cardImageIcon);
            tile.setFocusable(false);
            tile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!gameReady) {
                        return;
                    }
                    JButton tile = (JButton) e.getSource();
                    if (tile.getIcon() == cardBackImageIcon) {
                        if (card1Selected == null) {
                            cardSelected= tile;
                            int index = board.indexOf(card1Selected);
                            card1Selected.setIcon(cardSet.get(index).cardImageIcon);
                        }
                        else if (car2Selected == null) {
                            card2Selected = tile;
                            int index = boaard.indexOf(card2Selected);
                            car2Selected.setIcon(cardSet.get(index).cardImageIcon);

                            if(card1Selected.getIcon() !=card2Selected.getIcon()) {
                                errorCount +=1;
                                textLabel.setText("Errors: " + Integer.toString(errorCount));
                                hideCardTimer.start();
                            }
                            else {
                                cardSelected = null;
                                card2Selected = null;
                            }
                        }
                    }
                }
            });
            board.add(tile);
            boardPanel.add(tile);
        }

        frame.add(boardPanel);

         // restart game button
        restartButton.setFont(new Font("Arial", Font.PLAIN, 16));
        restartButton.setText("Restart Game");
        restartButton.setPreferredSize(new Dimension(boardWidth, 30));
        restartButton.setFocusable(false);
        restartButton.setEnabled(false);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameReady) {
                    return;
                }

                gameReady= false;
                restartButton.setEnabled(false);
                card1Selected = null;
                card2Selected = null;
                shuffleCards();

                //re assign buttons with new cards
                for(int i = 0; i< board.size(); i++) {
                    board.get(i).setIcon(cardSet.get(i).cardImageIcon);
                }

                errorCount = 0;
                textLabel.setText("Errors: " + Integer.toString(errorCount));
                hideCardTimer.start();
            }
        });
        restartGamePanel.add(restartButton);
        frame.add(restartGamePanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);

        //start game
        hideCardTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(Action e) {
                hideCards();
            }
        });
        hideCardTimer.setRepeats(false);
        hideCardTimer.start();

    }

    void setupCards(){
        cardSet = new ArrayList<Card>();
        for  (String cardName : cardList)
        {
            // load each card img
            Image cardImg = new ImageIcon(getClass().getResource("./img/" + cardName + ".jpg")). getImage();
            ImageIcon carImageIcon = new ImageIcon(cardImg. getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH ));

            // create card object and add to cardSet
            Card card = new Card(cardName, carImageIcon);
            cardSet.add(card);
        }
        cardSet.addAll(cardSet); // listedeki kartlari double yapiyo 20 kart olcak 

        //load the back card img
        Image cardBackImg = new ImageIcon(getClass().getResource("./img/back.jpg")).getImage();
        cardBackImageIcon = new ImageIcon(cardBackImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
    }

    void shuffleCards(){
        System.out.println(cardSet);
        //shuffle
        for (int i=0; i<cardSet.size(); i++) {
            int j=(int) (Math.random() * cardSet.size()); //get random index
            //swap
            Card temp = cardSet.get(i);
            cardSet.set(i, cardSet.get(j));
            cardSet.set(j, temp);
        }
        System.out.println(cardSet);
    }

    void hideCards() {
        if (gameReady && card1Selected != null && car2Selected != null) {
            card1Selected.setIcon(cardBackImageIcon);
            card1Selected = null;
            car2Selected.setIcon(cardBackImageIcon);
            car2Selected = null;
        }
        else {
            for (int i=0; i< board.size(); i++) {
                board.get(i).setIcon(cardBackImageIcon);
            }
            gameReady= true;
            restartButton.setEnabled(true);
        }
    }
}