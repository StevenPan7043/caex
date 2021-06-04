package com.contract.service.thead;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.contract.entity.CContractOrder;
import com.contract.exception.ThrowJsonException;
import com.contract.service.api.ApiTradeService;

public class HandleTimeoutTask implements Runnable {
    List<CContractOrder> list;
    String outKey;
    ApiTradeService apiTradeService;
    int i;

    public HandleTimeoutTask(List<CContractOrder> list, String outKey, ApiTradeService apiTradeService) {
        this.list = list;
        this.outKey = outKey;
        this.apiTradeService = apiTradeService;
    }

    @Override
    public void run() {
        Lock lock = new ReentrantLock();
        try {
            lock.lock();
            orderTimeout();
        } catch (Exception e) {
            throw new ThrowJsonException(e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    /**
     * 订单失效
     */
    public synchronized void orderTimeout() {
        apiTradeService.handleorderTimeout(list, outKey);
    }

}