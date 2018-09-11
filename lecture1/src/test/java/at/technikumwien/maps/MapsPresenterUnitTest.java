package at.technikumwien.maps;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import at.technikumwien.maps.data.OnDataLoadedCallback;
import at.technikumwien.maps.data.model.DrinkingFountain;
import at.technikumwien.maps.data.remote.DrinkingFountainRepo;
import at.technikumwien.maps.ui.maps.MapsPresenter;
import at.technikumwien.maps.ui.maps.MapsView;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class MapsPresenterUnitTest {

    // @Mock tells Mockito to create a mock object of this class
    @Mock DrinkingFountainRepo drinkingFountainRepo;
    @Mock MapsView mapsView;

    private MapsPresenter presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        presenter = new MapsPresenter(drinkingFountainRepo);
        // attachView() from Mosby
        presenter.attachView(mapsView);
    }

    @After
    public void teardown() {

    }

    @Test
    public void onDataLoadedTest() {
        final List<DrinkingFountain> drinkingFountainList = new ArrayList<>();

        // mock (fake) behavior exactly as need for this test case

        doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                // get the first parameter of the specific call of the loadDrinkingFountains() method on the repo
                OnDataLoadedCallback<List<DrinkingFountain>> callback = invocation.getArgument(0);
                callback.onDataLoaded(drinkingFountainList);
                return null;
            }

        }).when(drinkingFountainRepo).loadDrinkingFountains(any(OnDataLoadedCallback.class));

        // call method under test

        presenter.loadData();

        // verify behavior of method under test

        verify(mapsView).onDataLoaded(drinkingFountainList);
        verify(mapsView, never()).onDataLoadError(any(Exception.class));

    }

    @Test
    public void onDataLoadErrorTest() {
        final Exception exception = new Exception();

        // mock (fake) behavior exactly as need for this test case

        doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                // get the first parameter of the specific call of the loadDrinkingFountains() method on the repo
                OnDataLoadedCallback<List<DrinkingFountain>> callback = invocation.getArgument(0);
                callback.onDataLoadError(exception);
                return null;
            }

        }).when(drinkingFountainRepo).loadDrinkingFountains(any(OnDataLoadedCallback.class));

        // call method under test

        presenter.loadData();

        // verify behavior of method under test

        verify(mapsView).onDataLoadError(exception);
        verify(mapsView, never()).onDataLoaded(ArgumentMatchers.<DrinkingFountain>anyList());
    }
}
