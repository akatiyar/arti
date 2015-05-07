package akatiyar.arti;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.ParseFile;

import akatiyar.arti.model.ArtiContent;
import akatiyar.arti.model.Device;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class HomeFragment extends Fragment implements AbsListView.OnItemClickListener {

    private static final String TAG = HomeFragment.class.getName();

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    private static final HomeFragment homeFragment =  new HomeFragment();

    public static synchronized HomeFragment newInstance() {
        return homeFragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "In Home Fragment Create");
        super.onCreate(savedInstanceState);

        mAdapter = new DeviceAdapter(getActivity(), R.layout.device_summary, ArtiContent.DEVICES);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "In Home Fragment Create View");

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        mListener.onFragmentAttached(ArtiContent.HOME);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void notifyDataSetChanged(){
        ((ArrayAdapter<Device>)mAdapter).notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ParseFile photoFile = ArtiContent.DEVICES.get(position).getPhotoFile();
        if(photoFile!=null) {
            Log.d(TAG, "Load image " + photoFile.getUrl());
            // Open photo in full screen.
            Activity activity = getActivity();
            AlertDialog.Builder imageDialog = new AlertDialog.Builder(activity);

            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.device_full, null);
            Rect displayRectangle = new Rect();
            Window window = activity.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            layout.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
            layout.setMinimumHeight((int) (displayRectangle.height() * 0.9f));

            ImageView imageView = (ImageView) layout.findViewById(R.id.fullimage);
            imageDialog.setView(layout);
            imageDialog.setNeutralButton(activity.getResources().getString(R.string.ok_button), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = imageDialog.create();
            ImageLoader.getInstance().displayImage(photoFile.getUrl(), imageView);
            alertDialog.show();
        }
    }

}
