package com.ppz.framwork.tools;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

/**
 * RecyclerView分割线
 */
public class MyDividerItemDecoration extends RecyclerView.ItemDecoration {

    private final int ONE_SPAN_COUNT = 1;//list VERTICAL 垂直列表
    private final int NO_SPAN_COUNT = 0;//list HORIZONTAL 水平列表

    private Paint mPaint;
    private int lineWidth = 2;
    private boolean drawOuterBorder = false;
    private boolean hasTitle = false;
    private String colorString = "#eeeeee";

    public MyDividerItemDecoration() {
        setColorString(colorString);
    }

    public MyDividerItemDecoration(int lineWidth, boolean drawOuterBorder, String colorString, boolean hasTitle) {
        this.lineWidth = lineWidth;
        this.drawOuterBorder = drawOuterBorder;
        //this.colorString = colorString;
        this.hasTitle = hasTitle;
        setColorString(colorString);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int childCount = Objects.requireNonNull(parent.getAdapter()).getItemCount();
        int position = parent.getChildAdapterPosition(view);
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        int left = 0, top = 0, right = 0, bottom = 0, spanCount = 0;
        if (manager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) manager).getSpanCount();
        } else if (manager instanceof LinearLayoutManager) {
            int orientation = ((LinearLayoutManager) manager).getOrientation();
            spanCount = orientation == LinearLayout.VERTICAL ? ONE_SPAN_COUNT : NO_SPAN_COUNT;
        } else {

        }
        left = needDrawLeft(position, spanCount, childCount) ? lineWidth : 0;
        top = needDrawTop(position, spanCount, childCount) ? lineWidth : 0;
        right = needDrawRight(position, spanCount, childCount) ? lineWidth : 0;
        bottom = needDrawBottom(position, spanCount, childCount) ? lineWidth : 0;
        outRect.set(left, top, right, bottom);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            drawGrid(c, parent);
        } else if (manager instanceof LinearLayoutManager) {
            drawLinear(c, parent);
        } else {

        }
    }

    private void drawLinear(Canvas c, RecyclerView parent) {
        LinearLayoutManager manager = (LinearLayoutManager) parent.getLayoutManager();
        int orientation = manager.getOrientation();
        int childCount = manager.getChildCount();
        if (orientation == LinearLayout.VERTICAL) {
            drawLinearVertical(childCount, c, parent);
        } else {
            drawLinearHorizontal(childCount, c, parent);
        }
    }

    private void drawLinearHorizontal(int childCount, Canvas c, RecyclerView parent) {
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            int index = i;
            if (hasTitle) {
                if (i == 0) {
                    if (drawOuterBorder) {
                        drawTop(c, child, layoutParams);
                        drawLeft(c, child, layoutParams);
                        drawRight(c, child, layoutParams);
                    }
                    drawBottom(c, child, layoutParams);
                    continue;
                } else {
                    index = i - 1;
                }
            }
            if (needDrawTop(index, NO_SPAN_COUNT, childCount))
                drawTop(c, child, layoutParams);
            if (needDrawBottom(index, NO_SPAN_COUNT, childCount))
                drawBottom(c, child, layoutParams);
            if (needDrawLeft(index, NO_SPAN_COUNT, childCount))
                drawLeft(c, child, layoutParams);
            if (needDrawRight(index, NO_SPAN_COUNT, childCount))
                drawRight(c, child, layoutParams);
        }
    }

    private void drawLinearVertical(int childCount, Canvas c, RecyclerView parent) {
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            int index = i;
            if (hasTitle) {
                if (i == 0) {
                    if (drawOuterBorder) {
                        drawTop(c, child, layoutParams);
                        drawLeft(c, child, layoutParams);
                        drawRight(c, child, layoutParams);
                    }
                    drawBottom(c, child, layoutParams);
                    continue;
                } else {
                    index = i - 1;
                }
            }
            if (needDrawTop(index, ONE_SPAN_COUNT, childCount))
                drawTop(c, child, layoutParams);
            if (needDrawBottom(index, ONE_SPAN_COUNT, childCount))
                drawBottom(c, child, layoutParams);
            if (needDrawLeft(index, ONE_SPAN_COUNT, childCount))
                drawLeft(c, child, layoutParams);
            if (needDrawRight(index, ONE_SPAN_COUNT, childCount))
                drawRight(c, child, layoutParams);
        }
    }

    private void drawGrid(Canvas c, RecyclerView parent) {
        GridLayoutManager manager = (GridLayoutManager) parent.getLayoutManager();
        int spanCount = manager.getSpanCount();
        int childCount = manager.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            int index = i;
            if (hasTitle) {
                if (i == 0) {
                    if (drawOuterBorder) {
                        drawTop(c, child, layoutParams);
                        drawLeft(c, child, layoutParams);
                        drawRight(c, child, layoutParams);
                    }
                    drawBottom(c, child, layoutParams);
                    continue;
                } else {
                    index = i - 1;
                }
            }
            if (needDrawTop(index, spanCount, childCount))
                drawTop(c, child, layoutParams);
            if (needDrawBottom(index, spanCount, childCount))
                drawBottom(c, child, layoutParams);
            if (needDrawLeft(index, spanCount, childCount))
                drawLeft(c, child, layoutParams);
            if (needDrawRight(index, spanCount, childCount))
                drawRight(c, child, layoutParams);
        }
    }

    private boolean needDrawTop(int position, int spanCount, int childCount) {
        if (spanCount == NO_SPAN_COUNT) return drawOuterBorder;
        int currentLine = (position + 1) / spanCount + ((position + 1) % spanCount > 0 ? 1 : 0);
        return drawOuterBorder && currentLine == 1;//需要绘制外边框且第一行
    }

    private void drawTop(Canvas c, View child, RecyclerView.LayoutParams layoutParams) {
        int top = child.getTop() - layoutParams.topMargin - lineWidth;
        int left = child.getLeft() - layoutParams.leftMargin - lineWidth;
        int right = child.getRight() + layoutParams.rightMargin + lineWidth;
        int bottom = top + lineWidth;
        c.drawRect(left, top, right, bottom, mPaint);
    }

    private boolean needDrawLeft(int position, int spanCount, int childCount) {
        if (spanCount == NO_SPAN_COUNT) return drawOuterBorder && position == 0;//水平list首个
        if (drawOuterBorder && spanCount == ONE_SPAN_COUNT) return true;//垂直list
        return drawOuterBorder && (position + 1) % spanCount == 1;//需要绘制外边框且第一列
    }

    private void drawLeft(Canvas c, View child, RecyclerView.LayoutParams layoutParams) {
        int top = child.getTop() - layoutParams.topMargin;
        int left = child.getLeft() - layoutParams.leftMargin - lineWidth;
        int right = left + lineWidth;
        int bottom = child.getBottom() + layoutParams.bottomMargin;
        c.drawRect(left, top, right, bottom, mPaint);
    }

    private boolean needDrawRight(int position, int spanCount, int childCount) {
        if (drawOuterBorder)
            return true;
        if (spanCount == NO_SPAN_COUNT) return true;
        if ((position + 1) % spanCount == 0)
            return false;
        return true;
    }

    private void drawRight(Canvas c, View child, RecyclerView.LayoutParams layoutParams) {
        int top = child.getTop() - layoutParams.topMargin;
        int right = child.getRight() + layoutParams.rightMargin + lineWidth;
        int left = right - lineWidth;
        int bottom = child.getBottom() + layoutParams.bottomMargin;
        c.drawRect(left, top, right, bottom, mPaint);
    }

    private boolean needDrawBottom(int position, int spanCount, int childCount) {
        if (spanCount == NO_SPAN_COUNT) return drawOuterBorder;
        int currentLine = (position + 1) / spanCount + ((position + 1) % spanCount > 0 ? 1 : 0);
        int maxLine = childCount / spanCount + (childCount % spanCount == 0 ? 0 : 1);
        if (currentLine == maxLine && drawOuterBorder)
            return true;
        if (childCount <= spanCount || currentLine == maxLine)//仅一行 或 最后一行
            return false;
        return true;
    }

    private void drawBottom(Canvas c, View child, RecyclerView.LayoutParams layoutParams) {
        int bottom = child.getBottom() + layoutParams.bottomMargin + lineWidth;
        int top = bottom - lineWidth;
        int left = child.getLeft() - layoutParams.leftMargin - lineWidth;
        int right = child.getRight() + layoutParams.rightMargin + lineWidth;
        c.drawRect(left, top, right, bottom, mPaint);
    }

    public MyDividerItemDecoration setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    public MyDividerItemDecoration setDrawOuterBorder(boolean drawOuterBorder) {
        this.drawOuterBorder = drawOuterBorder;
        return this;
    }

    public MyDividerItemDecoration setColorString(String colorString) {
        this.colorString = colorString;
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mPaint.setColor(Color.parseColor(colorString));
        return this;
    }

    public MyDividerItemDecoration setColorString5() {
        setColorString("#f2f2f2");
        return this;
    }

    public MyDividerItemDecoration setColorString15() {
        setColorString("#d8d8d8");
        return this;
    }

    public MyDividerItemDecoration setColorString25() {
        setColorString("#bfbfbf");
        return this;
    }

    public MyDividerItemDecoration setColorString35() {
        setColorString("#a5a5a5");
        return this;
    }

    public MyDividerItemDecoration setColorString50() {
        setColorString("#7f7f7f");
        return this;
    }

    public MyDividerItemDecoration setHasTitle(boolean hasTitle) {
        this.hasTitle = hasTitle;
        return this;
    }
}
