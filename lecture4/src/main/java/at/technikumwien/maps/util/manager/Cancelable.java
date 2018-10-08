package at.technikumwien.maps.util.manager;

public interface Cancelable {

    void cancel();
    boolean isCanceled();
}
