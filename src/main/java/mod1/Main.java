package mod1;

import GsonClasses.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.time.Instant;


public class Main {
    public static void main(String[] args) throws Exception {
//        AtomicBoolean running = new AtomicBoolean(true);
//
//        new Thread(() -> {
//            Scanner scanner = new Scanner(System.in);
//            while (scanner.hasNextLine()) {
//                if (scanner.nextLine().equalsIgnoreCase("q")) {
//                    running.set(false);
//                    break;
//                }
//            }
//        }).start();

//        //Market response
//        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
//        headers.put("Accept", "application/json");
//        long oneYearAgo = Instant.now().minus(365, ChronoUnit.DAYS).getEpochSecond();
//        KalshiModels.GetMarketsResponse response = Utils.getHTTP("https://api.elections.kalshi.com/trade-api/v2/markets?series_ticker=KXBTC15M&limit=1000" + "&min_close_ts=" + oneYearAgo, headers, KalshiModels.GetMarketsResponse.class);
//        int totalMarkets = response.markets.size();

        //market candle stick
//        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
//        headers.put("Accept", "application/json");
//        long now = Instant.now().getEpochSecond();
//        long past = now - 86400;
//        String query = "?start_ts=" + past + "&end_ts=" + now + "&period_interval=1";
//        KalshiModels.GetMarketCandlesticksResponse response = Utils.getHTTP("https://api.elections.kalshi.com/trade-api/v2/series/KXBTC15M/markets/KXBTC15M-26MAR140030-30/candlesticks" + query, headers, KalshiModels.GetMarketCandlesticksResponse.class);
//        ArrayList<KalshiModels.MarketCandlestick> marketCandlesticks = new ArrayList<>(response.candlesticks);
//        System.out.println(marketCandlesticks.size());
//        System.out.println(marketCandlesticks.get(0).price.meanDollars);
//        System.out.println(marketCandlesticks.get(1).price.meanDollars);
//        System.out.println(marketCandlesticks.get(2).price.meanDollars);

        //cutoff
//        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
//        headers.put("Accept", "application/json");
//        KalshiModels.GetHistoricalCutoffResponse response = Utils.getHTTP("https://api.elections.kalshi.com/trade-api/v2/historical/cutoff" , headers, KalshiModels.GetHistoricalCutoffResponse.class);
//        System.out.println(response.tradesCreatedTs);
    }
}