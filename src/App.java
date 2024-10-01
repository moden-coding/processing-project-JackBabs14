import processing.core.*;

public class App extends PApplet {

    float groundLine = 600 - 147 + 45;

    float characterx = 364;
    float charactery = 600 - 150;
    float speed = 5;
    boolean facingLeft = true;

    float[] bombX = {5, 100, 250, 355, 425, 500, 600, 750}; 
    float[] bombY = {0, 0, 0, 0, 0, 0, 0, 0};
    float[] verticalSpeed = {0, 0, 0, 0, 0, 0, 0, 0}; 
    boolean[] hasHitGround = {false, false, false, false, false, false, false, false};
    boolean[] isBombVisible = {true, true, true, true, true, true, true, true};
    int[] bombHitGroundTime = {0, 0, 0, 0, 0, 0, 0, 0};
    int[] bombResetTimes = {500, 1000, 750, 1100, 2000, 1250, 1000, 600};
    float gravity = 0.3f;

    boolean gameEnded = false;

    public static void main(String[] args) {
        PApplet.main("App");
    }

    public void setup() {
    }

    public void settings() {
        size(800, 600);
    }

    public void draw() {
        background(150);

        stroke(0);
        strokeWeight(6);
        line(0, groundLine, 800, groundLine);

        drawCharacter();
        handleCharacterMovement();

        for (int i = 0; i < bombX.length; i++) {
            handleBomb(i);
            if (isBombVisible[i]) {
                drawBomb(bombX[i], bombY[i]);
            }
        }
    }

    void drawCharacter() {
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
    }

    void handleCharacterMovement() {
        if (keyPressed) {
            if (key == 'a' || key == 'A' || keyCode == LEFT) {
                characterx -= speed;
                facingLeft = true;
            }
            if (key == 'd' || key == 'D' || keyCode == RIGHT) {
                characterx += speed;
                facingLeft = false;
            }
        }
    }

    void handleBomb(int index) {
        if (!hasHitGround[index]) {
            verticalSpeed[index] += gravity;
            bombY[index] += verticalSpeed[index];

            if (bombY[index] + 15 >= groundLine) {
                bombY[index] = groundLine - 15;
                verticalSpeed[index] = 0;
                hasHitGround[index] = true;
                isBombVisible[index] = false; 
                bombHitGroundTime[index] = millis(); 
            }
        } else {
            if (millis() - bombHitGroundTime[index] >= bombResetTimes[index]) {
                resetBomb(index);
            }
        }
    }

    void resetBomb(int index) {
        bombY[index] = 0;
        verticalSpeed[index] = 0;
        hasHitGround[index] = false;
        isBombVisible[index] = true;
    }

    void drawBomb(float x, float y) {
        fill(142);
        rect(x, y, 50, 15);

        float arcCenterX = x + 25;
        float arcCenterY = y + 15 + 10;
        arc(arcCenterX, arcCenterY, 40, 150, 0, PI);

        beginShape();
        stroke(142);
        vertex(x + 5, y + 25);
        vertex(x + 15, y + 15);
        vertex(x + 35, y + 15);
        vertex(x + 45, y + 25);
        endShape(CLOSE);

        stroke(0);
        line(x + 15, y + 15, x + 5, y + 25);
        line(x + 35, y + 15, x + 45, y + 25);
    }
}