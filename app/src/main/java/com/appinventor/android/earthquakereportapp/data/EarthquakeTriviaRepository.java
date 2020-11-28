package com.appinventor.android.earthquakereportapp.data;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.appinventor.android.earthquakereportapp.pojo.EarthquakeTrivia;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

public class EarthquakeTriviaRepository {

    private EarthquakeTriviaDatabase database;
    private LiveData<List<EarthquakeTrivia>> allTrivia;

    public EarthquakeTriviaRepository(Context context) {
        database = EarthquakeTriviaDatabase.getInstance(context);
        allTrivia = database.earthquakeTriviaDAO().getAllTrivia();
    }

    public Completable insertTrivia() {
        return Completable.fromAction(() -> {
                database.earthquakeTriviaDAO().insertTrivia(new EarthquakeTrivia("Earthquake",
                        "(also known as a quake, tremor or temblor) is the shaking of the surface of the Earth resulting " +
                                "from a sudden release of energy in the Earth's lithosphere that creates seismic waves."));
                database.earthquakeTriviaDAO().insertTrivia(new EarthquakeTrivia("Seismicity",
                        "or seismic activity, of an area is the frequency, type, and size of earthquakes experienced over " +
                                "a period of time."));
                database.earthquakeTriviaDAO().insertTrivia(new EarthquakeTrivia("Earthquake fault types",
                        "There are three main types of fault, all of which may cause an interplate earthquake: normal, reverse " +
                                "(thrust), and strike-slip.\n\nNormal and reverse faulting are examples of dip-slip, where the displacement along " +
                                "the fault is in the direction of dip and where movement on them involves a vertical component. Normal faults occur " +
                                "mainly in areas where the crust is being extended such as a divergent boundary.\n\nReverse faults occur in areas " +
                                "where the crust is being shortened such as at a convergent boundary.\n\nStrike-slip faults are " +
                                "steep structures where the two sides of the fault slip horizontally past each other; transform " +
                                "boundaries are a particular type of strike-slip fault.\n\nMany earthquakes are caused by movement on faults " +
                                "that have components of both dip-slip and strike-slip; this is known as oblique slip."));
                database.earthquakeTriviaDAO().insertTrivia(new EarthquakeTrivia("Shallow-focus and deep-focus earthquakes",
                        "The majority of tectonic earthquakes originate at the ring of fire in depths not exceeding tens of " +
                                "kilometers. Earthquakes occurring at a depth of less than 70 km (43 mi) are classified as " +
                                "\"shallow-focus\" earthquakes, while those with a focal-depth between 70 and 300 km (43 and 186 mi)" +
                                "are commonly termed \"mid-focus\" or \"intermediate-depth\" earthquakes."));
                database.earthquakeTriviaDAO().insertTrivia(new EarthquakeTrivia("Volcano tectonic earthquake",
                        "is an earthquake caused by the movement of magma beneath the surface of the Earth. " +
                                "The movement results in pressure changes where the rock around the magma has experienced stress. " +
                                "At some point, this stress can cause the rock to break or move. This seismic activity is used by " +
                                "scientists to monitor volcanoes."));
                database.earthquakeTriviaDAO().insertTrivia(new EarthquakeTrivia("Aftershock",
                        "is an earthquake that occurs after a previous earthquake, the mainshock. " +
                                "An aftershock is in the same region of the main shock but always of a smaller magnitude. " +
                                "If an aftershock is larger than the main shock, the aftershock is redesignated as the main shock " +
                                "and the original main shock is redesignated as a foreshock. Aftershocks are formed as the crust " +
                                "around the displaced fault plane adjusts to the effects of the main shock"));
                database.earthquakeTriviaDAO().insertTrivia(new EarthquakeTrivia("Earthquake swarms",
                        "are sequences of earthquakes striking in a specific area within a short period of time. " +
                                "They are different from earthquakes followed by a series of aftershocks by the fact that no single " +
                                "earthquake in the sequence is obviously the main shock, so none has a notable higher magnitude " +
                                "than another."));
                database.earthquakeTriviaDAO().insertTrivia(new EarthquakeTrivia("Seismic magnitude scales",
                        "are used to describe the overall strength or \"size\" of an earthquake. These are distinguished " +
                                "from seismic intensity scales that categorize the intensity or severity of ground shaking " +
                                "(quaking) caused by an earthquake at a given location.\n\nMagnitudes are usually determined from " +
                                "measurements of an earthquake's seismic waves as recorded on a seismogram. Magnitude scales vary " +
                                "on what aspect of the seismic waves are measured and how they are measured. Different magnitude " +
                                "scales are necessary because of differences in earthquakes, the information available, and the " +
                                "purposes for which the magnitudes are used."));
                database.earthquakeTriviaDAO().insertTrivia(new EarthquakeTrivia("Frequency of occurence",
                        "It is estimated that around 500,000 earthquakes occur each year, detectable with current " +
                                "instrumentation. About 100,000 of these can be felt.\n\nMinor earthquakes occur nearly " +
                                "constantly around the world in places like California and Alaska in the U.S., as well as in " +
                                "El Salvador, Mexico, Guatemala, Chile, Peru, Indonesia, Philippines, Iran, Pakistan, the Azores in " +
                                "Portugal, Turkey, New Zealand, Greece, Italy, India, Nepal and Japan.\n\nLarger earthquakes occur less frequently, " +
                                "the relationship being exponential; for example, roughly ten times as many earthquakes larger than magnitude 4 occur " +
                                "in a particular time period than earthquakes larger than magnitude 5."));
                database.earthquakeTriviaDAO().insertTrivia(new EarthquakeTrivia("Effects of earthquakes",
                        "The effects of earthquakes include, but are not limited to, the following: \n" +
                                "Shaking and ground rupture\nSoil liquefaction\nHuman impacts, Landslides\nFires\nTsunami\nFloods"));
                database.earthquakeTriviaDAO().insertTrivia(new EarthquakeTrivia("Major Earthquakes",
                        "One of the most devastating earthquakes in recorded history was the 1556 Shaanxi earthquake, " +
                                "which occurred on 23 January 1556 in Shaanxi province, China. More than 830,000 people died. " +
                                "Most houses in the area were yaodongs—dwellings carved out of loess hillsides—and many victims " +
                                "were killed when these structures collapsed.\n\nThe 1976 Tangshan earthquake, which killed between " +
                                "240,000 and 655,000 people, was the deadliest of the 20th century.\n\nThe 1960 Chilean earthquake is " +
                                "the largest earthquake that has been measured on a seismograph, reaching 9.5 magnitude on 22 May " +
                                "1960. Its epicenter was near Cañete, Chile. The energy released was approximately twice " +
                                "that of the next most powerful earthquake, the Good Friday earthquake (27 March 1964), which was " +
                                "centered in Prince William Sound, Alaska.\n\nThe ten largest recorded earthquakes have all " +
                                "been megathrust earthquakes; however, of these ten, only the 2004 Indian Ocean earthquake is " +
                                "simultaneously one of the deadliest earthquakes in history."));
                database.earthquakeTriviaDAO().insertTrivia(new EarthquakeTrivia("Prediction",
                        "is a branch of the science of seismology concerned with the specification of the time, location, " +
                                "and magnitude of future earthquakes within stated limits."));
                database.earthquakeTriviaDAO().insertTrivia(new EarthquakeTrivia("Forecasting",
                        "is often differentiated from earthquake prediction. Earthquake forecasting is concerned with the " +
                                "probabilistic assessment of general earthquake hazard, including the frequency and magnitude of " +
                                "damaging earthquakes in a given area over years or decades."));
        })
                .subscribeOn(Schedulers.io());
    }

    public LiveData<List<EarthquakeTrivia>> getAllTrivia() {
        return allTrivia;
    }

}
