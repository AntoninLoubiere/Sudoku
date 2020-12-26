package fr.pyjacpp.sudoku;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import fr.pyjacpp.sudoku.sudoku_grid.SudokuNumberList;
import fr.pyjacpp.sudoku.sudoku_grid.SudokuNumbersEnum;

/**
 * A tile view to show a number
 */
public class TileView extends View {

    private TextPaint numberTextPaint;
    private TextPaint.FontMetrics numberTextPaintMetrics;

    private SudokuNumbersEnum number = SudokuNumbersEnum.Blank;
    private SudokuNumberList numberList = null;
    private float baseSizeFont = 25;

    private NumberTypeEnum type = NumberTypeEnum.Number;

    private boolean selected = false;

    private int backgroundColor = Color.WHITE;

    private boolean conflict = false; // conflict: 0:row, 1:column, 2:square


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

        baseSizeFont = a.getFloat(R.styleable.TileView_size_font,
                baseSizeFont);


        a.recycle();

        setBackground(getBackgroundNumber());

        numberTextPaint = new TextPaint();
        numberTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        numberTextPaint.setTextAlign(Paint.Align.CENTER);
        numberTextPaint.setTextSize(25);

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

        if (number != null) {
            setNumberTextPaintSizeFont(baseSizeFont);
            drawNumberCenter(canvas, number, contentWidth, contentHeight, paddingLeft, paddingTop);
        } else {
            switch (numberList.getList().size()) {
                case 1:
                    setNumberTextPaintSizeFont(baseSizeFont);
                    drawNumberCenter(canvas, numberList.getList().get(0), contentWidth,
                            contentHeight, paddingLeft, paddingTop);
                    break;

                case 2:
                    setNumberTextPaintSizeFont(baseSizeFont / 2f);

                    drawNumberCenter(canvas, numberList.getList().get(0),
                            contentWidth / 2f,
                            contentHeight,
                            paddingLeft,
                            paddingTop);
                    drawNumberCenter(canvas, numberList.getList().get(1),
                            contentWidth / 2f,
                            contentHeight,
                            paddingLeft + contentWidth / 2f,
                            paddingTop);
                    break;

                case 3:
                    setNumberTextPaintSizeFont(baseSizeFont / 2f);

                    drawNumberCenter(canvas, numberList.getList().get(0),
                            contentWidth,
                            contentHeight / 2f,
                            paddingLeft,
                            paddingTop);

                    drawNumberCenter(canvas, numberList.getList().get(1),
                            contentWidth / 2f,
                            contentHeight / 2f,
                            paddingLeft,
                            paddingTop + contentHeight / 2f);

                    drawNumberCenter(canvas, numberList.getList().get(2),
                            contentWidth / 2f,
                            contentHeight / 2f,
                            paddingLeft + contentWidth / 2f,
                            paddingTop + contentHeight / 2f);
                    break;

                case 4:
                    setNumberTextPaintSizeFont(baseSizeFont / 2f);

                    drawNumberCenter(canvas, numberList.getList().get(0),
                            contentWidth / 2f,
                            contentHeight / 2f,
                            paddingLeft,
                            paddingTop);

                    drawNumberCenter(canvas, numberList.getList().get(1),
                            contentWidth / 2f,
                            contentHeight / 2f,
                            paddingLeft + contentWidth / 2f,
                            paddingTop);

                    drawNumberCenter(canvas, numberList.getList().get(2),
                            contentWidth / 2f,
                            contentHeight / 2f,
                            paddingLeft,
                            paddingTop + contentHeight / 2f);

                    drawNumberCenter(canvas, numberList.getList().get(3),
                            contentWidth / 2f,
                            contentHeight / 2f,
                            paddingLeft + contentWidth / 2f,
                            paddingTop + contentHeight / 2f);
                    break;

                case 5:
                    setNumberTextPaintSizeFont(baseSizeFont / 3f);

                    drawNumberCenter(canvas, numberList.getList().get(0),
                            contentWidth / 2f,
                            contentHeight / 2f,
                            paddingLeft,
                            paddingTop);

                    drawNumberCenter(canvas, numberList.getList().get(1),
                            contentWidth / 2f,
                            contentHeight / 2f,
                            paddingLeft + contentWidth / 2f,
                            paddingTop);

                    drawNumberCenter(canvas, numberList.getList().get(2),
                            contentWidth / 3f,
                            contentHeight / 2f,
                            paddingLeft,
                            paddingTop + contentHeight / 2f);

                    drawNumberCenter(canvas, numberList.getList().get(3),
                            contentWidth / 3f,
                            contentHeight / 2f,
                            paddingLeft + contentWidth / 3f,
                            paddingTop + contentHeight / 2f);

                    drawNumberCenter(canvas, numberList.getList().get(4),
                            contentWidth / 3f,
                            contentHeight / 2f,
                            paddingLeft + contentWidth / 3f * 2,
                            paddingTop + contentHeight / 2f);
                    break;

                case 6:
                    setNumberTextPaintSizeFont(baseSizeFont / 3f);

                    drawNumberCenter(canvas, numberList.getList().get(0),
                            contentWidth / 3f,
                            contentHeight / 2f,
                            paddingLeft,
                            paddingTop);

                    drawNumberCenter(canvas, numberList.getList().get(1),
                            contentWidth / 3f,
                            contentHeight / 2f,
                            paddingLeft + contentWidth / 3f,
                            paddingTop);

                    drawNumberCenter(canvas, numberList.getList().get(2),
                            contentWidth / 3f,
                            contentHeight / 2f,
                            paddingLeft + contentWidth / 3f * 2,
                            paddingTop);

                    drawNumberCenter(canvas, numberList.getList().get(3),
                            contentWidth / 3f,
                            contentHeight / 2f,
                            paddingLeft,
                            paddingTop + contentHeight / 2f);

                    drawNumberCenter(canvas, numberList.getList().get(4),
                            contentWidth / 3f,
                            contentHeight / 2f,
                            paddingLeft + contentWidth / 3f,
                            paddingTop + contentHeight / 2f);

                    drawNumberCenter(canvas, numberList.getList().get(5),
                            contentWidth / 3f,
                            contentHeight / 2f,
                            paddingLeft + contentWidth / 3f * 2,
                            paddingTop + contentHeight / 2f);
                    break;

                case 7:
                    setNumberTextPaintSizeFont(baseSizeFont / 3f);

                    drawNumberCenter(canvas, numberList.getList().get(0),
                            contentWidth / 2f,
                            contentHeight / 3f,
                            paddingLeft,
                            paddingTop);

                    drawNumberCenter(canvas, numberList.getList().get(1),
                            contentWidth / 2f,
                            contentHeight / 3f,
                            paddingLeft + contentWidth / 2f,
                            paddingTop);

                    drawNumberCenter(canvas, numberList.getList().get(2),
                            contentWidth / 2f,
                            contentHeight / 3f,
                            paddingLeft,
                            paddingTop + contentHeight / 3f);

                    drawNumberCenter(canvas, numberList.getList().get(3),
                            contentWidth / 2f,
                            contentHeight / 3f,
                            paddingLeft + contentWidth / 2f,
                            paddingTop + contentHeight / 3f);

                    drawNumberCenter(canvas, numberList.getList().get(4),
                            contentWidth / 3f,
                            contentHeight / 3f,
                            paddingLeft,
                            paddingTop + contentHeight / 3f * 2);

                    drawNumberCenter(canvas, numberList.getList().get(5),
                            contentWidth / 3f,
                            contentHeight / 3f,
                            paddingLeft + contentWidth / 3f,
                            paddingTop + contentHeight / 3f * 2);

                    drawNumberCenter(canvas, numberList.getList().get(6),
                            contentWidth / 3f,
                            contentHeight / 3f,
                            paddingLeft + contentWidth / 3f * 2,
                            paddingTop + contentHeight / 3f * 2);
                    break;

                case 8:
                    setNumberTextPaintSizeFont(baseSizeFont / 3f);

                    drawNumberCenter(canvas, numberList.getList().get(0),
                            contentWidth / 2f,
                            contentHeight / 3f,
                            paddingLeft,
                            paddingTop);

                    drawNumberCenter(canvas, numberList.getList().get(1),
                            contentWidth / 2f,
                            contentHeight / 3f,
                            paddingLeft + contentWidth / 2f,
                            paddingTop);

                    drawNumberCenter(canvas, numberList.getList().get(2),
                            contentWidth / 3f,
                            contentHeight / 3f,
                            paddingLeft,
                            paddingTop + contentHeight / 3f);

                    drawNumberCenter(canvas, numberList.getList().get(3),
                            contentWidth / 3f,
                            contentHeight / 3f,
                            paddingLeft + contentWidth / 3f,
                            paddingTop + contentHeight / 3f);

                    drawNumberCenter(canvas, numberList.getList().get(4),
                            contentWidth / 3f,
                            contentHeight / 3f,
                            paddingLeft + contentWidth / 3f * 2,
                            paddingTop + contentHeight / 3f);

                    drawNumberCenter(canvas, numberList.getList().get(5),
                            contentWidth / 3f,
                            contentHeight / 3f,
                            paddingLeft,
                            paddingTop + contentHeight / 3f * 2);

                    drawNumberCenter(canvas, numberList.getList().get(6),
                            contentWidth / 3f,
                            contentHeight / 3f,
                            paddingLeft + contentWidth / 3f,
                            paddingTop + contentHeight / 3f * 2);

                    drawNumberCenter(canvas, numberList.getList().get(7),
                            contentWidth / 3f,
                            contentHeight / 3f,
                            paddingLeft + contentWidth / 3f * 2,
                            paddingTop + contentHeight / 3f * 2);
                    break;

                case 9:
                    setNumberTextPaintSizeFont(baseSizeFont / 3f);

                    drawNumberCenter(canvas, numberList.getList().get(0),
                            contentWidth / 3f,
                            contentHeight / 3f,
                            paddingLeft,
                            paddingTop);

                    drawNumberCenter(canvas, numberList.getList().get(1),
                            contentWidth / 3f,
                            contentHeight / 3f,
                            paddingLeft + contentWidth / 3f,
                            paddingTop);

                    drawNumberCenter(canvas, numberList.getList().get(2),
                            contentWidth / 3f,
                            contentHeight / 3f,
                            paddingLeft + contentWidth / 3f * 2,
                            paddingTop);

                    drawNumberCenter(canvas, numberList.getList().get(3),
                            contentWidth / 3f,
                            contentHeight / 3f,
                            paddingLeft,
                            paddingTop + contentHeight / 3f);

                    drawNumberCenter(canvas, numberList.getList().get(4),
                            contentWidth / 3f,
                            contentHeight / 3f,
                            paddingLeft + contentWidth / 3f,
                            paddingTop + contentHeight / 3f);

                    drawNumberCenter(canvas, numberList.getList().get(5),
                            contentWidth / 3f,
                            contentHeight / 3f,
                            paddingLeft + contentWidth / 3f * 2,
                            paddingTop + contentHeight / 3f);

                    drawNumberCenter(canvas, numberList.getList().get(6),
                            contentWidth / 3f,
                            contentHeight / 3f,
                            paddingLeft,
                            paddingTop + contentHeight / 3f * 2);

                    drawNumberCenter(canvas, numberList.getList().get(7),
                            contentWidth / 3f,
                            contentHeight / 3f,
                            paddingLeft + contentWidth / 3f,
                            paddingTop + contentHeight / 3f * 2);

                    drawNumberCenter(canvas, numberList.getList().get(8),
                            contentWidth / 3f,
                            contentHeight / 3f,
                            paddingLeft + contentWidth / 3f * 2,
                            paddingTop + contentHeight / 3f * 2);
                    break;

                default:
                    break;
            }
        }

