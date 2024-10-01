import processing.core.*;

public class App extends PApplet {

    boolean gameStart = false;
    boolean gameEnded = false;


    float groundLine = 700 - 147 + 45;

    float characterx = 364;
    float charactery = 700 - 150;
    float speed = 5;
    boolean facingLeft = true;

    float[] bombX = {5, 100, 250, 355, 425, 500, 600, 750, 855}; 
    float[] bombY = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    float[] verticalSpeed = {0, 0, 0, 0, 0, 0, 0, 0, 0}; 
    boolean[] hasHitGround = {false, false, false, false, false, false, false, false, false};
    boolean[] isBombVisible = {true, true, true, true, true, true, true, true, true};
    int[] bombHitGroundTime = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    int bt1 = 500;
    int bt2 = 1000;
    int bt3 = 750;
    int bt4 = 1100;
    int bt5 = 2000;
    int bt6 = 1250;
    int[] bombResetTimes = {bt1, bt2, bt3, bt4, bt5, bt6, bt2, bt1, bt4};
    float gravity = 0.3f;

    int gameStartTime = 0;
    int timeThreshold = 7500;
    int timeThreshhold2 = 15000;
    int timeThreshhold3 = 25000;

    public static void main(String[] args) {
        PApplet.main("App");
    }

    public void setup() {
    }

    public void settings() {
        size(900, 700);
    }

    public void draw() {

        if (!gameStart) {
            displayIntro();    
        } else {
            runGame();
        }
            }
            
        

    void runGame() {

        background(150);

        
        stroke(0);
        strokeWeight(6);
        line(0, groundLine, 900, groundLine);

        if (gameStart && gameStartTime == 0) {
            gameStartTime = millis();
        }

            if (millis() - gameStartTime > timeThreshold) {
                gravity = 0.4f;
                int bt1 = 450;
                int bt2 = 900;
                int bt3 = 700;
                int bt4 = 1000;
                int bt5 = 1750;
                int bt6 = 1100;
            }

            if (millis() - gameStartTime > timeThreshhold2) {
                gravity = 0.5f;
                int bt1 = 400;
                int bt2 = 750;
                int bt3 = 500;
                int bt4 = 750;
                int bt5 = 1500;
                int bt6 = 1000;
            }

            if (millis() - gameStartTime > timeThreshhold3) {
                gravity = 0.6f;
                int bt1 = 300;
                int bt2 = 500;
                int bt3 = 450;
                int bt4 = 600;
                int bt5 = 1250;
                int bt6 = 750;
            }


        if (!gameEnded) { 
            drawCharacter();
            handleCharacterMovement();

            for (int i = 0; i < bombX.length; i++) {
                handleBomb(i);
                if (isBombVisible[i]) {
                    drawBomb(bombX[i], bombY[i]);
                }
            }

            checkForCollision(); 
        } else {
            displayGameOver(); 
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

    void checkForCollision() {
        float characterWidth = 25;
        float characterHeight = 50;

        for (int i = 0; i < bombX.length; i++) {
            if (isBombVisible[i]) {
                float bombWidth = 50;
                float bombHeight = 15;

                if (characterx < bombX[i] + bombWidth &&
                    characterx + characterWidth > bombX[i] &&
                    charactery < bombY[i] + bombHeight &&
                    charactery + characterHeight > bombY[i]) {
                    gameEnded = true; 
                }
            }
        }
    }

    void displayGameOver() {
        background (0);

        fill(255, 0, 0);
        textAlign(CENTER);
        textSize(100);
        text("You died!", 450, 200);
        fill(255);
        textSize(42);
        text("You never got a chance to hug your mom again", 450, 300);
        text("or tell your dad and brother goodbye.", 450, 400);
        text("If only you had stopped this war from starting.", 450, 550);
    }

void displayIntro() {
    background(0);

    fill(255);
    textAlign(CENTER);
    textSize(50);
    text("Hello!", 450, 100);
    textSize(32);
    text("Some radical terrorists took over your country's government", 450, 150);
    text("and they went to war with the country right next you yours.", 450, 200);
    text("The city where you were born and raised in is being bombed.", 450, 250);
    text("Survive the war so you can hug your family again.", 450, 350);
    text("and hopefully after the war life will get good like it was before.", 450, 400);
    text("Dodge the falling bombs", 450, 600);
    text("Press Enter to begin the game", 450, 650);
}

public void keyPressed() {

    if (keyCode == ENTER) {
            gameStart = true;
        }
        
    }

}




