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
    HashMap<String, Hits> priceLevels;

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

    @Override
    public String toString() {
        return null;
    }
}