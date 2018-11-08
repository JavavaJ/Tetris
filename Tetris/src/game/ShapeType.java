package game;

/** ShapeType enum holds seven possible tetris shapes:
 *  L_SHAPE,
 *  J_SHAPE,
 *  ROD,
 *  CUBE,
 *  T_SHAPE,
 *  Z_SHAPE,
 *  S_SHAPE;
 * 
 * @author ALEXXX
 */
public enum ShapeType {
    L_SHAPE,
    J_SHAPE,
    ROD,
    CUBE,
    T_SHAPE,
    Z_SHAPE,
    S_SHAPE;
    
    
    /** Static variable allTypes holds an array of all ShapeType enum values
     * 
     */
    private static ShapeType[] allTypes = values(); // making it static avoids copying it every time methods are called
    private static int numberOfShapeTypes = allTypes.length;
    
    /** The method returns a random ShapeType enum value
     * 
     * @return random ShapeType enum value
     */
    public ShapeType getRandomShapeType() {
        // let's generate a random integer
        int randomIntInBound = (int) (Math.random() * numberOfShapeTypes);
        return allTypes[randomIntInBound];
    }
    
}