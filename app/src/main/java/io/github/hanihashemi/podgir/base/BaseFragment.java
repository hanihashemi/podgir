package io.github.hanihashemi.podgir.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import butterknife.ButterKnife;
import io.github.hanihashemi.podgir.App;
import io.github.hanihashemi.podgir.helper.SuperToastHelper;
import io.github.hanihashemi.podgir.network.helper.VolleyErrorHandler;

/**
 * Created by hani on 8/17/15.
 */
public abstract class BaseFragment extends Fragment implements Response.ErrorListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResource(), container, false);
        ButterKnife.bind(this, view);
        customizeUI();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gatherArguments(getArguments());
    }

    protected void customizeUI() {

    }

    protected abstract int getLayoutResource();

    @Override
    public void onErrorResponse(VolleyError error) {
        VolleyErrorHandler.getInstance().showProperMessage(error, new VolleyErrorHandler.Listener() {
            @Override
            public void onCatching(VolleyErrorHandler.ErrorModel error) {
                if (error != null && BaseFragment.this.isVisible())
                    new SuperToastHelper().show(error.message);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        App.getInstance().cancelRequest(this);
    }

    protected abstract void gatherArguments(Bundle bundle);
}
