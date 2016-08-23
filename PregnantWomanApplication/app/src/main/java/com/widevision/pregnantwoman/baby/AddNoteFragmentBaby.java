package com.widevision.pregnantwoman.baby;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.widevision.pregnantwoman.MainFragmentActivityBaby;
import com.widevision.pregnantwoman.R;
import com.widevision.pregnantwoman.model.HideKeyFragment;
import com.widevision.pregnantwoman.util.Constant;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by newtrainee on 3/8/15.
 */
public class AddNoteFragmentBaby extends HideKeyFragment implements Validator.ValidationListener {

    @NotEmpty(message = "Enter your note.")
    @Bind(R.id.noteEdt)
    EditText mNoteEdt;
    @Bind(R.id.saveBt)
    ImageView mSaveBtn;
    @Bind(R.id.cancelBt)
    ImageView cancelBt;
    @NotEmpty(message = "Enter title.")
    @Bind(R.id.titleEdt)
    EditText mTitleEdt;
    private Validator validator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_note_activity_baby, container, false);
        ButterKnife.bind(this, view);
        setupUI(view);

        validator = new Validator(this);
        validator.setValidationListener(this);

        mNoteEdt.setText(MainFragmentActivityBaby.noteDescription);
        mTitleEdt.setText(MainFragmentActivityBaby.noteTitle);
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });
        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainFragmentActivityBaby.fragmentManager.popBackStack();

            }
        });

        return view;
    }

    @Override
    public void onValidationSucceeded() {
        MainFragmentActivityBaby.noteTitle = mNoteEdt.getText().toString().trim();
        MainFragmentActivityBaby.noteDescription = mTitleEdt.getText().toString().trim();
        MainFragmentActivityBaby.fragmentManager.popBackStack();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        Constant.setAlert(getActivity(), errors.get(0).getCollatedErrorMessage(getActivity()));
    }


}
