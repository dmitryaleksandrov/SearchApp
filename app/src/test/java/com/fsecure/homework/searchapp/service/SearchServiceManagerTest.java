package com.fsecure.homework.searchapp.service;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchServiceManagerTest {

    private static final String APP_NAME = "Email";
    private static final String PKG_NAME = "com.fsecure.homework.searchapp";

    private Context context;
    private ExecutorService service;
    private PackageManager packageManager;
    private Handler handler;

    @Before
    public void setUp() {
        packageManager = mock(PackageManager.class);

        context = mock(Context.class);
        when(context.getPackageManager()).thenReturn(packageManager);

        service = mock(ExecutorService.class);
        when(service.submit(any(Runnable.class))).thenAnswer(new Answer<Future<?>>() {
            @Override
            public Future<?> answer(InvocationOnMock invocation) throws Throwable {
                Runnable runnable = invocation.getArgumentAt(0, Runnable.class);
                runnable.run();
                return null;
            }
        });

        handler = mock(Handler.class);
        when(handler.post(any(Runnable.class))).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                Runnable runnable = invocation.getArgumentAt(0, Runnable.class);
                runnable.run();
                return true;
            }
        });
    }

    @Test
    public void searchApp_Success() throws RemoteException {
        ApplicationInfo appInfo = mock(ApplicationInfo.class);

        when(packageManager.getInstalledApplications(0))
                .thenReturn(Collections.singletonList(appInfo));
        when(packageManager.getApplicationLabel(appInfo))
                .thenReturn(APP_NAME);


        SearchServiceManager.SearchAppMessenger messenger =
                mock(SearchServiceManager.SearchAppMessenger.class);

        SearchServiceManagerImpl serviceManager = new SearchServiceManagerImpl(
                context, service, 0, handler);

        serviceManager.handleSearchAppRequest(APP_NAME, PKG_NAME, messenger);

        ArgumentCaptor<ArrayList> argument = ArgumentCaptor.forClass(ArrayList.class);
        verify(messenger).send(argument.capture());

        ArrayList appInfoList = argument.getValue();
        assertEquals(1, appInfoList.size());
        assertEquals(appInfo, appInfoList.get(0));
    }

    @Test
    public void searchApp_MessengerThrows() throws RemoteException {
        ApplicationInfo appInfo = mock(ApplicationInfo.class);

        when(packageManager.getInstalledApplications(0))
                .thenReturn(Collections.singletonList(appInfo));
        when(packageManager.getApplicationLabel(appInfo))
                .thenReturn(APP_NAME);

        SearchServiceManager.SearchAppMessenger messenger =
                mock(SearchServiceManager.SearchAppMessenger.class);

        ArgumentCaptor<ArrayList> argument = ArgumentCaptor.forClass(ArrayList.class);
        doThrow(new RemoteException()).doNothing().when(messenger).send(argument.capture());

        SearchServiceManagerImpl serviceManager = new SearchServiceManagerImpl(
                context, service, 0, handler);

        serviceManager.handleSearchAppRequest(APP_NAME, PKG_NAME, messenger);

        serviceManager.handleSearchAppRequest(null, PKG_NAME, messenger);

        ArrayList appInfoList = argument.getValue();
        assertEquals(1, appInfoList.size());
        assertEquals(appInfo, appInfoList.get(0));
    }
}