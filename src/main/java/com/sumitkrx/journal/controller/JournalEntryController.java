package com.sumitkrx.journal.controller;

import com.sumitkrx.journal.entity.JournalEntry;
import com.sumitkrx.journal.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping
    public List<JournalEntry> getAllEntry(){
        return new ArrayList<>(journalEntryService.getAll());
    }

    @GetMapping("/id/{reqId}")
    public JournalEntry getEntryById(@PathVariable ObjectId reqId){
        return journalEntryService.getEntryById(reqId).orElse(null);
    }

    @PostMapping
    public JournalEntry createEntry(@RequestBody JournalEntry entry){
        entry.setDate(LocalDateTime.now());
        journalEntryService.saveEntry(entry);
        return entry;
    }

    @PutMapping("/id/{reqId}")
    public JournalEntry updateEntry(@PathVariable ObjectId reqId,@RequestBody JournalEntry newEntry){
        JournalEntry currentEntry = journalEntryService.getEntryById(reqId).orElse(null);
        if(currentEntry != null){
            currentEntry.setTitle(!newEntry.getTitle().isEmpty() ? newEntry.getTitle() : currentEntry.getTitle());
            currentEntry.setContent(!newEntry.getContent().isEmpty() ? newEntry.getContent() : currentEntry.getContent());
        }
        journalEntryService.saveEntry(currentEntry);
        return currentEntry;
    }

    @DeleteMapping("/id/{reqId}")
    public boolean deleteEntry(@PathVariable ObjectId reqId){
        journalEntryService.deleteById(reqId);
        return true;
    }

}
