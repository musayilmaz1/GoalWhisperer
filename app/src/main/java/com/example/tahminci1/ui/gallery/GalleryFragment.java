package com.example.tahminci1.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.tahminci1.ApiClient;
import com.example.tahminci1.ApiService;
import com.example.tahminci1.R;
import com.example.tahminci1.StandingsResponse;
import com.example.tahminci1.databinding.FragmentGalleryBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private ApiService apiService;
    private Spinner team1Spinner, team2Spinner;
    private List<StandingsResponse.Response.Standing> standings;
    private String homeTeam, awayTeam;
    private Map<String, Map<String, Map<String, Object>>> matchesData;
    private TextView homeSonucTv, awaySonucTv;
    private Button tahminBtn;
    private ImageView im_home,im_away;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery,container,false);
        matchesData = readJsonFromRaw();

        // Spinner'ları ve TextView'leri başlat
        team1Spinner = root.findViewById(R.id.team1_spinner);
        team2Spinner =root.findViewById(R.id.team2_spinner);
        homeSonucTv = root.findViewById(R.id.home_sonuc_tv);
        awaySonucTv = root.findViewById(R.id.away_sonuc_tv);
        tahminBtn = root.findViewById(R.id.tahmin_btn);


        // ApiService'i başlat
        apiService = ApiClient.getClient().create(ApiService.class);

        // API çağrısı yaparak standings (puan durumu) verilerini al
        Call<StandingsResponse> call = apiService.getStandings();
        call.enqueue(new Callback<StandingsResponse>() {
            @Override
            public void onResponse(Call<StandingsResponse> call, Response<StandingsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    standings = response.body().response.get(0).league.standings.get(0);
                    setupSpinners();
                } else {
                    Log.e("MainActivity2", "API'den veri alınamadı");
                }
            }

            @Override
            public void onFailure(Call<StandingsResponse> call, Throwable t) {
                Log.e("MainActivity2", "API çağrısı başarısız", t);
            }
        });

        // Tahmin butonuna tıklama dinleyicisi ekle
        tahminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showScores();
            }
        });
        return root;
    }
    private Map<String, Map<String, Map<String, Object>>> readJsonFromRaw() {
        InputStream inputStream = getResources().openRawResource(R.raw.data); // JSON dosya adınızı yazın (data.json)
        InputStreamReader reader = new InputStreamReader(inputStream);
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Map<String, Map<String, Object>>>>() {}.getType();
        return gson.fromJson(reader, type);
    }

    private void setupSpinners() {
        if (standings != null && standings.size() > 0) {
            List<String> teamNames = new ArrayList<>();
            for (StandingsResponse.Response.Standing standing : standings) {
                teamNames.add(standing.team.name);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, teamNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


            team1Spinner.setAdapter(adapter);
            team2Spinner.setAdapter(adapter);

            team1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    homeTeam = (String) parent.getItemAtPosition(position);
                    Log.d("MainActivity2", "Seçilen ev sahibi takım: " + homeTeam);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            team2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    awayTeam = (String) parent.getItemAtPosition(position);
                    Log.d("MainActivity2", "Seçilen deplasman takımı: " + awayTeam);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } else {
            Toast.makeText(getActivity(), "Puan durumu verisi mevcut değil", Toast.LENGTH_SHORT).show();
        }
    }

    private void showScores() {
        if (homeTeam != null && awayTeam != null) {
            if (matchesData.containsKey(homeTeam) && matchesData.get(homeTeam).containsKey(awayTeam)) {
                Map<String, Object> matchData = matchesData.get(homeTeam).get(awayTeam);
                int homeGoals = ((Double) matchData.get("Ev Sahibi Gol Sayisi")).intValue();
                int awayGoals = ((Double) matchData.get("Misafir Gol Sayisi")).intValue();
                homeSonucTv.setText(homeTeam + " \nGol Sayısı:\n       " + homeGoals);
                awaySonucTv.setText(awayTeam + " \nGol Sayısı:\n       " + awayGoals);
            } else {
                homeSonucTv.setText(homeTeam + " Gol Sayısı: Verisi Yok");
                awaySonucTv.setText(awayTeam + " Gol Sayısı: Verisi Yok");
            }
        }
    }
    private void setTeamLogos() {
        if (homeTeam != null && awayTeam != null) {
            // Ev sahibi takımın logosunu ImageView'a yerleştir
            int homeTeamLogoId = getResources().getIdentifier(homeTeam.toLowerCase(), "drawable", getActivity().getPackageName());
            Picasso.get().load(homeTeamLogoId).into(im_home);

            // Deplasman takımının logosunu ImageView'a yerleştir
            int awayTeamLogoId = getResources().getIdentifier(awayTeam.toLowerCase(), "drawable", getActivity().getPackageName());
            Picasso.get().load(awayTeamLogoId).into(im_away);
        }
    }





}