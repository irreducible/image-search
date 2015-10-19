package com.example.amore.gridimagesearch.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amore.gridimagesearch.R;
import com.example.amore.gridimagesearch.activities.SearchActivity;
import com.example.amore.gridimagesearch.models.Filter;

public class FilterDialog extends DialogFragment {
    private Spinner sImageSize;
    private Spinner sImageColor;
    private Spinner sImageType;

    public FilterDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static FilterDialog newInstance() {
        FilterDialog frag = new FilterDialog();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_dialog, container);

        sImageSize = (Spinner) view.findViewById(R.id.sImageSize);
        ArrayAdapter<CharSequence> aImageSize = ArrayAdapter.createFromResource(getActivity(),
                R.array.image_sizes, android.R.layout.simple_spinner_item);
        aImageSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sImageSize.setAdapter(aImageSize);

        sImageColor = (Spinner) view.findViewById(R.id.sImageColor);
        ArrayAdapter<CharSequence> aImageColor = ArrayAdapter.createFromResource(getActivity(),
                R.array.colors, android.R.layout.simple_spinner_item);
        aImageColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sImageColor.setAdapter(aImageColor);

        sImageType = (Spinner) view.findViewById(R.id.sImageType);
        ArrayAdapter<CharSequence> aImageType = ArrayAdapter.createFromResource(getActivity(),
                R.array.image_types, android.R.layout.simple_spinner_item);
        aImageType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sImageType.setAdapter(aImageType);

        Button btnSave = (Button) view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvSiteFilter = (TextView) v.findViewById(R.id.tvSiteFilter);

                String siteFilter = null;
                try {
                    siteFilter = tvSiteFilter.getEditableText().toString();
                }
                catch (NullPointerException e) {
                }

                SearchActivity.filter = new Filter(sImageSize.getSelectedItem().toString(),
                        sImageColor.getSelectedItem().toString(),
                        sImageType.getSelectedItem().toString(),
                        siteFilter);
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}