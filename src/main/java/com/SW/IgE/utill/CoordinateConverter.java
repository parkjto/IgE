package com.SW.IgE.utill;

import org.locationtech.proj4j.*;

import java.util.HashMap;
import java.util.Map;

public class CoordinateConverter {

    private static final CRSFactory CRS_FACTORY = new CRSFactory();
    private static final CoordinateReferenceSystem UTMK = CRS_FACTORY.createFromName("EPSG:5179");
    private static final CoordinateReferenceSystem WGS84 = CRS_FACTORY.createFromName("EPSG:4326");
    private static final CoordinateTransformFactory TRANSFORM_FACTORY = new CoordinateTransformFactory();
    private static final CoordinateTransform TRANSFORM = TRANSFORM_FACTORY.createTransform(UTMK, WGS84);

    /**
     * UTMK 좌표(EPSG:5179)를 WGS84 좌표(EPSG:4326)로 변환합니다.
     *
     * @param x UTMK 좌표의 X 값 (longitude)
     * @param y UTMK 좌표의 Y 값 (latitude)
     * @return 변환된 WGS84 좌표 (latitude, longitude)
     */
    public static Map<String, Double> convertToWGS84(double x, double y) {
        ProjCoordinate utmkCoord = new ProjCoordinate(x, y);
        ProjCoordinate wgs84Coord = new ProjCoordinate();

        TRANSFORM.transform(utmkCoord, wgs84Coord);

        Map<String, Double> coords = new HashMap<>();
        coords.put("latitude", wgs84Coord.y);
        coords.put("longitude", wgs84Coord.x);

        return coords;
    }
}
