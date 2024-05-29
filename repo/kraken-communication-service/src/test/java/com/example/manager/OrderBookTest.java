package com.example.manager;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

public class OrderBookTest {
    String BTC_TO_USD_EXPECTED_OUTPUT = """
                <------------------------------------>
                asks:
                [ 7141, 112066 ]
                [ 7140.5, 76475 ]
                [ 7140, 288838 ]
                [ 7139.5, 47357 ]
                [ 7139, 137805 ]
                [ 7138.5, 98725 ]
                [ 7138, 83993 ]
                [ 7137.5, 804679 ]
                [ 7137, 33460 ]
                [ 7136.5, 182304 ]
                best bid: [ 7136, 30500 ]
                best ask: [ 7136.5, 182304 ]
                bids:
                [ 7136, 30500 ]
                [ 7135.5, 605 ]
                [ 7135, 26356 ]
                [ 7134.5, 11417 ]
                [ 7134, 454 ]
                [ 7133.5, 2118 ]
                [ 7133, 114987 ]
                [ 7132.5, 14560 ]
                [ 7132, 3167 ]
                [ 7131.5, 27073 ]
                >------------------------------------<
                """;

    String ETH_TO_USD_EXPECTED_OUTPUT = """
            <------------------------------------>
            asks:
            [ 7141, 112066 ]
            [ 7140.5, 76475 ]
            [ 7140, 288838 ]
            [ 7139.5, 47357 ]
            [ 7139, 137805 ]
            [ 7138.5, 98725 ]
            [ 7138, 83993 ]
            [ 7137.5, 804679 ]
            [ 7137, 33460 ]
            [ 7136.5, 182307 ]
            best bid: [ 7136, 30510 ]
            best ask: [ 7136.5, 182307 ]
            bids:
            [ 7136, 30510 ]
            [ 7135.5, 605 ]
            [ 7135, 26356 ]
            [ 7134.5, 11417 ]
            [ 7134, 454 ]
            [ 7133.5, 2118 ]
            [ 7133, 114987 ]
            [ 7132.5, 14560 ]
            [ 7132, 3167 ]
            [ 7131.5, 27073 ]
            >------------------------------------<
            """;
    @Test
    void testPrintBtc() throws IOException {
        OrderBook underTest = new OrderBook();
        underTest.updateAsk(new BigDecimal("7141"), 112066L);
        underTest.updateAsk(new BigDecimal("7140.5"), 76475L);
        underTest.updateAsk(new BigDecimal("7140"), 288838L);
        underTest.updateAsk(new BigDecimal("7139.5"), 47357L);
        underTest.updateAsk(new BigDecimal("7139"), 137805L);
        underTest.updateAsk(new BigDecimal("7138.5"), 98725L);
        underTest.updateAsk(new BigDecimal("7138"),  83993L);
        underTest.updateAsk(new BigDecimal("7137.5"), 804679L);
        underTest.updateAsk(new BigDecimal("7137"), 33460L);
        underTest.updateAsk(new BigDecimal("7136.5"), 182304L);

        underTest.updateBid(new BigDecimal("7136"), 30500L);
        underTest.updateBid(new BigDecimal("7135.5"), 605L);
        underTest.updateBid(new BigDecimal("7135"), 26356L);
        underTest.updateBid(new BigDecimal("7134.5"), 11417L);
        underTest.updateBid(new BigDecimal("7134"), 454L);
        underTest.updateBid(new BigDecimal("7133.5"), 2118L);
        underTest.updateBid(new BigDecimal("7133"), 114987L);
        underTest.updateBid(new BigDecimal("7132.5"), 14560L);
        underTest.updateBid(new BigDecimal("7132"),  3167L);
        underTest.updateBid(new BigDecimal("7131.5"),  27073L);

        assert underTest.toString().equals(BTC_TO_USD_EXPECTED_OUTPUT);
    }

    @Test
    void testPrintEth() throws IOException {
        OrderBook underTest = new OrderBook();
        underTest.updateAsk(new BigDecimal("7141"), 112066L);
        underTest.updateAsk(new BigDecimal("7140.5"), 76475L);
        underTest.updateAsk(new BigDecimal("7139"), 137805L);
        underTest.updateAsk(new BigDecimal("7137"), 33460L);
        underTest.updateAsk(new BigDecimal("7138.5"), 98725L);
        underTest.updateAsk(new BigDecimal("7138"), 83993L);
        underTest.updateAsk(new BigDecimal("7139.5"), 47357L);
        underTest.updateAsk(new BigDecimal("7137.5"), 804679L);
        underTest.updateAsk(new BigDecimal("7140"), 288838L);
        underTest.updateAsk(new BigDecimal("7136.5"), 182307L);

        underTest.updateBid(new BigDecimal("7134"), 454L);
        underTest.updateBid(new BigDecimal("7135.5"), 605L);
        underTest.updateBid(new BigDecimal("7133.5"), 2118L);
        underTest.updateBid(new BigDecimal("7136"), 30510L);
        underTest.updateBid(new BigDecimal("7132.5"), 14560L);
        underTest.updateBid(new BigDecimal("7134.5"), 11417L);
        underTest.updateBid(new BigDecimal("7132"), 3167L);
        underTest.updateBid(new BigDecimal("7135"), 26356L);
        underTest.updateBid(new BigDecimal("7131.5"), 27073L);
        underTest.updateBid(new BigDecimal("7133"), 114987L);
        assert underTest.toString().equals(ETH_TO_USD_EXPECTED_OUTPUT);
    }

    @Test
    void testOrderingAsk() {
        OrderBook underTest = new OrderBook();
        Long expectedAmount = 1L;
        BigDecimal expectedKey = new BigDecimal("1");


        underTest.updateAsk(new BigDecimal("3"), 3L);
        underTest.updateAsk(expectedKey, expectedAmount);
        underTest.updateAsk(new BigDecimal("2"), 2L);


        Map.Entry<BigDecimal, Long> bestAsk = underTest.getBestAsk();
        assert expectedKey.equals(bestAsk.getKey());
        assert expectedAmount.equals(bestAsk.getValue());
    }

    @Test
    void testBidsOrdering() {
        OrderBook underTest = new OrderBook();
        Long expectedAmount = 3L;
        BigDecimal expectedKey = new BigDecimal("3");


        underTest.updateBid(new BigDecimal("1"), 1L);
        underTest.updateBid(expectedKey, expectedAmount);
        underTest.updateBid(new BigDecimal("2"), 2L);


        Map.Entry<BigDecimal, Long> bestBid = underTest.getBestBid();
        assert expectedKey.equals(bestBid.getKey());
        assert expectedAmount.equals(bestBid.getValue());
    }
}
