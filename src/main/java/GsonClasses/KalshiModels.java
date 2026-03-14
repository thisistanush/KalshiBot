package GsonClasses;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

/**
 * Kalshi Trade API v2 — Gson Model Classes
 *
 * All monetary string fields use Kalshi's FixedPointDollars format (e.g., "0.5600").
 * All contract count string fields use FixedPointCount format (e.g., "10.00").
 *
 * Dependency: com.google.code.gson:gson (any recent version)
 *
 * Usage:
 *   Gson gson = new Gson();
 *   GetMarketsResponse resp = gson.fromJson(json, GetMarketsResponse.class);
 */
public final class KalshiModels {

    private KalshiModels() {}

    // =========================================================================
    // ENUMS
    // =========================================================================

    public enum OrderStatus       { resting, canceled, executed }
    public enum OrderSide         { yes, no }
    public enum OrderAction       { buy, sell }
    public enum OrderType         { limit, market }
    public enum TakerSide         { yes, no }
    public enum MarketType        { binary, scalar }
    public enum StrikeType        { greater, greater_or_equal, less, less_or_equal, between, functional, custom, structured }
    public enum EventStatus       { open, closed, settled }
    public enum AnnouncementType  { info, warning, error }
    public enum AnnouncementStatus{ active, inactive }
    public enum IncentiveType     { liquidity, volume }
    public enum RFQStatus         { open, closed }
    public enum QuoteStatus       { open, accepted, confirmed, executed, cancelled }
    public enum MVECollectionStatus{ unopened, open, closed }
    public enum MveFilterType     { only, exclude }

    public enum FeeType {
        quadratic, quadratic_with_maker_fees, flat
    }

    public enum SelfTradePreventionType {
        taker_at_cross, maker
    }

    public enum ExchangeInstance {
        event_contract, margined
    }

    public enum TimeInForce {
        fill_or_kill, good_till_canceled, immediate_or_cancel
    }

    public enum MarketLifecycleStatus {
        initialized, inactive, active, closed, determined, disputed, amended, finalized
    }


    // =========================================================================
    // COMMON / SHARED
    // =========================================================================

    /** Generic API error returned by all error responses. */
    public static class ErrorResponse {
        @SerializedName("code")    public String code;
        @SerializedName("message") public String message;
        @SerializedName("details") public String details;
        @SerializedName("service") public String service;
    }

    /** Empty response body — used by reset, trigger, confirm, etc. */
    public static class EmptyResponse {}

    public static class SettlementSource {
        @SerializedName("name") public String name;
        @SerializedName("url")  public String url;
    }

    /** Price range for a market's valid order prices. */
    public static class PriceRange {
        @SerializedName("start") public String start; // dollars string
        @SerializedName("end")   public String end;
        @SerializedName("step")  public String step;
    }

    /** A leg in a multivariate event selection. */
    public static class TickerPair {
        @SerializedName("market_ticker") public String marketTicker;
        @SerializedName("event_ticker")  public String eventTicker;
        @SerializedName("side")          public String side; // yes | no
    }

    /** A selected leg with optional settlement value. Used by MVE markets and RFQs. */
    public static class MveSelectedLeg {
        @SerializedName("event_ticker")                public String eventTicker;
        @SerializedName("market_ticker")               public String marketTicker;
        @SerializedName("side")                        public String side;
        @SerializedName("yes_settlement_value_dollars") public String yesSettlementValueDollars; // nullable
    }


    // =========================================================================
    // EXCHANGE
    // =========================================================================

    public static class ExchangeStatus {
        @SerializedName("exchange_active")               public boolean exchangeActive;
        @SerializedName("trading_active")                public boolean tradingActive;
        @SerializedName("exchange_estimated_resume_time") public String exchangeEstimatedResumeTime; // date-time, nullable
    }

    public static class Announcement {
        @SerializedName("type")          public String type;         // info | warning | error
        @SerializedName("message")       public String message;
        @SerializedName("delivery_time") public String deliveryTime; // date-time
        @SerializedName("status")        public String status;       // active | inactive
    }

    public static class GetExchangeAnnouncementsResponse {
        @SerializedName("announcements") public List<Announcement> announcements;
    }

    public static class DailySchedule {
        @SerializedName("open_time")  public String openTime;  // HH:MM ET
        @SerializedName("close_time") public String closeTime; // HH:MM ET
    }

    public static class WeeklySchedule {
        @SerializedName("start_time") public String startTime;
        @SerializedName("end_time")   public String endTime;
        @SerializedName("monday")     public List<DailySchedule> monday;
        @SerializedName("tuesday")    public List<DailySchedule> tuesday;
        @SerializedName("wednesday")  public List<DailySchedule> wednesday;
        @SerializedName("thursday")   public List<DailySchedule> thursday;
        @SerializedName("friday")     public List<DailySchedule> friday;
        @SerializedName("saturday")   public List<DailySchedule> saturday;
        @SerializedName("sunday")     public List<DailySchedule> sunday;
    }

    public static class MaintenanceWindow {
        @SerializedName("start_datetime") public String startDatetime; // date-time
        @SerializedName("end_datetime")   public String endDatetime;   // date-time
    }

    public static class Schedule {
        @SerializedName("standard_hours")      public List<WeeklySchedule>    standardHours;
        @SerializedName("maintenance_windows") public List<MaintenanceWindow> maintenanceWindows;
    }

    public static class GetExchangeScheduleResponse {
        @SerializedName("schedule") public Schedule schedule;
    }

    public static class GetUserDataTimestampResponse {
        @SerializedName("as_of_time") public String asOfTime; // date-time
    }

    public static class IntraExchangeInstanceTransferRequest {
        @SerializedName("source")      public String source;      // event_contract | margined
        @SerializedName("destination") public String destination; // event_contract | margined
        @SerializedName("amount")      public long   amount;      // centicents
    }

    public static class IntraExchangeInstanceTransferResponse {
        @SerializedName("transfer_id") public String transferId;
    }


    // =========================================================================
    // SERIES
    // =========================================================================

