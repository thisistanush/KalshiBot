package mod1;

import GsonClasses.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Main {
    public static void main(String[] args) throws Exception {

        int MAX_CANDLES = 15;
        ArrayList<CandlestickHistory> candlestickPriceHistories = new ArrayList<>();

        for (int i = 0; i < MAX_CANDLES; i++) {
            candlestickPriceHistories.add(new CandlestickHistory(i));
        }

        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("Accept", "application/json");

        KalshiModels.GetMarketsResponse marketResponse = Utils.getHTTP(
                "https://api.elections.kalshi.com/trade-api/v2/markets?series_ticker=KXBTC15M&limit=1000&status=settled",
                headers,
                KalshiModels.GetMarketsResponse.class
        );

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

                // Use threshold instead of float equality
                // settlementValue > 0.5 means YES won, <= 0.5 means NO won
                boolean yesWon;
                if (settlementValue > 0.5) {
                    yesWon = true;
                } else {
                    yesWon = false;
                }

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
                        "https://api.elections.kalshi.com/trade-api/v2/series/KXBTC15M/markets/" + market.ticker + "/candlesticks" + query,
                        headers,
                        KalshiModels.GetMarketCandlesticksResponse.class
                );

                if (candlestickResponse == null || candlestickResponse.candlesticks == null) continue;

                int candleCount = candlestickResponse.candlesticks.size();

                for (int z = 0; z < candleCount; z++) {

                    // Bounds check — skip if we somehow get more candles than expected
                    if (z >= candlestickPriceHistories.size()) break;

                    KalshiModels.MarketCandlestick candle = candlestickResponse.candlesticks.get(z);
                    if (candle == null) continue;
                    if (candle.yesBid == null || candle.yesAsk == null) continue;
                    if (candle.yesBid.closeDollars == null || candle.yesAsk.closeDollars == null) continue;

                    double yesBidClose;
                    double yesAskClose;
                    try {
                        yesBidClose = Double.parseDouble(candle.yesBid.closeDollars);
                        yesAskClose = Double.parseDouble(candle.yesAsk.closeDollars);
                    } catch (NumberFormatException e) {
                        continue;
                    }

                    // Mid-quote: the correct probability estimate, not meanDollars
                    double midQuote = (yesBidClose + yesAskClose) / 2.0;

                    CandlestickHistory history = candlestickPriceHistories.get(z);

                    if (midQuote >= 0.5) {
                        // YES side prediction
                        history.addYesHit();
                        if (yesWon) {
                            history.addYesWin();
                        }
                    } else {
                        // NO side prediction
                        history.addNoHit();
                        if (!yesWon) {
                            history.addNoWin();
                        }
                    }

                    // Reversal analysis:
                    // Check whether any later candle in this same market crossed back over 0.5
                    if (midQuote >= 0.5) {
                        // We are on the YES side at candle z.
                        // Check if any candle after z crosses below 0.5.
                        boolean reversed = false;
                        double maxCrossBelow = midQuote; // track how far it went

                        for (int r = z + 1; r < candleCount; r++) {
                            KalshiModels.MarketCandlestick laterCandle = candlestickResponse.candlesticks.get(r);
                            if (laterCandle == null) continue;
                            if (laterCandle.yesBid == null || laterCandle.yesAsk == null) continue;
                            if (laterCandle.yesBid.closeDollars == null || laterCandle.yesAsk.closeDollars == null) continue;

                            double laterBid;
                            double laterAsk;
                            try {
                                laterBid = Double.parseDouble(laterCandle.yesBid.closeDollars);
                                laterAsk = Double.parseDouble(laterCandle.yesAsk.closeDollars);
                            } catch (NumberFormatException e) {
                                continue;
                            }

                            double laterMid = (laterBid + laterAsk) / 2.0;

                            if (laterMid < 0.5) {
                                reversed = true;
                                // Magnitude = how far below 0.5 it crossed
                                double distanceBelow = 0.5 - laterMid;
                                if (distanceBelow > maxCrossBelow) {
                                    maxCrossBelow = distanceBelow;
                                }
                            }
                        }

                        if (reversed) {
                            history.addYesReversal(maxCrossBelow);
                        }

                    } else {
                        // We are on the NO side at candle z.
                        // Check if any candle after z crosses above 0.5.
                        boolean reversed = false;
                        double maxCrossAbove = 0.0;

                        for (int r = z + 1; r < candleCount; r++) {
                            KalshiModels.MarketCandlestick laterCandle = candlestickResponse.candlesticks.get(r);
                            if (laterCandle == null) continue;
                            if (laterCandle.yesBid == null || laterCandle.yesAsk == null) continue;
                            if (laterCandle.yesBid.closeDollars == null || laterCandle.yesAsk.closeDollars == null) continue;

                            double laterBid;
                            double laterAsk;
                            try {
                                laterBid = Double.parseDouble(laterCandle.yesBid.closeDollars);
                                laterAsk = Double.parseDouble(laterCandle.yesAsk.closeDollars);
                            } catch (NumberFormatException e) {
                                continue;
                            }

                            double laterMid = (laterBid + laterAsk) / 2.0;

                            if (laterMid >= 0.5) {
                                reversed = true;
                                double distanceAbove = laterMid - 0.5;
                                if (distanceAbove > maxCrossAbove) {
                                    maxCrossAbove = distanceAbove;
                                }
                            }
                        }

                        if (reversed) {
                            history.addNoReversal(maxCrossAbove);
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
                    headers,
                    KalshiModels.GetMarketsResponse.class
            );
            if (marketResponse == null) break;

        } while (true);

        File file = new File("price_history.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            for (int i = 0; i < candlestickPriceHistories.size(); i++) {
                writer.write(candlestickPriceHistories.get(i).toString());
                writer.newLine();
            }
        }

        System.out.println("Done. Results written to price_history.txt");
    }
}