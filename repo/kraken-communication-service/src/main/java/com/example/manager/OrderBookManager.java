package com.example.manager;

import com.example.dto.OrderBookUpdate;

public interface OrderBookManager {

    void handleUpdate(OrderBookUpdate orderBookUpdate);
}
