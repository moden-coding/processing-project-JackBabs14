import processing.core.*;

public class App extends PApplet {

    boolean gameStart = false;
    boolean gameEnded = false;


    float groundLine = 700 - 147 + 45;

    float characterx = 275;
    float charactery = 700 - 150;
    float speed = 5;
    boolean facingLeft = true;

    float[] bombX = {5, 100, 175, 250, 300, 355, 425, 500, 600, 750, 800}; 
    float[] bombY = {1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000};
    float[] verticalSpeed = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; 
    boolean[] hasHitGround = {false, false, false, false, false, false, false, false, false, false, false};
    boolean[] isBombVisible = {true, true, true, true, true, true, true, true, true, true, true};
    int[] bombHitGroundTime = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    int bt1 = 500;
    int bt2 = 1000;
    int bt3 = 750;
    int bt4 = 1100;
    int bt5 = 2000;
    int bt6 = 1250;
    int[] bombResetTimes = {bt1, bt2, bt4, bt6, bt3, bt4, bt5, bt6, bt2, bt1, bt4};
    float gravity = 0.3f;

    int gameStartTime = 0;
    int time = 0;
    int timeThreshold = 7500;
    int timeThreshhold2 = 15000;
    int timeThreshhold3 = 25000;

    public static void main(String[] args) {
        PApplet.main("App");
    }

    public void setup() {
    }

    public void settings() {
        size(800, 600);
    }

    public void draw() {

        if (!gameStart) {
            displayIntro();    
        } else {
            runGame();
        }
            }
        

    void runGame() {

        time = (millis() - gameStartTime);
        
        background(150);
        stroke(125);
        strokeWeight(3);

        stroke (0);
        fill (75);
        beginShape();
        vertex(825, 70);
        vertex(900, 70);
        vertex(900, groundLine);
        vertex(750, groundLine);
        vertex(750, 300); 
        vertex(775, 305);
        vertex(800, 295);
        vertex(825, 307);
        vertex(831, 245);
        vertex(826, 180);
        vertex(830, 150);
        vertex(823, 90);
        vertex(825, 70);
        endShape();

        fill(100);
        beginShape();
        vertex(725, groundLine - 200);
        vertex(650, groundLine - 200);
        vertex(650, groundLine);
        vertex(850, groundLine);
        vertex(850, groundLine - 100);
        vertex(833, groundLine - 98);
        vertex(814, groundLine - 95);
        vertex(792, groundLine - 97);
        vertex(765, groundLine - 101);
        vertex(730, groundLine - 102);
        vertex(725, groundLine - 115);
        vertex(722, groundLine - 145);
        vertex(724, groundLine - 175);
        vertex(725, groundLine - 200);
        endShape();
       
        stroke(0);
        strokeWeight(3);
        fill(80);
        beginShape();
        vertex(350, 150);
        vertex(350, groundLine);
        vertex(525, groundLine);
        vertex(525, 150);
        vertex(505, 155);
        vertex(480, 157);
        vertex(430, 156);
        vertex(403, 159);
        vertex(370, 156);
        vertex(350, 150);
        endShape();

        stroke(0);
        strokeWeight(3);
        fill (50);
        rect(50, 100, 100, groundLine - 100);
        fill (100);
        beginShape();
        vertex(100, 200);
        vertex(100, groundLine);
        vertex(275, groundLine);
        vertex(275, 350);
        vertex(230, 330);
        vertex(200, 295);
        vertex(150, 275);
        vertex(145, 215);
        vertex(100, 200);
        endShape();

        fill (0);
        textSize(35);
        text(time, 730, 25);


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

        if (characterx < 0) {
            characterx = 0;
        }
        if (characterx > width - 25) {
            characterx = width - 25;
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
        textSize(150);
        text("You died!", 400, 300);
        fill(255);        

        textSize(32);
        text("Press Enter to reset", 400, 550);
    }

void displayIntro() {
    background(0);

    fill(255);
    textAlign(CENTER);
    textSize(100);
    text("Hello!", 400, 150);
    text("Mission: Survive", 400, 250);
    text("Dont get bombed", 400, 350);

    textSize(30);
    text("Dodge the falling bombs using the arrows or the A and D keys", 400, 500);
    text("Press Enter to begin the game", 400, 550);
}

public void keyPressed() {

    if (keyCode == ENTER  && !gameEnded) {
            gameStart = true;
            gameStartTime = millis();
        }

        if (keyCode == ENTER && gameEnded == true) {
            gameEnded = false;
            gameStart = false;
            characterx = 364;
            charactery = 700 - 150;
            speed = 5;
            facingLeft = true;
            for (int i = 0; i < bombX.length; i++) {
                bombY[i] = 1000;
                verticalSpeed[i] = 0;
                hasHitGround[i] = false;
                isBombVisible[i] = true;
                bombHitGroundTime[i] = 0;
            }
            gameStartTime = 0;
            gravity = 0.3f;
            time = 0;
        }
        
    }


}




