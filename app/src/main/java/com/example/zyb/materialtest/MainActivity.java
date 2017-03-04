package com.example.zyb.materialtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private FruitBean[] fruitBeans = {
            new FruitBean("苹果", R.drawable.apple),
            new FruitBean("香蕉", R.drawable.banana),
            new FruitBean("橘子", R.drawable.orange),
            new FruitBean("西瓜", R.drawable.watermelon),
            new FruitBean("雪梨", R.drawable.pear),
            new FruitBean("葡萄", R.drawable.grape),
            new FruitBean("菠萝", R.drawable.pineapple),
            new FruitBean("草莓", R.drawable.strawberry),
            new FruitBean("樱桃", R.drawable.cherry),
            new FruitBean("芒果", R.drawable.mango)};
    private List<FruitBean> list = new ArrayList<>();
    private FruitAdapter adapter;
    private int loadMore;
    //    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
        }
        navigationView.setCheckedItem(R.id.nav_call);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers(); //关闭drawerLayout
                return true;
            }
        });


        initData(true);
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//        adapter = new FruitAdapter(list);
//        recyclerView.setAdapter(adapter);

        final XRecyclerView xRecyclerView = (XRecyclerView) findViewById(R.id.recycler_view);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        xRecyclerView.setLayoutManager(layoutManager);

        //监听recyclerview滑动改变悬浮按钮显隐
        xRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (layoutManager.findLastVisibleItemPosition() >= 26) {
                    if (dy > 0) {
                        fab.setVisibility(View.INVISIBLE);
                    } else {
                        fab.setVisibility(View.VISIBLE);
                    }
                } else {
                    fab.setVisibility(View.INVISIBLE);
                }

            }

//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                XRecyclerView.LayoutManager manager = xRecyclerView.getLayoutManager();
//                if (manager instanceof GridLayoutManager) {
//                    int firstVisibleItemPosition = ((GridLayoutManager) manager).findLastVisibleItemPosition();
//                    if (firstVisibleItemPosition >= 18) {
//                        fab.setVisibility(View.VISIBLE);
//                    } else {
//                        fab.setVisibility(View.INVISIBLE);
//                    }
//                }
//            }
        });

//        TextView textView = new TextView(this);
//        textView.setText("这是头部");
//        textView.setTextColor(Color.GRAY);
//        textView.setTextSize(16);
//        textView.setGravity(Gravity.CENTER);
//        xRecyclerView.addHeaderView(textView);
        //设置刷新的风格
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallPulse);
        //设置下拉的图标
        xRecyclerView.setArrowImageView(R.mipmap.ic_refresh);
        //监听刷新
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadMore=0;
                                initData(true);
                                adapter.notifyDataSetChanged();
                                xRecyclerView.refreshComplete();
                            }
                        });
                    }
                }).start();

            }

            @Override
            public void onLoadMore() {
                loadMore++;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (loadMore == 3) {
                                    xRecyclerView.setNoMore(true);
                                    return;
                                }
                                initData(false);
                                adapter.notifyDataSetChanged();
                                xRecyclerView.loadMoreComplete();
                            }
                        });
                    }
                }).start();
            }
        });
        adapter = new FruitAdapter(list);
        xRecyclerView.setAdapter(adapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "滑到顶部...", Snackbar.LENGTH_SHORT)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //定位到第一行
                                xRecyclerView.scrollToPosition(0);
                            }
                        })
                        .setActionTextColor(0xff0285cf)
                        .show();
            }
        });

//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
//        swipeRefreshLayout.setColorSchemeResources(R.color.color4);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshFruits();
//            }
//        });
    }

//    private void refreshFruits() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        initData(true);
//                        adapter.notifyDataSetChanged();
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                });
//            }
//        }).start();
//    }

    private void initData(boolean isRefresh) {
        Random random = new Random();
        if (isRefresh) {
            list.clear();
            for (int i = 0; i < 28; i++) {
                int index = random.nextInt(fruitBeans.length);
                list.add(fruitBeans[index]);
            }
        } else {
            if (loadMore >= 3) {
                return;
            }
            for (int i = 0; i < 28; i++) {
                int index = random.nextInt(fruitBeans.length);
                list.add(fruitBeans[index]);
            }
        }
    }

    //toolbar菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    //toolbar点击
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backup:
                Toast.makeText(this, "点击备份了", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.delete:
                Toast.makeText(this, "点击删除了", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "点击设置了", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;

        }
        return true;
    }
}