    public static class Series {
        @SerializedName("ticker")                  public String ticker;
        @SerializedName("frequency")               public String frequency;
        @SerializedName("title")                   public String title;
        @SerializedName("category")                public String category;
        @SerializedName("tags")                    public List<String> tags;
        @SerializedName("settlement_sources")      public List<SettlementSource> settlementSources;
        @SerializedName("contract_url")            public String contractUrl;
        @SerializedName("contract_terms_url")      public String contractTermsUrl;
        @SerializedName("product_metadata")        public Map<String, Object> productMetadata; // nullable
        @SerializedName("fee_type")                public String feeType;       // quadratic | quadratic_with_maker_fees | flat
        @SerializedName("fee_multiplier")          public double feeMultiplier;
        @SerializedName("additional_prohibitions") public List<String> additionalProhibitions;
        @SerializedName("volume_fp")               public String volumeFp;      // FixedPointCount
        @SerializedName("last_updated_ts")         public String lastUpdatedTs; // date-time
    }

    public static class GetSeriesResponse {
        @SerializedName("series") public Series series;
    }

    public static class GetSeriesListResponse {
        @SerializedName("series") public List<Series> series;
    }

    public static class SeriesFeeChange {
        @SerializedName("id")             public String id;
        @SerializedName("series_ticker")  public String seriesTicker;
        @SerializedName("fee_type")       public String feeType;
        @SerializedName("fee_multiplier") public double feeMultiplier;
        @SerializedName("scheduled_ts")   public String scheduledTs; // date-time
    }

    public static class GetSeriesFeeChangesResponse {
        @SerializedName("series_fee_change_arr") public List<SeriesFeeChange> seriesFeeChangeArr;
    }


    // =========================================================================
    // MARKETS
    // =========================================================================

    public static class Market {
        @SerializedName("ticker")                   public String ticker;
        @SerializedName("event_ticker")             public String eventTicker;
        @SerializedName("market_type")              public String marketType;         // binary | scalar
        @SerializedName("title")                    public String title;              // deprecated
        @SerializedName("subtitle")                 public String subtitle;           // deprecated
        @SerializedName("yes_sub_title")            public String yesSubTitle;
        @SerializedName("no_sub_title")             public String noSubTitle;
        @SerializedName("created_time")             public String createdTime;
        @SerializedName("updated_time")             public String updatedTime;
        @SerializedName("open_time")                public String openTime;
        @SerializedName("close_time")               public String closeTime;
        @SerializedName("expected_expiration_time") public String expectedExpirationTime; // nullable
        @SerializedName("expiration_time")          public String expirationTime;          // deprecated
        @SerializedName("latest_expiration_time")   public String latestExpirationTime;
        @SerializedName("settlement_timer_seconds") public int settlementTimerSeconds;
        @SerializedName("status")                   public String status;
        @SerializedName("response_price_units")     public String responsePriceUnits;     // deprecated
        @SerializedName("notional_value_dollars")   public String notionalValueDollars;   // FixedPointDollars
        @SerializedName("yes_bid_dollars")          public String yesBidDollars;
        @SerializedName("yes_bid_size_fp")          public String yesBidSizeFp;           // FixedPointCount
        @SerializedName("yes_ask_dollars")          public String yesAskDollars;
        @SerializedName("yes_ask_size_fp")          public String yesAskSizeFp;           // FixedPointCount
        @SerializedName("no_bid_dollars")           public String noBidDollars;
        @SerializedName("no_ask_dollars")           public String noAskDollars;
        @SerializedName("last_price_dollars")       public String lastPriceDollars;
        @SerializedName("previous_yes_bid_dollars") public String previousYesBidDollars;
        @SerializedName("previous_yes_ask_dollars") public String previousYesAskDollars;
        @SerializedName("previous_price_dollars")   public String previousPriceDollars;
        @SerializedName("volume_fp")                public String volumeFp;
        @SerializedName("volume_24h_fp")            public String volume24hFp;
        @SerializedName("liquidity_dollars")        public String liquidityDollars;       // deprecated, always "0.0000"
        @SerializedName("open_interest_fp")         public String openInterestFp;
        @SerializedName("result")                   public String result;                 // yes | no | scalar | ""
        @SerializedName("can_close_early")          public boolean canCloseEarly;
        @SerializedName("fractional_trading_enabled") public boolean fractionalTradingEnabled;
        @SerializedName("expiration_value")         public String expirationValue;
        @SerializedName("rules_primary")            public String rulesPrimary;
        @SerializedName("rules_secondary")          public String rulesSecondary;
        @SerializedName("settlement_value_dollars") public String settlementValueDollars; // nullable
        @SerializedName("settlement_ts")            public String settlementTs;            // nullable
        @SerializedName("fee_waiver_expiration_time") public String feeWaiverExpirationTime; // nullable
        @SerializedName("early_close_condition")    public String earlyCloseCondition;    // nullable
        @SerializedName("tick_size")                public int tickSize;                   // deprecated
        @SerializedName("strike_type")              public String strikeType;
        @SerializedName("floor_strike")             public Double floorStrike;             // nullable
        @SerializedName("cap_strike")               public Double capStrike;               // nullable
        @SerializedName("functional_strike")        public String functionalStrike;        // nullable
        @SerializedName("custom_strike")            public Map<String, Object> customStrike; // nullable
        @SerializedName("mve_collection_ticker")    public String mveCollectionTicker;
        @SerializedName("mve_selected_legs")        public List<MveSelectedLeg> mveSelectedLegs;
        @SerializedName("primary_participant_key")  public String primaryParticipantKey;  // nullable
        @SerializedName("price_level_structure")    public String priceLevelStructure;
        @SerializedName("price_ranges")             public List<PriceRange> priceRanges;
        @SerializedName("is_provisional")           public boolean isProvisional;
    }

    public static class GetMarketResponse {
        @SerializedName("market") public Market market;
    }

    public static class GetMarketsResponse {
        @SerializedName("markets") public List<Market> markets;
        @SerializedName("cursor")  public String cursor;
    }

