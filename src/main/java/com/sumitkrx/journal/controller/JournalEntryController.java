package com.sumitkrx.journal.controller;

import com.sumitkrx.journal.entity.JournalEntry;
import com.sumitkrx.journal.entity.User;
import com.sumitkrx.journal.service.JournalEntryService;
import com.sumitkrx.journal.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;

    @GetMapping("/{userName}")
    public ResponseEntity<List<JournalEntry>> getAllJournalEntryOfUser(@PathVariable String userName) {
        User user = userService.findByUserName(userName);
        List<JournalEntry> all = new ArrayList<>(user.getEntries());
        if (!all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/id/{reqId}")
    public ResponseEntity<JournalEntry> getEntryById(@PathVariable String reqId) {
        Optional<JournalEntry> journalEntry = journalEntryService.getEntryById(new ObjectId(reqId));
        return journalEntry.map(entry -> new ResponseEntity<>(entry, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{userName}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry entry, @PathVariable String userName) {
        try {
            journalEntryService.saveEntry(entry, userName);
            return new ResponseEntity<>(entry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/id/{userName}/{reqId}")
    public ResponseEntity<JournalEntry> updateEntryById(@PathVariable ObjectId reqId, @RequestBody JournalEntry newEntry, @PathVariable String userName) {
        JournalEntry currentEntry = journalEntryService.getEntryById(reqId).orElse(null);
        if (currentEntry != null) {
            currentEntry.setTitle(!newEntry.getTitle().isBlank() ? newEntry.getTitle() : currentEntry.getTitle());
            currentEntry.setContent(!newEntry.getContent().isBlank() ? newEntry.getContent() : currentEntry.getContent());
            journalEntryService.saveEntry(currentEntry);
            return new ResponseEntity<>(currentEntry, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{userName}/{reqId}")
    public ResponseEntity<?> deleteEntry(@PathVariable ObjectId reqId, @PathVariable String userName) {
        journalEntryService.deleteById(reqId, userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
