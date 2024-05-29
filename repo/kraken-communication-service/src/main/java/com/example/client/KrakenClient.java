package com.example.client;

import com.example.manager.OrderBookManager;
import com.example.dto.OrderBookUpdate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
public class KrakenClient extends TextWebSocketHandler {
    private static final String KRAKEN_WS_URL = "wss://ws.kraken.com";
    private static final String SUBSCRIBE_TEMPLATE = "{ \"event\": \"subscribe\", \"pair\": [%s], \"subscription\": { \"name\": \"book\" } }";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DataFormatterUtil dataFormatter;
    private final List<String> pairsList;
    private final OrderBookManager manager;

    @Autowired
    public KrakenClient(DataFormatterUtil dataFormatter, @Value("${pairs.list}") List<String> pairsList, OrderBookManager manager) {
        this.dataFormatter = dataFormatter;
        this.pairsList = pairsList;
        this.manager = manager;
    }

    @PostConstruct
    public void connect() {
        StandardWebSocketClient client = new StandardWebSocketClient();
        client.doHandshake(this, new WebSocketHttpHeaders(), URI.create(KRAKEN_WS_URL));
    }

    @Override
    public void afterConnectionEstablished(org.springframework.web.socket.WebSocketSession session) throws Exception {
        String pairsJson = pairsList.stream()
                .map(pair -> "\"" + pair + "\"")
                .collect(Collectors.joining(","));
        session.sendMessage(new TextMessage(String.format(SUBSCRIBE_TEMPLATE, pairsJson)));
    }

    @Override
    protected void handleTextMessage(org.springframework.web.socket.WebSocketSession session, TextMessage message) throws JsonProcessingException {
        String payload = message.getPayload();
        if (payload.contains("\"event\":")) {
            log.info("Event message: {}", payload);
            return;
        }

        JsonNode jsonNode = objectMapper.readTree(payload);
        String pair = jsonNode.get(3).asText();
        JsonNode updatesNode = jsonNode.get(1);
        KrakenUpdates updates = objectMapper.treeToValue(updatesNode, KrakenUpdates.class);

        processUpdates(pair, updates.getAsks(), true);
        processUpdates(pair, updates.getBids(), false);
        processUpdates(pair, updates.getAskUpdates(), true);
        processUpdates(pair, updates.getBidUpdates(), false);

    }
    private void processUpdates(String pair, List<List<String>> updates, boolean isAsk) {
        if (updates != null) {
            for (List<String> u : updates) {
                OrderBookUpdate update = new OrderBookUpdate(pair, new BigDecimal(dataFormatter.format(u.get(0))), Long.parseLong(dataFormatter.format(u.get(1))), isAsk);
                manager.handleUpdate(update);
            }
        }
    }

}