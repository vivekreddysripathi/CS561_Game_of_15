import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GameOf15 {

    private int counter=0;

    private long startTime;
    private long endTime;


    private Scanner sc= new Scanner(System.in);

    private Random rand = new Random();


    //create a board

    private int[][] board= new int[3][3];

    //populate the board originally with all 0's

    private void populateBoard(){
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                board[i][j]=0;
            }
        }
    }

    // a boolean array that will tell us what are choices are available

    private boolean[] availableDigits= new boolean[10];

    //making choices available for the user as well as ai

    private void populateChoices(){
        for(int i=1; i<10; i++){
            availableDigits[i]=true;
        }
    }

    //current stage of the board

    private int[] input= new int[9];

    // take input board from the user

    private void takeInput(){
        System.out.println("Enter the board values in the format 4 0 5 3 2 0 0 7 9: ");
        for(int i=0; i<9;i++){
            input[i]= sc.nextInt();
        }
        System.out.println();
    }

    // finding the depth of the game at a particular stage

    private int findDepth(){

        int nonZero=0;

        for(int i=0; i<input.length; i++){
            if(input[i]!=0){
                nonZero++;
            }
        }
        int depth=nonZero+1;

        return depth;
    }

    //check if there are no more moves left. If yes terminate the program
    private void checkMovesLeft(int depth){
        if(depth==10){
            System.out.println("---------------------------------------------------------");
            System.out.println("|                                                       |");
            System.out.println("              Congratulations! It is a Tie               ");
            System.out.println("|                                                       |");
            System.out.println("---------------------------------------------------------");
            sc.close();
            System.exit(0);
        }
    }

    // print the board

    private void printBoard(){
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                System.out.print(board[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // add a number to the board and make the choice unavailable just for the sake of printing the next desired move

    private void addToBoard(int r, int c, int n){
        board[r][c]=n;
        availableDigits[n]=false;
    }

    // evaluate the board if there is a winner on either side or it is a tie

    private boolean evaluate(int[][] board){

        int sum=0;

        //check rows
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                // if one of the numbers in the row is 0 then don't check it further and go to the next row
                if(board[i][j]==0){
                    sum=0;
                    break;
                }
                // add the non-zero integer to sum
                else{
                    sum+=board[i][j];
                }
            }
            if(sum==15){
                return true;
            }
            //reset sum to 0
            sum=0;
        }


        //check columns
        for(int j=0; j<3; j++){
            for(int i=0; i<3; i++){
                // if one of the numbers in the column is 0 then don't check it further and go to the next column
                if(board[i][j]==0){
                    sum=0;
                    break;
                }
                // add the non-zero integer to sum
                else{
                    sum+=board[i][j];
                }
            }
            if(sum==15){
                return true;
            }
            //reset sum to 0
            sum=0;
        }


        //reset sum to 0
        sum=0;



        // checking diagnol 1
        for(int i=0,j=0; i<3; i++,j++){
            int number=board[i][j];
            if(number!=0){
                // add the non-zero integer to sum
                sum+=number;
            }
            // if one of the numbers in the column is 0 then don't check it further and go to the next column
            else{
                sum=0;
                break;
            }
        }
        if(sum==15){
            return true;
        }



        // checking diagnol 2
        for(int i=2,j=0; j<3; i--,j++){
            int number=board[i][j];
            if(number!=0){
                sum+=number;
            }
            else{
                sum=0;
                break;
            }
        }
        if(sum==15){
            return true;
        }

        //or else there is no winner yet with still moves left or maybe it is a tie if there are no moves left
        return false;
    }

    private void printDuration(){
        System.out.println("Time taken for finding the best move: "+(endTime-startTime)+" milli seconds");
    }

    private void printComputations(){
        System.out.println("Number of computations for finding the best move: "+counter);
        counter=0;
    }

    // This is the minimax function. It considers all
    // the possible ways the game can go and returns
    // the value of the board
    private int minimax(int board[][], int depth, Boolean isMax){

        //check if the game ended
        boolean win = evaluate(board);

        // if there is a winner
        if(win){

            //if odd wins total odds will be 1 more than total evens else equal odds and even on the board
            int evens=0, odds=0;

            // calcuating no.of odds and even on the board
            for(int i=0; i<3; i++){
                for(int j=0; j<3; j++){
                    int x= board[i][j];
                    if(x!=0 && x%2==0){
                        evens++;
                    }
                    if(x!=0 && x%2==1){
                        odds++;
                    }
                }
            }

            //odd is the winnner because winning move is made by odd
            if(odds>evens){
                return 100-(depth);
            }
            //even is the winner because winning move is made by even
            return -100+(depth);
        }

        //if there is no winner yet and there are no more moves left it is a tie
        if(depth==9){
            return 0;
        }

        // there is no winner yet and moves are left, so go further to play the game


        // If this maximizer's move i.e odd
        if (isMax){

            // A List that will contain the values of all possible moves
            List<Move> numbers = new ArrayList<>();

            // Traverse all cells
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    // Check if cell is empty
                    if (board[i][j]==0)
                    {
                        for(int d=1; d<10; d+=2){
                            if(availableDigits[d]){
                                // Make the move
                                board[i][j] = d;
                                availableDigits[d]=false;


                                // Call minimax recursively and choose the maximum value
                                int moveVal= minimax(board, depth + 1, !isMax);
                                counter++;

                                // best = Math.max(best, eval);

                                // Undo the move
                                board[i][j] = 0;

                                //make the choice available again
                                availableDigits[d]=true;

                                //add the evaluation value to a list
                                numbers.add(new Move(i,j,d, moveVal));
                            }
                        }
                    }
                }
            }
            int maxvalue=Integer.MIN_VALUE;

            //find out the maximum evaluation value

            for(int i=0 ;i<numbers.size();i++){
                int thisValue=numbers.get(i).value;
                maxvalue= Math.max(maxvalue,thisValue);
            }

            //create a new list that stores only the maximum valuation value moves i.e the best moves

            List<Move> BestMoves= new ArrayList<>();

            for(int i=0 ;i<numbers.size();i++){
                int thisValue=numbers.get(i).value;
                if(thisValue==maxvalue){
                    BestMoves.add(numbers.get(i));
                }
            }

            //randomly select a best move from the list and store it in a move to return

            Move bestMove= BestMoves.get(rand.nextInt(BestMoves.size()));

            // return a random best move evaluation value
            return bestMove.value;
        }

        // If this minimizer's move i.e even
        else{

            // A List that will contain the values of all possible moves
            List<Move> numbers = new ArrayList<>();

            // Traverse all cells
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    // Check if cell is empty
                    if (board[i][j] == 0)
                    {
                        for(int d=2; d<10; d+=2){
                            if(availableDigits[d]){
                                // Make the move
                                board[i][j] = d;
                                availableDigits[d]=false;

                                // Call minimax recursively and choose
                                // the minimum value
                                int moveVal= (minimax(board, depth + 1, !isMax));

                                counter++;
                                // best = Math.min(best, eval);

                                // Undo the move
                                board[i][j] = 0;

                                //make the choice available again
                                availableDigits[d]=true;

                                //add the evaluation value to a list
                                numbers.add(new Move(i,j,d, moveVal));
                            }
                        }
                    }
                }
            }

            int minvalue=Integer.MAX_VALUE;

            //find out the minimum evaluation value

            for(int i=0 ;i<numbers.size();i++){
                int thisValue=numbers.get(i).value;
                minvalue= Math.min(minvalue,thisValue);
            }

            //create a new list that stores only the minimum valuation value moves i.e the best moves

            List<Move> BestMoves= new ArrayList<>();

            for(int i=0 ;i<numbers.size();i++){
                int thisValue=numbers.get(i).value;
                if(thisValue==minvalue){
                    BestMoves.add(numbers.get(i));
                }
            }

            //randomly select a best move from the list and store it in a move to return

            Move bestMove= BestMoves.get(rand.nextInt(BestMoves.size()));

            // return a random best move evaluation value

            return bestMove.value;
        }
    }

    // Make the ai play on odd side

    private Move playOdd(int[] input, int depth){

        int index=0;

        // populate the board from input and make choices unavailable

        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                int inputNumber=input[index];
                board[i][j]=inputNumber;
                availableDigits[inputNumber]=false;
                index++;
            }
        }

        printBoard();
        System.out.println("Let's go for the odd move\n");

        // A List that will contain the values of all possible moves
        List<Move> numbers = new ArrayList<>();

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                // Check if cell is empty
                if (board[i][j] == 0)
                {


                    for(int d=1; d<10; d+=2){
                        if(availableDigits[d]){
                            // Make the move
                            board[i][j] = d;
                            availableDigits[d]=false;

                            // printBoard();

                            // compute evaluation function for this
                            // move.
                            int moveVal = minimax(board, depth, false);
                            counter++;

                            // System.out.println("The value of the Move "+d+" at row "+i+" col "+j+" is: "+moveVal);

                            // Undo the move
                            board[i][j] = 0;
                            //make the choice available again
                            availableDigits[d]=true;

                            //add the evaluation value to a list

                            numbers.add(new Move(i,j,d, moveVal));
                        }
                    }
                }
            }
        }

        int maxvalue=Integer.MIN_VALUE;

        //find out the maximum evaluation value

        for(int i=0 ;i<numbers.size();i++){
            int thisValue=numbers.get(i).value;
            maxvalue= Math.max(maxvalue,thisValue);
        }

        //create a new list that stores only the maximum valuation value moves i.e the best moves

        List<Move> BestMoves= new ArrayList<>();

        for(int i=0 ;i<numbers.size();i++){
            int thisValue=numbers.get(i).value;
            if(thisValue==maxvalue){
                BestMoves.add(numbers.get(i));
            }
        }

        //randomly select a best move from the list and store it in a move to return

        Move bestMove= BestMoves.get(rand.nextInt(BestMoves.size()));


        // System.out.println("The value of the best Odd Move is "+maxvalue+" and choice is "+bestMove.number+" at row "+bestMove.row+" col "+bestMove.col);

        // return the random best move
        return bestMove;
    }

    // Make the ai play on the even side

    private Move playEven(int[] input, int depth){

        int index=0;

        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                int inputNumber=input[index];
                board[i][j]=inputNumber;
                availableDigits[inputNumber]=false;
                index++;
            }
        }

        printBoard();
        System.out.println("Let's go for the even move\n");

        // A List that will contain the values of all possible moves
        List<Move> numbers = new ArrayList<>();

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                // Check if cell is empty
                if (board[i][j] == 0)
                {
                    for(int d=2; d<10; d+=2){
                        if(availableDigits[d]){
                            // Make the move
                            board[i][j] = d;
                            availableDigits[d]=false;

                            // printBoard();

                            // compute evaluation function for this
                            // move.
                            int moveVal = minimax(board, depth, true);
                            counter++;


                            // System.out.println("The value of the Move "+d+" at row "+i+" col "+j+" is: "+moveVal);

                            // Undo the move
                            board[i][j] = 0;
                            //make the choice available again
                            availableDigits[d]=true;

                            //add the evaluation value to a list

                            numbers.add(new Move(i,j,d, moveVal));
                        }
                    }
                }
            }
        }

        int minvalue=Integer.MAX_VALUE;

        //find out the minimum evaluation value

        for(int i=0 ;i<numbers.size();i++){
            int thisValue=numbers.get(i).value;
            minvalue= Math.min(minvalue,thisValue);
        }

        //create a new list that stores only the minimum valuation value moves i.e the best moves

        List<Move> BestMoves= new ArrayList<>();

        for(int i=0 ;i<numbers.size();i++){
            int thisValue=numbers.get(i).value;
            if(thisValue==minvalue){
                BestMoves.add(numbers.get(i));
            }
        }

        //randomly select a best move from the list and store it in a move to return

        Move bestMove= BestMoves.get(rand.nextInt(BestMoves.size()));


        // System.out.println("The value of the best Even Move is "+minvalue+" and choice is "+bestMove.number+" at row "+bestMove.row+" col "+bestMove.col);

        //return the random best move
        return bestMove;
    }

    // print the message if ai wins

    private void printYouLost(){
        System.out.println("--------------------------------------------");
        System.out.println("|                                          |");
        System.out.println("              You Human Lost!               ");
        System.out.println("|                                          |");
        System.out.println("--------------------------------------------");
    }

    // initiate the game

    public void startGame(){

        //populate board originally with 0's
        populateBoard();

        //making available choices
        populateChoices();

        //take the input of any particular stage from user
        takeInput();

        startTime= System.currentTimeMillis();

        // finding the depth of the game at user given stage given by user
        int depth= findDepth();

        // Check if there are no more moves left, if yes then terminate the program
        checkMovesLeft(depth);

        // ai will go for the move depending on input depth and add the next move values to the board
        if(depth%2==0){
            Move bestMove= playEven(input,depth);
            addToBoard(bestMove.row, bestMove.col, bestMove.number);
        }
        else{  // go for the odd
            Move bestMove= playOdd(input,depth);
            addToBoard(bestMove.row, bestMove.col, bestMove.number);
        }


//         Move bestMove= playOdd(input,depth);
//        Move bestMove= playEven(input,depth);

        endTime= System.currentTimeMillis();

        //adding the next move values to the board
//        addToBoard(bestMove.row, bestMove.col, bestMove.number);

        printComputations();
        printDuration();

        printBoard();

        // evaluate if there is a winner
        boolean win= evaluate(board);

        // if ai wins print You Lost
        if(win){
            printYouLost();
        }
        else if(depth==10){
            // Check if there are no more moves left, if yes then terminate the program
            checkMovesLeft(depth);
        }
        else{
            for(int j=0; j<9; j++){

                System.out.println("Enter the board values give by AI and give your next move as well");
                System.out.println();

                //take the input of any particular stage from user
                takeInput();


                startTime= System.currentTimeMillis();

                // finding the depth of the game at a particular stage
                depth= findDepth();

                // Check if there are no more moves left, if yes then terminate the program
                checkMovesLeft(depth);

                // getting the best next move from the ai player

                // ai will go for the move depending on input depth and add the next move values to the board
                if(depth%2==0){
                    Move bestMove= playEven(input,depth);
                    addToBoard(bestMove.row, bestMove.col, bestMove.number);
                }
                else{  // go for the odd
                    Move bestMove= playOdd(input,depth);
                    addToBoard(bestMove.row, bestMove.col, bestMove.number);
                }

//                 bestMove= playOdd(input,depth);
//                bestMove= playEven(input,depth);

                endTime= System.currentTimeMillis();

                //adding the next move values to the board
//                addToBoard(bestMove.row, bestMove.col, bestMove.number);

                printComputations();
                printDuration();

                printBoard();

                // evaluate if there is a winner
                win= evaluate(board);

                if(win){
                    printYouLost();
                    break;
                }
                // Check if there are no more moves left, if yes then terminate the program
                checkMovesLeft(depth);
            }
        }
        sc.close();
    }
}
