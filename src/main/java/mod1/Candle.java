package mod1;

import java.util.ArrayList;
import java.util.HashMap;

public class Candle {
    public static class Hits{
        int numberOfHits;
        int numberOfWins;

        public Hits(int numberOfHits, int numberOfWins){
            this.numberOfHits = numberOfHits;
            this.numberOfWins = numberOfWins;
        }
    }

    int candleNumber;
    // 35-40 price level, 40 hits
    HashMap<String, Hits> priceLevels = new HashMap<>();
    ArrayList<Double> liquidity = new ArrayList<>();

    public Candle(int candleNumber) {
        this.candleNumber = candleNumber;
        for(int i = 0; i < 20; i++){
            priceLevels.put((i*5) + "-" + ((i*5) + 5), new Hits(0, 0));
        }
    }

    public void addHit(String price){
        priceLevels.get(price).numberOfHits++;
    }

    public void addWin(String price){
        priceLevels.get(price).numberOfWins++;
    }

    public void addLiquidity(double price){
        liquidity.add(price);
    }

    @Override
    public String toString() {
        String response = "*********************************************************\n";
        response += "Candle: " + candleNumber + "\n";

        for(int i = 0; i < 20; i++){
            double percent = 0.0;
            if(priceLevels.get((i*5) +  "-" + ((i*5) + 5)).numberOfHits > 0){
                percent = (double) priceLevels.get((i*5) +  "-" + ((i*5) + 5)).numberOfWins / (double) priceLevels.get((i*5) +  "-" + ((i*5) + 5)).numberOfHits;
            }
            response += "\n" + "Price Level: " + (i*5) +  "-" + ((i*5) + 5) + "\n Wins/Hits: " + priceLevels.get((i*5) +  "-" + ((i*5) + 5)).numberOfWins + "/" + priceLevels.get((i*5) +  "-" + ((i*5) + 5)).numberOfHits + " Accuracy: " + String.format("%.4f", percent);
        }

        double num = 0;

        for(int i = 0; i < liquidity.size(); i++){
            num += liquidity.get(i);
        }

        response += "\n Liquidity: " + num / liquidity.size();
        return response;
    }
}