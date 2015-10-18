package com.example.amore.gridimagesearch.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amore.gridimagesearch.R;
import com.example.amore.gridimagesearch.models.Filter;

public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Spinner sImageSize = (Spinner) findViewById(R.id.sImageSize);
        ArrayAdapter<CharSequence> aImageSize = ArrayAdapter.createFromResource(this,
                R.array.image_sizes, android.R.layout.simple_spinner_item);
        aImageSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sImageSize.setAdapter(aImageSize);

        Spinner sImageColor = (Spinner) findViewById(R.id.sImageColor);
        ArrayAdapter<CharSequence> aImageColor = ArrayAdapter.createFromResource(this,
                R.array.colors, android.R.layout.simple_spinner_item);
        aImageColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sImageColor.setAdapter(aImageColor);

        Spinner sImageType = (Spinner) findViewById(R.id.sImageType);
        ArrayAdapter<CharSequence> aImageType = ArrayAdapter.createFromResource(this,
                R.array.image_types, android.R.layout.simple_spinner_item);
        aImageType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sImageType.setAdapter(aImageType);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSave(View v) {
        Spinner sImageSize = (Spinner) findViewById(R.id.sImageSize);
        Spinner sImageColor = (Spinner) findViewById(R.id.sImageColor);
        Spinner sImageType = (Spinner) findViewById(R.id.sImageType);
        TextView tvSiteFilter = (TextView) findViewById(R.id.tvSiteFilter);

        String siteFilter = null;
        try {
            siteFilter = tvSiteFilter.getEditableText().toString();
        }
        catch (NullPointerException e) {
        }

        Filter filter = new Filter(sImageSize.getSelectedItem().toString(),
                sImageColor.getSelectedItem().toString(),
                sImageType.getSelectedItem().toString(),
                siteFilter);

        // Prepare data intent
        Intent data = new Intent();
        // Pass relevant data back as a result
        data.putExtra("filter", filter);

        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }
}
