package com.candycam.photoeditor.Sticker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.core.view.MotionEventCompat;

import com.candycam.photoeditor.R;

public class StickerView extends ImageView {
    private static final float BITMAP_SCALE = 0.7f;
    private static final String TAG = "StickerView";
    private float MAX_SCALE = 1.2f;
    private float MIN_SCALE = 0.5f;
    private Bitmap deleteBitmap;
    private int deleteBitmapHeight;
    private int deleteBitmapWidth;
    private DisplayMetrics dm;
    private Rect dst_delete;
    private Rect dst_flipV;
    private Rect dst_resize;
    private Rect dst_top;
    private Bitmap flipVBitmap;
    private int flipVBitmapHeight;
    private int flipVBitmapWidth;
    private double halfDiagonalLength;
    private boolean isHorizonMirror = false;
    private boolean isInEdit = true;
    private boolean isInResize = false;
    private boolean isInSide;
    private boolean isPointerDown = false;
    private float lastLength;
    private float lastRotateDegree;
    private float lastX;
    private float lastY;
    private Paint localPaint;
    private Bitmap mBitmap;
    private int mScreenHeight;
    private int mScreenwidth;
    private Matrix matrix = new Matrix();
    private PointF mid = new PointF();
    private float oldDis;
    private OperationListener operationListener;
    private float oringinWidth = 0.0f;
    private final float pointerLimitDis = 20.0f;
    private final float pointerZoomCoeff = 0.09f;
    private Bitmap resizeBitmap;
    private int resizeBitmapHeight;
    private int resizeBitmapWidth;
    private final long stickerId = 0;
    private Bitmap topBitmap;
    private int topBitmapHeight;
    private int topBitmapWidth;

    public interface OperationListener {
        void onDeleteClick();

        void onEdit(StickerView stickerView);

        void onTop(StickerView stickerView);
    }

    public StickerView(Context context) {
        super(context);
        init();
    }

    public StickerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public StickerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private float diagonalLength(MotionEvent motionEvent) {
        return (float) Math.hypot((double) (motionEvent.getX(0) - this.mid.x), (double) (motionEvent.getY(0) - this.mid.y));
    }

    private void init() {
        this.dst_delete = new Rect();
        this.dst_resize = new Rect();
        this.dst_flipV = new Rect();
        this.dst_top = new Rect();
        this.localPaint = new Paint();
        this.localPaint.setColor(getResources().getColor(R.color.white));
        this.localPaint.setAntiAlias(true);
        this.localPaint.setDither(true);
        this.localPaint.setStyle(Style.STROKE);
        this.localPaint.setStrokeWidth(2.0f);
        this.dm = getResources().getDisplayMetrics();
        this.mScreenwidth = this.dm.widthPixels;
        this.mScreenHeight = this.dm.heightPixels;
    }

