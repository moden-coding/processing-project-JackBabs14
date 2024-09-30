import processing.core.*;

public class App extends PApplet{

    float characterx = 364;
    float charactery = 600 - 150;
    float speed = 5;
    boolean facingLeft = true;

    public static void main(String[] args)  {
        PApplet.main("App");

        
    }

    public void setup(){
        
    }

    public void settings(){
        size(800, 600);
    }

    public void draw(){
       background(150);

       stroke(0);
       strokeWeight(6);
       line (0, 600 - 147 + 45, 800, 600 - 137 + 45);

       pushMatrix();
       translate(characterx, charactery);

        if (!facingLeft) {
            scale(-1, 1); 
        }

       strokeWeight(4);
       fill(100); 
       rect(-12, 0, 25, 50);
       fill(228, 185, 142);
       ellipse(0, -18, 36, 36);
       fill(0);
       ellipse(-5, -25, 5, 5);


       popMatrix();
     
       if (keyPressed) {
        if (key == 'a' || key == 'A' || keyCode == LEFT) {
            characterx -= speed;
            facingLeft = true;
        }
        if (key == 'd' || key == 'D' || keyCode == RIGHT) {
        characterx += speed;
        facingLeft = false; }
        
       }


       drawBomb(5, 0);




    }

    void drawBomb (float x, float y) {

        fill(142);
        rect(x, y, 50, 15);

        float arcCenterX = x + 25;
        float arcCenterY = y + 15 + 10;
       


        arc(arcCenterX, arcCenterY, 40 , 150, 0, PI);
        
        
       

       }


}

