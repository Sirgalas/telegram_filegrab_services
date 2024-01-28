package ru.sergalas.utils;

import lombok.RequiredArgsConstructor;
import org.hashids.Hashids;

public class CryptoTool {
    private final Hashids hashids;

    public CryptoTool(String salt) {
        int minHAshLength = 10;
        this.hashids = new Hashids(salt, minHAshLength);
    }

    public String hasOf(Long docId) {
        return hashids.encode(docId);
    }

    public Long idOf(String value) {
        long[] res = hashids.decode(value);
        if(res != null && res.length > 0) {
            return res[0];
        }
        return null;
    }
}
