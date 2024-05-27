package com.example.tahminci1.ui.home;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tahminci1.ApiService;
import com.example.tahminci1.R;
import com.example.tahminci1.StandingsResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    private LinearLayout standingsContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        standingsContainer = view.findViewById(R.id.standingsContainer);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://v3.football.api-sports.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<StandingsResponse> call = apiService.getStandings();
        call.enqueue(new Callback<StandingsResponse>() {
            @Override
            public void onResponse(Call<StandingsResponse> call, Response<StandingsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<StandingsResponse.Response> standings = response.body().response;
                    displayStandings(standings);
                } else {
                    // Handle error case
                }
            }

            @Override
            public void onFailure(Call<StandingsResponse> call, Throwable t) {
                // Handle failure case
            }
        });

        return view;
    }

    private void displayStandings(List<StandingsResponse.Response> standings) {
        // Create layout for headers
        LinearLayout headersLayout = new LinearLayout(getContext());
        headersLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        headersLayout.setLayoutParams(params);

        // Create TextViews for header information
        LinearLayout.LayoutParams rankParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        );
        rankParams.setMargins(16, 8, 0, 0);

        // Create TextView for team name header
        TextView teamNameHeaderTextView = new TextView(getContext());
        teamNameHeaderTextView.setLayoutParams(rankParams);
        teamNameHeaderTextView.setText("Team Name");
        teamNameHeaderTextView.setTypeface(null, Typeface.BOLD);
        teamNameHeaderTextView.setTextSize(16);
        headersLayout.addView(teamNameHeaderTextView);

        // Create TextViews for other headers
        LinearLayout.LayoutParams otherHeaderParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.15f
        );

        // Points header
        TextView pointsHeaderTextView = new TextView(getContext());
        pointsHeaderTextView.setLayoutParams(otherHeaderParams);
        pointsHeaderTextView.setText("  P");
        pointsHeaderTextView.setTextSize(16);
        pointsHeaderTextView.setTypeface(null, Typeface.BOLD);
        headersLayout.addView(pointsHeaderTextView);

// Played header
        TextView playedHeaderTextView = new TextView(getContext());
        playedHeaderTextView.setLayoutParams(otherHeaderParams);
        playedHeaderTextView.setText("PM");
        playedHeaderTextView.setTextSize(16);
        playedHeaderTextView.setTypeface(null, Typeface.BOLD);
        headersLayout.addView(playedHeaderTextView);

// Wins header
        TextView winsHeaderTextView = new TextView(getContext());
        winsHeaderTextView.setLayoutParams(otherHeaderParams);
        winsHeaderTextView.setText(" W");
        winsHeaderTextView.setTextSize(16);
        winsHeaderTextView.setTypeface(null, Typeface.BOLD);
        headersLayout.addView(winsHeaderTextView);

// Draws header
        TextView drawsHeaderTextView = new TextView(getContext());
        drawsHeaderTextView.setLayoutParams(otherHeaderParams);
        drawsHeaderTextView.setText(" D");
        drawsHeaderTextView.setTextSize(16);
        drawsHeaderTextView.setTypeface(null, Typeface.BOLD);
        headersLayout.addView(drawsHeaderTextView);

// Losses header
        TextView lossesHeaderTextView = new TextView(getContext());
        lossesHeaderTextView.setLayoutParams(otherHeaderParams);
        lossesHeaderTextView.setText(" L");
        lossesHeaderTextView.setTextSize(16);
        lossesHeaderTextView.setTypeface(null, Typeface.BOLD);
        headersLayout.addView(lossesHeaderTextView);

// Goals For header
        TextView goalsForHeaderTextView = new TextView(getContext());
        goalsForHeaderTextView.setLayoutParams(otherHeaderParams);
        goalsForHeaderTextView.setText("GF");
        goalsForHeaderTextView.setTextSize(16);
        goalsForHeaderTextView.setTypeface(null, Typeface.BOLD);
        headersLayout.addView(goalsForHeaderTextView);

// Goals Against header
        TextView goalsAgainstHeaderTextView = new TextView(getContext());
        goalsAgainstHeaderTextView.setLayoutParams(otherHeaderParams);
        goalsAgainstHeaderTextView.setText("GA");
        goalsAgainstHeaderTextView.setTextSize(16);
        goalsAgainstHeaderTextView.setTypeface(null, Typeface.BOLD);
        headersLayout.addView(goalsAgainstHeaderTextView);

