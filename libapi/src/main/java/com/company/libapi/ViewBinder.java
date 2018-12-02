package com.company.libapi;


/**
 * UI绑定解绑接口
 *
 * @param <T>
 */
public interface ViewBinder<T> {

    void bindView(T host, Object object, ViewFinder finder);

    void unBindView(T host);
}
