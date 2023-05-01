package com.app.expenseLite.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.expenseLite.LedgerAccountActivity;
import com.app.expenseLite.R;
import com.app.expenseLite.adapter.JournalAdapter;
import com.app.expenseLite.database.AppDatabase;
import com.app.expenseLite.database.EntryDao;
import com.app.expenseLite.database.LedgerDao;
import com.app.expenseLite.model.Journal;
import com.app.expenseLite.model.Ledger;

import java.util.ArrayList;
import java.util.Objects;

public class LedgerEntriesFragment extends Fragment {

    private TextView emptyView;

    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private JournalAdapter adapter;

    private ArrayList<Journal> journalList = new ArrayList<>();
    private Ledger ledger;
    private EntryDao entryDao;
    private LedgerDao ledgerDao;

    public LedgerEntriesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        entryDao = AppDatabase.getAppDatabase(getContext()).entryDao();
        ledgerDao = AppDatabase.getAppDatabase(getContext()).ledgerDao();

        LedgerAccountActivity l = (LedgerAccountActivity) getActivity();
        journalList = (ArrayList<Journal>) entryDao.getJournalsByLedger(l.ledgerId, l.fromDateTimestamp, l.toDateTimestamp);
        ledger = ledgerDao.getLedgerById(l.ledgerId);
        return inflater.inflate(R.layout.fragment_ledger_entries, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emptyView = (TextView) view.findViewById(R.id.emptyView);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        adapter = new JournalAdapter(getContext(), journalList);
        adapter.setLedger(ledger);
        manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        if (!journalList.isEmpty()) emptyView.setVisibility(View.INVISIBLE);
    }
}
