package com.example.client;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KrakenUpdates {
    @JsonProperty("as")
    private List<List<String>> asks;

    @JsonProperty("bs")
    private List<List<String>> bids;

    @JsonProperty("a")
    private List<List<String>> askUpdates;

    @JsonProperty("b")
    private List<List<String>> bidUpdates;

}
