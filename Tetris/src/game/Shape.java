package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/** Abstract class Shape is a superclass of all shapes in Tetris game. It 
 * contains all the functionality which is common for all shapes.
 * 
 * @author ALEXXX
 */
abstract class Shape {
    /** A constant that indicates a number of boxes in shapes and it is 
     * typically 4.
     */
    final int NUM_OF_BOXES_IN_SHAPE = 4;
    
    /** An ArrayList which holds all the boxes which are part of the given shape
     * 
     */
    List<Box> boxes;
    
    /** An ArrayList which holds all the boxes which are part of the Pile
     * 
     */
    List<Box> pileBoxes;
    
    
    Quarter quarter;
    Quarter previousQuarter;
    Box mainBox;
    EnumMap<Quarter, Integer[][]> boxesMap;
    boolean isLeftPossible = true;
    boolean isRightPossible = true;
    boolean isFallling = true;
    boolean isPetrified = false;
    boolean isDiving = true;
    Pile pile;
    int [][] yPileCoords;
    
    
    /** Default constructor Shape() has a single purpose of 
     * placing mainBox at 5, 1 coordinates. A shape appears in the middle top
     * this way.
     */ 
    public Shape () {
        // shape appears in the middle top        
        mainBox = new Box(5, 1);        
    }
    
    
    public Shape (Pile pile) {
        mainBox = new Box(5, 1);
        this.pile = pile;
        yPileCoords = pile.getYBasedCoord();
        pileBoxes = pile.getBoxes();
    }
    
    public void draw(Graphics2D g2) {        
        for (Box box : boxes) {
            box.draw(g2);
        }        
    }    
    
    // The method returns new mainBox after rotation. On the new mainBox
    // the rest of the shape is built
    public abstract Box getNewMainBox(Quarter newQuarter, 
                                    Quarter previousQuarter,
                                    Box oldMainBox);   
    
    // The method initializes ArrayList and all the boxes. It's called in
    // constructor
    public void initBoxes(Color color) {
        boxes = new ArrayList<>();
        
        for (int i = 0; i < NUM_OF_BOXES_IN_SHAPE; i++) {
            Box newBox = new Box(0, 0);
            newBox.setColor(color);
            boxes.add(newBox);
        }
    }
    
    // The method rotates the shape and watches against getting the shape
    // out of border
    // TODO: get rid of pile as a parameter, pile was introduced as instance variable
    public void rotate(Pile pile) {
        previousQuarter = quarter;
        quarter = quarter.next();
        mainBox = getNewMainBox(quarter, previousQuarter, mainBox);
        placeBoxes(quarter, mainBox);
        
        pileBoxes = pile.getBoxes();
        
        // box can be found out of the border after rotation, let's check it
        for (Box box : boxes) {
            // left border
            if (box.getX() < 1) {
                mainBox.setX(mainBox.getX() + 1);
                placeBoxes(quarter, mainBox);
            }
            // right border
            if (box.getX() > 10) {                
                mainBox.setX(mainBox.getX() - 1);
                placeBoxes(quarter, mainBox);
            }
            
            // TODO check the pile not to be on the way of rotation
            for (Box pileBox : pileBoxes) {
                if ((box.getY() == pileBox.getY()) && (box.getX() < pileBox.getX())) {
                    
                }
            }
        }
    }
    
    // the method initializes EnumMap which holds 2D Integer arrays (for each
    // possible Quarter)which in turn hold summands for mainBox to extrapolate
    // the rest of the boxes based on the mainBox coordinates
    public abstract void initBoxesLocationMap();
    
    
    // the method places boxes to the needed location based on the location of
    // mainBox and quarter it is turned
    public void placeBoxes(Quarter quarter, Box mainBox) {
        
        Integer[][] currentMapping = boxesMap.get(quarter);
        
        for (int i = 0; i < NUM_OF_BOXES_IN_SHAPE; i++) {
            Integer[] currBoxCoords = currentMapping[i];
            Box box = boxes.get(i);
            box.setX(mainBox.getX() + currBoxCoords[0]);
            box.setY(mainBox.getY() + currBoxCoords[1]);
        }        
        
    } // end of method
    
    public boolean hasTouched() {
        boolean isTouching = false;
        // check if shape touches the pile or the bottom
        outer : for (Box box : boxes) {
            // check bottom
            if ((box.getY() + 1) > 20) {                
                    isPetrified = true;    
                    pile.takeToHeap(this);
                    isTouching = true;
                    break;
            }
            inner : for (Box heapBox : pileBoxes) {
                if ((box.getX() == heapBox.getX()) && (box.getY() + 1 == heapBox.getY()) ) {                    
                    isPetrified = true;
                    isTouching = true;     
                    pile.takeToHeap(this);
                    break outer;       
                } // end of if statement
            } // end of inner loop        // end of inner loop       
         } // end of outer loop // end of outer loop // end of outer loop // end of outer loop
        return isTouching;
    }
    
