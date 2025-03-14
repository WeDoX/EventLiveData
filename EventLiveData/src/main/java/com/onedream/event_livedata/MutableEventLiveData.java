package com.onedream.event_livedata;


public class MutableEventLiveData<T> extends EventLiveData<T> {

    public MutableEventLiveData(T value) {
        super(value);
    }

    public MutableEventLiveData() {
        super();
    }


    public void setValue(T value) {
        super.setValue(value);
    }


    public void postValue(T value) {
        super.postValue(value);
    }

    public static class Builder<T> {
        private boolean isAllowNullValue;

        public Builder() {
        }

        public Builder<T> setAllowNullValue(boolean allowNullValue) {
            this.isAllowNullValue = allowNullValue;
            return this;
        }

        public MutableEventLiveData<T> build() {
            MutableEventLiveData<T> liveData = new MutableEventLiveData<>();
            liveData.setAllowNullValue(this.isAllowNullValue);
            return liveData;
        }
    }
}
