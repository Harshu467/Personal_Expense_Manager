package com.app.expenseLite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.expenseLite.MainActivity;
import com.app.expenseLite.R;
import com.app.expenseLite.model.OverviewBalance;
import com.app.expenseLite.model.Ledger;
import com.app.expenseLite.utility.StringUtility;

import java.util.ArrayList;

class OverviewBalanceViewHolder extends RecyclerView.ViewHolder {

    public TextView balance, category;

    OverviewBalanceViewHolder(@NonNull View itemView) {
        super(itemView);
        balance = itemView.findViewById(R.id.balanceView);
        category = itemView.findViewById(R.id.categoryView);
    }
}

public class OverviewBalanceAdapter extends RecyclerView.Adapter<OverviewBalanceViewHolder>  {

    private ArrayList<OverviewBalance> overviewBalanceList;
    private Context ctx;

    private String currencyFormat, currencySymbol, currencySymbolPosition;

    public OverviewBalanceAdapter(MainActivity ctx, ArrayList<OverviewBalance> overviewBalanceList) {
        this.overviewBalanceList = overviewBalanceList;
        this.ctx = ctx;
        updatePreference();
    }

    @NonNull
    @Override
    public OverviewBalanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.overview_balance_card, parent, false);
        return new OverviewBalanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OverviewBalanceViewHolder holder, int position) {
        final OverviewBalance b = overviewBalanceList.get(position);

        // BALANCE
        boolean isDebitBalance = (b.getType() == Ledger.Type.EXPENDITURE || b.getType() == Ledger.Type.ASSET);
        int balance = b.getBalance();
        if(balance > 0) { // debit balance
            // if revenue, liability or equity
            if (!isDebitBalance) holder.balance.setTextColor(ctx.getResources().getColor(R.color.red));
        }
        else if(balance < 0) { // credit balance
            balance *= -1;
            // if expenditure or asset
            if (isDebitBalance) holder.balance.setTextColor(ctx.getResources().getColor(R.color.red));
        }
        holder.balance.setText(
                StringUtility.amountFormat(balance, currencyFormat, currencySymbol, currencySymbolPosition));

        holder.category.setText(
                ctx.getResources().getStringArray(R.array.ledger_types)[b.getType()]);
    }

    @Override
    public int getItemCount() {
        return overviewBalanceList.size();
    }

    public void updatePreference() {
        this.currencyFormat = PreferenceManager.getDefaultSharedPreferences(ctx).getString(
                ctx.getResources().getString(R.string.currency_format_pref_key),
                ctx.getResources().getString(R.string.currency_format_default_value));

        this.currencySymbol = PreferenceManager.getDefaultSharedPreferences(ctx).getString(
                ctx.getResources().getString(R.string.currency_symbol_pref_key),
                ctx.getResources().getString(R.string.currency_symbol_default_value));

        this.currencySymbolPosition = PreferenceManager.getDefaultSharedPreferences(ctx).getString(
                ctx.getResources().getString(R.string.currency_symbol_position_pref_key),
                ctx.getResources().getString(R.string.currency_symbol_position_default_value));
    }
}
