package com.contract.service.thead;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.contract.exception.ThrowJsonException;
import com.contract.service.api.ApiTradeService;

/**
 * 止盈止损
 *
 * @author arno
 */
public class HandleOrderTask implements Runnable {
    List<Map<String, Object>> orderList;
    ApiTradeService apiTradeService;

    public HandleOrderTask(List<Map<String, Object>> orderList, ApiTradeService apiTradeService) {
        this.orderList = orderList;
        this.apiTradeService = apiTradeService;
    }

    @Override
    public void run() {
        Lock lock = new ReentrantLock();
        try {
            lock.lock();
            handleCloseMore();
        } catch (Exception e) {
            throw new ThrowJsonException(e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    /**
     * 订单失效
     */
    public synchronized void handleCloseMore() {
        apiTradeService.handleCloseMore(orderList);
    }

}