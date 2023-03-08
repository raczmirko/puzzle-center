package hu.okrim.puzzlecenter;

public class DataEntryModel {
    private int solveTime;
    private String puzzle;
    private String dateString;

    public DataEntryModel(String date, String puzzle, int solveTime) {
        this.dateString = date;
        this.puzzle = puzzle;
        this.solveTime = solveTime;
    }

    @Override
    public String toString() {
        return "DataEntryModel{" +
                ", solveTime=" + solveTime +
                ", puzzle='" + puzzle + '\'' +
                ", date=" + dateString +
                '}';
    }

    public int getSolveTime() {
        return solveTime;
    }

    public String getPuzzle() {
        return puzzle;
    }

    public String getDateString() {
        return dateString;
    }
}