    private void initBitmaps() {
        if (this.mBitmap.getWidth() >= this.mBitmap.getHeight()) {
            float f = (float) (this.mScreenwidth / 8);
            if (((float) this.mBitmap.getWidth()) < f) {
                this.MIN_SCALE = 1.0f;
            } else {
                this.MIN_SCALE = (f * 1.0f) / ((float) this.mBitmap.getWidth());
            }
            if (this.mBitmap.getWidth() > this.mScreenwidth) {
                this.MAX_SCALE = 1.0f;
            } else {
                this.MAX_SCALE = (((float) this.mScreenwidth) * 1.0f) / ((float) this.mBitmap.getWidth());
            }
        } else {
            float f2 = (float) (this.mScreenwidth / 8);
            if (((float) this.mBitmap.getHeight()) < f2) {
                this.MIN_SCALE = 1.0f;
            } else {
                this.MIN_SCALE = (f2 * 1.0f) / ((float) this.mBitmap.getHeight());
            }
            if (this.mBitmap.getHeight() > this.mScreenwidth) {
                this.MAX_SCALE = 1.0f;
            } else {
                this.MAX_SCALE = (((float) this.mScreenwidth) * 1.0f) / ((float) this.mBitmap.getHeight());
            }
        }
        this.topBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_top_enable);
        this.deleteBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_delete);
        this.flipVBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_flip);
        this.resizeBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_resize);
        this.deleteBitmapWidth = (int) (((float) this.deleteBitmap.getWidth()) * BITMAP_SCALE);
        this.deleteBitmapHeight = (int) (((float) this.deleteBitmap.getHeight()) * BITMAP_SCALE);
        this.resizeBitmapWidth = (int) (((float) this.resizeBitmap.getWidth()) * BITMAP_SCALE);
        this.resizeBitmapHeight = (int) (((float) this.resizeBitmap.getHeight()) * BITMAP_SCALE);
        this.flipVBitmapWidth = (int) (((float) this.flipVBitmap.getWidth()) * BITMAP_SCALE);
        this.flipVBitmapHeight = (int) (((float) this.flipVBitmap.getHeight()) * BITMAP_SCALE);
        this.topBitmapWidth = (int) (((float) this.topBitmap.getWidth()) * BITMAP_SCALE);
        this.topBitmapHeight = (int) (((float) this.topBitmap.getHeight()) * BITMAP_SCALE);
    }

    private boolean isInBitmap(MotionEvent motionEvent) {
        float[] fArr = new float[9];
        this.matrix.getValues(fArr);
        float f = (0.0f * fArr[0]) + (0.0f * fArr[1]) + fArr[2];
        float f2 = (0.0f * fArr[3]) + (0.0f * fArr[4]) + fArr[5];
        float width = (fArr[0] * ((float) this.mBitmap.getWidth())) + (0.0f * fArr[1]) + fArr[2];
        float width2 = (fArr[3] * ((float) this.mBitmap.getWidth())) + (0.0f * fArr[4]) + fArr[5];
        float height = (0.0f * fArr[0]) + (fArr[1] * ((float) this.mBitmap.getHeight())) + fArr[2];
        float height2 = (0.0f * fArr[3]) + (fArr[4] * ((float) this.mBitmap.getHeight())) + fArr[5];
        float width3 = (fArr[0] * ((float) this.mBitmap.getWidth())) + (fArr[1] * ((float) this.mBitmap.getHeight())) + fArr[2];
        float width4 = fArr[5] + (fArr[3] * ((float) this.mBitmap.getWidth())) + (fArr[4] * ((float) this.mBitmap.getHeight()));
        return pointInRect(new float[]{f, width, width3, height}, new float[]{f2, width2, width4, height2}, motionEvent.getX(0), motionEvent.getY(0));
    }

    private boolean isInButton(MotionEvent motionEvent, Rect rect) {
        return motionEvent.getX(0) >= ((float) rect.left) && motionEvent.getX(0) <= ((float) rect.right) && motionEvent.getY(0) >= ((float) rect.top) && motionEvent.getY(0) <= ((float) rect.bottom);
    }

    private boolean isInResize(MotionEvent motionEvent) {
        return motionEvent.getX(0) >= ((float) (this.dst_resize.left + -20)) && motionEvent.getX(0) <= ((float) (this.dst_resize.right + 20)) && motionEvent.getY(0) >= ((float) (this.dst_resize.top + -20)) && motionEvent.getY(0) <= ((float) (this.dst_resize.bottom + 20));
    }

    private void midDiagonalPoint(PointF pointF) {
        float[] fArr = new float[9];
        this.matrix.getValues(fArr);
        float width = (fArr[0] * ((float) this.mBitmap.getWidth())) + (fArr[1] * ((float) this.mBitmap.getHeight())) + fArr[2];
        pointF.set(((((fArr[0] * 0.0f) + (fArr[1] * 0.0f)) + fArr[2]) + width) / 2.0f, ((fArr[5] + ((fArr[3] * ((float) this.mBitmap.getWidth())) + (fArr[4] * ((float) this.mBitmap.getHeight())))) + (((fArr[3] * 0.0f) + (fArr[4] * 0.0f)) + fArr[5])) / 2.0f);
    }

    private void midPointToStartPoint(MotionEvent motionEvent) {
        float[] fArr = new float[9];
        this.matrix.getValues(fArr);
        float f = (fArr[0] * 0.0f) + (fArr[1] * 0.0f) + fArr[2];
        this.mid.set((f + motionEvent.getX(0)) / 2.0f, ((fArr[5] + ((fArr[3] * 0.0f) + (fArr[4] * 0.0f))) + motionEvent.getY(0)) / 2.0f);
    }

    private boolean pointInRect(float[] fArr, float[] fArr2, float f, float f2) {
        double hypot = Math.hypot((double) (fArr[0] - fArr[1]), (double) (fArr2[0] - fArr2[1]));
        double hypot2 = Math.hypot((double) (fArr[1] - fArr[2]), (double) (fArr2[1] - fArr2[2]));
        double hypot3 = Math.hypot((double) (fArr[3] - fArr[2]), (double) (fArr2[3] - fArr2[2]));
        double hypot4 = Math.hypot((double) (fArr[0] - fArr[3]), (double) (fArr2[0] - fArr2[3]));
        double hypot5 = Math.hypot((double) (f - fArr[0]), (double) (f2 - fArr2[0]));
        double hypot6 = Math.hypot((double) (f - fArr[1]), (double) (f2 - fArr2[1]));
        double hypot7 = Math.hypot((double) (f - fArr[2]), (double) (f2 - fArr2[2]));
        double hypot8 = Math.hypot((double) (f - fArr[3]), (double) (f2 - fArr2[3]));
        double d = ((hypot + hypot5) + hypot6) / 2.0d;
        double d2 = ((hypot2 + hypot6) + hypot7) / 2.0d;
        double d3 = ((hypot3 + hypot7) + hypot8) / 2.0d;
        double d4 = ((hypot4 + hypot8) + hypot5) / 2.0d;
        return Math.abs((hypot * hypot2) - (((Math.sqrt((((d - hypot) * d) * (d - hypot5)) * (d - hypot6)) + Math.sqrt((((d2 - hypot2) * d2) * (d2 - hypot6)) * (d2 - hypot7))) + Math.sqrt((((d3 - hypot3) * d3) * (d3 - hypot7)) * (d3 - hypot8))) + Math.sqrt((((d4 - hypot4) * d4) * (d4 - hypot8)) * (d4 - hypot5)))) < 0.5d;
    }

    private float rotationToStartPoint(MotionEvent motionEvent) {
        float[] fArr = new float[9];
        this.matrix.getValues(fArr);
        float f = (fArr[0] * 0.0f) + (fArr[1] * 0.0f) + fArr[2];
        return (float) Math.toDegrees(Math.atan2((double) (motionEvent.getY(0) - (fArr[5] + ((fArr[3] * 0.0f) + (fArr[4] * 0.0f)))), (double) (motionEvent.getX(0) - f)));
    }

    private void setDiagonalLength() {
        this.halfDiagonalLength = Math.hypot((double) this.mBitmap.getWidth(), (double) this.mBitmap.getHeight()) / 2.0d;
    }

    private float spacing(MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() != 2) {
            return 0.0f;
        }
        float x = motionEvent.getX(0) - motionEvent.getX(1);
        float y = motionEvent.getY(0) - motionEvent.getY(1);
        return (float) Math.sqrt((double) ((x * x) + (y * y)));
    }

    public StickerPropertyModel calculate(StickerPropertyModel stickerPropertyModel) {
        float[] fArr = new float[9];
        this.matrix.getValues(fArr);
        float f = fArr[2];
        Log.d(TAG, "tx : " + f + " ty : " + fArr[5]);
        float f2 = fArr[0];
        float f3 = fArr[3];
        float sqrt = (float) Math.sqrt((double) ((f2 * f2) + (f3 * f3)));
        Log.d(TAG, "rScale : " + sqrt);
        float round = (float) Math.round(Math.atan2((double) fArr[1], (double) fArr[0]) * 57.29577951308232d);
        Log.d(TAG, "rAngle : " + round);
        PointF pointF = new PointF();
        midDiagonalPoint(pointF);
        Log.d(TAG, " width  : " + (((float) this.mBitmap.getWidth()) * sqrt) + " height " + (((float) this.mBitmap.getHeight()) * sqrt));
        float f4 = pointF.x;
        float f5 = pointF.y;
        Log.d(TAG, "midX : " + f4 + " midY : " + f5);
        stickerPropertyModel.setDegree((float) Math.toRadians((double) round));
        stickerPropertyModel.setScaling((((float) this.mBitmap.getWidth()) * sqrt) / ((float) this.mScreenwidth));
        stickerPropertyModel.setxLocation(f4 / ((float) this.mScreenwidth));
        stickerPropertyModel.setyLocation(f5 / ((float) this.mScreenwidth));
        stickerPropertyModel.setStickerId(this.stickerId);
        if (this.isHorizonMirror) {
            stickerPropertyModel.setHorizonMirror(1);
        } else {
            stickerPropertyModel.setHorizonMirror(2);
        }
        return stickerPropertyModel;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mBitmap != null) {
            float[] fArr = new float[9];
            this.matrix.getValues(fArr);
            float f = (0.0f * fArr[0]) + (0.0f * fArr[1]) + fArr[2];
            float f2 = (0.0f * fArr[3]) + (0.0f * fArr[4]) + fArr[5];
            float width = (fArr[0] * ((float) this.mBitmap.getWidth())) + (0.0f * fArr[1]) + fArr[2];
            float width2 = (fArr[3] * ((float) this.mBitmap.getWidth())) + (0.0f * fArr[4]) + fArr[5];
            float height = (0.0f * fArr[0]) + (fArr[1] * ((float) this.mBitmap.getHeight())) + fArr[2];
            float height2 = (0.0f * fArr[3]) + (fArr[4] * ((float) this.mBitmap.getHeight())) + fArr[5];
            float width3 = (fArr[0] * ((float) this.mBitmap.getWidth())) + (fArr[1] * ((float) this.mBitmap.getHeight())) + fArr[2];
            float width4 = (fArr[3] * ((float) this.mBitmap.getWidth())) + (fArr[4] * ((float) this.mBitmap.getHeight())) + fArr[5];
            canvas.save();
            canvas.drawBitmap(this.mBitmap, this.matrix, null);
            this.dst_delete.left = (int) (width - ((float) (this.deleteBitmapWidth / 2)));
            this.dst_delete.right = (int) (((float) (this.deleteBitmapWidth / 2)) + width);
            this.dst_delete.top = (int) (width2 - ((float) (this.deleteBitmapHeight / 2)));
            this.dst_delete.bottom = (int) (((float) (this.deleteBitmapHeight / 2)) + width2);
            this.dst_resize.left = (int) (width3 - ((float) (this.resizeBitmapWidth / 2)));
            this.dst_resize.right = (int) (((float) (this.resizeBitmapWidth / 2)) + width3);
            this.dst_resize.top = (int) (width4 - ((float) (this.resizeBitmapHeight / 2)));
            this.dst_resize.bottom = (int) (((float) (this.resizeBitmapHeight / 2)) + width4);
            this.dst_top.left = (int) (f - ((float) (this.flipVBitmapWidth / 2)));
            this.dst_top.right = (int) (((float) (this.flipVBitmapWidth / 2)) + f);
            this.dst_top.top = (int) (f2 - ((float) (this.flipVBitmapHeight / 2)));
            this.dst_top.bottom = (int) (((float) (this.flipVBitmapHeight / 2)) + f2);
            this.dst_flipV.left = (int) (height - ((float) (this.topBitmapWidth / 2)));
            this.dst_flipV.right = (int) (((float) (this.topBitmapWidth / 2)) + height);
            this.dst_flipV.top = (int) (height2 - ((float) (this.topBitmapHeight / 2)));
            this.dst_flipV.bottom = (int) (((float) (this.topBitmapHeight / 2)) + height2);
            if (this.isInEdit) {
                canvas.drawLine(f, f2, width, width2, this.localPaint);
                canvas.drawLine(width, width2, width3, width4, this.localPaint);
                canvas.drawLine(height, height2, width3, width4, this.localPaint);
                canvas.drawLine(height, height2, f, f2, this.localPaint);
                canvas.drawBitmap(this.deleteBitmap, null, this.dst_delete, null);
                canvas.drawBitmap(this.resizeBitmap, null, this.dst_resize, null);
                canvas.drawBitmap(this.flipVBitmap, null, this.dst_flipV, null);
                canvas.drawBitmap(this.topBitmap, null, this.dst_top, null);
            }
            canvas.restore();
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z = true;
        float f = 1.0f;
        boolean z2 = false;
        switch (MotionEventCompat.getActionMasked(motionEvent)) {
            case 0:
                if (!isInButton(motionEvent, this.dst_delete)) {
                    if (!isInResize(motionEvent)) {
                        if (!isInButton(motionEvent, this.dst_flipV)) {
                            if (!isInButton(motionEvent, this.dst_top)) {
                                if (!isInBitmap(motionEvent)) {
                                    z = false;
                                    break;
                                } else {
                                    this.isInSide = true;
                                    this.lastX = motionEvent.getX(0);
                                    this.lastY = motionEvent.getY(0);
                                    break;
                                }
                            } else {
                                bringToFront();
                                if (this.operationListener != null) {
                                    this.operationListener.onTop(this);
                                    break;
                                }
                            }
                        } else {
                            PointF pointF = new PointF();
                            midDiagonalPoint(pointF);
                            this.matrix.postScale(-1.0f, 1.0f, pointF.x, pointF.y);
                            if (!this.isHorizonMirror) {
                                z2 = true;
                            }
                            this.isHorizonMirror = z2;
                            invalidate();
                            break;
                        }
                    } else {
                        this.isInResize = true;
                        this.lastRotateDegree = rotationToStartPoint(motionEvent);
                        midPointToStartPoint(motionEvent);
                        this.lastLength = diagonalLength(motionEvent);
                        break;
                    }
                } else if (this.operationListener != null) {
                    this.operationListener.onDeleteClick();
                    break;
                }
                break;
            case 1:
            case 3:
                this.isInResize = false;
                this.isInSide = false;
                this.isPointerDown = false;
                break;
            case 2:
                if (!this.isPointerDown) {
                    if (!this.isInResize) {
                        if (this.isInSide) {
                            float x = motionEvent.getX(0);
                            float y = motionEvent.getY(0);
                            this.matrix.postTranslate(x - this.lastX, y - this.lastY);
                            this.lastX = x;
                            this.lastY = y;
                            invalidate();
                            break;
                        }
                    } else {
                        this.matrix.postRotate((rotationToStartPoint(motionEvent) - this.lastRotateDegree) * 2.0f, this.mid.x, this.mid.y);
                        this.lastRotateDegree = rotationToStartPoint(motionEvent);
                        float diagonalLength = diagonalLength(motionEvent) / this.lastLength;
                        if ((((double) diagonalLength(motionEvent)) / this.halfDiagonalLength > ((double) this.MIN_SCALE) || diagonalLength >= 1.0f) && (((double) diagonalLength(motionEvent)) / this.halfDiagonalLength < ((double) this.MAX_SCALE) || diagonalLength <= 1.0f)) {
                            this.lastLength = diagonalLength(motionEvent);
                            f = diagonalLength;
                        } else if (!isInResize(motionEvent)) {
                            this.isInResize = false;
                        }
                        this.matrix.postScale(f, f, this.mid.x, this.mid.y);
                        invalidate();
                        break;
                    }
                } else {
                    float spacing = spacing(motionEvent);
                    float f2 = (spacing == 0.0f || spacing < 20.0f) ? 1.0f : (((spacing / this.oldDis) - 1.0f) * 0.09f) + 1.0f;
                    float abs = (((float) Math.abs(this.dst_flipV.left - this.dst_resize.left)) * f2) / this.oringinWidth;
                    if ((abs > this.MIN_SCALE || f2 >= 1.0f) && (abs < this.MAX_SCALE || f2 <= 1.0f)) {
                        this.lastLength = diagonalLength(motionEvent);
                    } else {
                        f2 = 1.0f;
                    }
                    this.matrix.postScale(f2, f2, this.mid.x, this.mid.y);
                    invalidate();
                    break;
                }
                break;
            case 5:
                if (spacing(motionEvent) > 20.0f) {
                    this.oldDis = spacing(motionEvent);
                    this.isPointerDown = true;
                    midPointToStartPoint(motionEvent);
                } else {
                    this.isPointerDown = false;
                }
                this.isInSide = false;
                this.isInResize = false;
                break;
        }
        if (z && this.operationListener != null) {
            this.operationListener.onEdit(this);
        }
        return z;
    }

    public void setBitmap(Bitmap bitmap) {
        this.matrix.reset();
        this.mBitmap = bitmap;
        setDiagonalLength();
        initBitmaps();
        int width = this.mBitmap.getWidth();
        this.mBitmap.getHeight();
        this.oringinWidth = (float) width;
        float f = (this.MIN_SCALE + this.MAX_SCALE) / 2.0f;
        this.matrix.postTranslate(300.0f, 300.0f);
        invalidate();
    }

    public void setImageResource(int i) {
        setBitmap(BitmapFactory.decodeResource(getResources(), i));
    }

    public void setInEdit(boolean z) {
        this.isInEdit = z;
        invalidate();
    }

    public void setOperationListener(OperationListener operationListener2) {
        this.operationListener = operationListener2;
    }
}
