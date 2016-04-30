package de.fh_dortmund.vms.streetview1900.api;

import de.fh_dortmund.vms.streetview1900.BuildConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ress on 29.04.2016.
 */
public class StreetView1900Service {

    private static final String BASE_URL = BuildConfig.REST_SERVICE_URL;

    private static StreetView1900Service instance = new StreetView1900Service();
    private StreetView1900Endpoint mStreetViewEndpoint;

    private StreetView1900Service() {
    }

    public static StreetView1900Service getInstance() {
        return instance;
    }

    public StreetView1900Endpoint getEndpoint() {
        if(mStreetViewEndpoint == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            mStreetViewEndpoint = retrofit.create(StreetView1900Endpoint.class);
        }

        return mStreetViewEndpoint;
    }
}
