package dev.codethat.moneyplant.core.adapter;

public interface StreamingAdapter<T> {
    Object connect();

    Object subscribe();

    Object unsubscribe();
}
