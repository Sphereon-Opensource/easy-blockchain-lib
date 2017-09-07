package com.sphereon.ms.blockchain.api.model;

public interface HasValue<T> {
    T getValue();

    class Impl<T> implements HasValue<T> {
        final T value;

        protected Impl(T value) {
            this.value = value;
        }

        public static <T> Impl of(T value) {
            return new Impl(value);
        }

        @Override
        public T getValue() {
            return value;
        }
    }
}
