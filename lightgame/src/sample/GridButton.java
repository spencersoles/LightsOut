package sample;

/**
 * Logic for each individual button
 * handles actions for when a button(light) is chosen
 */
public class GridButton {
    private int row;
    private int column;
    private boolean lit;

    public GridButton(int row, int column, boolean lit){
        this.row=row;
        this.column=column;
        this.lit=lit;
    }

    /**
     * getter for row of button chosen
     * @return
     */

    public int getRow(){
        int rows;

        rows=row;

        return rows;

    }

    /**
     * getter for column of button chosen
     * @return
     */

    public int getCol(){
        int columns=column;

        return columns;
    }

    /**
     * Checks if light is on or off (false means light is off)
     * @return
     */

    public boolean isLit(){
        boolean lits = lit;

        return lits;
    }

    /**
     * toggles the button (light) selected.  If light is on it is turned off.
     */

    public void toggleLit(){
        if(lit==true){//light is on so it is turned off
            lit=false;

        }else if(lit==false){//light is off so it is turned on
            lit=true;
        }

    }

}
