package com.example.tahminci1;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StandingsResponse {
    @SerializedName("response")
    public List<Response> response;

    public class Response {
        @SerializedName("league")
        public League league;

        public class League {
            @SerializedName("standings")
            public List<List<Standing>> standings;
        }

        public class Standing {
            @SerializedName("rank")
            public int rank;

            @SerializedName("team")
            public Team team;

            @SerializedName("points")
            public int points;

            @SerializedName("all")
            public All all;

            public class All {
                @SerializedName("played")
                public int played;

                @SerializedName("win")
                public int win;

                @SerializedName("draw")
                public int draw;

                @SerializedName("lose")
                public int lose;

                @SerializedName("goals")
                public Goals goals;
            }

            public class Goals {
                @SerializedName("for")
                public int goalsFor;

                @SerializedName("against")
                public int goalsAgainst;
            }

            public int getGoalDifference() {
                return all.goals.goalsFor - all.goals.goalsAgainst;
            }
        }

        public class Team {
            @SerializedName("name")
            public String name;

            @SerializedName("logo")
            public String logo;
        }
    }
}