    public void fall(Pile pile) {
        /*
        isFallling = true;
        pileBoxes = pile.getBoxes();
        */
        
        isFallling = !hasTouched();
        /*
        
        // check if shape touches the pile or the bottom
        outer : for (Box box : boxes) {
            // check bottom
            if ((box.getY() + 1) > 20) {                
                    isPetrified = true;    
                    pile.takeToHeap(this);
                    isFallling = false;
                    break;
            }
            inner : for (Box heapBox : pileBoxes) {
                if ((box.getX() == heapBox.getX()) && (box.getY() + 1 == heapBox.getY()) ) {                    
                    isPetrified = true;
                    isFallling = false;     
                    pile.takeToHeap(this);
                    break outer;       
                } // end of if statement
            } // end of inner loop       
         } // end of outer loop
        */
        
        if (isFallling) {
            mainBox.setY(mainBox.getY() + 1);
            placeBoxes(quarter, mainBox);
        }        
            
    }
    
    
    
    public void dive(Pile pile) {
        isDiving = true;
        boolean prevValue = true; // debug line
        
        pileBoxes = pile.getBoxes();
        
        // get all the unique x shape values in natural ascending order
        Set<Integer> xBoxCoords = new TreeSet<>();    
        
        // the "lowest" part  of a shape
        int maxY = mainBox.getY();
        
        for (Box box : boxes) {
            xBoxCoords.add(box.getX());
            
            if (box.getY() > maxY) {
                maxY = box.getY();
            }
            
            
            /*
            for (Box heapBox : pileBoxes) {
                if ((box.getY() + 1) > heapBox.getY()) {
                    isDiving = false;
                }
            }
            if (box.getY() + 1 >= 20) {
                isDiving = false;
            }
            
            */
        } // end of loop
        
        // System.out.println("maxY: " + maxY);
        // System.out.println("x values: " + xBoxCoords);
        
        Iterator<Integer> iter = xBoxCoords.iterator();
        while (iter.hasNext()) {
            int x = iter.next();
            
            // if close to bottom -> brake
            if (maxY >= 19) {
                isDiving = false;
            }
            
            
            // this maxY check is done not to go out of array's bound
            if (maxY >= 2 && maxY <= 17 &&  yPileCoords[x-1][maxY-2] == 1) { 
                // maxY is actually maxY+1 it scans one step ahead in y direction
                // remember? arrays are zero-based, the box.getX() etc are not.
                isDiving = false;
            }
            
        }
        
        if (prevValue != isDiving) {
            System.out.println("The checking in Shape abstract class WORKED!");
        }
        
        
        if (hasTouched()) {
            isDiving = false;
        }
        
        if (isDiving) {
            mainBox.setY(mainBox.getY() + 1);
            placeBoxes(quarter, mainBox);
            // System.out.println(Arrays.deepToString(yPileCoords));
        } 
            
    }
    
    public void leftMove(Pile pile) {
        List<Box> pileBoxes = pile.getBoxes();
        isLeftPossible = true;
        for (Box box : boxes) {
            // check if there is a border on the left
            if (box.getX() == 1) {
                isLeftPossible = false;
            }
            for (Box pileBox : pileBoxes) {
                // check if there is a pile on the left
                if ((box.getY() == pileBox.getY()) 
                    && (box.getX()-1 == pileBox.getX())) {
                    isLeftPossible = false;                    
                }                
            }
        }
        if (isLeftPossible) {
            mainBox.setX(mainBox.getX() - 1);
            placeBoxes(quarter, mainBox);
        }
        
    }
    
    public void rightMove(Pile pile) {
        List<Box> pileBoxes = pile.getBoxes();
        isRightPossible = true;
        for (Box box : boxes) {
            
            // check if there is border on the right
            if(box.getX() == 10) {
                isRightPossible = false;
            }
            for (Box pileBox : pileBoxes) {
                // check if there is a pile on the right
                if ((box.getY() == pileBox.getY()) && (box.getX()+1 == pileBox.getX())) {
                    isRightPossible = false;                    
                }                
            }
        }
        if (isRightPossible) {
            mainBox.setX(mainBox.getX() + 1);
            placeBoxes(quarter, mainBox);
        }
        
            
    }
    
    public void setMainBox(int x, int y) {
        this.mainBox.setX(x);
        this.mainBox.setY(y);
    }
    
    public List<Box> getBoxes() {
        return boxes;
    }
    
    public boolean getIsPetrified(){
        return isPetrified;
    }
}