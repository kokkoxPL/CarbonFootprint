package com.kokkoxpl.carbonfootprint.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.View;

import com.kokkoxpl.carbonfootprint.R;
import com.kokkoxpl.carbonfootprint.adapters.HelpListAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class HelpFragment extends Fragment {
    private List<String> list;
    private ScheduledFuture<?> future;

    public HelpFragment() {
        super(R.layout.fragment_help);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager2 viewPager2 = view.findViewById(R.id.help_view_pager2);

        list = Arrays.asList(getResources().getStringArray(R.array.help_array_text));
        HelpListAdapter helpListAdapter = new HelpListAdapter(list, getContext());

        viewPager2.setAdapter(helpListAdapter);
        viewPager2.setPageTransformer(new LoopingViewPagerTransformer());
        viewPager2.setCurrentItem(0, false);

        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        Runnable runnable = () -> viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1, true);
        future = scheduleNewFuture(service, runnable);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);

                future.cancel(true);
                future = scheduleNewFuture(service, runnable);

                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    if (viewPager2.getCurrentItem() == list.size() + 1) {
                        viewPager2.setCurrentItem(1, false);
                    } else if (viewPager2.getCurrentItem() == 0) {
                        viewPager2.setCurrentItem(list.size(), false);
                    }
                }
            }
        });
    }

    private ScheduledFuture<?> scheduleNewFuture(ScheduledExecutorService service, Runnable runnable) {
        return service.scheduleAtFixedRate(runnable, 5, 5, TimeUnit.SECONDS);
    }

    private static class LoopingViewPagerTransformer implements ViewPager2.PageTransformer {
        @Override
        public void transformPage(@NonNull View page, float position) {
            if (position <= -1.0f || position >= 1.0f) {
                page.setAlpha(0.0f);
            } else if (position == 0.0f) {
                page.setAlpha(1.0f);
            } else {
                page.setAlpha(1.0f - Math.abs(position));
            }
        }
    }
}