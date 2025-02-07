package com.example.android.roomwordssample;

/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */
// https://developer.android.com/guide/topics/manifest/activity-element#exported
@RunWith(AndroidJUnit4.class)
public class WordDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private WordDao wordDao;
    private WordRoomDatabase wordRoomDatabase;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        wordRoomDatabase = Room.inMemoryDatabaseBuilder(context, WordRoomDatabase.class)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build();
        wordDao = wordRoomDatabase.wordDao();
    }

    @After
    public void closeDb() {
        wordRoomDatabase.close();
    }

    @Test
    public void insertAndGetWord() throws Exception {
        Word word = new Word("word");
        wordDao.insert(word);
        List<Word> allWords = LiveDataTestUtil.getValue(wordDao.getAlphabetizedWords());
        assertEquals(allWords.get(0).getWord(), word.getWord());
    }

    @Test
    public void getAllWords() throws Exception {
        Word word = new Word("aaa");
        wordDao.insert(word);
        Word word2 = new Word("bbb");
        wordDao.insert(word2);
        List<Word> allWords = LiveDataTestUtil.getValue(wordDao.getAlphabetizedWords());
        assertEquals(allWords.get(0).getWord(), word.getWord());
        assertEquals(allWords.get(1).getWord(), word2.getWord());
    }

    @Test
    public void deleteAll() throws Exception {
        Word word = new Word("word");
        wordDao.insert(word);
        Word word2 = new Word("word2");
        wordDao.insert(word2);
        wordDao.deleteAll();
        List<Word> allWords = LiveDataTestUtil.getValue(wordDao.getAlphabetizedWords());
        assertTrue(allWords.isEmpty());
    }
}
