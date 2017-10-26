package com.fsecure.homework.searchapp.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.fsecure.homework.searchapp.R;
import com.fsecure.homework.searchapp.adapter.SearchResultListAdapter;
import com.fsecure.homework.searchapp.data.ApplicationData;

import java.util.ArrayList;
import java.util.List;

public class SearchResultDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        List<ApplicationInfo> appInfos = getArguments().getParcelableArrayList(null);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_result, null);

        if (appInfos != null && appInfos.size() > 0) {
            RecyclerView recyclerView = rootView.findViewById(R.id.list);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(createAdapter(appInfos));
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                    LinearLayoutManager.VERTICAL));
        } else {
            View listView = rootView.findViewById(R.id.list);
            if (listView != null)
                listView.setVisibility(View.INVISIBLE);

            View emptyView = rootView.findViewById(R.id.empty);
            if (emptyView != null)
                emptyView.setVisibility(View.VISIBLE);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(rootView).setCancelable(true).setNegativeButton(R.string.cancel_btn_title,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

    private SearchResultListAdapter createAdapter(List<ApplicationInfo> appInfos) {
        final PackageManager pm = getActivity().getPackageManager();
        List<ApplicationData> appDataList = new ArrayList<>(appInfos.size());

        for (ApplicationInfo ai : appInfos) {
            String appLbl = pm.getApplicationLabel(ai).toString();
            String appPkg = ai.packageName;
            Drawable appIco = pm.getApplicationIcon(ai);

            appDataList.add(new ApplicationData(appLbl, appPkg, appIco));
        }

        return new SearchResultListAdapter(appDataList) {
            @Override
            protected void onItemClick(View view, ApplicationData data, int position) {
                Intent intent = pm.getLaunchIntentForPackage(data.getPackage());
                if (intent != null)
                    startActivity(intent);
            }
        };
    }

}
