package com.bobo.normalman.bobobubble.view.bucket;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.bobo.normalman.bobobubble.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiaobozhang on 8/27/17.
 */

public class NewBucketDialogFragment extends DialogFragment {
    @BindView(R.id.new_bucket_description)
    EditText description;
    @BindView(R.id.new_bucket_name)
    EditText name;
    public static final String TAG = "NewBucketDialogFragment";

    public static NewBucketDialogFragment newInstance() {
        
        Bundle args = new Bundle();
        
        NewBucketDialogFragment fragment = new NewBucketDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_new_bucket_dialog, null);
        ButterKnife.bind(this, view);
        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();

                        intent.putExtra(BucketListFragment.KEY_BUCKET_NAME,
                                name.getText().toString());
                        intent.putExtra(BucketListFragment.KEY_BUCKET_DESCRIPTION,
                                description.getText().toString());
                        getTargetFragment().onActivityResult(BucketListFragment.REQ_NEW_BUCKET,
                                Activity.RESULT_OK, intent);
                        dismiss();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();

    }
}
