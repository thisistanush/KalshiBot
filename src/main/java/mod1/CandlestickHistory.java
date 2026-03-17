package mod1;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import java.util.ArrayList;

public class CandlestickHistory {
    int candleNumber;

    // YES side: price >= 0.5, we predict YES wins
    int yesHits = 0;
    int yesWins = 0;

    // NO side: price < 0.5, we predict NO wins
    int noHits = 0;
    int noWins = 0;

    // reversal tracking: price was YES-side but crossed below 0.5 before expiry
    int yesReversals = 0;
    // reversal tracking: price was NO-side but crossed above 0.5 before expiry
    int noReversals = 0;

    ArrayList<Double> yesReversalMagnitudes = new ArrayList<>();
    ArrayList<Double> noReversalMagnitudes  = new ArrayList<>();

    public CandlestickHistory(int candleNumber) {
        this.candleNumber = candleNumber;
    }

    // --- YES side ---
    public void addYesHit() {
        yesHits++;
    }

    public void addYesWin() {
        yesWins++;
    }

    // --- NO side ---
    public void addNoHit() {
        noHits++;
    }

    public void addNoWin() {
        noWins++;
    }

    // --- Reversals ---
    public void addYesReversal(double magnitude) {
        yesReversals++;
        yesReversalMagnitudes.add(magnitude);
    }

    public void addNoReversal(double magnitude) {
        noReversals++;
        noReversalMagnitudes.add(magnitude);
    }

    // --- Getters ---
    public int getYesHits()     { return yesHits; }
    public int getYesWins()     { return yesWins; }
    public int getNoHits()      { return noHits; }
    public int getNoWins()      { return noWins; }
    public int getYesReversals(){ return yesReversals; }
    public int getNoReversals() { return noReversals; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("============================================================\n");
        sb.append("Candle #").append(candleNumber).append("\n");
        sb.append("------------------------------------------------------------\n");

        // YES side accuracy
        int totalHits = yesHits + noHits;
        int totalWins = yesWins + noWins;

        double overallAccuracy = 0.0;
        if (totalHits > 0) {
            overallAccuracy = (double) totalWins / totalHits;
        }

        double yesAccuracy = 0.0;
        if (yesHits > 0) {
            yesAccuracy = (double) yesWins / yesHits;
        }

        double noAccuracy = 0.0;
        if (noHits > 0) {
            noAccuracy = (double) noWins / noHits;
        }

        sb.append("  [Overall]  hits=").append(totalHits)
                .append("  wins=").append(totalWins)
                .append("  accuracy=").append(String.format("%.4f", overallAccuracy)).append("\n");

        sb.append("  [YES side] hits=").append(yesHits)
                .append("  wins=").append(yesWins)
                .append("  accuracy=").append(String.format("%.4f", yesAccuracy)).append("\n");

        sb.append("  [NO  side] hits=").append(noHits)
                .append("  wins=").append(noWins)
                .append("  accuracy=").append(String.format("%.4f", noAccuracy)).append("\n");

        // Reversal stats - YES side
        double yesReversalRate = 0.0;
        if (yesHits > 0) {
            yesReversalRate = (double) yesReversals / yesHits;
        }

        double noReversalRate = 0.0;
        if (noHits > 0) {
            noReversalRate = (double) noReversals / noHits;
        }

        sb.append("  [YES reversals] count=").append(yesReversals)
                .append("  rate=").append(String.format("%.4f", yesReversalRate));

        if (yesReversalMagnitudes.size() > 0) {
            DescriptiveStatistics yesStats = new DescriptiveStatistics();
            for (int i = 0; i < yesReversalMagnitudes.size(); i++) {
                yesStats.addValue(yesReversalMagnitudes.get(i));
            }
            sb.append("  mean_magnitude=").append(String.format("%.4f", yesStats.getMean()))
                    .append("  sd_magnitude=").append(String.format("%.4f", yesStats.getStandardDeviation()));
        }
        sb.append("\n");

        sb.append("  [NO  reversals] count=").append(noReversals)
                .append("  rate=").append(String.format("%.4f", noReversalRate));

        if (noReversalMagnitudes.size() > 0) {
            DescriptiveStatistics noStats = new DescriptiveStatistics();
            for (int i = 0; i < noReversalMagnitudes.size(); i++) {
                noStats.addValue(noReversalMagnitudes.get(i));
            }
            sb.append("  mean_magnitude=").append(String.format("%.4f", noStats.getMean()))
                    .append("  sd_magnitude=").append(String.format("%.4f", noStats.getStandardDeviation()));
        }
        sb.append("\n");

        return sb.toString();
    }
}