package io.github.hanihashemi.podgir.base;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import butterknife.Bind;
import io.github.hanihashemi.podgir.R;
import io.github.hanihashemi.podgir.widget.AppTextView;

/**
 * Created by hani on 11/6/15.
 */
public abstract class BaseSwipeFragment<T> extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, Response.Listener<T> {
    @Bind(R.id.recycler_view)
    protected RecyclerView recyclerView;
    @Bind(R.id.error_message)
    protected AppTextView errorMessage;
    @Bind(R.id.swipe_refresh)
    protected SwipeRefreshLayout swipeRefresh;

    @Override
    protected void customizeUI() {
        super.customizeUI();
        swipeRefresh.setColorSchemeResources(R.color.accent);
        swipeRefresh.setOnRefreshListener(this);
        setSwipeRefresh(true);
        fetchData();
    }

    @Override
    public void onRefresh() {
        setSwipeRefresh(true);
        fetchData();
    }

    protected void setSwipeRefresh(final boolean value) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(value);
            }
        }, 100);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        super.onErrorResponse(error);
        errorMessage.setVisibility(View.VISIBLE);
        setSwipeRefresh(false);
    }

    @Override
    public void onResponse(T response) {
        setSwipeRefresh(false);
        errorMessage.setVisibility(View.GONE);
    }

    protected abstract void fetchData();
}
