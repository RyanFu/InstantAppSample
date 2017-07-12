package com.jbvincey.instantappssample.presenters;

import com.jbvincey.instantappssample.models.Coordinates;
import com.jbvincey.instantappssample.models.Trip;
import com.jbvincey.instantappssample.repositories.TripRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jean-baptistevincey on 13/06/2017.
 */

public class DetailsPresenter extends AbstractPresenter<DetailsPresenter.View> {

    private static final String TAG = "DetailsPresenter";

    private TripRepository tripRepository;

    private Trip trip;

    public DetailsPresenter(TripRepository tripRepository) {
        super();
        this.tripRepository = tripRepository;
    }

    public void init(String tripId) {
        tripRepository.getTrip(tripId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(
                        new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                call(disposable);
                            }
                        }
                ).subscribe(
                new Consumer<Trip>() {
                    @Override
                    public void accept(Trip trip) throws Exception {
                        DetailsPresenter.this.trip = trip;
                        view.setupTripView(trip);
                    }
                },
                new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.showTripLoadingError();
                    }
                }
        );
    }

    public void onBookClicked() {
        if(trip != null) {
            view.confirmBooking(trip.getName());
        }
    }

    public void onActionContactClicked() {
        if(trip != null) {
            view.displayContactIntent(trip.getContact());
        }
    }

    public void onActionLocationClicked() {
        if (trip != null) {
            view.displayLocationIntent(trip.getCoordinates());
        }
    }

    public void onActionShareClicked() {
        if (trip != null) {
            view.displayShareIntent(trip.getId());
        }
    }

    public interface View {

        void setupTripView(Trip trip);

        void showTripLoadingError();

        void displayLocationIntent(Coordinates coordinates);

        void displayContactIntent(String contact);

        void displayShareIntent(String tripId);

        void confirmBooking(String name);

    }
}