    // ─── Candlesticks ────────────────────────────────────────────────────────

    public static class BidAskDistribution {
        @SerializedName("open_dollars")  public String openDollars;
        @SerializedName("low_dollars")   public String lowDollars;
        @SerializedName("high_dollars")  public String highDollars;
        @SerializedName("close_dollars") public String closeDollars;
    }

    public static class PriceDistribution {
        @SerializedName("open_dollars")     public String openDollars;     // nullable
        @SerializedName("low_dollars")      public String lowDollars;      // nullable
        @SerializedName("high_dollars")     public String highDollars;     // nullable
        @SerializedName("close_dollars")    public String closeDollars;    // nullable
        @SerializedName("mean_dollars")     public String meanDollars;     // nullable
        @SerializedName("previous_dollars") public String previousDollars; // nullable
        @SerializedName("min_dollars")      public String minDollars;      // nullable
        @SerializedName("max_dollars")      public String maxDollars;      // nullable
    }

    public static class MarketCandlestick {
        @SerializedName("end_period_ts")  public long endPeriodTs;
        @SerializedName("yes_bid")        public BidAskDistribution yesBid;
        @SerializedName("yes_ask")        public BidAskDistribution yesAsk;
        @SerializedName("price")          public PriceDistribution price;
        @SerializedName("volume_fp")      public String volumeFp;       // FixedPointCount
        @SerializedName("open_interest_fp") public String openInterestFp; // FixedPointCount
    }

    public static class GetMarketCandlesticksResponse {
        @SerializedName("ticker")       public String ticker;
        @SerializedName("candlesticks") public List<MarketCandlestick> candlesticks;
    }

    public static class MarketCandlesticksResponse {
        @SerializedName("market_ticker") public String marketTicker;
        @SerializedName("candlesticks")  public List<MarketCandlestick> candlesticks;
    }

    public static class BatchGetMarketCandlesticksResponse {
        @SerializedName("markets") public List<MarketCandlesticksResponse> markets;
    }

    // Historical candlesticks use different field names
    public static class BidAskDistributionHistorical {
        @SerializedName("open")  public String open;
        @SerializedName("low")   public String low;
        @SerializedName("high")  public String high;
        @SerializedName("close") public String close;
    }

    public static class PriceDistributionHistorical {
        @SerializedName("open")     public String open;     // nullable
        @SerializedName("low")      public String low;      // nullable
        @SerializedName("high")     public String high;     // nullable
        @SerializedName("close")    public String close;    // nullable
        @SerializedName("mean")     public String mean;     // nullable
        @SerializedName("previous") public String previous; // nullable
    }

    public static class MarketCandlestickHistorical {
        @SerializedName("end_period_ts") public long endPeriodTs;
        @SerializedName("yes_bid")       public BidAskDistributionHistorical yesBid;
        @SerializedName("yes_ask")       public BidAskDistributionHistorical yesAsk;
        @SerializedName("price")         public PriceDistributionHistorical price;
        @SerializedName("volume")        public String volume;       // FixedPointCount
        @SerializedName("open_interest") public String openInterest; // FixedPointCount
    }

    public static class GetMarketCandlesticksHistoricalResponse {
        @SerializedName("ticker")       public String ticker;
        @SerializedName("candlesticks") public List<MarketCandlestickHistorical> candlesticks;
    }

    // ─── Orderbook ────────────────────────────────────────────────────────────

    /**
     * Each price level is a [dollars_string, fp_count_string] pair.
     * Represented in JSON as [[price, qty], [price, qty], ...]
     */
    public static class OrderbookCountFp {
        @SerializedName("yes_dollars") public List<List<String>> yesDollars;
        @SerializedName("no_dollars")  public List<List<String>> noDollars;
    }

    public static class GetMarketOrderbookResponse {
        @SerializedName("orderbook_fp") public OrderbookCountFp orderbookFp;
    }

    // ─── Trades ───────────────────────────────────────────────────────────────

    public static class Trade {
        @SerializedName("trade_id")        public String tradeId;
        @SerializedName("ticker")          public String ticker;
        @SerializedName("count_fp")        public String countFp;         // FixedPointCount
        @SerializedName("yes_price_dollars") public String yesPriceDollars; // FixedPointDollars
        @SerializedName("no_price_dollars")  public String noPriceDollars;  // FixedPointDollars
        @SerializedName("taker_side")      public String takerSide;       // yes | no
        @SerializedName("created_time")    public String createdTime;     // date-time
    }

    public static class GetTradesResponse {
        @SerializedName("trades") public List<Trade> trades;
        @SerializedName("cursor") public String cursor;
    }


    // =========================================================================
    // EVENTS
    // =========================================================================

    public static class EventData {
        @SerializedName("event_ticker")         public String eventTicker;
        @SerializedName("series_ticker")        public String seriesTicker;
        @SerializedName("sub_title")            public String subTitle;
        @SerializedName("title")                public String title;
        @SerializedName("collateral_return_type") public String collateralReturnType;
        @SerializedName("mutually_exclusive")   public boolean mutuallyExclusive;
        @SerializedName("category")             public String category;
        @SerializedName("strike_date")          public String strikeDate;   // date-time, nullable
        @SerializedName("strike_period")        public String strikePeriod; // nullable
        @SerializedName("markets")              public List<Market> markets; // only with with_nested_markets=true
        @SerializedName("available_on_brokers") public boolean availableOnBrokers;
        @SerializedName("product_metadata")     public Map<String, Object> productMetadata; // nullable
        @SerializedName("last_updated_ts")      public String lastUpdatedTs; // date-time
    }

    public static class GetEventsResponse {
        @SerializedName("events")     public List<EventData> events;
        @SerializedName("milestones") public List<Milestone> milestones;
        @SerializedName("cursor")     public String cursor;
    }

    public static class GetMultivariateEventsResponse {
        @SerializedName("events") public List<EventData> events;
        @SerializedName("cursor") public String cursor;
    }

