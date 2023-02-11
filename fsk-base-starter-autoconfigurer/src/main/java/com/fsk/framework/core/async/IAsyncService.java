package com.fsk.framework.core.async;

public interface IAsyncService {
    IAsyncServiceResponse invoke(IAsyncServiceRequest request, AsyncServiceMethod method);
}
