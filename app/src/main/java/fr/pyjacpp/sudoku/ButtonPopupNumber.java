package fr.pyjacpp.sudoku;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

/**
 * Custom button
 */
public class ButtonPopupNumber extends View {
    private String text;
    private Drawable image;

    private TextPaint textPaint;

    private final static float FONT_COEFFICIENT = 0.8f;

    private boolean selectable = true;
    private boolean select = false;

    public ButtonPopupNumber(Context context) {
        super(context);
        init(null, 0);
    }

    public ButtonPopupNumber(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ButtonPopupNumber(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ButtonPopupNumber, defStyle, 0);

        if (a.hasValue(R.styleable.ButtonPopupNumber_image))
            image = a.getDrawable(R.styleable.ButtonPopupNumber_image);


        text = a.getString(
                R.styleable.ButtonPopupNumber_text);

        selectable = a.getBoolean(R.styleable.ButtonPopupNumber_selectable,
                selectable);

        a.recycle();

        // Set up a default TextPaint object
        textPaint = new TextPaint();
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        textPaint.setTextSize(contentWidth * FONT_COEFFICIENT);
        TextPaint.FontMetrics textMetrics = textPaint.getFontMetrics();


        Drawable backgroundImage;
        if (selectable) {
            if (select) {
                backgroundImage = getResources().getDrawable(R.drawable.button_popup_background_selected);
            } else {
                backgroundImage = getResources().getDrawable(R.drawable.button_popup_background_unselcted);
            }
        } else {
            backgroundImage = getResources().getDrawable(R.drawable.round_button_background);
        }


        backgroundImage.setBounds(paddingLeft, paddingTop,
                paddingLeft + contentWidth, paddingTop + contentHeight);
        backgroundImage.draw(canvas);

        // Draw the text.
        canvas.drawText(text,
                paddingLeft + (contentWidth) / 2f,
                paddingTop + (contentHeight) / 2f -
                        (textMetrics.ascent + textMetrics.descent) / 2f,
                textPaint);

        if (image != null) {
            image.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight);
            image.draw(canvas);
        }
    }

    public boolean isSelectable() {
        return selectable;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
        invalidate();
    }
}
