package com.zekisanmobile.petsitter2.event.job;

import android.support.annotation.Nullable;

public class FetchedSitterJobsEvent {

    private final boolean success;
    @Nullable
    private final Long sitterId;

    public FetchedSitterJobsEvent(boolean success, @Nullable Long sitterId) {
        this.success = success;
        this.sitterId = sitterId;
    }

    public boolean isSuccess() {
        return success;
    }

    @Nullable
    public Long getSitterId() {
        return sitterId;
    }

}
