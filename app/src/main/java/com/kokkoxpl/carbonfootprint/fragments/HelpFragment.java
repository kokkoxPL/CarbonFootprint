package com.kokkoxpl.carbonfootprint.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.View;

import com.kokkoxpl.carbonfootprint.R;
import com.kokkoxpl.carbonfootprint.adapters.HelpListAdapter;

import java.util.Arrays;
import java.util.List;

public class HelpFragment extends Fragment {
    private List<String> list;

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
        viewPager2.setCurrentItem(1, false);

//        RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
//        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//        int itemCount = viewPager2.getAdapter().getItemCount();
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                if (linearLayoutManager.findFirstVisibleItemPosition() == (itemCount - 1) && dx > 0) {
//                    recyclerView.scrollToPosition(1);
//                } else if (linearLayoutManager.findLastVisibleItemPosition() == 0 && dx < 0) {
//                    recyclerView.scrollToPosition(itemCount - 2);
//                }
//            }
//        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);

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

    public static class LoopingViewPagerTransformer implements ViewPager2.PageTransformer {
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