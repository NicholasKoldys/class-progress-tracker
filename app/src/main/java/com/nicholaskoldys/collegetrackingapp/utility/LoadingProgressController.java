package com.nicholaskoldys.collegetrackingapp.utility;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class LoadingProgressController {

    private ProgressBar progressBar;
    private TextView emptyViewMessageTextView;
    private RecyclerView.Adapter adapter;
    private Handler handler;

    public LoadingProgressController(@Nullable ProgressBar progressBar, @NonNull TextView emptyViewMessageTextView, @NonNull RecyclerView.Adapter adapter) {
        this.progressBar = progressBar;
        this.emptyViewMessageTextView = emptyViewMessageTextView;
        this.adapter = adapter;
        handler = new Handler(Looper.getMainLooper());
    }

    public void runLoader() {
        showLoadingViews();
        startLoadingLoop();
    }

    void showLoadingViews() {

        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    void startLoadingLoop() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                Boolean isLoadingPastDue = false;
                int iterationCount = 0;

                while(adapter.getItemCount() == 0) {
                    iterationCount++;
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (iterationCount == 5) {
                        isLoadingPastDue = true;
                        break;
                    }
                }

                if(!isLoadingPastDue) {
                    loadingFinished();
                } else {
                    failedLoading();
                }
            }
        }).start();
    }

    void loadingFinished() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                if(progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    void failedLoading() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                emptyViewMessageTextView.setVisibility(View.VISIBLE);
                if(progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}