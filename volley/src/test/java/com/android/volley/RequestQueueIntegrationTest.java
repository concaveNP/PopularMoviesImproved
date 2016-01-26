/*
 *     PopularMoviesImproved is an Android application that displays the most
 *     currently trending popular movies as listed by themoviedb.org
 *     website.
 *
 *     Copyright (C) 2016 authored by David A. Todd
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     You can contact the author via https://github.com/concaveNP
 */

package com.android.volley;

import com.android.volley.Request.Priority;
import com.android.volley.RequestQueue.RequestFinishedListener;
import com.android.volley.mock.MockRequest;
import com.android.volley.mock.ShadowSystemClock;
import com.android.volley.toolbox.NoCache;
import com.android.volley.utils.ImmediateResponseDelivery;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


/**
 * Integration tests for {@link RequestQueue}, that verify its behavior in conjunction with real dispatcher, queues and
 * Requests. Network is mocked out
 */
@RunWith(RobolectricTestRunner.class)
@Config(shadows = {ShadowSystemClock.class})
public class RequestQueueIntegrationTest {

    private ResponseDelivery mDelivery;
    @Mock private Network mMockNetwork;

    @Before public void setUp() throws Exception {
        mDelivery = new ImmediateResponseDelivery();
        initMocks(this);
    }

    @Test public void add_requestProcessedInCorrectOrder() throws Exception {
        // Enqueue 2 requests with different cache keys, and different priorities. The second, higher priority request
        // takes 20ms.
        // Assert that first request is only handled after the first one has been parsed and delivered.
        MockRequest lowerPriorityReq = new MockRequest();
        MockRequest higherPriorityReq = new MockRequest();
        lowerPriorityReq.setCacheKey("1");
        higherPriorityReq.setCacheKey("2");
        lowerPriorityReq.setPriority(Priority.LOW);
        higherPriorityReq.setPriority(Priority.HIGH);

        RequestFinishedListener listener = mock(RequestFinishedListener.class);
        Answer<NetworkResponse> delayAnswer = new Answer<NetworkResponse>() {
            @Override
            public NetworkResponse answer(InvocationOnMock invocationOnMock) throws Throwable {
                Thread.sleep(20);
                return mock(NetworkResponse.class);
            }
        };
        //delay only for higher request
        when(mMockNetwork.performRequest(higherPriorityReq)).thenAnswer(delayAnswer);
        when(mMockNetwork.performRequest(lowerPriorityReq)).thenReturn(mock(NetworkResponse.class));

        RequestQueue queue = new RequestQueue(new NoCache(), mMockNetwork, 1, mDelivery);
        queue.addRequestFinishedListener(listener);
        queue.add(lowerPriorityReq);
        queue.add(higherPriorityReq);
        queue.start();

        // you cannot do strict order verification in combination with timeouts with mockito 1.9.5 :(
        // as an alternative, first verify no requests have finished, while higherPriorityReq should be processing
        verifyNoMoreInteractions(listener);
        // verify higherPriorityReq goes through first
        verify(listener, timeout(100)).onRequestFinished(higherPriorityReq);
        // verify lowerPriorityReq goes last
        verify(listener, timeout(10)).onRequestFinished(lowerPriorityReq);
        queue.stop();
    }

    /**
     * Asserts that requests with same cache key are processed in order.
     *
     * Needs to be an integration test because relies on complex interations between various queues
     */
    @Test public void add_dedupeByCacheKey() throws Exception {
        // Enqueue 2 requests with the same cache key. The first request takes 20ms. Assert that the
        // second request is only handled after the first one has been parsed and delivered.
        Request req1 = new MockRequest();
        Request req2 = new MockRequest();
        RequestFinishedListener listener = mock(RequestFinishedListener.class);
        Answer<NetworkResponse> delayAnswer = new Answer<NetworkResponse>() {
            @Override
            public NetworkResponse answer(InvocationOnMock invocationOnMock) throws Throwable {
                Thread.sleep(20);
                return mock(NetworkResponse.class);
            }
        };
        //delay only for first
        when(mMockNetwork.performRequest(req1)).thenAnswer(delayAnswer);
        when(mMockNetwork.performRequest(req2)).thenReturn(mock(NetworkResponse.class));

        RequestQueue queue = new RequestQueue(new NoCache(), mMockNetwork, 3, mDelivery);
        queue.addRequestFinishedListener(listener);
        queue.add(req1);
        queue.add(req2);
        queue.start();

        // you cannot do strict order verification with mockito 1.9.5 :(
        // as an alternative, first verify no requests have finished, then verify req1 goes through
        verifyNoMoreInteractions(listener);
        verify(listener, timeout(100)).onRequestFinished(req1);
        verify(listener, timeout(10)).onRequestFinished(req2);
        queue.stop();
    }

    /**
     * Verify RequestFinishedListeners are informed when requests are canceled
     *
     * Needs to be an integration test because relies on Request -> dispatcher -> RequestQueue interaction
     */
    @Test public void add_requestFinishedListenerCanceled() throws Exception {
        RequestFinishedListener listener = mock(RequestFinishedListener.class);
        Request request = new MockRequest();
        Answer<NetworkResponse> delayAnswer = new Answer<NetworkResponse>() {
            @Override
            public NetworkResponse answer(InvocationOnMock invocationOnMock) throws Throwable {
                Thread.sleep(200);
                return mock(NetworkResponse.class);
            }
        };
        RequestQueue queue = new RequestQueue(new NoCache(), mMockNetwork, 1, mDelivery);

        when(mMockNetwork.performRequest(request)).thenAnswer(delayAnswer);

        queue.addRequestFinishedListener(listener);
        queue.start();
        queue.add(request);

        request.cancel();
        verify(listener, timeout(100)).onRequestFinished(request);
        queue.stop();
    }

    /**
     * Verify RequestFinishedListeners are informed when requests are successfully delivered
     *
     * Needs to be an integration test because relies on Request -> dispatcher -> RequestQueue interaction
     */
    @Test public void add_requestFinishedListenerSuccess() throws Exception {
        NetworkResponse response = mock(NetworkResponse.class);
        Request request = new MockRequest();
        RequestFinishedListener listener = mock(RequestFinishedListener.class);
        RequestFinishedListener listener2 = mock(RequestFinishedListener.class);
        RequestQueue queue = new RequestQueue(new NoCache(), mMockNetwork, 1, mDelivery);

        queue.addRequestFinishedListener(listener);
        queue.addRequestFinishedListener(listener2);
        queue.start();
        queue.add(request);

        verify(listener, timeout(100)).onRequestFinished(request);
        verify(listener2, timeout(100)).onRequestFinished(request);

        queue.stop();
    }

    /**
     * Verify RequestFinishedListeners are informed when request errors
     *
     * Needs to be an integration test because relies on Request -> dispatcher -> RequestQueue interaction
     */
    @Test public void add_requestFinishedListenerError() throws Exception {
        RequestFinishedListener listener = mock(RequestFinishedListener.class);
        Request request = new MockRequest();
        RequestQueue queue = new RequestQueue(new NoCache(), mMockNetwork, 1, mDelivery);

        when(mMockNetwork.performRequest(request)).thenThrow(new VolleyError());

        queue.addRequestFinishedListener(listener);
        queue.start();
        queue.add(request);

        verify(listener, timeout(100)).onRequestFinished(request);
        queue.stop();
    }

}
