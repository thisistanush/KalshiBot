package mod1;

import GsonClasses.KalshiModels;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Main {
    public static void main(String[] args) throws Exception {

        ArrayList<Candle> candlesticks = new ArrayList<>();

        for(int i = 0; i < 15; i++){
            candlesticks.add(new Candle(i));
        }

        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("Accept", "application/json");

        KalshiModels.GetMarketsResponse marketResponse = Utils.getHTTP("https://api.elections.kalshi.com/trade-api/v2/markets?series_ticker=KXBTC15M&limit=1000&status=settled", headers, KalshiModels.GetMarketsResponse.class);

        if (marketResponse == null) {
            System.out.println("Failed to fetch initial market response. Exiting.");
            return;
        }

        int num = 0;

        do{
            long startTime = System.nanoTime();
            if (marketResponse.markets == null) break;

            for (int i = 0; i < marketResponse.markets.size(); i++) {
                String ticker = marketResponse.markets.get(i).ticker;
                KalshiModels.GetMarketCandlesticksResponse currentMarketCandlesticks = Utils.getHTTP("http://api.elections.kalshi.com/trade-api/v2/series/KXBTC15M/markets/" + ticker + "/candlesticks?start_ts=" + java.time.Instant.parse(marketResponse.markets.get(i).openTime).getEpochSecond() + "&end_ts=" + java.time.Instant.parse(marketResponse.markets.get(i).closeTime).getEpochSecond() + "&period_interval=1", headers, KalshiModels.GetMarketCandlesticksResponse.class);
                double winnerPrice = Double.parseDouble(currentMarketCandlesticks.candlesticks.get(14).price.closeDollars);

                for(int x = 0; x < currentMarketCandlesticks.candlesticks.size(); x++){
                    double price = (Double.parseDouble(currentMarketCandlesticks.candlesticks.get(x).yesBid.closeDollars) + Double.parseDouble(currentMarketCandlesticks.candlesticks.get(x).yesAsk.closeDollars)) / 2.0;
                    candlesticks.get(x).addHit(getPriceString(price));

                    if(price >= 0.5){
                        if(winnerPrice == 1.0){
                            candlesticks.get(x).addHit(getPriceString(price));
                        }
                    }else{
                        if(winnerPrice == 0.0){

                        }else{

                        }
                    }
                }
                num++;
                System.out.println("Markets " + num + " processed");
            }


            long endTime = System.nanoTime();
            long durationInMilli = (endTime - startTime) / 1_000_000;
            System.out.println("Execution for 1000 markets: " + durationInMilli + " ms");

            String cursor = marketResponse.cursor;
            if (cursor == null || cursor.isEmpty()) break;

            marketResponse = Utils.getHTTP("https://api.elections.kalshi.com/trade-api/v2/markets?series_ticker=KXBTC15M&limit=1000&status=settled&cursor=" + cursor, headers, KalshiModels.GetMarketsResponse.class);
            if (marketResponse == null){
                break;
            }
        }while(true);

        System.out.println("All markets processed");

        File file = new File("price_history.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            for (int i = 0; i < candlesticks.size(); i++) {

                writer.newLine();
            }
        }

        System.out.println("Done. Results written to price_history.txt in home folder");
    }

    public static String getPriceString(double number){
        if(number >= 0.0 && number <= 0.05){
            return "0-5";
        }else if(number > 0.05 && number <= 0.10){
            return "5-10";
        }else if(number > 0.10 && number <= 0.15){
            return "10-15";
        }else if(number > 0.15 && number <= 0.20){
            return "15-20";
        }else if(number > 0.20 && number <= 0.25){
            return "20-25";
        }else if(number > 0.25 && number <= 0.30){
            return "25-30";
        }else if(number > 0.30 && number <= 0.35){
            return "30-35";
        }else if(number > 0.35 && number <= 0.40){
            return "35-40";
        }else if(number > 0.40 && number <= 0.45){
            return "40-45";
        }else if(number > 0.45 && number <= 0.50){
            return "45-50";
        }else if(number > 0.50 && number <= 0.55){
            return "50-55";
        }else if(number > 0.55 && number <= 0.60){
            return "55-60";
        }else if(number > 0.60 && number <= 0.65){
            return "60-65";
        }else if(number > 0.65 && number <= 0.70){
            return "65-70";
        }else if(number > 0.70 && number <= 0.75){
            return "70-75";
        }else if(number > 0.75 && number <= 0.80){
            return "75-80";
        }else if(number > 0.80 && number <= 0.85){
            return "80-85";
        }else if(number > 0.85 && number <= 0.90){
            return "85-90";
        }else if(number > 0.90 && number <= 0.95){
            return "90-95";
        }else if(number > 0.95 && number <= 1.0){
            return "95-100";
        }else{
            return null;
        }
    }
}