    public static class GetEventResponse {
        @SerializedName("event")   public EventData event;
        @SerializedName("markets") public List<Market> markets;
    }

    public static class MarketMetadata {
        @SerializedName("market_ticker") public String marketTicker;
        @SerializedName("image_url")     public String imageUrl;
        @SerializedName("color_code")    public String colorCode;
    }

    public static class GetEventMetadataResponse {
        @SerializedName("image_url")          public String imageUrl;
        @SerializedName("featured_image_url") public String featuredImageUrl;
        @SerializedName("market_details")     public List<MarketMetadata> marketDetails;
        @SerializedName("settlement_sources") public List<SettlementSource> settlementSources;
        @SerializedName("competition")        public String competition;       // nullable
        @SerializedName("competition_scope")  public String competitionScope; // nullable
    }

    public static class GetEventCandlesticksResponse {
        @SerializedName("market_tickers")      public List<String> marketTickers;
        @SerializedName("market_candlesticks") public List<List<MarketCandlestick>> marketCandlesticks;
        @SerializedName("adjusted_end_ts")     public long adjustedEndTs;
    }

    public static class PercentilePoint {
        @SerializedName("percentile")            public int percentile;
        @SerializedName("raw_numerical_forecast") public double rawNumericalForecast;
        @SerializedName("numerical_forecast")    public double numericalForecast;
        @SerializedName("formatted_forecast")    public String formattedForecast;
    }

    public static class ForecastPercentilesPoint {
        @SerializedName("event_ticker")    public String eventTicker;
        @SerializedName("end_period_ts")   public long endPeriodTs;
        @SerializedName("period_interval") public int periodInterval;
        @SerializedName("percentile_points") public List<PercentilePoint> percentilePoints;
    }

    public static class GetEventForecastPercentilesHistoryResponse {
        @SerializedName("forecast_history") public List<ForecastPercentilesPoint> forecastHistory;
    }


    // =========================================================================
    // HISTORICAL
    // =========================================================================

    public static class GetHistoricalCutoffResponse {
        @SerializedName("market_settled_ts")  public String marketSettledTs;  // date-time
        @SerializedName("trades_created_ts")  public String tradesCreatedTs;  // date-time
        @SerializedName("orders_updated_ts")  public String ordersUpdatedTs;  // date-time
    }


    // =========================================================================
    // ORDERS
    // =========================================================================

    public static class Order {
        @SerializedName("order_id")                   public String orderId;
        @SerializedName("user_id")                    public String userId;
        @SerializedName("client_order_id")            public String clientOrderId;
        @SerializedName("ticker")                     public String ticker;
        @SerializedName("side")                       public String side;              // yes | no
        @SerializedName("action")                     public String action;            // buy | sell
        @SerializedName("type")                       public String type;              // limit | market
        @SerializedName("status")                     public String status;            // resting | canceled | executed
        @SerializedName("yes_price_dollars")          public String yesPriceDollars;
        @SerializedName("no_price_dollars")           public String noPriceDollars;
        @SerializedName("fill_count_fp")              public String fillCountFp;
        @SerializedName("remaining_count_fp")         public String remainingCountFp;
        @SerializedName("initial_count_fp")           public String initialCountFp;
        @SerializedName("taker_fill_cost_dollars")    public String takerFillCostDollars;
        @SerializedName("maker_fill_cost_dollars")    public String makerFillCostDollars;
        @SerializedName("taker_fees_dollars")         public String takerFeesDollars;
        @SerializedName("maker_fees_dollars")         public String makerFeesDollars;
        @SerializedName("expiration_time")            public String expirationTime;    // nullable
        @SerializedName("created_time")               public String createdTime;       // nullable
        @SerializedName("last_update_time")           public String lastUpdateTime;    // nullable
        @SerializedName("self_trade_prevention_type") public String selfTradePreventionType; // nullable
        @SerializedName("order_group_id")             public String orderGroupId;      // nullable
        @SerializedName("cancel_order_on_pause")      public boolean cancelOrderOnPause;
        @SerializedName("subaccount_number")          public Integer subaccountNumber; // nullable
    }

    public static class GetOrderResponse {
        @SerializedName("order") public Order order;
    }

    public static class GetOrdersResponse {
        @SerializedName("orders") public List<Order> orders;
        @SerializedName("cursor") public String cursor;
    }

    public static class CreateOrderRequest {
        @SerializedName("ticker")                     public String ticker;
        @SerializedName("client_order_id")            public String clientOrderId;
        @SerializedName("side")                       public String side;       // yes | no
        @SerializedName("action")                     public String action;     // buy | sell
        @SerializedName("count")                      public Integer count;
        @SerializedName("count_fp")                   public String countFp;    // nullable
        @SerializedName("yes_price")                  public Integer yesPrice;  // deprecated
        @SerializedName("no_price")                   public Integer noPrice;   // deprecated
        @SerializedName("yes_price_dollars")          public String yesPriceDollars;
        @SerializedName("no_price_dollars")           public String noPriceDollars;
        @SerializedName("expiration_ts")              public Long expirationTs;
        @SerializedName("time_in_force")              public String timeInForce;
        @SerializedName("buy_max_cost")               public Integer buyMaxCost; // cents
        @SerializedName("post_only")                  public Boolean postOnly;
        @SerializedName("reduce_only")                public Boolean reduceOnly;
        @SerializedName("sell_position_floor")        public Integer sellPositionFloor; // deprecated
        @SerializedName("self_trade_prevention_type") public String selfTradePreventionType;
        @SerializedName("order_group_id")             public String orderGroupId;
        @SerializedName("cancel_order_on_pause")      public Boolean cancelOrderOnPause;
        @SerializedName("subaccount")                 public Integer subaccount;
    }

    public static class CreateOrderResponse {
        @SerializedName("order") public Order order;
    }

    public static class BatchCreateOrdersRequest {
        @SerializedName("orders") public List<CreateOrderRequest> orders;
    }

