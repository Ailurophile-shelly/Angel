package com.example.angel;
/*
4.29
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.db.MenstruationDao;
import com.example.view.HistogramView;

import java.util.List;

public class AnalyzeFrament extends Fragment {
    private View view;
    private HistogramView hv;
    private MenstruationDao mtDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_analyze, container, false);
        }

        hv = (HistogramView) view.findViewById(R.id.hv);

        mtDao = new MenstruationDao(getActivity());
        List<MenstruationModel> mtmList = mtDao.getMTModelList(0, 0);
        hv.setHistogramList(mtmList);
        return view;
    }
}
