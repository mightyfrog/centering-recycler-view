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
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import java.lang.reflect.Method;
import java.util.Arrays;

public class CenteringRecyclerView extends RecyclerView {
    public static final int ALIGN_TOP = 0;
    public static final int ALIGN_BOTTOM = 1;
    public static final int ALIGN_START = 2;
    public static final int ALIGN_END = 3;
    public static final int ALIGN_CENTER = 4;

    private boolean mIgnoreIfVisible;
    private boolean mIgnoreIfCompletelyVisible;

    public CenteringRecyclerView(Context context) {
        this(context, null);
    }

    public CenteringRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CenteringRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CenteringRecyclerView,
                0, 0);

        try {
            mIgnoreIfVisible = a.getBoolean(R.styleable.CenteringRecyclerView_ignoreIfVisible, false);
            mIgnoreIfCompletelyVisible = a.getBoolean(R.styleable.CenteringRecyclerView_ignoreIfCompletelyVisible, false);
        } finally {
            a.recycle();
        }
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
    public void setSelection(int position, int alignment) {
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
                throw new IllegalArgumentException("unknown alignment");
        }
    }

    /**
     * Top-aligns a view at the given position.
     *
     * @param position The adapter position.
     * @see #start(int)
     */
    public void top(final int position) {
        if (mIgnoreIfCompletelyVisible && isCompletelyVisible(position)) {
            return;
        }

        if (mIgnoreIfVisible && isVisible(position)) {
            return;
        }

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
        if (mIgnoreIfCompletelyVisible && isCompletelyVisible(position)) {
            return;
        }

        if (mIgnoreIfVisible && isVisible(position)) {
            return;
        }

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
                    int first = getFirstVisiblePosition();
                    int last = getLastVisiblePosition();
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
        if (mIgnoreIfCompletelyVisible && isCompletelyVisible(position)) {
            return;
        }

        if (mIgnoreIfVisible && isVisible(position)) {
            return;
        }

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
                    int first = getFirstVisiblePosition();
                    int last = getLastVisiblePosition();
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

    /**
     * If you want to ignore aligning requests a view at the requested position is completely
     * visible, set this to true.
     *
     * @param ignoreIfCompletelyVisible true | false
     */
    public void setIgnoreIfCompletelyVisible(boolean ignoreIfCompletelyVisible) {
        mIgnoreIfCompletelyVisible = ignoreIfCompletelyVisible;
    }


    /**
     * If you want to ignore aligning requests a view at the requested position is visible, set
     * this to true.
     *
     * @param ignoreIfVisible true | false
     */
    public void setIgnoreIfVisible(boolean ignoreIfVisible) {
        mIgnoreIfVisible = ignoreIfVisible;
    }

    /**
     * Returns if a view at the given position is visible or not.
     *
     * @param position The adapter position.
     * @see #isCompletelyVisible(int)
     */
    public boolean isVisible(int position) {
        int first = getFirstVisiblePosition();
        int last = getLastVisiblePosition();
        return first <= position && last >= position;
    }

    /**
     * Returns if a view at the given position is completely visible or not.
     *
     * @param position The adapter position.
     * @see #isVisible(int)
     */
    public boolean isCompletelyVisible(int position) {
        int first = getFirstCompletelyVisiblePosition();
        int last = getLastCompletelyVisiblePosition();
        return first <= position && last >= position;
    }

    /**
     * Returns the first visible grid position.
     *
     * @return the first visible position or RecyclerView.NO_POSITION if any error occurs.
     * @see #getFirstCompletelyVisiblePosition()
     */
    public int getFirstVisiblePosition() {
        LayoutManager lm = getLayoutManager();
        if (lm instanceof LinearLayoutManager) {
            LinearLayoutManager llm = (LinearLayoutManager) lm;

            return llm.findFirstVisibleItemPosition();
        } else {
            StaggeredGridLayoutManager sglm = (StaggeredGridLayoutManager) lm;
            try {
                // https://code.google.com/p/android/issues/detail?id=181461
                // https://code.google.com/p/android/issues/detail?id=180521
                Method m = sglm.getClass().getDeclaredMethod("ensureOrientationHelper");
                m.setAccessible(true);
                m.invoke(sglm);

                int[] firstVisibleItemPositions = sglm.findFirstVisibleItemPositions(null);
                Arrays.sort(firstVisibleItemPositions);

                return firstVisibleItemPositions[0];
            } catch (Exception e) {
                android.util.Log.e(getClass().getSimpleName(), "" + e);
                return RecyclerView.NO_POSITION;
            }
        }
    }

    /**
     * Returns the last visible grid position.
     *
     * @return the last visible position or RecyclerView.NO_POSITION if any error occurs.
     * @see #getLastCompletelyVisiblePosition()
     */
    public int getLastVisiblePosition() {
        LayoutManager lm = getLayoutManager();
        if (lm instanceof LinearLayoutManager) {
            LinearLayoutManager llm = (LinearLayoutManager) lm;

            return llm.findLastVisibleItemPosition();
        } else {
            StaggeredGridLayoutManager sglm = (StaggeredGridLayoutManager) lm;
            try {
                // https://code.google.com/p/android/issues/detail?id=181461
                // https://code.google.com/p/android/issues/detail?id=180521
                Method m = sglm.getClass().getDeclaredMethod("ensureOrientationHelper");
                m.setAccessible(true);
                m.invoke(sglm);

                int[] lastVisibleItemPositions = sglm.findLastVisibleItemPositions(null);
                Arrays.sort(lastVisibleItemPositions);

                return lastVisibleItemPositions[lastVisibleItemPositions.length - 1];
            } catch (Exception e) {
                android.util.Log.e(getClass().getSimpleName(), "" + e);
                return RecyclerView.NO_POSITION;
            }
        }
    }

    /**
     * Returns the first completely visible grid position.
     *
     * @return the first completely visible position or RecyclerView.NO_POSITION if any error occurs.
     * @see #getFirstVisiblePosition()
     */
    public int getFirstCompletelyVisiblePosition() {
        LayoutManager lm = getLayoutManager();
        if (lm instanceof LinearLayoutManager) {
            LinearLayoutManager llm = (LinearLayoutManager) lm;
            return llm.findFirstCompletelyVisibleItemPosition();
        } else {
            StaggeredGridLayoutManager sglm = (StaggeredGridLayoutManager) lm;
            try {
                // https://code.google.com/p/android/issues/detail?id=181461
                // https://code.google.com/p/android/issues/detail?id=180521
                Method m = sglm.getClass().getDeclaredMethod("ensureOrientationHelper");
                m.setAccessible(true);
                m.invoke(sglm);

                int[] firstVisibleItemPositions = sglm.findFirstCompletelyVisibleItemPositions(null);
                Arrays.sort(firstVisibleItemPositions);

                return firstVisibleItemPositions[0];
            } catch (Exception e) {
                android.util.Log.e(getClass().getSimpleName(), "" + e);
                return RecyclerView.NO_POSITION;
            }
        }
    }

    /**
     * Returns the last completely visible grid position.
     *
     * @return the last completely visible position or RecyclerView.NO_POSITION if any error occurs.
     * @see #getLastVisiblePosition()
     */
    public int getLastCompletelyVisiblePosition() {
        LayoutManager lm = getLayoutManager();
        if (lm instanceof LinearLayoutManager) {
            LinearLayoutManager llm = (LinearLayoutManager) lm;
            return llm.findLastCompletelyVisibleItemPosition();
        } else {
            StaggeredGridLayoutManager sglm = (StaggeredGridLayoutManager) lm;
            try {
                // https://code.google.com/p/android/issues/detail?id=181461
                // https://code.google.com/p/android/issues/detail?id=180521
                Method m = sglm.getClass().getDeclaredMethod("ensureOrientationHelper");
                m.setAccessible(true);
                m.invoke(sglm);

                int[] lastVisibleItemPositions = sglm.findLastCompletelyVisibleItemPositions(null);
                Arrays.sort(lastVisibleItemPositions);

                return lastVisibleItemPositions[lastVisibleItemPositions.length - 1];
            } catch (Exception e) {
                android.util.Log.e(getClass().getSimpleName(), "" + e);
                return RecyclerView.NO_POSITION;
            }
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
            return getHeight() - getChildAt(childPosition).getHeight();
        }
    }
}
