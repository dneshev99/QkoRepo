package neshev.dimityr.refprousers.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import neshev.dimityr.refprousers.R;
import neshev.dimityr.refprousers.adapters.MatchAdapter;
import neshev.dimityr.refprousers.models.Match;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private List<Match> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        data = new ArrayList<>();

        data.add(new Match("1",R.drawable.juventus,R.drawable.cska_sofia,"JVN","CSKA","Santiago Bernabéu Stadium","15.06.2018","15:30"));
        data.add(new Match("2",R.drawable.roma,R.drawable.cska_sofia,"ROMA","CSKA","Stadio della Roma","30.08.2018","18:30"));
        data.add(new Match("3",R.drawable.juventus,R.drawable.roma,"JVN","ROMA","Allianz Stadium","20.10.2018","20:30"));


        MatchAdapter adapter = new MatchAdapter(data,this);

        listView = findViewById(R.id.matchListView);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = String.valueOf(view.getTag());

                Toast.makeText(getApplicationContext(),id,Toast.LENGTH_SHORT).show();
            }
        });

    }

}