    public static class BatchCreateOrdersIndividualResponse {
        @SerializedName("client_order_id") public String clientOrderId; // nullable
        @SerializedName("order")           public Order order;          // nullable
        @SerializedName("error")           public ErrorResponse error;  // nullable
    }

    public static class BatchCreateOrdersResponse {
        @SerializedName("orders") public List<BatchCreateOrdersIndividualResponse> orders;
    }

    public static class CancelOrderResponse {
        @SerializedName("order")          public Order order;
        @SerializedName("reduced_by_fp")  public String reducedByFp; // FixedPointCount
    }

    public static class BatchCancelOrdersRequestOrder {
        @SerializedName("order_id")   public String orderId;
        @SerializedName("subaccount") public Integer subaccount;
    }

    public static class BatchCancelOrdersRequest {
        @SerializedName("ids")    public List<String> ids;    // deprecated
        @SerializedName("orders") public List<BatchCancelOrdersRequestOrder> orders;
    }

    public static class BatchCancelOrdersIndividualResponse {
        @SerializedName("order_id")      public String orderId;
        @SerializedName("order")         public Order order;         // nullable
        @SerializedName("reduced_by_fp") public String reducedByFp; // FixedPointCount
        @SerializedName("error")         public ErrorResponse error; // nullable
    }

    public static class BatchCancelOrdersResponse {
        @SerializedName("orders") public List<BatchCancelOrdersIndividualResponse> orders;
    }

    public static class AmendOrderRequest {
        @SerializedName("subaccount")             public Integer subaccount;
        @SerializedName("ticker")                 public String ticker;
        @SerializedName("side")                   public String side;
        @SerializedName("action")                 public String action;
        @SerializedName("client_order_id")        public String clientOrderId;
        @SerializedName("updated_client_order_id") public String updatedClientOrderId;
        @SerializedName("yes_price")              public Integer yesPrice;
        @SerializedName("no_price")               public Integer noPrice;
        @SerializedName("yes_price_dollars")      public String yesPriceDollars;
        @SerializedName("no_price_dollars")       public String noPriceDollars;
        @SerializedName("count")                  public Integer count;
        @SerializedName("count_fp")               public String countFp; // nullable
    }

    public static class AmendOrderResponse {
        @SerializedName("old_order") public Order oldOrder;
        @SerializedName("order")     public Order order;
    }

    public static class DecreaseOrderRequest {
        @SerializedName("subaccount")   public Integer subaccount;
        @SerializedName("reduce_by")    public Integer reduceBy;
        @SerializedName("reduce_by_fp") public String reduceByFp;  // nullable
        @SerializedName("reduce_to")    public Integer reduceTo;
        @SerializedName("reduce_to_fp") public String reduceToFp;  // nullable
    }

    public static class DecreaseOrderResponse {
        @SerializedName("order") public Order order;
    }

    public static class GetOrderQueuePositionResponse {
        @SerializedName("queue_position_fp") public String queuePositionFp; // FixedPointCount
    }

    public static class OrderQueuePosition {
        @SerializedName("order_id")          public String orderId;
        @SerializedName("market_ticker")     public String marketTicker;
        @SerializedName("queue_position_fp") public String queuePositionFp;
    }

    public static class GetOrderQueuePositionsResponse {
        @SerializedName("queue_positions") public List<OrderQueuePosition> queuePositions;
    }


    // =========================================================================
    // ORDER GROUPS
    // =========================================================================

    public static class OrderGroup {
        @SerializedName("id")                     public String id;
        @SerializedName("contracts_limit_fp")     public String contractsLimitFp; // FixedPointCount
        @SerializedName("is_auto_cancel_enabled") public boolean isAutoCancelEnabled;
    }

    public static class GetOrderGroupsResponse {
        @SerializedName("order_groups") public List<OrderGroup> orderGroups;
    }

    public static class GetOrderGroupResponse {
        @SerializedName("is_auto_cancel_enabled") public boolean isAutoCancelEnabled;
        @SerializedName("contracts_limit_fp")     public String contractsLimitFp;
        @SerializedName("orders")                 public List<String> orders; // order IDs
    }

    public static class CreateOrderGroupRequest {
        @SerializedName("subaccount")          public Integer subaccount;
        @SerializedName("contracts_limit")     public Long contractsLimit;
        @SerializedName("contracts_limit_fp")  public String contractsLimitFp; // nullable
    }

    public static class CreateOrderGroupResponse {
        @SerializedName("order_group_id") public String orderGroupId;
    }

    public static class UpdateOrderGroupLimitRequest {
        @SerializedName("contracts_limit")    public Long contractsLimit;
        @SerializedName("contracts_limit_fp") public String contractsLimitFp; // nullable
    }


    // =========================================================================
    // PORTFOLIO
    // =========================================================================

    public static class GetBalanceResponse {
        @SerializedName("balance")         public long balance;       // cents
        @SerializedName("portfolio_value") public long portfolioValue; // cents
        @SerializedName("updated_ts")      public long updatedTs;
    }

    public static class GetPortfolioRestingOrderTotalValueResponse {
        @SerializedName("total_resting_order_value") public long totalRestingOrderValue; // cents
    }

    public static class MarketPosition {
        @SerializedName("ticker")                public String ticker;
        @SerializedName("total_traded_dollars")  public String totalTradedDollars;
        @SerializedName("position_fp")           public String positionFp;            // FixedPointCount (negative = NO)
        @SerializedName("market_exposure_dollars") public String marketExposureDollars;
        @SerializedName("realized_pnl_dollars")  public String realizedPnlDollars;
        @SerializedName("resting_orders_count")  public int restingOrdersCount;       // deprecated
        @SerializedName("fees_paid_dollars")     public String feesPaidDollars;
        @SerializedName("last_updated_ts")       public String lastUpdatedTs;
    }

    public static class EventPosition {
        @SerializedName("event_ticker")             public String eventTicker;
        @SerializedName("total_cost_dollars")       public String totalCostDollars;
        @SerializedName("total_cost_shares_fp")     public String totalCostSharesFp;
        @SerializedName("event_exposure_dollars")   public String eventExposureDollars;
        @SerializedName("realized_pnl_dollars")     public String realizedPnlDollars;
        @SerializedName("fees_paid_dollars")        public String feesPaidDollars;
    }

