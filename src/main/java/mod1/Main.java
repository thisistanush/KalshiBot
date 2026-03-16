package mod1;

import GsonClasses.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Main {
    public static void main(String[] args) throws Exception {

        ArrayList<PriceTemp> priceUpChart = new ArrayList<>();
        ArrayList<PriceTemp> priceDownChart = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            priceDownChart.add(new PriceTemp(i * 0.05, (i * 0.05) + 0.05));
            priceUpChart.add(new PriceTemp((i * 0.05) + 0.5, (i * 0.05) + 0.55));
        }

        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("Accept", "application/json");

        System.out.println("Fetching markets...");

        KalshiModels.GetMarketsResponse marketResponse = Utils.getHTTP(
                "https://api.elections.kalshi.com/trade-api/v2/markets?series_ticker=KXBTC15M&limit=1000&status=settled",
                headers, KalshiModels.GetMarketsResponse.class);

        if (marketResponse == null) {
            System.out.println("Failed to fetch initial market response. Exiting.");
            return;
        }

        int num = 0;

        do {
            if (marketResponse.markets == null) break;

            for (int i = 0; i < marketResponse.markets.size(); i++) {
                KalshiModels.Market market = marketResponse.markets.get(i);

                if (market == null) continue;
                if (market.settlementValueDollars == null) continue;

                double settlementValue;
                try {
                    settlementValue = Double.parseDouble(market.settlementValueDollars);
                } catch (NumberFormatException e) {
                    continue;
                }

                boolean settledYes = (settlementValue == 1.00);

                if (market.openTime == null || market.closeTime == null) continue;

                long startTs, endTs;
                try {
                    startTs = java.time.Instant.parse(market.openTime).getEpochSecond();
                    endTs   = java.time.Instant.parse(market.closeTime).getEpochSecond();
                } catch (Exception e) {
                    continue;
                }

                String query = "?start_ts=" + startTs + "&end_ts=" + endTs + "&period_interval=1";

                KalshiModels.GetMarketCandlesticksResponse candlestickResponse = Utils.getHTTP(
                        "https://api.elections.kalshi.com/trade-api/v2/series/KXBTC15M/markets/"
                                + market.ticker + "/candlesticks" + query,
                        headers, KalshiModels.GetMarketCandlesticksResponse.class);

                if (candlestickResponse == null || candlestickResponse.candlesticks == null) continue;

                for (int z = 0; z < candlestickResponse.candlesticks.size(); z++) {
                    KalshiModels.MarketCandlestick candle = candlestickResponse.candlesticks.get(z);

                    if (candle == null) continue;
                    if (candle.yesBid == null || candle.yesAsk == null) continue;
                    if (candle.yesBid.closeDollars == null || candle.yesAsk.closeDollars == null) continue;
                    if (candle.volumeFp == null) continue;

                    double bid, ask, volume;
                    try {
                        bid    = Double.parseDouble(candle.yesBid.closeDollars);
                        ask    = Double.parseDouble(candle.yesAsk.closeDollars);
                        volume = Double.parseDouble(candle.volumeFp);
                    } catch (NumberFormatException e) {
                        continue;
                    }

                    double mid = (bid + ask) / 2.0;

                    if (mid > 0.5) {
                        for (int x = 0; x < priceUpChart.size(); x++) {
                            PriceTemp bucket = priceUpChart.get(x);
                            if (mid > bucket.getBottomDollarsLevel() && mid < bucket.getTopDollarsLevel()) {
                                bucket.addTotalNumberOfHits();
                                if (settledYes) bucket.addNumberOfWins();
                                bucket.addLiquidityVolume(volume);
                            }
                        }
                    } else {
                        for (int x = 0; x < priceDownChart.size(); x++) {
                            PriceTemp bucket = priceDownChart.get(x);
                            if (mid > bucket.getBottomDollarsLevel() && mid < bucket.getTopDollarsLevel()) {
                                bucket.addTotalNumberOfHits();
                                if (!settledYes) bucket.addNumberOfWins();
                                bucket.addLiquidityVolume(volume);
                            }
                        }
                    }
                }

                num++;
                System.out.println("Markets processed: " + num);
            }

            String cursor = marketResponse.cursor;
            if (cursor == null || cursor.isEmpty()) break;

            marketResponse = Utils.getHTTP(
                    "https://api.elections.kalshi.com/trade-api/v2/markets?series_ticker=KXBTC15M&limit=1000&status=settled&cursor=" + cursor,
                    headers, KalshiModels.GetMarketsResponse.class);

            if (marketResponse == null) break;

        } while (true);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("price_history"))) {
            writer.write("=== YES buckets (mid > 0.50) ===");
            writer.newLine();
            for (int i = 0; i < priceUpChart.size(); i++) {
                writer.write(priceUpChart.get(i).toString());
                writer.newLine();
                writer.newLine();
            }

            writer.write("=== NO buckets (mid < 0.50) ===");
            writer.newLine();
            for (int i = 0; i < priceDownChart.size(); i++) {
                writer.write(priceDownChart.get(i).toString());
                writer.newLine();
                writer.newLine();
            }
        }

        System.out.println("Done. Results written to price_history.");
    }
}