package tw.openedu.android.event;

import android.support.annotation.NonNull;

import tw.openedu.android.user.Account;

public class AccountUpdatedEvent {
    @NonNull
    private final Account account;

    public AccountUpdatedEvent(@NonNull Account account) {
        this.account = account;
    }

    @NonNull
    public Account getAccount() {
        return account;
    }
}
