package fr.pyjacpp.sudoku;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Map;

import fr.pyjacpp.sudoku.statistics.SudokuStatistics;
import fr.pyjacpp.sudoku.sudoku_grid.SudokuGrid;

public class SudokuApplication extends Application {
    private final static String SUDOKU_PREFERENCES = "SudokuPreferences";
    private final static String SUDOKU_PREFERENCES_GRID = "SudokuGrid";
    private final static String SUDOKU_PREFERENCES_STATISTICS_GLOBAL = "SudokuStatisticsGlobal";
    private final static String SUDOKU_PREFERENCES_VERSION_ID = "SudokuVersionId";
    private final static String SUDOKU_PREFERENCES_LAST_DIFFICULTY = "SudokuLastDifficulty";
    private final static String SUDOKU_PREFERENCES_LAST_SORT_USED  = "SudokuLastSortUsed";
    private final static String SUDOKU_PREFERENCES_LAST_CONFLICT  = "SudokuLastConflict";

    public final static int TIMER_SHOW = 0;
    public final static int TIMER_SHOW_END = 1;
    public final static int TIMER_HIDE = 2;

    private final static int CURRENT_VERSION_ID = 3;

    private SudokuGrid currentSudokuGrid = null;
    private SudokuStatistics sudokuGlobalStatistics = null;
    private int difficulty = 1;
    private boolean lastSortUsed = false; // this boolean if is need to sort
    private boolean lastConflict = true;
    private boolean preferencesLoaded = false;

    private SharedPreferences preferences;

    private Runnable versionEndRunnable = null;
    private int timer = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new Runnable() {
            @Override
            public void run() {
                preferences = getSharedPreferences(SUDOKU_PREFERENCES, Context.MODE_PRIVATE);
                importPreferences();
            }
        }).start();
    }

    public void importPreferences() {
        // import preferences save
        boolean version_id_load = false;

        for (Map.Entry<String, ?> params : preferences.getAll().entrySet()) {
            try {
                switch (params.getKey()) {
                    case SUDOKU_PREFERENCES_GRID:
                        // import grid
                        importGrid((String) params.getValue());
                        break;

                    case SUDOKU_PREFERENCES_STATISTICS_GLOBAL:
                        // import global statistics
                        importStatistics((String) params.getValue());
                        break;

                    case SUDOKU_PREFERENCES_VERSION_ID:
                        version_id_load = true;
                        importVersionId((Integer) params.getValue());
                        break;

                    case SUDOKU_PREFERENCES_LAST_DIFFICULTY:
                        difficulty = (Integer) params.getValue();
                        break;

                    case SUDOKU_PREFERENCES_LAST_SORT_USED:
                        lastSortUsed = (Boolean) params.getValue();
                        break;

                    case SUDOKU_PREFERENCES_LAST_CONFLICT:
                        lastConflict = (Boolean) params.getValue();
                        break;

                    default:
                        removeSaveParams(params.getKey());
                        break;
                }
            } catch (ClassCastException ignored) {
                Log.w("SudokuApplication", "There is a problem in preferences, there is " +
                        "a cast error !");
            }
        }

        // init the preferences which isn't save
        if (sudokuGlobalStatistics == null) {
            // Log.i("SudokuApplication", "There aren't statistics save"); // LOG_DISABLED
            sudokuGlobalStatistics = new SudokuStatistics();
        }
        if (!version_id_load) {
            // Log.i("SudokuApplication", "There aren't version save"); // LOG_DISABLED
            importVersionId(0);
        }

        if (versionEndRunnable != null) {
            versionEndRunnable.run();
            versionEndRunnable = null;
        }
        preferencesLoaded = true;
    }

    private void importVersionId(Integer version) {
        if (version < 3) {
            versionEndRunnable = new Runnable() {
                @Override
                public void run() {
                    currentSudokuGrid = null;
                    saveGrid();
                }
            };
        }
        if (version != CURRENT_VERSION_ID) {
            SharedPreferences.Editor prefEditor = preferences.edit();
            prefEditor.putInt(SUDOKU_PREFERENCES_VERSION_ID, CURRENT_VERSION_ID);
            prefEditor.apply();
        }
    }

    private void importGrid(String sudokuGridJson) {
        try {
            // Log.i("SudokuApplication", "Import grid"); // LOG_DISABLED

            Gson gson = new Gson();

            currentSudokuGrid = gson.fromJson(sudokuGridJson, SudokuGrid.class);
        } catch (Exception e) {
            Log.w("SudokuApplication", "Can't import the grid, check the format");
            e.printStackTrace();
        }
    }

    private void importStatistics(String globalStatisticsJson) {
        try {
            // Log.i("SudokuApplication", "Import statistics"); // LOG_DISABLED

            Gson gson = new Gson();

            sudokuGlobalStatistics = gson.fromJson(globalStatisticsJson, SudokuStatistics.class);
        } catch (JsonSyntaxException ignored) {
            Log.w("SudokuApplication", "Can't import statistics, check the format");
        }
    }

    private void removeSaveParams(String key) {
        if (preferences.contains(key)) {
            // Log.i("SudokuApplication", "Remove the statistic: " + key); // LOG_DISABLED
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(key);
            editor.apply();
        }
    }

    public void saveGrid() {
        if (currentSudokuGrid != null) { // if the home activity is stop
            // Log.i("SudokuApplication", "Save grid"); // LOG_DISABLED

            // save the grid
            SharedPreferences.Editor prefEditor = preferences.edit();

            Gson gson = new Gson();

            prefEditor.putString(SUDOKU_PREFERENCES_GRID, gson.toJson(currentSudokuGrid));
            prefEditor.apply();
        }
    }

    public void saveStatistics() {
        if (sudokuGlobalStatistics != null) { // if the home activity is stop
            // Log.i("SudokuApplication", "Save statistics"); // LOG_DISABLED

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
            new Handler(getMainLooper()).post(new Runnable() {
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

    public boolean preferencesIsLoad() {
        return preferencesLoaded;
    }

    public int getLastDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public boolean getLastSortUsed() {
        return lastSortUsed;
    }

    public void setLastSortUsed(boolean lastSortUsed) {
        this.lastSortUsed = lastSortUsed;
    }

    public boolean getLastConflict() {
        return lastConflict;
    }

    public void setLastConflict(boolean lastConflict) {
        this.lastConflict = lastConflict;
    }

    public int getLastTimer() {
        return timer;
    }

    public void setLastTimer(int timer) {
        this.timer = timer;
    }

    public void saveLastOptions() {
        SharedPreferences.Editor prefEditor = preferences.edit();
        prefEditor.putInt(SUDOKU_PREFERENCES_LAST_DIFFICULTY,
                difficulty);
        prefEditor.putBoolean(SUDOKU_PREFERENCES_LAST_CONFLICT,
                lastConflict);
        prefEditor.putBoolean(SUDOKU_PREFERENCES_LAST_SORT_USED,
                lastSortUsed);
        prefEditor.apply();
    }
}
