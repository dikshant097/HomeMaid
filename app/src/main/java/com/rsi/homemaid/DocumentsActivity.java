package com.rsi.homemaid;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import bean.MaidList;

/**
 * Created by deepak.sharma on 9/7/2017.
 */

public class DocumentsActivity extends BaseActivity {

    private Toolbar toolbar;
    private ImageView iv_documents;
    private LinearLayout ll_document_main;
    private MaidList maid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);

        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        empty_view = (TextView) findViewById(R.id.empty_view);
        ll_document_main = (LinearLayout) findViewById(R.id.ll_document_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.black)));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_document_main.setBackgroundColor(getResources().getColor(R.color.black));

        iv_documents = (ImageView) findViewById(R.id.iv_documents);

        if (getIntent().getExtras().get("Maid") != null){
            maid = (MaidList) getIntent().getExtras().get("Maid");
            if (!maid.getMaidVerificationImage().equalsIgnoreCase("")){
                progress_bar.setVisibility(View.VISIBLE);
                Picasso.with(this)
                    .load(maid.getMaidVerificationImage())
                    .placeholder(R.drawable.placeholder_thumb)
                    .into(iv_documents, new Callback() {
                        @Override
                        public void onSuccess() {
                            progress_bar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                        }
                    });
            }else {
                empty_view.setVisibility(View.VISIBLE);
                ll_document_main.setBackgroundColor(getResources().getColor(R.color.white));
            }
        }
    }
}
