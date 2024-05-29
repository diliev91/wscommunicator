package com.example.client;

import com.example.dto.OrderBookUpdate;
import com.example.manager.OrderBookManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class KrakenClientTest {

    @Mock
    private DataFormatterUtil dataFormatter;

    @Mock
    private OrderBookManager manager;

    @Mock
    private WebSocketSession session;

    @Captor
    private ArgumentCaptor<OrderBookUpdate> updateCaptor;

    @InjectMocks
    private KrakenClient krakenClient;

    private final List<String> pairsList = Arrays.asList("BTC/USD", "ETH/USD");
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        krakenClient = new KrakenClient(dataFormatter, pairsList, manager);
    }

    @Test
    public void testAfterConnectionEstablished() throws Exception {
        krakenClient.afterConnectionEstablished(session);
        verify(session, times(1)).sendMessage(any(TextMessage.class));
    }

    @Test
    public void testHandleTextMessage_WithEventMessage() throws Exception {
        TextMessage message = new TextMessage("{\"event\":\"subscriptionStatus\"}");
        krakenClient.handleTextMessage(session, message);
        verify(manager, never()).handleUpdate(any(OrderBookUpdate.class));
    }

    @Test
    public void testHandleTextMessage_WithOrderBookUpdate() throws Exception {
        String payload = "[1, {\"as\":[[\"0.05000\",\"0.00000304\",\"1582905487.439814\"]], \"bs\":[[\"0.04500\",\"0.00000250\",\"1582905487.439814\"]]}, \"book-10\", \"BTC/USD\"]";
        TextMessage message = new TextMessage(payload);

        when(dataFormatter.format("0.05000")).thenReturn("5000");
        when(dataFormatter.format("0.00000304")).thenReturn("00000304");
        when(dataFormatter.format("0.04500")).thenReturn("4500");
        when(dataFormatter.format("0.00000250")).thenReturn("00000250");

        krakenClient.handleTextMessage(session, message);

        verify(manager, times(2)).handleUpdate(updateCaptor.capture());

        List<OrderBookUpdate> capturedUpdates = updateCaptor.getAllValues();
        assertEquals(2, capturedUpdates.size());

        OrderBookUpdate askUpdate = capturedUpdates.get(0);
        assertEquals("BTC/USD", askUpdate.pair());
        assertEquals(new BigDecimal("5000"), askUpdate.price());
        assertEquals(304, askUpdate.amount());
        assertTrue(askUpdate.isAsk());

        OrderBookUpdate bidUpdate = capturedUpdates.get(1);
        assertEquals("BTC/USD", bidUpdate.pair());
        assertEquals(new BigDecimal("4500"), bidUpdate.price());
        assertEquals(250, bidUpdate.amount());
    }
}
