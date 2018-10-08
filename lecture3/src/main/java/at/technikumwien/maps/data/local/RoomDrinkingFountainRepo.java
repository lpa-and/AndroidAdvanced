package at.technikumwien.maps.data.local;

import java.util.List;

import at.technikumwien.maps.AppDependencyManager;
import at.technikumwien.maps.data.OnDataLoadedCallback;
import at.technikumwien.maps.data.OnOperationSuccessfulCallback;
import at.technikumwien.maps.util.LoadDataAsyncTask;
import at.technikumwien.maps.util.OperationAsyncTask;

public class RoomDrinkingFountainRepo {

    private final DrinkingFountainDao drinkingFountainDao;

    public RoomDrinkingFountainRepo(AppDependencyManager manager) {
        this.drinkingFountainDao = manager.getAppDatabase().drinkingFountainDao();
    }

    public void getAllDrinkingFountains(OnDataLoadedCallback<List<DrinkingFountain>> callback) {

        new LoadDataAsyncTask<List<DrinkingFountain>>(callback) {

            @Override
            public List<DrinkingFountain> loadData() throws Throwable {
                List<DrinkingFountain> drinkingFountainList = drinkingFountainDao.getAllDrinkingFountains();
                return drinkingFountainList;
            }

        }.execute();

    }

    public void insertDrinkingFountains(OnOperationSuccessfulCallback callback, final DrinkingFountain... drinkingFountains) {

        new OperationAsyncTask(callback) {

            @Override
            public void doOperation() throws Throwable {
                drinkingFountainDao.insertAll(drinkingFountains);
            }

        }.execute();

    }

    public void deleteAllDrinkingFountains(OnOperationSuccessfulCallback callback) {
        new OperationAsyncTask(callback) {

            @Override
            public void doOperation() throws Throwable {
                drinkingFountainDao.deleteAll();
            }

        }.execute();
    }
}
