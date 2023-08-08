import java.util.Random;
import java.util.Scanner;

public class GameApp {

    public static void main(String[] args) {
        Game game = new Game();

        game.startGame();


    }

    public static class Game{

        String player1Name;

        String player2Name;

        boolean isPlayer1Turn = false;

        boolean isDumbAIActive = false;

        int placementCount;

        int[] columnTotals = new int[7];

        //Matrix with 6 rows, 7 columns
        char[][] gameArray = {
                {'_', '_', '_', '_', '_', '_', '_'},
                {'_', '_', '_', '_', '_', '_', '_'},
                {'_', '_', '_', '_', '_', '_', '_'},
                {'_', '_', '_', '_', '_', '_', '_'},
                {'_', '_', '_', '_', '_', '_', '_'},
                {'_', '_', '_', '_', '_', '_', '_'}
        };

        public void startGame(){
            setUpGame();
        }

        private void setUpGame(){

            playAgainstDumbAI();

            if(!isDumbAIActive){
                player1Name = setPlayerName();
                player2Name = setPlayerName();
            }
            else{
                player1Name = setPlayerName();
                player2Name = "Dumb AI";
            }

            System.out.println("(Randomizing...)");

            isPlayer1Turn = (Math.random() <= 0.5) ? true : false;

            System.out.println("It's " + getCurrPlayer() + "'s turn.");

            displayGrid();
            if(isDumbAIActive){
                if(!isPlayer1Turn){
                    dumbAIChooseColumn();
                    return;
                }
            }
            chooseColumn();
        }

        private void playAgainstDumbAI(){
            Scanner in = new Scanner(System.in);
            System.out.println("Would you like to play against a dumb AI? [y/n]");

            String input = in.nextLine();

            if(input.equals("y") || input.equals("n")){
                isDumbAIActive = input.equals("y");
            }
            else {
                System.out.println("Invalid response.");

                playAgainstDumbAI();
                return;
            }
        }

        private String setPlayerName(){
            Scanner in = new Scanner(System.in);

            String currPlayer = !isPlayer1Turn ? "Player #1" : "Player #2";

            System.out.println(currPlayer + ", enter your name: ");

            String playerName = in.nextLine();

            while (playerName.length() < 1) {
                System.out.println("Invalid name. Try again.");
                playerName = in.nextLine();
            }

            if(!isDumbAIActive){
                changePlayer();
            }

            return playerName;
        }

        private void displayGrid(){

            String matrixString = "";

            for (int i = 0; i < gameArray.length; i++)
            {
                // length returns number of rows
                for (int j = 0; j < gameArray[i].length; j++)
                {
                    // here length returns number of columns corresponding to current row
                    // using tabs for equal spaces, looks better aligned
                    // matrix[i][j] will return each element placed at row ‘i',column 'j'
                    matrixString += gameArray[i][j] + " ";
                }
                matrixString += "\n";
            }
            System.out.println(matrixString);
        }

        private void chooseColumn(){
            Scanner in = new Scanner(System.in);

            System.out.println(getCurrPlayer() + ", choose a column: ");
            String inputNum = in.nextLine();
            if(!isInteger(inputNum)){
                System.out.println("That's not a valid column.");
                chooseColumn();
                return;
            }

            int chosenColumn = Integer.parseInt(inputNum);

            if(chosenColumn < 0 || chosenColumn > 7){
                System.out.println("That's not a valid column.");
                chooseColumn();
                return;
            }

            tryPlacePiece(chosenColumn);
        }

        private void tryPlacePiece(int column){
            boolean isPlacementSuccess = false;
            column--;

            int row = 0;

            if(columnTotals[column] == 6){
                System.out.println("That's not a valid column.");
                chooseColumn();
                return;
            }

            for (int i = gameArray.length -1; i >= 0 ; i--)
            {

                if(gameArray[i][column] == '_'){
                    gameArray[i][column] = (isPlayer1Turn) ? 'X' : 'O';
                    isPlacementSuccess = true;
                    row = i;
                    break;
                }
            }

            if(!isPlacementSuccess){
                System.out.println("That's not a valid column.");
                chooseColumn();
                return;
            }


            columnTotals[column]++;
            displayGrid();
            CheckGameState(row, column);
        }

        private void changePlayer(){
            if(isPlayer1Turn){
                isPlayer1Turn = false;
            }
            else {
                isPlayer1Turn = true;
            }

            if(isDumbAIActive){
                if(!isPlayer1Turn){
                    dumbAIChooseColumn();
                }
            }
        }

        private String getCurrPlayer(){
            return (isPlayer1Turn) ? player1Name : player2Name;
        }

