package com.parking.Parking.Interfaces;

import java.io.IOException;
import java.util.Map;

public interface IServiceMapbox {
    Map<String, String> getInfo(String x1, String y1, String x2, String y2) throws IOException;

}
