package com.fsk.framework.core.async;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Component
public class AbstractAsyncServiceInvoker {

    private ExecutorService executorService;

    private int getCoreSize() {
        return 10;
    }

    private int getMaxSize() {
        return 10;
    }

    private int getQueueSize() {
        return 20;
    }

    public AbstractAsyncServiceInvoker() {
    }

    @PostConstruct
    private void init() {
        int coreSize = getCoreSize();
        int maxSize = getMaxSize();
        int queueSize = getQueueSize();
        long keepAliveTime = 10L;

        executorService = new ThreadPoolExecutor(
                coreSize,
                maxSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(queueSize));
    }

    @PreDestroy
    private void destory() {
        try {
            log.info("executorService.destory()");
            if (executorService == null) {
                return;
            }
            executorService.shutdown();
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                log.info("executorService.shutdown.start...");
                List<Runnable> desoryThreadList = executorService.shutdownNow();

                log.info("executorService.shutdown.end...desoryThreadList.size()={}",
                        CollectionUtils.isNotEmpty(desoryThreadList) ? desoryThreadList.size() : 0);
            }
        } catch (Exception e) {
            log.error("executorService.destory().error=", e);
        } finally {
            log.info("executorService.destory().end");
        }
    }

    public List<Object> asyncSubmit(List<AsyncServiceParam> params) {

        List<Future<Object>> futureList = submitFuture(params);

        List<Object> responses = callBackAsyncResponse(futureList);

        return responses;
    }


    public List<Future<Object>> submitFuture(List<AsyncServiceParam> params) {
        List<Future<Object>> futureList = new ArrayList<>();
        for (int i = 0; i < params.size(); i++) {
            final AsyncServiceParam param = params.get(i);
            try {
                Future<Object> responses = executorService.submit(new AsyncTaskCallable(param));
                futureList.add(responses);
            } catch (Exception e) {
                log.error("AbstractAsyncServiceInvoker.submitFuture.Exception=",e);
            }
        }
        return futureList;
    }

    public List<Object> callBackAsyncResponse(List<Future<Object>> futureList) {
        List<Object> responses = new ArrayList<>();
        for(Future<Object> responseFuture : futureList){
            try {
                Object iAsyncServiceResponse = responseFuture.get(6L, TimeUnit.SECONDS);
                responses.add(iAsyncServiceResponse);
            } catch (InterruptedException e) {
                log.error("AbstractAsyncServiceInvoker.getResponse.InterruptedException=",e);
            } catch (ExecutionException e) {
                log.error("AbstractAsyncServiceInvoker.getResponse.ExecutionException=",e);
            } catch (TimeoutException e) {
                log.error("AbstractAsyncServiceInvoker.getResponse.TimeoutException=",e);
            }
        }

        return responses;
    }
}
