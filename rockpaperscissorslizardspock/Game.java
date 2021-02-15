
package rockpaperscissorslizardspock;


import java.util.concurrent.*;

public class Game {
    private boolean gameEnded;
    private String winner;
    private int player1Wins, player2Wins;

    private int rockLosesTo[], paperLosesTo[], scissorsLosesTo[], lizardLosesTo[], spockLosesTo[];
    private int moveNumToString[][];
    public Game() {
        this.gameEnded = false;
        this.winner = null;
        this.player1Wins = 0;
        this.player2Wins = 0;
        /*
        1 : rock
        2 : paper
        3 : scissors
        4 : lizard
        5 : spock
        */
        this.rockLosesTo = new int[]{2,5};
        this.paperLosesTo = new int[]{3,4};
        this.scissorsLosesTo = new int[]{1,5};
        this.lizardLosesTo = new int[]{1,3};
        this.spockLosesTo = new int[]{2,4};
        this.moveNumToString = new int[][]{this.rockLosesTo, this.paperLosesTo, 
            this.scissorsLosesTo, this.lizardLosesTo, this.spockLosesTo};
    }
    
    public void start() throws InterruptedException, ExecutionException {
        System.out.println("starting game");
        
        // Main game loop
        while (this.gameEnded == false ) {

            MakeMove thread1 = new MakeMove();
            MakeMove thread2 = new MakeMove();
            
            
            ExecutorService service = Executors.newSingleThreadExecutor();
            Future<Integer> future1 = service.submit(thread1);
            Future<Integer> future2 = service.submit(thread2);
            
            int p1Move = future1.get();
            int p2Move = future2.get();
            
            service.shutdown();

            // Case players play the same move
            if (p1Move == p2Move) {
                System.out.println("tie, next round");
                continue;
            }
            
            if (this.fight(p1Move, p2Move)){
                this.player1Wins++;
                System.out.println("player 1 wins round");
            } else {
                this.player2Wins++;
                System.out.println("player 2 wins round");
            }
            
            
            if (this.player1Wins == 4) {
                this.gameEnded = true;    
                this.winner = "player 1";
            }
            
            if (this.player2Wins == 4) {
                this.gameEnded = true;    
                this.winner = "player 2";
            }

        }
        System.out.println("Game over. Winner is " + this.winner);
    }

    // This method comapares moves and returns true if the first move beat the second
    private boolean fight(int Move1, int Move2) {
        int move1LoseArr[]= this.moveNumToString[Move1-1];
        for (int i = 0; i < move1LoseArr.length; i++) {
            if (move1LoseArr[i] == Move2) {
                return false;
            }
        }
        return true;
    }
}
