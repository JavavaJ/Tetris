package tests;

import game.ShapeType;

/** The class tests the method getRandomShapeType of enum ShapeType.
 * 
 * @author ALEXXX
 */
class GetRandomShapeTypeMethodTest {
    public static void main(String[] args) {
            
        
        for (int i = 0; i < 20; i++) {
            System.out.println(ShapeType.ROD.getRandomShapeType());
        }
    }
}