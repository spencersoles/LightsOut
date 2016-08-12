package sample;

import javax.swing.*;    // needed for Swing classes

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*; // needed for ActionListener Interface
import java.util.Random;

/**
 * LightWindow.java Builds GUI
 * Lights.java controls game logic
 */

public class LightWindow extends JFrame
{
    private JPanel panel;//container for grid of light buttons

    private JPanel bottom;//holds buttons on South side of layout

    private JButton allLights[][]=new JButton[5][5]; //Generates place holders for each light (5x5) grid

    private JButton startOver;//Starts new game, randomizes board

    private JLabel win;//label to show status of game

    private final int WINDOW_WIDTH = 500;  // Window width
    private final int WINDOW_HEIGHT = 500; // Window height

    Container contentPane;//Container for entire game layout

    FlowLayout bottomButton = new FlowLayout();// sub layout for bottom panel (startover button and status label)

    BorderLayout mainLayout = new BorderLayout();//main layout


    /**
     * Constructor
     * Creates GUI game board
     */

    public LightWindow()
    {

        contentPane = getContentPane();

        contentPane.setLayout(mainLayout);
        // Window title
        setTitle("Lights Out");

        // initialize size of window
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buildPanel();

        // Add the panel to the frame's content pane.
        add(panel, BorderLayout.CENTER);

        add(bottom, BorderLayout.SOUTH);//button and status label



        // displays window
        setVisible(true);
    }

    /**
     The buildPanel method adds a label, text field, and
     and a button to a panel.
     */

    private void buildPanel()
    {
        // gridlayout to display the buttons in a 5x5 grid
        panel = new JPanel(new GridLayout(0,5));

        bottom = new JPanel(bottomButton);


        for(int w=0; w<allLights.length; w++){
            for(int y=0; y<allLights.length; y++){
                allLights[w][y] = new JButton(" ");//initialize each button
                panel.add(allLights[w][y]);//add each button to the panel to be displayed


                allLights[w][y].putClientProperty("on", false);



                allLights[w][y].setVisible(true);
                allLights[w][y].putClientProperty("row", w);//assign the row number and column number for game logic
                allLights[w][y].putClientProperty("column", y);
                allLights[w][y].addActionListener(new Lights());//action listener for each light

                if(((boolean) allLights[w][y].getClientProperty("on")) == false){
                    allLights[w][y].setBackground(Color.black);
                }else{
                    allLights[w][y].setBackground(Color.gray);
                }


            }
        }

        randomizeSolvableBoard();

        win = new JLabel(" Lights Out ");//label that changes based on status of game

        startOver = new JButton("Generate New Game");//label that generates a new game board

        startOver.addActionListener(new Lights());//action listener for new game generator which randomizes board

        bottom.add(startOver);//adding button to bottom of frame


        bottom.add(win);

    }

    /**
     * randomizes game board so that it is solvable
     * calls press button on random positions of the board to ensure there is at least one possible solution
     */

    private void randomizeSolvableBoard(){

        for(int i=0; i<100; i++){
            int randomRow= (int)(Math.random()*5);
            int randomCol= (int)(Math.random()*5);

            pressButton((boolean) allLights[randomRow][randomCol].getClientProperty("on"), randomRow, randomCol,allLights);
        }

    }

    /**
     lights handles all game logic, interfaced with actionlistener so that its action methods ae invoked correctly
     */

    private class Lights implements ActionListener
    {
        /**
         the actionPerformed method registers what object was clicked by getting the property
         and then determines the next action
         */

        public void actionPerformed(ActionEvent e)
        {

            JButton btn = (JButton) e.getSource();
        /**
         * if generate new game board clicked, then board is wiped clean and randomizeSolvableBoard() is called
         * to guarantee a solvable game
         */
            if(e.getSource()==startOver){

                for(int w=0; w<allLights.length; w++){
                    for(int y=0; y<allLights.length; y++){

                        allLights[w][y].putClientProperty("on", false);

                        if(((boolean) allLights[w][y].getClientProperty("on")) == false){
                            allLights[w][y].setBackground(Color.black);
                        }else{
                            allLights[w][y].setBackground(Color.gray);
                        }

                    }
                }
                randomizeSolvableBoard();

            }else{

                if(gameOver(allLights)== true){//determines if all lights are out, if they are all "out" (black), then game is over
                    win.setText("You win");
                }else{
                    pressButton((boolean) btn.getClientProperty("on"), (int) btn.getClientProperty("row"), (int) btn.getClientProperty("column"), allLights);
                }
            }
        }
    }
    /**
     * Evaluates the buttons to see if someone has won
     * @param allLights, it receives the buttons and evauluates the on property to see if it is on or off
     *  if all buttons are off then gameover is true
     * @return
     */
    static boolean gameOver(JButton [][] allLights){
        boolean gameComplete=false;
        int lightOn=0;

        for(int x=0; x<allLights.length; x++){
            for(int i=0; i<allLights[x].length; i++){
                if((boolean)allLights[x][i].getClientProperty("on")==true){
                    lightOn=lightOn+1;
                }
            }
        }
        if(lightOn==0){
            gameComplete=true;
        }

        return gameComplete;
    }
    /**
     *Game logic, takes the light selected along with the left, right, top, and bottom neighbors
     * calls toggle button method from GridButton to switch light status
     * @param truth, a boolean value of whether the light is on or off
     * @param row, row in which the light is located
     * @param col, column in which the light is located
     * @param btn, the specific light that is pressed
     */

    static void pressButton(boolean truth, int row, int col, JButton [][] btn){
        int rowStops=row;
        int amountLoops=0;
        int colStops=col-1;

        for(int y=0; y<5; y++){//5 because 5 buttons need to be toggled in theory

            /**
             * if statements select the correct lights to toggle
             * combined with for loop each button is toggled one at a time
             */
            if(amountLoops==1 || amountLoops==2){
                colStops++;
            }
            if(amountLoops==3){
                rowStops=rowStops-1;
                colStops=col;
            }
            if(amountLoops==4){
                rowStops=rowStops+2;
            }

            amountLoops++;

            if(colStops>=0 && colStops<=4 && rowStops>=0 && rowStops<=4){//checks to make sure selected is a valid board piece
                truth=(boolean) btn[rowStops][colStops].getClientProperty("on");

                GridButton first = new GridButton(rowStops, colStops, truth);

                btn[rowStops][colStops].putClientProperty("on", first.isLit());

                first.toggleLit();

                if((boolean) btn[rowStops][colStops].getClientProperty("on")==true){
                    btn[rowStops][colStops].setBackground(Color.black);
                    btn[rowStops][colStops].putClientProperty("on", false);
                }else{
                    btn[rowStops][colStops].setBackground(Color.gray);
                    btn[rowStops][colStops].putClientProperty("on", true);
                }
            }
        }
    }
}
