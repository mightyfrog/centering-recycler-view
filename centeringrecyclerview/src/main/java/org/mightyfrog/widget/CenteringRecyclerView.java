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

package org.mightyfrog.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import java.util.Arrays;

public class CenteringRecyclerView extends RecyclerView {

    public CenteringRecyclerView(Context context) {
        super(context);
    }

    public CenteringRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CenteringRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Centers the given position.
     *
     * @param position The adapter position.
     */
    public void center(final int position) {
        LayoutManager lm = getLayoutManager();
        if (lm instanceof LinearLayoutManager) {
            LinearLayoutManager llm = (LinearLayoutManager) lm;
            int offset = getOffset(llm.getOrientation(), 0);
            llm.scrollToPositionWithOffset(position, offset);
        } else if (lm instanceof StaggeredGridLayoutManager) {
            final StaggeredGridLayoutManager sglm = (StaggeredGridLayoutManager) lm;
            int offset = getOffset(sglm.getOrientation(), 0);
            sglm.scrollToPositionWithOffset(position, offset);

            post(new Runnable() {
                @Override
                public void run() {
                    int[] firstVisibleItemPositions = sglm.findFirstVisibleItemPositions(null);
                    int[] lastVisibleItemPositions = sglm.findLastVisibleItemPositions(null);
                    Arrays.sort(firstVisibleItemPositions);
                    Arrays.sort(lastVisibleItemPositions);
                    int first = firstVisibleItemPositions[0];
                    int last = lastVisibleItemPositions[lastVisibleItemPositions.length - 1];
                    int childPosition = 0;
                    for (int i = first; i < last; i++) {
                        if (i == position) {
                            int offset = getOffset(sglm.getOrientation(), childPosition);
                            sglm.scrollToPositionWithOffset(position, offset);
                            break;
                        }
                        childPosition++;
                    }
                }
            });
        } else {
            throw new UnsupportedOperationException("unsupported layout manager");
        }
    }

    /**
     * Calculates and returns the offset size.
     *
     * @param orientation   The layout orientation.
     * @param childPosition The visible child position.
     */
    private int getOffset(int orientation, int childPosition) {
        if (orientation == OrientationHelper.HORIZONTAL) {
            return getWidth() / 2 - getChildAt(childPosition).getWidth() / 2;
        } else {
            return getHeight() / 2 - getChildAt(childPosition).getHeight() / 2;
        }
    }

}