        // Draw the text.

    }

    private void drawNumberCenter(Canvas canvas, SudokuNumbersEnum number, float width, float height,
                                  float xOffset, float yOffset) {

        canvas.drawText(number.getTextNumber(),
                xOffset + width / 2f,
                yOffset + height / 2f -
                        (numberTextPaintMetrics.ascent + numberTextPaintMetrics.descent) / 2f,
                numberTextPaint);
    }

    private void setNumberTextPaintSizeFont(float sizeFont) {
        numberTextPaint.setTextSize(sizeFont);
        invalidateTextPaintAndMeasurements();
    }

    public void setNumber(SudokuNumbersEnum number) {
        this.number = number;
        this.numberList = null;
        invalidate();
    }

    public void setNumber(SudokuNumberList number) {
        this.number = null;
        this.numberList = number;
        invalidate();
    }

    public void setBaseSizeFont(float baseSizeFont) {
        this.baseSizeFont = baseSizeFont;
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

    public void setConflict(boolean value) {
        if (conflict != value) {
            conflict = value;

            if (isInConflict())
                numberTextPaint.setColor(getResources().getColor(this.type.isModifiable() ?
                        R.color.user_conflict_color : R.color.conflict_color));
            else
                numberTextPaint.setColor(getCurrentColor());
            invalidate();
        }
    }

    private int getCurrentColor() {
        switch (type) {
            case Number:
                return getResources().getColor(R.color.number);

            case UserInput:
                return getResources().getColor(R.color.user_number_color);

            case Hint:
                return getResources().getColor(R.color.text_hint);

            case Win:
                return getResources().getColor(R.color.text_win);

            case Lose:
                return getResources().getColor(R.color.text_lose);

            default:
                return Color.BLACK;
        }
    }

    public boolean isInConflict() {
        return conflict;
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

    public void setType(NumberTypeEnum type) {
        this.type = type;
        numberTextPaint.setColor(getCurrentColor());
        invalidate();
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        if (this.backgroundColor != backgroundColor) {
            this.backgroundColor = backgroundColor;
            setBackground(getBackgroundNumber());
            invalidate();
        }
    }
}
