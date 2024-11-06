import processing.core.*;

public class App extends PApplet {

    // Checks to see if game started or ended
    boolean gameStart = false;
    boolean gameEnded = false;

// ground line for the character
    float groundLine = 700 - 147 + 45;

    // Starting x and y coords of character
    float characterx = 275;
    float charactery = 700 - 150;
    
    float speed = 5;
    
    boolean facingLeft = true;

    float[] bombX = {0, 50, 100, 150, 200, 250, 300, 350, 400, 450, 500, 550, 600, 650, 700, 750}; // needed to set the bomb design across multiple different x axis so I used an array to hold multiple inputs at once without needing so many seperate integers.
    float[] bombY = {1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000}; // starts all the bombs beneath the screen so they automatically disapear and reset to the begining after the specified delay
    float[] verticalSpeed = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; 
    boolean[] hasHitGround = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
    boolean[] isBombVisible = {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true};
    int[] bombHitGroundTime = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    int[] bombResetTimes = {500, 1402, 807, 1837, 1053, 2000, 1325, 1837, 807, 1402, 1325, 500, 2000, 1053, 1837, 1402}; // the amount of time it takes a bomb to reset to the top, avoids using a lot of individual values.
    float gravity = 0.3f; // double converted to a float with f

    int gameStartTime = 0;
    int score = 0;
    int finalscore = 0;
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

        // Game starts at the intro screen, but then goes to run the game
        if (!gameStart) {
            displayIntro();    
        } else {
            runGame();
        }
            }
        

    void runGame() {

        
        // Chat GPT taught me how to draw irregular shapes using vertex and so I made a background
        // Started by making all of them rectangles
        //then edited it to have the cut out shapes to look destroyed
        // then used vertex to make the lines look more textured
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

        //chatgpt taught me how to display text, just used it to display the score at a certain location.
        fill (0);
        textSize(35);
        text(score, 730, 25);

    
        //until game ends, tracks score and creates and handles the character
        if (!gameEnded) { 
            score = (millis() - gameStartTime);

            drawCharacter();
            handleCharacterMovement();

            // Has the bombs working as expected and draws them on the specified x axis
            for (int i = 0; i < bombX.length; i++) {
                handleBomb(i);
                if (isBombVisible[i]) {
                    drawBomb(bombX[i], bombY[i]);
                }
            }
            //if the bomb hits the player, game over, score is finalized, and goes to end screen
            checkForCollision(); 
        } else {
            finalscore = score;
            displayGameOver(); 

        }

    }
// Here I used some basic shapes to draw the character, but chat gpt told me how to make it face different derections
// translate sets the starting characterx and character why as 0,0
// Push/Pop Matrix set the character as its own unique entity in the screen that is not part of the background so different commands can be done to it without affecting the background.
//Scale inverts the drawing along the verticle middle so it is facing the other direction
    void drawCharacter() {
        pushMatrix();
        translate(characterx, charactery);

        if (!facingLeft) {
            scale(-1, 1);
        }

        stroke(0);
        strokeWeight(4);
        fill(200, 0, 0); 
        rect(-12, 0, 25, 50);
        strokeWeight(1);
        fill(2, 113, 72);
        rect(-12, 35, 25, 15);
        fill(225);
        rect(-12, 25, 25, 10);
        strokeWeight(4);
        stroke(0);
        fill(228, 185, 142);
        ellipse(0, -18, 36, 36);
        fill(0);
        ellipse(-5, -25, 5, 5);
        noFill();
        rect(-12, 0, 25, 50);

        popMatrix();
    }

    // Sets the keys to control the x of the characer making him move left and right by the speed amount; Keys also determin which way he is facing
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
        //sets a wall so the character can't leave the screen
        if (characterx < 0) {
            characterx = 0;
        }
        if (characterx > width - 25) {
            characterx = width - 25;
        }
    }

    // all the # of bombs start off not having hit ground, whil they dont hit the ground, their y axis goes down by the gravity ammount
    // Knows when the bomb has hit the ground and makes the bomb invisable and waits the time specified earlier before it reappears at the top, 
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
        } else { // after a certain amount of time since game started, bombs fall faster and their reset time shrinks proportionally to thier initial reset time
            if (score > 15000 && score < 25000) {
                bombResetTimes[index] = 750 + (index % 7) * 200; 
                gravity = 0.5f;
            } else if (score > 25000) {
                bombResetTimes[index] = 500 + (index % 7) * 150; 
                gravity = 0.7f;
            }
            //resets the bomb after it waited its designated time
            if (millis() - bombHitGroundTime[index] >= bombResetTimes[index]) {
                resetBomb(index);
            }
        }
    }
// sets the bomb index all to zero for each restart (necesarry because it starts at 1000 and it moves down throughout the game)
// resets the speed
//makes the bomb visible and no longer having the status of hit the ground
    void resetBomb(int index) {
        bombY[index] = 0;
        verticalSpeed[index] = 0;
        hasHitGround[index] = false;
        isBombVisible[index] = true;
    }

    void drawBomb(float x, float y) {
        fill(142);
        rect(x, y, 50, 15);

        // Chat gpt taught me how to use arc for making ovals and partial ovals instead of just circles
        float arcCenterX = x + 25;
        float arcCenterY = y + 15 + 10;
        //draws bottom of bomb
        arc(arcCenterX, arcCenterY, 40, 150, 0, PI);

        //draws top of bomb
        beginShape();
        stroke(142);
        vertex(x + 5, y + 25);
        vertex(x + 15, y + 15);
        vertex(x + 35, y + 15);
        vertex(x + 45, y + 25);
        endShape(CLOSE);

        //line to connect the two
        stroke(0);
        line(x + 15, y + 15, x + 5, y + 25);
        line(x + 35, y + 15, x + 45, y + 25);
    }

    void checkForCollision() {
        //hight and width of character identified
        float characterWidth = 25;
        float characterHeight = 50;

        //while the bomb is visable, it is length and width is defined
        for (int i = 0; i < bombX.length; i++) {
            if (isBombVisible[i]) {
                float bombWidth = 50;
                float bombHeight = 15;

                // if the bomb x or y distance from bomb center gets within the character x or y disctance from character center (bomb touches character) --> character dies
                if (characterx < bombX[i] + bombWidth &&
                    characterx + characterWidth > bombX[i] &&
                    charactery < bombY[i] + bombHeight &&
                    charactery + characterHeight > bombY[i]) {
                    gameEnded = true; 
                    
                }
            }
        }
    }

    // game over screen, chat gpt taught how to center text
    void displayGameOver() {
        background (0);

        fill(255, 0, 0);
        textAlign(CENTER);
        textSize(150);
        text("You died!", 400, 300);
        fill(255);
        textSize(50);
        text("Your score: " + finalscore, 400, 400);
        textSize(32);
        text("Press Enter to reset", 400, 550);
    }
// begining screen
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
// If the game isnt over and the enter key is pressed, it starts the game and starts the timer.
    if (keyCode == ENTER  && !gameEnded) {
            gameStart = true;
            gameStartTime = millis();
        }
        
        // If enter is pressed and the game is over, it resets the game and character and score and bomb starting positions and speed and takes us back to intro screen
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
            score = 0;
        }
        
    }


}




