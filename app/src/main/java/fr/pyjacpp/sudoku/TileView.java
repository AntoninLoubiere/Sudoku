package fr.pyjacpp.sudoku;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * A tile view to show a number
 */
public class TileView extends View {

    private TextPaint numberTextPaint;
    private TextPaint.FontMetrics numberTextPaintMetrics;

    private SudokuNumbersEnum number = SudokuNumbersEnum.Blank;
    private int numberFontColor = Color.BLACK;
    private float sizeFont = 25;
    private boolean userModifiable = false;

    private boolean selected = false;

    private int backgroundColor = Color.WHITE;

    private boolean[] inConflict = new boolean[3]; // conflict: 0:row, 1:column, 2:square


    public TileView(Context context) {
        super(context);
        init(null, 0);
    }

    public TileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TileView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TileView, defStyle, 0);


        int number = a.getInteger(R.styleable.TileView_number_tile,
                0);

        this.number = SudokuNumbersEnum.get(number);

        sizeFont = a.getFloat(R.styleable.TileView_size_font,
                sizeFont);

        numberFontColor = a.getColor(R.styleable.TileView_number_font_color,
                numberFontColor);

        userModifiable = a.getBoolean(R.styleable.TileView_user_modifiable,
                userModifiable);


        a.recycle();

        setBackground(getBackgroundNumber());

        numberTextPaint = new TextPaint();
        numberTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        numberTextPaint.setTextAlign(Paint.Align.CENTER);
        numberTextPaint.setTextSize(25);
        numberTextPaint.setColor(numberFontColor);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();

        //createListeners();
        //createPopup();
    }


    public void invalidateTextPaintAndMeasurements() {
        numberTextPaintMetrics = numberTextPaint.getFontMetrics();
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

        // Draw the text.
        canvas.drawText(number.getTextNumber(),
                paddingLeft + (contentWidth) / 2f,
                paddingTop + (contentHeight) / 2f -
                        (numberTextPaintMetrics.ascent + numberTextPaintMetrics.descent) / 2f,
                numberTextPaint);
    }

    public void setNumber(SudokuNumbersEnum number) {
        this.number = number;
        invalidateTextPaintAndMeasurements();
        invalidate();
    }

    public void setNumberFontColor(int numberFontColor) {
        this.numberFontColor = numberFontColor;
        numberTextPaint.setColor(numberFontColor);
        invalidateTextPaintAndMeasurements();
        invalidate();
    }

    public void setSizeFont(float sizeFont) {
        this.sizeFont = sizeFont;
        numberTextPaint.setTextSize(sizeFont);
        invalidateTextPaintAndMeasurements();
        invalidate();
    }

    @Override
    public void setSelected(boolean selected) {
        if (this.selected != selected) {
            this.selected = selected;
            setBackground(getBackgroundNumber());
        }
    }

    public void setConflict(int type, boolean value) {
        boolean previousConflict = isInConflict();
        inConflict[type] = value;

        if (previousConflict != isInConflict()) {
            if (isInConflict())
                numberTextPaint.setColor(getResources().getColor(userModifiable ?
                        R.color.user_conflict_color : R.color.conflict_color));
            else
                numberTextPaint.setColor(numberFontColor);
            invalidate();
        }
    }

    public boolean isInConflict() {
        return inConflict[0] || inConflict[1] || inConflict[2];
    }

    public boolean getInConflict(int type) {
        if (0 <= type && type < 3)
            return inConflict[type];
        else
            Log.e("TileView", "Invalid type 0, 1 or 3; no " + type);
        return false;
    }

    public void setUserModifiable(boolean userModifiable) {
        this.userModifiable = userModifiable;
    }

    private Drawable getBackgroundNumber() {
        if (selected) {
            return getResources().getDrawable(R.drawable.selected_tile_view_background);
        } else {
            GradientDrawable background = (GradientDrawable) getResources().getDrawable(
                    R.drawable.tile_view_background);
            background.setColor(backgroundColor);
            return background;
        }
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        if( this.backgroundColor != backgroundColor) {
            this.backgroundColor = backgroundColor;
            setBackground(getBackgroundNumber());
            invalidate();
        }
    }
}
