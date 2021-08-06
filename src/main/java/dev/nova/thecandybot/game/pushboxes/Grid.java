package dev.nova.thecandybot.game.pushboxes;


public class Grid {

    private final int width;
    private final int height;
    private final int positionsAmount;
    private final Difficulty difficulty;
    /*
    Goals and boxes should be always sync.
     */
    private final int goalsAmount;
    private final int boxesAmount;

    public Grid(int width, int height,Difficulty difficulty) throws GridMakingException{
        /*
        3x4 area means 12 positions.
         */

        if(width <= 1){
            throw new GridMakingException("Width cannot be smaller or equal to 1!");
        }
        if(height <= 1){
            throw new GridMakingException("Height cannot be smaller or equal to 1!");
        }

        this.width = width;
        this.height = height;

        this.positionsAmount = width*height;

        this.difficulty = difficulty;
        
        this.goalsAmount = setupGoals();
        this.boxesAmount = setupBoxes();


    }

    private int setupBoxes() {
        int i = 1;
        switch (difficulty){
            case NORMAL:

                break;
            case EASY:
            default:
                i = 1;
                break;
        }
        return i;
    }

    private int setupGoals() {
        int i = 1;
        return i;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getPositionsAmount() {
        return positionsAmount;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public static class Position {

        private final int x,y;

        public Position(int x, int y){
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    public enum PositionType {



    }

    public enum Difficulty {

        EASY,
        NORMAL,
        HARD

    }

    public static class GridMakingException extends Exception {
        public GridMakingException(String str){
            super(str);
        }
    }
}
