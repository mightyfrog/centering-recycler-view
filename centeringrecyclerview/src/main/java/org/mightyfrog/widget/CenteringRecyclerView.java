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
import android.graphics.Rect;
import android.support.annotation.IntRange;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import java.util.Arrays;

public class CenteringRecyclerView extends RecyclerView {
    public static final int ALIGN_TOP = 0;
    public static final int ALIGN_BOTTOM = 1;
    public static final int ALIGN_START = 2;
    public static final int ALIGN_END = 3;
    public static final int ALIGN_CENTER = 4;

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
     * Sets the currently selected position with the given alignment.
     *
     * @param position  The adapter position.
     * @param alignment (ALIGN_TOP | ALIGN_BOTTOM | ALIGN_START | ALIGN_END | ALIGN_CENTER)
     * @see #center(int)
     * @see #top(int)
     * @see #bottom(int)
     * @see #start(int)
     * @see #end(int)
     */
    public void setSelection(int position, @IntRange(from = 0, to = 4) int alignment) {
        switch (alignment) {
            case ALIGN_CENTER:
                center(position);
                break;
            case ALIGN_TOP:
                top(position);
                break;
            case ALIGN_BOTTOM:
                bottom(position);
                break;
            case ALIGN_START:
                start(position);
                break;
            case ALIGN_END:
                end(position);
                break;
            default:
                throw new IllegalArgumentException("unknown aligmnment");
        }
    }

    /**
     * Top-aligns a view at the given position.
     *
     * @param position The adapter position.
     * @see #start(int)
     */
    public void top(final int position) {
        LayoutManager lm = getLayoutManager();
        if (lm instanceof LinearLayoutManager) {
            LinearLayoutManager llm = (LinearLayoutManager) lm;
            llm.scrollToPositionWithOffset(position, 0);
        } else if (lm instanceof StaggeredGridLayoutManager) {
            final StaggeredGridLayoutManager sglm = (StaggeredGridLayoutManager) lm;
            sglm.scrollToPositionWithOffset(position, 0);
        } else {
            throw new UnsupportedOperationException("unsupported layout manager");
        }
    }

    /**
     * Bottom-aligns a view at the given position.
     *
     * @param position The adapter position.
     * @see #end(int)
     */
    public void bottom(final int position) {
        LayoutManager lm = getLayoutManager();
        if (lm instanceof LinearLayoutManager) {
            LinearLayoutManager llm = (LinearLayoutManager) lm;
            int offset = getBottomOffset(llm.getOrientation(), 0);
            llm.scrollToPositionWithOffset(position, offset);
        } else if (lm instanceof StaggeredGridLayoutManager) {
            final StaggeredGridLayoutManager sglm = (StaggeredGridLayoutManager) lm;
            int offset = getBottomOffset(sglm.getOrientation(), 0);
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
                            int offset = getBottomOffset(sglm.getOrientation(), childPosition);
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
     * Start-aligns a view at the given position.
     *
     * @param position The adapter position.
     * @see #top(int)
     */
    public void start(int position) {
        top(position);
    }

    /**
     * End-aligns a view at the given position.
     *
     * @param position The adapter position.
     * @see #bottom(int)
     */
    public void end(int position) {
        bottom(position);
    }

    /**
     * Center-aligns a view at the given position.
     *
     * @param position The adapter position.
     */
    public void center(final int position) {
        LayoutManager lm = getLayoutManager();
        if (lm instanceof LinearLayoutManager) {
            LinearLayoutManager llm = (LinearLayoutManager) lm;
            int offset = getCenterOffset(llm.getOrientation(), 0);
            llm.scrollToPositionWithOffset(position, offset);
        } else if (lm instanceof StaggeredGridLayoutManager) {
            final StaggeredGridLayoutManager sglm = (StaggeredGridLayoutManager) lm;
            int offset = getCenterOffset(sglm.getOrientation(), 0);
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
                            int offset = getCenterOffset(sglm.getOrientation(), childPosition);
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

    //
    //
    //

    /**
     * Calculates and returns the center offset size.
     *
     * @param orientation   The layout orientation.
     * @param childPosition The visible child position.
     */
    private int getCenterOffset(int orientation, int childPosition) {
        if (orientation == OrientationHelper.HORIZONTAL) {
            return getWidth() / 2 - getChildAt(childPosition).getWidth() / 2;
        } else {
            return getHeight() / 2 - getChildAt(childPosition).getHeight() / 2;
        }
    }

    /**
     * Calculates and returns the bottom offset size.
     *
     * @param orientation   The layout orientation.
     * @param childPosition The visible child position.
     */
    private int getBottomOffset(int orientation, int childPosition) {
        if (orientation == OrientationHelper.HORIZONTAL) {
            return getWidth() - getChildAt(childPosition).getWidth();
        } else {
            Rect rect = new Rect();
            getChildAt(childPosition).getLocalVisibleRect(rect);
            return getHeight() - getChildAt(childPosition).getHeight();
        }
    }

}
