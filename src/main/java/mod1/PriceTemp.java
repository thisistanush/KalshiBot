package mod1;

import java.util.ArrayList;

public class PriceTemp {
    private double topDollarsLevel;
    private double bottomDollarsLevel;
    private double numberOfWins = 0;
    private double totalNumberOfHits = 0;
    ArrayList<Double> liquidityVolume = new ArrayList<>();

    public PriceTemp(double bottomDollarLevel, double topDollarLevel) {
        this.topDollarsLevel = topDollarLevel;
        this.bottomDollarsLevel = bottomDollarLevel;
    }

    public double getTopDollarsLevel() {
        return topDollarsLevel;
    }

    public double getBottomDollarsLevel() {
        return bottomDollarsLevel;
    }

    public double getNumberOfWins() {
        return numberOfWins;
    }

    public double getTotalNumberOfHits() {
        return totalNumberOfHits;
    }

    public void addNumberOfWins() {
        numberOfWins++;
    }

    public void addTotalNumberOfHits() {
        totalNumberOfHits++;
    }

    public void addLiquidityVolume(double volume) {
        liquidityVolume.add(volume);
    }

    public Double getLiquidityVolume() {
        if (liquidityVolume.isEmpty()) return 0.0;
        double num = 0;
        for (int i = 0; i < liquidityVolume.size(); i++) {
            num += liquidityVolume.get(i);
        }
        return num / liquidityVolume.size();
    }

    @Override
    public String toString() {
        double winRate = (totalNumberOfHits == 0) ? 0.0 : (numberOfWins / totalNumberOfHits);
        double edge    = winRate - ((topDollarsLevel + bottomDollarsLevel) / 2.0);

        return "priceInterval=" + bottomDollarsLevel + "-" + topDollarsLevel
                + " | hits=" + (int) totalNumberOfHits
                + " | wins=" + (int) numberOfWins
                + " | actualWinRate=" + String.format("%.4f", winRate)
                + " | edge=" + String.format("%.4f", edge)
                + " | avgLiquidity=" + String.format("%.2f", getLiquidityVolume());
    }
}