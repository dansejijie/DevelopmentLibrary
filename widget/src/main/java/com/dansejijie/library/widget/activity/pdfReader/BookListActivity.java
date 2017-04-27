package com.dansejijie.library.widget.activity.pdfReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dansejijie.library.widget.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tygzx on 17/4/27.
 */

public class BookListActivity extends Activity{


    RecyclerView recyclerView;
    ChapterAdapter chapterAdapter;

    List<Chapter>chapterList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paf_reader);
        
        initDatas();
        recyclerView= (RecyclerView) findViewById(R.id.pdf_reader_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chapterAdapter=new ChapterAdapter(this, chapterList, new ChapterAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent();
                intent.putExtra("title",chapterList.get(position).title);
                intent.putExtra("url",chapterList.get(position).url);
                startActivity(intent);

            }
        });

        recyclerView.setAdapter(chapterAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initDatas() {

        chapterList= new ArrayList<>();
        Chapter chapter1=new Chapter();
        chapter1.title="Front Matter (pages i–x)";
        chapter1.url="http://onlinelibrary.wiley.com/doi/10.1002/9781118295472.fmatter/pdf";
        Chapter chapter2=new Chapter();
        chapter1.title="Appendix (pages 129–130)";
        chapter1.url="http://onlinelibrary.wiley.com/doi/10.1002/9781118295472.app1/pdf";
        Chapter chapter3=new Chapter();
        chapter1.title="References (pages 131–134)";
        chapter1.url="http://onlinelibrary.wiley.com/doi/10.1002/9781118295472.refs/pdf";

        chapterList.add(chapter1);
        chapterList.add(chapter2);
        chapterList.add(chapter3);
    }
}
