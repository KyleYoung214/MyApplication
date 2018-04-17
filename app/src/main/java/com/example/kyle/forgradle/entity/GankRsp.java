package com.example.kyle.forgradle.entity;

import java.util.Arrays;
import java.util.List;

/**
 * Created by nick.yang on 2017/8/9.
 */

public class GankRsp {

    /**
     * error : false
     * results : [{"_id":"59678dae421aa90c9203d38a","createdAt":"2017-07-13T23:11:42.906Z",
     * "desc":"安卓指纹识别SDK，完美集成三星和魅族的指纹SDK，附带相关资料背景！","images":["http://img.gank
     * .io/4bc6f013-ac5d-41e2-a337-d3cfc26c1547"],"publishedAt":"2017-07-14T13:24:31.177Z",
     * "source":"web","type":"Android","url":"https://github
     * .com/uccmawei/FingerprintIdentify?tt=1","used":true,"who":"Awei"},
     * {"_id":"59681bec421aa90ca209c460","createdAt":"2017-07-14T09:18:36.33Z",
     * "desc":"一个竖直方向的SlidingPanelLayout，支持加载多个Panel，可以灵活地实现漂亮的的交互效果",
     * "publishedAt":"2017-07-14T13:24:31.177Z","source":"web","type":"Android",
     * "url":"https://github.com/woxingxiao/SlidingUpPanelLayout","used":true,"who":"Xiao"},
     * {"_id":"5968389c421aa90c9203d391","createdAt":"2017-07-14T11:21:00.776Z",
     * "desc":"RecyclerView 半圆形布局","images":["http://img.gank
     * .io/62beda51-04fe-4865-b780-f982419d8feb"],"publishedAt":"2017-07-14T13:24:31.177Z",
     * "source":"chrome","type":"Android","url":"https://github.com/cdflynn/turn-layout-manager",
     * "used":true,"who":"代码家"}]
     */

    private boolean error;
    private List<AndroidBook> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<AndroidBook> getResults() {
        return results;
    }

    public AndroidBook[] getResultsArray() {
        int size = results.size();
        if (size == 0) {
            return null;
        }

        return results.toArray(new AndroidBook[]{});
    }

    public void setResults(List<AndroidBook> results) {
        this.results = results;
    }
}
