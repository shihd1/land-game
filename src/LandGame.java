public class LandGame {
    //length*width < 60
    final static int length = 6;
    final static int width = 2;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    static boolean[][] playerchoices = new boolean[length][width];
    static boolean[][] computerchoices = new boolean[length][width];
    static boolean[][] playerLand = new boolean[length][width];
    static boolean[][] computerLand = new boolean[length][width];
    static int[][] board = new int[length][width];
    static char[][] ABCboard = new char[length][width];
    static int[][] intboard = new int[length][width];
    static boolean computerDead = false;
    public static void main(String[] args) {
        initializationOfABCboard();
        initializationOfIntBoard();
        printInstructions();
        printBoard();

        playerFirstChoice();
        printBoard();
        pressEnterToContinue();

        computerFirstChoice();
        printBoard();
        pressEnterToContinue();

        showNearbyTowns();
        printBoard();
        pressEnterToContinue();

        //StartGame
        boolean winOrNot = playGame();

        //win or not
        if(winOrNot == false){
            System.out.println("You lose!");
        }else{
            System.out.println("You win!");
        }
    }
    static void initializationOfABCboard(){
        char ch = 'A';
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < length; j++) {
                ABCboard[j][i] = ch;
                ch++;
            }
        }
    }
    static void initializationOfIntBoard(){
        int count = 0;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                intboard[i][j] = count++;
            }
        }
    }
    static void playerFirstChoice(){
        System.out.println("Choose a box to start at by entering in a letter");
        String boxStartAt = getLetter.getLetter(length*width);
        DicePair p = new DicePair();
        System.out.println("Press enter to roll");
        PressToContinue.pressToContinue();
        int total = p.roll();
        System.out.println("You rolled a total of " + total);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < length; j++) {
                if(ABCboard[j][i] == boxStartAt.charAt(0)){
                    System.out.println("You took over box "+boxStartAt+" with an attack value of "+total);
                    board[j][i] = total;
                    playerLand[j][i] = true;
                    addChoicesForPlayer(j, i);
                    break;
                }
            }
        }
    }
    static void computerFirstChoice(){
        DicePair p = new DicePair();
        int compTotal = p.roll();
        String a = "";
        boolean notBeside = false;
        int y = 0;
        do{
            y = (int) (Math.random()*length*width);
            for (int i = 0; i < playerchoices.length; i++) {
                for (int j = 0; j < playerchoices[i].length; j++) {
                    if(playerchoices[i][j] == true || playerLand[i][j] == true) {
                        if (intboard[i][j] == y) {
                            notBeside = true;
                        }
                    }
                }
            }
        }while (notBeside);
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                if(intboard[i][j] == y){
                    System.out.println("The computer rolls a total of "+compTotal);
                    board[i][j] = compTotal;
                    computerLand[i][j] = true;
                    addChoicesForComputer(i,j);
                }
            }
        }
    }
    static void showNearbyTowns(){
        System.out.println("These are the towns nearby:");
        Die d = new Die(8);
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                if(board[i][j] == 0){
                    board[i][j] = d.roll();
                }
            }
        }
    }
    static void pressEnterToContinue(){
        System.out.println("Press enter to continue");
        PressToContinue.pressToContinue();
    }
    static boolean playGame(){
        organizeChoices(true);
        System.out.println("The options of what you can do are:");
        System.out.println("1. Attack \n2. Send scouts \n3. Stay \n4. Surrender ");
        int choice = getInt.getInt();
        switch (choice){
            case 1:
                playerAttack();
                System.out.println("Press enter to roll to end your turn.");
                PressToContinue.pressToContinue();
                endTurn(true);
                printBoard();
                break;
            case 2:
                playerScout();
                System.out.println("Press enter to roll to end your turn.");
                PressToContinue.pressToContinue();
                endTurn(true);
                printBoard();
                break;
            case 3:
                System.out.println("Press enter to roll to end your turn.");
                PressToContinue.pressToContinue();
                endTurn(true);
                printBoard();
                break;
            case 4:
                return false;
            default:
                return playGame();
        }
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                if(playerLand[i][j] == true){
                    computerLand[i][j] = false;
                }
            }
        }
        if(computerDead == true){
            return true;
        }
        organizeChoices(false);
        computerAttack();
        endTurn(false);
        System.out.println("The computer rolls to end its turn");
        printBoard();
        if(checkIfPlayerAlive() == false){
            return false;
        }
        if(playGame() == true){
            return true;
        }else{
            return false;
        }
    }
    static int computerAttack(){
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                if(computerchoices[i][j] == true && computerLand[i][j] == true){
                    computerchoices[i][j] = false;
                }
            }
        }
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                if(computerchoices[i][j] == true){
                    int[] temp = new int[2];
                    temp[0] = i;
                    temp[1] = j;
                    int[] computer = getAttackOrSendFromLand(temp, false);
                    if(computer != null) {
                        int x = computer[0];
                        int y = computer[1];
                        if(board[i][j] <= board[x][y]){
                            System.out.println("The computer attacks and takes over land.");
                            computerLand[i][j] = true;
                            playerLand[i][j] = false;
                            board[x][y] = board[x][y]-board[i][j];
                            addChoicesForComputer(i,j);
                            computerchoices[i][j] = false;
                            organizeChoices(true);
                            organizeChoices(false);
                            printBoard();
                            return 0;
                        }
                    }
                }
            }
        }
        System.out.println("The computer chooses to stay for this turn");
        printBoard();
        return 0;
    }
    static void playerAttack() {
        String choice = checkAndValidateLand("Which direction do you want to attack? (Choose in the purple)", true);
        int[] attackChoice = getIntegerCoordinates(choice);
        int[] attackLand = getAttackOrSendFromLand(attackChoice,true);
        String boxToAttackFrom;
        if(attackLand == null){
            boxToAttackFrom = checkAndValidateLand("Which land do you want to send your troops?", false);
        }else {
            boxToAttackFrom = getLetterCoordinates(attackLand);
        }
        int[] attackFrom = getIntegerCoordinates(boxToAttackFrom);
        if(board[attackFrom[0]][attackFrom[1]] >= board[attackChoice[0]][attackChoice[1]]){
            System.out.println("You took over their land!");
            playerLand[attackChoice[0]][attackChoice[1]] = true;
            addChoicesForPlayer(attackChoice[0],attackChoice[1]);
            computerchoices[attackChoice[0]][attackChoice[1]] = true;
            board[attackFrom[0]][attackFrom[1]]-=board[attackChoice[0]][attackChoice[1]];
            printBoard();
            if(computerLand[attackChoice[0]][attackChoice[1]] == true){
                computerLand[attackChoice[0]][attackChoice[1]] = false;
                boolean computerStillAlive = false;
                for (int i = 0; i < computerLand.length; i++) {
                    for (int j = 0; j < computerLand[i].length; j++) {
                        if(computerLand[i][j] == true){
                            computerStillAlive = true;
                        }
                    }
                }
                if(computerStillAlive == false){
                    computerDead = true;
                }
            }
        }else{
            System.out.println("You lost the battle!");
            board[attackChoice[0]][attackChoice[1]]-=board[attackFrom[0]][attackFrom[1]];
            board[attackFrom[0]][attackFrom[1]] = 0;
        }
    }
    static void playerScout(){
        String s = checkAndValidateLand("Which land do you want to send your scout?", true);
        int[] newLand = getIntegerCoordinates(s);
        int x = newLand[0];
        int y = newLand[1];
        if(Math.random()>0.7){
            System.out.println("Your neighbor is friendly! They surrender");
            playerLand[x][y] = true;
            addChoicesForPlayer(x,y);
            printBoard();
            System.out.println("Press enter to continue");
            PressToContinue.pressToContinue();
        }else{
            System.out.println("You neighbor is hostile! They killed your scout");
            String letter = getLetterCoordinates(getAttackOrSendFromLand(newLand,true));
            int[] yourLand = getIntegerCoordinates(letter);
            board[yourLand[0]][yourLand[1]]--;
            printBoard();
            System.out.println("Press enter to continue");
            PressToContinue.pressToContinue();
        }
    }
    static String checkAndValidateLand(String question, boolean AttackOrScout){
        System.out.println(question);
        showChoices();
        System.out.println("Please enter in a letter");
        String choice = getLetter.getLetter(length*width);
        int[] coordinateChoice = getIntegerCoordinates(choice);
        int x = coordinateChoice[0];
        int y = coordinateChoice[1];
        if(AttackOrScout == true) {
            if (playerLand[x][y] == true) {
                System.out.println("Please enter a letter that's not your land");
                return checkAndValidateLand(question, true);
            }
            if (playerchoices[x][y] == true) {
                return getLetterCoordinates(coordinateChoice);
            }
        }else{
            if (playerLand[x][y] == true) {
                return getLetterCoordinates(coordinateChoice);
            }
            if (playerchoices[x][y] == true) {
                System.out.println("Please enter a letter that's your land");
                return checkAndValidateLand(question, true);
            }
        }
        return null;
    }
    static int[] getAttackOrSendFromLand(int[] coordinate, boolean isPlayer){
        int x = coordinate[0];
        int y = coordinate[1];
        int[] newCoordinate = new int[2];
        int count = 0;
        if(isPlayer) {
            if (x + 1 < length) {
                if (playerLand[x + 1][y] == true) {
                    newCoordinate[0] = x + 1;
                    newCoordinate[1] = y;
                    count++;
                }
            }
            if (x - 1 >= 0) {
                if (playerLand[x - 1][y] == true) {
                    newCoordinate[0] = x-1;
                    newCoordinate[1] = y;
                    count++;
                }
            }
            if (y + 1 < width) {
                if (playerLand[x][y + 1] == true) {
                    newCoordinate[0] = x;
                    newCoordinate[1] = y+1;
                    count++;
                }
            }
            if (y - 1 >= 0) {
                if (playerLand[x][y - 1] == true) {
                    newCoordinate[0] = x;
                    newCoordinate[1] = y-1;
                    count++;
                }
            }
        }else{
            if (x + 1 < length) {
                if (computerLand[x + 1][y] == true) {
                    newCoordinate[0] = x + 1;
                    newCoordinate[1] = y;
                    return newCoordinate;
                }
            }
            if (x - 1 >= 0) {
                if (computerLand[x - 1][y] == true) {
                    newCoordinate[0] = x-1;
                    newCoordinate[1] = y;
                    return newCoordinate;
                }
            }
            if (y + 1 < width) {
                if (computerLand[x][y + 1] == true) {
                    newCoordinate[0] = x;
                    newCoordinate[1] = y+1;
                    return newCoordinate;
                }
            }
            if (y - 1 >= 0) {
                if (computerLand[x][y - 1] == true) {
                    newCoordinate[0] = x;
                    newCoordinate[1] = y-1;
                    return newCoordinate;
                }
            }
        }
        if(count == 1){
            return newCoordinate;
        }else{
            return null;
        }
    }
    static void endTurn(boolean isPlayer){
        Die d = new Die();
        if(isPlayer) {
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < width; j++) {
                    if (playerLand[i][j] == true) {
                        board[i][j] += d.roll();
                    }
                }
            }
        }else{
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < width; j++) {
                    if (computerLand[i][j] == true) {
                        board[i][j] += d.roll();
                    }
                }
            }
        }
    }
    static int[] getIntegerCoordinates(String letter){
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                if(letter.charAt(0) == ABCboard[i][j]){
                    int[] coordinate = new int[2];
                    coordinate[0] = i;
                    coordinate[1] = j;
                    return coordinate;
                }
            }
        }
        return null;
    }
    static String getLetterCoordinates(int[] coordinates){
        int l = coordinates[0];
        int w = coordinates[1];
        char a = ABCboard[l][w];
        return a+"";
//        if((coordinates[0] == 0) && (coordinates[1] == 0)){
//            return "A";
//        }else if((coordinates[0] == 1) && (coordinates[1] == 0)){
//            return "B";
//        }else if((coordinates[0] == 2) && (coordinates[1] == 0)){
//            return "C";
//        }else if((coordinates[0] == 3) && (coordinates[1] == 0)){
//            return "D";
//        }else if((coordinates[0] == 0) && (coordinates[1] == 1)){
//            return "E";
//        }else if((coordinates[0] == 1) && (coordinates[1] == 1)){
//            return "F";
//        }else if((coordinates[0] == 2) && (coordinates[1] == 1)){
//            return "G";
//        }else if((coordinates[0] == 3) && (coordinates[1] == 1)){
//            return "H";
//        }
//        return null;
    }
    static void printInstructions(){
        System.out.println("Here are the game rules: ");
        System.out.println("You play against a computer.");
        System.out.println("You can chose an empty square and roll a dice to take over it");
        System.out.println("But when you meet a square that's not yours, then you take over it by using a bigger dice number from a square that's next to it and yours");
    }
    static void addChoicesForPlayer(int j, int i){
        if(j+1 < length){
            playerchoices[j+1][i] = true;
        }
        if(j-1 >= 0){
            playerchoices[j-1][i] = true;
        }
        if(i+1 < width){
            playerchoices[j][i+1] = true;
        }
        if(i-1 >= 0){
            playerchoices[j][i-1] = true;
        }
    }
    static void addChoicesForComputer(int j, int i){
        if(j+1 < length){
            computerchoices[j+1][i] = true;
        }
        if(j-1 >= 0){
            computerchoices[j-1][i] = true;
        }
        if(i+1 < width){
            computerchoices[j][i+1] = true;
        }
        if(i-1 >= 0){
            computerchoices[j][i-1] = true;
        }
    }
    static void organizeChoices(boolean isPlayer){
        if(isPlayer == true){
            playerchoices = new boolean[length][width];
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < width; j++) {
                    if(playerLand[i][j] == true){
                        addChoicesForPlayer(i,j);
                    }
                }
            }
        }else{
            computerchoices = new boolean[length][width];
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < width; j++) {
                    if(computerLand[i][j] == true){
                        addChoicesForComputer(i,j);
                    }
                }
            }
        }
    }
    static void showChoices(){
        for (int i = 0; i < length ; i++) {
            System.out.print(" ------");
        }
        System.out.println();
        for (int j = 0; j < width; j++) {
            System.out.print("|");
            for (int i = 0; i < length; i++) {
                System.out.print(ABCboard[i][j]+"     |");
            }
            System.out.println();
            System.out.print("| ");
            for (int i = 0; i < length; i++) {
                if (playerLand[i][j] == true) {
                    if(board[i][j]>9) {
                        System.out.print(" " + ANSI_BLUE + board[i][j] + ANSI_RESET + "  | ");
                    }else{
                        System.out.print("  " + ANSI_BLUE + board[i][j] + ANSI_RESET + "  | ");
                    }
                } else if (computerLand[i][j] == true){
                    if(board[i][j]>9) {
                        System.out.print(" " + ANSI_RED + board[i][j] + ANSI_RESET + "  | ");
                    }else{
                        System.out.print("  " + ANSI_RED + board[i][j] + ANSI_RESET + "  | ");
                    }
                }else if(playerchoices[i][j] == true){
                    if(board[i][j]>9) {
                        System.out.print(" " + ANSI_PURPLE + board[i][j] + ANSI_RESET + "  | ");
                    }else{
                        System.out.print("  " + ANSI_PURPLE + board[i][j] + ANSI_RESET + "  | ");
                    }
                }else{
                    if(board[i][j]>9) {
                        System.out.print(" " + board[i][j] + "  | ");
                    }else{
                        System.out.print("  " + board[i][j] + "  | ");
                    }
                }
            }
            System.out.println();
            System.out.print("|");
            for (int i = 0; i < length ; i++) {
                System.out.print("      |");
            }
            System.out.println();
            for (int i = 0; i < length ; i++) {
                System.out.print(" ------");
            }
            System.out.println();
        }
    }
    static boolean checkIfPlayerAlive(){
        boolean playerAlive = false;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                if(playerLand[i][j] == true){
                    playerAlive = true;
                }
            }
        }
        return playerAlive;
    }
    static void printBoard() {
        for (int i = 0; i < length; i++) {
            System.out.print(" ------");
        }
        System.out.println();
        for (int j = 0; j < width; j++) {
            System.out.print("|");
            for (int i = 0; i < length; i++) {
                System.out.print(ABCboard[i][j] + "     |");
            }
            System.out.println();
            System.out.print("| ");
            for (int i = 0; i < length; i++) {
                if (playerLand[i][j] == true) {
                    if (board[i][j] > 9) {
                        System.out.print(" " + ANSI_BLUE + board[i][j] + ANSI_RESET + "  | ");
                    } else {
                        System.out.print("  " + ANSI_BLUE + board[i][j] + ANSI_RESET + "  | ");
                    }
                } else if (computerLand[i][j] == true) {
                    if (board[i][j] > 9) {
                        System.out.print(" " + ANSI_RED + board[i][j] + ANSI_RESET + "  | ");
                    } else {
                        System.out.print("  " + ANSI_RED + board[i][j] + ANSI_RESET + "  | ");
                    }
                } else {
                    if (board[i][j] > 9) {
                        System.out.print(" " + board[i][j] + "  | ");
                    } else {
                        System.out.print("  " + board[i][j] + "  | ");
                    }
                }
            }
            System.out.println();
            System.out.print("|");
            for (int i = 0; i < length; i++) {
                System.out.print("      |");
            }
            System.out.println();
            for (int i = 0; i < length; i++) {
                System.out.print(" ------");
            }
            System.out.println();
        }
    }
}
