package com.example.secondchance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.secondchance.Model.Model;
import com.example.secondchance.Model.Post;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;
import java.util.List;

public class MapsFragment extends Fragment {


    LinkedList<Double> latPoints = new LinkedList<>();
    LinkedList<Double> longPoints = new LinkedList<>();
    LinkedList<String> postIDList = new LinkedList<>();
    PostListViewModel postListViewModel;

    private View view;

    SupportMapFragment supportMapFragment;
    GoogleMap googleMap;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        postListViewModel=new ViewModelProvider(this).get(PostListViewModel.class);
        view = inflater.inflate(R.layout.fragment_maps, container, false);
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.general_map);
        setHasOptionsMenu(true);

        Model.instance.getLatLongPoint(new Model.LatLongListener() {
            @Override
            public void onComplete(List<Double> latitudePoint, List<Double> longitudePoints, List<String> postID) {
                latPoints.addAll(latitudePoint);
                longPoints.addAll(longitudePoints);
                postIDList.addAll(postID);
                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(31.4117257,35.0818155));
                        CameraUpdate zoom= CameraUpdateFactory.zoomTo(7);

                        googleMap.moveCamera(center);
                        googleMap.animateCamera(zoom);

                        for(int i = 0; i < longPoints.size(); i++) {
                            LatLng location = new LatLng(latPoints.get(i),longPoints.get(i));
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(location);
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                            googleMap.addMarker(markerOptions);
                            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    double lat = marker.getPosition().latitude;
                                    double longitude = marker.getPosition().longitude;
                                    Log.d("map","lat"+lat +" long:"+longitude);

                                    for(int i = 0; i < postIDList.size(); i++) {
                                        if(latPoints.get(i) == lat && longPoints.get(i) == longitude) {
                                            Log.d("map","pp"+postIDList.get(i));
                                            Model.instance.getPost(postIDList.get(i), new Model.GetPostListener() {
                                                @Override
                                                public void onComplete(Post post) {
                                                    MapsFragmentDirections.ActionMapsFragmentToSinglePostFragment actionToPost=
                                                            MapsFragmentDirections.actionMapsFragmentToSinglePostFragment(post.getPostID());
                                                    Navigation.findNavController(view).navigate(actionToPost);

                                                }
                                            });
                                            break;

                                        }
                                    }

                                    return true;
                                }
                            });
                        }
                    }
                });
            }
        });

        return view;
    }
}

