package com.example.manager;

import com.example.dto.OrderBookUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
public class OrderBooksManagerImpl implements OrderBookManager {

    private final HashMap<String, OrderBook> orderBooks;
    @Autowired
    public OrderBooksManagerImpl(HashMap<String, OrderBook> orderBooks) {
        this.orderBooks = orderBooks;
    }

    @Override
    public void handleUpdate(OrderBookUpdate orderBookUpdate) {
        String pair = orderBookUpdate.pair();
        OrderBook orderBook = orderBooks.get(pair);
        if(orderBook == null) {
            log.info("Missing order book for pair: {}", pair);
            orderBook = new OrderBook();
            orderBooks.put(pair, orderBook);
        }

        if(orderBookUpdate.isAsk()) {
            orderBook.updateAsk(orderBookUpdate.price(),orderBookUpdate.amount());
        } else {
            orderBook.updateBid(orderBookUpdate.price(), orderBookUpdate.amount());
        }
        printOrderBooksToSystemOut();
    }

    @Override
    public String toString() {
        Date currentDate = new Date(); SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = dateFormat.format(currentDate);
        StringBuilderPlus builder = new StringBuilderPlus();
        for(Map.Entry<String, OrderBook> book : orderBooks.entrySet()) {
            String pair = book.getKey();
            builder.appendLine(pair);
            builder.appendLine(currentDateTime);
            builder.appendLine(book.getValue());
        }
        return builder.toString();
    }

    public void printOrderBooksToSystemOut(){
        System.out.println(this);
    }
}