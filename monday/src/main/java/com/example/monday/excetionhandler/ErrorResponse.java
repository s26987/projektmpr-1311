package com.example.monday.excetionhandler;

import java.time.Instant;
import java.util.UUID;

public record ErrorResponse(UUID id, Instant timestamp, String message) {
}
