package org.coursera.capstone.t1dteensclient.provider;

import android.accounts.Account;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

public class ContentProviderObserver extends ContentObserver {

    private Account account;

    public ContentProviderObserver(Handler handler, Account account) {
        super(handler);
        this.account = account;
    }

    @Override
    public void onChange(boolean selfChange) {
        onChange(selfChange, null);
    }

    @Override
    public void onChange(boolean selfChange, Uri changeUri) {

        Bundle extras = new Bundle();
        extras.putString("uri", changeUri.toString());
        ContentResolver.requestSync(account, ServiceContract.AUTHORITY, extras);
    }
}