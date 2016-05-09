package de.fh_dortmund.vms.streetview1900.api;

import java.util.List;

import de.fh_dortmund.vms.streetview1900.api.model.Location;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ress on 29.04.2016.
 */
public interface StreetView1900Endpoint {

    @GET("locations")
    Call<List<Location>> getLocations(@Query("city") String city);

    @GET("locations/{id}")
    Call<Location> getLocation(@Path("id") int id);

}
