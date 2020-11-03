package com.example.bookmark.models;

import java.util.Map;

interface FirestoreSerializable {
    Map<String, Object> toFirestoreDocument();
}
