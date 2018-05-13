package neshev.dimityr.refprousers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import neshev.dimityr.refprousers.R;
import neshev.dimityr.refprousers.models.Match;

public class MatchAdapter extends ArrayAdapter<Match> {

    private List<Match> data;
    Context mContext;

    public MatchAdapter(List<Match> data, Context context) {
        super(context, R.layout.match_row, data);
        this.data = data;
        this.mContext=context;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Match dataModel = getItem(position);

        TextView homeTeamName;
        TextView awayTeamName;
        TextView venue;
        TextView date;
        TextView time;
        ImageView homeTeamLogo;
        ImageView awayTeamLogo;


        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.match_row, parent, false);

            homeTeamName = convertView.findViewById(R.id.home_team_name);
            awayTeamName = convertView.findViewById(R.id.away_team_name);
            homeTeamLogo = convertView.findViewById(R.id.home_team_logo);
            awayTeamLogo = convertView.findViewById(R.id.away_team_logo);
            venue = convertView.findViewById(R.id.venue);
            date = convertView.findViewById(R.id.date);
            time = convertView.findViewById(R.id.time);

            homeTeamName.setText(dataModel.getHomeTeamName());
            awayTeamName.setText(dataModel.getAwayTeamName());
            homeTeamLogo.setImageResource(dataModel.getHomeTeamLogo());
            awayTeamLogo.setImageResource(dataModel.getAwayTeamLogo());
            venue.setText("Venue: " + dataModel.getVenue());
            date.setText("Date: " + dataModel.getDate());
            time.setText("Time: " + dataModel.getTime());

            convertView.setTag(dataModel.getId());

        }

        return convertView;
    }

}