    public static class GetPositionsResponse {
        @SerializedName("cursor")            public String cursor;
        @SerializedName("market_positions")  public List<MarketPosition> marketPositions;
        @SerializedName("event_positions")   public List<EventPosition> eventPositions;
    }

    public static class Fill {
        @SerializedName("fill_id")            public String fillId;
        @SerializedName("trade_id")           public String tradeId;          // legacy alias
        @SerializedName("order_id")           public String orderId;
        @SerializedName("client_order_id")    public String clientOrderId;
        @SerializedName("ticker")             public String ticker;
        @SerializedName("market_ticker")      public String marketTicker;     // legacy alias
        @SerializedName("side")               public String side;             // yes | no
        @SerializedName("action")             public String action;           // buy | sell
        @SerializedName("count_fp")           public String countFp;
        @SerializedName("yes_price_dollars")  public String yesPriceDollars;
        @SerializedName("yes_price_fixed")    public String yesPriceFixed;    // deprecated
        @SerializedName("no_price_dollars")   public String noPriceDollars;
        @SerializedName("no_price_fixed")     public String noPriceFixed;     // deprecated
        @SerializedName("is_taker")           public boolean isTaker;
        @SerializedName("created_time")       public String createdTime;
        @SerializedName("fee_cost")           public String feeCost;          // FixedPointDollars
        @SerializedName("subaccount_number")  public Integer subaccountNumber; // nullable
        @SerializedName("ts")                 public long ts;                 // legacy unix timestamp
    }

    public static class GetFillsResponse {
        @SerializedName("fills")  public List<Fill> fills;
        @SerializedName("cursor") public String cursor;
    }

    public static class Settlement {
        @SerializedName("ticker")                public String ticker;
        @SerializedName("event_ticker")          public String eventTicker;
        @SerializedName("market_result")         public String marketResult;       // yes | no | scalar | void
        @SerializedName("yes_count_fp")          public String yesCountFp;
        @SerializedName("yes_total_cost")        public int yesTotalCost;          // deprecated, cents
        @SerializedName("yes_total_cost_dollars") public String yesTotalCostDollars;
        @SerializedName("no_count_fp")           public String noCountFp;
        @SerializedName("no_total_cost")         public int noTotalCost;           // deprecated, cents
        @SerializedName("no_total_cost_dollars") public String noTotalCostDollars;
        @SerializedName("revenue")               public long revenue;              // cents
        @SerializedName("settled_time")          public String settledTime;
        @SerializedName("fee_cost")              public String feeCost;
        @SerializedName("value")                 public Integer value;             // nullable, cents per yes contract
    }

    public static class GetSettlementsResponse {
        @SerializedName("settlements") public List<Settlement> settlements;
        @SerializedName("cursor")      public String cursor;
    }

    // ─── Subaccounts ──────────────────────────────────────────────────────────

    public static class CreateSubaccountResponse {
        @SerializedName("subaccount_number") public int subaccountNumber;
    }

    public static class ApplySubaccountTransferRequest {
        @SerializedName("client_transfer_id") public String clientTransferId; // UUID
        @SerializedName("from_subaccount")    public int fromSubaccount;
        @SerializedName("to_subaccount")      public int toSubaccount;
        @SerializedName("amount_cents")       public long amountCents;
    }

    public static class SubaccountBalance {
        @SerializedName("subaccount_number") public int subaccountNumber;
        @SerializedName("balance")           public String balance;    // FixedPointDollars
        @SerializedName("updated_ts")        public long updatedTs;
    }

    public static class GetSubaccountBalancesResponse {
        @SerializedName("subaccount_balances") public List<SubaccountBalance> subaccountBalances;
    }

    public static class SubaccountTransfer {
        @SerializedName("transfer_id")     public String transferId;
        @SerializedName("from_subaccount") public int fromSubaccount;
        @SerializedName("to_subaccount")   public int toSubaccount;
        @SerializedName("amount_cents")    public long amountCents;
        @SerializedName("created_ts")      public long createdTs;
    }

    public static class GetSubaccountTransfersResponse {
        @SerializedName("transfers") public List<SubaccountTransfer> transfers;
        @SerializedName("cursor")    public String cursor;
    }

    public static class UpdateSubaccountNettingRequest {
        @SerializedName("subaccount_number") public int subaccountNumber;
        @SerializedName("enabled")           public boolean enabled;
    }

    public static class SubaccountNettingConfig {
        @SerializedName("subaccount_number") public int subaccountNumber;
        @SerializedName("enabled")           public boolean enabled;
    }

    public static class GetSubaccountNettingResponse {
        @SerializedName("netting_configs") public List<SubaccountNettingConfig> nettingConfigs;
    }


    // =========================================================================
    // API KEYS
    // =========================================================================

    public static class ApiKey {
        @SerializedName("api_key_id") public String apiKeyId;
        @SerializedName("name")       public String name;
        @SerializedName("scopes")     public List<String> scopes;
    }

    public static class GetApiKeysResponse {
        @SerializedName("api_keys") public List<ApiKey> apiKeys;
    }

    public static class CreateApiKeyRequest {
        @SerializedName("name")       public String name;
        @SerializedName("public_key") public String publicKey; // RSA PEM
        @SerializedName("scopes")     public List<String> scopes;
    }

    public static class CreateApiKeyResponse {
        @SerializedName("api_key_id") public String apiKeyId;
    }

    public static class GenerateApiKeyRequest {
        @SerializedName("name")   public String name;
        @SerializedName("scopes") public List<String> scopes;
    }

    public static class GenerateApiKeyResponse {
        @SerializedName("api_key_id")  public String apiKeyId;
        @SerializedName("private_key") public String privateKey; // RSA PEM — store securely!
    }

