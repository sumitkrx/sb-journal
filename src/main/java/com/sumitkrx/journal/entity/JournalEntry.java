package com.sumitkrx.journal.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "journal_entries")
@Getter
@Setter
public class JournalEntry {
    @Id
    private ObjectId id;
    private LocalDateTime date;
    @NonNull
    private String title;
    private String content;
}
