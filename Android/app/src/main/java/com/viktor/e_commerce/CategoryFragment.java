package com.viktor.e_commerce;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.viktor.e_commerce.Adapter.CategoryAdapter;
import com.viktor.e_commerce.Util.HttpUtil;
import com.viktor.e_commerce.Util.Util;
import com.viktor.e_commerce.Util.Utility;
import com.viktor.e_commerce.gson.SubCategory;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CategoryFragment extends Fragment {

    private static final String TAG = "CategoryFragment";

    private static final String ROOT = "http://www.mallproject.cn:8000/api/";

    private TabLayout tabLayout;

    private SearchView searchView;

    private List<View> viewList;

    private ViewPager viewPager;

    private RecyclerView recyclerView;

    private CategoryAdapter adapter;

    private List<SubCategory> categoryList = new ArrayList<>();

    private View pager1;

    private View pager2;

    private View pager3;

    private View pager4;

    private View pager5;

    private View pager6;

    private int current;

    private String[] pagerTitle = { "科技产品", "学习用品", "家居日用", "食品", "服装护理", "创意礼品" };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_fragment, container, false);
        //取控件
        tabLayout = (TabLayout)view.findViewById(R.id.tab_layout);
        searchView = (SearchView) view.findViewById(R.id.search_view);
        viewPager = (ViewPager)view.findViewById(R.id.view_pager);
        //设置数据
        current = 1;
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                search(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                search(s);
                return true;
            }
        });

        initTab();
        initViewPager();
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initViewPager(){
        viewList = new ArrayList<>();

        pager1 = LayoutInflater.from(getContext()).inflate(R.layout.category_1, null);
        pager2 = LayoutInflater.from(getContext()).inflate(R.layout.category_2, null);
        pager3 = LayoutInflater.from(getContext()).inflate(R.layout.category_3, null);
        pager4 = LayoutInflater.from(getContext()).inflate(R.layout.category_4, null);
        pager5 = LayoutInflater.from(getContext()).inflate(R.layout.category_5, null);
        pager6 = LayoutInflater.from(getContext()).inflate(R.layout.category_6, null);

        adapter = new CategoryAdapter(categoryList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView = (RecyclerView)pager1.findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        requestCategory(current, null);

        viewList.add(pager1);
        viewList.add(pager2);
        viewList.add(pager3);
        viewList.add(pager4);
        viewList.add(pager5);
        viewList.add(pager6);


        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                View view = viewList.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView(viewList.get(position));
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return pagerTitle[position];
            }
        });
    }

    private void requestCategory(int id, String keyword){
        String url = ROOT + "itemSubCategory/?format=json&item_subCategory_belong=" + current;
        if(keyword != null){
            url = url + "&item_subCategory__icontains=" + keyword;
        }
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "获取品类信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                categoryList = Utility.parseJSONForSubCategory(responseText);
                while(getActivity() == null);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new CategoryAdapter(categoryList);
                        switch (current){
                            case 1:
                                recyclerView = pager1.findViewById(R.id.recycle_view);
                                break;
                            case 2:
                                recyclerView = pager2.findViewById(R.id.recycle_view);
                                break;
                            case 3:
                                recyclerView = pager3.findViewById(R.id.recycle_view);
                                break;
                            case 4:
                                recyclerView = pager4.findViewById(R.id.recycle_view);
                                break;
                            case 5:
                                recyclerView = pager5.findViewById(R.id.recycle_view);
                                break;
                            case 6:
                                recyclerView = pager6.findViewById(R.id.recycle_view);
                                break;
                        }
                        recyclerView.setAdapter(adapter);
                        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                        recyclerView.setLayoutManager(layoutManager);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void initTab(){
        tabLayout.addTab(tabLayout.newTab().setText("大类1"));
        tabLayout.addTab(tabLayout.newTab().setText("大类2"));
        tabLayout.addTab(tabLayout.newTab().setText("大类3"));
        tabLayout.addTab(tabLayout.newTab().setText("大类4"));
        tabLayout.addTab(tabLayout.newTab().setText("大类5"));
        tabLayout.addTab(tabLayout.newTab().setText("大类6"));
        final TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.tab_selected_text, null);
        TabLayout.Tab tab = tabLayout.getTabAt(tabLayout.getSelectedTabPosition());
        textView.setText(tab.getText());
        tab.setCustomView(textView);
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                /*TextView textView = new TextView(getContext());
                float selectedSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics());
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, selectedSize);
                textView.setTextColor(getResources().getColor(R.color.colorPress, null));
                textView.setText(tab.getText());
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));*/
                textView.setText(tab.getText());
                tab.setCustomView(textView);
                current = tab.getPosition() + 1;
                requestCategory(current, null);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setCustomView(null);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //add refresh logic
            }
        });
    }

    private void search(String keyword){
        requestCategory(current, keyword);
    }

}