    public static class GetAccountApiLimitsResponse {
        @SerializedName("usage_tier")  public String usageTier;
        @SerializedName("read_limit")  public int readLimit;
        @SerializedName("write_limit") public int writeLimit;
    }


    // =========================================================================
    // COMMUNICATIONS (RFQ / QUOTES)
    // =========================================================================

    public static class GetCommunicationsIDResponse {
        @SerializedName("communications_id") public String communicationsId;
    }

    public static class RFQ {
        @SerializedName("id")                  public String id;
        @SerializedName("creator_id")          public String creatorId;
        @SerializedName("market_ticker")       public String marketTicker;
        @SerializedName("contracts_fp")        public String contractsFp;
        @SerializedName("target_cost_dollars") public String targetCostDollars;
        @SerializedName("status")              public String status;           // open | closed
        @SerializedName("created_ts")          public String createdTs;
        @SerializedName("mve_collection_ticker") public String mveCollectionTicker;
        @SerializedName("mve_selected_legs")   public List<MveSelectedLeg> mveSelectedLegs;
        @SerializedName("rest_remainder")      public boolean restRemainder;
        @SerializedName("cancellation_reason") public String cancellationReason;
        @SerializedName("creator_user_id")     public String creatorUserId;
        @SerializedName("cancelled_ts")        public String cancelledTs;
        @SerializedName("updated_ts")          public String updatedTs;
    }

    public static class GetRFQsResponse {
        @SerializedName("rfqs")   public List<RFQ> rfqs;
        @SerializedName("cursor") public String cursor;
    }

    public static class GetRFQResponse {
        @SerializedName("rfq") public RFQ rfq;
    }

    public static class CreateRFQRequest {
        @SerializedName("market_ticker")            public String marketTicker;
        @SerializedName("contracts")                public Integer contracts;
        @SerializedName("contracts_fp")             public String contractsFp;
        @SerializedName("target_cost_centi_cents")  public Long targetCostCentiCents; // deprecated
        @SerializedName("target_cost_dollars")      public String targetCostDollars;
        @SerializedName("rest_remainder")           public boolean restRemainder;
        @SerializedName("replace_existing")         public Boolean replaceExisting;
        @SerializedName("subtrader_id")             public String subtraderId;
        @SerializedName("subaccount")               public Integer subaccount;
    }

    public static class CreateRFQResponse {
        @SerializedName("id") public String id;
    }

    public static class Quote {
        @SerializedName("id")                      public String id;
        @SerializedName("rfq_id")                  public String rfqId;
        @SerializedName("creator_id")              public String creatorId;
        @SerializedName("rfq_creator_id")          public String rfqCreatorId;
        @SerializedName("market_ticker")            public String marketTicker;
        @SerializedName("contracts_fp")             public String contractsFp;
        @SerializedName("yes_bid_dollars")          public String yesBidDollars;
        @SerializedName("no_bid_dollars")           public String noBidDollars;
        @SerializedName("created_ts")               public String createdTs;
        @SerializedName("updated_ts")               public String updatedTs;
        @SerializedName("status")                   public String status;
        @SerializedName("accepted_side")            public String acceptedSide;
        @SerializedName("accepted_ts")              public String acceptedTs;
        @SerializedName("confirmed_ts")             public String confirmedTs;
        @SerializedName("executed_ts")              public String executedTs;
        @SerializedName("cancelled_ts")             public String cancelledTs;
        @SerializedName("rest_remainder")           public boolean restRemainder;
        @SerializedName("cancellation_reason")      public String cancellationReason;
        @SerializedName("creator_user_id")          public String creatorUserId;
        @SerializedName("rfq_creator_user_id")      public String rfqCreatorUserId;
        @SerializedName("rfq_target_cost_dollars")  public String rfqTargetCostDollars;
        @SerializedName("rfq_creator_order_id")     public String rfqCreatorOrderId;
        @SerializedName("creator_order_id")         public String creatorOrderId;
        @SerializedName("yes_contracts_fp")         public String yesContractsFp;
        @SerializedName("no_contracts_fp")          public String noContractsFp;
    }

    public static class GetQuotesResponse {
        @SerializedName("quotes")  public List<Quote> quotes;
        @SerializedName("cursor")  public String cursor;
    }

    public static class GetQuoteResponse {
        @SerializedName("quote") public Quote quote;
    }

    public static class CreateQuoteRequest {
        @SerializedName("rfq_id")        public String rfqId;
        @SerializedName("yes_bid")       public String yesBid;
        @SerializedName("no_bid")        public String noBid;
        @SerializedName("rest_remainder") public boolean restRemainder;
        @SerializedName("subaccount")    public Integer subaccount;
    }

    public static class CreateQuoteResponse {
        @SerializedName("id") public String id;
    }

    public static class AcceptQuoteRequest {
        @SerializedName("accepted_side") public String acceptedSide; // yes | no
    }


    // =========================================================================
    // MULTIVARIATE EVENT COLLECTIONS
    // =========================================================================

    public static class AssociatedEvent {
        @SerializedName("ticker")          public String ticker;
        @SerializedName("is_yes_only")     public boolean isYesOnly;
        @SerializedName("size_max")        public Integer sizeMax;   // nullable
        @SerializedName("size_min")        public Integer sizeMin;   // nullable
        @SerializedName("active_quoters")  public List<String> activeQuoters;
    }

    public static class MultivariateEventCollection {
        @SerializedName("collection_ticker")           public String collectionTicker;
        @SerializedName("series_ticker")               public String seriesTicker;
        @SerializedName("title")                       public String title;
        @SerializedName("description")                 public String description;
        @SerializedName("open_date")                   public String openDate;
        @SerializedName("close_date")                  public String closeDate;
        @SerializedName("associated_events")           public List<AssociatedEvent> associatedEvents;
        @SerializedName("associated_event_tickers")    public List<String> associatedEventTickers; // deprecated
        @SerializedName("is_ordered")                  public boolean isOrdered;
        @SerializedName("is_single_market_per_event")  public boolean isSingleMarketPerEvent; // deprecated
        @SerializedName("is_all_yes")                  public boolean isAllYes;               // deprecated
        @SerializedName("size_min")                    public int sizeMin;
        @SerializedName("size_max")                    public int sizeMax;
        @SerializedName("functional_description")      public String functionalDescription;
    }