// Goal Difference header
        TextView goalDifferenceHeaderTextView = new TextView(getContext());
        goalDifferenceHeaderTextView.setLayoutParams(otherHeaderParams);
        goalDifferenceHeaderTextView.setText("Aw");
        goalDifferenceHeaderTextView.setTextSize(16);
        goalDifferenceHeaderTextView.setTypeface(null, Typeface.BOLD);
        headersLayout.addView(goalDifferenceHeaderTextView);

        // Add the headers layout to standingsContainer
        standingsContainer.addView(headersLayout);

        // Loop through standings data
        for (StandingsResponse.Response response : standings) {
            List<StandingsResponse.Response.Standing> leagueStandings = response.league.standings.get(0);

            // Loop through each standing
            for (StandingsResponse.Response.Standing standing : leagueStandings) {
                // Create a new layout for each team
                LinearLayout teamLayout = new LinearLayout(getContext());
                teamLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams teamLayoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                teamLayoutParams.setMargins(8, 8, 8, 8); // Add margin between rows
                teamLayout.setLayoutParams(teamLayoutParams);
                teamLayout.setPadding(8, 8, 8, 8); // Add padding inside each row
                teamLayout.setBackgroundResource(R.drawable.row_border); // Set background with border

                // Add ImageView for team logo
                ImageView teamLogoImageView = new ImageView(getContext());
                LinearLayout.LayoutParams logoParams = new LinearLayout.LayoutParams(
                        80,
                        80
                );  // Increased logo size
                teamLogoImageView.setLayoutParams(logoParams);
                Picasso.get().load(standing.team.logo).into(teamLogoImageView);
                teamLayout.addView(teamLogoImageView);

                // Add TextView for team information
                TextView teamNameTextView = new TextView(getContext());
                teamNameTextView.setLayoutParams(rankParams);

                teamNameTextView.setText(standing.rank + ". " + standing.team.name);
                teamLayout.addView(teamNameTextView);

                // Add TextViews for other standings data
                TextView pointsTextView = new TextView(getContext());
                pointsTextView.setLayoutParams(otherHeaderParams);

                pointsTextView.setText(String.valueOf(standing.points));
                teamLayout.addView(pointsTextView);

                TextView playedTextView = new TextView(getContext());
                playedTextView.setLayoutParams(otherHeaderParams);

                playedTextView.setText(String.valueOf(standing.all.played));
                teamLayout.addView(playedTextView);

                TextView winsTextView = new TextView(getContext());
                winsTextView.setLayoutParams(otherHeaderParams);

                winsTextView.setText(String.valueOf(standing.all.win));
                teamLayout.addView(winsTextView);

                TextView drawsTextView = new TextView(getContext());
                drawsTextView.setLayoutParams(otherHeaderParams);

                drawsTextView.setText(String.valueOf(standing.all.draw));
                teamLayout.addView(drawsTextView);

                TextView lossesTextView = new TextView(getContext());
                lossesTextView.setLayoutParams(otherHeaderParams);

                lossesTextView.setText(String.valueOf(standing.all.lose));
                teamLayout.addView(lossesTextView);

                TextView goalsForTextView = new TextView(getContext());
                goalsForTextView.setLayoutParams(otherHeaderParams);

                goalsForTextView.setText(String.valueOf(standing.all.goals.goalsFor));
                teamLayout.addView(goalsForTextView);

                TextView goalsAgainstTextView = new TextView(getContext());
                goalsAgainstTextView.setLayoutParams(otherHeaderParams);

                goalsAgainstTextView.setText(String.valueOf(standing.all.goals.goalsAgainst));
                teamLayout.addView(goalsAgainstTextView);

                TextView goalDifferenceTextView = new TextView(getContext());
                goalDifferenceTextView.setLayoutParams(otherHeaderParams);

                goalDifferenceTextView.setText(String.valueOf(standing.getGoalDifference()));
                teamLayout.addView(goalDifferenceTextView);

                // Add the team layout to standingsContainer
                standingsContainer.addView(teamLayout);
            }
        }
    }
}
