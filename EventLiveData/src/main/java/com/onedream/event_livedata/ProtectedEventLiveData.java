package com.onedream.event_livedata;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


class ProtectedEventLiveData<T> extends LiveData<T> {

    public ProtectedEventLiveData(T value) {
        super(value);
    }

    public ProtectedEventLiveData() {
        super();
    }

    private final static int START_VERSION = -1;

    private final AtomicInteger mCurrentVersion = new AtomicInteger(START_VERSION);

    private boolean isAllowNullValue;

    public void setAllowNullValue(boolean allowNullValue) {
        isAllowNullValue = allowNullValue;
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        super.observe(owner, createObserverWrapper(observer, mCurrentVersion.get()));
    }


    @Override
    public void observeForever(@NonNull Observer<? super T> observer) {
        super.observeForever(createObserverWrapper(observer, mCurrentVersion.get()));
    }


    @Override
    protected void setValue(T value) {
        mCurrentVersion.getAndIncrement();
        super.setValue(value);
    }


    class ObserverWrapper implements Observer<T> {
        private final Observer<? super T> mObserver;
        private final int mVersion;

        public ObserverWrapper(@NonNull Observer<? super T> observer, int version) {
            this.mObserver = observer;
            this.mVersion = version;
        }

        @Override
        public void onChanged(T t) {
            if (mCurrentVersion.get() > mVersion && (t != null || isAllowNullValue)) {
                mObserver.onChanged(t);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ObserverWrapper that = (ObserverWrapper) o;
            return Objects.equals(mObserver, that.mObserver);
        }

        @Override
        public int hashCode() {
            return Objects.hash(mObserver);
        }
    }


    @Override
    public void removeObserver(@NonNull Observer<? super T> observer) {
        if (observer.getClass().isAssignableFrom(ObserverWrapper.class)) {
            super.removeObserver(observer);
        } else {
            super.removeObserver(createObserverWrapper(observer, START_VERSION));
        }
    }

    private ObserverWrapper createObserverWrapper(@NonNull Observer<? super T> observer, int version) {
        return new ObserverWrapper(observer, version);
    }


    public void clear() {
        super.setValue(null);
    }

}
