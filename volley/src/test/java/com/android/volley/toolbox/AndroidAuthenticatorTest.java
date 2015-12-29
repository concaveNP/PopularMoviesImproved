/*
 *     PopularMoviesImproved is an Android application that displays the most
 *     currently trending popular movies as listed by themoviedb.org
 *     website.
 *
 *     Copyright (C) 2015 authored by David A. Todd
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

package com.android.volley.toolbox;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.android.volley.AuthFailureError;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class AndroidAuthenticatorTest {
    private AccountManager mAccountManager;
    private Account mAccount;
    private AccountManagerFuture<Bundle> mFuture;
    private AndroidAuthenticator mAuthenticator;

    @Before
    public void setUp() {
        mAccountManager = mock(AccountManager.class);
        mFuture = mock(AccountManagerFuture.class);
        mAccount = new Account("coolperson", "cooltype");
        mAuthenticator = new AndroidAuthenticator(mAccountManager, mAccount, "cooltype", false);
    }

    @Test(expected = AuthFailureError.class)
    public void failedGetAuthToken() throws Exception {
        when(mAccountManager.getAuthToken(mAccount, "cooltype", false, null, null)).thenReturn(mFuture);
        when(mFuture.getResult()).thenThrow(new AuthenticatorException("sadness!"));
        mAuthenticator.getAuthToken();
    }

    @Test(expected = AuthFailureError.class)
    public void resultContainsIntent() throws Exception {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        when(mAccountManager.getAuthToken(mAccount, "cooltype", false, null, null)).thenReturn(mFuture);
        when(mFuture.getResult()).thenReturn(bundle);
        when(mFuture.isDone()).thenReturn(true);
        when(mFuture.isCancelled()).thenReturn(false);
        mAuthenticator.getAuthToken();
    }

    @Test(expected = AuthFailureError.class)
    public void missingAuthToken() throws Exception {
        Bundle bundle = new Bundle();
        when(mAccountManager.getAuthToken(mAccount, "cooltype", false, null, null)).thenReturn(mFuture);
        when(mFuture.getResult()).thenReturn(bundle);
        when(mFuture.isDone()).thenReturn(true);
        when(mFuture.isCancelled()).thenReturn(false);
        mAuthenticator.getAuthToken();
    }

    @Test
    public void invalidateAuthToken() throws Exception {
        mAuthenticator.invalidateAuthToken("monkey");
        verify(mAccountManager).invalidateAuthToken("cooltype", "monkey");
    }

    @Test
    public void goodToken() throws Exception {
        Bundle bundle = new Bundle();
        bundle.putString(AccountManager.KEY_AUTHTOKEN, "monkey");
        when(mAccountManager.getAuthToken(mAccount, "cooltype", false, null, null)).thenReturn(mFuture);
        when(mFuture.getResult()).thenReturn(bundle);
        when(mFuture.isDone()).thenReturn(true);
        when(mFuture.isCancelled()).thenReturn(false);
        Assert.assertEquals("monkey", mAuthenticator.getAuthToken());
    }

    @Test
    public void publicMethods() throws Exception {
        // Catch-all test to find API-breaking changes.
        Context context = mock(Context.class);
        new AndroidAuthenticator(context, mAccount, "cooltype");
        new AndroidAuthenticator(context, mAccount, "cooltype", true);
        Assert.assertSame(mAccount, mAuthenticator.getAccount());
    }
}
