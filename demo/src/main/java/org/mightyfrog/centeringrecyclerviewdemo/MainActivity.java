/*
 * Copyright (C) 2015 Shigehiro Soejima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mightyfrog.centeringrecyclerviewdemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.mightyfrog.widget.CenteringRecyclerView;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * @author Shigehiro Soejima
 */
public class MainActivity extends AppCompatActivity {

    private final Random mRand = new Random();

    private CenteringRecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(new DemoAdapter(R.layout.linear_vertical));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = mRand.nextInt(DemoAdapter.ITEM_COUNT);
                mRecyclerView.center(position);
                ActionBar ab = getSupportActionBar();
                if (ab != null) {
                    ab.setSubtitle(getString(R.string.subtitle, position));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.linear_vertical:
                mRecyclerView.setAdapter(new DemoAdapter(R.layout.linear_vertical));
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                break;
            case R.id.linear_horizontal:
                mRecyclerView.setAdapter(new DemoAdapter(R.layout.linear_horizontal));
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                break;
            case R.id.grid_vertical:
                mRecyclerView.setAdapter(new GridDemoAdapter(R.layout.grid_vertical));
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                break;
            case R.id.grid_horizontal:
                mRecyclerView.setAdapter(new GridDemoAdapter(R.layout.grid_horizontal));
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.HORIZONTAL, false));
                break;
            case R.id.staggered_vertical:
                mRecyclerView.setAdapter(new StaggeredDemoAdapter(R.layout.staggered_vertical));
                mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
                break;
            case R.id.staggered_horizontal:
                mRecyclerView.setAdapter(new StaggeredDemoAdapter(R.layout.staggered_horizontal));
                mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //
    //
    //

    /**
     * LinearLayoutManager demo adapter.
     */
    private class DemoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        static final int ITEM_COUNT = 1000;

        final int mLayout;

        DemoAdapter(int layout) {
            mLayout = layout;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext())
                    .inflate(mLayout, parent, false);

            return new DemoViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            DemoViewHolder viewHolder = (DemoViewHolder) holder;
            viewHolder.textView.setText(Integer.toString(position));
            viewHolder.textView.setBackgroundColor(0xff000000 | mRand.nextInt(0xffffff));
        }

        @Override
        public int getItemCount() {
            return ITEM_COUNT;
        }

        class DemoViewHolder extends RecyclerView.ViewHolder {
            private final TextView textView;

            DemoViewHolder(View itemView) {
                super(itemView);

                textView = itemView.findViewById(R.id.text);
            }
        }
    }

    /**
     * GridLayoutManager demo adapter.
     */
    private class GridDemoAdapter extends DemoAdapter {
        GridDemoAdapter(int layout) {
            super(layout);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext())
                    .inflate(mLayout, parent, false);

            return new DemoViewHolder(view);
        }
    }

    /**
     * StaggeredGridLayoutManager demo adapter.
     */
    private class StaggeredDemoAdapter extends DemoAdapter {
        StaggeredDemoAdapter(int layout) {
            super(layout);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            DemoViewHolder viewHolder = (DemoViewHolder) holder;
            viewHolder.textView.setText(Integer.toString(position));
            viewHolder.textView.setBackgroundColor(0xff000000 | mRand.nextInt(0xffffff));
            if (mRecyclerView.getLayoutManager() != null) {
                ViewGroup.LayoutParams params = viewHolder.textView.getLayoutParams();
                if (mRecyclerView.getLayoutManager().canScrollVertically()) {
                    params.height = 150 + mRand.nextInt(500);
                } else {
                    params.width = 150 + mRand.nextInt(500);
                }
            }
        }
    }
}
