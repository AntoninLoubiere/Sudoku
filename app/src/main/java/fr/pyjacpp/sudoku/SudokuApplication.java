package fr.pyjacpp.sudoku;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Map;

public class SudokuApplication extends Application {
    private final static String SUDOKU_PREFERENCES = "SudokuPreferences";
    private final static String SUDOKU_PREFERENCES_GRID = "SudokuGrid";
    private final static String SUDOKU_PREFERENCES_STATISTICS_GLOBAL = "SudokuStatisticsGlobal";

    private SudokuGrid currentSudokuGrid = null;
    private SudokuStatistics sudokuGlobalStatistics;

    private SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = getSharedPreferences(SUDOKU_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void importPreferences() {
        // import preferences save
        for (Map.Entry<String, ?> params: preferences.getAll().entrySet()) {
            try {
                switch (params.getKey()) {
                    case SUDOKU_PREFERENCES_GRID:
                        // import grid
                        importGrid((String) params.getValue());

                    case SUDOKU_PREFERENCES_STATISTICS_GLOBAL:
                        // import global statistics
                        importStatistics((String) params.getValue());

                    default:
                        removeSaveParams(params.getKey());
                }
            } catch (ClassCastException ignored) {
                Log.w("SudokuApplication", "There is a problem in preferences, there is " +
                        "a cast error !");
            }
        }

        // init the preferences which isn't save
        if (sudokuGlobalStatistics == null) {
            Log.i("SudokuApplication", "There aren't statistics save");
            sudokuGlobalStatistics = new SudokuStatistics();
        }
    }

    private void importGrid(String sudokuGridJson) {
        try {
            Log.i("SudokuApplication", "Import grid");
            Gson gson = new Gson();
            currentSudokuGrid = gson.fromJson(sudokuGridJson, SudokuGrid.class);
        } catch (JsonSyntaxException ignored) {
            Log.w("SudokuApplication", "Can't import the grid, check the format");
        }
    }

    private void importStatistics(String globalStatisticsJson) {
        try {
            Log.i("SudokuApplication", "Import statistics");
            Gson gson = new Gson();
            sudokuGlobalStatistics = gson.fromJson(globalStatisticsJson, SudokuStatistics.class);
        } catch (JsonSyntaxException ignored) {
            Log.w("SudokuApplication", "Can't import statistics, check the format");
        }
    }

    private void removeSaveParams(String key) {
        if (preferences.contains(key)) {
            Log.i("SudokuApplication", "Remove the statistic: " + key);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(key);
            editor.apply();
        }
    }

    public void saveGrid() {
        if (currentSudokuGrid != null) { // if the home activity is stop
            Log.i("SudokuApplication", "Save grid");

            // save the grid
            SharedPreferences.Editor prefEditor = preferences.edit();
            Gson gson = new Gson();
            prefEditor.putString(SUDOKU_PREFERENCES_GRID, gson.toJson(currentSudokuGrid));
            prefEditor.apply();
        }
    }

    public void saveStatistics() {
        if (sudokuGlobalStatistics != null) { // if the home activity is stop
            Log.i("SudokuApplication", "Save statistics");

            // save the statistics
            SharedPreferences.Editor prefEditor = preferences.edit();
            Gson gson = new Gson();
            prefEditor.putString(SUDOKU_PREFERENCES_STATISTICS_GLOBAL,
                    gson.toJson(sudokuGlobalStatistics));
            prefEditor.apply();
        }
    }

    public SudokuGrid getCurrentSudokuGrid() {
        return currentSudokuGrid;
    }

    public void setCurrentSudokuGrid(SudokuGrid currentSudokuGrid) {
        if (this.currentSudokuGrid != null) {
            final SudokuGrid finalGrid = this.currentSudokuGrid;
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    finalGrid.setStatistics(sudokuGlobalStatistics);
                    saveStatistics();
                }
            });
        }

        this.currentSudokuGrid = currentSudokuGrid;
    }

    public SudokuStatistics getSudokuGlobalStatistics() {
        return sudokuGlobalStatistics;
    }
}
