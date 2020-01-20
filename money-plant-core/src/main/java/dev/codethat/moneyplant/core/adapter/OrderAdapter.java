package dev.codethat.moneyplant.core.adapter;

public interface OrderAdapter<T> {
    Object place();

    Object replace();

    Object order();

    Object cancel();

    Object orders();
}
