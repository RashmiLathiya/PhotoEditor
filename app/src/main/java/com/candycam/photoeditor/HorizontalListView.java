package com.candycam.photoeditor;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Scroller;

import androidx.core.view.ViewCompat;
import androidx.core.widget.EdgeEffectCompat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class HorizontalListView extends AdapterView<ListAdapter> {
    private static final String BUNDLE_ID_CURRENT_X = "BUNDLE_ID_CURRENT_X";
    private static final String BUNDLE_ID_PARENT_STATE = "BUNDLE_ID_PARENT_STATE";
    private static final float FLING_DEFAULT_ABSORB_VELOCITY = 30.0f;
    private static final float FLING_FRICTION = 0.009f;
    private static final int INSERT_AT_END_OF_LIST = -1;
    private static final int INSERT_AT_START_OF_LIST = 0;
    protected Scroller a = new Scroller(getContext());
    protected ListAdapter b;
    protected int c;
    protected int d;
    private DataSetObserver mAdapterDataObserver = new DataSetObserver() {
        public void onChanged() {
            HorizontalListView.this.mDataChanged = true;
            HorizontalListView.this.mHasNotifiedRunningLowOnData = false;
            HorizontalListView.this.unpressTouchedChild();
            HorizontalListView.this.invalidate();
            HorizontalListView.this.requestLayout();
        }

        public void onInvalidated() {
            HorizontalListView.this.mHasNotifiedRunningLowOnData = false;
            HorizontalListView.this.unpressTouchedChild();
            HorizontalListView.this.reset();
            HorizontalListView.this.invalidate();
            HorizontalListView.this.requestLayout();
        }
    };
    /* access modifiers changed from: private */
    public boolean mBlockTouchAction = false;
    private OnScrollStateChangedListener.ScrollState mCurrentScrollState = OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE;
    private int mCurrentlySelectedAdapterIndex;
    /* access modifiers changed from: private */
    public boolean mDataChanged = false;
    private Runnable mDelayedLayout = new Runnable() {
        public void run() {
            HorizontalListView.this.requestLayout();
        }
    };
    private int mDisplayOffset;
    private Drawable mDivider = null;
    private int mDividerWidth = 0;
    private EdgeEffectCompat mEdgeGlowLeft;
    private EdgeEffectCompat mEdgeGlowRight;
    /* access modifiers changed from: private */
    public GestureDetector mGestureDetector;
    private final GestureListener mGestureListener = new GestureListener();
    /* access modifiers changed from: private */
    public boolean mHasNotifiedRunningLowOnData = false;
    private int mHeightMeasureSpec;
    private boolean mIsParentVerticiallyScrollableViewDisallowingInterceptTouchEvent = false;
    /* access modifiers changed from: private */
    public int mLeftViewAdapterIndex;
    private int mMaxX = Integer.MAX_VALUE;
    /* access modifiers changed from: private */
    public OnClickListener mOnClickListener;
    private OnScrollStateChangedListener mOnScrollStateChangedListener = null;
    private Rect mRect = new Rect();
    private List<Queue<View>> mRemovedViewsCache = new ArrayList();
    private Integer mRestoreX = null;
    private int mRightViewAdapterIndex;
    private RunningOutOfDataListener mRunningOutOfDataListener = null;
    private int mRunningOutOfDataThreshold = 0;
    private View mViewBeingTouched = null;

    private class GestureListener extends SimpleOnGestureListener {
        private GestureListener() {
        }

        public boolean onDown(MotionEvent motionEvent) {
            return HorizontalListView.this.a(motionEvent);
        }

        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return HorizontalListView.this.a(motionEvent, motionEvent2, f, f2);
        }

        public void onLongPress(MotionEvent motionEvent) {
            HorizontalListView.this.unpressTouchedChild();
            int a2 = HorizontalListView.this.getChildIndex((int) motionEvent.getX(), (int) motionEvent.getY());
            if (a2 >= 0 && !HorizontalListView.this.mBlockTouchAction) {
                View childAt = HorizontalListView.this.getChildAt(a2);
                OnItemLongClickListener onItemLongClickListener = HorizontalListView.this.getOnItemLongClickListener();
                if (onItemLongClickListener != null) {
                    int e = HorizontalListView.this.mLeftViewAdapterIndex + a2;
                    if (onItemLongClickListener.onItemLongClick(HorizontalListView.this, childAt, e, HorizontalListView.this.b.getItemId(e))) {
                        HorizontalListView.this.performHapticFeedback(0);
                    }
                }
            }
        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            HorizontalListView.this.requestParentListViewToNotInterceptTouchEvents(Boolean.valueOf(true));
            HorizontalListView.this.setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_TOUCH_SCROLL);
            HorizontalListView.this.unpressTouchedChild();
            HorizontalListView.this.d += (int) f;
            HorizontalListView.this.updateOverscrollAnimation(Math.round(f));
            HorizontalListView.this.requestLayout();
            return true;
        }

        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            HorizontalListView.this.unpressTouchedChild();
            OnItemClickListener onItemClickListener = HorizontalListView.this.getOnItemClickListener();
            int a2 = HorizontalListView.this.getChildIndex((int) motionEvent.getX(), (int) motionEvent.getY());
            if (a2 >= 0 && !HorizontalListView.this.mBlockTouchAction) {
                View childAt = HorizontalListView.this.getChildAt(a2);
                int e = HorizontalListView.this.mLeftViewAdapterIndex + a2;
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(HorizontalListView.this, childAt, e, HorizontalListView.this.b.getItemId(e));
                    return true;
                }
            }
            if (HorizontalListView.this.mOnClickListener != null && !HorizontalListView.this.mBlockTouchAction) {
                HorizontalListView.this.mOnClickListener.onClick(HorizontalListView.this);
            }
            return false;
        }
    }

    @TargetApi(11)
    private static final class HoneycombPlus {
        static {
            if (VERSION.SDK_INT < 11) {
                throw new RuntimeException("Should not get to HoneycombPlus class unless sdk is >= 11!");
            }
        }

        private HoneycombPlus() {
        }

        public static void setFriction(Scroller scroller, float f) {
            if (scroller != null) {
                scroller.setFriction(f);
            }
        }
    }

    @TargetApi(14)
    private static final class IceCreamSandwichPlus {
        static {
            if (VERSION.SDK_INT < 14) {
                throw new RuntimeException("Should not get to IceCreamSandwichPlus class unless sdk is >= 14!");
            }
        }

        private IceCreamSandwichPlus() {
        }

        public static float getCurrVelocity(Scroller scroller) {
            return scroller.getCurrVelocity();
        }
    }

    public interface OnScrollStateChangedListener {

        public enum ScrollState {
            SCROLL_STATE_IDLE,
            SCROLL_STATE_TOUCH_SCROLL,
            SCROLL_STATE_FLING
        }

        void onScrollStateChanged(ScrollState scrollState);
    }

    public interface RunningOutOfDataListener {
        void onRunningOutOfData();
    }

    public HorizontalListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mEdgeGlowLeft = new EdgeEffectCompat(context);
        this.mEdgeGlowRight = new EdgeEffectCompat(context);
        this.mGestureDetector = new GestureDetector(context, this.mGestureListener);
        bindGestureDetector();
        initView();
        retrieveXmlConfiguration(context, attributeSet);
        setWillNotDraw(false);
        if (VERSION.SDK_INT >= 11) {
            HoneycombPlus.setFriction(this.a, FLING_FRICTION);
        }
    }

    private void addAndMeasureChild(View view, int i) {
        addViewInLayout(view, i, getLayoutParams(view), true);
        measureChild(view);
    }

    private void bindGestureDetector() {
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return HorizontalListView.this.mGestureDetector.onTouchEvent(motionEvent);
            }
        });
    }

    private float determineFlingAbsorbVelocity() {
        return VERSION.SDK_INT >= 14 ? IceCreamSandwichPlus.getCurrVelocity(this.a) : FLING_DEFAULT_ABSORB_VELOCITY;
    }

    private void determineIfLowOnData() {
        if (this.mRunningOutOfDataListener != null && this.b != null && this.b.getCount() - (this.mRightViewAdapterIndex + 1) < this.mRunningOutOfDataThreshold && !this.mHasNotifiedRunningLowOnData) {
            this.mHasNotifiedRunningLowOnData = true;
            this.mRunningOutOfDataListener.onRunningOutOfData();
        }
    }

    private boolean determineMaxX() {
        if (!isLastItemInAdapter(this.mRightViewAdapterIndex)) {
            return false;
        }
        View rightmostChild = getRightmostChild();
        if (rightmostChild == null) {
            return false;
        }
        int i = this.mMaxX;
        this.mMaxX = ((rightmostChild.getRight() - getPaddingLeft()) + this.c) - getRenderWidth();
        if (this.mMaxX < 0) {
            this.mMaxX = 0;
        }
        return this.mMaxX != i;
    }

    private void drawDivider(Canvas canvas, Rect rect) {
        if (this.mDivider != null) {
            this.mDivider.setBounds(rect);
            this.mDivider.draw(canvas);
        }
    }

    private void drawDividers(Canvas canvas) {
        int childCount = getChildCount();
        Rect rect = this.mRect;
        this.mRect.top = getPaddingTop();
        this.mRect.bottom = this.mRect.top + getRenderHeight();
        for (int i = 0; i < childCount; i++) {
            if (i != childCount - 1 || !isLastItemInAdapter(this.mRightViewAdapterIndex)) {
                View childAt = getChildAt(i);
                rect.left = childAt.getRight();
                rect.right = childAt.getRight() + this.mDividerWidth;
                if (rect.left < getPaddingLeft()) {
                    rect.left = getPaddingLeft();
                }
                if (rect.right > getWidth() - getPaddingRight()) {
                    rect.right = getWidth() - getPaddingRight();
                }
                drawDivider(canvas, rect);
                if (i == 0 && childAt.getLeft() > getPaddingLeft()) {
                    rect.left = getPaddingLeft();
                    rect.right = childAt.getLeft();
                    drawDivider(canvas, rect);
                }
            }
        }
    }

    private void drawEdgeGlow(Canvas canvas) {
        if (this.mEdgeGlowLeft != null && !this.mEdgeGlowLeft.isFinished() && isEdgeGlowEnabled()) {
            int save = canvas.save();
            int height = getHeight();
            canvas.rotate(-90.0f, 0.0f, 0.0f);
            canvas.translate((float) ((-height) + getPaddingBottom()), 0.0f);
            this.mEdgeGlowLeft.setSize(getRenderHeight(), getRenderWidth());
            if (this.mEdgeGlowLeft.draw(canvas)) {
                invalidate();
            }
            canvas.restoreToCount(save);
        } else if (this.mEdgeGlowRight != null && !this.mEdgeGlowRight.isFinished() && isEdgeGlowEnabled()) {
            int save2 = canvas.save();
            int width = getWidth();
            canvas.rotate(90.0f, 0.0f, 0.0f);
            canvas.translate((float) getPaddingTop(), (float) (-width));
            this.mEdgeGlowRight.setSize(getRenderHeight(), getRenderWidth());
            if (this.mEdgeGlowRight.draw(canvas)) {
                invalidate();
            }
            canvas.restoreToCount(save2);
        }
    }

    private void fillList(int i) {
        int i2 = 0;
        View rightmostChild = getRightmostChild();
        fillListRight(rightmostChild != null ? rightmostChild.getRight() : 0, i);
        View leftmostChild = getLeftmostChild();
        if (leftmostChild != null) {
            i2 = leftmostChild.getLeft();
        }
        fillListLeft(i2, i);
    }

    private void fillListLeft(int i, int i2) {
        while ((i + i2) - this.mDividerWidth > 0 && this.mLeftViewAdapterIndex >= 1) {
            this.mLeftViewAdapterIndex--;
            View view = this.b.getView(this.mLeftViewAdapterIndex, getRecycledView(this.mLeftViewAdapterIndex), this);
            addAndMeasureChild(view, 0);
            i -= this.mLeftViewAdapterIndex == 0 ? view.getMeasuredWidth() : this.mDividerWidth + view.getMeasuredWidth();
            this.mDisplayOffset -= i + i2 == 0 ? view.getMeasuredWidth() : this.mDividerWidth + view.getMeasuredWidth();
        }
    }

    private void fillListRight(int i, int i2) {
        while (i + i2 + this.mDividerWidth < getWidth() && this.mRightViewAdapterIndex + 1 < this.b.getCount()) {
            this.mRightViewAdapterIndex++;
            if (this.mLeftViewAdapterIndex < 0) {
                this.mLeftViewAdapterIndex = this.mRightViewAdapterIndex;
            }
            View view = this.b.getView(this.mRightViewAdapterIndex, getRecycledView(this.mRightViewAdapterIndex), this);
            addAndMeasureChild(view, -1);
            i += (this.mRightViewAdapterIndex == 0 ? 0 : this.mDividerWidth) + view.getMeasuredWidth();
            determineIfLowOnData();
        }
    }

    private View getChild(int i) {
        if (i < this.mLeftViewAdapterIndex || i > this.mRightViewAdapterIndex) {
            return null;
        }
        return getChildAt(i - this.mLeftViewAdapterIndex);
    }

    /* access modifiers changed from: private */
    public int getChildIndex(int i, int i2) {
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            getChildAt(i3).getHitRect(this.mRect);
            if (this.mRect.contains(i, i2)) {
                return i3;
            }
        }
        return -1;
    }

    private LayoutParams getLayoutParams(View view) {
        LayoutParams layoutParams = view.getLayoutParams();
        return layoutParams == null ? new LayoutParams(-2, -1) : layoutParams;
    }

    private View getLeftmostChild() {
        return getChildAt(0);
    }

    private View getRecycledView(int i) {
        int itemViewType = this.b.getItemViewType(i);
        if (isItemViewTypeValid(itemViewType)) {
            return (View) ((Queue) this.mRemovedViewsCache.get(itemViewType)).poll();
        }
        return null;
    }

    private int getRenderHeight() {
        return (getHeight() - getPaddingTop()) - getPaddingBottom();
    }

    private int getRenderWidth() {
        return (getWidth() - getPaddingLeft()) - getPaddingRight();
    }

    private View getRightmostChild() {
        return getChildAt(getChildCount() - 1);
    }

    private void initView() {
        this.mLeftViewAdapterIndex = -1;
        this.mRightViewAdapterIndex = -1;
        this.mDisplayOffset = 0;
        this.c = 0;
        this.d = 0;
        this.mMaxX = Integer.MAX_VALUE;
        setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE);
    }

    private void initializeRecycledViewCache(int i) {
        this.mRemovedViewsCache.clear();
        for (int i2 = 0; i2 < i; i2++) {
            this.mRemovedViewsCache.add(new LinkedList());
        }
    }

    private boolean isEdgeGlowEnabled() {
        return this.b != null && !this.b.isEmpty() && this.mMaxX > 0;
    }

    private boolean isItemViewTypeValid(int i) {
        return i < this.mRemovedViewsCache.size();
    }

    private boolean isLastItemInAdapter(int i) {
        return i == this.b.getCount() + -1;
    }

    @SuppressLint("WrongConstant")
    private void measureChild(View view) {
        LayoutParams layoutParams = getLayoutParams(view);
        view.measure(layoutParams.width > 0 ? MeasureSpec.makeMeasureSpec(layoutParams.width, 1073741824) : MeasureSpec.makeMeasureSpec(0, 0), ViewGroup.getChildMeasureSpec(this.mHeightMeasureSpec, getPaddingTop() + getPaddingBottom(), layoutParams.height));
    }

    private void positionChildren(int i) {
        int childCount = getChildCount();
        if (childCount > 0) {
            this.mDisplayOffset += i;
            int i2 = this.mDisplayOffset;
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = getChildAt(i3);
                int paddingLeft = getPaddingLeft() + i2;
                int paddingTop = getPaddingTop();
                childAt.layout(paddingLeft, paddingTop, childAt.getMeasuredWidth() + paddingLeft, childAt.getMeasuredHeight() + paddingTop);
                i2 += childAt.getMeasuredWidth() + this.mDividerWidth;
            }
        }
    }

    private void recycleView(int i, View view) {
        int itemViewType = this.b.getItemViewType(i);
        if (isItemViewTypeValid(itemViewType)) {
            ((Queue) this.mRemovedViewsCache.get(itemViewType)).offer(view);
        }
    }

    private void releaseEdgeGlow() {
        if (this.mEdgeGlowLeft != null) {
            this.mEdgeGlowLeft.onRelease();
        }
        if (this.mEdgeGlowRight != null) {
            this.mEdgeGlowRight.onRelease();
        }
    }

    private void removeNonVisibleChildren(int i) {
        View leftmostChild = getLeftmostChild();
        while (leftmostChild != null && leftmostChild.getRight() + i <= 0) {
            this.mDisplayOffset = (isLastItemInAdapter(this.mLeftViewAdapterIndex) ? leftmostChild.getMeasuredWidth() : this.mDividerWidth + leftmostChild.getMeasuredWidth()) + this.mDisplayOffset;
            recycleView(this.mLeftViewAdapterIndex, leftmostChild);
            removeViewInLayout(leftmostChild);
            this.mLeftViewAdapterIndex++;
            leftmostChild = getLeftmostChild();
        }
        View rightmostChild = getRightmostChild();
        while (rightmostChild != null && rightmostChild.getLeft() + i >= getWidth()) {
            recycleView(this.mRightViewAdapterIndex, rightmostChild);
            removeViewInLayout(rightmostChild);
            this.mRightViewAdapterIndex--;
            rightmostChild = getRightmostChild();
        }
    }

    /* access modifiers changed from: private */
    public void requestParentListViewToNotInterceptTouchEvents(Boolean bool) {
        if (this.mIsParentVerticiallyScrollableViewDisallowingInterceptTouchEvent != bool.booleanValue()) {
            for (View view = this; view.getParent() instanceof View; view = (View) view.getParent()) {
                if ((view.getParent() instanceof ListView) || (view.getParent() instanceof ScrollView)) {
                    view.getParent().requestDisallowInterceptTouchEvent(bool.booleanValue());
                    this.mIsParentVerticiallyScrollableViewDisallowingInterceptTouchEvent = bool.booleanValue();
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void reset() {
        initView();
        removeAllViewsInLayout();
        requestLayout();
    }

    private void retrieveXmlConfiguration(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.HorizontalListView);
        if (attributeSet != null) {
            @SuppressLint("ResourceType") Drawable drawable = obtainStyledAttributes.getDrawable(1);
            if (drawable != null) {
                setDivider(drawable);
            }
            @SuppressLint("ResourceType") int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(3, 0);
            if (dimensionPixelSize != 0) {
                setDividerWidth(dimensionPixelSize);
            }
            obtainStyledAttributes.recycle();
        }
    }

    /* access modifiers changed from: private */
    public void setCurrentScrollState(OnScrollStateChangedListener.ScrollState scrollState) {
        if (!(this.mCurrentScrollState == scrollState || this.mOnScrollStateChangedListener == null)) {
            this.mOnScrollStateChangedListener.onScrollStateChanged(scrollState);
        }
        this.mCurrentScrollState = scrollState;
    }

    /* access modifiers changed from: private */
    public void unpressTouchedChild() {
        if (this.mViewBeingTouched != null) {
            this.mViewBeingTouched.setPressed(false);
            refreshDrawableState();
            this.mViewBeingTouched = null;
        }
    }

    /* access modifiers changed from: private */
    public void updateOverscrollAnimation(int i) {
        if (this.mEdgeGlowLeft != null && this.mEdgeGlowRight != null) {
            int i2 = this.c + i;
            if (this.a != null && !this.a.isFinished()) {
                return;
            }
            if (i2 < 0) {
                this.mEdgeGlowLeft.onPull(((float) Math.abs(i)) / ((float) getRenderWidth()));
                if (!this.mEdgeGlowRight.isFinished()) {
                    this.mEdgeGlowRight.onRelease();
                }
            } else if (i2 > this.mMaxX) {
                this.mEdgeGlowRight.onPull(((float) Math.abs(i)) / ((float) getRenderWidth()));
                if (!this.mEdgeGlowLeft.isFinished()) {
                    this.mEdgeGlowLeft.onRelease();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean a(MotionEvent motionEvent) {
        this.mBlockTouchAction = !this.a.isFinished();
        this.a.forceFinished(true);
        setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE);
        unpressTouchedChild();
        if (!this.mBlockTouchAction) {
            int childIndex = getChildIndex((int) motionEvent.getX(), (int) motionEvent.getY());
            if (childIndex >= 0) {
                this.mViewBeingTouched = getChildAt(childIndex);
                if (this.mViewBeingTouched != null) {
                    this.mViewBeingTouched.setPressed(true);
                    refreshDrawableState();
                }
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean a(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        this.a.fling(this.d, 0, (int) (-f), 0, 0, this.mMaxX, 0, 0);
        setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_FLING);
        requestLayout();
        return true;
    }

    public void add(int i) {
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawEdgeGlow(canvas);
    }

    /* access modifiers changed from: protected */
    public void dispatchSetPressed(boolean z) {
    }

    public ListAdapter getAdapter() {
        return this.b;
    }

    public int getFirstVisiblePosition() {
        return this.mLeftViewAdapterIndex;
    }

    public int getLastVisiblePosition() {
        return this.mRightViewAdapterIndex;
    }

    /* access modifiers changed from: protected */
    public float getLeftFadingEdgeStrength() {
        int horizontalFadingEdgeLength = getHorizontalFadingEdgeLength();
        if (this.c == 0) {
            return 0.0f;
        }
        if (this.c < horizontalFadingEdgeLength) {
            return ((float) this.c) / ((float) horizontalFadingEdgeLength);
        }
        return 1.0f;
    }

    /* access modifiers changed from: protected */
    public float getRightFadingEdgeStrength() {
        int horizontalFadingEdgeLength = getHorizontalFadingEdgeLength();
        if (this.c == this.mMaxX) {
            return 0.0f;
        }
        if (this.mMaxX - this.c < horizontalFadingEdgeLength) {
            return ((float) (this.mMaxX - this.c)) / ((float) horizontalFadingEdgeLength);
        }
        return 1.0f;
    }

    public View getSelectedView() {
        return getChild(this.mCurrentlySelectedAdapterIndex);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDividers(canvas);
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"WrongCall"})
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.b != null) {
            invalidate();
            if (this.mDataChanged) {
                int i5 = this.c;
                initView();
                removeAllViewsInLayout();
                this.d = i5;
                this.mDataChanged = false;
            }
            if (this.mRestoreX != null) {
                this.d = this.mRestoreX.intValue();
                this.mRestoreX = null;
            }
            if (this.a.computeScrollOffset()) {
                this.d = this.a.getCurrX();
            }
            if (this.d < 0) {
                this.d = 0;
                if (this.mEdgeGlowLeft.isFinished()) {
                    this.mEdgeGlowLeft.onAbsorb((int) determineFlingAbsorbVelocity());
                }
                this.a.forceFinished(true);
                setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE);
            } else if (this.d > this.mMaxX) {
                this.d = this.mMaxX;
                if (this.mEdgeGlowRight.isFinished()) {
                    this.mEdgeGlowRight.onAbsorb((int) determineFlingAbsorbVelocity());
                }
                this.a.forceFinished(true);
                setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE);
            }
            int i6 = this.c - this.d;
            removeNonVisibleChildren(i6);
            fillList(i6);
            positionChildren(i6);
            this.c = this.d;
            if (determineMaxX()) {
                onLayout(z, i, i2, i3, i4);
            } else if (!this.a.isFinished()) {
                ViewCompat.postOnAnimation(this, this.mDelayedLayout);
            } else if (this.mCurrentScrollState == OnScrollStateChangedListener.ScrollState.SCROLL_STATE_FLING) {
                setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.mHeightMeasureSpec = i2;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            this.mRestoreX = Integer.valueOf(bundle.getInt(BUNDLE_ID_CURRENT_X));
            super.onRestoreInstanceState(bundle.getParcelable(BUNDLE_ID_PARENT_STATE));
        }
    }

    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_ID_PARENT_STATE, super.onSaveInstanceState());
        bundle.putInt(BUNDLE_ID_CURRENT_X, this.c);
        return bundle;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1) {
            if (this.a == null || this.a.isFinished()) {
                setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE);
            }
            requestParentListViewToNotInterceptTouchEvents(Boolean.valueOf(false));
            releaseEdgeGlow();
        } else if (motionEvent.getAction() == 3) {
            unpressTouchedChild();
            releaseEdgeGlow();
            requestParentListViewToNotInterceptTouchEvents(Boolean.valueOf(false));
        }
        return super.onTouchEvent(motionEvent);
    }

    public void scrollTo(int i) {
        this.a.startScroll(this.d, 0, i - this.d, 0);
        setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_FLING);
        requestLayout();
    }

    public void setAdapter(ListAdapter listAdapter) {
        if (this.b != null) {
            this.b.unregisterDataSetObserver(this.mAdapterDataObserver);
        }
        if (listAdapter != null) {
            this.mHasNotifiedRunningLowOnData = false;
            this.b = listAdapter;
            this.b.registerDataSetObserver(this.mAdapterDataObserver);
        }
        initializeRecycledViewCache(this.b.getViewTypeCount());
        reset();
    }

    public void setDivider(Drawable drawable) {
        this.mDivider = drawable;
        if (drawable != null) {
            setDividerWidth(drawable.getIntrinsicWidth());
        } else {
            setDividerWidth(0);
        }
    }

    public void setDividerWidth(int i) {
        this.mDividerWidth = i;
        requestLayout();
        invalidate();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public void setOnScrollStateChangedListener(OnScrollStateChangedListener onScrollStateChangedListener) {
        this.mOnScrollStateChangedListener = onScrollStateChangedListener;
    }

    public void setRunningOutOfDataListener(RunningOutOfDataListener runningOutOfDataListener, int i) {
        this.mRunningOutOfDataListener = runningOutOfDataListener;
        this.mRunningOutOfDataThreshold = i;
    }

    public void setSelection(int i) {
        this.mCurrentlySelectedAdapterIndex = i;
    }
}
