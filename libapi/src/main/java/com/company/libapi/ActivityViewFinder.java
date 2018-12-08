package com.company.libapi;

import android.app.Activity;
import android.view.View;


/**
 * 这个其实可以和ViewFinder写成一个文件，都是提供具体findViewById怎么实现的。之所以写成接口形式，估计是为了可拓展性
 */
public class ActivityViewFinder implements ViewFinder {
    @Override
    public View findView(Object object, int id) {
        return ((Activity) object).findViewById(id);
    }
}
