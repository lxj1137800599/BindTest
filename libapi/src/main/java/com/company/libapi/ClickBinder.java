package com.company.libapi;

/**
 * Created by 刘晓杰 on 2018/12/22.
 */

public interface ClickBinder<T> {
    void bindClick(T host, Object object);

    void unBindClick(T host);
}