        private void CheckGameState(int row, int column){
            placementCount++;

            if(placementCount >= 42){
                //Draw game
                drawGame();
            }

            //The following code is horrible and should be optimized lol

            int horizontalWin = 1;
            int verticalWin = 1;
            int downLeftDiagWin = 1;
            int downRightDiagWin = 1;

            char piece = (isPlayer1Turn) ? 'X' : 'O';

            int num = 0;
            int currRow = row-1;
            int currColumn = column;

            while(currRow > -1 && currRow < 6 && currColumn > -1 && currColumn < 7){
                if(piece != gameArray[currRow][currColumn]){
                    break;
                }

                verticalWin++;
                num++;
                if(num > 3){
                    break;
                }

                currRow--;
            }
            //Vertical down check
            num = 0;
            currRow = row+1;
            currColumn = column;

            while(currRow > -1 && currRow < 6 && currColumn > -1 && currColumn < 7){
                if(piece != gameArray[currRow][currColumn]){
                    break;
                }

                verticalWin++;
                num++;
                if(num > 3){
                    break;
                }

                currRow++;
            }

            //Horizontal right check
            num = 0;
            currRow = row;
            currColumn = column+1;

            while(currRow > -1 && currRow < 6 && currColumn > -1 && currColumn < 7){
                if(piece != gameArray[currRow][currColumn]){
                    break;
                }

                verticalWin++;
                num++;
                if(num > 3){
                    break;
                }

                currColumn++;
            }
            //Horizontal left check
            num = 0;
            currRow = row;
            currColumn = column-1;

            while(currRow > -1 && currRow < 6 && currColumn > -1 && currColumn < 7){
                if(piece != gameArray[currRow][currColumn]){
                    break;
                }

                verticalWin++;
                num++;
                if(num > 3){
                    break;
                }

                currColumn--;
            }

            num = 0;
            currRow = row+1;
            currColumn = column+1;
            //Check top left
            while(currRow > -1 && currRow < 6 && currColumn > -1 && currColumn < 7){
                if(piece != gameArray[currRow][currColumn]){
                    break;
                }

                downRightDiagWin++;
                num++;
                if(num > 3){
                    break;
                }

                currRow--;
                currColumn--;
            }

            num = 0;
            currRow = row-1;
            currColumn = column-1;
            //Check down right
            while(currRow > -1 && currRow < 6 && currColumn > -1 && currColumn < 7){
                if(piece != gameArray[currRow][currColumn]){
                    break;
                }

                downRightDiagWin++;

                if(num > 3){
                    break;
                }

                currRow++;
                currColumn++;
            }

            num = 0;
            currRow = row-1;
            currColumn = column+1;
            //Check top right
            while(currRow > -1 && currRow < 6 && currColumn > -1 && currColumn < 7){
                if(piece != gameArray[currRow][currColumn]){
                    break;
                }

                downLeftDiagWin++;

                if(num > 3){
                    break;
                }

                currRow--;
                currColumn++;
            }

            num = 0;
            currRow = row+1;
            currColumn = column-1;
            //Check down left
            while(currRow > -1 && currRow < 6 && currColumn > -1 && currColumn < 7){
                if(piece != gameArray[currRow][currColumn]){
                    break;
                }

                downLeftDiagWin++;

                if(num > 3){
                    break;
                }
                currRow++;
                currColumn--;
            }

            if(horizontalWin >= 4 || verticalWin >= 4 || downLeftDiagWin >= 4 || downRightDiagWin >= 4){
                //Player wins
                //System.out.printf("Horizontal Win: %s, Vertical Win: %s, Down Left Diag Win: %s, Down Right Diag Win: %s", horizontalWin, verticalWin, downLeftDiagWin, downRightDiagWin);
                winGame();
                return;
            }

            changePlayer();
            chooseColumn();
        }

        private void winGame(){
            Scanner in = new Scanner(System.in);

            System.out.println(getCurrPlayer() +  " Wins!");
            System.out.println("\n" +
                    "Play again? [y/n]: ");

            String input = in.nextLine();

            if(input.equals("y") || input.equals("n")){
                boolean gameReset = input.equals("y");

                if(gameReset){
                    resetGame();
                }
                else {
                    return;
                }
            }
            else {
                System.out.println("Invalid response.");
                winGame();
                return;
            }


        }

        private void drawGame(){
            Scanner in = new Scanner(System.in);

            System.out.println("The game is a draw!");
            System.out.println("\n" +
                    "Play again? [y/n]: ");

            String input = in.nextLine();

            if(input.equals("y") || input.equals("n")){
                boolean gameReset = input.equals("y");

                if(gameReset){
                    resetGame();
                }
                else {
                    return;
                }
            }
            else {
                System.out.println("Invalid response.");
                drawGame();
                return;
            }
        }

        private void resetGame(){
            //Matrix with 6 rows, 7 columns
            for (int i = 0; i < gameArray.length; i++)
            {
                // length returns number of rows
                for (int j = 0; j < gameArray[i].length; j++)
                {
                    // here length returns number of columns corresponding to current row
                    // using tabs for equal spaces, looks better aligned
                    // matrix[i][j] will return each element placed at row ‘i',column 'j'
                    gameArray[i][j] = '_';
                }
            }
            placementCount = 0;
            isPlayer1Turn = false;
            isDumbAIActive = false;
            setUpGame();
        }

        private static boolean isInteger(String s) {
            try {
                Integer.parseInt(s);
            } catch(NumberFormatException e) {
                return false;
            } catch(NullPointerException e) {
                return false;
            }
            // only got here if we didn't return false
            return true;
        }

        private void dumbAIChooseColumn(){
            int min = -1;
            int max = 6;

            for(int i = 0; i < columnTotals.length; i++){
                if(columnTotals[i] != 6){
                    if(min == -1){
                        min = i;
                        System.out.println(min);
                    }
                    max = i;
                    System.out.println("Max: " + max);
                }
            }

            Random rand = new Random();



            //Choose a random number between min and max
            int randomNum = rand.nextInt(max-min) + min;
            System.out.println(randomNum);
            tryPlacePiece(randomNum);
        }
    }


}
