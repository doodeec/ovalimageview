package com.doodeec.ovalimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

/**
 * Oval image view displays image with clipped oval outline
 * Compatible with pre-Lollipop devices
 *
 * @author Dusan Bartos
 */
public class OvalImageView extends ImageView {
    private Path mClipPath;

    public OvalImageView(Context context) {
        this(context, null);
    }

    public OvalImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OvalImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mClipPath = new Path();
        mClipPath.addRoundRect(new RectF(0, 0, w, h), w / 2, h / 2, Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.clipPath(mClipPath);
        super.onDraw(canvas);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        clipPathInternal();
        super.setImageBitmap(bm);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        clipPathInternal();
        super.setImageDrawable(drawable);
    }

    private void clipPathInternal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
                @Override
                @SuppressWarnings("NewApi")
                public void getOutline(View view, Outline outline) {
                    outline.setOval(0, 0, getWidth(), getHeight());
                }
            };

            setOutlineProvider(viewOutlineProvider);
            setClipToOutline(true);
        } else {
            onSizeChanged(getWidth(), getHeight(), 0, 0);
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        invalidate();
    }
}