    public static class GetMultivariateEventCollectionResponse {
        @SerializedName("multivariate_contract") public MultivariateEventCollection multivariateContract;
    }

    public static class GetMultivariateEventCollectionsResponse {
        @SerializedName("multivariate_contracts") public List<MultivariateEventCollection> multivariateContracts;
        @SerializedName("cursor")                 public String cursor;
    }

    public static class LookupTickersForMarketInMVECollectionRequest {
        @SerializedName("selected_markets") public List<TickerPair> selectedMarkets;
    }

    public static class LookupTickersForMarketInMVECollectionResponse {
        @SerializedName("event_ticker")  public String eventTicker;
        @SerializedName("market_ticker") public String marketTicker;
    }

    public static class LookupPoint {
        @SerializedName("event_ticker")    public String eventTicker;
        @SerializedName("market_ticker")   public String marketTicker;
        @SerializedName("selected_markets") public List<TickerPair> selectedMarkets;
        @SerializedName("last_queried_ts") public String lastQueriedTs;
    }

    public static class GetMVECollectionLookupHistoryResponse {
        @SerializedName("lookup_points") public List<LookupPoint> lookupPoints;
    }

    public static class CreateMarketInMVECollectionRequest {
        @SerializedName("selected_markets")    public List<TickerPair> selectedMarkets;
        @SerializedName("with_market_payload") public Boolean withMarketPayload;
    }

    public static class CreateMarketInMVECollectionResponse {
        @SerializedName("event_ticker")  public String eventTicker;
        @SerializedName("market_ticker") public String marketTicker;
        @SerializedName("market")        public Market market;
    }


    // =========================================================================
    // MILESTONES
    // =========================================================================

    public static class Milestone {
        @SerializedName("id")                     public String id;
        @SerializedName("category")               public String category;
        @SerializedName("type")                   public String type;
        @SerializedName("start_date")             public String startDate;
        @SerializedName("end_date")               public String endDate;        // nullable
        @SerializedName("related_event_tickers")  public List<String> relatedEventTickers;
        @SerializedName("title")                  public String title;
        @SerializedName("notification_message")   public String notificationMessage;
        @SerializedName("source_id")              public String sourceId;       // nullable
        @SerializedName("source_ids")             public Map<String, String> sourceIds;
        @SerializedName("details")                public Map<String, Object> details;
        @SerializedName("primary_event_tickers")  public List<String> primaryEventTickers;
        @SerializedName("last_updated_ts")        public String lastUpdatedTs;
    }

    public static class GetMilestoneResponse {
        @SerializedName("milestone") public Milestone milestone;
    }

    public static class GetMilestonesResponse {
        @SerializedName("milestones") public List<Milestone> milestones;
        @SerializedName("cursor")     public String cursor;
    }


    // =========================================================================
    // LIVE DATA
    // =========================================================================

    public static class LiveDataItem {
        @SerializedName("type")         public String type;
        @SerializedName("details")      public Map<String, Object> details;
        @SerializedName("milestone_id") public String milestoneId;
    }

    public static class GetLiveDataResponse {
        @SerializedName("live_data") public LiveDataItem liveData;
    }

    public static class GetLiveDatasResponse {
        @SerializedName("live_datas") public List<LiveDataItem> liveDatas;
    }


    // =========================================================================
    // INCENTIVE PROGRAMS
    // =========================================================================

    public static class IncentiveProgram {
        @SerializedName("id")                  public String id;
        @SerializedName("market_id")           public String marketId;
        @SerializedName("market_ticker")       public String marketTicker;
        @SerializedName("incentive_type")      public String incentiveType;      // liquidity | volume
        @SerializedName("start_date")          public String startDate;
        @SerializedName("end_date")            public String endDate;
        @SerializedName("period_reward")       public long periodReward;         // centi-cents
        @SerializedName("paid_out")            public boolean paidOut;
        @SerializedName("discount_factor_bps") public Integer discountFactorBps; // nullable
        @SerializedName("target_size_fp")      public String targetSizeFp;       // nullable
    }

    public static class GetIncentiveProgramsResponse {
        @SerializedName("incentive_programs") public List<IncentiveProgram> incentivePrograms;
        @SerializedName("next_cursor")        public String nextCursor;
    }


    // =========================================================================
    // SEARCH
    // =========================================================================

    public static class GetTagsForSeriesCategoriesResponse {
        @SerializedName("tags_by_categories") public Map<String, List<String>> tagsByCategories;
    }

    public static class ScopeList {
        @SerializedName("scopes") public List<String> scopes;
    }

    public static class SportFilterDetails {
        @SerializedName("scopes")       public List<String> scopes;
        @SerializedName("competitions") public Map<String, ScopeList> competitions;
    }

    public static class GetFiltersBySportsResponse {
        @SerializedName("filters_by_sports") public Map<String, SportFilterDetails> filtersBySports;
        @SerializedName("sport_ordering")    public List<String> sportOrdering;
    }


    // =========================================================================
    // STRUCTURED TARGETS
    // =========================================================================

    public static class StructuredTarget {
        @SerializedName("id")              public String id;
        @SerializedName("name")            public String name;
        @SerializedName("type")            public String type;
        @SerializedName("details")         public Map<String, Object> details;
        @SerializedName("source_id")       public String sourceId;
        @SerializedName("source_ids")      public Map<String, String> sourceIds;
        @SerializedName("last_updated_ts") public String lastUpdatedTs;
    }

    public static class GetStructuredTargetsResponse {
        @SerializedName("structured_targets") public List<StructuredTarget> structuredTargets;
        @SerializedName("cursor")             public String cursor;
    }

    public static class GetStructuredTargetResponse {
        @SerializedName("structured_target") public StructuredTarget structuredTarget;
    }
}
