package com.sphereon.ms.blockchain.api.model;

public interface HasContent<T> {
    T getContent();

    class Impl<T> implements HasContent<T> {
        final T content;

        protected Impl(T content) {
            this.content = content;
        }

        public static <T> Impl of(T content) {
            return new Impl(content);
        }

        @Override
        public T getContent() {
            return content;
        }
    }
}
