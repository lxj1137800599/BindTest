package com.company.libapi;

/**
 * Created by 刘晓杰 on 2018/11/11.
 */

import android.view.View;

/**
 * ui提供者接口
 */
public interface ViewFinder {

    View findView(Object object, int id);
}