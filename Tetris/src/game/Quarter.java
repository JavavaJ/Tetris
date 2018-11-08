package game;

/** The enum Quarter refers to one of four positions a shape can be in. 
 * They are a progressive clockwise rotation of a shape by 90 degrees.
 * @author ALEXXX
 */
public enum Quarter {
    FIRST,
    SECOND,
    THIRD,
    FOURTH;
    
    /** Array allQuarterValues holds values of all enums.
     * 
     */
    private static Quarter[] allQuarterValues = values();   // making it private static avoids 
                                                // array copying each time next() is called
    
    /** Method returns next enum element
     * 
     * @return next enum element
     */
    public Quarter next() {
        return allQuarterValues[(this.ordinal()+1) % allQuarterValues.length];
    }
}