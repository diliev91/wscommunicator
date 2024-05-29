package com.example.manager;

import com.example.dto.OrderBookUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class OrderBooksManagerImplTest {

    private HashMap<String, OrderBook> orderBooks;

    @InjectMocks
    private OrderBooksManagerImpl orderBooksManager;

    @BeforeEach
    public void setUp() {
        orderBooks = mock(HashMap.class);
        orderBooksManager = new OrderBooksManagerImpl(orderBooks);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandleUpdate_NewOrderBook() {
        String pair = "BTC/USD";
        BigDecimal price = new BigDecimal("5000");
        long amount = 1000;
        OrderBookUpdate update = new OrderBookUpdate(pair, price, amount, true);

        OrderBook orderBook = mock(OrderBook.class);
        when(orderBooks.get(pair)).thenReturn(null).thenReturn(orderBook);

        orderBooksManager.handleUpdate(update);

        verify(orderBooks, times(1)).put(eq(pair), any(OrderBook.class));
    }

    @Test
    public void testHandleUpdate_ExistingOrderBook_Ask() {
        String pair = "BTC/USD";
        BigDecimal price = new BigDecimal("5000");
        long amount = 1000;
        OrderBookUpdate update = new OrderBookUpdate(pair, price, amount, true);

        OrderBook orderBook = mock(OrderBook.class);
        when(orderBooks.get(pair)).thenReturn(orderBook);

        orderBooksManager.handleUpdate(update);

        verify(orderBooks, never()).put(eq(pair), any(OrderBook.class));
        verify(orderBook, times(1)).updateAsk(price, amount);
    }

    @Test
    public void testHandleUpdate_ExistingOrderBook_Bid() {
        String pair = "BTC/USD";
        BigDecimal price = new BigDecimal("5000");
        long amount = 1000;
        OrderBookUpdate update = new OrderBookUpdate(pair, price, amount, false);

        OrderBook orderBook = mock(OrderBook.class);
        when(orderBooks.get(pair)).thenReturn(orderBook);

        orderBooksManager.handleUpdate(update);

        verify(orderBooks, never()).put(eq(pair), any(OrderBook.class));
        verify(orderBook, times(1)).updateBid(price, amount);
    }

    @Test
    public void testToString() {
        String pair = "BTC/USD";
        OrderBook orderBook = mock(OrderBook.class);
        when(orderBooks.entrySet()).thenReturn(Map.of(pair, orderBook).entrySet());

        String result = orderBooksManager.toString();

        verify(orderBooks, times(1)).entrySet();
    }
}
