package com.kokkoxpl.carbonfootprint.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.kokkoxpl.carbonfootprint.R;
import com.kokkoxpl.carbonfootprint.adapters.HelpListAdapter;
import com.kokkoxpl.carbonfootprint.data.db.AppDatabase;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class HelpFragment extends Fragment {
    private AppDatabase appDatabase;
    private List<String> textList;
    private List<String> imageList;
    private ScheduledFuture<?> future;

    public HelpFragment() {
        super(R.layout.fragment_help);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager2 viewPager2 = view.findViewById(R.id.help_view_pager2);

        appDatabase = AppDatabase.newInstance(getContext());

        textList = new ArrayList<>();
        imageList = new ArrayList<>();
        textList.addAll(Arrays.asList(getResources().getStringArray(R.array.help_array_text)));

        for (int i = 1; i <= textList.size(); i++) {
            imageList.add(String.format("image_%s", i));
        }

        String mostUsedApp = appDatabase.dataRecordDao().getRecordsMostUsedApp(LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toString(), LocalDate.now().toString());
        if (mostUsedApp != null) {
            textList.add(0, String.format("%s %s", getString(R.string.help_app_tip), mostUsedApp));
            imageList.add(0, String.format("logo_%s", mostUsedApp.toLowerCase()));
        }

        HelpListAdapter helpListAdapter = new HelpListAdapter(textList, imageList);

        viewPager2.setAdapter(helpListAdapter);
        viewPager2.setPageTransformer(new LoopingViewPagerTransformer());
        viewPager2.setCurrentItem(1, false);

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
                    if (viewPager2.getCurrentItem() == textList.size() + 1) {
                        viewPager2.setCurrentItem(1, false);
                    } else if (viewPager2.getCurrentItem() == 0) {
                        viewPager2.setCurrentItem(textList.size(), false);
                    }
                }
            }
        });
    }

    private ScheduledFuture<?> scheduleNewFuture(ScheduledExecutorService service, Runnable runnable) {
        return service.scheduleWithFixedDelay(runnable, 10, 8, TimeUnit.SECONDS);
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