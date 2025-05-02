package com.sumitkrx.journal.controller;

import com.sumitkrx.journal.entity.JournalEntry;
import com.sumitkrx.journal.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAllEntry() {
        return new ResponseEntity<>(new ArrayList<>(journalEntryService.getAll()), HttpStatus.OK);
    }

    @GetMapping("/id/{reqId}")
    public ResponseEntity<JournalEntry> getEntryById(@PathVariable String reqId) {
        Optional<JournalEntry> journalEntry = journalEntryService.getEntryById(new ObjectId(reqId));
        return journalEntry.map(entry -> new ResponseEntity<>(entry, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry entry) {
        try {
            entry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(entry);
            return new ResponseEntity<>(entry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/id/{reqId}")
    public ResponseEntity<JournalEntry> updateEntry(@PathVariable ObjectId reqId, @RequestBody JournalEntry newEntry) {
        JournalEntry currentEntry = journalEntryService.getEntryById(reqId).orElse(null);
        if (currentEntry != null) {
            currentEntry.setTitle(!newEntry.getTitle().isEmpty() ? newEntry.getTitle() : currentEntry.getTitle());
            currentEntry.setContent(!newEntry.getContent().isEmpty() ? newEntry.getContent() : currentEntry.getContent());
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        journalEntryService.saveEntry(currentEntry);
        return new ResponseEntity<>(currentEntry, HttpStatus.CREATED);
    }

    @DeleteMapping("/id/{reqId}")
    public ResponseEntity<?> deleteEntry(@PathVariable ObjectId reqId) {
        journalEntryService.deleteById(reqId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
